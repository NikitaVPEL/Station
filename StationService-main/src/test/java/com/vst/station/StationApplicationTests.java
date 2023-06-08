//package com.vst.station;
//
////import static org.junit.Assert.assertEquals;
////import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.vst.station.converter.StationConveter;
//import com.vst.station.dto.StationDTO;
//import com.vst.station.model.Station;
//import com.vst.station.repository.StationRepository;
//import com.vst.station.service.StationServiceImpl;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//class StationApplicationTests {
//
//	@Autowired
//	StationServiceImpl stationServiceImpl;
//	
//	@MockBean
//	StationRepository stationRepository;
//	
//	@Autowired
//	StationConveter conveter;
//		
//	@Test
//	public void saveUserTest() {
//		Station station = new Station();
//		station.setStationId("S001");
//		station.setStationName("Central Station");
//		station.setStationHostId("H001");
//		station.setStationVendorId("V001");
//		station.setStationArea("Los Angeles");
//		station.setStationLocationURL("www.centralstation.com");
//		station.setStationParkingArea("North Parking");
//		station.setStationContactNumber("555-555-5555");
//		station.setStationWorkingTime("24/7");
//		station.setStationParkingType("Paid");
//		ArrayList<String> stationAmenities = new ArrayList<>();
//		stationAmenities.add("ATM");
//		stationAmenities.add("Restaurant");
//		station.setStationAmenity(stationAmenities);
//		station.setCreatedDate("2022-01-01");
//		station.setModifiedDate("2022-01-02");
//		station.setCreatedBy("Admin");
//		station.setModifiedBy("Admin");
//		station.setActive(true);
//		StationDTO dto =conveter.entityToDto(station);
//		when(stationRepository.save(station)).thenReturn(station);
////		assertEquals(dto, stationServiceImpl.addStation(dto));
//	}
//}
