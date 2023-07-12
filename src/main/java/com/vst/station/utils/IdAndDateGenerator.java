package com.vst.station.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdAndDateGenerator {
	
	Date dNow = new Date();
	
	SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	public String idGenerator() {
		
		String idGen = dateFormat1.format(dNow);
				
		return idGen;
	}

	public Date dateSetter() {
		
		return dNow;
	}
	
	public String dateUpdate() {
		
		String dateSetter = dateFormat1.format(dNow);
		
		return dateSetter;
	}

}
