package com.vst.station.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class StationErrorResponse {

	private String message;
    private HttpStatus status;
    private String statusCode;
    private LocalDateTime timeStamp;
}

