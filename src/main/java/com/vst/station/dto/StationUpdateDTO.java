package com.vst.station.dto;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Pattern;
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
public class StationUpdateDTO {

//	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Please Enter Correct Name")
	private String stationName;

	private String stationHostId;

	private String stationVendorId;

	private String stationArea;

	private String stationAddressLineOne;

	private String stationAddressLineTwo;

	@Pattern(regexp = "^[1-9][0-9]{5}$", message = "Please Enter Correct PIN Code")
	private String stationZipCode;

	@Pattern(regexp = "^[A-Za-z]+(?:[ -][A-Za-z]+)*$", message = "Please Check Enter City Name. Try Again.")
	private String stationCity;

	private double stationLatitude;

	private double stationLongitude;

	@URL(message = "Please Check URL. Try Again")
	private String stationLocationURL;

	private String stationParkingArea;

	@Pattern(regexp = ("(0|91)?[6-9][0-9]{9}"), message = "please Enter Valid ContactNo")
	private String stationContactNumber;

//	@Pattern(regexp = "^(24\\/7)|(([01]\\d|2[0-3]):[0-5]\\d-([01]\\d|2[0-3]):[0-5]\\d)$", message = "Please Enter Correct Time")
	private String stationWorkingTime;

	private int chargerNumber;

	@Pattern(regexp = "^[a-zA-Z]*$", message = "Enter Valid Parking Type")
	private String stationParkingType;

	private List<String> stationAmenity = new ArrayList<>();
	private List<ChargerDTO> chargers = new ArrayList<>();
	private String stationShareId;

	private String stationStatus;

	private String stationPowerStandard;

}
