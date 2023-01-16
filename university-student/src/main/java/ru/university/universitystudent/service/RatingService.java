package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.university.universityentity.model.Rating;
import ru.university.universitystudent.dto.CreateRatingDTO;
import ru.university.universitystudent.dto.UpdateRatingDTO;
import ru.university.universitystudent.repo.RatingRepo;
import ru.university.universityutils.exceptions.custom_exception.EntityNotFoundException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    private final RatingRepo ratingRepo;
    private final StudentService studentService;

    public Rating createRating(CreateRatingDTO dto) {
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

    public Rating updateRating(Long ratingId, Long teacherId, UpdateRatingDTO dto) {
        Rating rating = findRatingForTeacher(ratingId, teacherId);
        rating.setComment(dto.getComment());
        rating.setMark(dto.getMark());

        return ratingRepo.save(rating);
    }

    public Rating findRatingForStudent(Long taskId, Long studentId) {
        return ratingRepo.findByTaskIdAndStudentId(taskId, studentId).orElseThrow(()
                -> new EntityNotFoundException("Оценка по заданию с id = " + taskId + " не найдена!"));
    }

    public Rating findRatingForTeacher(Long ratingId, Long teacherId) {
        return ratingRepo.findByIdAndTeacherId(ratingId, teacherId).orElseThrow(() ->
                new EntityNotFoundException("Оценка с id = " + ratingId + " не найдена!"));
    }

    public List<Rating> findRatingsForTeacher(Long taskId, Long teacherId) {
        return ratingRepo.findRatingsByTaskIdAndTeacherId(taskId, teacherId);
    }

    public List<Rating> findRatingsForSubject(Long subjectId, Long studentId) {
        return ratingRepo.findBySubjectIdAndStudentId(subjectId, studentId);
    }

    public Rating findRatingById(Long ratingId) {
        return ratingRepo.findById(ratingId).orElseThrow(() ->
                new EntityNotFoundException("Оценка с id = " + ratingId + " не найден!"));
    }
}