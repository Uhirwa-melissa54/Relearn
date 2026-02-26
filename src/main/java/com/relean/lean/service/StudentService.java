package com.relean.lean.service;

import com.relean.lean.dtos.LoginRequest;
import com.relean.lean.dtos.LoginResponse;
import com.relean.lean.entities.Student;
import com.relean.lean.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        Student student = studentRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String accessToken = jwtService.generateAccessToken(
                student.getEmail(),
                student.getRole()
        );

        String refreshToken = jwtService.generateRefreshToken(
                student.getEmail()
        );

        return LoginResponse.builder()
                .email(student.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
