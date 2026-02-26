package com.relean.lean.repository;
import com.relean.lean.entities.PasswordReset;
import com.relean.lean.entities.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
}
