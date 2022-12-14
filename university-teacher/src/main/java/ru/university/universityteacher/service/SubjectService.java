package ru.university.universityteacher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.university.universityentity.model.Subject;
import ru.university.universityentity.model.Teacher;
import ru.university.universityteacher.repo.SubjectRepo;

import javax.transaction.Transactional;

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

    public Subject addGroupIdToSubject(Long groupId, Long subjectId) {
        Subject subject = findSubjectById(subjectId);
        subject.getGroupsId().add(groupId);
        subjectRepo.save(subject);

        return subject;
    }

    public void addTeacherToSubject(Long teacherId, Long subjectId) {
        Subject subject = findSubjectById(subjectId);
        Teacher teacher = teacherService.findTeacherById(teacherId);
        subject.getTeachers().add(teacher);
        subjectRepo.save(subject);
    }

    public void detachTeacherFromSubject(Long teacherId, Long subjectId) {
        Subject subject = findSubjectById(subjectId);
        subject.getTeachers().remove(teacherService.findTeacherById(teacherId));
        subjectRepo.save(subject);
    }

    public void detachGroupFromSubject(Long groupId, Long subjectId) {
        Subject subject = findSubjectById(subjectId);
        subject.getGroupsId().remove(groupId);
        subjectRepo.save(subject);
    }

    public Subject findSubjectByName(String name) {
        return subjectRepo.findBySubjectName(name)
                .orElseThrow(() -> new RuntimeException("???? ?????????????? ?????????? ?????????????? ?? ???????????? "
                        + name + "."));
    }

    public void deleteSubjectById(Long subjectId) {
        if (subjectRepo.existsById(subjectId)) subjectRepo.deleteById(subjectId);
        else throw new RuntimeException("???????????? ?????????????????? ????????????????! " +
                "?????????????? ?? id=" + subjectId + " ???? ????????????????????.");
    }

    public Subject findSubjectById(Long subjectId) {
        return subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("?????????????? ?? id=" + subjectId + "???? ????????????."));
    }
}
