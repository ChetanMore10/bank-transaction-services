package com.banktransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankTransactionServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankTransactionServicesApplication.class, args);
        System.err.println("Bank Transaction Started Successfully..!");
	}
}