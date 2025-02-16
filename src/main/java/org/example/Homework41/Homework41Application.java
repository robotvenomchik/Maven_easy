package org.example.Homework41;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Homework41Application {
    public static void main(String[] args) {
        SpringApplication.run(Homework41Application.class, args);
    }
}