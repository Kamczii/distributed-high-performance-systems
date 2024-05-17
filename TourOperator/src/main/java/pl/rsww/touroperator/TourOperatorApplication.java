package pl.rsww.touroperator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TourOperatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourOperatorApplication.class, args);
	}

}
