package com.vst.station.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vst.station.dto.ChargerDTO;
import com.vst.station.dto.StationDTO1;
import com.vst.station.dto.StationFindDTO;
import com.vst.station.dto.StationUpdateDTO;
import com.vst.station.dto.StationDTO;
import com.vst.station.model.Charger;
import com.vst.station.model.Station;

import java.util.ArrayList;
import java.util.List;

@Component
public class StationConveter {

	@Autowired
	ChargerConverter chargerConverter;

	public Station dtoToEntity(StationDTO stationDTO) {
		Station station = new Station();
		BeanUtils.copyProperties(stationDTO, station);

		List<ChargerDTO> chargerList = stationDTO.getChargers();
		if (chargerList != null) {
			List<Charger> chargers = new ArrayList<>();

			for (ChargerDTO chargerDTO : chargerList) {
				chargers.add(chargerConverter.dtoToEntity(chargerDTO));
			}
			station.setChargers(chargers);
		}
		return station;
	}

	public StationDTO entityToDto(Station station) {
		StationDTO stationDTO = new StationDTO();
		BeanUtils.copyProperties(station, stationDTO);
		return stationDTO;
	}

	public StationDTO1 entitytoStationDTO1(Station station) {
		StationDTO1 stationDTO1 = new StationDTO1();
		BeanUtils.copyProperties(station, stationDTO1);
		return stationDTO1;
	}

	public Station updateDtoToEntity(StationUpdateDTO stationUpdateDTO) {
		Station station = new Station();
		BeanUtils.copyProperties(stationUpdateDTO, station);
		return station;
	}

	public StationFindDTO entitytoStationFind(Station station) {
		StationFindDTO dto = new StationFindDTO();
		BeanUtils.copyProperties(station, dto);
		return dto;
		
	}


}
