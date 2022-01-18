package sci.final_project.logistics_system;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Component
public class GlobalData {

    private long profit;
    private LocalDate currentDate = LocalDate.of(2021, 12, 15);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");



    public synchronized void increaseProfit(long amount) {
        profit += amount;
    }
}
