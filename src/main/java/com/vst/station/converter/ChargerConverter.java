package com.vst.station.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vst.station.dto.ChargerDTO;
import com.vst.station.dto.ChargerGetDTO;
import com.vst.station.model.Charger;

@Component
public class ChargerConverter {
	
	@Autowired
	ConnectorConverter connectorConverter;
	
	public Charger dtoToEntity(ChargerDTO chargerDTO) {
		Charger charger = new Charger();
		BeanUtils.copyProperties(chargerDTO, charger);
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
