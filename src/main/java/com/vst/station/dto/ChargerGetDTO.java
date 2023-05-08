package com.vst.station.dto;

import java.util.Date;
import java.util.List;

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
public class ChargerGetDTO {
	
	
	@Id
	private String chargerId;
	private String chargerName;
	private int chargerNumber;
	private String chargerInputVoltage;
	private String chargerOutputVoltage;
	private String chargerMinInputAmpere;
	private String chargerMaxInputAmpere;
	private String chargerOutputAmpere;
	private String chargerInputFrequency;
	private String chargerOutputFrequency;
	private String chargerIPRating;
	private String chargerMountType;
	private String isRFID;
	private String chargerSerialNumber;
	private String chargerOCPPProtocol;
	private String chargerConnectorType;
	private String isAppSupport;
	private String isTBCutOff;
	private String isAntitheft;
	private String isLEDDisplay;
	private String isLEDIndications;
	private String isSmart;
	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	
	
	
}
