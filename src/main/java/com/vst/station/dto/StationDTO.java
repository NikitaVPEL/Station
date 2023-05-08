package com.vst.station.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StationDTO {

	@Id
	private String stationId;
	
	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
//	@Pattern(regexp = "^[a-zA-Z0-9]*$",message = "Please Enter Correct Name")
	private String stationName;

	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	private String stationHostId;
	
	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	private String stationVendorId;
	
	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	private String stationArea;
	
	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	private String stationAddressLineOne;
	
	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	private String stationAddressLineTwo;


	@NotNull(message = "This PIN Code cannot be left blank. Please enter a valid input to continue.")
	@Pattern(regexp = "^[1-9][0-9]{5}$", message = "Please Enter Correct PIN Code")
	@NotBlank(message = "Please enter a value. PIN Code cannot be left blank.")
	private String stationZipCode;

	@Pattern(regexp = "^[A-Za-z]+(?:[ -][A-Za-z]+)*$", message = "Please Check Enter City Name. Try Again.")
	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	private String stationCity;

//	@NotBlank(message = "Please enter a value.")
//	@NotNull(message = "Please enter a value.")
	private double stationLatitude;

//	@NotBlank(message = "Please enter a value.")
//	@NotNull(message = "Please enter a value.")
	private double stationLongitude;

	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	@URL(message ="Please Check URL. Try Again")
	private String stationLocationURL;

	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	private String stationParkingArea;

	@NotBlank(message = "Please enter a Contact. Contact Details cannot be left blank.")
	@NotNull(message = "This PIN Code cannot be left blank. Please enter a valid input to continue.")
//	@Pattern(regexp = ("(0|91)?[6-9][0-9]{9}"), message = "please Enter Valid ContactNo")
	private String stationContactNumber;
	
	@NotBlank(message ="Please Enter Valid Working Time")
	@NotNull(message ="Please Enter Valid Working Time")
//	@Pattern(regexp = "^(24\\/7)|(([01]\\d|2[0-3]):[0-5]\\d-([01]\\d|2[0-3]):[0-5]\\d)$",message="Please Enter Correct Time")
	private String stationWorkingTime;
	
//	@NotBlank(message = "Please enter a value.")
//	@NotNull(message = "Please enter a value.")
	private int chargerNumber;
	
	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	@Pattern(regexp = "^[a-zA-Z]*$", message="Enter Valid Parking Type")
	private String stationParkingType;
	
	private List<String> stationAmenity = new ArrayList<>();
	private List<ChargerDTO> chargers = new ArrayList<>();
	
	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	private String stationStatus;
	
	@NotBlank(message = "Please enter a value.")
	@NotNull(message = "Please enter a value.")
	private String stationPowerStandard;
	
	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private boolean isActive;

}