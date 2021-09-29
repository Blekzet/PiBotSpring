package ru.blekzet.pibot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class PiBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiBotApplication.class, args);
	}

}
