package com.vst.station.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChargerUpdateDTO {

	@Id
	private String chargerId;

	private String chargerName;

	private int chargerNumber;

	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Enter correct charger input voltage")
	private String chargerInputVoltage;

	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Enter correct charger output voltage")
	private String chargerOutputVoltage;

	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Enter correct charger MinInput Ampere")
	private String chargerMinInputAmpere;

	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Enter correct charger MaxInput Ampere")
	private String chargerMaxInputAmpere;

	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Enter correct charger output Ampere")
	private String chargerOutputAmpere;

	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Enter correct charger Input Frequency")
	private String chargerInputFrequency;

	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Enter correct charger output Ampere")
	private String chargerOutputFrequency;

	private String chargerIPRating;

	private String chargerMountType;

	private int chargerNumberOfConnector;

	private String isRFID;

	private String chargerPointSerialNumber;

	private String chargerOCPPProtocol;

	private String chargerConnectorType;

	private String isAppSupport;

	private String isTBCutOff;

	private String isAntitheft;

	private String isLEDDisplay;

	private String isLEDIndications;

	private String isSmart;

	private String chargerStatus;

	private String chargePointVendor;
	
	private String chargePointModel;

	private String chargeBoxSerialNumber;

	private String meterType;

	private String firmwareVersion;

	private String chargerSerialNumber;

	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private boolean isActive;
	private List<ConnectorDTO> connectors = new ArrayList<>();

}
