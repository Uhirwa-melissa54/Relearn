package com.relean.lean.dtos;

import com.relean.lean.entities.AttendanceStatus;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AttendanceRequestDto {
    private Long studentId;
    private String courseCode;
    private LocalDate date;
    private AttendanceStatus status;
    private String reason;
}
