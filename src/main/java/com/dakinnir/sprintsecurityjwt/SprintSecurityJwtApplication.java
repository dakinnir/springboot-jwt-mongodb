package com.dakinnir.sprintsecurityjwt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class SprintSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprintSecurityJwtApplication.class, args);
    }

    /**
    @Bean
    CommandLineRunner test(MongoTemplate mongoTemplate) {
        return args -> {
            System.out.println("Connected DB: " + mongoTemplate.getDb().getName());
        };
    }
*/
}
