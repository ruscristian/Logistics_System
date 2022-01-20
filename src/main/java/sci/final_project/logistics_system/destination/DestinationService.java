package sci.final_project.logistics_system.destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class DestinationService {

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    final DestinationRepository destinationRepository;
    private final Logger logger = LoggerFactory.getLogger(DestinationService.class);


    public List<DestinationEntity> getAllDestinations() {
        List<DestinationEntity> list = new ArrayList<>(destinationRepository.findAll());
        logger.info("All destinations returned.");
        return list;
    }

    public Optional<DestinationEntity> getDestination(Long destinationId) {
        Optional<DestinationEntity> destination = destinationRepository.findById(destinationId);
        if (destination.isPresent()) {
            logger.info("Destination with id " + destinationId + " is " + destination.get().getName());
        }
        else {
            logger.info("Destination id does not exist.");
        }
        return destinationRepository.findById(destinationId);

    }

    public ResponseEntity<DestinationEntity> addDestinations(DestinationEntity destinationToAdd) {
        if (destinationRepository.findByName(destinationToAdd.getName()).isEmpty() &&
                destinationRepository.findAllById(Collections.singleton(destinationToAdd.getId())).isEmpty()) {
            destinationRepository.save(destinationToAdd);
            logger.info("Destination " + destinationToAdd.getName() + " added.");
            return new ResponseEntity<>(destinationToAdd, HttpStatus.CREATED);
        }else {
            logger.info("Destination and id name already exists.");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity<DestinationEntity> updateDestination(DestinationEntity destinationToUpdate) {
        if (destinationRepository.findByName(destinationToUpdate.getName()).isEmpty()) {
            logger.info("Provided destination does not exist");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            DestinationEntity destinationEntity = destinationRepository.getById(destinationToUpdate.getId());
            destinationEntity.setDistance(destinationToUpdate.getDistance());
            destinationRepository.save(destinationEntity);
            logger.info("Destination found and updated");
            return new ResponseEntity<>(destinationEntity, HttpStatus.OK);
// trebuie introdus cu id
        }
    }

    public ResponseEntity<DestinationEntity> deleteDestinationById(Long destinationId) {
        if (destinationRepository.findById(destinationId).isPresent()) {
            DestinationEntity destinationEntity = destinationRepository.getById(destinationId);
            destinationRepository.delete(destinationEntity);
            logger.info("Destination " + destinationEntity.getName() + " has been deleted.");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            logger.info("Provided ID does not exist.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }



}
