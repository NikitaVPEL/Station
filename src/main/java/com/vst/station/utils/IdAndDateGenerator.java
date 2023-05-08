package com.vst.station.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IdAndDateGenerator {
	
	
	public String idGenerator() {

		Date dNow = new Date();
		Random random = new Random();

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		String idGen = dateFormat1.format(dNow);
		
//		double idGen=dateFormat1.format(dNow);

		return idGen;
	}

	public Date dateSetter() {
		Date dNow = new Date();
		return dNow;
	}

}
