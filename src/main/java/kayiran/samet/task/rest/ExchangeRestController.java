package kayiran.samet.task.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kayiran.samet.task.dto.ExchangeDto;
import kayiran.samet.task.entity.Balance;
import kayiran.samet.task.entity.Currency;
import kayiran.samet.task.entity.Exchange;
import kayiran.samet.task.entity.User;
import kayiran.samet.task.exception.FieldValidationException;
import kayiran.samet.task.exception.InsufficientBalanceException;
import kayiran.samet.task.exception.LimitExceededMaxExchangePerDay;
import kayiran.samet.task.exception.ResourceNotFoundException;
import kayiran.samet.task.repository.BalanceRepository;
import kayiran.samet.task.repository.CurrencyRepository;
import kayiran.samet.task.repository.ExchangeRepository;
import kayiran.samet.task.service.UserService;

@RestController
@RequestMapping("/api")
public class ExchangeRestController {

	@Autowired
	UserService userService;

	@Autowired
	BalanceRepository balanceRepository;

	@Autowired
	ExchangeRepository exchangeRepository;
	
	@Autowired
	CurrencyRepository currencyRepository;
	

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/users/{id}/currencies")
	public ResponseEntity<ExchangeDto> addExchange(@Valid @RequestBody ExchangeDto exchangeDto,
			BindingResult theBindingResult, @PathVariable Long id, Authentication authentication) {

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.findByTC(userDetails.getUsername());

		if (user.getID() != id) {
			throw new AccessDeniedException("You are not allowed to access user with id " + id);
		}

		if (theBindingResult.hasErrors()) {

			// create error exception and return it
			for (ObjectError er : theBindingResult.getAllErrors()) {

				throw new FieldValidationException(er.getDefaultMessage());

			}
		}

		// 10 exchange control
		List<Exchange> exchanges = exchangeRepository.findByUserId(user.getID());

		LocalDate today = LocalDateTime.now().toLocalDate();

		int todayExchangeCount = 0;

		for (Exchange exchange : exchanges) {

			if (exchange.getDateTime().toLocalDate().equals(today)) {
				++todayExchangeCount;
			}
		}
		if (todayExchangeCount == 10) {

			throw new LimitExceededMaxExchangePerDay("Daily exchange limit exceeded.");
		}

		boolean completed = false;
		Exchange exchange = new Exchange();

		// Buying
		if (exchangeDto.getBuying()) {

			for (Balance balance : user.getBalances()) {

				// buying process
				if (balance.getCurrency().equals(Currency.TRY) && balance.getAmount() > 0) {

					try {
						String jsonCurrencies = Jsoup.connect("https://api.exchangeratesapi.io/latest?base=TRY")
								.ignoreContentType(true).execute().body();

						ObjectMapper mapper = new ObjectMapper();
						JsonNode actualObj = mapper.readTree(jsonCurrencies);
						JsonNode rates = actualObj.get("rates");

						float currencyPrice = Float
								.parseFloat(rates.get(exchangeDto.getCurrency().toString()).toString());

						exchange.setPrice(currencyPrice);

						// Convert TRY to new currency
						float newBalanceAmount = currencyPrice * balance.getAmount();
						boolean balanceUpdated = false;

						for (Balance newBalance : user.getBalances()) {

							// Check target user has same currency balance. If has, sum balances.
							if (newBalance.getCurrency().equals(exchangeDto.getCurrency())) {

								// update new currency
								newBalance.setAmount(newBalance.getAmount() + newBalanceAmount);
								balanceRepository.save(newBalance);

								exchange.setAmount(newBalanceAmount);

								// update TRY 0
								balance.setAmount(0);
								balanceRepository.save(balance);

								balanceUpdated = true;

								break;
							}

						}

						// user doesn't have same currency balance create new.
						if (balanceUpdated == false) {

							Balance newBalance = new Balance();
							newBalance.setAmount(newBalanceAmount);
							newBalance.setCurrency(exchangeDto.getCurrency());

							exchange.setAmount(newBalanceAmount);

							userService.save(user);
							balanceRepository.save(newBalance);

							newBalance.setUser(user);
							user.addBalance(newBalance);

							// update try 0
							balance.setAmount(0);
							balanceRepository.save(balance);

						}

					} catch (Exception e) {

						throw new InternalError("Internal server error.");
					}

					completed = true;
					break;
				}

			}

			if (!completed) {
				throw new InsufficientBalanceException("You do not have enough TRY to buy.");
			}

			// Selling
		} else {

			for (Balance balance : user.getBalances()) {

				// Selling process
				if (balance.getCurrency().equals(exchangeDto.getCurrency()) && balance.getAmount() > 0) {

					try {
						String jsonCurrencies = Jsoup.connect("https://api.exchangeratesapi.io/latest?base=TRY")
								.ignoreContentType(true).execute().body();

						ObjectMapper mapper = new ObjectMapper();
						JsonNode actualObj = mapper.readTree(jsonCurrencies);
						JsonNode rates = actualObj.get("rates");

						float currencyPrice = Float
								.parseFloat(rates.get(exchangeDto.getCurrency().toString()).toString());

						exchange.setPrice(currencyPrice);

						// Convert to TRY
						float newBalanceAmount = balance.getAmount() / currencyPrice;

						for (Balance trBalance : user.getBalances()) {

							if (trBalance.getCurrency().equals(Currency.TRY)) {

								exchange.setAmount(balance.getAmount());

								// update sold currency's balance
								balance.setAmount(0);
								balanceRepository.save(balance);

								// update TRY balance
								trBalance.setAmount(trBalance.getAmount() + newBalanceAmount);
								balanceRepository.save(trBalance);

								break;
							}
						}

					} catch (Exception e) {

						throw new InternalError("Internal server error.");
					}

					completed = true;
					break;
				}
			}
			if (!completed) {
				throw new InsufficientBalanceException(
						"You do not have enough " + exchangeDto.getCurrency() + " to sell.");
			}

		}

		exchange.setCurrency(exchangeDto.getCurrency());
		exchange.setBuying(exchangeDto.getBuying());
		exchange.setUser(user);
		exchange.setDateTime(LocalDateTime.now());
		exchangeRepository.save(exchange);

		exchangeDto = modelMapper.map(exchange, ExchangeDto.class);

		exchangeDto.setUserID(user.getID());

		Link selfRel = ControllerLinkBuilder.linkTo(TransferRestController.class).slash("/users/" + id + "/currencies")
				.slash(exchangeDto.getID()).withSelfRel();
		exchangeDto.add(selfRel);

		return new ResponseEntity<>(exchangeDto, HttpStatus.CREATED);

	}

