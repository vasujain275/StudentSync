package me.vasujain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Getter @Setter
    @Column(unique = true, nullable = false)
    private String username;

    @Getter @Setter
    @Column(unique = true, nullable = false)
    private String email;

    @Getter @Setter
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Getter @Setter
    @Column(nullable = false)
    private String role;


    @Getter @Setter private String firstName;
    @Getter @Setter private String lastName;
    @Getter @Setter private Timestamp createdAt;
    @Getter @Setter private Timestamp updatedAt;
}
