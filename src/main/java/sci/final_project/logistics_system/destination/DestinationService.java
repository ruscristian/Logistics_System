package sci.final_project.logistics_system.destination;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DestinationService {

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    final DestinationRepository destinationRepository;



    public List<DestinationEntity> getAllDestinations() {
        List<DestinationEntity> list = new ArrayList<>();
        list.addAll(destinationRepository.findAll());
        return list;
    }

//    public List<DestinationEntity> getDestination(Long destinationId) {
//        List<DestinationEntity> list = new ArrayList<>();
//        for (DestinationEntity destinationEntity : destinationRepository.findAll()) {
//            if (destinationEntity.getId().equals(destinationId)) {
//                list.add(destinationEntity);
//            }
//        }
//        //aici cred ca merge si metoda findById din repository
//
//            return list;
//    }

//    public ResponseEntity<DestinationEntity> updateStudent(DestinationEntity payload)
//    {
//        if (payload.getId() == null) {
//            studentInfoContributor.incrementNoOfFailedStudentUpdates();
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        if (studentRepository.findById(payload.getId()).isPresent()) {
//            StudentEntity savedEntity = studentRepository.save(StudentConverter.fromStudentDto(payload));
//            StudentDto studentDto = StudentConverter.fromStudentEntity(savedEntity);
//            return new ResponseEntity<>(studentDto, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

}
