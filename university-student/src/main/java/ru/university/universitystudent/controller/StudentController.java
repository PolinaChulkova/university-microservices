package ru.university.universitystudent.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.university.universitystudent.dto.MessageResponse;
import ru.university.universitystudent.dto.StudentDTO;
import ru.university.universitystudent.service.StudentService;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

//    @RabbitListener(queues = "studentQueue")
//    public void notificationListener(String message) {
//        log.info("Студент получил сообщение: " + message);
//    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> findStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok().body(studentService.findStudentById(studentId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO dto) {
        try {
            return ResponseEntity.ok().body(studentService.createStudent(dto));

        } catch (RuntimeException e) {
            log.error("Студент с email: " + dto.getEmail() + " не создан. Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Студент с email: " + dto.getEmail()
                    + " не создан. Error: " + e.getLocalizedMessage()));
        }
    }

    @PutMapping("/update/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable Long studentId,
                                           @RequestBody StudentDTO dto) {
        try {
            return ResponseEntity.ok().body(studentService.updateStudent(studentId, dto));

        } catch (RuntimeException e) {
            log.error("Студент с Id= " + studentId + " не обновлён. Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Студент с email: " + dto.getEmail()
                    + " не обновлён. Error: " + e.getLocalizedMessage()));
        }
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudentById(@PathVariable Long studentId) {
        studentService.deleteStudentById(studentId);
        return ResponseEntity.ok().body(new MessageResponse("Студент с id=" + studentId + " удалён."));
    }
}