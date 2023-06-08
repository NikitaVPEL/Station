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
public class ChargerDTO1 {
	
	private String chargerId;
	private String chargerName;
	private int chargerNumberOfConnector;
	private List<ConnectorDTO> connectors= new ArrayList<>();
	
	
	
	

}
