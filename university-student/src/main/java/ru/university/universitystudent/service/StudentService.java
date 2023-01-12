package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.university.universityentity.model.Student;
import ru.university.universitystudent.dto.StudentDTO;
import ru.university.universitystudent.repo.StudentRepo;
import ru.university.universityutils.exceptions.custom_exception.EntityNotFoundException;

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

    public void deleteStudentById(Long studentId) {
        if (studentRepo.existsById(studentId)) studentRepo.deleteById(studentId);
        else throw new EntityNotFoundException("Студент с id = " + studentId + " не найден!");
    }

    public Student findStudentById(Long studentId) {
        return studentRepo.findById(studentId).orElseThrow(() ->
                new EntityNotFoundException("Студент с id = " + studentId + " не найден!"));
    }

    public void save(Student student) {
        studentRepo.save(student);
    }
}
