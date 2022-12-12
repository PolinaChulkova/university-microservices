package ru.university.universitystudent.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.university.universitystudent.dto.GroupDTO;
import ru.university.universitystudent.dto.MessageResponse;
import ru.university.universitystudent.service.GroupService;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok().body(groupService.findGroupById(groupId));
    }

//    @GetMapping("/groups/{teacherId}")
//    public ResponseEntity<?> findTeacherGroups(
//            @PathVariable Long teacherId,
//            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
//            @RequestParam(value = "size", required = false, defaultValue = "2") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.ok().body(groupService.findTeacherGroups(teacherId, pageable)
//                .getContent());
//    }

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
            log.error("Студент с id= " + studentId + " не добавлен в группу с id=" + groupId + ". {}"
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
}
