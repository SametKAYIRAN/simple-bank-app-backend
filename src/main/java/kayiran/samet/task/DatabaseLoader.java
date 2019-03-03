/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kayiran.samet.task;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import kayiran.samet.task.dto.UserDto;
import kayiran.samet.task.entity.Menu;
import kayiran.samet.task.entity.Role;
import kayiran.samet.task.repository.MenuRepository;
import kayiran.samet.task.repository.RoleRepository;
import kayiran.samet.task.service.UserService;

@Component
public class DatabaseLoader implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private UserService userService;

	@Override
	public void run(String... strings) throws Exception {

		//Add some roles
		Role role = new Role();
		role.setName("CUSTOMER");
		roleRepository.save(role);
		
		ArrayList<Role> roles = new ArrayList<Role>();
		roles.add(role);
		
		//Create menus
		Menu menu = new Menu();
		menu.setVisible(true);
		menu.setName("Users");
		menu.setRoles(roles);
		menuRepository.save(menu);
		
		menu = new Menu();
		menu.setVisible(true);
		menu.setName("Currency");
		menu.setRoles(roles);
		menuRepository.save(menu);
		
		menu = new Menu();
		menu.setVisible(true);
		menu.setName("Transfer");
		menu.setRoles(roles);
		menuRepository.save(menu);
		
		menu = new Menu();
		menu.setVisible(true);
		menu.setName("Transactions History");
		menu.setRoles(roles);
		menuRepository.save(menu);
		
		//Add some users
		UserDto userDto = new UserDto();
		userDto.setName("Samet Kayıran");
		userDto.setTC("12345678900");
		userDto.setPassword("1234");
		userDto.setMatchingPassword("1234");
		userService.save(userDto);
		
		userDto = new UserDto();
		userDto.setName("Sema Kayıran");
		userDto.setTC("12345678902");
		userDto.setPassword("1234");
		userDto.setMatchingPassword("1234");
		
		userService.save(userDto);
	}
}
