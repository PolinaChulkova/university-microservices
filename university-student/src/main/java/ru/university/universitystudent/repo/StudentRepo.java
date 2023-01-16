package ru.university.universitystudent.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.university.universityentity.model.Student;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    Optional<Student> findById(Long id);

    Page<Student> findAll(Pageable pageable);

    boolean existsById(Long id);

    void deleteById(Long id);
}
