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

public class StationFindDTO {

	private String stationName;

	private String stationArea;
	private String stationAddressLineOne;
	private String stationAddressLineTwo;
	private String stationZipCode;
	private String stationCity;

}
