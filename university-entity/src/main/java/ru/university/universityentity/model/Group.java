package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;
    @Column(name = "group_name", unique = true)
    private String name;

    @JsonBackReference
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "group_tasks", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "tasksId")
    private List<Long> tasksId = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "group_subjects", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "subjectsId")
    private List<Long> subjectsId = new ArrayList<>();
}
