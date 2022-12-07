package ru.university.universityteacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@Getter@Setter
public class SubjectDTO {
    private String subjectName;
    private Collection<Long> teachersId;
}
