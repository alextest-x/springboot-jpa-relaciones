package relaciones.entities;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="addresses")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Street;

    private Integer number;


    public Address() {
    }

    public Address(String street, Integer number) {
        Street = street;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return Objects.equals(getId(), address.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", Street='" + Street + '\'' +
                ", number=" + number +
                '}';
    }



    //Api de colecciones de java para la Api List
    //que se usa para que pueda eliminar un objeto mediante la busquedad o comparacion
    //por el id que biene de la bd y no por referencia


}
