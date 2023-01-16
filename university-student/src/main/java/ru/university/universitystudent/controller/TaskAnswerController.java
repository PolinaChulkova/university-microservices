package ru.university.universitystudent.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.university.universityentity.model.TaskAnswer;
import ru.university.universitystudent.dto.CreateTaskAnswerDTO;
import ru.university.universitystudent.feign.TeacherFeignClient;
import ru.university.universitystudent.service.TaskAnswerService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task-answer")
@AllArgsConstructor
@Slf4j
public class TaskAnswerController {

    private final TaskAnswerService taskAnswerService;
    private final TeacherFeignClient teacherFeignClient;

    @GetMapping("/for-student")
    public ResponseEntity<?> getTaskAnswerForStudent(@RequestParam("studentId") Long studentId,
                                                     @RequestParam("taskId") Long taskId) {
        return ResponseEntity.ok().body(taskAnswerService.getTaskAnswerForStudent(taskId, studentId));
    }

    @GetMapping("/for-teacher")
    public ResponseEntity<?> getTaskAnswersForTeacher(@RequestParam("teacherId") Long teacherId,
                                                      @RequestParam("taskId") Long taskId) {
        return ResponseEntity.ok().body(taskAnswerService.getTaskAnswersForTeacher(teacherId, taskId));
    }

    @PostMapping("/send")
    private ResponseEntity<?> sendTaskAnswer(@RequestBody CreateTaskAnswerDTO dto) {
        TaskAnswer taskAnswer = taskAnswerService.sendTaskAnswer(dto);
        return ResponseEntity.ok().body(taskAnswer);
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadTaskFromTaskAnswer(@RequestParam("taskAnswerId") Long taskAnswerId,
                                                        @RequestParam("filename") String filename) {
        return ResponseEntity.ok().body(taskAnswerService.downloadFileFromAnswer(taskAnswerId, filename));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFilesToTaskAnswer(@RequestParam("taskAnswerId") Long taskAnswerId,
                                                     @RequestParam("files") MultipartFile[] files) {
        return ResponseEntity.ok().body(taskAnswerService.uploadFilesToAnswer(taskAnswerId, files));
    }

    @PostMapping("/delete-file")
    public ResponseEntity<?> deleteFileFromTaskAnswer(@RequestParam("taskAnswerId") Long taskAnswerId,
                                                      @RequestParam("filename") String filename) {
        return ResponseEntity.ok().body(taskAnswerService.deleteFileFromAnswer(taskAnswerId, filename));
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTaskAnswer(@RequestParam("taskAnswerId") Long taskAnswerId,
                                              @RequestParam("studentId") Long studentId) {
        TaskAnswer taskAnswer = taskAnswerService.deleteTaskAnswer(taskAnswerId, studentId);
        teacherFeignClient.deleteTaskAnswerFromTask(taskAnswer.getTaskId(), taskAnswerId);
        return ResponseEntity.ok().body(taskAnswer);
    }
}