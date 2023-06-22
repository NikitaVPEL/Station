package com.vst.station.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vst.station.converter.ChargerConverter;
import com.vst.station.converter.ConnectorConverter;
import com.vst.station.dto.ChargerDTO;
import com.vst.station.dto.ChargerUpdateDTO;
import com.vst.station.dto.ConnectorDTO;
import com.vst.station.dto.ConnectorUpdateDTO;
import com.vst.station.dto.ocppVerificationDTO;
import com.vst.station.exception.ChargerNotFoundException;
import com.vst.station.exception.InValidDataException;
import com.vst.station.exception.InValidIdExcepetion;
import com.vst.station.exception.StationException;
import com.vst.station.exception.StationIdNotAcceptableException;
import com.vst.station.exception.StationNotFoundException;
import com.vst.station.model.Charger;
import com.vst.station.model.Connector;
import com.vst.station.model.Station;
import com.vst.station.repository.StationRepository;
import com.vst.station.utils.IdAndDateGenerator;
import com.vst.station.utils.Utility;

/**
 * @exception : @throws : {@link @stationException} if any error occur while the
 *              code.
 * @exception : @throws : {@link @InValidIdExcepetion} if received id is null.
 * @exception : @throws : {@link @InValidDataException} if received object is
 *              null.
 * @exception : @throws : {@link @StationNotFoundException} if station object is
 *              null.
 * @exception : @throws : {@link @StationIdNotAcceptableException} if station id
 *              is null
 * @exception : @throws : {@link @ChargerNotFoundException} if charger object is
 *              null
 *
 */

@SuppressWarnings("unused")
@Service
public class ChargerServiceImpl implements ChargerServiceInterface {

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private ChargerConverter chargerConverter;

	@Autowired
	private ConnectorConverter connectorConverter;

	IdAndDateGenerator idAndDateGenerator = new IdAndDateGenerator();

	Utility utility = new Utility();

	public static final Logger logger = LogManager.getLogger(ChargerServiceImpl.class);

