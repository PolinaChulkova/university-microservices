package ru.university.universityteacher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.university.universityentity.model.Subject;
import ru.university.universityentity.model.Teacher;
import ru.university.universityteacher.repo.SubjectRepo;
import ru.university.universityutils.exceptions.custom_exception.EntityNotFoundException;

import javax.transaction.Transactional;
import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectService {
    private final SubjectRepo subjectRepo;
    private final TeacherService teacherService;

    public Page<Subject> searchTeacherSubject(Long teacherId, String key, Pageable pageable) {
        return subjectRepo.searchTeacherSubject(teacherId, key, pageable);
    }

    public Page<Subject> findAllTeacherSubjects(Long teacherId, Pageable pageable) {
        return subjectRepo.findAllByTeacherId(teacherId, pageable);
    }

    public Page<Subject> findAllGroupSubjects(Long groupId, Pageable pageable) {
        return subjectRepo.findAllByGroupsIdIsContaining(groupId, pageable);
    }

    public Subject createSubject(String subjectName) {
        Subject subject = new Subject(subjectName);
        subjectRepo.save(subject);
        return subject;
    }

    public Set<Long> addGroupIdToSubject(Long groupId, Long subjectId) {
        Subject subject = findSubjectById(subjectId);
        subject.getGroupsId().add(groupId);
        return subjectRepo.save(subject).getGroupsId();
    }

    public Set<Teacher> addTeacherToSubject(Long teacherId, Long subjectId) {
        Subject subject = findSubjectById(subjectId);
        Teacher teacher = teacherService.findTeacherById(teacherId);
        subject.getTeachers().add(teacher);
        return subjectRepo.save(subject).getTeachers();
    }

    public Set<Teacher> detachTeacherFromSubject(Long teacherId, Long subjectId) {
        Subject subject = findSubjectById(subjectId);
        subject.getTeachers().remove(teacherService.findTeacherById(teacherId));
        return subjectRepo.save(subject).getTeachers();
    }

    public Set<Long> detachGroupFromSubject(Long groupId, Long subjectId) {
        Subject subject = findSubjectById(subjectId);
        subject.getGroupsId().remove(groupId);
        return subjectRepo.save(subject).getGroupsId();
    }

    public void deleteSubjectById(Long subjectId) {
        if (subjectRepo.existsById(subjectId))
            subjectRepo.deleteById(subjectId);

        else throw new EntityNotFoundException("Нельзя совершить удаление! " +
                "Предмет с id = " + subjectId + " не существует.");
    }

    public Subject findSubjectById(Long subjectId) {
        return subjectRepo.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException("Предмет с id=" + subjectId + "не найден."));
    }
}
