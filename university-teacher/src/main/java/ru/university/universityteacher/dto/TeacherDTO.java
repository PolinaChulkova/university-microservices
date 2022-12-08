package ru.university.universityteacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class TeacherDTO {
    private String fullName;
    private String email;
    private String password;
    private String phoneNum;
    private String academicDegree;
}
