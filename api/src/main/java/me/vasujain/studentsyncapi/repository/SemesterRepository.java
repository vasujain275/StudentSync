package me.vasujain.studentsyncapi.repository;

import me.vasujain.studentsyncapi.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, UUID> {
}
