package ru.university.universitystudent.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.university.universityentity.model.Rating;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepo extends JpaRepository<Rating, Long> {

    List<Rating> findRatingsByTaskIdAndTeacherId(Long taskId, Long teacherId);

    Optional<Rating> findByIdAndTeacherId(Long id, Long teacherId);

    List<Rating> findBySubjectIdAndStudentId(Long subjectId, Long studentId);

    Optional<Rating> findByTaskIdAndStudentId(Long taskId, Long studentId);

    Page<Rating> findAll(Pageable pageable);

    Optional<Rating> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);
}