	/**
	 * Usage: add Charger in the specific station using station id
	 * 
	 * @param stationId, ChargerDto
	 * @return Boolean (true/false)
	 */
	@Transactional
	@Override
	public boolean addCharger(String stationId, ChargerDTO chargerDTO) {
		logger.info("StationServiceImpl :: addCharger : execution Started");
		try {
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

						if (stationRepository.save(obj) != null) {
							logger.info("StationServiceImpl :: addCharger : execution ended");
							return true;
						} else
							throw new InValidDataException("Charger Data Cannot Be Empty. Please Check and Try Again");
					} else
						throw new StationNotFoundException("Station not Available Please Check and try again");
				} else
					throw new InValidDataException("Please ensure that you have provided a valid Data and try again.");
			} else
				throw new InValidIdExcepetion("Please Provide Valid ID");

		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidIdExcepetion(e.getLocalizedMessage());

		} catch (Exception e) {

			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(), "Add New Charger",
					e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(), "Add New Charger",
					e.getLocalizedMessage());
		}

	}

	/**
	 * Usage: add Connector in specific charger of specific Station using station id
	 * and charger id
	 * 
	 * @param stationId, chargerId, ConnectorDto
	 * @return boolean (true/false)
	 */
	@Transactional
	@Override
	public boolean addConnector(String stationId, String chargerId, ConnectorDTO connectorDTO) {
		logger.info("StationServiceImpl :: addConnector : execution Started");
		try {

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
						if (charger != null) {
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
							if (obj != null) {

								return true;
							} else
								throw new InValidDataException(
										"Connector Data Cannot Be Empty. Please Check and Try Again");
						} else {
							throw new InValidDataException("Charger Not Found. Please Check and Try Again");
						}
					} else {
						throw new StationNotFoundException("Station not Available Please Check and try again");
					}
				} else
					throw new InValidDataException("Connector details cannot be null or invalid details");
			} else
				throw new InValidIdExcepetion("Invalid ID. The ID provided is not valid. Please check and try again.");
		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());

		} catch (StationIdNotAcceptableException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidIdExcepetion(e.getLocalizedMessage());

		} catch (Exception e) {

			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(), "Add New Connector",
					e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(), "Add New Connector",
					e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: update Charger using station id and Charger id
	 * 
	 * @param stationId,chargerId, chargerDTO
	 * @return boolean (true/false)
	 */
	@Transactional
	@Override
	public boolean updateCharger(String stationId, String chargerId, ChargerUpdateDTO chargerDTO) {
		logger.info("StationServiceImpl :: updateCharger : execution Started");
		try {
			if (!stationId.isBlank() && stationId != null) {
				if (!chargerId.isBlank() && chargerId != null) {

					Station station = stationRepository
							.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
					if (station != null) {
						Charger obj = chargerConverter.dtoToEntity1(chargerDTO);

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

								if (obj.getChargerMinInputAmpere() != null
										&& obj.getChargerMinInputAmpere().isBlank()) {
									charger.setChargerMinInputAmpere(obj.getChargerMinInputAmpere());
									flag = true;
								}

								if (obj.getChargerMaxInputAmpere() != null
										&& obj.getChargerMaxInputAmpere().isBlank()) {
									charger.setChargerMaxInputAmpere(obj.getChargerMaxInputAmpere());
									flag = true;
								}

								if (obj.getChargerOutputAmpere() != null && obj.getChargerOutputAmpere().isBlank()) {
									charger.setChargerOutputAmpere(obj.getChargerOutputAmpere());
									flag = true;
								}

								if (obj.getChargerInputFrequency() != null
										&& obj.getChargerInputFrequency().isBlank()) {
									charger.setChargerInputFrequency(obj.getChargerInputFrequency());
									flag = true;
								}

								if (obj.getChargerOutputFrequency() != null
										&& obj.getChargerOutputFrequency().isBlank()) {
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
									if (stationRepository.save(station) != null) {
										logger.info("StationServiceImpl :: updateCharger : execution Ended");
										return true;
									} else
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
								"Station Not Found,Please verify the station ID and try again");
				} else
					throw new InValidIdExcepetion(
							"Invalid charger ID. The ID provided is not valid. Please check and try again.");
			} else
				throw new InValidIdExcepetion("Invalid station ID, Please check and try again.");

		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());

		} catch (StationIdNotAcceptableException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (ChargerNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new ChargerNotFoundException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidIdExcepetion(e.getLocalizedMessage());

		} catch (Exception e) {

			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"update charger using Station Id and charger id ", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"update charger using Station Id and charger id ", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: update connector of specific charger
	 * 
	 * @param connectorId
	 * @return boolean (true/false)
	 */
	@Transactional
	@Override
	public boolean updateConnectorById(String connectorId, ConnectorUpdateDTO connectorDTO) {

		logger.info("StationServiceImpl :: updateConnectorById : execution Started");
		try {
			if (connectorId != null && !connectorId.isBlank()) {
				Station station = stationRepository.findByConnectorId(utility.stringSanitization(connectorId));
				if (station != null && station.isActive()) {

					boolean foundFlag = false;
					List<Charger> chargerList = station.getChargers();

					if (!chargerList.isEmpty()) {
						for (int i = 0; i < chargerList.size(); i++) {
							Charger charger = chargerList.get(i);
							if (charger.isActive() == true) {

								List<Connector> connectorList = charger.getConnectors();
								if (!connectorList.isEmpty()) {

									for (int j = 0; j < connectorList.size(); j++) {
										Connector conn = connectorList.get(j);

										if (conn.getConnectorId().equals(utility.stringSanitization(connectorId))) {
											if (conn.isActive() == true) {
												Connector obj = connectorConverter.dtoToEntity1(connectorDTO);

												if (obj.getConnectorNumber() != 0)
													conn.setConnectorNumber(obj.getConnectorNumber());

												if (obj.getConnectorType() != null && !obj.getConnectorType().isBlank())
													conn.setConnectorType(obj.getConnectorType());

												if (obj.getConnectorSocket() != null
														&& !obj.getConnectorSocket().isBlank())
													conn.setConnectorSocket(obj.getConnectorSocket());

												if (obj.getConnectorStatus() != null
														&& !obj.getConnectorStatus().isBlank())
													conn.setConnectorStatus(obj.getConnectorStatus());

												if (obj.getConnectorOutputPower() != null
														&& !obj.getConnectorOutputPower().isBlank())
													conn.setConnectorOutputPower(obj.getConnectorOutputPower());

												if (obj.getConnectorCharges() != null
														&& !obj.getConnectorCharges().isBlank())
													conn.setConnectorCharges(obj.getConnectorCharges());

												conn.setModifiedBy("Admin");
												conn.setModifiedDate(idAndDateGenerator.dateSetter());

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
											} else
												throw new InValidIdExcepetion(
														"Connector Not Found. There are no Connector available in the system. Please verify and try again.");
										}
									}
								}
								if (foundFlag)
									break;
							}
						}
						if (foundFlag == true) {
							stationRepository.save(station);
							logger.info("StationServiceImpl :: updateConnectorById : execution ended");
							return true;
						} else
							throw new InValidDataException("Connector Not Found, please try again");
					} else
						throw new ChargerNotFoundException(
								"No Chargers Found. There are no Charger available in the system. Please verify and try again");
				} else
					throw new StationNotFoundException("Station Not Found,Please verify and try again ");
			} else
				throw new InValidIdExcepetion(
						"Invalid Connector ID. The ID provided is not valid. Please check and try again.");

		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());

		} catch (ChargerNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new ChargerNotFoundException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidIdExcepetion(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"update connector using connector id ", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"update connector using connector id ", e.getLocalizedMessage());
		}

	}

	/**
	 * Usage: get all chargers of specific station
	 * 
	 * @param stationId
	 * @return List of chargers
	 */
	@Transactional
	@Override
	public List<Charger> getAllStationChargers(String stationId) {
		logger.info("StationServiceImpl :: getAllStationChargers : execution Started");
		try {
			if (!stationId.isBlank() && stationId != null) {
				Station station = stationRepository
						.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
				if (station != null) {
					List<Charger> charger = station.getChargers();
					List<Charger> finalList = new ArrayList<>();
					if (!charger.isEmpty()) {
						for (Charger list : charger) {
							if (list.isActive() == true) {
								List<Connector> connectors = list.getConnectors();
								List<Connector> connectorList = new ArrayList<>();
								for (Connector list2 : connectors) {
									if (list2.isActive() == true) {
										connectorList.add(list2);
									}
								}
								list.setConnectors(connectorList);
								finalList.add(list);
							}
						}
						logger.info("StationServiceImpl :: getAllStationChargers : execution ended");
						return finalList;
					} else
						return null;
				} else
					throw new StationNotFoundException("No Station Found,Please Check and try again");
			} else
				throw new StationIdNotAcceptableException("Invalid Station ID, Please check and try again.");

		} catch (StationIdNotAcceptableException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get chargers of specific station by station id", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get chargers of specific station by station id", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: get Connector of specific station and specific charger
	 * 
	 * @param chargerId
	 * @return charger object
	 */
	@Transactional
	@Override
	public Charger getCharger(String chargerId) {
		logger.info("StationServiceImpl :: getCharger : execution Started");
		try {
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
							if (charger.isActive() == true) {
								logger.info("StationServiceImpl :: getCharger : execution ended");
								return charger;
							} else
								return null;
						} else
							throw new ChargerNotFoundException(
									"Charger Not Found. There is no Charger available in the Station. Please Check and try again");
					} else
						throw new ChargerNotFoundException(
								"Chargers Not Found. There are no Chargers available in the Station. Please Check and try again");
				} else
					throw new StationNotFoundException(
							"Station Not Found. There is not station available. Please Check and try again");
			} else
				throw new InValidIdExcepetion(
						"Invalid Charger ID. The ID provided is not valid. Please check and try again.");

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (ChargerNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get specific charger using charger id", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get specific charger using charger id", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: get all Connector of specific station and specific charger
	 * 
	 * @param stationId
	 * @return List of connector
	 */
	@Transactional
	@Override
	public List<Connector> getAllStationConnector(String stationId, String chargerId) {
		logger.info("StationServiceImpl :: getAllStationConnector : execution Started");

		try {
			if (!stationId.isBlank() && stationId != null) {
				if (!chargerId.isBlank() && chargerId != null) {
					Station station = stationRepository.findByStationIdAndIsActiveTrue(stationId);
					if (station != null) {
						List<Charger> chargers = station.getChargers();
						if (!chargers.isEmpty()) {

							Charger chargerOne = new Charger();
							for (Charger c : chargers) {
								if (c.getChargerId().equals(chargerId)) {
									chargerOne = c;
									break;
								}
							}
							if (chargerOne != null) {

								List<Connector> connectors = chargerOne.getConnectors();
								if (!connectors.isEmpty()) {
									List<Connector> finalConnector = new ArrayList<>();
									for (int i = 0; i < connectors.size(); i++) {
										if (connectors.get(i).isActive() == true) {
											finalConnector.add(connectors.get(i));
										}
									}
									if (!finalConnector.isEmpty()) {
										logger.info("StationServiceImpl :: getAllStationConnector : execution ended");
										return finalConnector;
									} else
										return null;
								} else
									return null;
							} else
								throw new ChargerNotFoundException(
										"No Charger Found. There is no Charger available. Please Check and try again");
						} else
							throw new ChargerNotFoundException(
									"No Chargers Found. There are no Charger available in the Station. Please Check and try again");
					} else
						throw new StationNotFoundException("No station Found. Please Check and try again");
				} else
					throw new InValidIdExcepetion(
							"Invalid Charger ID. The ID provided is not valid. Please check and try again.");
			} else
				throw new StationIdNotAcceptableException(
						"Invalid Station ID. The ID provided is not valid. Please check and try again.");

		} catch (ChargerNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new ChargerNotFoundException(e.getLocalizedMessage());

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidIdExcepetion(e.getLocalizedMessage());

		} catch (StationIdNotAcceptableException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get all connector of specific station charger by station and charger id",
					e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get all connector of specific station charger by station and charger id", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: get Connector of specific station and specific charger
	 * 
	 * @param connectorId
	 * @return connector object
	 */
	@Transactional
	@Override
	public Connector getConnector(String connectorId) {
		logger.info("StationServiceImpl :: getConnector : execution Started");
		try {
			if (connectorId != null && !connectorId.isBlank()) {
				Station station = stationRepository.findByConnectorId(utility.stringSanitization(connectorId));
				if (station != null && station.isActive()) {

					Connector connector = null;
					boolean foundFlag = false;
					List<Charger> chargerList = station.getChargers();

					if (!chargerList.isEmpty()) {
						for (Charger charger : chargerList) {
							if (charger.isActive() == true) {

								List<Connector> connectorList = charger.getConnectors();
								if (!connectorList.isEmpty()) {

									for (Connector conn : connectorList) {

										if (conn.getConnectorId().equals(utility.stringSanitization(connectorId))) {
											if (conn.isActive() == true) {
												connector = conn;
												;
												foundFlag = true;
												break;
											} else
												throw new InValidIdExcepetion(
														"Connector Not Found. There are no Connector available in the system. Please verify and try again.");
										}
									}
								}
								if (foundFlag)
									break;
							}
						}
						if (foundFlag == true) {
							logger.info("StationServiceImpl :: getConnector : execution ended");
							return connector;
						} else
							throw new InValidDataException("Connector Not Found, please try again");
					} else
						throw new ChargerNotFoundException(
								"No Chargers Found. There are no Charger available in the system. Please verify and try again");
				} else
					throw new StationNotFoundException(
							"Station Not Found. The station with the provided ID does not exist. Please verify and try again ");
			} else
				throw new InValidIdExcepetion(
						"Invalid Connector ID. The ID provided is not valid. Please check and try again.");

		} catch (ChargerNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new ChargerNotFoundException(e.getLocalizedMessage());

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidIdExcepetion(e.getLocalizedMessage());

		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get connector of specific station charger by connector id", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get connector of specific station charger by connector id", e.getLocalizedMessage());
		}

	}

	/**
	 * Usage: remove charger of specific station
	 * 
	 * @param stationId,chargerId
	 * @return boolean (true/false)
	 */
	@Transactional
	@Override
	public boolean removeStationCharger(String stationId, String chargerId) {
		logger.info("StationServiceImpl :: removeStationCharger : execution Started");
		try {

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
							if (stationRepository.save(station) != null) {
								logger.info("StationServiceImpl :: removeStationCharger : execution ended");
								return true;
							} else
								throw new InValidDataException("Deletion Failed: The Charger could not be deleted.");
						} else
							throw new ChargerNotFoundException(
									"No Chargers Found. There are no Charger available in the system. Please verify and try again");
					} else
						throw new StationNotFoundException("Station Not Found, Please verify and try again");
				} else
					throw new InValidIdExcepetion(
							"Invalid Charger ID. The ID provided is not valid. Please check and try again.");
			} else
				throw new StationIdNotAcceptableException(
						"Invalid Station ID. The ID provided is not valid. Please check and try again.");

		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());

		} catch (StationIdNotAcceptableException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (ChargerNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new ChargerNotFoundException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidIdExcepetion(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"remove charger using Station Id, charger id", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"remove charger using Station Id, charger id", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: remove charger using charger id
	 * 
	 * {@link @removeStationCharger} pass the station and charger id
	 * 
	 * @param chargerId
	 * @return boolean (true/false)
	 */
	@Transactional
	@Override
	public boolean removeCharger(String chargerId) {
		logger.info("StationServiceImpl :: removeCharger : execution Started");
		try {
			if (!chargerId.isBlank() && chargerId != null) {
				Station station = stationRepository.findByChargerId(utility.stringSanitization(chargerId));
				String stationId = station.getStationId();
				if (removeStationCharger(stationId, chargerId) == true) {
					logger.info("StationServiceImpl :: removeCharger : execution ended");
					return true;
				} else
					return false;
			} else
				throw new InValidIdExcepetion(
						"Invalid Charger ID. The ID provided is not valid. Please check and try again.");

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"remove charger by charger id and call the remove station charger method",
					e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"remove charger by charger id and call the remove station charger method", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: remove Connector of specific station using connector id
	 * 
	 * @param Connector ID
	 * @return boolean (true/false)
	 */
	@Transactional
	@Override
	public boolean removeConnector(String connectorId) {
		logger.info("StationServiceImpl :: removeConnector : execution Started");
		try {
			if (connectorId != null && !connectorId.isBlank()) {
				Station station = stationRepository.findByConnectorId(utility.stringSanitization(connectorId));
				if (station != null && station.isActive()) {

					boolean foundFlag = false;
					List<Charger> chargerList = station.getChargers();

					if (!chargerList.isEmpty()) {
						for (int i = 0; i < chargerList.size(); i++) {
							Charger charger = chargerList.get(i);
							if (charger.isActive() == true) {

								List<Connector> connectorList = charger.getConnectors();
								if (!connectorList.isEmpty()) {

									for (int j = 0; j < connectorList.size(); j++) {
										Connector conn = connectorList.get(j);

										if (conn.getConnectorId().equals(utility.stringSanitization(connectorId))) {
											if (conn.isActive() == true) {
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
											} else
												throw new InValidIdExcepetion(
														"Connector Not Found. There are no Connector available in the system. Please verify and try again.");
										}
									}
								}
								if (foundFlag)
									break;
							}
						}
						if (foundFlag == true) {
							stationRepository.save(station);
							logger.info("StationServiceImpl :: removeConnector : execution ended");
							return true;
						} else
							throw new InValidDataException("Connector Not Found, please try again");
					} else
						throw new ChargerNotFoundException(
								"No Chargers Found. There are no Charger available in the system. Please verify and try again");
				} else
					throw new StationNotFoundException("Station Not Found, Please verify and try again ");
			} else
				throw new InValidIdExcepetion(
						"Invalid Connector ID. The ID provided is not valid. Please check and try again.");

		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());

		} catch (ChargerNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new ChargerNotFoundException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidIdExcepetion(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"remove connector using connector id ", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"remove connector using connector id ", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: change the charger status active/ inactive
	 * 
	 * @param String stationId, List<String> chargerIdList, String status
	 * @return true/false
	 */
	@Transactional
	@Override
	public boolean updateChargerStatus(String stationId, List<String> chargerIdList, String status) {
		logger.info("StationServiceImpl :: updateChargerStatus : execution Started");
		try {
			if (stationId != null && !stationId.isBlank() && !chargerIdList.isEmpty()
					&& status.equalsIgnoreCase("inactive") || status.equalsIgnoreCase("active")) {

				Station station = stationRepository
						.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
				if (station != null) {
					List<Charger> chargers = station.getChargers();

					if (!chargers.isEmpty()) {
						for (String chargerId : chargerIdList) {

							for (int i = 0; i < chargers.size(); i++) {
								Charger charger = chargers.get(i);
								if (charger.isActive()
										&& charger.getChargerId().equals(utility.stringSanitization(chargerId))) {

									List<Connector> connectors = charger.getConnectors();
									for (int j = 0; j < connectors.size(); j++) {
										Connector connector = connectors.get(j);
										if (status.equalsIgnoreCase("Active")) {
											connector.setConnectorStatus("Available");
										} else
											connector.setConnectorStatus("OutOfOrder");
										charger.setChargerStatus(utility.toTitleCase(status));

										connectors.set(j, connector);
										charger.setConnectors(connectors);
										chargers.set(i, charger);
										station.setChargers(chargers);
									}
								}
							}
						}
						if (stationRepository.save(station) != null) {
							logger.info("StationServiceImpl :: updateChargerStatus : execution ended");
							return true;
						} else
							return false;
					} else
						throw new ChargerNotFoundException(
								"Chargers Not Found. There are no Chargers available in the Station. Please Check and try again");
				} else
					throw new StationNotFoundException("Station Not Found, Please Check and try again");
			} else
				throw new InValidDataException("Invalid ID, please provide valid data and try again");

		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (ChargerNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (Exception e) {

			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"change the charger status to active/ inactive", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"change the charger status to active/ inactive", e.getLocalizedMessage());
		}
	}

	public boolean getChargerStatusByChargerSerialNumber(String chargerSerialNumber) {
		try {

			if (!chargerSerialNumber.isBlank() && chargerSerialNumber != null) {
				Station station = stationRepository
						.findByChargersChargerSerialNumberAndIsActiveTrue(chargerSerialNumber);
				if (station != null) {

					List<Charger> chargers = station.getChargers();

					for (Charger charger : chargers) {
						if (charger.getChargerSerialNumber().equals(chargerSerialNumber)) {
							return charger.isActive();
						}
					}
				}
			} else
				throw new InValidIdExcepetion(
						"Invalid ChargerSerialNumber. The ChargerSerialNumber provided is not valid. Please check and try again.");
		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());
		} catch (Exception e) {

			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(), "Something went wrong",
					e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(), "Something went wrong",
					e.getLocalizedMessage());
		}
		return false;
	}

	@Override
	public Boolean initialVerification(String chargerSerialNumber, ocppVerificationDTO ocppVerificationDTO) {
		if (!chargerSerialNumber.isBlank() && chargerSerialNumber != null) {
			Station station = stationRepository.findByChargersChargerSerialNumberAndIsActiveTrue(chargerSerialNumber);

			if (station != null) {

				List<Charger> chargers = station.getChargers();

				Charger object = new Charger();

				for (Charger charger : chargers) {
					if (charger.getChargerSerialNumber().equals(chargerSerialNumber)) {
						object = charger;
						break;
					} else
						return false;

				}

				boolean flag = false;

//					if(object.getChargePointVendor().equals(ocppVerificationDTO.getChargePointVendor())) {
//						if(object.getChargePointModel().equals(ocppVerificationDTO.getChargePointModel())) 
//							if(object.getChargeBoxSerialNumber().equals(ocppVerificationDTO.getChargeBoxSerialNumber())) 
//								if(object.getMeterType().equals(ocppVerificationDTO.getMeterType()))
//									if(object.getFirmwareVersion().equals(ocppVerificationDTO.getFirmwareVersion()))
//											flag=true;
//					}

				if (object.getChargePointVendor().equalsIgnoreCase(ocppVerificationDTO.getChargePointVendor())) {
					if (object.getChargePointModel().equals(ocppVerificationDTO.getChargePointModel()))
						if (object.getChargeBoxSerialNumber().equals(ocppVerificationDTO.getChargeBoxSerialNumber()))
							if (object.getFirmwareVersion().equals(ocppVerificationDTO.getFirmwareVersion()))
								flag = true;
				}

				if (flag == true)
					return true;
				else
					return flag;
			} else
				throw new InValidIdExcepetion(
						"Invalid ChargerSerialNumber 1. The ChargerSerialNumber provided is not valid. Please check and try again.");
		} else
			throw new InValidIdExcepetion(
					"Invalid ChargerSerialNumber 2. The ChargerSerialNumber provided is not valid. Please check and try again.");

	}

//		@Override
	public String getChargerOCPPProtocol(String chargerSerialNumber) {
		if (!chargerSerialNumber.isBlank() && chargerSerialNumber != null) {

			Station station = stationRepository.findByChargersChargerSerialNumberAndIsActiveTrue(chargerSerialNumber);

			if (station != null) {
				List<Charger> chargers = station.getChargers();

				for (Charger charger : chargers) {
					charger.getChargerSerialNumber().equals(chargerSerialNumber);
					Charger obj = new Charger();
					obj = charger;
					return obj.getChargerOCPPProtocol();
				}
				throw new InValidIdExcepetion(
						"Invalid ChargerSerialNumber. The ChargerSerialNumber provided is not valid. Please check and try again.");
			} else
				throw new InValidIdExcepetion(
						"Invalid ChargerSerialNumber. The ChargerSerialNumber provided is not valid. Please check and try again.");
		}

		return null;
	}

}
