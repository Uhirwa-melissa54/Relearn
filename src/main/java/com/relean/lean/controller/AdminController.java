package com.relean.lean.controller;

import com.relean.lean.dtos.*;
import com.relean.lean.entities.Classroom;
import com.relean.lean.entities.Course;
import com.relean.lean.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/create-course")
    public ResponseEntity<Course> createCourse(@RequestBody CourseDto req) {
        return ResponseEntity.ok(adminService.createCourse(req));
    }

    @PostMapping("/create-classroom")
    public ResponseEntity<Classroom> createClassroom(@RequestBody ClassroomDto req) {
        return ResponseEntity.ok(adminService.createClassroom(req));
    }

    @PatchMapping("/assign-course/{courseId}/teacher/{teacherId}")
    public ResponseEntity<Course> assignCourseToTeacher(
            @PathVariable Long courseId,
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(adminService.assignCourseToTeacher(courseId, teacherId));
    }

    @PostMapping("/enroll-students")
    public ResponseEntity<String> enrollStudentsToCourse(@RequestBody EnrollmentRequestDto req) {
        adminService.enrollStudentsToCourse(req);
        return ResponseEntity.ok("Students enrolled successfully");
    }



    @PostMapping("/register-student")
    public ResponseEntity<RegisterResponse> registerStudent(
            @RequestBody StudentRegisterRequestDto req
    ) {
        RegisterResponse response = adminService.registerStudent(req);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register-teacher")
    public ResponseEntity<RegisterResponse> registerTeacher(
            @RequestBody TeacherRequestDto req
    ) {
        RegisterResponse response = adminService.registerTeacher(req);
        return ResponseEntity.ok(response);
    }
}

