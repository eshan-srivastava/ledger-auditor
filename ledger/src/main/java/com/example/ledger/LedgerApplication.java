package com.example.ledger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.ledger.repository.TransactionRepo;

@SpringBootApplication
public class LedgerApplication {

	private static final Logger logger = LoggerFactory.getLogger(LedgerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LedgerApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TransactionRepo repository) {
		return (args) -> {
			// demoing
			logger.info("null");
		};
	}
}
