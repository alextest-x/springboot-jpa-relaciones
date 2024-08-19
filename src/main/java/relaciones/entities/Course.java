package relaciones.entities;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String instructor;


    //ponemos el atributo student para hacer la relacion bidereccional
    //ponemos un set e inicializamos en el cosntructor vacio en Course();  new HashSet<>();
    //ponemos la relacion
    @ManyToMany(mappedBy = "courses")
     private Set<Student> students;

    public Course() {
        this.students = new HashSet<>();
        //hay que poner this() en el constructor donde tenemos los  atributos name e instructor
    }

    public Course(String name, String instructor) {
        this(); //Llmamos e inicializamos la lista de estudiantes
        this.name = name;
        this.instructor = instructor;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }


    //agreamos el hashcode para que pueda eliminar el padre estudiante a sus hijos cursos
    //para que pueda comparar por id, nombre e instructor
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return Objects.equals(getId(), course.getId()) && Objects.equals(getName(), course.getName()) && Objects.equals(getInstructor(), course.getInstructor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getInstructor());
    }

    @Override
    public String toString() {
        return "{id=" + id +
                ", name='" + name + '\'' +
                ", instructor='" + instructor + '\'' +
                "}";
    }


}

