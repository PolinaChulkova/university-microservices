package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teacher", schema = "teacher", catalog = "university-teacher")
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

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Task> tasks;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"),
            schema = "teacher", catalog = "university-teacher")
    private Set<Subject> subjects;

    @ElementCollection
    @CollectionTable(name = "teacher_groups", joinColumns = @JoinColumn(name = "teacher_id"),
            schema = "teacher", catalog = "university-teacher")
    @Column(name = "groups_id")
    private Set<Long> groupsId;

    public Teacher(String fullName, String email, String password,
                   String phoneNum, String academicDegree) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
        this.academicDegree = academicDegree;
    }
}
