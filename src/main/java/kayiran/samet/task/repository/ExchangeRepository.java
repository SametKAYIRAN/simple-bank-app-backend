package kayiran.samet.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kayiran.samet.task.entity.Exchange;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

	@Query("SELECT e FROM Exchange e JOIN FETCH e.user u WHERE u.id = ?1")
	List<Exchange> findByUserId(Long id);
}
