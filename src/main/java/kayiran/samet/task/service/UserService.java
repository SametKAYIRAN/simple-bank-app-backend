package kayiran.samet.task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import kayiran.samet.task.dto.UserDto;
import kayiran.samet.task.entity.User;

public interface UserService extends UserDetailsService {

	
	User save(UserDto userDto);
	
	User save(User user);

	User findByTC(String tc);
	
	List<User> getUsers();
	
	Optional<User> getUser(Long id);
	
	void deleteUser(User user);
	

}
