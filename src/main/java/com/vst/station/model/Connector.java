package com.vst.station.model;


import java.util.Date;

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
public class Connector {

	@Id
	private String connectorId;
	private int connectorNumber;
	private String connectorType;
	private String connectorSocket;
	private String connectorStatus;
	private Date connectorLastUnavailableTimeStamp;
	private String connectorLastAvailableTimeStamp;
	private String connectorOutputPower;
	private String connectorCharges;
	private String connectorErrorCode;
	private String connectorInfo;
	private String connectorTimeStamp;
	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private boolean isActive;
		
		
}
