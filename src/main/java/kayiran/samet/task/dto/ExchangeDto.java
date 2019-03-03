package kayiran.samet.task.dto;


import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;

import kayiran.samet.task.entity.Currency;
import kayiran.samet.task.entity.User;
import lombok.Data;

@Data
public class ExchangeDto extends ResourceSupport{
	
	private Long ID;
	
	@NotNull(message = "TransactionDto.currency is required")
	private Currency currency;
	
	@NotNull(message = "ExchangeDto.buying is required")
	private Boolean buying;
	
	private float amount;
	
	private float price;
	
	private LocalDateTime dateTime;
	
	private Long userID;
	
	public ExchangeDto() {
	}

}
