package sci.final_project.logistics_system.shipping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sci.final_project.logistics_system.GlobalData;
import sci.final_project.logistics_system.destination.DestinationEntity;
import sci.final_project.logistics_system.order.OrdersEntity;
import sci.final_project.logistics_system.order.OrdersRepository;
import sci.final_project.logistics_system.order.StatusEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Component
public class CourierContainer {


    private final Logger logger = LoggerFactory.getLogger(CourierContainer.class);
    private final GlobalData globalData;
    private final OrdersRepository ordersRepository;

    public CourierContainer(GlobalData globalData, OrdersRepository ordersRepository) {
        this.globalData = globalData;
        this.ordersRepository = ordersRepository;
    }


    @Async("taskExecutor")
    public void threadCourier(DestinationEntity destination, List<OrdersEntity> orders) {
        logger.info("STARTING deliveries for " + destination.getName() + " on " +
                Thread.currentThread().getName() + " for " + destination.getDistance() + "km");

        List<Long> ordersIdList = new ArrayList<>();
        orders.forEach(ordersEntity -> ordersIdList.add(ordersEntity.getId()));

        List<OrdersEntity> refreshedOrders1 = ordersRepository.findAllById(ordersIdList);

        for (OrdersEntity orderStatus : refreshedOrders1) {
            if (orderStatus.getStatus().equals(StatusEnum.NEW)) {
                orderStatus.setStatus(StatusEnum.DELIVERING);
                orderStatus.setLastUpdated(LocalDate.now().format(globalData.getDateTimeFormatter()));
                ordersRepository.save(orderStatus);
            }
            else  { logger.info("The order with the idNr:" +"\""+ orderStatus.getId() +"\""+
                    " has been canceled or has been delivered or is new."); }
        }
        try {
            Thread.sleep(destination.getDistance() * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        List<OrdersEntity> refreshedOrders = ordersRepository.findAllById(ordersIdList);

        for (OrdersEntity orderStatus : refreshedOrders) {

            if (orderStatus.getStatus().equals(StatusEnum.CANCELLED) ||
                    orderStatus.getStatus().equals(StatusEnum.DELIVERED) ||
                    orderStatus.getStatus().equals(StatusEnum.NEW)) {
                logger.info("The order has been canceled or has been delivered or is new");
            } else {
                orderStatus.setStatus(StatusEnum.DELIVERED);
                orderStatus.setLastUpdated(LocalDate.now().format(globalData.getDateTimeFormatter()));
                globalData.increaseProfit(destination.getDistance());
            }
        }
        ordersRepository.saveAll(refreshedOrders);
        logger.info("DELIVERED in " + destination.getName() + " on " +
                Thread.currentThread().getName());

    }

}
