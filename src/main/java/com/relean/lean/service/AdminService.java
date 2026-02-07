package com.relean.lean.service;



import com.relean.lean.dtos.*;
import com.relean.lean.entities.*;
import com.relean.lean.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public  registerStudent( StudentRegisterRequestDto req) {

        if (studentRepository.existsByEmail(req.getEmail()) || teacherRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        Student student = Student.builder()
                .studentId(req.getStudentId())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .gender(req.getGender())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(Role.STUDENT)
                .build();

        studentRepository.save(student);

        String token = jwtService.generateToken(student.getEmail(), student.getRole());

        return AuthResponse.builder()
                .token(token)
                .role(student.getRole())
                .id(student.getStudentId())
                .fullName(student.getFirstName() + " " + student.getLastName())
                .build();
    }

    public AuthResponse registerTeacher(RegisterTeacherRequest req) {

        if (studentRepository.existsByEmail(req.getEmail()) || teacherRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        Role role = (req.getTeacherId() != null && req.getTeacherId() == 12345L)
                ? Role.ADMIN
                : Role.TEACHER;

        Teacher teacher = Teacher.builder()
                .teacherId(req.getTeacherId())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .gender(req.getGender())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .build();

        teacherRepository.save(teacher);

        String token = jwtService.generateToken(teacher.getEmail(), teacher.getRole());

        return AuthResponse.builder()
                .token(token)
                .role(teacher.getRole())
                .id(teacher.getTeacherId())
                .fullName(teacher.getFirstName() + " " + teacher.getLastName())
                .build();
    }
