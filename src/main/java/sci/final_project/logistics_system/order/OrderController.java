package sci.final_project.logistics_system.order;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {


 private final OrdersService ordersService;

    public OrderController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


    @GetMapping("/get")
    public List<OrdersEntity> getOrders(){

        return ordersService.getAllOrders();
    }

}
