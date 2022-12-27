package ru.university.universitystudent.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.university.universityentity.model.Group;
import ru.university.universityentity.model.Teacher;
import ru.university.universitystudent.dto.MessageResponse;
import ru.university.universitystudent.feign.TeacherFeignClient;
import ru.university.universitystudent.service.GroupService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupService;
    private final TeacherFeignClient teacherFeignClient;

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok().body(groupService.findGroupById(groupId));
    }

    @GetMapping("/groups")
    public ResponseEntity<?> findTeacherGroups(
            @RequestParam Long teacherId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "2") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Group> groups = teacherFeignClient.findTeacherById(teacherId).getBody()
                .getGroupsId()
                .stream()
                .map(groupService::findGroupById)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new PageImpl<Group>(groups, pageable, size).getContent());
    }

    @GetMapping("/students/{groupId}")
    public ResponseEntity<?> getStudentsByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok().body(groupService.findGroupById(groupId).getStudents());
    }

    //    для админа
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody String groupName) {
        try {
            return ResponseEntity.ok().body(groupService.createGroup(groupName));

        } catch (RuntimeException e) {
            log.error("Группа с названием  " + groupName + " не создана. Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Группа не создана. Error: "
                    + e.getLocalizedMessage()));
        }
    }

    //    для админа
    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroupById(groupId);
        return ResponseEntity.ok().body(new MessageResponse("Группа с " + groupId + " удалена."));
    }

    //    для админа
    @PostMapping("/add-student")
    public ResponseEntity<?> addStudentToGroup(@RequestParam("groupId") Long groupId,
                                               @RequestParam("studentId") Long studentId) {
        try {
            groupService.addStudentToGroup(groupId, studentId);
            return ResponseEntity.ok().body(new MessageResponse("В группу с id=" + groupId
                    + " добавлен студент с id=" + studentId));

        } catch (RuntimeException e) {
            log.error("Студент с id= " + studentId + " не добавлен в группу с id=" + groupId + ". Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Студент не добавлен в группу. " +
                    "Error: " + e.getLocalizedMessage()));
        }
    }

    //    для админа
    @PostMapping("/delete-student")
    public ResponseEntity<?> deleteStudentFromGroup(@RequestParam("groupId") Long groupId,
                                                    @RequestParam("studentId") Long studentId) {
        groupService.deleteStudentFromGroup(groupId, studentId);
        return ResponseEntity.ok().body(new MessageResponse("Из группы с id=" + groupId
                + " удалён студент с id=" + studentId));
    }

    //    для админа
//    используется в studentWebClientBuilder
    @PostMapping("/add-subject")
    public ResponseEntity<?> addSubjectToGroup(@RequestParam("groupId") Long groupId,
                                               @RequestParam("subjectId") Long subjectId) {
        try {
            groupService.addSubjectIdToGroup(subjectId, groupId);
            return ResponseEntity.ok().body(new MessageResponse("Предмет добавлен к группе"));

        } catch (RuntimeException e) {
            log.error("Предмет с id = " + subjectId + " не добавлен к группе с id=" + groupId + ". Error: "
                    + e.getLocalizedMessage());

            return ResponseEntity.badRequest().body(new MessageResponse("Предмет не добавлен к группе. " +
                    "Error: " + e.getLocalizedMessage()));
        }
    }

    @PostMapping("/detach-subject")
    public ResponseEntity<?> detachSubjectFromGroup(@RequestParam("groupId") Long groupId,
                                                    @RequestParam("subjectId") Long subjectId) {
        try {
            groupService.detachSubjectIdFromGroup(subjectId, groupId);
            return ResponseEntity.ok().body(new MessageResponse("Группа откреплена от предмета"));

        } catch (RuntimeException e) {
            log.error("Группа с id = " + groupId + " не откреплена от предмета с id=" + subjectId + ". Error: "
                    + Arrays.toString(e.getStackTrace()));

            return ResponseEntity.badRequest().body(new MessageResponse("Группа не откреплена от пердмета " +
                    "Error: " + Arrays.toString(e.getStackTrace())));
        }
    }
}
