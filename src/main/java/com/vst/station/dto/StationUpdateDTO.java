package com.vst.station.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.URL;

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

	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Enter a valid station parking area")
	private String stationParkingArea;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Please enter a valid 10-digit contact number.")
	private String stationContactNumber;

	private String stationOpeningTime;

	private String stationClosingTime;

	private int chargerNumber;

	private String stationParkingType;

	private List<String> stationAmenity = new ArrayList<>();
	private List<ChargerDTO> chargers = new ArrayList<>();
	private String stationShareId;

	private String stationStatus;

	private String stationPowerStandard;

}
