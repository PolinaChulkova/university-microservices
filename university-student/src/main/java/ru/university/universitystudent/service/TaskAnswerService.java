package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.university.entity.TaskAnswer;
import ru.university.studentuniversity.dto.CreateTaskAnswerDTO;
import ru.university.studentuniversity.repo.TaskAnswerRepo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskAnswerService {

    private final TaskService taskService;
    private final TaskAnswerRepo taskAnswerRepo;
    private final StudentService studentService;
    private final FileService fileService;

    public TaskAnswer getTaskAnswerForStudent(Long taskId, Long studentId) {
        return taskAnswerRepo.findByTaskIdAndStudentId(taskId, studentId).orElseThrow(() ->
                new RuntimeException("Ваш ответ не найден"));
    }

    public List<TaskAnswer> getTaskAnswersForTeacher(Long taskId, Long teacherId) {
        return taskAnswerRepo.findTaskAnswersByTaskId(taskService
                .findTaskByIdForTeacher(taskId, teacherId).getId());
    }

    public TaskAnswer sendTaskAnswer(CreateTaskAnswerDTO dto) {
        if (taskService.findTaskByIdForStudent(dto.getTaskId(), dto.getStudentId()) == null) {
            throw new RuntimeException("Нельзя отправить ответ на это задание");
        }

        TaskAnswer answer = new TaskAnswer(
                dto.getComment(),
                studentService.findStudentById(dto.getStudentId()),
                dto.getTaskId()
        );

        taskAnswerRepo.save(answer);
        return answer;
    }

    public void uploadFilesToAnswer(Long taskAnswerId, MultipartFile[] files) {
        TaskAnswer taskAnswer = findTaskAnswerById(taskAnswerId);
        taskAnswer.getFilesUri().addAll(Arrays.stream(files)
                .map(fileService::uploadFile).collect(Collectors.toSet()));
        taskAnswerRepo.save(taskAnswer);
    }

    public void updateTaskAnswer(Long taskAnswerId, String comment) {
        TaskAnswer answer = findTaskAnswerById(taskAnswerId);
        answer.setComment(comment);
        taskAnswerRepo.save(answer);
    }

    public TaskAnswer findTaskAnswerById(Long taskAnswerId) {
        return taskAnswerRepo.findById(taskAnswerId).orElseThrow(() ->
                new RuntimeException("Ваш ответ не существует"));
    }

    public void deleteFileFromAnswer(Long taskAnswerId, String fileUri) throws RuntimeException {
        TaskAnswer answer = findTaskAnswerById(taskAnswerId);
        answer.getFilesUri().remove(fileUri);
        taskAnswerRepo.save(answer);
    }

    public void deleteTaskAnswer(Long taskAnswerId, Long studentId) {
        if (taskAnswerRepo.existsByIdAndStudentId(taskAnswerId, studentId));
            taskAnswerRepo.deleteById(taskAnswerId);
    }
}
