package com.example.citaPeluqueria;

import com.example.citaPeluqueria.repositories.ServiceRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@EnableScheduling
@SpringBootApplication
public class CitaPeluqueriaApplication {
	public static void main(String[] args) {
		SpringApplication.run(CitaPeluqueriaApplication.class, args);
	}

}
