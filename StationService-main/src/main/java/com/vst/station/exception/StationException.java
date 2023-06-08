package com.vst.station.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StationException extends RuntimeException { 
	

	private static final long serialVersionUID = -7136395761401538992L;
	
	private String serviceCode;
	private String serviceName;
	private String className;
	private String methodName;
	private int lineNumber;
	private String functionality;
	private String message;
	
	

	
	
}