	@GetMapping("/users/{id}/currencies")
	public ResponseEntity<List<ExchangeDto>> getExchanges(@PathVariable Long id, Authentication authentication) {

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.findByTC(userDetails.getUsername());

		if (user.getID() != id) {
			throw new AccessDeniedException("You are not allowed to access user with id " + id);
		}

		List<Exchange> exchanges = new ArrayList<Exchange>();
		exchanges = exchangeRepository.findByUserId(user.getID());

		List<ExchangeDto> exchangeDtos = new ArrayList<ExchangeDto>();

		for (Exchange exchange : exchanges) {

			ExchangeDto exchangeDto = new ExchangeDto();

			Link selfRel = ControllerLinkBuilder.linkTo(TransferRestController.class)
					.slash("/users/" + id + "/currencies").slash(exchangeDto.getID()).withSelfRel();
			exchangeDto.add(selfRel);

			exchangeDtos.add(modelMapper.map(exchange, ExchangeDto.class));

		}

		return new ResponseEntity<>(exchangeDtos, HttpStatus.OK);

	}

	@GetMapping("/users/{id}/currencies/{currencyID}")
	public ResponseEntity<ExchangeDto> getExchange(@PathVariable Long id, @PathVariable Long exchangeID,
			Authentication authentication) {

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.findByTC(userDetails.getUsername());

		if (user.getID() != id) {
			throw new AccessDeniedException("You are not allowed to access user with id " + id);
		}

		Exchange exchange = exchangeRepository.findById(exchangeID)
				.orElseThrow(() -> new ResourceNotFoundException("Exchange with id = " + exchangeID + " not found."));

		ExchangeDto exchangeDto = modelMapper.map(exchange, ExchangeDto.class);

		Link selfRel = ControllerLinkBuilder.linkTo(TransferRestController.class).slash("/users/" + id + "/currencies")
				.slash(exchangeDto.getID()).withSelfRel();
		exchangeDto.add(selfRel);
		

		return new ResponseEntity<>(exchangeDto, HttpStatus.OK);
	}
}