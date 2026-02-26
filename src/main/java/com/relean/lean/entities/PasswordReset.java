package com.relean.lean.entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String otpHash;

    private LocalDateTime expiresAt;

    private int attempts;

    private boolean used;

    private LocalDateTime createdAt;
}
