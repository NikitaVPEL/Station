package com.vst.station.service;

import java.util.List;

import com.vst.station.dto.ConnectorDTO;
import com.vst.station.dto.ConnectorUpdateDTO;
import com.vst.station.dto.MeterValueDTO;
import com.vst.station.model.Connector;

public interface ConnectorServiceInterface {
	
	public boolean addConnector(String stationId, String chargerId, ConnectorDTO connectorDTO);
	
	public boolean updateConnectorById(String connectorId, ConnectorUpdateDTO connectorDTO);

	public boolean removeConnector(String connectorId);
	
	List<Connector> getAllStationConnector(String stationId, String chargerId);

	Connector getConnector(String connectorId);
	
	public boolean initialRequest(String chargerSerialNumber, MeterValueDTO meterValue);



}
