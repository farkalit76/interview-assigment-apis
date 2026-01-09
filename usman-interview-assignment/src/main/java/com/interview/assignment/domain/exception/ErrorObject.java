package com.interview.assignment.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorObject {

    private String code;
    private String error;
    private LocalDateTime timestamp;
}
