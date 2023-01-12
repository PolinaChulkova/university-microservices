package ru.university.universitystudent.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.university.universitystudent.dto.CreateTaskAnswerDTO;
import ru.university.universitystudent.feign.TeacherFeignClient;
import ru.university.universitystudent.service.TaskAnswerService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/task-answer")
@AllArgsConstructor
@Slf4j
public class TaskAnswerController {

    private final TaskAnswerService taskAnswerService;
    private final TeacherFeignClient teacherFeignClient;

    @GetMapping("/for-student/{studentId}/{taskId}")
    public ResponseEntity<?> getTaskAnswerForStudent(@PathVariable Long studentId,
                                                     @PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskAnswerService.getTaskAnswerForStudent(taskId, studentId));
    }

    @GetMapping("/for-teacher/{teacherId}/{taskId}")
    public ResponseEntity<?> getTaskAnswersForTeacher(@PathVariable Long teacherId,
                                                      @PathVariable Long taskId) {
        return ResponseEntity.ok().body(
                teacherFeignClient.getTeacherTask(taskId, teacherId).getBody()
                        .getTaskAnswersId()
                        .stream()
                        .map(taskAnswerService::findTaskAnswerById)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/send")
    private ResponseEntity<?> sendTaskAnswer(@RequestBody CreateTaskAnswerDTO dto) {
        return ResponseEntity.ok().body(taskAnswerService.sendTaskAnswer(dto));
    }

    @PostMapping("/upload/{taskAnswerId}")
    public ResponseEntity<?> uploadFilesToTaskAnswer(@PathVariable Long taskAnswerId,
                                                     @RequestParam MultipartFile[] files) {
        return ResponseEntity.ok().body(taskAnswerService.uploadFilesToAnswer(taskAnswerId, files));
    }

    @PutMapping("/update/{taskAnswerId}")
    public ResponseEntity<?> updateTaskAnswer(@PathVariable Long taskAnswerId,
                                              @RequestBody String comment) {
        return ResponseEntity.ok().body(taskAnswerService.updateTaskAnswer(taskAnswerId, comment));
    }

    @GetMapping("/{taskAnswerId}")
    public ResponseEntity<?> findTaskAnswerById(@PathVariable Long taskAnswerId) {
        return ResponseEntity.ok().body(taskAnswerService.findTaskAnswerById(taskAnswerId));
    }

    @DeleteMapping("/delete-file/{taskAnswerId}")
    public ResponseEntity<?> deleteFileFromTaskAnswer(@PathVariable Long taskAnswerId,
                                                      @RequestParam("file-uri") String fileUri) {
        return ResponseEntity.ok().body(taskAnswerService.deleteFileFromAnswer(taskAnswerId, fileUri));
    }

    @DeleteMapping("/delete/{taskAnswerId}/{studentId}")
    public ResponseEntity<?> deleteTaskAnswer(@PathVariable Long taskAnswerId,
                                              @PathVariable Long studentId) {
        return ResponseEntity.ok().body(taskAnswerService.deleteTaskAnswer(taskAnswerId, studentId));
    }
}