package sci.final_project.logistics_system.order;

import org.springframework.stereotype.Service;
import sci.final_project.logistics_system.GlobalData;
import sci.final_project.logistics_system.destination.DestinationEntity;
import sci.final_project.logistics_system.destination.DestinationRepository;

import java.util.*;


@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;



    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public OrdersEntity addOrder(OrdersEntity payload, String destinationName, DestinationRepository destination) {
        OrdersEntity newOrder = payload;
        newOrder.setStatus(StatusEnum.NEW);
        newOrder.setLastUpdated("15-12-2021");
        List<DestinationEntity> destinations = destination.findAll();
        for (DestinationEntity destination1 : destinations) {
            if (destination1.getName().equals(destinationName))
                newOrder.setDestination(destination1);
        }
        ordersRepository.save(newOrder);
        return newOrder;
    }

}




