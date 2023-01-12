package ru.university.universityteacher.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.university.universityteacher.dto.TeacherDTO;
import ru.university.universityteacher.service.TeacherService;

@RestController
@RequestMapping("/teacher")
@AllArgsConstructor
@Slf4j
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/{teacherId}")
    public ResponseEntity<?> findTeacherById(@PathVariable Long teacherId) {
        return ResponseEntity.ok().body(teacherService.findTeacherById(teacherId));
    }

    //    для админа
    @PostMapping("/create")
    public ResponseEntity<?> createTeacher(@RequestBody TeacherDTO dto) {
        return ResponseEntity.ok().body(teacherService.createTeacher(dto));
    }

    //    для админа
    @PutMapping("/update/{teacherId}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long teacherId,
                                           @RequestBody TeacherDTO dto) {
        return ResponseEntity.ok().body(teacherService.updateTeacher(teacherId, dto));
    }

    //    для админа
    @DeleteMapping("/{teacherId}")
    public void deleteTeacherById(@PathVariable Long teacherId) {
        teacherService.deleteTeacherById(teacherId);
    }
}
