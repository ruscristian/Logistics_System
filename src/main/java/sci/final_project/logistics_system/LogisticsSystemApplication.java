package sci.final_project.logistics_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.jdbc.Sql;

@SpringBootApplication
@Sql({"src/main/resources/data.sql"})
public class LogisticsSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogisticsSystemApplication.class, args);
	}


}
