package com.relean.lean.service;



import com.relean.lean.dtos.LoginRequest;
import com.relean.lean.dtos.LoginResponse;
import com.relean.lean.entities.Student;
import com.relean.lean.entities.Teacher;
import com.relean.lean.exceptions.ApiException;
import com.relean.lean.repository.StudentRepository;
import com.relean.lean.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        Teacher teacher =teacherRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), teacher.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid password or email");
        }

        String accessToken = jwtService.generateAccessToken(
                teacher.getEmail(),
                teacher.getRole()
        );

        String refreshToken = jwtService.generateRefreshToken(
                teacher.getEmail()
        );

        return LoginResponse.builder()
                .email(teacher.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

