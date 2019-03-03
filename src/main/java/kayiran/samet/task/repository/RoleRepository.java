package kayiran.samet.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kayiran.samet.task.entity.Role;

//@RepositoryRestResource(path="courses")
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	
	Role findByName(String role);
	
	/*
    User findByTC(String TC);
    
    UserDetails findByRoleName(String TC);*/
    
	/*
	List<Course> findByNameAndId(String name, Long id);

	List<Course> findByName(String name);

	List<Course> countByName(String name);
	
	List<Course> countByid(long id);

	List<Course> findByNameOrderByIdDesc(String name);

	List<Course> deleteByName(String name);

	@Query("Select  c  From Course c where name like '%100 Steps'")
	List<Course> courseWith100StepsInName();

	@Query(value = "Select  *  From Course c where name like '%100 Steps'", nativeQuery = true)
	List<Course> courseWith100StepsInNameUsingNativeQuery();

	@Query(name = "query_get_100_Step_courses")
	List<Course> courseWith100StepsInNameUsingNamedQuery();
	
	*/
}
