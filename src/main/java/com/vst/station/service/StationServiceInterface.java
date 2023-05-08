package com.vst.station.service;


import java.util.List;

import com.vst.station.dto.StationDTO;
import com.vst.station.dto.ChargerDTO;
import com.vst.station.dto.ConnectorDTO;
import com.vst.station.dto.StationDTO1;
import com.vst.station.dto.StationFindDTO;
import com.vst.station.dto.StationUpdateDTO;
import com.vst.station.model.Charger;
import com.vst.station.model.Connector;
import com.vst.station.model.Station;


public interface StationServiceInterface {

	public boolean addStation(StationDTO stationDTO);

	public boolean addCharger(String stationId, ChargerDTO chargerDTO);

	public boolean addConnector(String stationId, String chargerId, ConnectorDTO connectorDTO);

	public boolean updateStation(String stationId, StationUpdateDTO stationUpdateDTO);

	public boolean updateCharger(String stationId, String chargerId, ChargerDTO chargerDTO);

	public boolean updateConnector(String stationId, String chargerId, String connectorId, ConnectorDTO connectorDTO);
	
	public boolean updateConnectorById(String connectorId, ConnectorDTO connectorDTO);
	
	public boolean removeStation(String stationId);
	
	public boolean removeStationCharger(String stationId,String chargerId);
	
	public boolean removeStationConnector(String stationId, String chargerId, String connectorId);
	
	public boolean removeCharger(String chargerId);
	
	public boolean removeConnector(String connectorId);

	public List<Station> showAll();

	public Station show(String stationId);

	public List<Station> getByHostId(String stationHostId);

	public List<Station> getByVendorId(String stationVendorId);

	public List<Station> getInactiveStation();

	List<Charger> getAllStationChargers(String stationId);

	List<Connector> getAllStationConnector(String stationId, String chargerId);

	Station getStation(String stationId);

	Charger getCharger(String chargerId);

	Connector getConnector(String connectorId);

	List<StationDTO1> getRequiredStationData();
	
	List<StationDTO1> stationforApplication(String query);
	
	List<StationDTO1> getAllStationforRadius (double longitude, double latitude, double maxDistance);
	
	StationFindDTO getNameAndAddressStation(String stationId);

}
