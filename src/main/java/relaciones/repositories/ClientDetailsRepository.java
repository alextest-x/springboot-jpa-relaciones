package relaciones.repositories;

import org.springframework.data.repository.CrudRepository;
import relaciones.entities.ClientDetails;

public interface ClientDetailsRepository extends CrudRepository<ClientDetails, Long> {


}
