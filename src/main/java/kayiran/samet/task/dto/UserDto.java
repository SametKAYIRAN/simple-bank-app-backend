package kayiran.samet.task.dto;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.ResourceSupport;

import kayiran.samet.task.validation.FieldMatch;
import kayiran.samet.task.validation.ValidTC;
import lombok.Data;

@Data
@FieldMatch.List({
    @FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
})
public class UserDto extends ResourceSupport{
	

	@Id
	private Long ID;
	
	@NotNull(message = "UserDto.name is required")
	@Size(min = 1, message = "UserDto.firstname is required")
	private String name;
	
	@NotNull(message = "UserDto.password is required")
	@Size(min = 1, message = "UserDto.password is required")
	private String password;

	@NotNull(message = "UserDto.matchingPassword is required")
	@Size(min = 1, message = "UserDto.matchingPassword required")
	private String matchingPassword;

	@ValidTC
	private String tC;
		
	
	public UserDto() {

	}
	
	public String accessPassoword() {
		
		return password;
	}

}
