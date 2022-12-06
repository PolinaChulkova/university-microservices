package ru.university.universitystudent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class CreateRatingDTO {
    private Short mark;
    private String comment;
    private Long taskId;
    private Long subjectId;
    private Long teacherId;
    private Long studentId;
}
