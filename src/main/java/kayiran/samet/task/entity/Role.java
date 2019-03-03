package kayiran.samet.task.entity;

import java.util.Collection;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Cacheable
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;

	private String name;

	@JsonManagedReference
	@ManyToMany(mappedBy = "roles")
    private Collection<User> users;
	
	public Role() {
	}
		
}
