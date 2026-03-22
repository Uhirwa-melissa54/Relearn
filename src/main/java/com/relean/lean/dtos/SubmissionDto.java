package com.relean.lean.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SubmissionDto {
    private String courseCode;
    private String title;
    private String content;
    private LocalDateTime submittedAt;
}
