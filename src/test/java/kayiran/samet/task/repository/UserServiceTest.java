package kayiran.samet.task.repository;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kayiran.samet.task.TaskBootApplication;
import kayiran.samet.task.entity.User;
import kayiran.samet.task.service.UserService;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskBootApplication.class)
public class UserServiceTest {

	@Autowired
	UserService service;
	
	@Test
	public void findByTC() {
		User user = service.findByTC("12345678900");
		assertTrue(user.getTC().equals("12345678900"));
	}
	
	@Test
	public void getUsers() {
		List<User> users = service.getUsers();
		assertTrue(users.size() > 0);
	}
	

}

