package com.relean.lean.dtos;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EnrollmentRequestDto {
    private List<Long> studentIds;
    private String courseCode;
    private String className;
}
