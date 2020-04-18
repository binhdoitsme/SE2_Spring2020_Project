package com.hanu.domain.usecase;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.hanu.domain.model.PointOfInterest;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.mock.MockPoiRepository;

import org.junit.Test;

public class PointOfInterestUseCaseTest {
    private void setupDependency(Class<?> clazz, Object target, String fieldName, Object impl)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, impl);
    }

    @Test
    public void testAddPointOfInterests() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        AddPointOfInterestUseCase uc = new AddPointOfInterestUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockPoiRepository());
        assertEquals(0, uc.handle(new LinkedList<>()).intValue());
    }

    @Test
    public void testRemoveManyPointOfInterests() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        RemovePointOfInterestUseCase uc = new RemovePointOfInterestUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockPoiRepository());
        assertEquals(new Integer(0), uc.handle(new LinkedList<>()));
    }

    @Test
    public void testUpdateManyPointOfInterests() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        UpdatePointOfInterestUseCase uc = new UpdatePointOfInterestUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockPoiRepository());
        assertEquals(new Integer(0), uc.handle(new LinkedList<>()));
    }

    @Test
    public void testGetAllRecords() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        GetAllPointUseCase uc = new GetAllPointUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockPoiRepository());
        List<PointOfInterest> result = uc.handle(null);
        List<PointOfInterest> expected = new MockPoiRepository().getAll();
        assertEquals(expected, result); // not implemented Record#equals
    }
}