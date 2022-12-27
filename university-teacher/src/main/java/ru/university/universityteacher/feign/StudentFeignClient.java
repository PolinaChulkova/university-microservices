package ru.university.universityteacher.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.university.universityentity.model.Student;

@FeignClient(name = "university-student")
public interface StudentFeignClient {

    @GetMapping("/student/{studentId}")
    ResponseEntity<Student> findStudentById(@PathVariable Long studentId);
}
