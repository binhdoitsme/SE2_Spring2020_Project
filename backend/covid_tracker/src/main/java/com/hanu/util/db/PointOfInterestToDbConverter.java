package com.hanu.util.db;

import java.util.function.Function;

import com.hanu.base.Converter;
import com.hanu.domain.model.PointOfInterest;

public class PointOfInterestToDbConverter extends Converter<PointOfInterest, String>{

	public PointOfInterestToDbConverter(Function<PointOfInterest, String> forwardConverter,
			Function<String, PointOfInterest> backwardConverter) {
		super(forwardConverter, null);
	}

	public static String forwardConverter(PointOfInterest p) {
		return new String("($name,$code,$continent)")
				.replace("$name", "\'" + p.getName() + "\'")
				.replace("$code", "" + p.getCode() +  "\'")
				.replace("$continent", "\'" + p.getContinent() + "\'");
	}
	public static String forwardUpdateConverter(PointOfInterest p) {
		return new String("name = $name, code = $code, continent = $continent")
				.replace("$name","\'" + p.getName() + "\'")
				.replace("$code", "" + p.getCode() +  "\'")
				.replace("$continent", "\'" + p.getContinent() + "\'");
	}
}
