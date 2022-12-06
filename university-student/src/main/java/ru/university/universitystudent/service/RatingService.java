package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.university.model.universityentity.entity.Rating;
import ru.university.studentuniversity.dto.CreateRatingDTO;
import ru.university.studentuniversity.dto.UpdateRatingDTO;
import ru.university.studentuniversity.repo.RatingRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    private final RatingRepo ratingRepo;
    private final TaskService taskService;
    private final StudentService studentService;

    public Rating createRating(CreateRatingDTO dto) {
        if (taskService.findTaskByIdForTeacher(dto.getTaskId(), dto.getTeacherId()) == null)
            throw new RuntimeException("Невозможно поставить оценку студенту с id=" + dto.getStudentId()
                    + ", т.к. он закреплен за другим преподавателем");

        Rating rating = new Rating(
                dto.getMark(),
                dto.getComment(),
                dto.getTaskId(),
                dto.getSubjectId(),
                dto.getTeacherId(),
                studentService.findStudentById(dto.getStudentId())
        );

        ratingRepo.save(rating);
        return rating;
    }

    public void updateRating(Long ratingId, Long teacherId, UpdateRatingDTO dto) {
        Rating rating = findRatingForTeacher(ratingId, teacherId);
        rating.setComment(dto.getComment());
        rating.setMark(dto.getMark());

        ratingRepo.save(rating);
    }

    public Rating findRatingForStudent(Long taskId, Long studentId) {
        return ratingRepo.findByTaskIdAndStudentId(taskId, studentId).orElseThrow(()
                -> new RuntimeException("Оценка по заданию с id=" + taskId + " не найдена."));
    }

    public Rating findRatingForTeacher(Long ratingId, Long teacherId) {
        return ratingRepo.findByIdAndTeacherId(ratingId, teacherId).orElseThrow(() ->
                new RuntimeException("Оценка с id=" + ratingId + " не найдена"));
    }

    public List<Rating> findRatingsForTeacher(Long taskId, Long teacherId) {
        return ratingRepo.findRatingsByTaskIdAndTeacherId(taskId, teacherId);
    }

    public List<Rating> findRatingsForSubject(Long subjectId, Long studentId) {
        return ratingRepo.findBySubjectIdAndStudentId(subjectId, studentId);
    }

    public Rating findRatingById(Long ratingId) {
        return ratingRepo.findById(ratingId).orElseThrow(() ->
                new RuntimeException("Рейтинг с id=" + ratingId + " не найден."));
    }
}