package com.relean.lean.repository;

import com.relean.lean.entities.Submission;
import com.relean.lean.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudent(Student student);
}
