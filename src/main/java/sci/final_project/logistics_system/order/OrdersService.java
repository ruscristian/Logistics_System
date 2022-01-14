package sci.final_project.logistics_system.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sci.final_project.logistics_system.GlobalData;
import sci.final_project.logistics_system.destination.DestinationEntity;
import sci.final_project.logistics_system.destination.DestinationRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final GlobalData globalData;



    public OrdersService(OrdersRepository ordersRepository, GlobalData globalData) {
        this.ordersRepository = ordersRepository;
        this.globalData = globalData;
    }

    public ResponseEntity<OrdersEntity> addOrder(OrdersEntity payload, DestinationRepository destination) {
        if (compareDates(payload.getDeliveryDate()))
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
            else
            {
                OrdersEntity newOrder = payload;
                newOrder.setStatus(StatusEnum.NEW);
                newOrder.setLastUpdated(computeLastUpdated());
                List<DestinationEntity> destinations = destination.findAll();
                for (DestinationEntity destination1 : destinations) {
                    if (destination1.getName().equals(payload.getDestination().getName()))
                        newOrder.setDestination(destination1);
                }
                ordersRepository.save(newOrder);
                return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
            }
    }

    public String computeLastUpdated() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.now();
        return (dtf.format(localDate));
    }

    public boolean compareDates(String deliveryDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = deliveryDate;
        LocalDate localDate = LocalDate.parse(date, formatter);
        return (globalData.getCurrentDate().isAfter(localDate));
    }

}




