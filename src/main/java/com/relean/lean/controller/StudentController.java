package com.relean.lean.controller;

import com.relean.lean.dtos.LoginRequest;
import com.relean.lean.dtos.LoginResponse;
import com.relean.lean.entities.Submission;
import com.relean.lean.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(studentService.login(request));
    }

    @GetMapping("/{studentId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissions(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getSubmissions(studentId));
    }
}
