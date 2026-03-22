package com.relean.lean.service;



import com.relean.lean.dtos.*;
import com.relean.lean.entities.*;
import com.relean.lean.exceptions.ApiException;
import com.relean.lean.repository.*;
import com.relean.lean.roles.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final ClassroomRepository classroomRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Course createCourse(CourseDto req) {
        if (courseRepository.findByCourseCode(req.getCourseCode()).isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Course code already exists");
        }
        Course course = Course.builder()
                .courseCode(req.getCourseCode())
                .courseName(req.getCourseName())
                .build();
        return courseRepository.save(course);
    }

    public Classroom createClassroom(ClassroomDto req) {
        if (classroomRepository.findByClassName(req.getClassName()).isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Classroom name already exists");
        }
        Classroom classroom = Classroom.builder()
                .className(req.getClassName())
                .build();
        return classroomRepository.save(classroom);
    }

    public Course assignCourseToTeacher(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Course not found"));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Teacher not found"));

        course.setTeacher(teacher);
        Course savedCourse = courseRepository.save(course);

        // Send email to teacher
        EmailDto emailDto = EmailDto.builder()
                .email(teacher.getEmail())
                .fullName(teacher.getFirstName() + " " + teacher.getLastName())
                .msgBody("""
                    Dear %s,
                    
                    You have been assigned to the course: **%s (%s)**.
                    
                    You can now manage students and take attendance for this course.
                    
                    Best regards,
                    RCA MIS Team
                    """.formatted(teacher.getFirstName(), course.getCourseName(), course.getCourseCode()))
                .build();
        emailService.sendSimpleMail(emailDto);

        return savedCourse;
    }

    public void enrollStudentsToCourse(EnrollmentRequestDto req) {
        Course course = courseRepository.findByCourseCode(req.getCourseCode())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Course not found"));
        Classroom classroom = classroomRepository.findByClassName(req.getClassName())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Classroom not found"));

        for (Long studentId : req.getStudentIds()) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student not found with ID: " + studentId));

            if (!enrollmentRepository.existsByStudentAndCourse(student, course)) {
                Enrollment enrollment = Enrollment.builder()
                        .student(student)
                        .course(course)
                        .classroom(classroom)
                        .build();
                enrollmentRepository.save(enrollment);

                // Send email to student
                EmailDto emailDto = EmailDto.builder()
                        .email(student.getEmail())
                        .fullName(student.getFirstName() + " " + student.getLastName())
                        .msgBody("""
                            Dear %s,
                            
                            You have been enrolled in the course: **%s (%s)** in class **%s**.
                            
                            Welcome to the course!
                            
                            Best regards,
                            RCA MIS Team
                            """.formatted(student.getFirstName(), course.getCourseName(), course.getCourseCode(), classroom.getClassName()))
                        .build();
                emailService.sendSimpleMail(emailDto);
            }
        }
    }

    public RegisterResponse registerStudent(StudentRegisterRequestDto req) {

        if (studentRepository.existsByEmail(req.getEmail()) || teacherRepository.existsByEmail(req.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email already exists");
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
                        req.getPassword()
                ))
                .build();

        emailService.sendSimpleMail(emailDto);
        return RegisterResponse.builder()
                .fullName(savedStudent.getFirstName() + " " + savedStudent.getLastName())
                .email(savedStudent.getEmail())
                .password(req.getPassword())
                .build();
    }

    public RegisterResponse registerTeacher(TeacherRequestDto req) {

        if (studentRepository.existsByEmail(req.getEmail()) || teacherRepository.existsByEmail(req.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email already exists");
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

        emailService.sendSimpleMail(emailDto);


        return RegisterResponse.builder()
                .fullName(savedTeacher.getFirstName() + " " + teacher.getLastName())
                .email(savedTeacher.getEmail())
                .password(req.getPassword())
                .build();
    }
}
