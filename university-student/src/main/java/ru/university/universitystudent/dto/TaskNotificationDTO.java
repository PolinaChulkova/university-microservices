package ru.university.universitystudent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class TaskNotificationDTO {
    private final Long taskId;
    private final Long subjectId;
    private final Long groupId;
}
