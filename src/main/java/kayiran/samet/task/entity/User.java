package kayiran.samet.task.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@SQLDelete(sql="update user set is_deleted=true where id = ?")
@Where(clause="is_deleted = false")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;
	
	private boolean isDeleted;
	
	private String name;

	private String password;

	private String tC;

	@JsonManagedReference
	@OneToMany(mappedBy = "user")
	private List<Balance> balances = new ArrayList<Balance>();
	
	@JsonManagedReference
	@OneToMany(mappedBy = "user")
	private List<Exchange> exchanges = new ArrayList<Exchange>();

	
	@JsonBackReference
	@ManyToMany
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

	public void addBalance(Balance balance) {
		this.balances.add(balance);
	}

	public void removeBalance(Balance balance) {

		this.balances.remove(balance);
	}

	public User() {
	}

	
	

}
