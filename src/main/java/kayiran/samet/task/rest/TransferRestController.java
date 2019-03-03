package kayiran.samet.task.rest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kayiran.samet.task.dto.TransferDto;
import kayiran.samet.task.dto.UserDto;
import kayiran.samet.task.entity.Balance;
import kayiran.samet.task.entity.Transfer;
import kayiran.samet.task.entity.User;
import kayiran.samet.task.exception.FieldValidationException;
import kayiran.samet.task.exception.IllegalTcException;
import kayiran.samet.task.exception.InsufficientBalanceException;
import kayiran.samet.task.exception.MaxValuePerTransferException;
import kayiran.samet.task.exception.ResourceNotFoundException;
import kayiran.samet.task.repository.BalanceRepository;
import kayiran.samet.task.repository.TransferRepository;
import kayiran.samet.task.service.UserService;

@RestController
@RequestMapping("/api")
public class TransferRestController {

	@Autowired
	UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	BalanceRepository balanceRepository;

	@Autowired
	TransferRepository transferRepository;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@PostMapping("/users/{id}/transfers")
	public ResponseEntity<TransferDto> addTransfer(@Valid @RequestBody TransferDto transferDto,
			BindingResult theBindingResult, @PathVariable Long id, Authentication authentication) {

		UserDetails senderUserDetails = (UserDetails) authentication.getPrincipal();
		User senderUser = userService.findByTC(senderUserDetails.getUsername());

		if (senderUser.getID() != id) {
			throw new AccessDeniedException("You are not allowed to access user with id " + id);
		}

		if (theBindingResult.hasErrors()) {

			// create error exception and return it
			for (ObjectError er : theBindingResult.getAllErrors()) {

				throw new FieldValidationException(er.getDefaultMessage());

			}
		}

		User targetUser = userService.findByTC(transferDto.getTargetTC());

		// check target user existence
		if (targetUser == null) {
			throw new ResourceNotFoundException("Target TC number is not found.");
		}

		// 20k amount control
		if (transferDto.getAmount() > 20.0f || transferDto.getAmount() == 20.0f) {
			throw new MaxValuePerTransferException("Exceeded maximum value per Transfer.");
		}

		// Check target and sender TC's equality
		if (senderUser.getTC().equals(targetUser.getTC()))
			throw new IllegalTcException("You cannot send money yourself.");

		boolean balanceUpdated = false;

		for (Balance senderBalance : senderUser.getBalances()) {

			// Check balance and currency
			if (senderBalance.getCurrency().equals(transferDto.getCurrency())
					&& (senderBalance.getAmount() >= transferDto.getAmount())) {

				// export money
				senderBalance.setAmount(senderBalance.getAmount() - transferDto.getAmount());
				balanceRepository.save(senderBalance);

				// find target user

				for (Balance targetBalance : targetUser.getBalances()) {

					// Check target user has same currency balance. If has, sum balances.
					if (targetBalance.getCurrency().equals(transferDto.getCurrency())) {

						// import money
						targetBalance.setAmount(targetBalance.getAmount() + transferDto.getAmount());
						balanceRepository.save(targetBalance);

						balanceUpdated = true;

						break;
					}

				}

				// target user doesn't have same currency balance create new.
				if (balanceUpdated == false) {

					Balance newTargetBalance = new Balance();
					newTargetBalance.setAmount(transferDto.getAmount());
					newTargetBalance.setCurrency(transferDto.getCurrency());

					newTargetBalance.setUser(targetUser);
					targetUser.addBalance(newTargetBalance);

					userService.save(targetUser);
					balanceRepository.save(newTargetBalance);

					balanceUpdated = true;

				}

				break;
			}
		}

		if (balanceUpdated == false) {
			throw new InsufficientBalanceException("You do not have enough amount.");
		}

		Transfer transfer = new Transfer();
		transfer.setSenderName(senderUser.getName());
		transfer.setSenderTC(senderUser.getTC());
		transfer.setTargetName(targetUser.getName());
		transfer.setTargetTC(targetUser.getTC());
		transfer.setCurrency(transferDto.getCurrency());
		transfer.setDateTime(LocalDateTime.now());
		transfer.setAmount(transferDto.getAmount());

		transferRepository.save(transfer);

		transferDto = modelMapper.map(transfer, TransferDto.class);

		Link selfRel = ControllerLinkBuilder.linkTo(TransferRestController.class).slash("/users/" + id + "/transfers")
				.slash(transferDto.getID()).withSelfRel();
		transferDto.add(selfRel);

		return new ResponseEntity<>(transferDto, HttpStatus.CREATED);
	}

