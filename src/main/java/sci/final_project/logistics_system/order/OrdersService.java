package sci.final_project.logistics_system.order;

import org.springframework.stereotype.Service;
import sci.final_project.logistics_system.destination.DestinationEntity;

import java.util.*;


@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;


    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public String computeLastUpdated() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.now();
        return (dtf.format(localDate));
    }

}
