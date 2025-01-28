package me.vasujain.studentsyncapi.repository;

import me.vasujain.studentsyncapi.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> {
}
