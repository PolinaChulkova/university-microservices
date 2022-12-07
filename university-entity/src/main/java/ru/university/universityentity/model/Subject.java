package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "subject", schema = "teachers", catalog = "university-teachers")
@Getter@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long id;
    @Column(name = "subject_name")
    private String subjectName;

    @ElementCollection
    @CollectionTable(name = "subject_groups", joinColumns = @JoinColumn(name = "subject_id"),
            schema = "teachers", catalog = "university-teachers")
    private List<Long> groupsId = new ArrayList<>();

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"),
            schema = "teachers", catalog = "university-teachers")
    private Collection<Teacher> teachers = new ArrayList<>();
}
