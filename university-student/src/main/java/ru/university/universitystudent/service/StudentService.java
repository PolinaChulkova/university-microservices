package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.university.universityentity.model.Student;
import ru.university.universitystudent.dto.StudentDTO;
import ru.university.universitystudent.repo.StudentRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepo studentRepo;

    public Student createStudent(StudentDTO dto) {
            Student student = new Student(
                    dto.getFullName(),
                    dto.getEmail(),
                    dto.getPassword(),
                    dto.getPhoneNum()
            );
            studentRepo.save(student);
            return student;
    }

    public Student updateStudent(Long studentId, StudentDTO dto) {
            Student student = findStudentById(studentId);
            student.setFullName(dto.getFullName());
            student.setEmail(dto.getEmail());
            student.setPassword(dto.getPassword());
            student.setPhoneNum(dto.getPhoneNum());
            studentRepo.save(student);
            return student;
    }

    public Student findStudentByEmail(String studentEmail) {
        return studentRepo.findByEmail(studentEmail).orElseThrow(()
                -> new RuntimeException("Не удалось найти студента с email: " + studentEmail + "."));
    }

    public void deleteStudentById(Long studentId) {
        if (studentRepo.existsById(studentId)) studentRepo.deleteById(studentId);
        else throw new RuntimeException("Студента с Id=" + studentId + " не существует!");
    }

    public Student findStudentById(Long id) {
        return studentRepo.findById(id).orElseThrow(() -> new RuntimeException("Не удалось найти студента с Id="
                + id + "."));
    }

    public void save(Student student) {
        try {
            studentRepo.save(student);
        } catch (RuntimeException e) {
            log.error("Студент " + student.getFullName() + " не сохранен. " +
                    "Error: " + e.getLocalizedMessage());
        }
    }
}
