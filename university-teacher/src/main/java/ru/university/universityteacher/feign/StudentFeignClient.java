package ru.university.universityteacher.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.university.universityentity.model.Student;

import java.util.Set;

@FeignClient(name = "university-student")
public interface StudentFeignClient {

    @GetMapping("/student/{studentId}")
    ResponseEntity<Student> findStudentById(@PathVariable Long studentId);

    @PostMapping("/group/add-task")
    ResponseEntity<Set<Long>> addTaskToGroup(@RequestParam("groupId") Long groupId,
                                             @RequestParam("taskId") Long taskId);
    @PostMapping("/group/delete-task")
    ResponseEntity<?> deleteTaskFromGroup(@RequestParam("groupId") Long groupId,
                             @RequestParam("taskId") Long taskId);
    @PostMapping("/group/add-subject")
    void addSubjectToGroup(@RequestParam("groupId") Long groupId,
                           @RequestParam("subjectId") Long subjectId);
    @PostMapping("/group/detach-subject")
    void detachSubjectFromGroup(@RequestParam("groupId") Long groupId,
                           @RequestParam("subjectId") Long subjectId);
}
