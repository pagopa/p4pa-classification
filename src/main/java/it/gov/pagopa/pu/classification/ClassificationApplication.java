package it.gov.pagopa.pu.classification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class ClassificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassificationApplication.class, args);
	}

}
