package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.university.universityentity.model.TaskAnswer;
import ru.university.universitystudent.dto.CreateTaskAnswerDTO;
import ru.university.universitystudent.repo.TaskAnswerRepo;
import ru.university.universityutils.utils.FileService;
import ru.university.universityutils.exceptions.custom_exception.EntityNotFoundException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskAnswerService {

    private final TaskAnswerRepo taskAnswerRepo;
    private final StudentService studentService;
    private final FileService fileService;

    public TaskAnswer getTaskAnswerForStudent(Long taskId, Long studentId) {
        return taskAnswerRepo.findByTaskIdAndStudentId(taskId, studentId).orElseThrow(() ->
                new EntityNotFoundException("Ваша работа не найдена!"));
    }

    public TaskAnswer sendTaskAnswer(CreateTaskAnswerDTO dto) {
        TaskAnswer answer = new TaskAnswer(
                dto.getComment(),
                dto.getTaskId(),
                studentService.findStudentById(dto.getStudentId())
        );

        taskAnswerRepo.save(answer);
        return answer;
    }

    public Set<String> uploadFilesToAnswer(Long taskAnswerId, MultipartFile[] files) {
        TaskAnswer taskAnswer = findTaskAnswerById(taskAnswerId);
        taskAnswer.getFilesUri().addAll(Arrays.stream(files)
                .map(fileService::uploadFile).collect(Collectors.toSet()));
        return taskAnswerRepo.save(taskAnswer).getFilesUri();
    }

    public TaskAnswer updateTaskAnswer(Long taskAnswerId, String comment) {
        TaskAnswer answer = findTaskAnswerById(taskAnswerId);
        answer.setComment(comment);
        return taskAnswerRepo.save(answer);
    }

    public TaskAnswer findTaskAnswerById(Long taskAnswerId) {
        return taskAnswerRepo.findById(taskAnswerId).orElseThrow(() ->
                new EntityNotFoundException("Ваша работа не найдена!"));
    }

    public Set<String> deleteFileFromAnswer(Long taskAnswerId, String fileUri) throws RuntimeException {
        TaskAnswer answer = findTaskAnswerById(taskAnswerId);
        answer.getFilesUri().remove(fileUri);
        return taskAnswerRepo.save(answer).getFilesUri();
    }

    public TaskAnswer deleteTaskAnswer(Long taskAnswerId, Long studentId) {
        return taskAnswerRepo.deleteByIdAndStudentId(taskAnswerId, studentId).orElseThrow(() ->
                new EntityNotFoundException("Ваша работа не найдена!"));
    }
}
