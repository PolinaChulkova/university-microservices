package ru.university.universitystudent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter@Setter
public class TaskDTO {
    private final String name;
    private final String description;
    private final Date startLine;
    private final Date deadLine;
    private final Long subjectId;

    //    после созании Security преподаватель будет получаться из Principal
    private final Long teacherId;
    private final Long groupId;
}
