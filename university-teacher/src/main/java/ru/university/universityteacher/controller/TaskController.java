package ru.university.universityteacher.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.university.universityentity.model.Task;
import ru.university.universityteacher.dto.CreateTaskDTO;
import ru.university.universityteacher.dto.MessageResponse;
import ru.university.universityteacher.dto.UpdateTaskDTO;
import ru.university.universityteacher.feign.StudentFeignClient;
import ru.university.universityteacher.service.TaskService;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;
    private final StudentFeignClient studentFeignClient;

//    private final AmqpTemplate template;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentTasks(@PathVariable Long studentId) {
        try {
            return ResponseEntity.ok().body(
                    studentFeignClient.findStudentById(studentId).getBody().getGroup()
                            .getTasksId()
                            .stream()
                            .map(taskService::findTaskById)
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            log.error("Нельзя получить задания студента с id = " + studentId
                    + ". Error: " + Arrays.toString(e.getStackTrace()));

            return ResponseEntity.badRequest().body(new MessageResponse("Нельзя получить задания студента."
                    + " Error: " + Arrays.toString(e.getStackTrace())));
        }
    }

    @GetMapping("/subject")
    public ResponseEntity<?> getAllTaskBySubjectForStudent(@RequestParam("subjectId") Long subjectId,
                                                           @RequestParam("studentId") Long studentId) {
        try {
            return ResponseEntity.ok().body(
                    taskService.findTasksBySubjectIdAndGroupId(
                            subjectId,
                            studentFeignClient.findStudentById(studentId).getBody()
                                    .getGroup().getId()));

        } catch (RuntimeException e) {
            log.error("Задания для студента с id = " + subjectId + " по предмету с id = "
                    + subjectId + " не найдены. Error: "
                    + Arrays.toString(e.getStackTrace()));

            return ResponseEntity.badRequest().body(new MessageResponse("Задания не найдены. Error: "
                    + Arrays.toString(e.getStackTrace())));
        }
    }

    @GetMapping("/teacher")
    public ResponseEntity<?> getTeacherTask(@RequestParam("taskId") Long taskId,
                                            @RequestParam("teacherId") Long teacherId) {
        try {
            return ResponseEntity.ok().body(taskService.findTaskByIdForTeacher(taskId, teacherId));

        } catch (Exception e) {
            log.error("Задание с id = " + taskId + " для преподавателя с id = "
                    + teacherId + " не найдены. Error: "
                    + Arrays.toString(e.getStackTrace()));

            return ResponseEntity.badRequest().body(new MessageResponse("Задание не найдено. Error: "
                    + Arrays.toString(e.getStackTrace())));
        }
    }

    @GetMapping("/all-for-teacher/{teacherId}/{page}")
    public ResponseEntity<?> getAllTeacherTasks(@PathVariable Long teacherId,
                                                @PathVariable int page) {
        try {
            Pageable pageable = PageRequest.of(page, 5);
            return ResponseEntity.ok().body(taskService.findAllTeacherTasks(teacherId, pageable));

        } catch (Exception e) {
            log.error("Задания для преподавателя с id = " + teacherId + " не найдены. Error: "
                    + Arrays.toString(e.getStackTrace()));

            return ResponseEntity.badRequest().body(new MessageResponse("Задания не найдены. Error: "
                    + Arrays.toString(e.getStackTrace())));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDTO dto) {
        try {
            Task task = taskService.createTask(dto);
            studentFeignClient.addTaskToGroup(dto.getGroupId(), task.getId());
//            template.convertAndSend("studentQueue", "Создано новое задание по предмету \""
//                    + task.getName() + "\"");
            return ResponseEntity.ok().body(task);

        } catch (RuntimeException e) {
            log.error("Задание " + dto.getName() + " не создано. Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Задание не создано. Error: "
                    + e.getLocalizedMessage()));
        }
    }

    @PostMapping("/upload/{taskId}")
    public ResponseEntity<?> uploadFilesToTask(@PathVariable Long taskId,
                                               @RequestParam("files") MultipartFile[] files) {
        try {
            taskService.uploadFilesToTask(taskId, files);
            return ResponseEntity.ok().body(new MessageResponse("Файлы загружены"));

        } catch (RuntimeException e) {
            log.error("Не удалось загрузить файлы к заданию с id=" + taskId
                    + " Error:" + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Не удалось загрузить файлы." +
                    "Error: " + e.getLocalizedMessage()));
        }
    }

    @DeleteMapping("/delete-file/{taskId}")
    public ResponseEntity<?> deleteFileFromTask(@PathVariable Long taskId,
                                                @RequestParam("file-uri") String fileUri) {
        try {
            taskService.deleteFileFromTask(taskId, fileUri);
            return ResponseEntity.ok().body(new MessageResponse("Файл удалён из задания"));

        } catch (RuntimeException e) {
            log.error("Не удалось удалить файл с uri: " + fileUri + " Error: " + e.getLocalizedMessage());

            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Не удалось удалить файл из задания. Error: "
                            + e.getLocalizedMessage()));
        }
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<?> updateTeacherTask(@PathVariable Long taskId,
                                               @RequestBody UpdateTaskDTO dto) {
        try {
            return ResponseEntity.ok().body(taskService.updateTask(taskId, dto));

        } catch (RuntimeException e) {
            log.error("Задание " + dto.getName() + " не обновлёно. Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.ok().body(new MessageResponse("Задание " + dto.getName()
                    + " не обновлено. Error: " + e.getLocalizedMessage()));
        }
    }

    //    для админа
    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskService.findTaskById(taskId));
    }

    //    для админа
    @DeleteMapping("/delete-from-groups")
    public ResponseEntity<?> deleteTask(@RequestParam("taskId") Long taskId,
                                        @RequestParam("groupId") Long groupId) {
        taskService.deleteTaskById(taskId);
        studentFeignClient.deleteTaskFromGroup(groupId, taskId);
        return ResponseEntity.ok().body(new MessageResponse("Задание с id=" + taskId + " удалено."));
    }
}
