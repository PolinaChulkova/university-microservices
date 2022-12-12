package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "group_tasks", joinColumns = @JoinColumn(name = "group_id"),
            schema = "students", catalog = "university-students")
    @Column(name = "tasksId")
    private Set<Long> tasksId = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "group_subjects", joinColumns = @JoinColumn(name = "group_id"),
            schema = "students", catalog = "university-students")
    @Column(name = "subjectsId")
    private Set<Long> subjectsId = new HashSet<>();

    public Group(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (!Objects.equals(id, group.id)) return false;
        if (!Objects.equals(name, group.name)) return false;
        return Objects.equals(students, group.students);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (students != null ? students.hashCode() : 0);
        return result;
    }
}
