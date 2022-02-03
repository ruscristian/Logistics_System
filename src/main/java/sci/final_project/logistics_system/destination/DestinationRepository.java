package sci.final_project.logistics_system.destination;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DestinationRepository extends JpaRepository<DestinationEntity, Long> {

    DestinationEntity findDestinationByName(String name);
    Optional<DestinationEntity> findById(Long id);
    List<DestinationEntity> findByName(String name);


}
