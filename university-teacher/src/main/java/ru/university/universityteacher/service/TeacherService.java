package ru.university.universityteacher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.university.universityentity.model.Teacher;
import ru.university.universityteacher.dto.TeacherDTO;
import ru.university.universityteacher.repo.TeacherRepo;

import javax.transaction.Transactional;

@Transactional
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

    public Teacher createTeacher(TeacherDTO dto) {
        Teacher teacher = new Teacher(
                dto.getFullName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getPhoneNum(),
                dto.getAcademicDegree()
        );
        teacherRepo.save(teacher);
        return teacher;
    }

    public Teacher updateTeacher(Long teacherId, TeacherDTO dto) {
        Teacher teacher = findTeacherById(teacherId);
        teacher.setFullName(dto.getFullName());
        teacher.setEmail(dto.getEmail());
        teacher.setPassword(dto.getPassword());
        teacher.setPhoneNum(dto.getPhoneNum());
        teacher.setAcademicDegree(dto.getAcademicDegree());

        teacherRepo.save(teacher);
        return teacher;
    }

    public void deleteTeacherById(Long teacherId) {
        if (teacherRepo.existsById(teacherId)) teacherRepo.deleteById(teacherId);
        else throw new RuntimeException("Преподавателя с Id=" + teacherId + " не существует!");
    }
}
