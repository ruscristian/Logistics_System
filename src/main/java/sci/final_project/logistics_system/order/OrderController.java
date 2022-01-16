package sci.final_project.logistics_system.order;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {


 private final OrdersService ordersService;
 private final OrdersRepository ordersRepository;

    public OrderController(OrdersService ordersService, OrdersRepository ordersRepository, DestinationRepository destinationRepository, GlobalData globalData) {
        this.ordersService = ordersService;
        this.ordersRepository = ordersRepository;
    }


    @GetMapping("/get")
    public List<OrdersEntity> getOrders(){

//        return ordersService.getAllOrders();
        return ordersRepository.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<OrdersEntity> addOrder(@RequestBody OrdersEntity payload) throws IllegalArgumentException {
        return ordersService.addOrder(payload, destinationRepository);
    }

    @PutMapping("/cancel")
    public ResponseEntity<OrdersEntity> cancelOrder(@RequestBody OrdersEntity payload) throws IllegalArgumentException {
        return ordersService.cancelOrder(payload);
    }

    @GetMapping("/status")
    public List<OrdersEntity> searchOrders(@RequestParam(required = false) String destination, @RequestParam(required = false) String date) {
        return ordersService.findOrdersByCriteria(destination, date, globalData);
    }
}
