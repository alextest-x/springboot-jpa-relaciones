package relaciones.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import relaciones.entities.Student;

import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Long> {

    //consulta personaliazda para evitar la carga perezosa en student

    @Query("select s from Student s left join fetch s.courses where s.id=?1")
    Optional<Student> findOneWithCourses(Long id);

}
