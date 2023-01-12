package ru.university.universitystudent.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.university.universityentity.model.TaskAnswer;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskAnswerRepo extends JpaRepository<TaskAnswer, Long> {

    List<TaskAnswer> findTaskAnswersByTaskId(Long taskId);

    Optional<TaskAnswer> findById(Long id);

    Optional<TaskAnswer> findByTaskIdAndStudentId(Long taskId, Long studentId);

    Optional<TaskAnswer> deleteByIdAndStudentId(Long id, Long studentId);

    boolean existsByIdAndStudentId(Long id, Long studentId);

    void deleteById(Long id);
}
