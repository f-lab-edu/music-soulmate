package com.flab.musolmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MusolmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusolmateApplication.class, args);
	}

}
