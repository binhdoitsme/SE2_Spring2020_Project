package com.hanu.mock;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;

import com.hanu.domain.model.PointOfInterest;
import com.hanu.domain.repository.PointOfInterestRepository;

public class MockPoiRepository implements PointOfInterestRepository {

    public static final Map<Integer, PointOfInterest> pointOfInterests = new HashMap<Integer, PointOfInterest>() {

        private static final long serialVersionUID = 5403964494677058636L;

        {
            put(1, new PointOfInterest(1, "a", "b", "c"));
            put(2, new PointOfInterest(2, "a", "b", "c"));
            put(3, new PointOfInterest(3, "a", "b", "c"));
            put(4, new PointOfInterest(4, "a", "b", "c"));
            put(5, new PointOfInterest(5, "a", "b", "c"));
        }
    };

    @Override
    public List<PointOfInterest> getAll() {
        return pointOfInterests.values().stream().sorted((r1, r2) -> (new Integer(r1.getId()).compareTo(new Integer(r2.getId())))).collect(Collectors.toList());
    }

    @Override
    public PointOfInterest getById(Integer id) {
        return pointOfInterests.get(id);
    }

    @Override
    public int add(PointOfInterest item) {
        pointOfInterests.put(item.getId(), item);
        return 0;
    }

    @Override
    public int add(List<PointOfInterest> items) {
        items.forEach(itm -> {
            pointOfInterests.put(itm.getId(), itm);
        });
        return 0;
    }

    @Override
    public long count() {
        return pointOfInterests.size();
    }

    @Override
    public boolean exists(Integer id) {
        return pointOfInterests.containsKey(id);
    }

    @Override
    public int remove(Integer id) {

        return 0;
    }

    @Override
    public int remove(List<Integer> ids) {

        return 0;
    }

    @Override
    public int update(PointOfInterest item) {

        return 0;
    }

    @Override
    public int update(List<PointOfInterest> items) {

        return 0;
    }
}