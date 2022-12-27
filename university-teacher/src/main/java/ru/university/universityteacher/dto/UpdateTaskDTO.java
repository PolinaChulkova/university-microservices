package ru.university.universityteacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter@Setter
public class UpdateTaskDTO {
    private final String name;
    private final String description;
    private final Date deadLine;
//    потом буду получать из principal
    private final Long teacherId;
}

