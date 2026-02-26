package com.relean.lean.controller;

import com.relean.lean.dtos.EmailDto;
import com.relean.lean.entities.ForgotPasswordRequest;
import com.relean.lean.entities.PasswordReset;

import com.relean.lean.entities.Student;
import com.relean.lean.repository.PasswordResetRepository;
import com.relean.lean.repository.StudentRepository;
import com.relean.lean.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordResetRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {

        Optional<Student> userOptional = studentRepository.findByEmail(request.getEmail());

        // Always return same response (security reason)
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok("If account exists, OTP sent.");
        }

        Student user= userOptional.get();

        int otp = generateOtp();

        PasswordReset resetOtp = new PasswordReset();
        resetOtp.setUserId(user.getStudentId());
        resetOtp.setOtpHash(passwordEncoder.encode(String.valueOf(otp)));
        resetOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        resetOtp.setAttempts(0);
        resetOtp.setUsed(false);
        resetOtp.setCreatedAt(LocalDateTime.now());

        otpRepository.save(resetOtp);
        EmailDto emailDto = EmailDto.builder()
                .email(request.getEmail())
                .fullName(user.getFirstName())
                .password("No need for password")
                .msgBody(
                        "Dear " + user.getFirstName() + ",\n\n" +
                                "We received a request to reset your password.\n\n" +
                                "Your One-Time Password (OTP) is: " + otp + "\n\n" +
                                "This code will expire in 5 minutes.\n\n" +
                                "If you did not request this, please ignore this email.\n\n" +
                                "Best regards,\n" +
                                "Relean Team"
                )
                .build();
        emailService.sendSimpleMail(emailDto);

        return ResponseEntity.ok("If account exists, OTP sent.");
    }

    private int generateOtp() {
        SecureRandom random = new SecureRandom();
        return 100000 + random.nextInt(900000);
    }
}