package ru.university.universityteacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TaskNotificationDTO {
    private final Long taskId;
    private final Long subjectId;
    private final Long groupId;
}
