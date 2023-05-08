package com.vst.station.exception;

public class StationNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7136395761401538992L;

	public StationNotFoundException(String message) {
		super(message);
	}
}

