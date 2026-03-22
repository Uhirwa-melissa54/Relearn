package com.relean.lean.repository;

import com.relean.lean.entities.Attendance;
import com.relean.lean.entities.Course;
import com.relean.lean.entities.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByCourseAndDate(Course course, LocalDate date);
    List<Attendance> findByCourseAndDateAndStatus(Course course, LocalDate date, AttendanceStatus status);
}
