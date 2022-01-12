package sci.final_project.logistics_system.destination;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DestinationService {

    public DestinationService(DestinationRepository destinationRepository, List<DestinationEntity> destinationsList) {
        this.destinationRepository = destinationRepository;
        this.destinationsList = getAllDestinations();
    }

    final DestinationRepository destinationRepository;
    final List<DestinationEntity> destinationsList;


    public List<DestinationEntity> getAllDestinations() {
        List<DestinationEntity> list = new ArrayList<>();
        for (DestinationEntity destinationEntity : destinationRepository.findAll()) {
            list.add(destinationEntity);
        }
        return list;
    }

    public List<DestinationEntity> getDestination(Long destinationId) {
        List<DestinationEntity> list = new ArrayList<>();
        for (DestinationEntity destinationEntity : destinationRepository.findAll()) {
            if (destinationEntity.getId().equals(destinationId)) {
                list.add(destinationEntity);
            }
        }
            return list;
    }

    public List<DestinationEntity> getDestinationsList() {
        return destinationsList;
    }
}
