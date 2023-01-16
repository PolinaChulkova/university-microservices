package ru.university.universitystudent.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.university.universityentity.model.TaskAnswer;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskAnswerRepo extends JpaRepository<TaskAnswer, Long> {

    List<TaskAnswer> findTaskAnswersByTaskId(Long taskId);

    Optional<TaskAnswer> findById(Long id);

    Optional<TaskAnswer> findByTaskIdAndStudentId(Long taskId, Long studentId);

    @Transactional
    @Query(value = "SELECT ta FROM TaskAnswer ta WHERE ta.id = ?1 AND ta.student.id = ?2")
    Optional<TaskAnswer> findByIdAndStudentId(Long id, Long studentId);

    void deleteById(Long id);

    boolean existsByIdAndStudentId(Long id, Long studentId);
}
