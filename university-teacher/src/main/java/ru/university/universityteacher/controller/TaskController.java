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
import ru.university.universityteacher.dto.TaskNotificationDTO;
import ru.university.universityteacher.dto.UpdateTaskDTO;
import ru.university.universityteacher.feign.StudentFeignClient;
import ru.university.universityteacher.mq.func.MessageService;
import ru.university.universityteacher.service.TaskService;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final StudentFeignClient studentFeignClient;
    private final MessageService messageService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentTasks(@PathVariable Long studentId) {
        return ResponseEntity.ok().body(
                studentFeignClient.findStudentById(studentId).getBody().getGroup()
                        .getTasksId()
                        .stream()
                        .map(taskService::findTaskById)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/subject")
    public ResponseEntity<?> getAllTaskBySubjectForStudent(@RequestParam("subjectId") Long subjectId,
                                                           @RequestParam("studentId") Long studentId) {
        return ResponseEntity.ok().body(
                taskService.findTasksBySubjectIdAndGroupId(
                        subjectId,
                        Objects.requireNonNull(studentFeignClient.findStudentById(studentId).getBody())
                                .getGroup().getId()));
    }

    @GetMapping("/teacher")
    public ResponseEntity<?> getTeacherTask(@RequestParam("taskId") Long taskId,
                                            @RequestParam("teacherId") Long teacherId) {
        return ResponseEntity.ok().body(taskService.findTaskByIdForTeacher(taskId, teacherId));
    }

    @GetMapping("/all-for-teacher/{teacherId}/{page}")
    public ResponseEntity<?> getAllTeacherTasks(@PathVariable Long teacherId,
                                                @PathVariable int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return ResponseEntity.ok().body(taskService.findAllTeacherTasks(teacherId, pageable));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDTO createTaskDTO) {
        Task task = taskService.createTask(createTaskDTO);
//            studentFeignClient.addTaskToGroup(createTaskDTO.getGroupId(), task.getId());

        messageService.createNewTaskMessage(new TaskNotificationDTO(
                task.getId(),
                createTaskDTO.getSubjectId(),
                createTaskDTO.getGroupId()));

        return ResponseEntity.ok().body(task);
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFileFromTask(@RequestParam("taskId") Long taskId,
                                                  @RequestParam("filename") String filename) {
        return ResponseEntity.ok().body(taskService.downloadFileFromTask(taskId, filename));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFilesToTask(@RequestParam("taskId") Long taskId,
                                               @RequestParam("files") MultipartFile[] files) {
        return ResponseEntity.ok().body(taskService.uploadFilesToTask(taskId, files));
    }

    @PostMapping("/delete-file")
    public ResponseEntity<?> deleteFileFromTask(@RequestParam("taskId") Long taskId,
                                                @RequestParam("filename") String filename) {
        return ResponseEntity.ok().body(taskService.deleteFileFromTask(taskId, filename));
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<?> updateTeacherTask(@PathVariable Long taskId,
                                               @RequestBody UpdateTaskDTO dto) {
        return ResponseEntity.ok().body(taskService.updateTask(taskId, dto));
    }

    //    для админа
    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskService.findTaskById(taskId));
    }

    @PostMapping("/add-answer")
    public ResponseEntity<?> addTaskAnswerToTask(@RequestParam("taskId") Long taskId,
                                                 @RequestParam("taskAnswerId") Long taskAnswerId) {
        return ResponseEntity.ok().body(taskService.addTaskAnswerIdToTask(taskId, taskAnswerId));
    }

    @PostMapping("/delete-answer")
    public ResponseEntity<?> deleteTaskAnswerFromTask(@RequestParam("taskId") Long taskId,
                                                      @RequestParam("taskAnswerId") Long taskAnswerId) {
        return ResponseEntity.ok().body(taskService.deleteTaskAnswerIdFromTask(taskId, taskAnswerId));
    }

    //    для админа
    @DeleteMapping("/delete-from-groups")
    public void deleteTask(@RequestParam("taskId") Long taskId,
                           @RequestParam("groupId") Long groupId) {
        taskService.deleteTaskById(taskId);
        studentFeignClient.deleteTaskFromGroup(groupId, taskId);
    }
}
