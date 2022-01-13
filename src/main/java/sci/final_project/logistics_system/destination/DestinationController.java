package sci.final_project.logistics_system.destination;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    final DestinationService destinationService;
    //nu sunt sigur daca e corect sa folosesc repository-ul
    final DestinationRepository destinationRepository;

    public DestinationController(DestinationService destinationService, DestinationRepository destinationRepository) {
        this.destinationService = destinationService;
        this.destinationRepository = destinationRepository;
    }

    @GetMapping("/get")
    public List<DestinationEntity> getDestination() {
        return destinationService.getAllDestinations();
    }
    @GetMapping("/get/{destinationId}")
    public List<DestinationEntity> getDestinationById(@PathVariable Long destinationId){
        return destinationService.getDestination(destinationId);
    }

    @PostMapping("/add")
    public DestinationEntity addDestination(@RequestBody DestinationEntity destination){
        return destinationRepository.save(destination);
    }

    @PutMapping("/update")
    public DestinationService updateDestinationInfo(){
        return null;
    }

    @DeleteMapping("/delete/{destinationId}")
    public DestinationEntity deleteDestination(@PathVariable Long destinationId){
        return null;
    }
}
