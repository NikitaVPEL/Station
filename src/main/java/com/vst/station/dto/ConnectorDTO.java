package com.vst.station.dto;


import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ConnectorDTO {

//	@Id
//	private String connectorId;
//	
//	private int connectorNumber;
//	
//	@NotBlank(message = "Please Enter valid Connector Type")
//	@NotNull(message = "Please Enter valid Connector Type")
//	private String connectorType;
//	
//	@NotBlank(message = "Please Enter valid Connector Socket")
//	@NotNull(message = "Please Enter valid Connector Socket")
//	private String connectorSocket;
//	
////	@NotBlank(message = "Please Enter valid Connector Status")
////	@NotNull(message = "Please Enter valid Connector status")
//	private String connectorStatus;
//	private Date connectorLastUnavailableTimeStamp;
//	private String connectorLastAvailableTimeStamp;
//	
//	@NotBlank(message = "Please Enter valid Connector Output Power")
//	@NotNull(message = "Please Enter valid Connector Output Power")
//	@Pattern(regexp = "^\\d+(\\.\\d+)?$",message="Enter correct connector output power")
//	private String connectorOutputPower;
//	
//	@NotBlank(message = "Please Enter valid Connector Charges")
//	@NotNull(message = "Please Enter valid Connector Charges")
//	private String connectorCharges;
//	private Integer connectorMeterValue;
//	private String connectorMeterRequestTimeStamp;
//	private String connectorErrorCode;
//	private String connectorInfo;
//	private String connectorTimeStamp;
//	private Date createdDate;
//	private Date modifiedDate;
//	private String createdBy;
//	private String modifiedBy;
//	private boolean isActive;
	
	@Id
	private String connectorId;
	private Integer connectorNumber;
	private String connectorType;
	private String connectorSocket;
	private String connectorStatus;
	private Date connectorLastUnavailableTimeStamp;
	private Date connectorLastAvailableTimeStamp;
	private String connectorOutputPower;
	private String connectorCharges;
	private Integer connectorMeterValue;
	private String connectorErrorCode;
	private String connectorInfo;
	private String connectorTimeStamp;
	private String connectorMeterRequestTimeStamp;
	private String connectorUnitType;
	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private boolean isActive;
		
	
	

	
	
	
	
	
	
	
}

