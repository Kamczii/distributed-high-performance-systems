package pl.rsww.preference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PreferenceServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PreferenceServiceApplication.class, args);
	}
}
