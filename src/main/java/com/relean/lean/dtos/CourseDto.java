package com.relean.lean.dtos;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CourseDto {
    private String courseCode;
    private String courseName;
}
