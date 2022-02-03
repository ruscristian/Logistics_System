package sci.final_project.logistics_system.shipping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sci.final_project.logistics_system.GlobalData;
import sci.final_project.logistics_system.destination.DestinationEntity;
import sci.final_project.logistics_system.destination.DestinationRepository;
import sci.final_project.logistics_system.order.OrdersEntity;
import sci.final_project.logistics_system.order.OrdersRepository;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class ShippingService {


    private final OrdersRepository ordersRepository;
    private final GlobalData globalData;
    private final CourierContainer courierContainer;

    private final Logger logger = LoggerFactory.getLogger(ShippingService.class);

    public ShippingService(OrdersRepository ordersRepository, GlobalData globalData,
                           CourierContainer courierContainer) {

        this.ordersRepository = ordersRepository;
        this.globalData = globalData;
        this.courierContainer = courierContainer;
    }

    public void newDayMaker() {
        globalData.setCurrentDate(globalData.getCurrentDate().plusDays(1));
        startingThreads();
    }

    public void startingThreads() {

        List<String> currentDateDestinationList = Collections.synchronizedList(new ArrayList<>());
        Map<DestinationEntity, List<OrdersEntity>> ordersByDestination = new ConcurrentHashMap<>();

        ordersRepository.findByDeliveryDate(globalData.getCurrentDate().format(globalData.getDateTimeFormatter()))
                        .forEach(ordersEntity -> {
                    ordersByDestination.computeIfAbsent(ordersEntity.getDestination(), destination -> new ArrayList<>());

                    ordersByDestination.get(ordersEntity.getDestination()).add(ordersEntity);
                });

        for (DestinationEntity destination : ordersByDestination.keySet()) {
            currentDateDestinationList.add((destination).getName());
        }
        logger.info("New day starting : " + globalData.getCurrentDate().format(globalData.getDateTimeFormatter()));
        logger.info("Today we will deliver in " + currentDateDestinationList);

        for (DestinationEntity destination : ordersByDestination.keySet()) {
            courierContainer.threadCourier(destination, ordersByDestination.get(destination));
        }
    }
}



