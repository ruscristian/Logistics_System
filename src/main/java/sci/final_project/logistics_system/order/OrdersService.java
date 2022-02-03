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

    public ResponseEntity<String> cancelOrder(List<Long> idList) {
        int nullIds = 0;
        int succeeded = 0;
        int alreadyDelivered = 0;

        for (Long id : idList) {

            if (id != null) {
                if (ordersRepository.findById(id).isPresent()) {
                    OrdersEntity orderToCancel = ordersRepository.getById(id);
                    if (orderToCancel.getStatus().equals(StatusEnum.DELIVERED)) {
                        logger.info("Order already delivered.");
                        alreadyDelivered++;
                    } else {
                        orderToCancel.setStatus(StatusEnum.CANCELLED);
                        orderToCancel.setLastUpdated(computeLastUpdated());
                        ordersRepository.save(orderToCancel);
                        logger.info("Order cancelled: " + orderToCancel);
                        succeeded++;
                    }
                } else {
                    logger.info("Provided ID does not exist.");
                    nullIds++;
                }
            }
        }
        if (succeeded != 0) {
            return new ResponseEntity<String>(succeeded + " out of " + idList.size() + " orders cancelled", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("The provided orders are already delivered or the id is not valid.", HttpStatus.BAD_REQUEST);
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