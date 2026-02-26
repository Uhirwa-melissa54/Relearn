package com.relean.lean.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class ApiException extends RuntimeException {

    private HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }


}
