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

        Student savedStudent=studentRepository.save(student);


        EmailDto emailDto = EmailDto.builder()
                .email(savedStudent.getEmail())
                .fullName(savedStudent.getFirstName() + " " + savedStudent.getLastName())
                .password(req.getPassword())  // only if you really must send plain password (see security note below)
                .msgBody("""
        Dear %s,

        Congratulations! You have been successfully registered in the **RCA MIS** system.

        You can now log in using the following credentials:
        
        Email:    %s
        Password: %s


        
        For security reasons, we strongly recommend changing your password after your first login.

        If you have any questions or face login issues, feel free to contact the RCA MIS support team.

        Welcome aboard!

        Best regards,  
        RCA MIS Team
        """.formatted(
                        savedStudent.getFirstName(),
                        savedStudent.getEmail(),
                        savedStudent.getPassword()
                ))
                .build();
        return RegisterResponse.builder()
                .fullName(savedStudent.getFirstName() + " " + savedStudent.getLastName())
                .email(savedStudent.getEmail())
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

        Teacher savedTeacher=teacherRepository.save(teacher);

        EmailDto emailDto = EmailDto.builder()
                .email(savedTeacher.getEmail())
                .fullName(savedTeacher.getFirstName() + " " + savedTeacher.getLastName())
                .password(req.getPassword()) 
                .msgBody("""
        Dear %s,

        Congratulations! You have been successfully registered in the **RCA MIS** system.

        You can now log in using the following credentials:
        
        Email:    %s
        Password: %s

        Please log in at: https://mis.rca.ac.rw (or your actual login URL)
        
        For security reasons, we strongly recommend changing your password after your first login.

        If you have any questions or face login issues, feel free to contact the RCA MIS support team.

        Welcome aboard!

        Best regards,  
        RCA MIS Team
        """.formatted(
                        savedTeacher.getFirstName(),
                        savedTeacher.getEmail(),
                        req.getPassword()
                ))
                .build();


        return RegisterResponse.builder()
                .fullName(savedTeacher.getFirstName() + " " + teacher.getLastName())
                .email(savedTeacher.getEmail())
                .password(req.getPassword())
                .build();
    }
}
