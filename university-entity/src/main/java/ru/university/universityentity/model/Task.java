package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "task", schema = "teachers", catalog = "university-teachers")
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    @Column(name = "task_name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "start_line")
    private Date startLine;
    @Column(name = "dead_line")
    private Date deadLine;
    @Column(name = "group_id")
    private Long groupId;

    @ElementCollection
    @CollectionTable(name = "tasks_files", joinColumns = @JoinColumn(name = "task_id"),
            schema = "teachers", catalog = "university-teachers")
    @Column(name = "file_uri")
    private Set<String> filesUri = new HashSet<>();

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ElementCollection
    @CollectionTable(name = "task_task_answers", joinColumns = @JoinColumn(name = "task_id"),
            schema = "teachers", catalog = "university-teachers")
    @Column(name = "task_answers_id")
    private Set<Long> taskAnswers = new HashSet<>();

    public Task(String name, String description, Date startLine, Date deadLine,
                Long groupId, Teacher teacher) {
        this.name = name;
        this.description = description;
        this.startLine = startLine;
        this.deadLine = deadLine;
        this.groupId = groupId;
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!Objects.equals(id, task.id)) return false;
        if (!Objects.equals(name, task.name)) return false;
        if (!Objects.equals(startLine, task.startLine)) return false;
        if (!Objects.equals(deadLine, task.deadLine)) return false;
        if (!Objects.equals(groupId, task.groupId)) return false;
        return Objects.equals(teacher, task.teacher);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (startLine != null ? startLine.hashCode() : 0);
        result = 31 * result + (deadLine != null ? deadLine.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        return result;
    }
}
