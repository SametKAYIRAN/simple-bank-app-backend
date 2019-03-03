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
public class ExchangeRepositoryTest {


	@Autowired
	ExchangeRepository repository;
	
	@Autowired
	UserService userService;

	@Test
	public void save() {
		
		Exchange exchange = new Exchange();
		exchange.setAmount(10f);
		exchange.setBuying(true);
		exchange.setCurrency(Currency.EURO);
		exchange.setPrice(0.20f);
		exchange.setUser(userService.getUser(1l).get());
		
		Exchange e = repository.save(exchange);
		
		assertTrue(e.getID() > 0);
	}
	
	
	@Test
	public void findByUserId() {
		List<Exchange> exchanges = repository.findByUserId(1l);
		assertTrue(exchanges.size() > 0);
	}
	


}
