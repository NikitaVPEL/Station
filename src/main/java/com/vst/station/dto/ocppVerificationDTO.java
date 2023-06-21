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
public class ocppVerificationDTO {
	
	
	private String chargePointVendor;
	private String chargePointModel;
	private String chargeBoxSerialNumber;
	private String meterType;
	private String firmwareVersion;

}
