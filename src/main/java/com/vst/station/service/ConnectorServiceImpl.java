package com.vst.station.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vst.station.converter.ConnectorConverter;
import com.vst.station.dto.ConnectorDTO;
import com.vst.station.dto.ConnectorUpdateDTO;
import com.vst.station.dto.MeterValueDTO;
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

@Service
public class ConnectorServiceImpl implements ConnectorServiceInterface {

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private ConnectorConverter connectorConverter;

	IdAndDateGenerator idAndDateGenerator = new IdAndDateGenerator();

	Utility utility = new Utility();

	public static final Logger logger = LogManager.getLogger(ChargerServiceImpl.class);

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
							connector.setConnectorLastAvailableTimeStamp(idAndDateGenerator.dateUpdate());
							connector.setConnectorLastUnavailableTimeStamp(idAndDateGenerator.dateUpdate());
							connector.setConnectorMeterValue(0);
							connector.setConnectorErrorCode(" ");
							connector.setConnectorInfo(" ");
							connector.setConnectorTimeStamp(" ");
							connector.setConnectorMeterRequestTimeStamp(" ");
							connector.setCreatedBy("Admin");
							connector.setCreatedDate(idAndDateGenerator.dateSetter());
							connector.setModifiedBy("Admin");
							connector.setModifiedDate(idAndDateGenerator.dateSetter());
							connector.setActive(true);
							connectors.add(connector);
							charger.setConnectors(connectors);
							List<Connector> list = charger.getConnectors();
							for (int i = 0; i < list.size(); i++) {
								connectorNumber++;
							}
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
	 * Usage: get all Connector of specific station and specific charger
	 * 
	 * @param stationId
	 * @return List of connector
	 */
	@SuppressWarnings("unused")
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
//								throw new InValidIdExcepetion(
//										"Invalid Charger ID. The ID provided is not valid. Please check and try again.");
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

	@Override
	public boolean initialRequest(String chargerSerialNumber, MeterValueDTO meterValueDTO) {
		
		if (chargerSerialNumber != null & !chargerSerialNumber.isBlank()) {
			Station station = stationRepository.findByChargersChargerSerialNumberAndIsActiveTrue(chargerSerialNumber);
			if (station != null) {
				List<Charger> chargers = station.getChargers();
				Charger charger = null;
				int chargerIndex = 0;
				if (!chargers.isEmpty()) {
					for (int i = 0; i < chargers.size(); i++) {
						if (chargers.get(i).getChargerSerialNumber().equals(chargerSerialNumber))
							charger = chargers.get(i);
						chargerIndex = i;
						if (charger.isActive() == false) {
							return false;
						}
					}
				}
				if (charger != null) {
					List<Connector> connectors = charger.getConnectors();
					int connectorIndex = 0;
					Connector connector = null;
					if (!connectors.isEmpty()) {
						for (int i = 0; i < connectors.size(); i++) {
							if (connectors.get(i).getConnectorNumber() == meterValueDTO.getConnectorNumber()) {
								connector = connectors.get(i);
								connectorIndex = i;
								if (charger.isActive() == false) {
									return false;
								}
							}
						}
					}
					if (connector != null) {
						connector.setConnectorMeterValue(meterValueDTO.getMeterValue());
						connector.setConnectorMeterRequestTimeStamp(meterValueDTO.getTimeStamp());
						connector.setConnectorLastUnavailableTimeStamp(meterValueDTO.getConnectorUnitType());
						connectors.set(connectorIndex, connector);
						charger.setConnectors(connectors);
						chargers.set(chargerIndex, charger);
						station.setChargers(chargers);
						stationRepository.save(station);
						return true;
					} else
						return false;
				} else
					return false;
			} else
				return false;
		}
		return false;
	}
}
