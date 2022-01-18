package sci.final_project.logistics_system.order;

import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sci.final_project.logistics_system.GlobalData;
import sci.final_project.logistics_system.destination.DestinationEntity;
import sci.final_project.logistics_system.destination.DestinationRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final GlobalData globalData;
    private final DestinationRepository destinationRepository;


    public OrdersService(OrdersRepository ordersRepository, GlobalData globalData, DestinationRepository destinationRepository, DestinationRepository destinationRepository1) {
        this.ordersRepository = ordersRepository;
        this.globalData = globalData;
        this.destinationRepository = destinationRepository1;
    }

    public ResponseEntity<OrdersEntity> addOrder(OrdersEntity payload, DestinationRepository destination) {
        if (compareDates(payload.getDeliveryDate())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            payload.setStatus(StatusEnum.NEW);
            payload.setLastUpdated(computeLastUpdated());
            List<DestinationEntity> destinations = destination.findAll();
            for (DestinationEntity destination1 : destinations) {
                if (destination1.getName().equals(payload.getDestination().getName()))
                    payload.setDestination(destination1);
            }
            ordersRepository.save(payload);
            return new ResponseEntity<>(payload, HttpStatus.CREATED);
        }
    }

    public String computeLastUpdated() {

        LocalDate localDate = LocalDate.now();
        return (globalData.getDateTimeFormatter().format(localDate));
    }

    public boolean compareDates(String deliveryDate) {

        LocalDate localDate = LocalDate.parse(deliveryDate, globalData.getDateTimeFormatter());
        return globalData.getCurrentDate().isAfter(localDate);
    }

    public ResponseEntity<OrdersEntity> cancelOrder(OrdersEntity payload) {
        if (payload.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ordersRepository.findById(payload.getId()).isPresent()) {
            OrdersEntity orderToCancel = ordersRepository.getById(payload.getId());
            if (orderToCancel.getStatus().equals(StatusEnum.DELIVERED)) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            } else {
                orderToCancel.setStatus(StatusEnum.CANCELLED);
                orderToCancel.setLastUpdated(computeLastUpdated());
                ordersRepository.save(orderToCancel);
                return new ResponseEntity<>(orderToCancel, HttpStatus.ACCEPTED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public List<OrdersEntity> findOrdersByCriteria(String destination, String date, GlobalData globalData) {

        if (date == null) {
            date = globalData.getCurrentDate().format(globalData.getDateTimeFormatter());
        }

        if (destination != null) {
            List<DestinationEntity> destinations = destinationRepository.findAll();
            for (DestinationEntity destination1 : destinations) {
                if (destination1.getName().equals(destination)) {
                    return ordersRepository.findByDestinationIdAndDeliveryDate(destination1.getId(), date);
                }
                else {
                    //log si http response
                    System.out.println("Destination does not exist");
                }
            }
        } else {
            return ordersRepository.findByDeliveryDate(date);
        }
        return ordersRepository.findAll();
    }

//    public List<OrdersEntity> findOrdersByCriteria(String destination, String date, GlobalData globalData) {
//        List<DestinationEntity> destinations = destinationRepository.findAll();
//
//        if (destination != null && date != null) {
//            for (DestinationEntity destination1 : destinations) {
//                if (destination1.getName().equals(destination)) {
//                    return ordersRepository.findByDestinationIdAndDeliveryDate(destination1.getId(), date);
//                }
//                else {
//                    //log si http response
//                    System.out.println("Destination does not exist");
//                }
//            }
//        }
//        if (date == null && destination != null) {
//            String formattedString = globalData.getCurrentDate().format(globalData.getDateTimeFormatter());
//            for (DestinationEntity destination1 : destinations) {
//                if (destination1.getName().equals(destination)) {
//                    return ordersRepository.findByDestinationIdAndDeliveryDate(destination1.getId(), formattedString);
//                }
//                else {
//                    //log si http response
//                    System.out.println("Destination does not exist");
//                }
//            }
//        }
//        if (destination == null && date != null) {
//            return ordersRepository.findByDeliveryDate(date);
//        }
//        return ordersRepository.findAll();
//    }
}