package ru.university.universityteacher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.university.universityentity.model.Subject;
import ru.university.universityteacher.dto.SubjectDTO;
import ru.university.universityteacher.repo.SubjectRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectService {

    private final SubjectRepo subjectRepo;
    private final TeacherService teacherService;

    public Page<Subject> searchTeacherSubject(Long teacherId, String key, Pageable pageable) {
            return subjectRepo.findTeacherSubject(teacherId, key, pageable);
    }

    public Page<Subject> findAllTeacherSubjects(Long teacherId, Pageable pageable) {
        return subjectRepo.findAllByTeacherId(teacherId, pageable);
    }

    public Page<Subject> findAllGroupSubjects(Long groupId, Pageable pageable) {
        return subjectRepo.findAllByGroupsIdIsContaining(groupId, pageable);
    }

    public void createSubject(SubjectDTO dto) {
            Subject subject = new Subject();
            subject.setSubjectName(dto.getSubjectName());
            dto.getTeachersId().forEach(id ->
                    subject.getTeachers().add(teacherService.findTeacherById(id)));

            subjectRepo.save(subject);
    }

    public void addGroupToSubject(Long groupId, Long subjectId) {
        Subject subject = findSubjectById(subjectId);
        subject.getGroupsId().add(groupId);
        subjectRepo.save(subject);
    }

    public void addTeacherToSubject(Long teacherId, Long subjectId) {
            Subject subject = findSubjectById(subjectId);
            subject.getTeachers().add(teacherService.findTeacherById(teacherId));

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
                .orElseThrow(() -> new RuntimeException("Не удалось найти предмет с именем "
                        + name + "."));
    }

    public void deleteSubjectById(Long subjectId) {
      if (subjectRepo.existsById(subjectId)) subjectRepo.deleteById(subjectId);
      else throw new RuntimeException("Нельзя совершить удаление! " +
              "Предмет с id=" + subjectId + " не существует.");
    }

    public Subject findSubjectById(Long subjectId) {
        return subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Предмет с id=" + subjectId + "не найден."));
    }
}
