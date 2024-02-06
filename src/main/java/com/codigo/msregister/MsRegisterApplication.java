package com.codigo.msregister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.codigo.*")
public class MsRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRegisterApplication.class, args);
	}

}
