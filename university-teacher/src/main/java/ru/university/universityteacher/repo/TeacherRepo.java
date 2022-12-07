package ru.university.universityteacher.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.university.universityentity.model.Teacher;

import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findById(Long id);

    Page<Teacher> findAll(Pageable pageable);

    boolean existsById(Long id);

    void deleteById(Long id);
}