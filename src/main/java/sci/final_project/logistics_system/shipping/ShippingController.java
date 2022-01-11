package sci.final_project.logistics_system.shipping;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sci.final_project.logistics_system.GlobalData;


@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private final GlobalData globalData;
    private final ShippingService shippingService;

    public ShippingController(GlobalData globalData, ShippingService shippingService) {
        this.globalData = globalData;
        this.shippingService = shippingService;
    }

    @GetMapping("/date")
    public String showDate(){
        return globalData.getCurrentDate().toString();
    }

    @PostMapping("/new-day")
    public void newDay(){
        shippingService.newDayMaker();
    }


}
