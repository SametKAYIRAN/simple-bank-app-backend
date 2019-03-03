package kayiran.samet.task.dto;

import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.ResourceSupport;

import kayiran.samet.task.entity.Currency;
import lombok.Data;

@Data
public class TransferDto extends ResourceSupport{

	private Long ID;
	
	@NotNull(message = "TransactionDto.amount is required")
	@Min(value = 1, message = "TransactionDto.amount should be greater than 1")
	private float amount;
	
	@NotNull(message = "TransactionDto.currency is required")
	private Currency currency;
	
	@NotNull(message = "TransactionDto.targetTC is required")
	@Size(min = 1, message = "TransactionDto.targetTC is required")
	private String targetTC;
		
	private LocalDateTime dateTime;
			
	private String senderTC;
	
	private String senderName;
	
	private String targetName;
}
