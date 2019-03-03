package kayiran.samet.task.repository;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kayiran.samet.task.TaskBootApplication;
import kayiran.samet.task.entity.Balance;
import kayiran.samet.task.entity.Currency;
import kayiran.samet.task.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskBootApplication.class)
public class BalanceRepositoryTest {


	@Autowired
	BalanceRepository repository;
	
	@Autowired
	UserService service;
	
	@Test
	public void save() {
		
		Balance balance = new Balance();
		balance.setAmount(10.0f);
		balance.setCurrency(Currency.EURO);
		balance.setUser(service.findByTC("12345678900"));
		
		Balance b = repository.save(balance);
		
		assertTrue(b.getID() > 0);
	
	}
	
	@Test
	public void getBalance() {
		
		assertTrue(repository.findById(1l).isPresent());
	
	}
	
	
	
	


}
