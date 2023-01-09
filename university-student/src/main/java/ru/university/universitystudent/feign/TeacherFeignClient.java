package ru.university.universitystudent.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.university.universityentity.model.Task;
import ru.university.universityentity.model.Teacher;

@FeignClient(name = "university-teacher", url = "http://localhost:8765/university-teacher")
//        fallback = TeacherFeignClientFallback.class)
public interface TeacherFeignClient {

    @GetMapping("/teacher/{teacherId}")
    ResponseEntity<Teacher> findTeacherById(@PathVariable Long teacherId);

    @GetMapping("/task/teacher")
    ResponseEntity<Task> getTeacherTask(@RequestParam("taskId") Long taskId,
                                        @RequestParam("teacherId") Long teacherId);
}

//@Component
//class TeacherFeignClientFallback implements TeacherFeignClient {
//
//    @Override
//    public ResponseEntity<Teacher> findTeacherById(Long teacherId) {
//        return new ResponseEntity<Teacher>(new Teacher(), HttpStatus.NOT_FOUND);
//    }
//
//    @Override
//    public ResponseEntity<Task> getTeacherTask(Long taskId, Long teacherId) {
//        throw new ServiceIsUnavailable();
//    }
//}
