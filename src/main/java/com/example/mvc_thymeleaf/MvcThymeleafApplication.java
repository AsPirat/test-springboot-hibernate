package com.example.mvc_thymeleaf;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@EnableTransactionManagement
@SpringBootApplication
public class MvcThymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcThymeleafApplication.class, args);
	}
}
