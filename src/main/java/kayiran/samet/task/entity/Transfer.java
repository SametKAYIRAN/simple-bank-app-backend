package kayiran.samet.task.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.Data;

@Data
@Entity
@SQLDelete(sql="update transaction set is_deleted=true where id = ?")
@Where(clause="is_deleted = false")
@Transactional(isolation= Isolation.READ_COMMITTED)
public class Transfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;

	private boolean isDeleted;
	
	private float amount;
	
	@Enumerated(EnumType.STRING)
	private Currency currency;
		
	private LocalDateTime dateTime;
			
	@Column(name="sender_tc")
	private String senderTC;
	
	private String senderName;
	
	@Column(name="target_tc")
	private String targetTC;
	
	private String targetName;
	
	public Transfer() {
	}

}
