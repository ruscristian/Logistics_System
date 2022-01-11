package sci.final_project.logistics_system.destination;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import sci.final_project.logistics_system.order.OrdersEntity;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "destinations")
public class DestinationEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name="name", unique = true)
    private String name;

    @Column(name="distance")
    private long distance;

    @JsonManagedReference
    @OneToMany
    @JoinColumn(name = "destination_id")
    private List<OrdersEntity> orders;
}
