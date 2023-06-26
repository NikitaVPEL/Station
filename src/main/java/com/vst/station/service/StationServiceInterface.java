package com.vst.station.service;

import java.util.List;

import com.vst.station.dto.StationDTO;
import com.vst.station.dto.StationDTO1;
import com.vst.station.dto.StationFindDTO;
import com.vst.station.dto.StationHostDTO;
import com.vst.station.dto.StationUpdateDTO;
import com.vst.station.model.Station;

public interface StationServiceInterface {

	public boolean addStation(StationDTO stationDTO);

	public boolean updateStation(String stationId, StationUpdateDTO stationUpdateDTO);

//	public boolean updateConnector(String stationId, String chargerId, String connectorId, ConnectorDTO connectorDTO);

	public boolean removeStation(String stationId);

	public List<Station> showAll();

	public Station show(String stationId);

	public List<Station> getByHostId(String stationHostId);

	public List<Station> getByVendorId(String stationVendorId);

	List<StationDTO1> getRequiredStationData();

	List<StationDTO1> stationforApplication(String query);

	List<StationDTO1> getAllStationforRadius(double longitude, double latitude, double maxDistance);

//	public boolean removeStationConnector(String stationId, String chargerId, String connectorId);

//	public List<Station> getInactiveStation();

	Station getStation(String stationId);

	StationFindDTO getNameAndAddressStation(String stationId);
	
	public boolean addUserAccess(String stationId, String type, List<String> list);
	
	public List<String> GetUserAccessList(String stationId);
	
	public boolean addUserAccessList(String stationId, List<String> userIds);
	
	public boolean getIsUserPresentInRestrictionList(String stationId, String userId);
	
	public List<StationHostDTO> getStationByHostId(String stationHostId);
	
	
	
	
	

}
