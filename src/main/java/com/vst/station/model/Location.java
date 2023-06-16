
package com.vst.station.model;

//import org.bson.codecs.pojo.annotations.BsonProperty;
//import org.bson.types.ObjectId;

//import org.bson.codecs.pojo.annotations.BsonProperty;
//import org.bson.types.ObjectId;
//import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Location {

	private String type = "Point";
	private double[] coordinates = new double[2];

}
