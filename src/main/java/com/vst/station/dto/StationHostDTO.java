package com.vst.station.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StationHostDTO {

	private String stationId;
	private double stationLatitude;
	private double stationLongitude;
	private String stationName;
	private String stationStatus;

}
