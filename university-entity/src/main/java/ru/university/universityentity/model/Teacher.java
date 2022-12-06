package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teacher")
@Getter@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "phone_num")
    private String phoneNum;
    @Column(name = "academic_degree")
    private String academicDegree;

    @JsonBackReference
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Task> tasks;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

    @ElementCollection
    @CollectionTable(name = "teacher_groups", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "groups_id")
    private List<Long> groupsId = new ArrayList<>();

    public Teacher(String fullName, String email, String password,
                   String phoneNum, String academicDegree) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
        this.academicDegree = academicDegree;
    }
}
