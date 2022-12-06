package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.university.entity.Teacher;
import ru.university.studentuniversity.dto.TeacherDTO;
import ru.university.studentuniversity.repo.TeacherRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {

    private final TeacherRepo teacherRepo;

    public Teacher findTeacherById(Long teacherId) {
        return teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Преподаватель с id=" + teacherId + "не найден."));
    }

    public Teacher findTeacherByEmail(String email) {
        return teacherRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Преподаватель с email: " + email + "не найден."));
    }

    public void createTeacher(TeacherDTO dto) {
            Teacher teacher = new Teacher(
                    dto.getFullName(),
                    dto.getEmail(),
                    dto.getPassword(),
                    dto.getPhoneNum(),
                    dto.getAcademicDegree());
            teacherRepo.save(teacher);
    }

    public void updateTeacher(Long teacherId, TeacherDTO dto) {
        try {
            Teacher teacher = findTeacherById(teacherId);
            teacher.setFullName(dto.getFullName());
            teacher.setEmail(dto.getEmail());
            teacher.setPassword(dto.getPassword());
            teacher.setPhoneNum(dto.getPhoneNum());
            teacher.setAcademicDegree(dto.getAcademicDegree());

            teacherRepo.save(teacher);

        } catch (RuntimeException e) {
            log.error("Преподватель с Id= " + teacherId + " не обновлён. {}"
                    + e.getLocalizedMessage());
        }
    }

    public void deleteTeacherById(Long teacherId) {
        if (teacherRepo.existsById(teacherId)) teacherRepo.deleteById(teacherId);
        else throw new RuntimeException("Преподавателя с Id=" + teacherId + " не существует!");
    }
}
