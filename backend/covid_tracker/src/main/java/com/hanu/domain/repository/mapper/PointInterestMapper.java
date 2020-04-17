package com.hanu.domain.repository.mapper;

import java.sql.ResultSet;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hanu.base.Mapper;
import com.hanu.domain.model.PointOfInterest;

public class PointInterestMapper extends Mapper<PointOfInterest> {
	private static final Logger logger = LoggerFactory.getLogger(PointInterestMapper.class);
	public PointInterestMapper(Function<ResultSet, PointOfInterest> forwardConverter) {
		super(PointInterestMapper::forwardConvertOnce);
		// TODO Auto-generated constructor stub
	}

	public static PointOfInterest forwardConvertOnce(ResultSet source) {
    	PointOfInterest p = null;
        try {
        		p = new PointOfInterest(source.getInt("id"), 
            		source.getString("name"), 
            		source.getString("code"), 
            		source.getString("continent"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return p;
    }
}
