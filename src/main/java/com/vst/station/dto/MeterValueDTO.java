package com.vst.station.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MeterValueDTO {
	
	private Integer connectorNumber;
	private double meterValue;
	private String connectorUnitType;
	private String timeStamp;

}
