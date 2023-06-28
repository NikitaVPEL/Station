package com.vst.station.model;

//import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import com.mongodb.internal.connection.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "station")
public class Station {

	@Id
	private String stationId;
	private Location location= new Location();
	private String stationName;
	private String stationHostId;
	private String stationVendorId;
	private String stationArea;
	private String stationAddressLineOne;
	private String stationAddressLineTwo;
	private String stationZipCode;
	private String stationCity;
	private double stationLatitude;
	private double stationLongitude;
	private String stationLocationURL;
	private String stationParkingArea;
	private String stationContactNumber;
	private String stationOpeningTime;
	private String stationClosingTime;
	private int chargerNumber;
	private String stationParkingType;
	private String stationShareId;
	private String stationStatus;
	private String stationPowerStandard;
	private List<String> stationAmenity= new ArrayList<>();
	private List<Charger> chargers =new ArrayList<>();
	private List<String> userAccessList =  new ArrayList<>(); 
	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private boolean isActive;
		
}