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

    public OrdersEntity addOrder(OrdersEntity payload, DestinationEntity destination) {
        OrdersEntity newOrder = payload;
//        newOrder.setStatus(StatusEnum.NEW);
//        newOrder.setLastUpdated("15-12-2021");
        newOrder.setDestination(destination);
        ordersRepository.save(newOrder);
        return newOrder;
    }


}
