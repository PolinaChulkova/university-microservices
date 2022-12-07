package ru.university.universityteacher.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.university.universityteacher.dto.MessageResponse;
import ru.university.universityteacher.dto.TeacherDTO;
import ru.university.universityteacher.service.TeacherService;

@RestController
@RequestMapping("/teacher")
@AllArgsConstructor
@Slf4j
public class TeacherController {

    private final TeacherService teacherService;

//    @RabbitListener(queues = "teacherQueue")
//    public void notificationListener(String message) {
//        log.info("Преподаватель получил сообщение: " + message);
//    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<?> findTeacherById(@PathVariable Long teacherId) {
        return ResponseEntity.ok().body(teacherService.findTeacherById(teacherId));
    }

    //    для админа
    @PostMapping("/create")
    public ResponseEntity<?> createTeacher(@RequestBody TeacherDTO dto) {
        try {
            teacherService.createTeacher(dto);
            return ResponseEntity.ok().body(new MessageResponse("Создан преподаватель с email: " + dto.getEmail()));

        } catch (RuntimeException e) {
            log.error("Преподаватель с email: " + dto.getEmail() + " не создан. Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Преподаватель с email: " + dto.getEmail())
                    + " не создан. Error: " + e.getLocalizedMessage());
        }
    }

    //    для админа
    @PutMapping("/update/{teacherId}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long teacherId,
                                           @RequestBody TeacherDTO dto) {
        try {
            teacherService.updateTeacher(teacherId, dto);
            return ResponseEntity.ok().body(new MessageResponse("Обновлён преподаватель с email: "
                    + dto.getEmail()));

        } catch (RuntimeException e) {
            log.error("Преподватель с Id= " + teacherId + " не обновлён. {}"
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Преподаватель с email: "
                    + dto.getEmail() + " не обновлён. Error: ") + e.getLocalizedMessage());
        }
    }

    //    для админа
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<?> deleteTeacherById(@PathVariable Long teacherId) {
        teacherService.deleteTeacherById(teacherId);
        return ResponseEntity.ok().body(new MessageResponse("Удалён преподаватель с id=" + teacherId));
    }
}
