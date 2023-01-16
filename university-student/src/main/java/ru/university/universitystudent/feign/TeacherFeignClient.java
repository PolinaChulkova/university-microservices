package ru.university.universitystudent.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.university.universityentity.model.Task;
import ru.university.universityentity.model.Teacher;
import ru.university.universityutils.exceptions.custom_exception.RestRuntimeException;

import java.util.Set;

@FeignClient(name = "university-teacher", url = "http://localhost:8765/university-teacher",
        fallback = TeacherFeignClientFallback.class)
public interface TeacherFeignClient {

    @GetMapping("/teacher/{teacherId}")
    ResponseEntity<Teacher> findTeacherById(@PathVariable Long teacherId);

    @GetMapping("/task/teacher")
    ResponseEntity<Task> getTeacherTask(@RequestParam("taskId") Long taskId,
                                        @RequestParam("teacherId") Long teacherId);

    @PostMapping("/task/add-answer")
    ResponseEntity<Set<Long>> addTaskAnswerToTask(@RequestParam("taskId") Long taskId,
                                                  @RequestParam("taskAnswerId") Long taskAnswerId);

    @PostMapping("/task/delete-answer")
    ResponseEntity<Set<Long>> deleteTaskAnswerFromTask(@RequestParam("taskId") Long taskId,
                                  @RequestParam("taskAnswerId") Long taskAnswerId);
}

@Component
class TeacherFeignClientFallback implements TeacherFeignClient {

    @Override
    public ResponseEntity<Teacher> findTeacherById(Long teacherId) {
        throw new RestRuntimeException("Нет доступа к преподавателю. Сервис \"teacher-university\" недоступен.");
    }

    @Override
    public ResponseEntity<Task> getTeacherTask(Long taskId, Long teacherId) {
        throw new RestRuntimeException("Нет доступа к заданиям. Сервис \"teacher-university\" недоступен.");
    }

    @Override
    public ResponseEntity<Set<Long>> addTaskAnswerToTask(Long taskId, Long taskAnswerId) {
        throw new RestRuntimeException("Нет доступа к ответам на задание. Сервис \"teacher-university\" недоступен.");
    }

    @Override
    public ResponseEntity<Set<Long>> deleteTaskAnswerFromTask(Long taskId, Long taskAnswerId) {
        throw new RestRuntimeException("Нет доступа к к ответам на задание. Сервис \"teacher-university\" недоступен.");
    }
}
