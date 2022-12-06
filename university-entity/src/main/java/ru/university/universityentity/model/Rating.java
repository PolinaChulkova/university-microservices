package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rating")
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
    @Column(name = "taskId")
    private Long taskId;
    @Column(name = "subjectId")
    private Long subjectId;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    public Rating(Short mark, String comment, Long taskId, Long subjectId, Student student) {
        this.mark = mark;
        this.comment = comment;
        this.taskId = taskId;
        this.subjectId = subjectId;
        this.student = student;
    }
}
