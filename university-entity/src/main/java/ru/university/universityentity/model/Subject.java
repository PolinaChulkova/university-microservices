package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "subject", schema = "teacher", catalog = "university-teacher")
@Getter@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) && Objects.equals(subjectName, subject.subjectName) && Objects.equals(groupsId, subject.groupsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectName, groupsId);
    }
}
