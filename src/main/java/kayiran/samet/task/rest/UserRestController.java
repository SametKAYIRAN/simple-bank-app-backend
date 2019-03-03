package kayiran.samet.task.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kayiran.samet.task.dto.UserDto;
import kayiran.samet.task.entity.User;
import kayiran.samet.task.exception.FieldValidationException;
import kayiran.samet.task.service.UserService;

@RestController
@RequestMapping("/api")
public class UserRestController {

	@Autowired
	UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/users")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto, BindingResult theBindingResult) {

		if (theBindingResult.hasErrors()) {
			
			for (ObjectError er : theBindingResult.getAllErrors()) {

				throw new FieldValidationException(er.getDefaultMessage());
			}
		}

		User existingUser = userService.findByTC(userDto.getTC());

		if (existingUser != null) {
			throw new FieldValidationException("TC number is in use");
		}

		User user = modelMapper.map(userDto, User.class);

		user = userService.save(userDto);

		userDto.setID(user.getID());

		Link selfRel = ControllerLinkBuilder.linkTo(UserRestController.class).slash("users").slash(userDto.getID())
				.withSelfRel();
		userDto.add(selfRel);

		return new ResponseEntity<>(userDto, HttpStatus.CREATED);
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> getUsers() {

		List<User> users = userService.getUsers();

		if (users.size() == 0)
			throw new UsernameNotFoundException("User not found.");

		List<UserDto> userDtos = new ArrayList<UserDto>();

		for (User user : users) {

			UserDto userDto = new UserDto();
			userDto = modelMapper.map(user, UserDto.class);
			
			Link selfRel = ControllerLinkBuilder.linkTo(UserRestController.class).slash("users").slash(userDto.getID())
					.withSelfRel();
			userDto.add(selfRel);
			
			userDtos.add(userDto);
		}
		
		return new ResponseEntity<List<UserDto>>(userDtos, HttpStatus.OK);
	}

	@GetMapping("/users/{id}")
	ResponseEntity<UserDto> getUser(@PathVariable Long id, Authentication authentication) {

		User user = userService.getUser(id)
				.orElseThrow(() -> new UsernameNotFoundException(" User with id = " + id + " not found."));
		
		UserDto userDto = new UserDto();
		userDto = modelMapper.map(user, UserDto.class);
		
		Link selfRel = ControllerLinkBuilder.linkTo(UserRestController.class).slash("users").slash(userDto.getID())
				.withSelfRel();
		
		userDto.add(selfRel);
		
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	void deleteUser(@PathVariable Long id) {

		User user = userService.getUser(id)
				.orElseThrow(() -> new UsernameNotFoundException(" User with id = " + id + " not found."));
		
		userService.deleteUser(user);
	}

	@PutMapping("/users/{id}")
	ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Long id, BindingResult theBindingResult) {

		if (theBindingResult.hasErrors()) {

			// create error exception and return it
			for (ObjectError er : theBindingResult.getAllErrors()) {

				throw new FieldValidationException(er.getDefaultMessage());

			}
		}

		// if tc number will be updated check whether it is in use or not.
		if (userDto.getTC() != null) {
			User existingUser = userService.findByTC(userDto.getTC());

			if (existingUser != null) {
				throw new FieldValidationException("TC number is in use");
			}
		}

		User user = userService.getUser(id)
				.orElseThrow(() -> new UsernameNotFoundException(" User with id = " + id + " not found."));

					
		userDto.setID(user.getID());
		
		userService.save(userDto);


		Link selfRel = ControllerLinkBuilder.linkTo(UserRestController.class).slash("users").slash(userDto.getID())
				.withSelfRel();
		
		userDto.add(selfRel);
		
		return new ResponseEntity<>(userDto, HttpStatus.OK);

	}

}