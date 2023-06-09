package com.vst.station.repository;

//import java.time.Instant;
import java.util.List;

//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fasterxml.jackson.annotation.JsonInclude;
//import com.mongodb.internal.connection.Time;
//import com.vst.station.model.Charger;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.mongodb.BasicDBObject;
//import com.mongodb.client.MongoCollection;
import com.vst.station.model.Station;

public interface StationRepository extends MongoRepository<Station, String> {

	Station findByStationIdAndIsActiveTrue(String stationId);
	
	Station findByStationId(String stationId);

	List<Station> findAllByIsActiveTrue();

	List<Station> findByStationHostIdAndIsActiveTrue(String stationHostId);

	List<Station> findByStationVendorIdAndIsActiveTrue(String stationVendorId);

	List<Station> findByStationAreaAndIsActiveTrue(String stationLocation);

	List<Station> findByStationParkingAreaAndIsActiveTrue(String stationParkingArea);

	List<Station> findByStationWorkingTimeAndIsActiveTrue(String stationWorkingTime);

	List<Station> findByStationParkingTypeAndIsActiveTrue(String stationParkingType);

	List<Station> findAllByIsActiveFalse();

//	@Query("{'chargers._id's:?0},{'chargers.$':1}")
//	Charger findB

//	MongoCollection<Document> table = null;
//	
//	BasicDBObject query = new BasicDBObject("chargers._id":? 0, "chargers.isActive": true);

//	@Query(value = "{'chargers._id' : ?0, 'chargers.isActive': true }, { 'chargers.$': 1 }")
//	@Query(value = "{'query'},{'projection'}")
//	Object findByCharger(BasicDBObject stationId,BasicDBObject ChargerId);

//	@Query(value = "{'chargers.chargerId' : ?0 }, { '_id'=0,'chargers.$': 1 }")
//	@Query("{'chargers.chargerId' : ?0 },{'_id':0, chargers:{'$elemMatch':{'chargerId':?0}}},{ 'chargers.$': 1 }")
//	@Aggregation(pipeline = {
//			"{'$unwind'{'path':'$chargers'}}",
//			"{'$match':{'chargers.chargerId':?0}}",
//			"{'$match':{'chargers.isActive':true}}",
//			"{'$project':{'chargers':1}}"
//	})
//	@Query("{'chargers.chargerId':?0,'chargers.isActive':true}") for get all chargers from specific station with charger id
//	@Query("{'chargers.chargerId':?0,'chargers.isActive':true},{'_id':1,chargers:{'$elemMatch':{'chargers.chargerId':?0}}},{chargers.$':1}")
//	@Query(value = "{'chargerId':?0,'chargers.isActive':true}",fields="{'chargers.$':1,}") // got excepted result from this query 
	
//	@Query(value = "{'chargers._id':?0, 'chargers.isActive':true}", fields = "{'chargers.$':1}")
	@Query(value = "{'chargers._id':?0}", fields = "{'chargers.$':1}")
	Station findByChargerId(String chargerId);
	
//	@Query(value = "{'chargers.chargerSerialNumber': ?0}", fields = "{'chargers.isActive': 1}")
//	Station findByChargerSerialNumber(String chargerSerialNumber);
	
	 Station findByChargersChargerSerialNumberAndIsActiveTrue(String chargerSerialNumber);

	
//	({'chargers._id':'CHG20230419174707799'},{'chargers.$':1})

	@Query(value = "{'stationId':?0, 'isActive':true}", fields = "{'chargers':0}")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	Station findStationByStationId(String stationId);

	@Query(value = "{'chargers.connectors._id':?0, 'chargers.connectors.isActive':true}",fields = "{'location':0}") 																					
	Station findByConnectorId(String connectorId);

//	@Query(value = "{'stationId':?0, 'isActive':true}", fields = "{'stationId':1,'stationName':1,'stationLocation':1,'stationLatitude':1,'stationLongitude':1,'stationParkingType':1,'stationStatus':1}")
//	@JsonInclude(JsonInclude.Include.NON_ABSENT)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Query(value = "{ '_id' : '?0','isActive':true }", fields = "{ '_id' : 1, 'stationName' : 1, 'stationLocation' : 1, 'stationLatitude' : 1, 'stationLongitude' : 1, 'stationParkingType' : 1, 'stationStatus' : 1},{'isActive':0}")
	Station findBySpecificId(String stationId);

//	List<Station> findByStationNameContainingIgnoreCaseAndIsActiveTrueOrStationLocationContainingIgnoreCaseAndIsActiveTrueOrStationParkingAreaContainingIgnoreCaseAndIsActiveTrueOrStationWorkingTimeContainingIgnoreCaseAndIsActiveTrueOrStationParkingTypeContainingIgnoreCaseAndIsActiveTrueOrStationAmenityContainingIgnoreCaseAndIsActiveTrueOrStationStatusContainingIgnoreCaseAndIsActiveTrueOrStationPowerStandardContainingIgnoreCaseAndIsActiveTrueOrChargersChargerConnectorTypeContainingIgnoreCaseAndIsActiveTrueOrChargersChargerNameContainingIgnoreCaseAndIsActiveTrueOrChargersChargerOutputVoltageContainingIgnoreCaseAndIsActiveTrueOrChargersChargerInputVoltageContainingIgnoreCaseAndIsActiveTrueOrChargersChargerMinInputAmpereContainingIgnoreCaseAndIsActiveTrueOrChargersChargerMaxInputAmpereContainingIgnoreCaseAndIsActiveTrueOrChargersChargerOutputAmpereContainingIgnoreCaseAndIsActiveTrueOrChargersChargerInputFrequencyContainingIgnoreCaseAndIsActiveTrueOrChargersChargerOutputFrequencyContainingIgnoreCaseAndIsActiveTrueOrChargersChargerMountTypeContainingIgnoreCaseAndIsActiveTrueOrChargersConnectorsConnectorTypeContainingIgnoreCaseAndIsActiveTrueOrChargersConnectorsConnectorSocketContainingIgnoreCaseAndIsActiveTrueOrChargersConnectorsConnectorOutputPowerContainingIgnoreCaseAndIsActiveTrueOrChargersConnectorsConnectorStatusContainingIgnoreCaseAndIsActiveTrue(
//			String stationName, String stationLocation, String stationParkingArea, String stationWorkingTime,
//			String stationParkingType, String stationAmenity, String stationStatus, String stationPowerStandard,
//			String chargerConnectorType, String chargerName, String chargerOutputVoltage, String chargerInputVoltage,
//			String chargerMinInputAmpere, String chargerMaxInputAmpere, String chargerOutputAmpere,
//			String chargerInputFrequency, String chargerOutputFrequency, String chargerMountType, String connectorType,
//			String connectorSocket, String connectorStatus, String connectorOutputPower);
	
	List<Station> findByStationNameContainingIgnoreCaseAndIsActiveTrueOrStationAreaContainingIgnoreCaseAndIsActiveTrueOrStationZipCodeContainingIgnoreCaseAndIsActiveTrueOrStationCityContainingIgnoreCaseAndIsActiveTrueOrStationStatusContainingIgnoreCaseAndIsActiveTrue(
			String stationName, String stationArea, String stationZipCode, String stationCity, String stationStatus);
	
	
	@Query("{ 'location' : { '$nearSphere' : { '$geometry' : { 'type' : ?0, 'coordinates': [?1, ?2] }, '$maxDistance': ?3, '$minDistance': ?4 } } }")
	List<Station> findByGeoLocation(String type, double longitude, double latitude, double maxDistance, double minDistance);


}

