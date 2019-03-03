package kayiran.samet.task.dto;

import org.springframework.hateoas.ResourceSupport;

import lombok.Data;

@Data
public class CurrencyDto extends ResourceSupport {
	
	private float usd;
	private float jpy;
	private float eur;

	public CurrencyDto() {

	}
	

}
