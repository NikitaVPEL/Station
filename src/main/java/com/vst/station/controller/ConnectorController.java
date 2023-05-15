package com.vst.station.controller;

import java.util.List;

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
import com.vst.station.model.Connector;
import com.vst.station.service.StationServiceImpl;

@RequestMapping("/manageConnector")
@CrossOrigin(origins = "*")
@RestController
public class ConnectorController {
	
	@Autowired
	StationServiceImpl stationServiceImpl;
	
	boolean flag=false;

	public static final Logger logger = LogManager.getLogger(ConnectorController.class);
	
	@PostMapping("/addConnector")
	public ResponseEntity<String> addStationConnector(@RequestParam("stationId") String stationId,
			@RequestParam("chargerId") String chargerId, @RequestBody ConnectorDTO connectorDTO) {
		 flag = stationServiceImpl.addConnector(stationId, chargerId, connectorDTO);
		if (flag == true)
			return new ResponseEntity<>("Connector Added Successfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Connector not Added. Please check and try Again", HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/updateConnector")
	public ResponseEntity<String> updateConnectorDetailsById(@RequestParam("connectorId") String connectorId, @RequestBody ConnectorDTO connectorDTO){
		boolean flag = stationServiceImpl.updateConnectorById(connectorId, connectorDTO);
		if(flag==true)
			return new ResponseEntity<>("Connector Update Succesfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Connector Details not updated, Please check and try again", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/getConnectors")
	public ResponseEntity<List<Connector>> getAllConnectors(@RequestParam("stationId") String stationId,
			@RequestParam("chargerId") String chargerId) {
		return ResponseEntity.ok(stationServiceImpl.getAllStationConnector(stationId, chargerId));
	}
	
	@GetMapping("/getConnector")
	public ResponseEntity<Connector> getConnectorById(@RequestParam("connectorId") String connectorId) {
		return ResponseEntity.ok(stationServiceImpl.getConnector(connectorId));
	}
	
	@DeleteMapping("/deleteConnector")
	public ResponseEntity<String> deleteConnector(@RequestParam("connectorId") String connectorId) {
		boolean flag =stationServiceImpl.removeConnector(connectorId);
		if(flag==true)
			return new ResponseEntity<>("Connector Deleted Succesfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Connector Not Deleted. Please try again", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/addMultipleConnector")
	public String multipleAdd() { 
		stationServiceImpl.addMultipleConnector();
		return "connector added successfully";
	}
			
			
}
