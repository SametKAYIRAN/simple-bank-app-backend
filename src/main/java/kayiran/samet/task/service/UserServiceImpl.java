package kayiran.samet.task.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kayiran.samet.task.dto.UserDto;
import kayiran.samet.task.entity.Balance;
import kayiran.samet.task.entity.Currency;
import kayiran.samet.task.entity.Role;
import kayiran.samet.task.entity.User;
import kayiran.samet.task.repository.BalanceRepository;
import kayiran.samet.task.repository.RoleRepository;
import kayiran.samet.task.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BalanceRepository balanceRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		User user = userRepository.findByTC(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getTC(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(
			Collection<Role> list) {
		return list.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public User save(UserDto userDto) {

		User user = modelMapper.map(userDto, User.class);

		// encode password
		user.setPassword(passwordEncoder.encode(userDto.accessPassoword()));

		// give user default role of "CUSTOMER"
		user.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));

		Balance balance = new Balance();
		balance.setAmount(130);
		balance.setCurrency(Currency.TRY);
		balance.setUser(user);

		user.addBalance(balance);

		// save user
		user = userRepository.save(user);

		// save balances
		balanceRepository.save(balance);

		return user;
	}

	@Override
	public User save(User user) {

		// save user in the database
		user = userRepository.save(user);

		return user;
	}

	@Override
	public User findByTC(String tc) {

		return userRepository.findByTC(tc);
	}

	@Override
	public List<User> getUsers() {

		return userRepository.findAll();
	}

	@Override
	public Optional<User> getUser(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public void deleteUser(User user) {

		userRepository.delete(user);

	}

}
