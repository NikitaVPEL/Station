package com.vst.station.dto;


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
public class ConnectorDTO {

	@Id
	private String connectorId;
	private int connectorNumber;
	private String connectorType;
	private String connectorSocket;
	private String connectorStatus;
	private String connectorOutputPower;
	private String connectorCharges;
	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private boolean isActive;
	
	
	
	
}

