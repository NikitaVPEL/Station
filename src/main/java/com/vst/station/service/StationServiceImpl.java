package com.vst.station.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.vst.station.converter.ChargerConverter;
import com.vst.station.converter.ConnectorConverter;
import com.vst.station.converter.StationConveter;
import com.vst.station.dto.ChargerDTO;
import com.vst.station.dto.ConnectorDTO;
import com.vst.station.dto.StationDTO;
import com.vst.station.dto.StationDTO1;
import com.vst.station.dto.StationFindDTO;
import com.vst.station.dto.StationUpdateDTO;
import com.vst.station.exception.ChargerNotFoundException;
import com.vst.station.exception.InValidDataException;
import com.vst.station.exception.InValidIdExcepetion;
import com.vst.station.exception.StationException;
import com.vst.station.exception.StationIdNotAcceptableException;
import com.vst.station.exception.StationNotFoundException;
import com.vst.station.model.Charger;
import com.vst.station.model.Connector;
import com.vst.station.model.Coordinate;
import com.vst.station.model.Location;
import com.vst.station.model.Station;
import com.vst.station.repository.StationRepository;
import com.vst.station.utils.IdAndDateGenerator;
import com.vst.station.utils.Utility;


@SuppressWarnings("unused")
@Service
public class StationServiceImpl implements StationServiceInterface {

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private StationConveter stationConveter;

	@Autowired
	private ChargerConverter chargerConverter;

	@Autowired
	private ConnectorConverter connectorConverter;

	@Autowired
	MongoTemplate mongoTemplate;

	IdAndDateGenerator idAndDateGenerator = new IdAndDateGenerator();

	Utility utility = new Utility();

	Coordinate coordinates = new Coordinate();

	Instant time = Instant.now();

	public static final Logger logger = LogManager.getLogger(StationServiceImpl.class);

	@Override
	public boolean addStation(StationDTO stationDTO) {
		try {
			if (stationDTO != null) {
//			configuration.mongoClient();	
				Location location = new Location();
				Station station = stationConveter.dtoToEntity(stationDTO);
				station.setStationId("STN" + idAndDateGenerator.idGenerator());
				station.setStationName(utility.toTitleCase(station.getStationName()));
				station.setStationCity(utility.toTitleCase(station.getStationCity()));
				station.setActive(true);
				station.setCreatedBy("Admin");
				station.setCreatedDate(idAndDateGenerator.dateSetter());
				station.setModifiedBy("New Account");
				station.setModifiedDate(idAndDateGenerator.dateSetter());
				List<String> finalList = new ArrayList<>();
				for (int i = 0; i < station.getStationAmenity().size(); i++) {
					finalList.add(utility.toTitleCase(station.getStationAmenity().get(i)));
				}
				station.setStationAmenity(finalList);
				double a[] = location.getCoordinates();
				a[0] = station.getStationLongitude();
				a[1] = station.getStationLatitude();
				station.setStationLatitude(a[1]);
				station.setStationLongitude(a[0]);
				location.setCoordinates(a);
				station.setLocation(location);
				if (stationRepository.save(station) != null)
					return true;
				else
					return false;
			} else
				throw new InValidDataException("Station Data Cannot Be Empty. Please Check and Try Again");

		} catch (Exception e) {
			
			StationException exception = new StationException();
			exception.setErrorCode("STN001");
			exception.setStatusCode("404");
			exception.setStatus("Failed");
			exception.setServiceCode("ManageStation007");
//			exception.setMethodName();
			exception.setLineNumber(84);
			exception.setFunctionality("Add New Station");
			exception.setMessage("dhkahdk");
			logger.info(exception);

		} finally {
//			configuration.cleanUp();s
		}
		return false;
	}

