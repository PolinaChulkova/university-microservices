package ru.university.universitystudent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@Getter@Setter
public class CreateGroupDTO {
    private String name;
    private Collection<Long> studentsId;
}
