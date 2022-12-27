package ru.university.universityteacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class TeacherDTO {
    private final String fullName;
    private final String email;
    private final String password;
    private final String phoneNum;
    private final String academicDegree;
}
