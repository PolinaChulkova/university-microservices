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
import ru.university.universitystudent.feign.TeacherFeignClient;
import ru.university.universitystudent.service.GroupService;

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

    @GetMapping("/find-all")
    public ResponseEntity<?> getAllGroups(@RequestParam(value = "page", required = false, defaultValue = "0")
                                          int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok().body(groupService.findAllGroups(pageable).getContent());
    }

    @GetMapping("/groups")
    public ResponseEntity<?> findTeacherGroups(
            @RequestParam Long teacherId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "2") int size) {

        Pageable pageable = PageRequest.of(page, size);
        ResponseEntity<Teacher> teacherResponse = teacherFeignClient.findTeacherById(teacherId);

        List<Group> groups = teacherResponse.getBody()
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
        return ResponseEntity.ok().body(groupService.createGroup(groupName));
    }

    //    для админа
    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroupById(groupId);
    }

    //    для админа
    @PostMapping("/add-student")
    public ResponseEntity<?> addStudentToGroup(@RequestParam("groupId") Long groupId,
                                               @RequestParam("studentId") Long studentId) {
        return ResponseEntity.ok().body(groupService.addStudentToGroup(groupId, studentId));
    }

    //    для админа
    @PostMapping("/delete-student")
    public ResponseEntity<?> deleteStudentFromGroup(@RequestParam("groupId") Long groupId,
                                                    @RequestParam("studentId") Long studentId) {
        return ResponseEntity.ok().body(groupService.deleteStudentFromGroup(groupId, studentId));
    }

    //    для админа
//    используется в studentWebClientBuilder
    @PostMapping("/add-subject")
    public ResponseEntity<?> addSubjectToGroup(@RequestParam("groupId") Long groupId,
                                               @RequestParam("subjectId") Long subjectId) {
        return ResponseEntity.ok().body(groupService.addSubjectIdToGroup(subjectId, groupId));
    }

    @PostMapping("/detach-subject")
    public ResponseEntity<?> detachSubjectFromGroup(@RequestParam("groupId") Long groupId,
                                                    @RequestParam("subjectId") Long subjectId) {
        return ResponseEntity.ok().body(groupService.detachSubjectIdFromGroup(subjectId, groupId));
    }

    @PostMapping("/add-task")
    public ResponseEntity<?> addTaskToGroup(@RequestParam("groupId") Long groupId,
                                            @RequestParam("taskId") Long taskId) {
        return ResponseEntity.ok().body(groupService.addTaskToGroup(groupId, taskId));
    }

    @PostMapping("/delete-task")
    public ResponseEntity<?> deleteTaskFromGroup(@RequestParam("groupId") Long groupId,
                                                 @RequestParam("taskId") Long taskId) {
        return ResponseEntity.ok().body(groupService.deleteTaskFromGroup(groupId, taskId));
    }
}
