package sci.final_project.logistics_system.order;


import org.springframework.web.bind.annotation.*;
import sci.final_project.logistics_system.destination.DestinationEntity;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {


 private final OrdersService ordersService;
 private final OrdersRepository ordersRepository;


    public OrderController(OrdersService ordersService, OrdersRepository ordersRepository) {
        this.ordersService = ordersService;
        this.ordersRepository = ordersRepository;
    }


    @GetMapping("/get")
    public List<OrdersEntity> getOrders(){
        return ordersRepository.findAll();
    }




}
