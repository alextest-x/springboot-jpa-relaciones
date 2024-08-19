package relaciones.repositories;

import org.springframework.data.repository.CrudRepository;
import relaciones.entities.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {


}
