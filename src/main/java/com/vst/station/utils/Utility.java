package com.vst.station.utils;

public class Utility {

	public String stringSanitization(String inputId) {
		String sanitizedId = inputId.replaceAll("[^a-zA-Z0-9]", "");
		return sanitizedId;
	}

	public String toTitleCase(String input) {
		StringBuilder titleCase = new StringBuilder(input.length());
		boolean nextTitleCase = true;
		for (char c : input.toCharArray()) {
			if (Character.isSpaceChar(c)) {
				nextTitleCase = true;
			} else if (nextTitleCase) {
				c = Character.toTitleCase(c);
				nextTitleCase = false;
			}
			titleCase.append(c);
		}
		return titleCase.toString();
	}

//	public String locationSanitization(String input) {
//		String pattern = "\\d{2}\\.\\d+";
//		return pattern;
//	}
	
	public double sanitizeCoordinate(double coordinate) { 
		if (coordinate < -180.0 || coordinate > 180.0) { 
			throw new IllegalArgumentException("Invalid coordinate value: " + coordinate);
		}
		
	// Round the coordinate to 6 decimal places to prevent precision issues 
		double roundedCoordinate = Math.round(coordinate * 1e12) / 1e12; 
		return roundedCoordinate; 
	}
	
	
}


