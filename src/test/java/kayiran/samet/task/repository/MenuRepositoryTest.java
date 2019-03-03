package kayiran.samet.task.repository;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kayiran.samet.task.TaskBootApplication;
import kayiran.samet.task.entity.Menu;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskBootApplication.class)
public class MenuRepositoryTest {


	@Autowired
	MenuRepository repository;

	
	@Test
	public void findById() {
		Optional<Menu> menu = repository.findById(1);
		assertTrue(menu.isPresent());
	}
	


}
