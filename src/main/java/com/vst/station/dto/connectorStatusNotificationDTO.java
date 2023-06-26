package com.vst.station.dto;

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
public class connectorStatusNotificationDTO {
	
	private int connectorNumber;	
	private String connectorStatus;
	private String connectorErrorCode;
	private String connectorInfo;
	private String connectorTimeStamp;
	
}
