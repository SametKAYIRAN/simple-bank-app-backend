package kayiran.samet.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import kayiran.samet.task.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

	User findByTC(String TC);

}
