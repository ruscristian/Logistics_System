package sci.final_project.logistics_system.shipping;

import org.springframework.stereotype.Service;
import sci.final_project.logistics_system.GlobalData;
import sci.final_project.logistics_system.destination.DestinationRepository;
import sci.final_project.logistics_system.order.OrdersRepository;


@Service
public class ShippingService {

    private final DestinationRepository destinationRepository;
    private final OrdersRepository ordersRepository;
    private final GlobalData globalData;

    public ShippingService(DestinationRepository destinationRepository, OrdersRepository ordersRepository, GlobalData globalData) {
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

    // executor

    //     Se va folosi un executor cu maximum 4 threaduri active
    //     si un queue size de 100 de taskuri pentru a gestiona livrarile.



    public void newDayMaker(){
        globalData.setCurrentDate(globalData.getCurrentDate().plusDays(1));
        //TODO status comanda
    }

    public void threadCourier(){

    }

 


}
