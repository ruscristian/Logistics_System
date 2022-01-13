package sci.final_project.logistics_system.shipping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sci.final_project.logistics_system.GlobalData;
import sci.final_project.logistics_system.destination.DestinationEntity;
import sci.final_project.logistics_system.destination.DestinationRepository;
import sci.final_project.logistics_system.order.OrdersEntity;
import sci.final_project.logistics_system.order.OrdersRepository;
import sci.final_project.logistics_system.order.StatusEnum;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ShippingService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DestinationRepository destinationRepository;
    private final OrdersRepository ordersRepository;
    private GlobalData globalData;

    Logger logger = LoggerFactory.getLogger(ShippingService.class);

    public ShippingService(DestinationRepository destinationRepository, OrdersRepository ordersRepository,
                            GlobalData globalData) {
        this.destinationRepository = destinationRepository;
        this.ordersRepository = ordersRepository;

        this.globalData = globalData;
    }

    // New day maker

    //    Avanseaza data curenta a aplicatiei cu o zi. La pornirea aplicatiei,
    //    data curenta a aplicatiei va fi 15-12-2021. (log + console: “New day starting : 15-12-2021”)

    //TODO current day shipping status

    //   La inceputul fiecarei zile, toate livrarile din ziua respectiva vor fi grupate pe baza destinatiei
    //   si vor fi marcate ca fiind “In curs de livrare”.
    //   (log + console: “Today we will be delivering to Ploiesti, Pitesti, Craiova”)

    //TODO thread courier

    //     Pentru fiecare destinatie distincta se va submite cate un task
    //     catre executor care va face livrarile la destinatia respectiva
    //     (log + console: “Starting deliveries for Ploiesti on Thread 0 for 25 km”). (hint: folositi @Async)





    public void newDayMaker(){
        globalData.setCurrentDate(globalData.getCurrentDate().plusDays(1));
//        for(OrdersEntity orderUpdatingStatus: currentDateOrdersList){
//            orderUpdatingStatus.setStatus(StatusEnum.NEW);
//        }

        logger.info("New day starting : " + globalData.getCurrentDate().format(dateTimeFormatter));

    }

    public void startingThreads() {

        Map<DestinationEntity, List<OrdersEntity>> ordersByDestination = new HashMap<>();

        ordersRepository.findByDeliveryDate(globalData.getCurrentDate().format(dateTimeFormatter)).forEach(ordersEntity -> {
            ordersByDestination.computeIfAbsent(ordersEntity.getDestination(), destination -> new ArrayList<>());
            ordersByDestination.get(ordersEntity.getDestination()).add(ordersEntity);
        });

        for (DestinationEntity destination : ordersByDestination.keySet()) {
            threadCourier(destination, ordersByDestination.get(destination));
            logger.info("delivering on " + destination.getName() + "on courier " + Thread.currentThread().getName());
        }
    }

    @Async(value = "taskExecutor")
    public void threadCourier(DestinationEntity destination, List<OrdersEntity> orders){

        for(OrdersEntity orderStatus: orders){
            if(orderStatus.getStatus().equals(StatusEnum.NEW)){
                orderStatus.setStatus(StatusEnum.DELIVERING);
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
    }
}