	@Override
	public boolean addCharger(String stationId, ChargerDTO chargerDTO) {

		if (!stationId.isBlank() && stationId != null) {

			if (chargerDTO != null) {

				Station station = stationRepository
						.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));

				if (station != null) {
					Charger charger = chargerConverter.dtoToEntity(chargerDTO);
					int chargerNumber = 0;
					charger.setChargerId("CHG" + idAndDateGenerator.idGenerator());
					charger.setActive(true);
					charger.setCreatedBy("Admin");
					charger.setModifiedBy("Admin");
					charger.setCreatedDate(idAndDateGenerator.dateSetter());
					charger.setModifiedDate(idAndDateGenerator.dateSetter());
					station.getChargers().add(charger);
					Station obj = stationRepository.save(station);
					List<Charger> list = obj.getChargers();
					for (Charger c : list)
						chargerNumber++;
					station.setChargerNumber(chargerNumber);
					stationRepository.save(obj);
					if (obj != null)
						return true;
					else
						throw new InValidDataException("Charger Data Cannot Be Empty. Please Check and Try Again");
				} else
					throw new StationNotFoundException("Station not Available Please Check and try again");
			} else
				throw new InValidDataException("Please ensure that you have provided a valid Data and try again.");
		} else
			throw new InValidIdExcepetion("Please Provide Valid ID");

	}

	@Override
	public boolean addConnector(String stationId, String chargerId, ConnectorDTO connectorDTO) {

		if (!stationId.isBlank() && stationId != null && !chargerId.isBlank() && chargerId != null) {
			if (connectorDTO != null) {
				Station station = stationRepository
						.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
				if (station != null) {
					Charger charger = null;
					for (Charger c : station.getChargers()) {
						if (c.getChargerId().equals(utility.stringSanitization(chargerId))) {
							charger = c;
							break;
						}
					}
					if (charger == null) {
						throw new InValidDataException("Charger Data Cannot Be Empty. Please Check and Try Again");
					}
					List<Connector> connectors = charger.getConnectors();
					int connectorNumber = 0;
					charger.setChargerNumberOfConnector(connectorNumber);
					Connector connector = connectorConverter.dtoToEntity(connectorDTO);
					connector.setConnectorId("CONN" + idAndDateGenerator.idGenerator());
					connector.setActive(true);
					connector.setCreatedBy("Admin");
					connector.setCreatedDate(idAndDateGenerator.dateSetter());
					connector.setModifiedBy("Admin");
					connector.setModifiedDate(idAndDateGenerator.dateSetter());
					connectors.add(connector);
					charger.setConnectors(connectors);
					List<Connector> list = charger.getConnectors();
					for (Connector connector2 : list)
						connectorNumber++;
					charger.setChargerNumberOfConnector(connectorNumber);
					Station obj = stationRepository.save(station);
					if (obj != null)
						return true;
					else
						throw new InValidDataException("Connector Data Cannot Be Empty. Please Check and Try Again");
				}
			} else
				throw new InValidDataException("Object not found. The requested object is null or does not exist");
		} else
			throw new StationIdNotAcceptableException(
					"Invalid ID. The ID provided is not valid. Please check and try again.");
		return false;
	}

	@Transactional
	@Override
	public boolean updateStation(String stationId, StationUpdateDTO stationUpdateDTO) {
		if (!stationId.isBlank() && stationId != null) {

			Station stationObj = stationConveter.updateDtoToEntity(stationUpdateDTO);

			Station obj = stationRepository.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));

			if (obj != null) {
				boolean flag = false;

				if (stationObj.getStationName() != null && !stationObj.getStationName().isBlank()) {
					obj.setStationName(utility.toTitleCase(stationObj.getStationName()));
					flag = true;
				}

				if (stationObj.getStationHostId() != null && !stationObj.getStationHostId().isBlank()) {
					obj.setStationHostId(stationObj.getStationHostId());
					flag = true;
				}

				if (stationObj.getStationVendorId() != null && !stationObj.getStationVendorId().isBlank()) {
					obj.setStationVendorId(stationObj.getStationVendorId());
					flag = true;
				}

				if (stationObj.getStationArea() != null && !stationObj.getStationArea().isBlank()) {
					obj.setStationArea(stationObj.getStationArea());
					flag = true;
				}

				if (stationObj.getStationAddressLineOne() != null && !stationObj.getStationAddressLineOne().isBlank()) {
					obj.setStationAddressLineOne(stationObj.getStationAddressLineOne());
					flag = true;
				}

				if (stationObj.getStationAddressLineTwo() != null && !stationObj.getStationAddressLineTwo().isBlank()) {
					obj.setStationAddressLineTwo(stationObj.getStationAddressLineTwo());
					flag = true;
				}

				if (stationObj.getStationZipCode() != null && !stationObj.getStationZipCode().isBlank()) {
					obj.setStationZipCode(stationObj.getStationZipCode());
					flag = true;
				}

				if (stationObj.getStationCity() != null && !stationObj.getStationCity().isBlank()) {
					obj.setStationCity(utility.toTitleCase(stationObj.getStationCity()));
					flag = true;
				}

				if (stationObj.getStationLatitude() != 0) {
					obj.setStationLatitude(utility.sanitizeCoordinate(stationObj.getStationLatitude()));
					flag = true;
				}

				if (stationObj.getStationLongitude() != 0) {
					obj.setStationLongitude(utility.sanitizeCoordinate(stationObj.getStationLongitude()));
					flag = true;
				}

				if (stationObj.getStationLocationURL() != null && !stationObj.getStationLocationURL().isBlank())
					obj.setStationLocationURL(stationObj.getStationLocationURL());

				if (stationObj.getStationParkingArea() != null && !stationObj.getStationParkingArea().isBlank()) {
					obj.setStationParkingArea(stationObj.getStationParkingArea());

					if (stationObj.getStationContactNumber() != null && !stationObj.getStationContactNumber().isBlank())
						obj.setStationContactNumber(stationObj.getStationContactNumber());
					flag = true;
				}

				if (stationObj.getStationWorkingTime() != null && !stationObj.getStationWorkingTime().isBlank()) {
					obj.setStationWorkingTime(stationObj.getStationWorkingTime());
					flag = true;
				}

				if (stationObj.getChargerNumber() != 0) {
					obj.setChargerNumber(stationObj.getChargerNumber());
					flag = true;
				}

				if (stationObj.getStationParkingType() != null && !stationObj.getStationParkingType().isBlank()) {
					obj.setStationParkingType(stationObj.getStationParkingType());
					flag = true;
				}

				List<String> list = obj.getStationAmenity();

				List<String> inputList = stationObj.getStationAmenity();

				for (int i = 0; i < inputList.size(); i++) {
					String temp = utility.toTitleCase(inputList.get(i));
					if (temp != null && !temp.isBlank()) {
						list.set(i, temp);
						flag = true;
					}
				}

				obj.setModifiedBy("Admin");
				obj.setModifiedDate(idAndDateGenerator.dateSetter());
				if (flag) {
					if (stationRepository.save(obj) != null)
						return true;
					else
						throw new InValidDataException("Station Not Updated.Please Check and Try Again");
				} else
					throw new StationIdNotAcceptableException("Please Check Enter Data. And try Again");

			} else {
				throw new StationNotFoundException("Station Not Aavailable. Please Check and Try Again");
			}
		} else {
			throw new StationIdNotAcceptableException(
					"Invalid ID. The ID provided is not valid. Please check and try again.");
		}
	}

	@Transactional
	@Override
	public boolean updateCharger(String stationId, String chargerId, ChargerDTO chargerDTO) {

		if (!stationId.isBlank() && stationId != null) {

			if (!chargerId.isBlank() && chargerId != null) {

				Station station = stationRepository.findByStationIdAndIsActiveTrue(utility.toTitleCase(stationId));

				if (station != null) {

					Charger obj = chargerConverter.dtoToEntity(chargerDTO);

					List<Charger> chargers = station.getChargers();

					if (!chargers.isEmpty()) {
						Charger charger = null;
						int index = 0;
						for (int i = 0; i < chargers.size(); i++) {
							if (chargers.get(i).getChargerId().equals(utility.stringSanitization(chargerId))) {
								charger = chargers.get(i);
								index = i;
								break;
							}
						}

						if (charger != null) {
							boolean flag = false;

							if (obj.getChargerName() != null && !obj.getChargerName().isBlank()) {
								charger.setChargerName(utility.toTitleCase(obj.getChargerName()));
								flag = true;
							}

							if (obj.getChargerNumber() != 0) {
								charger.setChargerNumber(obj.getChargerNumber());
								flag = true;
							}

							if (obj.getChargerInputVoltage() != null && !obj.getChargerInputVoltage().isBlank()) {
								charger.setChargerInputVoltage(obj.getChargerInputVoltage());
								flag = true;
							}

							if (obj.getChargerOutputVoltage() != null && !obj.getChargerOutputVoltage().isBlank()) {
								charger.setChargerOutputVoltage(obj.getChargerOutputVoltage());
								flag = true;
							}

							if (obj.getChargerMinInputAmpere() != null && obj.getChargerMinInputAmpere().isBlank()) {
								charger.setChargerMinInputAmpere(obj.getChargerMinInputAmpere());
								flag = true;
							}

							if (obj.getChargerMaxInputAmpere() != null && obj.getChargerMaxInputAmpere().isBlank()) {
								charger.setChargerMaxInputAmpere(obj.getChargerMaxInputAmpere());
								flag = true;
							}

							if (obj.getChargerOutputAmpere() != null && obj.getChargerOutputAmpere().isBlank()) {
								charger.setChargerOutputAmpere(obj.getChargerOutputAmpere());
								flag = true;
							}

							if (obj.getChargerInputFrequency() != null && obj.getChargerInputFrequency().isBlank()) {
								charger.setChargerInputFrequency(obj.getChargerInputFrequency());
								flag = true;
							}

							if (obj.getChargerOutputFrequency() != null && obj.getChargerOutputFrequency().isBlank()) {
								charger.setChargerOutputFrequency(obj.getChargerOutputFrequency());
								flag = true;
							}

							if (obj.getChargerIPRating() != null && obj.getChargerIPRating().isBlank()) {
								charger.setChargerIPRating(obj.getChargerIPRating());
								flag = true;
							}

							if (obj.getChargerMountType() != null && obj.getChargerMountType().isBlank()) {
								charger.setChargerMountType(obj.getChargerMountType());
								flag = true;
							}

							if (obj.getChargerNumberOfConnector() != 0) {
								charger.setChargerNumberOfConnector(obj.getChargerNumberOfConnector());
								flag = true;
							}

							if (obj.getIsRFID() != null && obj.getIsRFID().isBlank()) {
								charger.setIsRFID(obj.getIsRFID());
								flag = true;
							}

							if (obj.getChargerSerialNumber() != null && obj.getChargerSerialNumber().isBlank()) {
								charger.setChargerSerialNumber(obj.getChargerSerialNumber());
								flag = true;
							}

							if (obj.getChargerOCPPProtocol() != null && obj.getChargerOCPPProtocol().isBlank()) {
								charger.setChargerOCPPProtocol(obj.getChargerOCPPProtocol());
								flag = true;
							}

							if (obj.getIsAppSupport() != null && obj.getIsAppSupport().isBlank()) {
								charger.setIsAppSupport(obj.getIsAppSupport());
								flag = true;
							}

							if (obj.getIsTBCutOff() != null && obj.getIsTBCutOff().isBlank()) {
								charger.setIsTBCutOff(obj.getIsTBCutOff());
								flag = true;
							}

							if (obj.getIsAntitheft() != null && obj.getIsAntitheft().isBlank()) {
								charger.setIsAntitheft(obj.getIsAntitheft());
								flag = true;
							}

							if (obj.getIsLEDDisplay() != null && obj.getIsLEDDisplay().isBlank()) {
								charger.setIsLEDDisplay(obj.getIsLEDDisplay());
								flag = true;
							}

							if (obj.getIsLEDIndications() != null && obj.getIsLEDIndications().isBlank()) {
								charger.setIsLEDIndications(obj.getIsLEDIndications());
								flag = true;
							}

							if (obj.getIsSmart() != null && obj.getIsSmart().isBlank()) {
								charger.setIsSmart(obj.getIsSmart());
								flag = true;
							}

							charger.setModifiedDate(idAndDateGenerator.dateSetter());
							charger.setModifiedBy("Admin");
							chargers.set(index, charger);
							station.setChargers(chargers);

							if (flag) {
								if (stationRepository.save(station) != null)
									return true;
								else
									throw new InValidDataException(
											"Charger Details Not Updated. Please Check and Try Again");
							} else
								throw new StationIdNotAcceptableException("Please Check Enter Data. And try Again");
						} else
							throw new ChargerNotFoundException("Charger Data Not Available");
					} else
						throw new ChargerNotFoundException(
								"No Charger Data. There is no data available for chargers at the moment. Please check again");
				} else
					throw new StationNotFoundException(
							"Station Not Found. The station with the provided ID does not exist. Please verify the station ID and try again");
			} else
				throw new InValidIdExcepetion("Invalid ID. The ID provided is not valid. Please check and try again.");
		} else
			throw new InValidIdExcepetion("Invalid ID. The ID provided is not valid. Please check and try again.");
	}

	@Override
	public boolean updateConnector(String stationId, String chargerId, String connectorId, ConnectorDTO connectorDTO) {
		if (!stationId.isBlank() && stationId != null) {

			if (!chargerId.isBlank() && chargerId != null) {

				if (!connectorId.isBlank() && connectorId != null) {

					Station station = stationRepository
							.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));

					if (station != null) {
						List<Charger> chargers = station.getChargers();

						if (!chargers.isEmpty()) {

							int chargerIndex = 0;
							Charger ch = new Charger();
							List<Connector> connectors = new ArrayList<>();

							for (int i = 0; i < chargers.size(); i++) {
								if (chargers.get(i).getChargerId().equals(utility.stringSanitization(chargerId))) {
									connectors = chargers.get(i).getConnectors();
									ch = chargers.get(i);
									chargerIndex = i;
									break;
								}
							}
							if (!connectors.isEmpty()) {
								Connector connector = null;
								int connectorIndex = 0;
								for (int i = 0; i < connectors.size(); i++) {
									if (connectors.get(i).getConnectorId()
											.equals(utility.stringSanitization(connectorId))) {
										connector = connectors.get(i);
										connectorIndex = i;
										break;
									}
								}

								if (connector != null) {

									Connector obj = connectorConverter.dtoToEntity(connectorDTO);

									if (obj.getConnectorNumber() != 0)
										connector.setConnectorNumber(obj.getConnectorNumber());

									if (obj.getConnectorType() != null && !obj.getConnectorType().isBlank())
										connector.setConnectorType(obj.getConnectorType());

									if (obj.getConnectorSocket() != null && !obj.getConnectorSocket().isBlank())
										connector.setConnectorSocket(obj.getConnectorSocket());

									if (obj.getConnectorStatus() != null && !obj.getConnectorStatus().isBlank())
										connector.setConnectorStatus(obj.getConnectorStatus());

									if (obj.getConnectorOutputPower() != null
											&& !obj.getConnectorOutputPower().isBlank())
										connector.setConnectorOutputPower(obj.getConnectorOutputPower());

									if (obj.getConnectorCharges() != null && !obj.getConnectorCharges().isBlank())
										connector.setConnectorCharges(obj.getConnectorCharges());

									connector.setModifiedBy("Admin");
									connector.setModifiedDate(idAndDateGenerator.dateSetter());

									connectors.set(connectorIndex, connector);

									ch.setConnectors(connectors);
									chargers.set(chargerIndex, ch);
									station.setChargers(chargers);

									if (stationRepository.save(station) != null)
										return true;
									else
										throw new InValidDataException(
												"Connector Not Updated. Please Check and Try Again");
								} else
									throw new InValidDataException(
											"Connector Not Found. The connector with the given ID is not present. Please verify the connector ID and try again");
							} else
								throw new InValidDataException(
										"No Connectors Found. There are no connectors available in the system. Please verify and try again");
						} else
							throw new InValidDataException(
									"No Chargers Found. There are no Charger available in the system. Please verify and try again");
					} else
						throw new StationNotFoundException(
								"Station Not Found. The station with the provided ID does not exist. Please verify and try again");
				} else
					throw new InValidIdExcepetion(
							"Invalid Connector ID. The ID provided is not valid. Please check and try again.");
			} else
				throw new InValidIdExcepetion(
						"Invalid Charger ID. The ID provided is not valid. Please check and try again.");
		} else
			throw new InValidIdExcepetion(
					"Invalid Station ID. The ID provided is not valid. Please check and try again.");
	}

	@Override
	@Transactional
	public boolean removeStation(String stationId) {
		if (!stationId.isBlank() && stationId != null) {
			Station obj = stationRepository.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
			if (obj != null) {
				obj.setActive(false);
				stationRepository.save(obj);
				return true;
			} else {
				throw new StationNotFoundException(
						"Station Not Found. The station with the provided ID does not exist. Please verify and try again");
			}
		} else {
			throw new InValidIdExcepetion(
					"Invalid Station ID. The ID provided is not valid. Please check and try again.");
		}
	}

	@Override
	@Transactional
	public boolean removeStationCharger(String stationId, String chargerId) {
		if (!stationId.isBlank() && stationId != null) {

			if (!chargerId.isBlank() && chargerId != null) {

				Station station = stationRepository
						.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));

				if (station != null) {

					List<Charger> chargers = station.getChargers();

					if (!chargers.isEmpty()) {
						Charger c = new Charger();
						int chargerIndex = 0;
						for (int i = 0; i < chargers.size(); i++) {
							if (chargers.get(i).getChargerId().equals(utility.stringSanitization(chargerId))) {
								c = chargers.get(i);
								if (c.isActive() == false) {
									throw new StationNotFoundException("No Data found");
								} else
									chargerIndex = i;
								break;
							}
						}
						c.setActive(false);
						int chargerNumber = c.getChargerNumber();
						chargerNumber--;
						c.setChargerNumber(chargerNumber);
						station.setChargerNumber(chargerNumber);
						c.setModifiedBy("Admin");
						c.setModifiedDate(idAndDateGenerator.dateSetter());
						chargers.set(chargerIndex, c);
						station.setChargers(chargers);
						if (stationRepository.save(station) != null)
							return true;
						else
							throw new InValidDataException("Deletion Failed: The Charger could not be deleted.");
					} else
						throw new InValidDataException(
								"No Chargers Found. There are no Charger available in the system. Please verify and try again");
				} else
					throw new StationNotFoundException(
							"Station Not Found. The station with the pr"
							+ "ovided ID does not exist. Please verify and try again");
			} else
				throw new InValidIdExcepetion(
						"Invalid Charger ID. The ID provided is not valid. Please check and try again.");
		} else
			throw new StationIdNotAcceptableException(
					"Invalid Station ID. The ID provided is not valid. Please check and try again.");
	}

	@Transactional
	@Override
	public boolean removeStationConnector(String stationId, String chargerId, String connectorId) {
		if (!stationId.isBlank() && stationId != null) {

			if (!chargerId.isBlank() && chargerId != null) {

				if (!connectorId.isBlank() && connectorId != null) {

					Station station = stationRepository
							.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));

					if (station != null) {

						List<Charger> chargers = station.getChargers();

						if (!chargers.isEmpty()) {

							Charger c = new Charger();

							int chargerIndex = 0;

							for (int i = 0; i < chargers.size(); i++) {
								if (chargers.get(i).getChargerId().equals(utility.stringSanitization(chargerId))) {
									c = chargers.get(i);

									if (c != null) {
										if (c.isActive() == true) {

											chargerIndex = i;
											break;
										} else
											throw new InValidDataException(
													"Charger not available. Please Check try again later");
									} else
										throw new InValidDataException(
												"Charger not available. Please Check try again later");
								} else
									throw new InValidIdExcepetion(
											"The Charger ID does not match. Please check and try again");
							}

							List<Connector> connectors = c.getConnectors();
							Connector conn = new Connector();

							int connectorIndex = 0;

							for (int i = 0; i < connectors.size(); i++) {

								if (connectors.get(i).getConnectorId()
										.equals(utility.stringSanitization(connectorId))) {

									conn = connectors.get(i);
									if (conn != null) {

										connectorIndex = i;
										if (conn.isActive() == true) {
											conn.setActive(false);
											int num = conn.getConnectorNumber();
											num--;
											conn.setConnectorNumber(num);
											c.setChargerNumberOfConnector(num);
											conn.setModifiedDate(idAndDateGenerator.dateSetter());
											conn.setModifiedBy("Admin");
											connectors.set(connectorIndex, conn);
											c.setModifiedDate(idAndDateGenerator.dateSetter());
											c.setModifiedBy("Admin");
											c.setConnectors(connectors);
											chargers.set(chargerIndex, c);
											station.setModifiedDate(idAndDateGenerator.dateSetter());
											station.setModifiedBy("Admin");
											station.setChargers(chargers);

											if (stationRepository.save(station) != null)
												return true;
											else
												throw new InValidDataException(
														"Deletion Failed: The Connector could not be deleted.");
										} else
											throw new InValidDataException(
													"Connector Not Found. There are no Connector available in the system. Please verify and try again.");
									} else
										throw new StationIdNotAcceptableException(
												"Connector Not Found. There are no Connector available in the system. Please verify and try again.");
								}
							}
						} else
							throw new InValidDataException(
									"No Chargers Found. There are no Charger available in the system. Please verify and try again");
					} else
						throw new StationNotFoundException(
								"Station Not Found. The station with the provided ID does not exist. Please verify and try again");
				} else
					throw new InValidIdExcepetion(
							"Invalid Connector ID. The ID provided is not valid. Please check and try again.");

			} else
				throw new InValidIdExcepetion(
						"Invalid Charger ID. The ID provided is not valid. Please check and try again.");

		} else
			throw new InValidIdExcepetion(
					"Invalid Station ID. The ID provided is not valid. Please check and try again.");
		return false;
	}

	@Override
	public List<Station> showAll() {
		List<Station> list = stationRepository.findAllByIsActiveTrue();
		if (!list.isEmpty()) {
			return list;
		} else {
			throw new StationNotFoundException("There is no stations. Please check and try agian");
		}
	}

	@Override
	public Station show(String stationId) {
		if (!stationId.trim().isBlank() && stationId != null) {
			Station station = stationRepository.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
			if (station != null) {
				return station;
			} else {
				return station;
//				throw new InValidDataException("Station Not Aavailable. Please Check and Try Again");
			}
		} else {
			throw new StationIdNotAcceptableException(
					"Invalid Station ID. The ID provided is not valid. Please check and try again.");
		}
	}

	@Override
	public List<Station> getByHostId(String stationHostId) {
		if (!stationHostId.trim().isBlank() && stationHostId != null) {
			List<Station> list = stationRepository
					.findByStationHostIdAndIsActiveTrue(utility.stringSanitization(stationHostId));
			if (!list.isEmpty()) {
				return list;
			} else {
				return list;
			}
		} else {
			throw new StationIdNotAcceptableException("Host Details are not found.Please check and try again.");
		}
	}

	@Override
	public List<Station> getByVendorId(String stationVendorId) {
		if (!stationVendorId.trim().isBlank() && stationVendorId != null) {
			List<Station> list = stationRepository
					.findByStationVendorIdAndIsActiveTrue(utility.stringSanitization(stationVendorId));
			if (!list.isEmpty()) {
				return list;
			} else {
				return list;
			}
		} else {
			throw new StationIdNotAcceptableException("Vendor Details are not found.Please check and try again.");
		}
	}

	@Override
	public List<Station> getInactiveStation() {
		List<Station> list = stationRepository.findAllByIsActiveFalse();
		if (!list.isEmpty()) {
			return list;
		} else {
			return list;
		}
	}

	@Override
	public List<Charger> getAllStationChargers(String stationId) {
		if (!stationId.isBlank() && stationId != null) {
			Station station = stationRepository.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
			if (station != null) {
				List<Charger> chargers = station.getChargers();
				if (!chargers.isEmpty()) {
					List<Charger> finalList = new ArrayList<>();
					for (Charger list : chargers) {
						boolean flag = list.isActive();
						if (flag == true)
							finalList.add(list);
					}
					return finalList;
				} else
					throw new InValidDataException(
							"No Charger Found. There are no Charger available in the Station. Please Check and try again");
			} else
				throw new InValidDataException(
						"No Station Found. There are no Station available in the system. Please Check and try again");
		} else
			throw new StationIdNotAcceptableException(
					"Invalid Station ID. The ID provided is not valid. Please check and try again.");
	}

	@Override
	public List<Connector> getAllStationConnector(String stationId, String chargerId) {
		if (!stationId.isBlank()) {
			if (!chargerId.isBlank()) {
				Station station = stationRepository.findByStationIdAndIsActiveTrue(stationId);
				if (station != null) {
					List<Charger> chargers = station.getChargers();
					if (!chargers.isEmpty()) {
						Charger chargerOne = null;
						for (Charger c : chargers) {
							if (c.getChargerId().equals(chargerId)) {
								chargerOne = c;
								break;
							}
						}
						if (chargerOne == null) {
							throw new InValidDataException(
									"No Charger Found. There is no Charger available. Please Check and try again");
						}
						List<Connector> connectors = chargerOne.getConnectors();
						if (!connectors.isEmpty()) {
							List<Connector> finalConnector = new ArrayList<>();
							for (int i = 0; i < connectors.size(); i++) {
								boolean flag = connectors.get(i).isActive();
								if (flag == true)
									finalConnector.add(connectors.get(i));
							}
							return finalConnector;
						} else
							throw new InValidDataException(
									"No Connectors Found. There are no Connectors available in the Charger. Please Check and try again");
					} else
						throw new InValidDataException(
								"No Chargers Found. There are no Charger available in the Station. Please Check and try again");
				} else
					throw new InValidDataException(
							"No Charger Found. There are no Connectors available in the Charger. Please Check and try again");
			} else
				throw new InValidIdExcepetion(
						"Invalid Charger ID. The ID provided is not valid. Please check and try again.");
		} else
			throw new StationIdNotAcceptableException(
					"Invalid Station ID. The ID provided is not valid. Please check and try again.");
	}

	@Override
	public Connector getConnector(String connectorId) {
		if (!connectorId.isBlank()) {
			Station s = stationRepository.findByConnectorId(connectorId);
			List<Charger> cs = s.getChargers();

			if (!cs.isEmpty()) {

				List<Connector> connectors = new ArrayList<>();

				for (Charger c : cs) {
					connectors = c.getConnectors();
				}

				Connector connector = null;

				if (!connectors.isEmpty()) {

					for (Connector list : connectors) {
						if (list.getConnectorId().equals(connectorId)) {
							connector = list;
							break;
						}
					}
					if (connector != null) {
						return connector;
					} else {
						throw new InValidDataException(
								"No Connectors Found. There is no Connector available in the Charger. Please Check and try again");
					}
				} else
					throw new InValidDataException(
							"No Connectors Found. There are no Connectors available in the Charger. Please Check and try again");

			} else
				throw new InValidDataException(
						"No Charger Found. There are no Chargers available in the Station. Please Check and try again");

		} else
			throw new InValidIdExcepetion(
					"Invalid Connector ID. The ID provided is not valid. Please check and try again.");
	}

	@Override
	public List<StationDTO1> getRequiredStationData() {
		List<Station> stations = stationRepository.findAllByIsActiveTrue();
		List<StationDTO1> dto1 = new ArrayList<>();
		if (!stations.isEmpty()) {

			for (Station s : stations) {
				dto1.add(stationConveter.entitytoStationDTO1(s));
			}
			return dto1;
		} else
			return dto1;
	}

	@Override
	public List<StationDTO1> stationforApplication(String query) {

		List<StationDTO1> dtos = new ArrayList<>();
		if (!query.isBlank()) {

			List<Station> list = stationRepository
					.findByStationNameContainingIgnoreCaseAndIsActiveTrueOrStationAreaContainingIgnoreCaseAndIsActiveTrueOrStationZipCodeContainingIgnoreCaseAndIsActiveTrueOrStationCityContainingIgnoreCaseAndIsActiveTrueOrStationStatusContainingIgnoreCaseAndIsActiveTrue(
							query, query, query, query, query);

			if (!list.isEmpty()) {

				for (Station station : list) {
					dtos.add(stationConveter.entitytoStationDTO1(station));
				}
				return dtos;
			} else
				return dtos;
		} else
			return dtos;
	}

	public List<String> am() {
		ArrayList<String> list = new ArrayList<>();
		list.add("Restrooms");
		list.add("Lounge Area");
		list.add("Food Service");
		list.add("Shops");
		list.add("Movie Theater");
		return list;

	}

	public void mul() {
		Coordinate coordinate = new Coordinate();
		for (int i = 0; i < 100; i++) {
			Station station = new Station();
			Location location = new Location();
			Station obj = null;
			while (true) {
				String id = "STN" + idAndDateGenerator.idGenerator();
				obj = stationRepository.findByStationId(id);
				if (obj == null) {
					station.setStationId(id);
					break;
				}
			}
			station.setStationName("Random");
			station.setStationHostId("HST00" + i);
			station.setStationVendorId("VTN00" + i);
			station.setStationArea("Random");
			double a[] = location.getCoordinates();
//			a[0] = coordinates.generateLongitude(i);
//			a[1] = coordinates.generateLatitude(i);
			a[0] = coordinate.generatelongitude();
			a[1] = coordinate.generatelatitude();
			location.setCoordinates(a);
			station.setLocation(location);
			station.setStationLatitude(a[1]);
			station.setStationLongitude(a[0]);
			station.setStationLocationURL(
					"https://www.google.com/maps/place/Electric+Vehicle+Charging+Station/@18.5542818,73.923676,14z/");
			station.setStationParkingArea("150sqft");
			station.setStationCity("Pune");
			station.setStationAddressLineOne(
					"108, 109, Pride Icon, Mundhwa - Kharadi Rd, next to Columbia Asia Hospital");
			station.setStationAddressLineTwo("Thite Vasti, Thite Nagar, Hadapsar,Pune, Maharashtra");
			station.setStationZipCode(" 411014");
			station.setStationContactNumber("+919876543210");
			station.setStationWorkingTime("9:00 AM - 10:00 PM");
			station.setChargerNumber(i + 1);
			station.setStationParkingType("Private");
			station.setStationAmenity(am());
			station.setStationShareId("ST000" + i);
			station.setStationStatus("Busy");
			station.setStationPowerStandard("IS-17017");
			station.setCreatedDate(idAndDateGenerator.dateSetter());
			station.setModifiedDate(idAndDateGenerator.dateSetter());
			station.setCreatedBy("Admin");
			station.setModifiedBy("Admin");
			station.setActive(true);
			try {
				stationRepository.save(station);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	@Override
	public Station getStation(String stationId) {
		if (!stationId.isBlank() && stationId != null) {
			Station station = stationRepository.findStationByStationId(stationId);
			if (station != null)
				return station;
			else
				throw new StationNotFoundException("Station Not Found");
		} else
			throw new InValidIdExcepetion(
					"Invalid Connector ID. The ID provided is not valid. Please check and try again.");
	}

	@Override
	public Charger getCharger(String chargerId) {
		if (!chargerId.isBlank() && chargerId != null) {

			Station station = stationRepository.findByChargerId(utility.stringSanitization(chargerId));

			if (station != null) {

				List<Charger> chargers = station.getChargers();

				if (!chargers.isEmpty()) {

					Charger charger = null;

					for (Charger c : chargers) {
						if (c.getChargerId().equals(utility.stringSanitization(chargerId))) {
							charger = c;
							break;
						}
					}
					if (charger != null) {
						if (charger.isActive() == true)
							return charger;
						else
							return null;
					} else
						throw new InValidDataException(
								"Charger Not Found. There is no Charger available in the Station. Please Check and try again");
				} else
					throw new InValidDataException(
							"Chargers Not Found. There are no Chargers available in the Station. Please Check and try again");
			} else
				throw new InValidDataException(
						"Station Not Found. There is not station available. Please Check and try again");
		} else
			throw new InValidIdExcepetion(
					"Invalid Charger ID. The ID provided is not valid. Please check and try again.");
	}

	@Override
	public List<StationDTO1> getAllStationforRadius(double longitude, double latitude, double maxDistance) {

		if (latitude != 0 && longitude != 0 && maxDistance != 0) {
			double longt = utility.sanitizeCoordinate(longitude);
			double lat = utility.sanitizeCoordinate(latitude);
			double minDistance = 10;
			String type = "Point";
			List<Station> stations = stationRepository.findByGeoLocation(type, longt, lat, maxDistance, minDistance);
			List<StationDTO1> finalList = new ArrayList<>();
			if (!stations.isEmpty()) {

				for (Station s : stations) {
					finalList.add(stationConveter.entitytoStationDTO1(s));
				}
				return finalList;
			} else
				return finalList;
		} else
			throw new InValidIdExcepetion("Provided Correct Longitude, Latitude Please check and try again.");
	}

	@Override
	public boolean removeCharger(String chargerId) {
		if (!chargerId.isBlank() && chargerId != null) {
			Station station = stationRepository.findByChargerId(utility.stringSanitization(chargerId));
			String stationId = station.getStationId();
			if (removeStationCharger(stationId, chargerId) == true)
				return true;
			else
				return false;
		} else
			throw new InValidIdExcepetion(
					"Invalid Charger ID. The ID provided is not valid. Please check and try again.");
	}

//	for (int i = 0; i < chargers.size()-1; i++) {
//		System.out.println("1149");
//	Charger charger = null;	
//	
//	
//	charger = chargers.get(i);
//	List<Connector> connector =	charger.getConnectors();
//	for (int j = 0; j < connector.size()-1; i++) {
//		Connector conn = connector.get(i);
//		if (conn.getConnectorId().equals(utility.stringSanitization(connectorId))) {
//			chargerId= charger.getChargerId(); 
//		}
//		}
//	}

	@Override
	public boolean removeConnector(String connectorId) {

		if (connectorId != null && !connectorId.isBlank()) {
			Station station = stationRepository.findByConnectorId(utility.stringSanitization(connectorId));
			if (station != null) {
				if (station.isActive()) {
					String stationId = station.getStationId();
					String chargerId = null;
					boolean foundFlag = false;

					List<Charger> chargerList = station.getChargers();

					if (!chargerList.isEmpty()) {

						for (int i = 0; i < chargerList.size(); i++) {
							Charger charger = chargerList.get(i);
							if (charger.isActive() == true) {

								List<Connector> connectorList = charger.getConnectors();

								for (int j = 0; j < connectorList.size(); j++) {

									Connector conn = connectorList.get(j);

									if (conn.getConnectorId().equals(utility.stringSanitization(connectorId))) {
										if (conn.isActive() == true) {
											chargerId = charger.getChargerId();
											conn.setActive(false);
											conn.setModifiedDate(idAndDateGenerator.dateSetter());
											conn.setModifiedBy("Admin");
											connectorList.set(j, conn);
											charger.setConnectors(connectorList);
											charger.setModifiedDate(idAndDateGenerator.dateSetter());
											charger.setModifiedBy("Admin");
											chargerList.set(i, charger);
											station.setChargers(chargerList);
											station.setModifiedDate(idAndDateGenerator.dateSetter());
											station.setModifiedBy("admin");
											foundFlag = true;
											break;
										} else {
											throw new InValidDataException(
													"Connector Not Found. There are no Connector available in the system. Please verify and try again.");
										}
									}
								}
							}
							if (foundFlag) {
								break;
							}
						}
						if (stationRepository.save(station) != null) {
							return true;
						} else {
							return false;
						}
					} else {
						throw new InValidDataException(
								"No Chargers Found. There are no Charger available in the system. Please verify and try again");
					}
				} else {
					throw new StationNotFoundException(
							"Station Not Found. The station with the provided ID does not exist. Please verify and try again ");
				}
			} else {
				throw new StationNotFoundException(
						"Station Not Found. The station with the provided ID does not exist. Please verify and try again ");
			}
		} else {
			throw new InValidIdExcepetion(
					"Invalid Connector ID. The ID provided is not valid. Please check and try again.");

		}
	}

	@Override
	public boolean updateConnectorById(String connectorId, ConnectorDTO connectorDTO) {
		Station station = stationRepository.findByConnectorId(utility.stringSanitization(connectorId));
		String chargerId = station.getChargers().get(0).getChargerId();
		String stationId = station.getStationId();
		if (updateConnector(stationId, chargerId, connectorId, connectorDTO) == true)
			return true;
		else
			return false;
	}

	@Override
	public StationFindDTO getNameAndAddressStation(String stationId) {
		Station station = stationRepository.findByStationId(utility.stringSanitization(stationId));
		return stationConveter.entitytoStationFind(station);
	}

	public void addMultipleCharger() {

		List<Station> stations = stationRepository.findAll();

		for (int i = 0; i < stations.size(); i++) {

			Station stn = stations.get(i);

			Charger charger = new Charger();

			charger.setChargerName("AC charger" + i);
			charger.setChargerNumber(2);
			charger.setChargerInputVoltage("220v");
			charger.setChargerOutputVoltage("12V");
			charger.setChargerMaxInputAmpere("10A");
			charger.setChargerMaxInputAmpere("20A");
			charger.setChargerOutputAmpere("50A");
			charger.setChargerInputFrequency("50Hz");
			charger.setChargerOutputFrequency("90Hz");
			charger.setChargerIPRating("IP67");
			charger.setChargerMountType("wall mounted");
			charger.setIsRFID("true");
			charger.setChargerSerialNumber("CHG1234" + i);
			charger.setChargerOCPPProtocol("Modbus");
			charger.setChargerConnectorType("type2");
			charger.setIsAppSupport("true");
			charger.setIsTBCutOff("false");
			charger.setIsAntitheft("true");
			charger.setIsLEDIndications("true");
			charger.setIsLEDDisplay("true");
			charger.setIsSmart("true");
			charger.setChargerId("CHG" + idAndDateGenerator.idGenerator());
			charger.setActive(true);
			charger.setCreatedBy("Admin");
			charger.setModifiedBy("Admin");
			charger.setCreatedDate(idAndDateGenerator.dateSetter());
			charger.setModifiedDate(idAndDateGenerator.dateSetter());
			stn.getChargers().add(charger);
			stationRepository.save(stn);
		}

	}

	public void addMultipleConnector() {

		List<Station> stations = stationRepository.findAll();

		for (int i = 0; i < stations.size(); i++) {

			Station stn = stations.get(i);

			List<Charger> chargers = stn.getChargers();

			for (int j = 0; j < chargers.size(); j++) {
				Charger charger = chargers.get(j);
				Connector connector = new Connector();

				connector.setConnectorNumber(2);
				connector.setConnectorType("Type 2");
				connector.setConnectorSocket("Socket B");
				connector.setConnectorStatus("available");
				connector.setConnectorOutputPower("60W");
				connector.setConnectorCharges("20RS");
				connector.setConnectorId("CONN" + idAndDateGenerator.idGenerator());
				connector.setActive(true);
				connector.setCreatedBy("Admin");
				connector.setCreatedDate(idAndDateGenerator.dateSetter());
				connector.setModifiedBy("Admin");
				connector.setModifiedDate(idAndDateGenerator.dateSetter());
				charger.getConnectors().add(connector);
				chargers.set(j, charger);
				stn.setChargers(chargers);
				stationRepository.save(stn);
			}

		}

	}

}
