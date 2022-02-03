package sci.final_project.logistics_system.order;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {

    List<OrdersEntity> findByDeliveryDate(String localDate);
    List<OrdersEntity> findByDestinationIdAndDeliveryDate(Long destination, String localDate);

}
