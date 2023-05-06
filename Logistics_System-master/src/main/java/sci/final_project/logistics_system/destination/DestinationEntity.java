package sci.final_project.logistics_system.destination;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.Hibernate;
import sci.final_project.logistics_system.order.OrdersEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
    @ToString.Exclude
    private List<OrdersEntity> orders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DestinationEntity that = (DestinationEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
