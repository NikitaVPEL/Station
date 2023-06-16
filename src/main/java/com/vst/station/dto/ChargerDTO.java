package com.vst.station.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class ChargerDTO {

	@Id
	private String chargerId;
	
	@NotBlank(message = "Please Enter Valid charger name ")
	@NotNull(message = "Please Enter Valid charger name ")
	private String chargerName;
	
	private int chargerNumber;
	
	@NotBlank(message = "Please Enter valid Charger Input Voltage")
	@NotNull(message = "Please Enter valid Charger Input Voltage")
	@Pattern(regexp = "^\\d+(\\.\\d+)?$",message="Enter correct charger input voltage")
	private String chargerInputVoltage;
	
	@NotBlank(message = "Please Enter valid Charger Output Voltage")
	@NotNull(message = "Please Enter valid Charger Output Voltage")
	@Pattern(regexp = "^\\d+(\\.\\d+)?$",message="Enter correct charger output voltage")
	private String chargerOutputVoltage;
	
	@NotBlank(message = "Please Enter valid Charger Minimum Input Ampere")
	@NotNull(message = "Please Enter valid Charger Minimum Input Ampere")
	@Pattern(regexp = "^\\d+(\\.\\d+)?$",message="Enter correct charger Minimum Input Ampere")
	private String chargerMinInputAmpere;
	
	@NotBlank(message = "Please Enter valid Charger Maximum Input Ampere")
	@NotNull(message = "Please Enter valid Charger Maximum Input Ampere")
	@Pattern(regexp = "^\\d+(\\.\\d+)?$",message="Enter correct charger Maximum Input Ampere")
	private String chargerMaxInputAmpere;
	
	@NotBlank(message = "Please Enter valid Charger Output Ampere")
	@NotNull(message = "Please Enter valid Charger Output Ampere")
	@Pattern(regexp = "^\\d+(\\.\\d+)?$",message="Enter correct charger output Ampere")
	private String chargerOutputAmpere;
	
	@NotBlank(message = "Please Enter valid Charger Input Frequency")
	@NotNull(message = "Please Enter valid Charger Input Frequency")
	@Pattern(regexp = "^\\d+(\\.\\d+)?$",message="Enter correct charger Input Frequency")
	private String chargerInputFrequency;
	
	@NotBlank(message = "Please Enter valid Charger Output Frequency")
	@NotNull(message = "Please Enter valid Charger Output Frequency")
	@Pattern(regexp = "^\\d+(\\.\\d+)?$",message="Enter correct charger output Ampere")
	private String chargerOutputFrequency;
	
	@NotBlank(message = "Please Enter valid Charger IP Rating")
	@NotNull(message = "Please Enter valid Charger IP Rating")
	private String chargerIPRating;
	
	@NotBlank(message = "Please Enter valid Charger Mount Type")
	@NotNull(message = "Please Enter valid Charger Mount Type ")
	private String chargerMountType;

	private int chargerNumberOfConnector;
	
	@NotBlank(message = "Please Enter valid RFID ")
	@NotNull(message = "Please Enter valid RFID ")
	private String isRFID;
	
	@NotBlank(message = "Please Enter valid Charger Serial Number ")
	@NotNull(message = "Please Enter valid Charger Serial Number")
	private String chargerSerialNumber;
	
	@NotBlank(message = "Please Enter valid Charger OCPP Protocol")
	@NotNull(message = "Please Enter valid Charger OCPP Protocol")
	private String chargerOCPPProtocol;
	
	@NotBlank(message = "Please Enter valid Charger Connector Type ")
	@NotNull(message = "Please Enter valid Charger Connector Type")
	private String chargerConnectorType;
	
	@NotBlank(message = "Please Enter valid App Support")
	@NotNull(message = "Please Enter valid App Support")
	private String isAppSupport;
	
	@NotBlank(message = "Please Enter valid TB CutOff ")
	@NotNull(message = "Please Enter valid TB CutOff")
	private String isTBCutOff;
	
	@NotBlank(message = "Please Enter valid Antitheft")
	@NotNull(message = "Please Enter valid Antitheft")
	private String isAntitheft;
	
	@NotBlank(message = "Please Enter valid LED Display")
	@NotNull(message = "Please Enter valid LED Display")
	private String isLEDDisplay;
	
	@NotBlank(message = "Please Enter valid LED Indications")
	@NotNull(message = "Please Enter valid LED Indications")
	private String isLEDIndications;
	
	@NotBlank(message = "Please Enter valid Smart")
	@NotNull(message = "Please Enter valid Smart")
	private String isSmart;
	
	@NotBlank(message = "Please Enter valid Charger Status")
	@NotNull(message = "Please Enter valid Charger Status")
	private String chargerStatus;
	
	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private boolean isActive;
	private List<ConnectorDTO> connectors= new ArrayList<>();
	
	
	
	
}
