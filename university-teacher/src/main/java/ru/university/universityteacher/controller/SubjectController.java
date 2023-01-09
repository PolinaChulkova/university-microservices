package ru.university.universityteacher.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.university.universityteacher.dto.MessageResponse;
import ru.university.universityteacher.feign.StudentFeignClient;
import ru.university.universityteacher.service.SubjectService;
import ru.university.universityteacher.service.TeacherService;

import java.util.Arrays;

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
        try {
            return ResponseEntity.ok().body(subjectService.findSubjectById(subjectId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    public ResponseEntity<?> deleteSubject(@PathVariable Long subjectId) {
        subjectService.deleteSubjectById(subjectId);
        return ResponseEntity.ok().body(new MessageResponse("Предмет с id="
                + subjectId + " удалён"));
    }

    //    для админа
    //    можно убрать дто потом
    @PostMapping("/create")
    public ResponseEntity<?> createSubject(@RequestBody String subjectName) {
        try {
            return ResponseEntity.ok().body(subjectService.createSubject(subjectName));

        } catch (RuntimeException e) {
            log.error("Предмет с названием " + subjectName + " не создан. Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Предмет: \""
                    + subjectName + "\" не создан. Error: " + e.getLocalizedMessage()));
        }
    }

    //    для админа
    @PostMapping("/add-teacher")
    public ResponseEntity<?> addTeacherToSubject(@RequestParam("teacherId") Long teacherId,
                                                 @RequestParam("subjectId") Long subjectId) {
        try {
            subjectService.addTeacherToSubject(teacherId, subjectId);
            return ResponseEntity.ok().body(new MessageResponse("Преподаватель с id=" + teacherId
                    + " добавлен к предмету с id=" + subjectId));

        } catch (RuntimeException e) {
            log.error("Не удалось добавить преподавателя с id= " + teacherId
                    + " к предмету с id=" + subjectId + ". Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Преподаватель не добавлен к предмету. " +
                    "Error: " + e.getLocalizedMessage()));
        }
    }

    //    для админа
    @PostMapping("/detach-teacher")
    public ResponseEntity<?> detachTeacherFromSubject(@RequestParam("subjectId") Long subjectId,
                                                      @RequestParam("teacherId") Long teacherId) {
        try {
            subjectService.detachTeacherFromSubject(teacherId, subjectId);
            return ResponseEntity.ok().body(new MessageResponse("Преподаватель с id=" + teacherId
                    + " откреплён от предмета с id=" + subjectId));

        } catch (RuntimeException e) {
            log.error("Не удалось открепить преподавателя с id=" + teacherId
                    + " от предмета с id=" + subjectId + ". Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Не удалось открепить преподавателя от предмета " +
                    "Error: ") + e.getLocalizedMessage());
        }
    }

    /**
     * Добавление группы к предмету, при этом сразу выбирается преподаватель по этому предмету
     */
    //    для админа
    @PostMapping("/add-group")
    public ResponseEntity<?> addGroupToSubjectAndToTeacher(@RequestParam("subjectId") Long subjectId,
                                                           @RequestParam("groupId") Long groupId,
                                                           @RequestParam("teacherId") Long teacherId) {
        try {
            subjectService.addGroupIdToSubject(groupId, subjectId);
            teacherService.addGroupIdToTeacher(teacherId, subjectId, groupId);
            studentFeignClient.addSubjectToGroup(groupId, subjectId);

            return ResponseEntity.ok().body(new MessageResponse("К предмету с id = " + subjectId +
                    " добавлена группа с id = " + groupId));

        } catch (RuntimeException e) {
            log.error("Не удалось добавить группу к предмету. Error: "
                    + Arrays.toString(e.getStackTrace()));

            return ResponseEntity.badRequest().body(new MessageResponse("Группа не добавлена к предмету. Error: "
                    + Arrays.toString(e.getStackTrace())));
        }
    }

    /**
     * Отсоединение группы от предмета
     */
    //    для админа
    @PostMapping("/detach-group")
    public ResponseEntity<?> detachGroupFromSubject(@RequestParam("subjectId") Long subjectId,
                                                    @RequestParam("groupId") Long groupId) {
        try {
            subjectService.detachGroupFromSubject(groupId, subjectId);
            studentFeignClient.detachSubjectFromGroup(groupId, subjectId);

            return ResponseEntity.ok().body(new MessageResponse("Группа с id = " + groupId
                    + " откреплёна от предмета с id = " + subjectId));

        } catch (RuntimeException e) {
            log.error("Не удалось открепить группу от предмета. Error: "
                    + Arrays.toString(e.getStackTrace()));

            return ResponseEntity.badRequest().body(new MessageResponse("Не удалось открепить группу от предмета " +
                    "Error: " + Arrays.toString(e.getStackTrace())));
        }
    }
}
