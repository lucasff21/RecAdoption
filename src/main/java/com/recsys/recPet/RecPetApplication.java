package com.recsys.recPet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.recsys.recPet.repository")
@EntityScan(basePackages = "com.recsys.recPet.model")
public class RecPetApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecPetApplication.class, args);
	}

}
