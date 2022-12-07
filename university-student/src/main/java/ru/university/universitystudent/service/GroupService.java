package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.university.universityentity.model.Group;
import ru.university.universitystudent.dto.CreateGroupDTO;
import ru.university.universitystudent.repo.GroupRepo;


@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final GroupRepo groupRepo;
    private final StudentService studentService;

    public Page<Group> findAllGroups(Pageable pageable) {
        return groupRepo.findAll(pageable);
    }

//    public Page<Group> findTeacherGroups(Long teacherId, Pageable pageable) {
//        List<Group> groups = teacherService.findTeacherById(teacherId)
//                .getGroupsId().stream().map(this::findGroupById).collect(Collectors.toList());
//
//        return new PageImpl<>(groups, pageable, groups.size());
//    }

    public void createGroup(CreateGroupDTO dto) {
        Group group = new Group();
        group.setName(dto.getName());
        dto.getStudentsId().forEach(id ->
                group.getStudents().add(studentService.findStudentById(id)));
        groupRepo.save(group);
    }

    public void addStudentToGroup(Long groupId, Long studentId) {
        Group group = findGroupById(groupId);
        group.getStudents().add(studentService.findStudentById(studentId));
        groupRepo.save(group);
    }

    public void deleteStudentFromGroup(Long groupId, Long studentId) {
        Group group = findGroupById(groupId);
        group.getStudents().remove(studentService.findStudentById(studentId));
        groupRepo.save(group);
    }

    public Group findGroupByGroupName(String groupName) {
        return groupRepo.findByName(groupName)
                .orElseThrow(() -> new RuntimeException("Группа с именем "
                        + groupName + " не найдена."));
    }

    public void deleteGroupByGroupName(String groupName) {
        if (groupRepo.existsByName(groupName)) groupRepo.deleteByName(groupName);
        else throw new RuntimeException("Нельзя совершить удаление! " +
                "Группа с именем " + groupName + " не существует.");
    }

    public void deleteGroupById(Long groupId) {
        if (groupRepo.existsById(groupId)) groupRepo.deleteById(groupId);
        else throw new RuntimeException("Нельзя совершить удаление! " +
                "Группа с id= " + groupId + " не существует.");
    }

    public Group findGroupById(Long groupId) {
        return groupRepo.findById(groupId).orElseThrow(() -> new RuntimeException("Группа с id= "
                + groupId + " не найдена."));
    }

    public void save(Group group) {
        try {
            groupRepo.save(group);
        } catch (RuntimeException e) {
            log.error("Группа " + group.getName() + " не сохранена. " +
                    "Error: " + e.getLocalizedMessage());
        }
    }
}
