package kayiran.samet.task.repository;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kayiran.samet.task.TaskBootApplication;
import kayiran.samet.task.entity.Currency;
import kayiran.samet.task.entity.Exchange;
import kayiran.samet.task.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskBootApplication.class)
public class CurrencyRepositoryTest {


	@Autowired
	CurrencyRepository repository;
	

	@Test
	public void getCurrencies() {
		
		assertTrue(repository.getCurrencies().getEur() > 0);
	
	}
	
	
	
	


}
