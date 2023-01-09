package ru.university.universityutils.web_client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.university.universityentity.model.Task;
import ru.university.universityentity.model.Teacher;

@Component
public class TeacherWebClientBuilder {

    private final String baseUrlTeacher = "http://localhost:8765/university-teacher";

    public Task findTaskByIdAndTeacherId(Long taskId, Long teacherId) {
        try {
            return WebClient.create(baseUrlTeacher)
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/task/teacher/{taskId}/{teacherId}")
                            .build(taskId, teacherId))
                    .retrieve()
                    .bodyToFlux(Task.class)
                    .blockFirst();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return null;
    }

    public Teacher findTeacherById(Long teacherId) {
        try {
            return WebClient.create(baseUrlTeacher)
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/teacher/{teacherId}")
                            .build(teacherId))
                    .retrieve()
                    .bodyToFlux(Teacher.class)
                    .blockFirst();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
