package kayiran.samet.task.repository;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kayiran.samet.task.TaskBootApplication;
import kayiran.samet.task.entity.Currency;
import kayiran.samet.task.entity.Transfer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskBootApplication.class)
public class TransferRepositoryTest {


	@Autowired
	TransferRepository repository;

	@Test
	public void save() {
		
		Transfer transfer = new Transfer();
		
		transfer.setAmount(10f);
		transfer.setCurrency(Currency.TRY);
		transfer.setSenderTC("12345678900");
		transfer.setTargetTC("12345678904");
		transfer.setSenderName("Samet Kayıran");
		transfer.setTargetName("Sema Kayıran");
		
		repository.save(transfer);
	}
	
	@Test
	public void findById() {
		
		save();
		
		Optional<Transfer> transfer = repository.findById(1L);
		assertTrue(transfer.isPresent());
	}
	


}
