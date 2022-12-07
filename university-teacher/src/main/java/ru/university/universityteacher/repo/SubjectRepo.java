package ru.university.universityteacher.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.university.universityentity.model.Subject;

import java.util.Optional;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Long> {

    @Transactional
    @Query(value = "SELECT s FROM Subject s JOIN s.teachers t WHERE t.id = ?1 AND s.subjectName LIKE %?2%")
    Page<Subject> findTeacherSubject(Long teacherId, String key, Pageable pageable);

    @Transactional
    @Query(value = "SELECT s FROM Subject s JOIN s.teachers t WHERE t.id = ?1")
    Page<Subject> findAllByTeacherId(Long teacherId, Pageable pageable);

    Page<Subject> findAllByGroupsIdIsContaining(Long groupId, Pageable pageable);

    Page<Subject> findAll(Pageable pageable);

    Optional<Subject> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    Optional<Subject> findBySubjectName(String subjectName);

}
