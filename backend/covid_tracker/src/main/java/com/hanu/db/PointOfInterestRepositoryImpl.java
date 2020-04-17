package com.hanu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.hanu.base.RepositoryImpl;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.domain.repository.PointOfInterestRepository;
import com.hanu.domain.repository.mapper.PointInterestMapper;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.db.PointOfInterestToDbConverter;

public class PointOfInterestRepositoryImpl  extends RepositoryImpl<PointOfInterest, Integer> implements PointOfInterestRepository{

	@Override
	public List<PointOfInterest> getAll() {
		// TODO Auto-generated method stub
		List<PointOfInterest> list = new ArrayList<PointOfInterest>();
		String query = "SELECT * FROM point_of_interest";
		try {
			ResultSet rs = this.getConnector().connect().executeSelect(query);
			while(rs.next()) {
				list.add(PointInterestMapper.forwardConvertOnce(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public PointOfInterest getById(Integer id) {
		PointOfInterest p = null;
		String query = "SELECT * FROM point_of_interest"
				+  " WHERE id = " + "\'" +id + "\'";
		try {
			ResultSet rs = this.getConnector().connect().executeSelect(query);
			while(rs.next()) {
				p = PointInterestMapper.forwardConvertOnce(rs);	
			}
		} catch (SQLException | InvalidQueryTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}

	@Override
	public int add(PointOfInterest item) {
		String query = new String("INSERT INTO point_of_interest(name, code, continent) VALUES $values")
				.replace("$values",PointOfInterestToDbConverter.forwardConverter(item));
		try {
			return this.getConnector().connect().executeInsert(query);
		} catch (SQLException | InvalidQueryTypeException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int add(List<PointOfInterest> items) {
		ArrayList<String> insertValueStrings = new ArrayList<String>();
		for (PointOfInterest p : items) {
			insertValueStrings.add(PointOfInterestToDbConverter.forwardConverter(p));
		}
		String query = new String("INSERT INTO point_of_interest VALUES $values")
								.replace("$values", String.join(",",insertValueStrings));
		try {
			return this.getConnector().connect().executeInsert(query);
		} catch (SQLException | InvalidQueryTypeException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int remove(PointOfInterest item) {
		String query = "DELETE FROM point_of_interest WHERE id = " + item.getId();
		try {
			return this.getConnector().connect().executeDelete(query);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return 0;
	}

	@Override
	public int remove(List<PointOfInterest> items) {
		ArrayList<String> insertID = new ArrayList<String>();
		for (PointOfInterest p : items) {
			insertID.add("\'" + p.getId() + "\'");
		}
		String query = "DELETE FROM	point_of_interest WHERE id IN ($value)"
						.replace("$value", String.join(",",insertID));
		try {
			return this.getConnector().connect().executeDelete(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int update(PointOfInterest item) {
		String query = "UPDATE point_of_interest SET $value WHERE id = " + "\'" + item.getId() + "\'";
		query.replace("$value", PointOfInterestToDbConverter.forwardUpdateConverter(item));
		try {
			return this.getConnector().connect().executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int update(Iterable<PointOfInterest> items) {
		int result = 0;
		try {
			for (PointOfInterest p : items) {
				result = update(p);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return result;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean exists(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

}
