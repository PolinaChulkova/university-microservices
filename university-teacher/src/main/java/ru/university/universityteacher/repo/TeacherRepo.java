package ru.university.universityteacher.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.university.universityentity.model.Teacher;

import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {

    @Transactional
    @Query(value = "SELECT t FROM Teacher t JOIN t.subjects s WHERE t.id = ?1 AND s.id = ?2")
    Optional<Teacher> findByIdAndSubjectId(Long teacherId, Long subjectId);

//    @Transactional
//    @Query(value = "SELECT t FROM Teacher t JOIN t.groupsId g WHERE t.id = ?1 AND g.")
//    Optional<Teacher> findTeacherByIdAndGroupId(Long teacherId, Long groupId);

    Optional<Teacher> findById(Long id);

    Page<Teacher> findAll(Pageable pageable);

    boolean existsById(Long id);

    void deleteById(Long id);
}