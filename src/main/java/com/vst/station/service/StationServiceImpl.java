package com.vst.station.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
* Service layer to write the business logic and throw the exception. 
*
* Inherited from : {@link : @StationServiceInterface }
*
* @author Nikita Chakole <nikita.chakole@vpel.in>
* @since  21/12/2022
*/

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.constraints.Pattern.Flag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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

/**
 * @exception : @throws : {@link @stationException} if any error occure while
 *              the code.
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
public class StationServiceImpl implements StationServiceInterface {

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private StationConveter stationConveter;

	@Autowired
	MongoTemplate mongoTemplate;

	IdAndDateGenerator idAndDateGenerator = new IdAndDateGenerator();

	Utility utility = new Utility();

	Coordinate coordinates = new Coordinate();

	Instant time = Instant.now();

	public static final Logger logger = LogManager.getLogger(StationServiceImpl.class);

	@Value("${userServiceLink}")
	private String userServiceLink;

	/**
	 * Usage: Add new Station
	 * 
	 * @param stationDTO
	 * @return Boolean(True/False)
	 */
	@Transactional // To avoid rollback on listed exception
	@Override
	public boolean addStation(StationDTO stationDTO) {
		logger.info("StationServiceImpl :: addStation : execution Started");
		try {
			if (stationDTO != null) {
//			configuration.mongoClient();	
				Location location = new Location();
				Station station = stationConveter.dtoToEntity(stationDTO);
				// station.setStationId("STN20230505105447818");
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

				Station existingStation = stationRepository.findByStationId(station.getStationId());

				if (existingStation != null) {
					station.setStationId("STN" + idAndDateGenerator.idGenerator());
				}
				if (stationRepository.save(station) != null) {
					logger.info("StationServiceImpl :: addStation : execution Ended");

					return true;
				} else {
					return false;
				}
			} else {
				throw new InValidDataException("Station Data Cannot Be Empty. Please Check and Try Again");
			}
		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());

		} catch (Exception e) {

			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(), "Add New Station",
					e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(), "Add New Station",
					e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: update station using station id
	 * 
	 * @param stationId, stationUpdateDTO
	 * @return boolean (true/false)
	 */
	@Transactional
	@Override
	public boolean updateStation(String stationId, StationUpdateDTO stationUpdateDTO) {
		logger.info("StationServiceImpl :: updateStation : execution Started");
		try {
			if (!stationId.isBlank() && stationId != null) {
				if (stationUpdateDTO != null) {

					Station stationObj = stationConveter.updateDtoToEntity(stationUpdateDTO);
					Station obj = stationRepository
							.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));

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

						if (stationObj.getStationAddressLineOne() != null
								&& !stationObj.getStationAddressLineOne().isBlank()) {
							obj.setStationAddressLineOne(stationObj.getStationAddressLineOne());
							flag = true;
						}

						if (stationObj.getStationAddressLineTwo() != null
								&& !stationObj.getStationAddressLineTwo().isBlank()) {
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

						if (stationObj.getStationLocationURL() != null
								&& !stationObj.getStationLocationURL().isBlank()) {
							obj.setStationLocationURL(stationObj.getStationLocationURL());
							flag = true;
						}

						if (stationObj.getStationParkingArea() != null
								&& !stationObj.getStationParkingArea().isBlank()) {
							obj.setStationParkingArea(stationObj.getStationParkingArea());
							flag = true;
						}

						if (stationObj.getStationContactNumber() != null
								&& !stationObj.getStationContactNumber().isBlank()) {
							obj.setStationContactNumber(stationObj.getStationContactNumber());
							flag = true;
						}

						if (stationObj.getStationOpeningTime() != null
								&& !stationObj.getStationOpeningTime().isBlank()) {
							obj.setStationOpeningTime(stationObj.getStationOpeningTime());
							flag = true;
						}
						if (stationObj.getStationClosingTime() != null
								&& !stationObj.getStationClosingTime().isBlank()) {
							obj.setStationClosingTime(stationObj.getStationClosingTime());
							flag = true;
						}

						if (stationObj.getChargerNumber() != 0) {
							obj.setChargerNumber(stationObj.getChargerNumber());
							flag = true;
						}

						if (stationObj.getStationParkingType() != null
								&& !stationObj.getStationParkingType().isBlank()) {
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
							if (stationRepository.save(obj) != null) {
								logger.info("StationServiceImpl :: UpdateStation : execution Ended");
								return true;
							} else
								throw new InValidDataException("Station Not Updated.Please Check and Try Again");
						} else
							throw new InValidIdExcepetion("Please Check Enter Data. And try Again");

					} else
						throw new StationNotFoundException("Station Not Aavailable. Please Check and Try Again");

				} else
					throw new InValidDataException("please provide station details");

			} else
				throw new StationIdNotAcceptableException(
						"Invalid ID. The ID provided is not valid. Please check and try again.");

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
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"update station using Station Id", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"update station using Station Id", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: remove Station using station id
	 * 
	 * @param stationId
	 * @return boolean (true/false)
	 */
	@Transactional
	@Override
	public boolean removeStation(String stationId) {
		logger.info("StationServiceImpl :: removeStation : execution Started");
		try {

			if (!stationId.isBlank() && stationId != null) {
				Station obj = stationRepository.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
				if (obj != null) {
					obj.setActive(false);
					if (stationRepository.save(obj) != null) {
						logger.info("StationServiceImpl :: removeStation : execution ended");
						return true;
					} else
						throw new InValidDataException("station is not removed, please try again.");

				} else
					throw new StationNotFoundException(
							"Station Not Found. The station with the provided ID does not exist. Please verify and try again");
			} else
				throw new StationIdNotAcceptableException(
						"Invalid Station ID. The ID provided is not valid. Please check and try again.");

		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());

		} catch (StationIdNotAcceptableException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"remove station using station id", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"remove station using station id", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: show all active station
	 * 
	 * @return List of station
	 */
	@Transactional
	@Override
	public List<Station> showAll() {
		logger.info("StationServiceImpl :: showAll : execution Started");
		try {
			List<Station> list = stationRepository.findAllByIsActiveTrue();
			if (!list.isEmpty()) {
				logger.info("StationServiceImpl :: showAll : execution ended");
				return list;
			} else
				throw new StationNotFoundException("There is no stations. Please check and try agian");

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"show all the active station", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"show all the active station", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: show a specific station using station id
	 * 
	 * @param stationId
	 * @return station object
	 */
	@Transactional
	@Override
	public Station show(String stationId) {
		logger.info("StationServiceImpl :: show : execution Started");
		try {
			if (!stationId.trim().isBlank() && stationId != null) {
				Station station = stationRepository
						.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
				if (station != null) {
					Station finalStation = station;
					List<Charger> charger = station.getChargers();
					List<Charger> finalList = new ArrayList<>();

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
						finalStation.setChargers(finalList);
					}
					logger.info("StationServiceImpl :: show : execution ended");
					return finalStation;
				} else
					throw new StationNotFoundException("Station Not Aavailable. Please Check and Try Again");
			} else
				throw new StationIdNotAcceptableException(
						"Invalid Station ID. The ID provided is not valid. Please check and try again.");

		} catch (StationIdNotAcceptableException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"show station using station id", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"show station using station id", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: get stations by host id
	 * 
	 * @param hostId
	 * @return list of stations
	 */
	@Transactional
	@Override
	public List<Station> getByHostId(String stationHostId) {
		logger.info("StationServiceImpl :: getByHostId : execution Started");
		try {
			if (!stationHostId.trim().isBlank() && stationHostId != null) {
				List<Station> list = stationRepository
						.findByStationHostIdAndIsActiveTrue(utility.stringSanitization(stationHostId));
				if (!list.isEmpty()) {
					logger.info("StationServiceImpl :: getByHostId : execution ended");
					return list;
				} else
					throw new StationNotFoundException("station not avavilable, please check and try again ");
			} else
				throw new StationIdNotAcceptableException("Host Details are not found.Please check and try again.");

		} catch (StationIdNotAcceptableException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"Get List Of Station By Host Id", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"Get List Of Station By Host Id", e.getLocalizedMessage());
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

	/**
	 * Usage: get required station details
	 * 
	 * @return List of stations
	 */
	@Transactional
	@Override
	public List<StationDTO1> getRequiredStationData() {
		logger.info("StationServiceImpl :: getRequiredStationData : execution Started");
		try {
			List<Station> stations = stationRepository.findAllByIsActiveTrue();
			List<StationDTO1> dto1 = new ArrayList<>();
			if (!stations.isEmpty()) {

				for (Station s : stations) {
					dto1.add(stationConveter.entitytoStationDTO1(s));
				}
				logger.info("StationServiceImpl :: getRequiredStationData : execution ended");
				return dto1;
			} else
				return dto1;

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get required details of station", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get required details of station", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: get stations by keyword fron search bar
	 * 
	 * @param query (keyword)
	 * @return List of stations
	 */
	@Transactional
	@Override
	public List<StationDTO1> stationforApplication(String query) {
		logger.info("StationServiceImpl :: stationforApplication : execution Started");
		try {
			List<StationDTO1> dtos = new ArrayList<>();
			if (!query.isBlank()) {

				List<Station> list = stationRepository
						.findByStationNameContainingIgnoreCaseAndIsActiveTrueOrStationAreaContainingIgnoreCaseAndIsActiveTrueOrStationZipCodeContainingIgnoreCaseAndIsActiveTrueOrStationCityContainingIgnoreCaseAndIsActiveTrueOrStationStatusContainingIgnoreCaseAndIsActiveTrue(
								query, query, query, query, query);

				if (!list.isEmpty()) {

					for (Station station : list) {
						dtos.add(stationConveter.entitytoStationDTO1(station));
					}
					logger.info("StationServiceImpl :: stationforApplication : execution ended");
					return dtos;
				} else
					return dtos;
			} else
				return dtos;
		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get station by keyword from serch bar", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get station by keyword from serch bar", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: get station details
	 * 
	 * @param station id
	 * @return station object
	 */
	@Transactional
	@Override
	public Station getStation(String stationId) {
		logger.info("StationServiceImpl :: getStation : execution Started");
		try {
			if (!stationId.isBlank() && stationId != null) {
				Station station = stationRepository.findStationByStationId(utility.stringSanitization(stationId));
				if (station != null) {
					logger.info("StationServiceImpl :: getStation : execution ended");
					return station;
				} else
					throw new StationNotFoundException("Station Not Found");
			} else
				throw new InValidIdExcepetion(
						"Invalid Connector ID. The ID provided is not valid. Please check and try again.");

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"update station using station id ", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"update station using station id ", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: get list of station in specific radius of map
	 * 
	 * @param latitute, longitute, max distance
	 * @return station list
	 */
	@Transactional
	@Override
	public List<StationDTO1> getAllStationforRadius(double longitude, double latitude, double maxDistance) {
		logger.info("StationServiceImpl :: getAllStationforRadius : execution Started");
		try {
			if (latitude != 0 && longitude != 0 && maxDistance != 0) {
				double longt = utility.sanitizeCoordinate(longitude);
				double lat = utility.sanitizeCoordinate(latitude);
				double minDistance = 10;
				String type = "Point";
				List<Station> stations = stationRepository.findByGeoLocation(type, longt, lat, maxDistance,
						minDistance);
				List<StationDTO1> finalList = new ArrayList<>();
				if (!stations.isEmpty()) {

					for (Station s : stations) {
						if (s.isActive() == true) {
							finalList.add(stationConveter.entitytoStationDTO1(s));
						}
					}
					logger.info("StationServiceImpl :: getAllStationforRadius : execution ended");
					return finalList;
				} else
					return finalList;
			} else
				throw new InValidIdExcepetion("Provided Correct Longitude, Latitude Please check and try again.");

		} catch (InValidIdExcepetion e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get stations of specific radius in map ", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get stations of specific radius in map ", e.getLocalizedMessage());
		}
	}

	/**
	 * Usage: to find station by name and address
	 * 
	 * @param station list
	 * @return station list
	 */
	@Transactional
	@Override
	public StationFindDTO getNameAndAddressStation(String stationId) {
		logger.info("StationServiceImpl :: getNameAndAddressStation : execution Started");
		try {
			Station station = stationRepository.findByStationId(utility.stringSanitization(stationId));
			logger.info("StationServiceImpl :: getNameAndAddressStation : execution ended");
			return stationConveter.entitytoStationFind(station);

		} catch (Exception e) {
			logger.error(new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get name and address of a specific station", e.getLocalizedMessage()));

			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get name and address of specific station", e.getLocalizedMessage());
		}
	}

	@Override
	public boolean addUserAccess(String stationId, String type, List<String> list) {

		if (!type.isBlank() && type != null && !list.isEmpty()) {

			Station station = stationRepository.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
			if (station != null) {

				List<String> accessList = station.getUserAcceessList();
				switch (type.toLowerCase()) {
				case "contactno":

					for (String id : list) {
						boolean flag = false;
						String userId = null;
						try {
							URL url = new URL(userServiceLink + "/manageUser/getByContactNo?userContactNo=" + id);
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							System.out.println(connection);
							int responseCode = connection.getResponseCode();
							if (responseCode == HttpURLConnection.HTTP_OK) {
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(connection.getInputStream()));
								userId = reader.readLine();
								reader.close();
							} else
								logger.error(id + "Not Found ");

						} catch (Exception e) {
							throw new InValidDataException(e.getLocalizedMessage());
						}

						if (userId != null) {
							if (!accessList.isEmpty()) {

								for (String access : accessList) {
									if (access.equals(userId)) {
										flag = true;
										break;
									}
								}
								if (flag == false) {
									accessList.add(userId);
									station.setUserAcceessList(accessList);
								}
							} else {
								accessList.add(userId);
								station.setUserAcceessList(accessList);
							}
						}
					}
					stationRepository.save(station);
					return true;

				case "emailid":

					for (String id : list) {
						boolean flag = false;
						String userId = null;
						;
						try {
							URL url = new URL(userServiceLink + "/manageUser/getByEmail?userEmail=" + id);
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							System.out.println(connection);
							int responseCode = connection.getResponseCode();
							if (responseCode == HttpURLConnection.HTTP_OK) {
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(connection.getInputStream()));
								userId = reader.readLine();
								reader.close();
							} else
								logger.error(id + "Not Found ");

						} catch (Exception e) {
							throw new InValidDataException(e.getLocalizedMessage());
						}

						if (userId != null) {
							if (!accessList.isEmpty()) {
								for (String access : accessList) {
									if (access.equals(userId)) {
										flag = true;
										break;
									}
								}
								if (flag == false) {
									accessList.add(userId);
									station.setUserAcceessList(accessList);
								}
							} else {
								accessList.add(userId);
								station.setUserAcceessList(accessList);
							}
						}
					}
					System.out.println(station.getUserAcceessList());
					stationRepository.save(station);
					return true;

				default:
					throw new InValidDataException("please provide correct details and try again");
				}

			} else
				throw new StationNotFoundException("user not found, please check the details and try again");
		} else
			throw new InValidDataException("Please provide details and try again");
	}

	@Override
	public List<String> GetUserAccessList(String stationId) {
		try {
			logger.info("StationServiceImpl :: GetUserAccessList : execution started");
			if (stationId != null && !stationId.isBlank()) {
				Station station = stationRepository
						.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
				if (station != null) {

					List<String> UserAccessList = station.getUserAcceessList();
					if (!UserAccessList.isEmpty()) {
						logger.info("StationServiceImpl :: GetUserAccessList : execution ended");
						return UserAccessList;
					} else {
						logger.info("StationServiceImpl :: GetUserAccessList : execution ended");
						return UserAccessList;
					}
				} else
					throw new StationNotFoundException("Station Not found, Please check and try again");
			} else
				throw new StationIdNotAcceptableException("Invalid ID, Please check details and try again");

		} catch (StationIdNotAcceptableException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {
			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get user access list of specific station ", e.getLocalizedMessage());
		}
	}

	@Override
	public boolean addUserAccessList(String stationId, List<String> userIds) {
		try {
			logger.info("StationServiceImpl :: addUserAccessList : execution started");
			if (stationId != null && !stationId.isBlank()) {
				if (!userIds.isEmpty()) {
					Station station = stationRepository
							.findByStationIdAndIsActiveTrue(utility.stringSanitization(stationId));
					if (station != null) {

						List<String> UserAccessList = station.getUserAcceessList();

						System.out.println(userIds);
						System.out.println(UserAccessList);
						for (String userId : userIds) {
							boolean flag = false;
							System.out.println("881");
							for (String userAccess : UserAccessList) {

								if (userId.equals(userAccess)) {
									flag = true;
									System.out.println("886");
									break;
								}
							}
							System.out.println(flag);
							if (flag == false) {
								UserAccessList.add(userId);
								System.out.println(UserAccessList);
								station.setUserAcceessList(UserAccessList);
								System.out.println(station.getUserAcceessList());
							}
						}
						if (stationRepository.save(station) != null) {
							logger.info("StationServiceImpl :: addUserAccessList : execution ended");
							return true;
						} else {
							logger.info("StationServiceImpl :: addUserAccessList : execution ended");
							return false;
						}
					} else
						throw new StationNotFoundException("Station Not found, Please check and try again");
				} else
					throw new InValidDataException("Please add at least one user");
			} else
				throw new StationIdNotAcceptableException("Invalid ID, Please check details and try again");

		} catch (StationIdNotAcceptableException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationIdNotAcceptableException(e.getLocalizedMessage());

		} catch (StationNotFoundException e) {
			logger.error(e.getLocalizedMessage());
			throw new StationNotFoundException(e.getLocalizedMessage());

		} catch (InValidDataException e) {
			logger.error(e.getLocalizedMessage());
			throw new InValidDataException(e.getLocalizedMessage());

		} catch (Exception e) {
			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"add user access list in specific station ", e.getLocalizedMessage());
		}

	}

	@Override
	public boolean getIsUserPresentInRestrictionList(String stationId, String userId) {
		try {
			logger.info("StationServiceImpl :: getIsUserPresentInRestrictionList : execution Started");
			if (stationId != null && !stationId.isBlank()) {
				if (userId != null && !userId.isBlank()) {

					Station station = stationRepository.findByStationIdAndIsActiveTrue(stationId);
					if (station != null) {

						List<String> userAccessList = station.getUserAcceessList();
						if (!userAccessList.isEmpty()) {
							boolean flag = false;
							for (String id : userAccessList) {
								if (userId.equals(id)) {
									flag = true;
									break;
								}
							}
							if (flag == true) {
								logger.info(
										"StationServiceImpl :: getIsUserPresentInRestrictionList : execution ended");
								return true;
							} else {
								logger.info(
										"StationServiceImpl :: getIsUserPresentInRestrictionList : execution ended");
								return false;
							}
						} else {
							logger.info("StationServiceImpl :: getIsUserPresentInRestrictionList : execution ended");
							return false;
						}
					} else
						throw new StationNotFoundException("Station Not found, Please check and try again");
				} else
					throw new InValidIdExcepetion("Invalid user Id, Please Check Details and Try Again");
			} else
				throw new StationIdNotAcceptableException("Invalid Station ID, Please check details and try again");

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
			throw new StationException("STN001", "ManageStation", e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(),
					"get user access of specific station ", e.getLocalizedMessage());
		}
	}

	public boolean deleteUserFromRestrictionList(String stationId, String userId) {
		if (stationId != null && !stationId.isBlank()) {
			if (userId != null && !userId.isBlank()) {

				Station station = stationRepository.findByStationIdAndIsActiveTrue(stationId);
				if (station != null) {
					List<String> userAccessList = station.getUserAcceessList();
					Boolean flag = false;
					for (int i = 0; i < userAccessList.size(); i++) {
						String id = userAccessList.get(i);
						if (userId.equals(id)) {
							flag = true;
						}
						if (flag == true) {
							userAccessList.remove(i);
							station.setUserAcceessList(userAccessList);
							break;
						}
					}
					if (flag == false) {
						return false;
					} else if (flag == true) {
						stationRepository.save(station);
						return true;
					} else
						return false;
				} else
					throw new StationNotFoundException("Station Not Found");
			} else
				throw new InValidIdExcepetion("Invalid Id, Please Check Details and Try Again");
		} else
			throw new StationIdNotAcceptableException("Invalid Id, Please Check Details and Try Again");
	}
}
