package sci.final_project.logistics_system.destination;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationRepository destinationRepository;
    private final DestinationService destinationService;

    public DestinationController(DestinationRepository destinationRepository, DestinationService destinationService) {
        this.destinationRepository = destinationRepository;
        this.destinationService = destinationService;
    }
    @GetMapping("/get")
    public List<DestinationEntity> getDestination() {
        return destinationService.getAllDestinations();
    }

    @GetMapping("/get/{destinationId}")
    public Optional<DestinationEntity> getDestinationById(@PathVariable Long destinationId){
        return destinationService.getDestination(destinationId);
    }

    @PostMapping("/add")
    public ResponseEntity<DestinationEntity> addDestination(@RequestBody DestinationEntity destination){
        return destinationService.addDestinations(destination);
    }

    @PutMapping("/update")
    public ResponseEntity<DestinationEntity> updateDestinationInfo(@RequestBody DestinationEntity destination){
        return destinationService.updateDestination(destination);
    }

    @DeleteMapping("/delete/{destinationId}")
    public ResponseEntity<DestinationEntity> deleteDestination(@PathVariable Long destinationId){
        return destinationService.deleteDestinationById(destinationId);
    }


}
