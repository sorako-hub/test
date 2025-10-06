package com.example.atsumori;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.atsumori.repository")  
public class AtsumoriApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtsumoriApplication.class, args);
	}

}
