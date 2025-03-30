package com.example.locking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OptimisticLockingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimisticLockingApplication.class, args);
	}

}
