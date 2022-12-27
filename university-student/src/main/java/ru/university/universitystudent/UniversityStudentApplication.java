package ru.university.universitystudent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ru.university"})
@EnableEurekaClient
@EnableJpaRepositories(basePackages = {"ru.university.universitystudent"})
@EnableFeignClients
public class UniversityStudentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityStudentApplication.class, args);
    }

}
