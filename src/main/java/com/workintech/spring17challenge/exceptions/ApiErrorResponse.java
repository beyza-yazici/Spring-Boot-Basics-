package com.workintech.spring17challenge.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private int status;
    private String message;
    private long timestamp;
}
