package ru.university.universityteacher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ru.university"})
@EnableEurekaClient
@EnableJpaRepositories(basePackages = {"ru.university.universityteacher"})
public class UniversityTeacherApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityTeacherApplication.class, args);
    }

}
