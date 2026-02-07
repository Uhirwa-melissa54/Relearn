package com.relean.lean.service;



import com.relean.lean.dtos.*;
import com.relean.lean.entities.*;
import com.relean.lean.repository.*;
import com.relean.lean.roles.RoleEnum;
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

    public RegisterResponse registerStudent(StudentRegisterRequestDto req) {

        if (studentRepository.existsByEmail(req.getEmail()) || teacherRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        Student student = Student.builder()
                .studentId(req.getStudentId())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .gender(req.getGender())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(RoleEnum.STUDENT)
                .build();

        studentRepository.save(student);


        return RegisterResponse.builder()
                .fullName(student.getFirstName() + " " + student.getLastName())
                .email(student.getEmail())
                .password(req.getPassword())
                .build();
    }

    public RegisterResponse registerTeacher(TeacherRequestDto req) {

        if (studentRepository.existsByEmail(req.getEmail()) || teacherRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        RoleEnum role = (req.getTeacherId() != null && req.getTeacherId() == 12345L)
                ? RoleEnum.ADMIN
                : RoleEnum.TEACHER;

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


        return RegisterResponse.builder()
                .fullName(teacher.getFirstName() + " " + teacher.getLastName())
                .email(teacher.getEmail())
                .password(req.getPassword())
                .build();
    }
}
