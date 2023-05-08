package com.vst.station.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vst.station.dto.ChargerDTO;
import com.vst.station.dto.ChargerGetDTO;
import com.vst.station.dto.ConnectorDTO;
import com.vst.station.model.Charger;
import com.vst.station.model.Connector;

@Component
public class ChargerConverter {
	
	@Autowired
	ConnectorConverter connectorConverter;
	
	public Charger dtoToEntity(ChargerDTO chargerDTO) {
		Charger charger = new Charger();
		BeanUtils.copyProperties(chargerDTO, charger);
		List<ConnectorDTO> connectorList = chargerDTO.getConnectors();

		if (connectorList != null) {
			
			List<Connector> connectors = new ArrayList<>();

			for (ConnectorDTO connectorDTO : connectorList) {
				connectors.add(connectorConverter.dtoToEntity(connectorDTO));
			}
			charger.setConnectors(connectors);
		}
		return charger;
	}
	
	public ChargerDTO entityToDto(Charger charger) {
		ChargerDTO chargerDTO = new ChargerDTO();
		BeanUtils.copyProperties(charger, chargerDTO);
		return chargerDTO;
	}
	
	public ChargerGetDTO entityToDto1(Charger charger) {
		ChargerGetDTO chargerGetDTO = new ChargerGetDTO();
		BeanUtils.copyProperties(charger, chargerGetDTO);
		return chargerGetDTO;
	}
}
