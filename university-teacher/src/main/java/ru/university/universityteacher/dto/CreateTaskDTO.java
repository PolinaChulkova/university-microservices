package ru.university.universityteacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter@Setter
public class CreateTaskDTO {
    private String name;
    private String description;
    private Date startLine;
    private Date deadLine;
    //    после созании Security преподаватель будет получаться из Principal
    private Long teacherId;
    private Long groupId;
}
