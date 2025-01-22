package me.vasujain.studentsyncapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "notices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Notice extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String notice;
}
