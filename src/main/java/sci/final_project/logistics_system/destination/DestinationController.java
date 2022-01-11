package sci.final_project.logistics_system.destination;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/get")
    public List<DestinationEntity> getDestination(@RequestParam(required = false) Long destinationId) {
        if (destinationId == null) {
            return destinationService.getAllDestinations();
        } else {
            return destinationService.getDestination(destinationId);
        }
    }
}
