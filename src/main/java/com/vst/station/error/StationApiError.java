package com.vst.station.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vst.station.exception.InValidDataException;
import com.vst.station.exception.InValidIdExcepetion;
import com.vst.station.exception.StationException;
import com.vst.station.exception.StationIdNotAcceptableException;
import com.vst.station.exception.StationNotFoundException;

@RestControllerAdvice
public class StationApiError {

	String message = "error";

	@ExceptionHandler(StationNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, Object> userNotFound(StationNotFoundException ex) {
		Map<String, Object> errorMap = new HashMap<>();
		StationErrorResponse response = new StationErrorResponse();
		response.setMessage(ex.getMessage());
		response.setStatus(HttpStatus.NOT_FOUND);
		response.setStatusCode("404");
		response.setTimeStamp(LocalDateTime.now());
		errorMap.put(message, response);
		return errorMap;
	}

	@ExceptionHandler(StationIdNotAcceptableException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public Map<String, Object> idNotFound(StationIdNotAcceptableException ex) {
		Map<String, Object> errorMap = new HashMap<>();
		StationErrorResponse response = new StationErrorResponse();
		response.setMessage(ex.getMessage());
		response.setStatus(HttpStatus.NOT_ACCEPTABLE);
		response.setStatusCode("406");
		response.setTimeStamp(LocalDateTime.now());
		errorMap.put(message, response);
		return errorMap;
	}

	@ExceptionHandler(InValidIdExcepetion.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public Map<String, Object> idNotValied(InValidIdExcepetion ex) {
		Map<String, Object> errorMap = new HashMap<>();
		StationErrorResponse response = new StationErrorResponse();
		response.setMessage(ex.getMessage());
		response.setStatus(HttpStatus.NOT_ACCEPTABLE);
		response.setStatusCode("406");
		response.setTimeStamp(LocalDateTime.now());
		errorMap.put(message, response);
		return errorMap;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}
	
	@ExceptionHandler(InValidDataException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, Object> inValidData(InValidDataException ex) {
		Map<String, Object> errorMap = new HashMap<>();
		StationErrorResponse response = new StationErrorResponse();
		response.setMessage(ex.getMessage());
		response.setStatus(HttpStatus.NOT_FOUND);
		response.setStatusCode("404");
		response.setTimeStamp(LocalDateTime.now());
		errorMap.put(message, response);
		return errorMap;
	}
	
	
	
	

}
