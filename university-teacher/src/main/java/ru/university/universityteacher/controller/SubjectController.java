package ru.university.universityteacher.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.university.universityteacher.dto.MessageResponse;
import ru.university.universityteacher.dto.SubjectDTO;
import ru.university.universityteacher.service.SubjectService;
import ru.university.universityutils.StudentWebClientBuilder;

@RestController
@RequestMapping("/subject")
@AllArgsConstructor
@Slf4j
public class SubjectController {

    private final SubjectService subjectService;
    private final StudentWebClientBuilder studentWebClientBuilder;

    @GetMapping("/{subjectId}")
    public ResponseEntity<?> getSubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok().body(subjectService.findSubjectById(subjectId));
    }

    @GetMapping("/student/all/{groupId}/{page}")
    public ResponseEntity<?> getAllGroupSubjects(@PathVariable Long groupId,
                                                 @PathVariable int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return ResponseEntity.ok()
                .body(subjectService.findAllGroupSubjects(groupId, pageable).getContent());
    }

    @GetMapping("/teacher/search/{teacherId}/{page}")
    public ResponseEntity<?> searchTeacherSubject(@PathVariable Long teacherId,
                                                  @PathVariable int page,
                                                  @RequestParam("key") String key) {
        Pageable pageable = PageRequest.of(page, 5);
        return ResponseEntity.ok()
                .body(subjectService.searchTeacherSubject(teacherId, key, pageable).getContent());
    }

    @GetMapping("/teacher/all/{teacherId}/{page}")
    public ResponseEntity<?> getAllTeacherSubjects(@PathVariable Long teacherId,
                                                   @PathVariable int page) {
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
    @PostMapping("/create")
    public ResponseEntity<?> createSubject(@RequestBody SubjectDTO dto) {
        try {
            return ResponseEntity.ok().body(subjectService.createSubject(dto));

        } catch (RuntimeException e) {
            log.error("Предмет с названием " + dto.getSubjectName() + " не создан. Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Предмет: \""
                    + dto.getSubjectName() + "\" не создан. Error: " + e.getLocalizedMessage()));
        }
    }

    //    для админа
    @PostMapping("/add-teacher-group")
    public ResponseEntity<?> addTeacherAndGroupToSubject(@RequestParam("teacherId") Long teacherId,
                                                         @RequestParam("groupId") Long groupId,
                                                         @RequestParam("subjectId") Long subjectId) {
        try {
            subjectService.addTeacherAndGroupToSubject(teacherId, groupId, subjectId);
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
    @PostMapping("/add-group/{subjectId}/{groupId}")
    public ResponseEntity<?> addGroupToSubject(@PathVariable Long subjectId,
                                               @PathVariable Long groupId) {
        try {
            subjectService.addGroupIdToSubject(groupId, subjectId);
            studentWebClientBuilder.addSubjectToGroup(groupId, subjectId);

            return ResponseEntity.ok().body(new MessageResponse("К предмету с id=" + subjectId +
                    " добавлена группа с id=" + groupId));

        } catch (RuntimeException e) {
            log.error("Не удалось добавить группу к предмету. Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Группа не добавлена к предмету. Error: "
                    + e.getLocalizedMessage()));
        }
    }

    //    для админа
    @PostMapping("/detach-teacher/{subjectId}/{teacherId}")
    public ResponseEntity<?> detachTeacherFromSubject(@PathVariable Long subjectId,
                                                      @PathVariable Long teacherId) {
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

    //    для админа
    @PostMapping("/detach-group/{subjectId}/{groupId}")
    public ResponseEntity<?> detachGroupFromSubject(@PathVariable Long subjectId,
                                                    @PathVariable Long groupId) {
        try {
            subjectService.detachGroupFromSubject(groupId, subjectId);
            return ResponseEntity.ok().body(new MessageResponse("Группа с id=" + groupId
                    + " откреплёна от предмета с id=" + subjectId));

        } catch (RuntimeException e) {
            log.error("Не удалось открепить группу от предмета. Error: " + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Не удалось открепить группу от предмета " +
                    "Error: ") + e.getLocalizedMessage());
        }
    }
}
