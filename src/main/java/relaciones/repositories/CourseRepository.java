package relaciones.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import relaciones.entities.Course;

import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long> {

    //consulta personaliazda para evitar la carga perezosa en cursos para traer toda la consulta
    //obtener cursos de los estudiantes con id
    @Query("select c from Course c left join fetch c.students where c.id=?1")
    Optional<Course> findOneWithStudents(Long id);
}
