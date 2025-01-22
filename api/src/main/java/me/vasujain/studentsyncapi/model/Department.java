package me.vasujain.studentsyncapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Department extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="school_id", nullable = false)
    private School school;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "department")
    private Set<User> users = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "department")
    private Set<Course> courses = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "department")
    private Set<Batch> batches = new HashSet<>();

}
