package com.vst.station.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vst.station.dto.StationDTO;
import com.vst.station.dto.StationDTO1;
import com.vst.station.dto.StationFindDTO;
import com.vst.station.dto.StationUpdateDTO;
import com.vst.station.model.Station;
import com.vst.station.service.StationServiceImpl;


@RequestMapping("/manageStation")
@CrossOrigin(origins = "*")
@RestController
public class StationController {

	@Autowired
	StationServiceImpl stationServiceImpl;

//	boolean flag=false;

	public static final Logger logger = LogManager.getLogger(StationController.class);

	@PostMapping("/addStation")
	public ResponseEntity<String> saveStation(@Valid @RequestBody StationDTO stationDTO) {
		if (stationServiceImpl.addStation(stationDTO) == true)
			return new ResponseEntity<>("Station Added", HttpStatus.OK);
		else
			return new ResponseEntity<>("Somthing Went Wrong", HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/updateStation")
	public ResponseEntity<String> updateStationDetails(@RequestParam("stationId") String stationId,
			@RequestBody StationUpdateDTO stationUpdateDTO) {
		if (stationServiceImpl.updateStation(stationId, stationUpdateDTO) == true)
			return new ResponseEntity<>("Station Details Updated Succesfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Somthing Went Wrong", HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/deleteStation")
	public ResponseEntity<String> deleteStationDetails(@RequestParam("stationId") String stationId) {
		if (stationServiceImpl.removeStation(stationId) == true)
			return new ResponseEntity<>("Station Deleted Succesfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Somthing Went Wrong", HttpStatus.NOT_FOUND);
	}

	@GetMapping("/getStation")
	public ResponseEntity<?> getStationById(@RequestParam("stationId") String stationId) {
		Station obj = stationServiceImpl.show(stationId);
		if (obj != null)
			return ResponseEntity.ok(obj);
		else
			return ResponseEntity.ok(obj);
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Station Not Available");
	}

	@GetMapping("/getStations")
	public ResponseEntity<List<?>> getAllStation() {
		return ResponseEntity.ok(stationServiceImpl.showAll());
	}

	@GetMapping("/getStationHost")
	public ResponseEntity<List<Station>> getStationByHostId(@RequestParam("stationHostId") String stationHostId) {
		List<Station> finalList = stationServiceImpl.getByHostId(stationHostId);
		if (!finalList.isEmpty())
			return ResponseEntity.ok(finalList);
		else
			return ResponseEntity.ok(finalList);
	}

	@GetMapping("/getStationVendor")
	public ResponseEntity<List<Station>> getStationByVendorId(@RequestParam("stationVendorId") String stationVendorId) {
		List<Station> finalList = stationServiceImpl.getByVendorId(stationVendorId);
		if (!finalList.isEmpty())
			return ResponseEntity.ok(finalList);
		else
			return ResponseEntity.ok(finalList);
	}

//	@GetMapping("/getStationHistory")
//	public ResponseEntity<List<Station>> getInactive() {
//		List<Station> finalList = stationServiceImpl.getInactiveStation();
//		if (!finalList.isEmpty())
//			return ResponseEntity.ok(finalList);
//		else
//			return ResponseEntity.ok(finalList);
//	}

	@GetMapping("/getStationById")
	public ResponseEntity<Station> getStationData(@RequestParam("stationId") String stationId) {
		return ResponseEntity.ok(stationServiceImpl.getStation(stationId));
	}

	@GetMapping("/getStationInterface")
	public ResponseEntity<List<StationDTO1>> getRequiredStationData() {
		return ResponseEntity.ok(stationServiceImpl.getRequiredStationData());
	}

	@GetMapping("/getStationsByKeyword")
	public ResponseEntity<List<StationDTO1>> searchStation(@RequestParam("query") String query) {
		return ResponseEntity.ok(stationServiceImpl.stationforApplication(query));
	}

	@PostMapping("/addMultiplesStations")
	public String adding() {
		stationServiceImpl.mul();
		return "Stations Added";
	}

	@GetMapping("/test")
	public Station test() {
		Station station = new Station();
		return station;
	}

	@GetMapping("/getStationsLocation")
	public ResponseEntity<List<StationDTO1>> geoLocation(@RequestParam("longitude") double longitude,
			@RequestParam("latitude") double latitude, @RequestParam("maxDistance") double maxDistance) {
		return ResponseEntity.ok(stationServiceImpl.getAllStationforRadius(longitude, latitude, maxDistance));
	}

	@GetMapping("/getStationAddress")
	public ResponseEntity<StationFindDTO> getAddrerss(@RequestParam("stationId") String stationId){
		return ResponseEntity.ok(stationServiceImpl.getNameAndAddressStation(stationId));
		}

}
