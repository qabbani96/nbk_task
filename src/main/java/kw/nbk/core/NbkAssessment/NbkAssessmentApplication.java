package kw.nbk.core.NbkAssessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NbkAssessmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(NbkAssessmentApplication.class, args);
	}

}
