package com.vst.station.model;

import java.util.Random;

public class Coordinate {

//	private static final double MIN_LATITUDE = 7.9; // Minimum latitude in Indian continent
//	private static final double MAX_LATITUDE = 37.6; // Maximum latitude in Indian continent
//	private static final double MIN_LONGITUDE = 67.0; // Minimum longitude in Indian continent
//	private static final double MAX_LONGITUDE = 97.4; // Maximum longitude in Indian continent

	// Generates a random latitude within the Indian continent
	public double generateLatitude(int index) {
//		Random random = new Random();
//		double latitude = MIN_LATITUDE + (MAX_LATITUDE - MIN_LATITUDE) * random.nextDouble();

		double latitude[] = { 18.5369, 18.4796, 18.5166, 18.5461, 18.4958, 18.4645, 18.5435, 18.4925, 18.4938, 18.5231,
				18.5162, 18.4817, 18.5085, 18.5127, 18.5505, 18.4753, 18.5391, 18.4937, 18.5429, 18.4757, 18.5492,
				18.4839, 18.5245, 18.5017, 18.5311, 18.4697, 18.5382, 18.5224, 18.5171, 18.5462, 18.5248, 18.4907,
				18.5309, 18.4965, 18.4988, 18.4902, 18.4936, 18.5125, 18.4813, 18.5221, 18.5109, 18.4952, 18.5366,
				18.5256, 18.5366, 18.5269, 18.5407, 18.4907, 18.4939, 18.5347, 18.5088, 18.4792, 18.4911, 18.5307,
				18.5218, 18.5493, 18.5029, 18.5162, 18.4986, 18.5236, 18.5432, 18.5362, 18.5418, 18.5108, 18.5067,
				18.5039, 18.5428, 18.5444, 18.5335, 18.5444, 18.5034, 18.5425, 18.5185, 18.5181, 18.5187, 18.5116,
				18.5349, 18.5173, 18.5407, 18.4991, 18.5182, 18.5269, 18.4923, 18.5303, 18.5371, 18.5237, 18.5343,
				18.5127, 18.5276, 18.4792, 18.5374, 18.4978, 18.4926, 18.4966, 18.5421, 18.5429, 18.5303, 18.5231,
				18.5152, 18.5257, 18.5418, 18.5331, 18.5006, 18.5121, 18.5241, 18.5352, 18.4964, 18.5404, 18.5288,
				18.4991, 18.5191, 18.5102, 18.5327, 18.5471, 18.5023, 18.4865, 18.5391, 18.5031, 18.5269, 18.5181,
				18.5216, 18.531 };
		double value = latitude[index];

		return value;
	}

	// Generates a random longitude within the Indian continent
	public double generateLongitude(int index) {
//		Random random = new Random();
//		double longitude = MIN_LONGITUDE + (MAX_LONGITUDE - MIN_LONGITUDE) * random.nextDouble();
//		return longitude;

		double longitude[] = { 73.8827, 73.8502, 73.9119, 73.7883, 73.8347, 73.8092, 73.8475, 73.8962, 73.8377, 73.8314,
				73.8608, 73.8344, 73.8985, 73.8335, 73.8508, 73.8733, 73.9016, 73.8576, 73.8489, 73.8536, 73.8793,
				73.8403, 73.8866, 73.8559, 73.8602, 73.8621, 73.8484, 73.8784, 73.8349, 73.8259, 73.8071, 73.8884,
				73.8894, 73.8605, 73.8525, 73.8473, 73.8388, 73.8407, 73.8665, 73.8444, 73.8708, 73.8357, 73.8279,
				73.8561, 73.9023, 73.8621, 73.8842, 73.8425, 73.8373, 73.8435, 73.8344, 73.8718, 73.8612, 73.8329,
				73.8396, 73.8607, 73.8718, 73.8833, 73.8749, 73.8692, 73.8691, 73.8682, 73.8989, 73.8431, 73.8366,
				73.8682, 73.8664, 73.8622, 73.8785, 73.8447, 73.8385, 73.8825, 73.8938, 73.8225, 73.8282, 73.8599,
				73.8718, 73.8783, 73.8259, 73.8571, 73.8588, 73.8491, 73.8843, 73.8339, 73.8742, 73.8574, 73.8909,
				73.8624, 73.8668, 73.8618, 73.8676, 73.8446, 73.8499, 73.8535, 73.8556, 73.8889, 73.8631, 73.8498,
				73.8568, 73.8731, 73.8759, 73.8577, 73.8444, 73.8457, 73.8814, 73.8749, 73.8491, 73.8457, 73.8329,
				73.8397, 73.8818, 73.8398, 73.8441, 73.8779, 73.8663, 73.8309, 73.8828, 73.8753, 73.8682, 73.8371,
				73.8327, 73.8588 };

		double value = longitude[index];
		return value;

	}

	public double generatelatitude() {
		Random random = new Random();
		double latitude = 8.4 + (37.6 - 8.4) * random.nextDouble();
		return latitude;
	}

	public double generatelongitude() {
		Random random = new Random();
		double longitude = 68.7 + (97.25 - 68.7) * random.nextDouble();
		return longitude;
	}

//	import java.util.Random;
//
//	public class MaharashtraLocationGenerator {
//
//	    public static void main(String[] args) {
//	        int numLocations = 10; // Number of locations to generate
//	        double[] latitudes = generateLatitudes(numLocations, "Maharashtra");
//	        double[] longitudes = generateLongitudes(numLocations, "Maharashtra");
//
//	        // Print the generated locations
//	        System.out.println("Generated Locations in Maharashtra:");
//	        for (int i = 0; i < numLocations; i++) {
//	            System.out.println("Location " + (i + 1) + ": Latitude = " + latitudes[i] + ", Longitude = " + longitudes[i]);
//	        }
//	    }
//
//	    public static double[] generateLatitudes(int numLocations, String region) {
//	        double[] latitudes = new double[numLocations];
//	        Random random = new Random();
//	        for (int i = 0; i < numLocations; i++) {
//	            // Generate latitude between the specified range for Maharashtra
//	            if (region.equalsIgnoreCase("Maharashtra")) {
//	                latitudes[i] = random.nextDouble() * (22.0 - 20.0) + 20.0; // Latitude range: 20 to 22 degrees
//	            } else {
//	                // Throw an exception for invalid region input
//	                throw new IllegalArgumentException("Invalid region specified. Please provide 'Maharashtra'.");
//	            }
//	        }
//	        return latitudes;
//	    }
//
//	    public static double[] generateLongitudes(int numLocations, String region) {
//	        double[] longitudes = new double[numLocations];
//	        Random random = new Random();
//	        for (int i = 0; i < numLocations; i++) {
//	            // Generate longitude between the specified range for Maharashtra
//	            if (region.equalsIgnoreCase("Maharashtra")) {
//	                longitudes[i] = random.nextDouble() * (77.0 - 72.0) + 72.0; // Longitude range: 72 to 77 degrees
//	            } else {
//	                // Throw an exception for invalid region input
//	                throw new IllegalArgumentException("Invalid region specified. Please provide 'Maharashtra'.");
//	            }
//	        }
//	        return longitudes;
//	    }
//	}

}
