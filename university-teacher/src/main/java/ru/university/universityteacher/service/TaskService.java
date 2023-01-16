package ru.university.universityteacher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.university.universityentity.model.Task;
import ru.university.universityentity.model.Teacher;
import ru.university.universityteacher.dto.CreateTaskDTO;
import ru.university.universityteacher.dto.UpdateTaskDTO;
import ru.university.universityteacher.repo.TaskRepo;
import ru.university.universityutils.exceptions.custom_exception.FileNotFoundException;
import ru.university.universityutils.utils.FileService;
import ru.university.universityutils.exceptions.custom_exception.EntityNotFoundException;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    @Value("${storage.location.task}")
    private String storageTaskLocation;

    private final TaskRepo taskRepo;
    private final FileService fileService;
    private final TeacherService teacherService;
    private final SubjectService subjectService;

    public Task createTask(CreateTaskDTO dto) {
        Teacher teacher = teacherService.findTeacherById(dto.getTeacherId());

        if (!teacher.getGroupsId().contains(dto.getGroupId()))
            throw new EntityNotFoundException("Невозможно создать задание для группы, " +
                    "т.к. она закреплена за другим преподавателем");

        Task task = new Task(
                dto.getName(),
                dto.getDescription(),
                dto.getStartLine(),
                dto.getDeadLine(),
                subjectService.findSubjectById(dto.getSubjectId()),
                dto.getGroupId(),
                teacher
        );
        taskRepo.save(task);
//            Здесь нужно отправить задание группе и сохранить ее
        return task;
    }

    public Set<String> uploadFilesToTask(Long taskId, MultipartFile[] files) {
        Task task = findTaskById(taskId);
        task.getFilenames().addAll(Arrays.stream(files)
                .map(f -> fileService.uploadFile(f, Path.of(storageTaskLocation + "/" + taskId)))
                        .collect(Collectors.toSet()));
        return taskRepo.save(task).getFilenames();
    }

    public Set<String> deleteFileFromTask(Long taskId, String filename) {
        Task task = findTaskById(taskId);
        task.getFilenames().remove(filename);
        fileService.deleteFile(filename, Path.of(storageTaskLocation + "/" + taskId));
        return taskRepo.save(task).getFilenames();
    }

    public Resource downloadFileFromTask(Long taskId, String filename) {
        if (!findTaskById(taskId).getFilenames().contains(filename))
            throw new FileNotFoundException("Файл \""+ filename + "\" не найден.");

        else return fileService.loadAsResource(filename, Path.of(storageTaskLocation + "/" + taskId));
    }

    public Task updateTask(Long taskId, UpdateTaskDTO dto) {
        Task task = findTaskById(taskId);

        if (!task.getTeacher().equals(teacherService.findTeacherById(dto.getTeacherId())))
            throw new EntityNotFoundException("Невозможно обновить задание" + dto.getName()
                    + ", т.к. оно создано другим преподавателем");

        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setDeadLine(dto.getDeadLine());

        taskRepo.save(task);
        return task;
    }

    public List<Task> findTasksBySubjectIdAndGroupId(Long subjectId, Long groupId) {
        return taskRepo.findBySubjectIdAndGroupId(subjectId, groupId);
    }

    public Task findTaskByIdForTeacher(Long taskId, Long teacherId) {
        return taskRepo.findByIdAndTeacher_Id(taskId, teacherId).orElseThrow(()
                -> new EntityNotFoundException("Задание с id = " + taskId + " не найдено!"));
    }

    public Page<Task> findAllTeacherTasks(Long teacherId, Pageable pageable) {
        return taskRepo.findAllByTeacher_Id(teacherId, pageable);
    }

    public Task findTaskById(Long taskId) {
        return taskRepo.findById(taskId).orElseThrow(()
                -> new EntityNotFoundException("Задание с id = " + taskId + " не найдено!"));
    }

    public void deleteTaskById(Long taskId) {
        if (taskRepo.existsById(taskId)) taskRepo.deleteById(taskId);
        else throw new EntityNotFoundException("Задание с id = " + taskId + " не существует.");

        fileService.deleteDirectory(Path.of(storageTaskLocation + "/" + taskId));
    }

    public Set<Long> addTaskAnswerIdToTask(Long taskId, Long taskAnswerId) {
        Task task = findTaskById(taskId);
        task.getTaskAnswersId().add(taskAnswerId);
        return taskRepo.save(task).getTaskAnswersId();
    }

    public Set<Long> deleteTaskAnswerIdFromTask(Long taskId, Long taskAnswerId) {
        Task task = findTaskById(taskId);
        task.getTaskAnswersId().remove(taskAnswerId);
        return taskRepo.save(task).getTaskAnswersId();
    }
}
