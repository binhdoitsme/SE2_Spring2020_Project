package com.hanu.domain.usecase;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.hanu.db.util.AggregationType;
import com.hanu.domain.model.Record;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.mock.MockPoiRepository;
import com.hanu.mock.MockRecordRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RecordUseCaseTest {

    private void setupDependency(Class<?> clazz, Object target, String fieldName, Object impl)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, impl);
    }

    @Test
    public void testAddManyRecords() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        AddManyRecordsUseCase uc = new AddManyRecordsUseCase();
        setupDependency(uc.getClass(), uc, "recordRepository", new MockRecordRepository());
        setupDependency(uc.getClass(), uc, "poiRepository", new MockPoiRepository());
        assertEquals(0, uc.handle(new HashMap<>()).intValue());
    }

    @Test
    public void testAddRecords() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        AddRecordUseCase uc = new AddRecordUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockRecordRepository());
        assertEquals(0, uc.handle(new LinkedList<>()).intValue());
    }

    @Test
    public void testAggregateRecords() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        GetAggregatedRecordsUseCase uc = new GetAggregatedRecordsUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockRecordRepository());
        List<Record> result = uc.handle(new AggregationType());
        List<Record> expected = new MockRecordRepository().getAggregatedRecords(new AggregationType());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetRecordsByPoiId() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        GetAllByPoiIdUseCase uc = new GetAllByPoiIdUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockRecordRepository());
        List<Record> result = uc.handle(0);
        List<Record> expected = new MockRecordRepository().getByPoiID(0);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetAllRecords() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        GetAllRecordUseCase uc = new GetAllRecordUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockRecordRepository());
        List<Record> result = uc.handle(null);
        List<Record> expected = new MockRecordRepository().getAll();
        assertEquals(expected.toString(), result.toString()); // not implemented Record#equals
    }

    @Test
    public void testGetRecordsByContinent() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        GetRecordByContinentUseCase uc = new GetRecordByContinentUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockRecordRepository());
        String[] values = { "", "null" };
        for (String s : values) {
            List<Record> result = uc.handle(s);
            List<Record> expected = new MockRecordRepository().getRecordByContinent(s);
            assertEquals(expected.toString(), result.toString()); // not implemented Record#equals
        }
    }

    @Test
    public void testRemoveManyRecords() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        RemoveManyRecordUseCase uc = new RemoveManyRecordUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockRecordRepository());
        assertEquals(new Integer(0), uc.handle(new LinkedList<>()));
    }

    @Test
    public void testUpdateManyRecords() throws SQLException, InvalidQueryTypeException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        UpdateManyRecordUseCase uc = new UpdateManyRecordUseCase();
        setupDependency(uc.getClass(), uc, "repository", new MockRecordRepository());
        assertEquals(new Integer(0), uc.handle(new LinkedList<>()));
    }
}