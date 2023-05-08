package com.vst.station.dto;

import java.util.ArrayList;
import java.util.List;

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
public class StationDTO2 {
	
	private String stationId;
	private String stationLocation;
    private String stationName;
    private String stationLocationURL;
    private String stationWorkingTime;
    private List<String> stationAmenity = new ArrayList<>();
	
    
    
}
