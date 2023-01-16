package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.university.universityentity.model.Group;
import ru.university.universityentity.model.Student;
import ru.university.universitystudent.repo.GroupRepo;
import ru.university.universityutils.exceptions.custom_exception.EntityNotFoundException;

import javax.transaction.Transactional;
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

    public Group createGroup(String groupName) {
        Group group = new Group(groupName);
        groupRepo.save(group);
        return group;
    }

    public Set<Student> addStudentToGroup(Long groupId, Long studentId) {
        Group group = findGroupById(groupId);
        Student student = studentService.findStudentById(studentId);
        group.getStudents().add(student);
        student.setGroup(group);
        studentService.save(student);
        return groupRepo.save(group).getStudents();
    }

    public Set<Long> addSubjectIdToGroup(Long subjectId, Long groupId) {
        Group group = findGroupById(groupId);
        group.getSubjectsId().add(subjectId);
        return save(group).getSubjectsId();
    }

    public Set<Long> detachSubjectIdFromGroup(Long subjectId, Long groupId) {
        Group group = findGroupById(groupId);
        group.getSubjectsId().remove(subjectId);
        return save(group).getSubjectsId();
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

    public Set<Student> deleteStudentFromGroup(Long groupId, Long studentId) {
        Group group = findGroupById(groupId);
        group.getStudents().remove(studentService.findStudentById(studentId));
        return groupRepo.save(group).getStudents();
    }

    public void deleteGroupById(Long groupId) {
        if (groupRepo.existsById(groupId)) groupRepo.deleteById(groupId);
        else throw new EntityNotFoundException("Группа с id = " + groupId + " не найдена!");
    }

    public Group findGroupById(Long groupId) {
        return groupRepo.findById(groupId).orElseThrow(() -> new EntityNotFoundException("Группа с id = "
                + groupId + " не найдена!"));
    }

    public Group save(Group group) {
            return groupRepo.save(group);
    }
}
