package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.university.universityentity.model.Group;
import ru.university.universityentity.model.Student;
import ru.university.universitystudent.repo.GroupRepo;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GroupService {

    private final GroupRepo groupRepo;
    private final StudentService studentService;

    public Page<Group> findAllGroups(Pageable pageable) {
        return groupRepo.findAll(pageable);
    }
//
//    public Page<Group> findTeacherGroups(Long teacherId, Pageable pageable) {
//        List<Group> groups = teacherService.findTeacherById(teacherId)
//                .getGroupsId().stream().map(this::findGroupById).collect(Collectors.toList());
//
//        return new PageImpl<>(groups, pageable, groups.size());
//    }

    public Group createGroup(String groupName) {
        Group group = new Group(groupName);
//
//        for (Student student : students) {
//            student.setGroup(group);
//            studentService.save(student);
//        }
        groupRepo.save(group);
        return group;
    }

    public void addStudentToGroup(Long groupId, Long studentId) {
        Group group = findGroupById(groupId);
        Student student = studentService.findStudentById(studentId);
        group.getStudents().add(student);
        student.setGroup(group);
        studentService.save(student);
        groupRepo.save(group);
    }

    public void addSubjectIdToGroup(Long subjectId, Long groupId) {
        Group group = findGroupById(groupId);
        group.getSubjectsId().add(subjectId);
        save(group);
    }

    public void detachSubjectIdFromGroup(Long subjectId, Long groupId) {
        Group group = findGroupById(groupId);
        group.getSubjectsId().remove(subjectId);
        save(group);
    }


    public Set<Long> addTaskToGroup(Long groupId, Long taskId) {
        Group group = findGroupById(groupId);
        group.getTasksId().add(taskId);
        return save(group).getTasksId();
    }

    public Set<Long> deleteTaskFromGroup(Long groupId, Long taskId) {
        Group group = findGroupById(groupId);
        group.getTasksId().remove(taskId);
        return save(group).getTasksId();
    }

    public void deleteStudentFromGroup(Long groupId, Long studentId) {
        Group group = findGroupById(groupId);
        group.getStudents().remove(studentService.findStudentById(studentId));
        groupRepo.save(group);
    }

    public Group findGroupByGroupName(String groupName) {
        return groupRepo.findByName(groupName)
                .orElseThrow(() -> new RuntimeException("???????????? ?? ???????????? "
                        + groupName + " ???? ??????????????."));
    }

    public void deleteGroupByGroupName(String groupName) {
        if (groupRepo.existsByName(groupName)) groupRepo.deleteByName(groupName);
        else throw new RuntimeException("???????????? ?????????????????? ????????????????! " +
                "???????????? ?? ???????????? " + groupName + " ???? ????????????????????.");
    }

    public void deleteGroupById(Long groupId) {
        if (groupRepo.existsById(groupId)) groupRepo.deleteById(groupId);
        else throw new RuntimeException("???????????? ?????????????????? ????????????????! " +
                "???????????? ?? id= " + groupId + " ???? ????????????????????.");
    }

    public Group findGroupById(Long groupId) {
        return groupRepo.findById(groupId).orElseThrow(() -> new RuntimeException("???????????? ?? id = "
                + groupId + " ???? ??????????????."));
    }

    public Group save(Group group) {
        try {
            return groupRepo.save(group);
        } catch (RuntimeException e) {
            log.error("???????????? " + group.getName() + " ???? ??????????????????. " +
                    "Error: " + Arrays.toString(e.getStackTrace()));

            throw new RuntimeException("???????????? " + group.getName() + " ???? ??????????????????. " +
                    "Error: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
