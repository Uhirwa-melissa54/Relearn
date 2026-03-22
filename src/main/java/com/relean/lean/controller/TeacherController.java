package com.relean.lean.controller;

import com.relean.lean.dtos.AttendanceRequestDto;
import com.relean.lean.entities.Attendance;
import com.relean.lean.entities.Student;
import com.relean.lean.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/course/{courseCode}/students")
    public ResponseEntity<List<Student>> getStudentsByCourse(@PathVariable String courseCode) {
        return ResponseEntity.ok(teacherService.getStudentsByCourse(courseCode));
    }

    @PostMapping("/attendance")
    public ResponseEntity<String> takeAttendance(@RequestBody List<AttendanceRequestDto> reqs) {
        teacherService.takeAttendance(reqs);
        return ResponseEntity.ok("Attendance recorded successfully");
    }

    @GetMapping("/attendance/absent")
    public ResponseEntity<List<Attendance>> getAbsentStudents(
            @RequestParam String courseCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(teacherService.getAbsentStudents(courseCode, date));
    }
}
