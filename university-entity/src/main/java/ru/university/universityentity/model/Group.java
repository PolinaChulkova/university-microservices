package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "groups", schema = "students", catalog = "university-students")
@Getter
@Setter
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
    @CollectionTable(name = "group_tasks", joinColumns = @JoinColumn(name = "group_id"),
            schema = "students", catalog = "university-students")
    @Column(name = "tasksId")
    private List<Long> tasksId = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "group_subjects", joinColumns = @JoinColumn(name = "group_id"),
            schema = "students", catalog = "university-students")
    @Column(name = "subjectsId")
    private List<Long> subjectsId = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Group group = (Group) o;
        return id != null && Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
