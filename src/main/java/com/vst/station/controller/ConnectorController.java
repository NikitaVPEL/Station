package com.vst.station.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Add;
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

import com.vst.station.dto.ConnectorDTO;
import com.vst.station.dto.ConnectorUpdateDTO;
import com.vst.station.model.Connector;
import com.vst.station.service.ChargerServiceImpl;
import com.vst.station.service.ConnectorServiceImpl;

@RequestMapping("/manageConnector")
@CrossOrigin(origins = "*")
@RestController
public class ConnectorController {
	
	@Autowired
	ConnectorServiceImpl connectorServiceImpl;

	boolean flag = false;

	public static final Logger logger = LogManager.getLogger(ConnectorController.class);

	/**
	 * Usage: Add new Connector in specific station charger
	 * 
	 * HTTP method : POST and URL : manageConnector/addConnector
	 * 
	 * @param stationId, chargerId, connectorDTO
	 * @return Http response and string message "Connector added successfully"
	 */
	@PostMapping("/addConnector")
	public ResponseEntity<String> addStationConnector(@RequestParam("stationId") String stationId,
			@RequestParam("chargerId") String chargerId, @Valid @RequestBody ConnectorDTO connectorDTO) {
		flag = connectorServiceImpl.addConnector(stationId, chargerId, connectorDTO);
		if (flag == true)
			return new ResponseEntity<>("Connector Added Successfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Connector not Added. Please check and try Again", HttpStatus.NOT_FOUND);
	}

	/**
	 * Usage: update the Connector details
	 * 
	 * HTTP method : PUT and URL : manageConnector/updateConnector
	 * 
	 * @param connectorId, connectorDTO
	 * @return Http response and string message "Connector updated successfully"
	 */
	@PutMapping("/updateConnector")
	public ResponseEntity<String> updateConnectorDetailsById(@RequestParam("connectorId") String connectorId,
			@Valid @RequestBody ConnectorUpdateDTO connectorDTO) {
		boolean flag = connectorServiceImpl.updateConnectorById(connectorId, connectorDTO);
		if (flag == true)
			return new ResponseEntity<>("Connector Update Successfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Connector Details not updated, Please check and try again",
					HttpStatus.NOT_FOUND);
	}

	/**
	 * Usage: Get/read the details of all the available connector in specific
	 * station charger
	 * 
	 * HTTP method : GET and URL : manageConnector/getConnectors
	 * 
	 * @param stationId, chargerId
	 * @return Http response and list of connector object
	 */
	@GetMapping("/getConnectors")
	public ResponseEntity<List<Connector>> getAllConnectors(@RequestParam("stationId") String stationId,
			@RequestParam("chargerId") String chargerId) {
		return ResponseEntity.ok(connectorServiceImpl.getAllStationConnector(stationId, chargerId));
	}

	/**
	 * Usage: Get the Connector object/ details by Connector Id
	 * 
	 * HTTP method : GET and URL : manageConnector/getConnector
	 * 
	 * @param connectorId
	 * @return Http response and Connector object
	 */
	@GetMapping("/getConnector")
	public ResponseEntity<Connector> getConnectorById(@RequestParam("connectorId") String connectorId) {
		return ResponseEntity.ok(connectorServiceImpl.getConnector(connectorId));
	}

	/**
	 * Usage: delete the specific Connector using connector Id
	 * 
	 * HTTP method : DELETE and URL : manageConnector/deleteConnector
	 * 
	 * @param connectorId
	 * @return Http response and string message "Connector successfully deleted"
	 */
	@DeleteMapping("/deleteConnector")
	public ResponseEntity<String> deleteConnector(@RequestParam("connectorId") String connectorId) {
		boolean flag = connectorServiceImpl.removeConnector(connectorId);
		if (flag == true)
			return new ResponseEntity<>("Connector Deleted Successfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Connector Not Deleted. Please try again", HttpStatus.NOT_FOUND);
	}

}