	@GetMapping("/users/{id}/transfers")
	public ResponseEntity<List<TransferDto>> getTransfers(@PathVariable Long id, Authentication authentication) {

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.findByTC(userDetails.getUsername());

		if (user.getID() != id) {
			throw new AccessDeniedException("You are not allowed to access user with id " + id);
		}

		List<Transfer> transfers = transferRepository.findByTC(user.getTC());
		List<TransferDto> transferDtos = new ArrayList<TransferDto>();
		
		for (Transfer transfer : transfers) {
			
			TransferDto transferDto = modelMapper.map(transfer, TransferDto.class);
			
			Link selfRel = ControllerLinkBuilder.linkTo(TransferRestController.class).slash("/users/" + id + "/transfers")
					.slash(transferDto.getID()).withSelfRel();
			transferDto.add(selfRel);

			transferDtos.add(transferDto);
		}
		
		
		return new ResponseEntity<>(transferDtos, HttpStatus.OK);
	}

	@GetMapping("/users/{id}/transfers/{transferId}")
	public ResponseEntity<TransferDto> getTransfer(@PathVariable Long id, @PathVariable Long transferId, Authentication authentication) {

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.findByTC(userDetails.getUsername());

		if (user.getID() != id) {
			throw new AccessDeniedException("You are not allowed to access user with id " + id);
		}

		Transfer transfer = transferRepository.findById(transferId)
				.orElseThrow(() -> new ResourceNotFoundException("Transfer with id = " + transferId + " not found."));

		TransferDto transferDto = modelMapper.map(transfer, TransferDto.class);
		
		Link selfRel = ControllerLinkBuilder.linkTo(TransferRestController.class).slash("/users/" + id + "/transfers")
				.slash(transferDto.getID()).withSelfRel();
		transferDto.add(selfRel);
		
		return new ResponseEntity<>(transferDto, HttpStatus.OK);

	}

	@DeleteMapping("/users/{id}/transfers/{transferId}")
	void deleteTransfer(@PathVariable Long id, @PathVariable Long transferId, Authentication authentication) {

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.findByTC(userDetails.getUsername());

		if (user.getID() != id) {
			throw new AccessDeniedException("You are not allowed to access user with id " + id);
		}

		Transfer transfer = transferRepository.findById(transferId)
				.orElseThrow(() -> new ResourceNotFoundException("Transfer with id = " + transferId + " not found."));

		transferRepository.delete(transfer);
	}

	@PutMapping("/users/{id}/transfers/{transferId}")
	ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto transferDto, @PathVariable Long id, @PathVariable Long transferId,  BindingResult theBindingResult) {

		if (theBindingResult.hasErrors()) {

			// create error exception and return it
			for (ObjectError er : theBindingResult.getAllErrors()) {

				throw new FieldValidationException(er.getDefaultMessage());

			}
		}
		
		Transfer transfer = transferRepository.findById(transferId)
				.orElseThrow(() -> new ResourceNotFoundException(" Transfer with id = " + id + " not found."));
					
		transferDto.setID(transfer.getID());

		Transfer updatedTransfer = modelMapper.map(transferDto, Transfer.class);
		
		transferRepository.save(updatedTransfer);
		
		Link selfRel = ControllerLinkBuilder.linkTo(UserRestController.class).slash("/users/" + id + "/transfers").slash(updatedTransfer.getID())
				.withSelfRel();
		
		transferDto.add(selfRel);
		
		return new ResponseEntity<>(transferDto, HttpStatus.OK);

	}

	

}