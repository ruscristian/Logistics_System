package sci.final_project.logistics_system.destination;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    final DestinationService destinationService;
    final DestinationRepository destinationRepository;

    public DestinationController(DestinationService destinationService, DestinationRepository destinationRepository) {
        this.destinationService = destinationService;
        this.destinationRepository = destinationRepository;
    }

    @GetMapping("/get")
    public List<DestinationEntity> getDestination() {
        return destinationRepository.findAll();
    }

    @GetMapping("/get/{destinationId}")
    public Optional<DestinationEntity> getDestinationById(@PathVariable Long destinationId){
        return destinationRepository.findById(destinationId);
    }

    @PostMapping("/add")
    public DestinationEntity addDestination(@RequestBody DestinationEntity destination){
        return destinationRepository.save(destination);
    }

    @PutMapping("/update")
    public DestinationEntity updateDestinationInfo(@RequestBody DestinationEntity destination){

        return destinationRepository.save(destination);
    }

    @DeleteMapping("/delete/{destinationId}")
    public void deleteDestination(@PathVariable Long destinationId){
        destinationRepository.deleteById(destinationId);
    }
}
