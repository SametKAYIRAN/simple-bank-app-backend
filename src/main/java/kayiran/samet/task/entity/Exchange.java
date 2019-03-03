package kayiran.samet.task.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@SQLDelete(sql="update exchange set is_deleted=true where id = ?")
@Where(clause="is_deleted = false")
@Transactional(isolation= Isolation.READ_COMMITTED)
public class Exchange {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;

	private boolean isDeleted;
	
	private float amount;
	
	private float price;
	
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
	private Boolean buying;
		
	private LocalDateTime dateTime;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;	
	

	public Exchange() {
	}

}
