package relaciones.entities;


import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name= "clients")
public class Client {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastname;



    /** @OneToMany
        un cliente tiene muchas direcciones
        con cascade.All aplica cuando se crea un cliente
        se guarda, se crea o se elimina sus direcciones
        con sus relaciones
        orphanRemoval = true  el campo queda en null para eliminar
        la direccion no queda asignado a un cliente elimina todos esos registros
         cuando es una relacion una lista hay que inicializar en el constructor vacio
         creamos la instancia del  addresses= new ArrayList<>();
     */

    //sin especificar un joincolum se crea una tabla intermedia entre direcciones y cliente
    //desacoplada de las dos que contine la llave forane de cliente que apunta al id_cliente
    // y la llave foranea de direcciones que apunta al id_direcciones
    //@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Address> addresses;


    // crea en la tabla de direcciones con mi llave foranea
    //@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    //@JoinColumn(name= "client_id") //crea en la tabla de direcciones con mi llave foranea
    //private List<Address> addresses;


     /** Mapeo a mi tabla intermedia personalizada
      *   crea la tabla intemedia con nombre personalizado
      *   @JoinTable(name= "tbl_clientes_to_direcciones")
      *   configuramos los nombre de la FK con joinColumns es la relacion principal
      *   el nombre @JoinColumn(name="id_cliente")) FK principal
      *
      *   inverseJoinColumns = @JoinColumn(name= "id_direcciones"))
      *   muchas direcciones estan asociado a un cliente (no se pueden repetir porque id cliente es unico)
      *   un cliente tiene muchas direcciones

      *   tenemos la reclacion inversa con inverseJoinColumns
      *
      *   orphanRemoval = true con esta anotacion se eliminan los registros que se quedan sin relacion
      *   sino la ponemos no se eliminan la direccion y no queda sin ralacion
      *
      *   fetch = FetchType.EAGER  carga todas las direcciones carga toda la consulta
      * */

     //se comenta porque se puso una consulta personalizada que trae las dos direcciones y una por una
    //@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name= "tbl_clientes_to_direcciones",
            joinColumns = @JoinColumn(name="id_cliente"),
    inverseJoinColumns = @JoinColumn(name= "id_direcciones"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_direcciones"})) //no se puede repetir
    //private List<Address> addresses;
    private Set<Address> addresses;




    //cuando el Set o list el fecth siempre es Lizzy o no se especefica

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
    //private List<Invoice> invoices; //se comenta para que no de error cannot simultaneously fetch multiple bags
    private Set<Invoice> invoices;    //se pone Set

    public Client() {

        //es una lista de direcciones tiene le objeto remove y lo elimina
        //addresses= new ArrayList<>();
        //invoices= new ArrayList<>();

        addresses= new HashSet<>();
        invoices= new HashSet<>();
    }

    /*
    //ahora la relacion esta del lado del cliente
    //la fk esta en la tabla Client y es el dueño de la relacion
    //simpre que termina en One cuando es una la relacion por defecto es Eager
    // No da error porque ya que el clientDetails no se otiene de la consulta, sino cunado se persiste,
    //es decir ya lo tenemos cuando hacemos el save() Lo tenemos ya que lo instanciamos, lo creamos e insertamos en la tabla
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente_detalle") //cambiando el nombre de la FK
    private ClientDetails clientDetails;
*/

    //comentamos JoinColum para indicar que el dueño de la ralacion es clientDatils
    //@JoinColumn(name = "id_cliente_detalle") //cambiando el nombre de la FK

    //En cliente se crea en cascada el clientdetalle es dueña de la ralacion la clase hija que tiene la fk
    // pero la clase Client es la clase padre y se tiene el casacade
    //la relacion inversa es el mappedBy y le ponemos el atributo de la contraparte  que es client
    //donde va el mappedBy es la clase padre
    //donde va el Joincolum es a clase hija dueña de la realcion que tiene la fk
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
    private ClientDetails clientDetails;



    //hay que crear la contraparte de la tabla clienteDetalis en la clase ClientDetails





    public Client(String name, String lastname) {
        this();  //inicializar direcciones y los demas parametros
        this.name = name;
        this.lastname = lastname;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }


    //en solo metodo se maneja la relacion inversa
    //le pasamos al cliente la factura
    //la factura le pasamos al cliente
    //del metodo oneToManyInvoiceBidireccional()
    public Client addInvoice(Invoice invoice){
        invoices.add(invoice);
        invoice.setClient(this);
        return this; //misma referencia
    }


    public void removeInvoice(Invoice invoice) {
        this.getInvoices().remove(invoice); //se elimina del cliente la factura
        invoice.setClient(null);            //se elimina de la factura el cliente
    }


    public ClientDetails getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(ClientDetails clientDetails) {
        this.clientDetails = clientDetails;
        //ponemos la relacion inversa y le ponemos this clientDetails.setClient(client);
        clientDetails.setClient(this);
    }

    public void removeClientDetails(ClientDetails clientDetails) {
        clientDetails.setClient(null);
        this.clientDetails = null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return Objects.equals(getId(), client.getId());
    }




    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    //cuando el Set o list el fecth siempre es Lizzy o no se especefica
    //cuando son lista son muchos es lizy
    //cuando es una sola relacion es Eager
    // se puso
    // @OneToOne(fetch = FetchType.LAZY)
    // private ClientDetails clientDetails;
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", addresses=" + addresses +     //se descomenta para que imprime y no de error pero ya se puso el Lyzzy
                ", invoices=" + invoices +       //se descomenta para que imprime y no de error pero ya se puso el Lyzzy
                ", clientDetails=" + clientDetails + //por defecto es Eager
                '}';
    }
}
