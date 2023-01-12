package ru.university.universityteacher.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.university.universityteacher.feign.StudentFeignClient;
import ru.university.universityteacher.service.SubjectService;
import ru.university.universityteacher.service.TeacherService;

@RestController
@RequestMapping("/subject")
@AllArgsConstructor
@Slf4j
public class SubjectController {
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final StudentFeignClient studentFeignClient;

    @GetMapping("/{subjectId}")
    public ResponseEntity<?> getSubject(@PathVariable Long subjectId) {
            return ResponseEntity.ok().body(subjectService.findSubjectById(subjectId));
    }

    @GetMapping("/student/all")
    public ResponseEntity<?> getAllGroupSubjects(@RequestParam("groupId") Long groupId,
                                                 @RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return ResponseEntity.ok()
                .body(subjectService.findAllGroupSubjects(groupId, pageable).getContent());
    }

    @GetMapping("/teacher/search")
    public ResponseEntity<?> searchTeacherSubject(@RequestParam("teacherId") Long teacherId,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("key") String key) {
        Pageable pageable = PageRequest.of(page, 5);
        return ResponseEntity.ok()
                .body(subjectService.searchTeacherSubject(teacherId, key, pageable).getContent());
    }

    @GetMapping("/teacher/all")
    public ResponseEntity<?> getAllTeacherSubjects(@RequestParam("teacherId") Long teacherId,
                                                   @RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return ResponseEntity.ok()
                .body(subjectService.findAllTeacherSubjects(teacherId, pageable).getContent());
    }

    //    для админа
    @DeleteMapping("/{subjectId}")
    public void deleteSubject(@PathVariable Long subjectId) {
        subjectService.deleteSubjectById(subjectId);
    }

    //    для админа
    //    можно убрать дто потом
    @PostMapping("/create")
    public ResponseEntity<?> createSubject(@RequestBody String subjectName) {
            return ResponseEntity.ok().body(subjectService.createSubject(subjectName));
    }

    //    для админа
    @PostMapping("/add-teacher")
    public ResponseEntity<?> addTeacherToSubject(@RequestParam("teacherId") Long teacherId,
                                                 @RequestParam("subjectId") Long subjectId) {
            return ResponseEntity.ok().body(subjectService.addTeacherToSubject(teacherId, subjectId));
    }

    //    для админа
    @PostMapping("/detach-teacher")
    public ResponseEntity<?> detachTeacherFromSubject(@RequestParam("subjectId") Long subjectId,
                                                      @RequestParam("teacherId") Long teacherId) {
            return ResponseEntity.ok().body(subjectService.detachTeacherFromSubject(teacherId, subjectId));
    }

    /**
     * Добавление группы к предмету, при этом сразу выбирается преподаватель по этому предмету
     */
    //    для админа
    @PostMapping("/add-group")
    public ResponseEntity<?> addGroupToSubjectAndToTeacher(@RequestParam("subjectId") Long subjectId,
                                                           @RequestParam("groupId") Long groupId,
                                                           @RequestParam("teacherId") Long teacherId) {
            teacherService.addGroupIdToTeacher(teacherId, subjectId, groupId);
            studentFeignClient.addSubjectToGroup(groupId, subjectId);
            return ResponseEntity.ok().body(subjectService.addGroupIdToSubject(groupId, subjectId));
    }

    /**
     * Отсоединение группы от предмета
     */
    //    для админа
    @PostMapping("/detach-group")
    public ResponseEntity<?> detachGroupFromSubject(@RequestParam("subjectId") Long subjectId,
                                                    @RequestParam("groupId") Long groupId) {
            studentFeignClient.detachSubjectFromGroup(groupId, subjectId);
            return ResponseEntity.ok().body(subjectService.detachGroupFromSubject(groupId, subjectId));
    }
}
