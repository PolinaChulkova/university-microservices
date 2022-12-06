package ru.university.universitystudent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class CreateTaskAnswerDTO {
    private String comment;
    //    после созании Security студент будет получаться из Principal
    private Long studentId;
    private Long taskId;
}
