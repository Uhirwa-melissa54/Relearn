package com.relean.lean.service;

import com.relean.lean.dtos.AttendanceRequestDto;
import com.relean.lean.entities.*;
import com.relean.lean.exceptions.ApiException;
import com.relean.lean.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public List<Student> getStudentsByCourse(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Course not found"));
        return enrollmentRepository.findByCourse(course).stream()
                .map(Enrollment::getStudent)
                .collect(Collectors.toList());
    }

    public void takeAttendance(List<AttendanceRequestDto> reqs) {
        for (AttendanceRequestDto req : reqs) {
            Course course = courseRepository.findByCourseCode(req.getCourseCode())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Course not found"));
            Student student = studentRepository.findById(req.getStudentId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student not found"));

            Attendance attendance = Attendance.builder()
                    .course(course)
                    .student(student)
                    .date(req.getDate() != null ? req.getDate() : LocalDate.now())
                    .status(req.getStatus())
                    .reason(req.getReason())
                    .build();
            attendanceRepository.save(attendance);
        }
    }

    public List<Attendance> getAbsentStudents(String courseCode, LocalDate date) {
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Course not found"));
        return attendanceRepository.findByCourseAndDateAndStatus(course, date, AttendanceStatus.ABSENT);
    }
}
