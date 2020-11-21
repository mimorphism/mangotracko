package com.mimorphism.mangotracko;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.mimorphism.mangotracko.controller.MangoController;
import com.mimorphism.mangotracko.model.Mango;
import com.mimorphism.mangotracko.persistence.MangoRepo;


@EnableJpaRepositories("com.mimorphism.mangotracko.persistence") 
@EntityScan("com.mimorphism.mangotracko.model")
@ComponentScan(basePackageClasses=MangoController.class)
@SpringBootApplication
public class MangotrackoApplication {

	
	private static final Logger log = LoggerFactory.getLogger(MangotrackoApplication.class);
	
	
	public static void main(String[] args) {
		SpringApplication.run(MangotrackoApplication.class, args);
	}
	
	@Bean
	  public CommandLineRunner demo(MangoRepo repository) {
	    return (args) -> {
	      // save a few customers
//	      repository.save(new Mango("Naruto", "Kishimoto Masashi", 700, 400, System.currentTimeMillis(), System.currentTimeMillis(), "SOGOOD"));
//	      repository.save(new Customer("Chloe", "O'Brian"));
//	      repository.save(new Customer("Kim", "Bauer"));
//	      repository.save(new Customer("David", "Palmer"));
//	      repository.save(new Customer("Michelle", "Dessler"));

	      // fetch all customers
//	      log.info("Customers found with findAll():");
//	      log.info("-------------------------------");
//	      for (Customer customer : repository.findAll()) {
//	        log.info(customer.toString());
//	      }
//	      log.info("");
//
	      // fetch an individual customer by ID
//	      Mango mango = repository.findByMangoTitle("Naruto");
		  Mango mango = repository.findByMangoTitle("Naruto");

	      log.info("Mango found with title:");
	      log.info("--------------------------------");
	      log.info(mango.getAuthor());
	      log.info("");
//
//	      // fetch customers by last name
//	      log.info("Customer found with findByLastName('Bauer'):");
//	      log.info("--------------------------------------------");
//	      repository.findByLastName("Bauer").forEach(bauer -> {
//	        log.info(bauer.toString());
//	      });
//	      // for (Customer bauer : repository.findByLastName("Bauer")) {
//	      //  log.info(bauer.toString());
//	      // }
//	      log.info("");
	    };
	  }

}
