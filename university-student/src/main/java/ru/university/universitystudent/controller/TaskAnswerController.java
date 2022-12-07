package ru.university.universitystudent.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.university.universityentity.model.TaskAnswer;
import ru.university.universitystudent.dto.CreateTaskAnswerDTO;
import ru.university.universitystudent.dto.MessageResponse;
import ru.university.universitystudent.service.TaskAnswerService;

@RestController
@RequestMapping("/task-answer")
@AllArgsConstructor
@Slf4j
public class TaskAnswerController {

    private final TaskAnswerService taskAnswerService;
//    private final AmqpTemplate amqpTemplate;

    @GetMapping("/for-student/{studentId}/{taskId}")
    public ResponseEntity<?> getTaskAnswerForStudent(@PathVariable Long studentId,
                                                     @PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskAnswerService.getTaskAnswerForStudent(taskId, studentId));
    }

//    @GetMapping("/for-teacher/{teacherId}/{taskId}")
//    public ResponseEntity<?> getTaskAnswersForTeacher(@PathVariable Long teacherId,
//                                                      @PathVariable Long taskId) {
//        return ResponseEntity.ok().body(taskAnswerService.getTaskAnswersForTeacher(taskId, teacherId));
//    }

    @PostMapping("/send")
    private ResponseEntity<?> sendTaskAnswer(@RequestBody CreateTaskAnswerDTO dto) {
        try {
            TaskAnswer answer = taskAnswerService.sendTaskAnswer(dto);
//            amqpTemplate.convertAndSend("teacherQueue", answer.getStudent().getFullName()
//                    + "  из группы \"" + answer.getStudent().getGroup()
//                    + " прислал(-а) задание на проверку по предмету");
            return ResponseEntity.ok().body(answer);

        } catch (RuntimeException e) {
            log.error("Не удалось отправить ответ на задание. Error: " + e.getLocalizedMessage());

            return ResponseEntity.internalServerError().body("Не удалось отправить ответ на задание. Error: "
                    + e.getLocalizedMessage());
        }
    }

    @PostMapping("/upload/{taskAnswerId}")
    public ResponseEntity<?> uploadFilesToTaskAnswer(@PathVariable Long taskAnswerId,
                                                     @RequestParam MultipartFile[] files) {
        try {
            taskAnswerService.uploadFilesToAnswer(taskAnswerId, files);
            return ResponseEntity.ok().body(new MessageResponse("Файлы загружены"));

        } catch (RuntimeException e) {
            log.error("Не удалось загрузить файлы к ответу на задание с id=" + taskAnswerId
                    + " Error: " + e.getLocalizedMessage());

            return ResponseEntity.internalServerError().body(new MessageResponse("Не удалось загрузить файлы"));
        }
    }

    @PutMapping("/update/{taskAnswerId}")
    public ResponseEntity<?> updateTaskAnswer(@PathVariable Long taskAnswerId,
                                              @RequestBody String comment) {
        try {
            taskAnswerService.updateTaskAnswer(taskAnswerId, comment);
            return ResponseEntity.ok().body(new MessageResponse("Вы обновили свой ответ на задние"));

        } catch (RuntimeException e) {
            log.error("Ваш ответ не обновлён. Error: " + e.getLocalizedMessage());

            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Не удалось обновить ответ на задние. Error: "
                            + e.getLocalizedMessage()));
        }
    }

    @GetMapping("/{taskAnswerId}")
    public ResponseEntity<?> findTaskAnswerById(@PathVariable Long taskAnswerId) {
        return ResponseEntity.ok().body(taskAnswerService.findTaskAnswerById(taskAnswerId));
    }

    @DeleteMapping("/delete-file/{taskAnswerId}")
    public ResponseEntity<?> deleteFileFromTaskAnswer(@PathVariable Long taskAnswerId,
                                                      @RequestParam("file-uri") String fileUri) {
        try {
            taskAnswerService.deleteFileFromAnswer(taskAnswerId, fileUri);
            return ResponseEntity.ok().body(new MessageResponse("Файл удалён из ответа на задание"));

        } catch (RuntimeException e) {
            log.error("Не удалось удалить файл с uri: " + fileUri + " Error: " + e.getLocalizedMessage());

            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Не удалось удалить файл из ответа на задание. Error: "
                            + e.getLocalizedMessage()));
        }
    }

    @DeleteMapping("/delete/{taskAnswerId}/{studentId}")
    public ResponseEntity<?> deleteTaskAnswer(@PathVariable Long taskAnswerId,
                                              @PathVariable Long studentId) {
        try {
            taskAnswerService.deleteTaskAnswer(taskAnswerId, studentId);
            return ResponseEntity.ok().body(new MessageResponse("Ответ на задание удалён"));

        } catch (RuntimeException e) {
            log.error("Не удалось удалить ответ на задание с id=" + taskAnswerId + " Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Не удалось удалить ответ на задание. Error: "
                            + e.getLocalizedMessage()));
        }
    }
}