package ru.university.universityentity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity(name = "TaskAnswer")
@Table(name = "task_answer", schema = "student", catalog = "university-student")
@Getter@Setter
@NoArgsConstructor
public class TaskAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_answer_id")
    private Long id;
    @Column(name = "comment")
    private String comment;
    @Column(name = "date")
    private Date date = new Date();
    @Column(name = "task_id")
    private Long taskId;

    @ElementCollection
    @CollectionTable(name = "answers_files", joinColumns = @JoinColumn(name = "task_answer_id"),
            schema = "student", catalog = "university-student")
    @Column(name = "filename")
    private Set<String> filenames;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    public TaskAnswer(String comment, Long taskId, Student student) {
        this.comment = comment;
        this.taskId = taskId;
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskAnswer that = (TaskAnswer) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(comment, that.comment)) return false;
        if (!Objects.equals(date, that.date)) return false;
        if (!Objects.equals(taskId, that.taskId)) return false;
        return Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        result = 31 * result + (student != null ? student.hashCode() : 0);
        return result;
    }
}
