package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.university.universityentity.model.TaskAnswer;
import ru.university.universitystudent.dto.CreateTaskAnswerDTO;
import ru.university.universitystudent.feign.TeacherFeignClient;
import ru.university.universitystudent.repo.TaskAnswerRepo;
import ru.university.universityutils.exceptions.custom_exception.EntityNotFoundException;
import ru.university.universityutils.exceptions.custom_exception.FileNotFoundException;
import ru.university.universityutils.exceptions.custom_exception.RestRuntimeException;
import ru.university.universityutils.utils.FileService;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TaskAnswerService {

    @Value("${storage.location.task-answer}")
    private Path storageTaskAnswerLocation;

    private final TeacherFeignClient teacherFeignClient;
    private final TaskAnswerRepo taskAnswerRepo;
    private final StudentService studentService;
    private final FileService fileService;

    public TaskAnswer getTaskAnswerForStudent(Long taskId, Long studentId) {
        return taskAnswerRepo.findByTaskIdAndStudentId(taskId, studentId).orElseThrow(() ->
                new EntityNotFoundException("Ваша работа не найдена!"));
    }

    public List<TaskAnswer> getTaskAnswersForTeacher(Long teacherId, Long taskId) {
        Set<Long> taskAnswersId = teacherFeignClient.getTeacherTask(taskId, teacherId).getBody().getTaskAnswersId();

        if(taskAnswersId.isEmpty())
            throw new EntityNotFoundException("Ответы на задание не найдены.");

        return taskAnswersId.stream().sorted()
                .map(this::findTaskAnswerById)
                .collect(Collectors.toList());
    }

    public TaskAnswer sendTaskAnswer(CreateTaskAnswerDTO dto) {
        TaskAnswer answer = new TaskAnswer(
                dto.getComment(),
                dto.getTaskId(),
                studentService.findStudentById(dto.getStudentId())
        );
        TaskAnswer taskAnswer = taskAnswerRepo.save(answer);
        if (teacherFeignClient.addTaskAnswerToTask(dto.getTaskId(), taskAnswer.getId()).getBody() == null)
            throw new RestRuntimeException("Ваш ответ не сохранен.");

        return taskAnswer;
    }

    public Set<String> uploadFilesToAnswer(Long taskAnswerId, MultipartFile[] files) {
        TaskAnswer taskAnswer = findTaskAnswerById(taskAnswerId);
        taskAnswer.getFilenames().addAll(Arrays.stream(files)
                .map(f -> fileService.uploadFile(f, Path.of(storageTaskAnswerLocation + "/" + taskAnswerId)))
                .collect(Collectors.toSet()));
        return taskAnswerRepo.save(taskAnswer).getFilenames();
    }

    public Set<String> deleteFileFromAnswer(Long taskAnswerId, String filename) {
        TaskAnswer answer = findTaskAnswerById(taskAnswerId);
        answer.getFilenames().remove(filename);
        fileService.deleteFile(filename, Path.of(storageTaskAnswerLocation + "/" + taskAnswerId));
        return taskAnswerRepo.save(answer).getFilenames();
    }

    public Resource downloadFileFromAnswer(Long taskAnswerId, String filename) {
        if (!findTaskAnswerById(taskAnswerId).getFilenames().contains(filename))
            throw new FileNotFoundException("Файл \"" + filename + "\" не найден");

        else return fileService.loadAsResource(filename, Path.of(storageTaskAnswerLocation + "/" + taskAnswerId));
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

    public TaskAnswer deleteTaskAnswer(Long taskAnswerId, Long studentId) {
//        TaskAnswer taskAnswer = findTaskAnswerById(taskAnswerId);
//        if (taskAnswer.getStudent().getId())studentService.findStudentById(studentId).getId()
//            taskAnswerRepo.deleteById(taskAnswerId);

        fileService.deleteDirectory(Path.of(storageTaskAnswerLocation + "/" + taskAnswerId));
        TaskAnswer taskAnswer = taskAnswerRepo.findByIdAndStudentId(taskAnswerId, studentId).orElseThrow(() ->
                new EntityNotFoundException("Ваша работа не найдена!"));
        taskAnswerRepo.deleteById(taskAnswerId);
        return taskAnswer;
    }
}
