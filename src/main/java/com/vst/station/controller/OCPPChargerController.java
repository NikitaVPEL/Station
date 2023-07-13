package com.vst.station.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vst.station.dto.connectorStatusNotificationDTO;
import com.vst.station.dto.ocppVerificationDTO;
import com.vst.station.service.ChargerServiceImpl;

@RequestMapping("/manageCharger")
@CrossOrigin(origins = "*")
@RestController
public class OCPPChargerController {

	@Autowired
	ChargerServiceImpl chargerServiceImpl;

	@GetMapping("/getChargerStatusByChargerSerialNumber") // first
	public ResponseEntity<?> getChargerStatus(@RequestParam("chargerSerialNumber") String chargerSerialNumber) {
		Boolean isActive = chargerServiceImpl.getChargerStatusByChargerSerialNumber(chargerSerialNumber);
		return ResponseEntity.ok(isActive);
	}

	@PostMapping("/chargerVerification") // boot notificaton
	public ResponseEntity<?> getVerification(@RequestParam("chargerSerialNumber") String chargerSerialNumber,
			@RequestBody ocppVerificationDTO ocppVerificationDTO) {
		Boolean flag = chargerServiceImpl.initialVerification(chargerSerialNumber, ocppVerificationDTO);
		return ResponseEntity.ok(flag);
	}

	@GetMapping("/chargerOCPPVersion")
	public ResponseEntity<String> getOCPPProtocol(@RequestParam("chargerSerialNumber") String chargerSerialNumber) {
		String chargerOCPPProtocol = chargerServiceImpl.getChargerOCPPProtocol(chargerSerialNumber);
		return new ResponseEntity<>(chargerOCPPProtocol, HttpStatus.OK);
	}

	@PostMapping("/chargerStatusNotification")
	public ResponseEntity<?> getStatusNotification(@RequestParam("chargerSerialNumber") String chargerPointSerialNumber,
			@RequestBody connectorStatusNotificationDTO connectorStatusNotificationDTO) {
		boolean flag = chargerServiceImpl.statusNotification(chargerPointSerialNumber, connectorStatusNotificationDTO);
		return ResponseEntity.ok(flag);
	}

	@GetMapping("/heartbeatStatus")
	public ResponseEntity<?> getHeartBeatStatus(@RequestParam("chargerSerialNumber") String chargerSerialNumber,
			@RequestParam("chargerTimeStamp") String chargerTimeStamp) {
		boolean flag = chargerServiceImpl.heartbeatNotification(chargerSerialNumber, chargerTimeStamp);
		return ResponseEntity.ok(flag);
	}

}
