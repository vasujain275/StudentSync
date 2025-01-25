package me.vasujain.studentsyncapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import me.vasujain.studentsyncapi.enums.UserRole;
import me.vasujain.studentsyncapi.enums.UserStatus;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity{

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    private String firstName;
    private String lastName;
    private Integer admissionYear;
    private Integer currentSemester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    private String avatar;

    @Column(length = 512)
    private String refreshToken;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private Set<Enrollment> enrollments = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private Set<Enrollment> teachingEnrollments = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "coordinator")
    private Set<Batch> coordinatedBatches = new HashSet<>();
}