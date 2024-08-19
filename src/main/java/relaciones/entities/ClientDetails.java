package relaciones.entities;

import jakarta.persistence.*;


/* ejemplo de @OneToOne */

@Entity
@Table(name="clients_details")
public class ClientDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean premium;

    private Integer points;

    /**
    // se comenta porque lo pasamos a la clase Client porque cambiamos la relacion
    //
    //direccion unidireccional
    //por defecto la pk esta en la clase ClientDetails esta la relacion
    @OneToOne
   private Client client;
   */


    //creamos el atributo client para hacer la direcion bidereccional @OneToOne
    //donde el dueño de la relacion esta en tabla client  y en clientDetails esta la llave foranea
    //client_id
    //y en clase client le ponemos el joinColumn va la Fk
    @OneToOne
    @JoinColumn(name = "id_cliente") // esta es la dueña de la relacion
    public Client client;

    public ClientDetails() {
    }

    public ClientDetails(boolean premium, Integer points) {
        this.premium = premium;
        this.points = points;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    /*
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
*/




    @Override
    public String toString() {
        return
                //"ClientDetails{" +
                "{id=" + id +
                ", premium=" + premium +
                ", points=" + points +
                //", client=" + client +
                "}";
    }
}
