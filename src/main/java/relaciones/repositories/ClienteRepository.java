package relaciones.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import relaciones.entities.Client;

import java.util.Optional;

public interface ClienteRepository extends CrudRepository<Client, Long> {

    /** El query personalizado sirve para que no de error por carga perezosa
     *
     */


    //consulta personalizada en una sola consulta busca el cliente con sus direcciones anidadas
    //@Query("select c from Client c join fetch c.addresses")
    //Optional<Client> findOne(Long id);  //obtenemos el cliente por id

    //join solo trae si el cliente tiene que tenga facturas y tenga direcciones
    //con left join trae el cliente tenga facturas o no
    @Query("select c from Client c left join fetch c.addresses where c.id=?1")
    Optional<Client> findOneWithAddresses(Long id);


    @Query("select c from Client c left join fetch c.invoices where c.id=?1")
    Optional<Client> findOneWithInvoices(Long id);

   // @Query("select c from Client c left join fetch c.invoices left join fetch c.addresses where c.id=?1")
   //trae mas detalle todos los campos
   @Query("select c from Client c left join fetch c.invoices left join fetch c.addresses left join fetch c.clientDetails where c.id=?1")
    Optional<Client> findOne(Long id);






/*
    @Query("select c from Client c left join fetch c.addresses where c.id=?1")
    Optional<Client> findOneWithAddresses(Long id);

    @Query("select c from Client c left join fetch c.invoices where c.id=?1")
    Optional<Client> findOneWithInvoices(Long id);

    @Query("select c from Client c left join fetch c.invoices left join fetch c.addresses left join fetch c.clientDetails where c.id=?1")
    Optional<Client> findOne(Long id);
*/


}
