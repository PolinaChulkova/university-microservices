package ru.university.universityteacher.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.university.universityentity.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    @Transactional
    @Query(value = "SELECT t FROM Task t JOIN t.teacher.subjects s WHERE s.id=?1 AND t.groupId=?2")
    List<Task> findBySubjectIdAndGroupId(Long subjectId, Long groupId);

    List<Task> findAllBySubject_Id(Long subjectId);
    Page<Task> findAllByTeacher_Id(Long teacherId, Pageable pageable);

    Optional<Task> findByIdAndTeacher_Id(Long id, Long teacherId);

    Page<Task> findAll(Pageable pageable);

    Optional<Task> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);
}
