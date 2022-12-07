package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rating", schema = "students", catalog = "university-students")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Long id;
    @Column(name = "mark")
    private Short mark;
    @Column(name = "comment")
    private String comment;
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "subject_id")
    private Long subjectId;
    @Column(name = "teacher_id")
    private Long teacherId;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    public Rating(Short mark, String comment, Long taskId, Long subjectId,
                  Long teacherId, Student student) {
        this.mark = mark;
        this.comment = comment;
        this.taskId = taskId;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.student = student;
    }
}
