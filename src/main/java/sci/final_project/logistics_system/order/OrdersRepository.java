package sci.final_project.logistics_system.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sci.final_project.logistics_system.destination.DestinationEntity;

import java.time.LocalDate;
import java.util.List;


public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {

    List<OrdersEntity> findByDeliveryDate(String localDate);
    List<OrdersEntity> findByDestinationIdAndDeliveryDate(Long destination, String localDate);

}
