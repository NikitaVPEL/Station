package com.vst.station.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vst.station.dto.MeterValueDTO;
import com.vst.station.service.ConnectorServiceImpl;

@RestController
@RequestMapping("/manageConnector")
public class OCPPConnectorController {

	@Autowired
	ConnectorServiceImpl connectorServiceImpl;

	@PostMapping("/meterValue")
	public ResponseEntity<?> setMeterValues(@RequestParam("chargerSerialNumber") String chargerSerialNumber,
			@RequestBody MeterValueDTO meterValue) {
		boolean flag = connectorServiceImpl.initialRequest(chargerSerialNumber, meterValue);
		return ResponseEntity.ok(flag);
	}

}
