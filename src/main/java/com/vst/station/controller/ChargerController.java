package com.vst.station.controller;



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

import com.vst.station.dto.ChargerDTO;
import com.vst.station.model.Charger;
import com.vst.station.service.StationServiceImpl;

@RequestMapping("/manageCharger")
@CrossOrigin(origins = "*")
@RestController
public class ChargerController {
	
	@Autowired
	StationServiceImpl stationServiceImpl;
	
	boolean flag=false;

	public static final Logger logger = LogManager.getLogger(ChargerController.class);
	
	@PostMapping("/addCharger")
	public ResponseEntity<String> addStationChargers(@RequestParam("stationId") String stationId,
			@RequestBody ChargerDTO chargerDTO) {
		 flag = stationServiceImpl.addCharger(stationId, chargerDTO);
		if (flag == true)
			return new ResponseEntity<>("Charger Added Successfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Please Check and try Again", HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/udpateCharger")
	public ResponseEntity<String> chargerUpdate(@RequestParam("stationId") String stationId,
			@RequestParam("chargerId") String chargerId, @RequestBody ChargerDTO chargerDTO) {
		flag =stationServiceImpl.updateCharger(stationId, chargerId, chargerDTO);
		if(flag==true)
		return new ResponseEntity<>("Charger Details Updated Succesfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Please Check and try Again", HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/deleteCharger")
	public ResponseEntity<String> deleteCharger(@RequestParam("chargerId") String chargerId) {
		 flag = stationServiceImpl.removeCharger(chargerId);
		if (flag == true) {
			return new ResponseEntity<>("Charger Deleted Successfully", HttpStatus.OK);
		} else
			return new ResponseEntity<>("Charger Not Deleted.Please Check and Try again.", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/getChargers")
	public ResponseEntity<?> getStationChargers(@RequestParam("stationId") String stationId) {
	
		return ResponseEntity.ok(stationServiceImpl.getAllStationChargers(stationId));
//		List<Charger> list = stationServiceImpl.getAllStationChargers(stationId);
//		if (!list.isEmpty())
//			return ResponseEntity.ok(list);
//		else
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Charger Not Available in Station. Please Check another Station");
	}
	
	@GetMapping("/getCharger")
	public ResponseEntity<?> getStationCharger(@RequestParam("chargerId") String chargerId) {
		Charger charger = stationServiceImpl.getCharger(chargerId);
		if (charger != null)
			return ResponseEntity.ok(charger);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Charger Not Present of the Specified of ID");
	}
	
	@GetMapping("/addMultipleCharger")
	public String addMultipleCharger() {
		stationServiceImpl.addMultipleCharger();
		return "charger saved successfully";
	}

}
