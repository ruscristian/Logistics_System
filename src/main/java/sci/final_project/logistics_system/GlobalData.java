package sci.final_project.logistics_system;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
public class GlobalData {

    private long profit;
    private LocalDate currentDate = LocalDate.of(2021, 12, 15);

}
