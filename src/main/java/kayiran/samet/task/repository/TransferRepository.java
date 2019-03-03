package kayiran.samet.task.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kayiran.samet.task.entity.Transfer;
import kayiran.samet.task.entity.User;


public interface TransferRepository extends JpaRepository<Transfer, Long> {

	@Query("SELECT t FROM Transfer t  WHERE t.senderTC = ?1 OR t.targetTC = ?1")
	List<Transfer> findByTC(String TC);
	
	
}
