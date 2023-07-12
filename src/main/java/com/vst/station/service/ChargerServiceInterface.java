package com.vst.station.service;

import java.util.List;

import com.vst.station.dto.ChargerDTO;
import com.vst.station.dto.ChargerUpdateDTO;
import com.vst.station.dto.connectorStatusNotificationDTO;
import com.vst.station.dto.ocppVerificationDTO;
import com.vst.station.model.Charger;

public interface ChargerServiceInterface {

	public boolean addCharger(String stationId, ChargerDTO chargerDTO);

	public boolean updateCharger(String stationId, String chargerId, ChargerUpdateDTO chargerDTO);

	public boolean removeStationCharger(String stationId, String chargerId);

	List<Charger> getAllStationChargers(String stationId);

	Charger getCharger(String chargerId);

	public boolean updateChargerStatus(String stationId, List<String> chargerIdList, String status);

	public boolean removeCharger(String chargerId);

	public boolean getChargerStatusByChargerSerialNumber(String chargerSerialNumber);

	public Boolean initialVerification(String chargerSerialNumber, ocppVerificationDTO ocppVerificationDTO);

	public String getChargerOCPPProtocol(String chargerSerialNumber);
	
	public boolean statusNotification(String chargerSerialNumber, connectorStatusNotificationDTO connectorStatusNotificationDTO);
	
	public boolean heartbeatNotification(String chargertPoinSerialNumber, String chargerTimeStamp);

}
