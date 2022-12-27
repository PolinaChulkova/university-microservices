package ru.university.universityentity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "student", schema = "student", catalog = "university-student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "phone_num")
    private String phoneNum;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER, cascade =
            {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "group_id")
    private Group group;

    @JsonBackReference
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Rating> rating;

    public Student(String fullName, String email, String password, String phoneNum) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (!Objects.equals(id, student.id)) return false;
        if (!Objects.equals(fullName, student.fullName)) return false;
        if (!Objects.equals(email, student.email)) return false;
        if (!Objects.equals(password, student.password)) return false;
        if (!Objects.equals(phoneNum, student.phoneNum)) return false;
        return Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (phoneNum != null ? phoneNum.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }
}
