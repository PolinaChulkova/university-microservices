package ru.university.universitystudent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter@Setter
public class UpdateTaskDTO {
    private String name;
    private String description;
    private Date deadLine;
//    потом буду получать из principal
    private Long teacherId;
}

