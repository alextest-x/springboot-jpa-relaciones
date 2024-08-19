package relaciones.entities;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name="invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private Long total;


    /**    @ManyToOne
     *     muchas facturas a un cliente
     *
     *     un cliente tiene muchas facturas
     *
     *     El primero de la carnalidad se refiere a la clase donde esta el atributo Invoice
     *     se genera un fK en la tabla Invioce es duena de la raclacion porque tiene la llave foranea
     *     hacia el cliente  client_id => fk en forma automatica
     *
     *     JoinColum poene el nombre de la llave foranea.
     *
     *     direccion bidedirecional cliente tengamos sus facturas
     *     facturas obtenemos al cliente asociado
     *
     */


    /** llave foranea le pone el nombre con @JoinColumn
     *  indica que quien tiene la relacion
     *  y hay que poner la contraparte en la clase de cliente
     *  en le atributo invoices mappedBy = "client"
     *  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
     *
     */

    @JoinColumn(name="client_id") //llave foranea le pone el nombre con @JoinColumn
    @ManyToOne
    private Client client;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Invoice() {
    }

    public Invoice(String descripcion, Long total) {
        this.descripcion = descripcion;
        this.total = total;
    }


    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", total=" + total +
                '}';
    }

    //el equals se aplica a los List
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice invoice)) return false;
        return Objects.equals(getId(), invoice.getId()) && Objects.equals(getDescripcion(), invoice.getDescripcion()) && Objects.equals(getTotal(), invoice.getTotal());
    }

    //el hascode se aplica a los set
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescripcion(), getTotal());
    }
}
