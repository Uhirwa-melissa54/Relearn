package com.relean.lean.repository;

import com.relean.lean.entities.Enrollment;
import com.relean.lean.entities.Student;
import com.relean.lean.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByCourse(Course course);
    List<Enrollment> findByStudent(Student student);
    boolean existsByStudentAndCourse(Student student, Course course);
}
