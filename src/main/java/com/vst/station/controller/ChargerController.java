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

import com.vst.station.dto.ChargerDTO;
import com.vst.station.dto.ChargerUpdateDTO;
import com.vst.station.model.Charger;
import com.vst.station.service.ChargerServiceImpl;

@RequestMapping("/manageCharger")
@CrossOrigin(origins = "*")
@RestController
public class ChargerController {

	@Autowired
	ChargerServiceImpl chargerServiceImpl;

	boolean flag = false;

	public static final Logger logger = LogManager.getLogger(ChargerController.class);

	/**	
	 * Usage: Add new Charger specific station
	 * 
	 * HTTP method : POST and URL : manageCharger/addCharger
	 * 
	 * @param stationId, chargerDTO
	 * @return Http response and string message "Charger added successfully" 
	 */
	@PostMapping("/addCharger")
	public ResponseEntity<String> addStationChargers(@RequestParam("stationId") String stationId,
			@Valid @RequestBody ChargerDTO chargerDTO) {
		flag = chargerServiceImpl.addCharger(stationId, chargerDTO);
		if (flag == true)
			return new ResponseEntity<>("Charger Added Successfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Please Check and try Again", HttpStatus.NOT_FOUND);
	}

	/**
	 * Usage: update the Charger details
	 * 
	 * HTTP method : PUT and URL : manageCharger/udpateCharger
	 * 
	 * @param stationId, chargerId, chargerDTO
	 * @return Http response and string message "Charger updated successfully"
	 */
	@PutMapping("/udpateCharger")
	public ResponseEntity<String> chargerUpdate(@RequestParam("stationId") String stationId,
			@RequestParam("chargerId") String chargerId, @Valid @RequestBody ChargerUpdateDTO chargerDTO) {
		flag = chargerServiceImpl.updateCharger(stationId, chargerId, chargerDTO);
		if (flag == true)
			return new ResponseEntity<>("Charger Details Updated Successfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Please Check and try Again", HttpStatus.NOT_FOUND);
	}

	/**
	 * Usage: delete the specific Charger using Station Id
	 * 
	 * HTTP method : DELETE and URL : manageCharger/deleteCharger
	 * 
	 * @param chargerId
	 * @return Http response and string message "Charger successfully deleted"
	 */
	@DeleteMapping("/deleteCharger")
	public ResponseEntity<String> deleteCharger(@RequestParam("chargerId") String chargerId) {
		flag = chargerServiceImpl.removeCharger(chargerId);
		if (flag == true) {
			return new ResponseEntity<>("Charger Deleted Successfully", HttpStatus.OK);
		} else
			return new ResponseEntity<>("Charger Not Deleted.Please Check and Try again.", HttpStatus.NOT_FOUND);
	}

	/**
	 * Usage: Get the details of all the available Chargers in specific station
	 * 
	 * HTTP method : GET and URL : manageCharger/getChargers
	 * 
	 * @param stationId
	 * @return Http response and list of Charger object
	 */
	@GetMapping("/getChargers")
	public ResponseEntity<List<Charger>> getStationChargers(@RequestParam("stationId") String stationId) {

		return ResponseEntity.ok(chargerServiceImpl.getAllStationChargers(stationId));
//		List<Charger> list = chargerServiceImpl.getAllStationChargers(stationId);
//		if (!list.isEmpty())
//			return ResponseEntity.ok(list);
//		else
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Charger Not Available in Station. Please Check another Station");
	}

	/**
	 * Usage: Get the Charger object/ details by Charger Id
	 * 
	 * HTTP method : GET and URL : manageCharger/getCharger
	 * 
	 * @param chargerId
	 * @return Http response and Charger object
	 */
	@GetMapping("/getCharger")
	public ResponseEntity<?> getStationCharger(@RequestParam("chargerId") String chargerId) {
		Charger charger = chargerServiceImpl.getCharger(chargerId);
		if (charger != null)
			return ResponseEntity.ok(charger);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Charger Not Present of the Specified of ID");
	}

	/**
	 * Usage: update the Charger status to active/inactive
	 * 
	 * HTTP method : PUT and URL : manageCharger/udpateChargerStatus
	 * 
	 * @param stationId, charrgerIdList, status
	 * @return Http response and string message "Charger activated successfully"
	 */
	@PutMapping("/updateChargerStatus")
	public ResponseEntity<String> updtaeChargerStatus(@RequestParam("stationId") String stationId,
			@RequestParam("chargerId") List<String> charrgerIdList, @RequestParam("chargerStatus") String status) {
		if (chargerServiceImpl.updateChargerStatus(stationId, charrgerIdList, status) == true) {
			return new ResponseEntity<>("Charger "+status + "updated Successfully", HttpStatus.OK);
		}else 
		return new ResponseEntity<>("Charger not "+status , HttpStatus.NOT_MODIFIED);
		

	}
	
	@SuppressWarnings("unused")
	@GetMapping("/getChargerStatusByChargerSerialNumber")
	public ResponseEntity<?> getChargerStatus(@RequestParam("chargerSerialNumber") String chargerSerialNumber) {
	    Boolean isActive = chargerServiceImpl.getChargerStatusByChargerSerialNumber(chargerSerialNumber);

	    if (isActive == null) {
	        return ResponseEntity.notFound().build();
	    } else {
	        return ResponseEntity.ok(isActive); 
	    }
	}

}

