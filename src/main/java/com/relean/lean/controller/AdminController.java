package com.relean.lean.controller;

import com.relean.lean.dtos.RegisterResponse;
import com.relean.lean.dtos.StudentRegisterRequestDto;
import com.relean.lean.dtos.TeacherRequestDto;
import com.relean.lean.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;



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

