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


    @Async(value = "taskExecutor")
    public void threadCourier(DestinationEntity destination, List<OrdersEntity> orders){
        logger.info("STARTING deliveries for " + destination.getName() + " on " +
                          Thread.currentThread().getName() + " for " + destination.getDistance() + "km");

        for(OrdersEntity orderStatus: orders){
            if(orderStatus.getStatus().equals(StatusEnum.NEW)){
                orderStatus.setStatus(StatusEnum.DELIVERING);
                ordersRepository.save(orderStatus);
            }
        }
        try {
            Thread.sleep(destination.getDistance()*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(OrdersEntity orderStatus: orders){

            if(!orderStatus.getStatus().equals(StatusEnum.DELIVERING)){
                logger.info("The order has been canceled or has been delivered or is new");
            }
            else {
                orderStatus.setStatus(StatusEnum.DELIVERED);

                globalData.increaseProfit(destination.getDistance());
            }
        }
        ordersRepository.saveAll(orders);
        logger.info("DELIVERED in " + destination.getName() + " on " +
                Thread.currentThread().getName());

    }

}
