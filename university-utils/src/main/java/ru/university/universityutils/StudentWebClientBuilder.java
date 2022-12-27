package ru.university.universityutils;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.university.universityentity.model.Student;

@Component
public class StudentWebClientBuilder {
    private final String baseUriStudent = "http://localhost:8765/university-student";

    public Student findStudentById(Long studentId) {
        try {
            return WebClient.create(baseUriStudent)
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/student/{studentId}")
                            .build(studentId))
                    .retrieve()
                    .bodyToFlux(Student.class)
                    .blockFirst();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addSubjectToGroup(Long groupId, Long subjectId) {
        try {
            WebClient.create(baseUriStudent)
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/group/add-subject")
                            .queryParam("groupId", groupId)
                            .queryParam("subjectId", subjectId)
                            .build())
                    .retrieve();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void detachSubjectFromGroup(Long groupId, Long subjectId) {
        try {
            WebClient.create(baseUriStudent)
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/group/detach-subject")
                            .queryParam("groupId", groupId)
                            .queryParam("subjectId", subjectId)
                            .build())
                    .retrieve();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
