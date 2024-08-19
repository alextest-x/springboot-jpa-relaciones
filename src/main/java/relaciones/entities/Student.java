package relaciones.entities;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="students")
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;

    /**
    muchos cursos por eso usamos el Set()
    es unidireccional la tiene el estudiante cascade para guardar los cursos tambien
    al hacer el save en studiante se registran los cursos en automatico dependientes que son los hijos
    y el padre es estudiante
    el cascade no se puede tener el ALL solo en persist y merge tampoco en remove
    ejemplo si borramos los cusros del estudiante a eliminar
    no se pueden eliminar porque estos cursos estan asignados a otro estudiante
    solo se puede eliminar el studiante que tenga un solo curso

     @JoinTable() configura el nombre de la tabla tbl_alumnos_cursos
     joinColumns configura la llave foranea de la clase entity principal Student
     que representa mi tabla aui ponemos el nombre de la llave foranea

     inverseJoinColumns = @JoinColumn(name = "curso_id"),
     ponemos la foreing Key de la contraparte de la relacion inversa

     @UniqueConstraint(columnNames = {"alumno_id", "curso_id"}))
     ponemos las dos foreign keys donde alumno_id y curso_id no se pueden repetir
     estas dos son unicas.

      Donde un alumno no puede tener un mismo curso repetido
      y un curso no puede tener un mismo alumno repetido

     si se puede tener un alumno puede tener muchos cursos no repetidos
     y un curso puede tener muchos alumnos no repetidos
     **/

    //solucion  a la carga lazy perezosa ponemos fetch = FetchType.EAGER
    //@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE,})
   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,})
   @JoinTable(name = "tbl_alumnos_cursos", joinColumns = @JoinColumn(name = "alumno_id"),
    inverseJoinColumns = @JoinColumn(name = "curso_id"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"alumno_id", "curso_id"}))
    private Set<Course> courses;


    public Student() {
        //inicializamos el set
        this.courses = new HashSet<>();

    }


    //en el constructor pasamos el nombre y apellido
    //a aqui asignamos el constructor vacio para que pueda asignar al HashSet o instanciar de los cursos
    //asignar datos porque si es null hay una exception
    public Student(String name, String lastname) {
        this(); //reutilizamos el constructor vacio inicializar la lista de cursos
        this.name = name;
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return Objects.equals(getId(), student.getId()) && Objects.equals(getName(), student.getName()) && Objects.equals(getLastname(), student.getLastname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLastname());
    }

    //agregar los metdodos addCourses para hacer la direccion bidireccional
    public void addCourse(Course course){
        this.courses.add(course);
        //para la relacion inversa
        course.getStudents().add(this);

    }

    //elimina el curso en ambos sentidos
    public void removeCourse(Course course){
        this.courses.remove(course);
        course.getStudents().remove(this);
    }




    //como studiante es el dueño de la relacion podemos dejar cursos
    //pero en cursos no podemos tener estudiantes cuando la relacion sea inversa
    //hay un error se hace ciclico

    @Override
    public String toString() {
        return "{id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", courses=" + courses +
                "}";
    }



}
