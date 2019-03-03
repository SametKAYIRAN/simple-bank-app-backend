package kayiran.samet.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kayiran.samet.task.entity.Balance;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

}
