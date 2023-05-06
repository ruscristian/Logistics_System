package sci.final_project.logistics_system;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class GlobalActuator implements InfoContributor {

        private final GlobalData globalData;

    public GlobalActuator(GlobalData globalData) {
        this.globalData = globalData;
    }

    @Override
        public void contribute(Info.Builder builder) {
            Map<String, String> stats = new HashMap<>();
        stats.put("date", globalData.getCurrentDate().format(globalData.getDateTimeFormatter()));
        stats.put("profit", String.valueOf(globalData.getProfit()));

        builder.withDetail("actuator", stats);
        }
}