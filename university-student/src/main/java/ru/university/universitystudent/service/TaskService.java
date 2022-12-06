package ru.university.universitystudent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.university.entity.Group;
import ru.university.entity.Task;
import ru.university.entity.Teacher;
import ru.university.studentuniversity.dto.CreateTaskDTO;
import ru.university.studentuniversity.dto.UpdateTaskDTO;
import ru.university.studentuniversity.repo.TaskRepo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepo taskRepo;
    private final GroupService groupService;
    private final FileService fileService;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public Task createTask(CreateTaskDTO dto) {
        Teacher teacher = teacherService.findTeacherById(dto.getTeacherId());
            if (!teacher.getGroupsId().contains(dto.getGroupId()))
                throw new RuntimeException("Невозможно создать задание для группы, " +
                        "т.к. она закреплена за другим преподавателем");

            Task task = new Task(
                    dto.getName(),
                    dto.getDescription(),
                    dto.getStartLine(),
                    dto.getDeadLine(),
                    dto.getGroupId(),
                    teacher
            );
            taskRepo.save(task);
            Group group = groupService.findGroupById(dto.getGroupId());
            group.getTasksId().add(task.getId());
            groupService.save(group);

            return task;
    }

    public void uploadFilesToTask(Long taskId, MultipartFile[] files) {
        Task task = findTaskById(taskId);
        task.getFilesUri().addAll(Arrays.stream(files)
                .map(fileService::uploadFile).collect(Collectors.toList()));
        taskRepo.save(task);
    }

    public void deleteFileFromTask(Long taskId, String fileUri) throws RuntimeException{
        Task task = findTaskById(taskId);
        task.getFilesUri().remove(fileUri);
        taskRepo.save(task);
    }

    public void updateTask(Long taskId, UpdateTaskDTO dto) {
            Task task = findTaskById(taskId);

            if (!task.getTeacher().equals(teacherService.findTeacherById(dto.getTeacherId())))
                throw new RuntimeException("Невозможно обновить задание" + dto.getName()
                        + ", т.к. оно создано другим преподавателем");

            task.setName(dto.getName());
            task.setDescription(dto.getDescription());
            task.setDeadLine(dto.getDeadLine());

            taskRepo.save(task);
    }

    public List<Task> findAllTasksBySubjectForStudent(Long subjectId, Long studentId) {
        return taskRepo.findBySubjectIdAndGroupId(subjectId,
                studentService.findStudentById(studentId).getGroup().getId());
    }

    public Task findTaskByIdForStudent(Long taskId, Long studentId) {
        List<Long> tasksId= studentService.findStudentById(studentId).getGroup().getTasksId();
        return findTaskById(tasksId.get(tasksId.indexOf(taskId)));
    }

    public Task findTaskByIdForTeacher(Long taskId, Long teacherId) {
        return taskRepo.findByIdAndTeacher_Id(taskId, teacherId).orElseThrow(()
                -> new RuntimeException("Задание с id=" + taskId + " недоступно."));
    }

    public Page<Task> findAllTeacherTasks(Long teacherId, Pageable pageable) {
        return taskRepo.findAllByTeacher_Id(teacherId, pageable);
    }

    public Task findTaskById(Long taskId) {
        return taskRepo.findById(taskId).orElseThrow(()
                -> new RuntimeException("Задание с id=" + taskId + " не найдено."));
    }

    public void deleteTaskById(Long taskId) {
        if (taskRepo.existsById(taskId)) taskRepo.deleteById(taskId);
        else throw new RuntimeException("Задание с id=" + taskId + "не существует.");
    }

    public void saveTask(Task task) {
        try {
            taskRepo.save(task);

        } catch (RuntimeException e) {
            log.error("Задание " + task.getName() + " не сохранёно. {}", e.getLocalizedMessage());
        }
    }
}
