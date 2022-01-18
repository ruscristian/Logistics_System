package sci.final_project.logistics_system.order;

import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sci.final_project.logistics_system.GlobalData;
import sci.final_project.logistics_system.destination.DestinationEntity;
import sci.final_project.logistics_system.destination.DestinationRepository;
import sci.final_project.logistics_system.shipping.ShippingService;

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

    Logger logger = LoggerFactory.getLogger(OrdersService.class);

    public OrdersService(OrdersRepository ordersRepository, GlobalData globalData, DestinationRepository destinationRepository, DestinationRepository destinationRepository1) {
        this.ordersRepository = ordersRepository;
        this.globalData = globalData;
        this.destinationRepository = destinationRepository1;
    }

    public ResponseEntity<OrdersEntity> addOrder(OrdersEntity payload, DestinationRepository destination) {
        if (compareDates(payload.getDeliveryDate())) {
            logger.info("Date is older than today.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            OrdersEntity newOrder = payload;
            newOrder.setStatus(StatusEnum.NEW);
            newOrder.setLastUpdated(computeLastUpdated());
            List<DestinationEntity> destinations = destination.findAll();
            for (DestinationEntity destination1 : destinations) {
                if (destination1.getName().equals(payload.getDestination().getName()))
                    newOrder.setDestination(destination1);
            }
            ordersRepository.save(newOrder);
            logger.info("New order added: " + newOrder);
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

    public ResponseEntity<OrdersEntity> cancelOrder(OrdersEntity payload) {
        if (payload.getId() == null) {
            logger.info("Provided ID does not exist.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ordersRepository.findById(payload.getId()).isPresent()) {
            OrdersEntity orderToCancel = ordersRepository.getById(payload.getId());
            if (orderToCancel.getStatus().equals(StatusEnum.DELIVERED)) {
                logger.info("Order already delivered.");
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            } else {
                orderToCancel.setStatus(StatusEnum.CANCELLED);
                orderToCancel.setLastUpdated(computeLastUpdated());
                ordersRepository.save(orderToCancel);
                logger.info("Order cancelled: " + orderToCancel) ;
                return new ResponseEntity<>(orderToCancel, HttpStatus.ACCEPTED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public List<OrdersEntity> findOrdersByCriteria(String destination, String date, GlobalData globalData) {
        List<DestinationEntity> destinations = destinationRepository.findAll();
        List<OrdersEntity> resultedList = ordersRepository.findAll();
        boolean orderFound = false;

        if (destination != null && date != null) {
            for (DestinationEntity destination1 : destinations) {
                if (destination1.getName().equals(destination)) {
                    orderFound = true;
                    logger.info("Orders list for " + destination + " on " + date + " has been generated.");
                    return resultedList = ordersRepository.findByDestinationIdAndDeliveryDate(destination1.getId(), date);

                }
            }
        }
        if (date == null && destination != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedString = globalData.getCurrentDate().format(formatter);
            for (DestinationEntity destination1 : destinations) {
                if (destination1.getName().equals(destination)) {
                    logger.info("Orders list for " + destination + " on current application date has been generated.");
                  return resultedList = ordersRepository.findByDestinationIdAndDeliveryDate(destination1.getId(), formattedString);
                }
            }
        }
        if (destination == null && date != null) {
            logger.info("Found orders by delivery date.");
           return resultedList = ordersRepository.findByDeliveryDate(date);
        }

        logger.info("Destination does not exist. Full orders list returned.");
        return resultedList = ordersRepository.findAll();

    }
}