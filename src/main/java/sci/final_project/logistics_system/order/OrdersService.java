package sci.final_project.logistics_system.order;

import org.springframework.stereotype.Service;
import sci.final_project.logistics_system.destination.DestinationEntity;

import java.util.*;


@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final List<OrdersEntity> ordersEntityList;


    public OrdersService(OrdersRepository ordersRepository, List<OrdersEntity> ordersEntityList) {
        this.ordersRepository = ordersRepository;
        this.ordersEntityList = ordersEntityList;
    }


    public List<OrdersEntity> getAllOrders() {
        List<OrdersEntity> list = new ArrayList<>();
        for (OrdersEntity ordersEntity : ordersRepository.findAll()) {
            list.add(ordersEntity);
        }
        return list;
    }

}
