package sci.final_project.logistics_system.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {



}
