package kayiran.samet.task.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kayiran.samet.task.dto.CurrencyDto;
import kayiran.samet.task.repository.BalanceRepository;
import kayiran.samet.task.repository.CurrencyRepository;
import kayiran.samet.task.repository.ExchangeRepository;
import kayiran.samet.task.service.UserService;

@RestController
@RequestMapping("/api")
public class CurrencyRestController {

	@Autowired
	UserService userService;

	@Autowired
	BalanceRepository balanceRepository;

	@Autowired
	ExchangeRepository exchangeRepository;

	@Autowired
	CurrencyRepository currencyRepository;
	

	@GetMapping("/currencies")
	public ResponseEntity<CurrencyDto> getCurrencies(Authentication authentication) {

		CurrencyDto currencyDto = currencyRepository.getCurrencies();

		Link selfRel = ControllerLinkBuilder.linkTo(CurrencyRestController.class).slash("/currencies/").withSelfRel();
		currencyDto.add(selfRel);

		return new ResponseEntity<>(currencyDto, HttpStatus.OK);
	}

}