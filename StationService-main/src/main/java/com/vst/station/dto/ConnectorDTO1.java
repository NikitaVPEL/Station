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
public class ConnectorDTO1 {
	
	private String connectorId;
	private String connectorType;
	private String connectorSocket;
	private String connectorStatus;
	private String connectorOutputPower;
	private String connectorCharges;
	
	
	
	
}
