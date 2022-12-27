package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subject", schema = "teacher", catalog = "university-teacher")
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
            schema = "teacher", catalog = "university-teacher")
    private Set<Long> groupsId = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"),
            schema = "teacher", catalog = "university-teacher")
    private Set<Teacher> teachers = new HashSet<>();

    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }
}
