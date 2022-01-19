package sci.final_project.logistics_system.destination;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface DestinationRepository extends JpaRepository<DestinationEntity, Long> {

    DestinationEntity findByName(String name);

}
