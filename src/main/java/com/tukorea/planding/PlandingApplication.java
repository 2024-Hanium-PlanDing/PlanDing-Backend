package com.tukorea.planding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class PlandingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlandingApplication.class, args);
	}

}
