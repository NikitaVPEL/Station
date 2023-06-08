package com.vst.station.dto;


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
public class StationDTO1 {
	
	@Id
    private String stationId;
    private String stationName;
    private String stationArea;
    private String stationCity;
    private double stationLatitude;
    private double stationLongitude;
    private String stationStatus;

    
    
   
}