package com.vst.station.dto;

import com.vst.station.model.Location;

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
	private Location location;
	private String stationName;
	private String stationStatus;

}
