package sci.final_project.logistics_system.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sci.final_project.logistics_system.GlobalData;
import sci.final_project.logistics_system.destination.DestinationEntity;
import sci.final_project.logistics_system.destination.DestinationRepository;

import java.time.LocalDate;
import java.util.List;


@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final GlobalData globalData;
    private final DestinationRepository destinationRepository;

    Logger logger = LoggerFactory.getLogger(OrdersService.class);

    public OrdersService(OrdersRepository ordersRepository, GlobalData globalData, DestinationRepository destinationRepository) {
        this.ordersRepository = ordersRepository;
        this.globalData = globalData;
        this.destinationRepository = destinationRepository;
    }

    public ResponseEntity<OrdersEntity> addOrder(OrdersEntity payload, DestinationRepository destination) {
        if (compareDates(payload.getDeliveryDate())) {
            logger.info("Date is older than today.");
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
            logger.info("New order added: " + payload);
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

        if (date == null) {
            date = globalData.getCurrentDate().format(globalData.getDateTimeFormatter());
        }

        if (destination != null) {
            DestinationEntity destinationToSearch = destinationRepository.findDestinationByName(destination);
            if (destinationToSearch != null) {
                logger.info("Orders list for " + destination + " on " + date + " has been generated.");
                return ordersRepository.findByDestinationIdAndDeliveryDate(destinationToSearch.getId(), date);
            } else {
                logger.info("Destination does not exist. Full order list returned.");
                return ordersRepository.findAll();
            }

        } else {
            logger.info("Found orders by delivery date.");
            return ordersRepository.findByDeliveryDate(date);
        }

    }


}