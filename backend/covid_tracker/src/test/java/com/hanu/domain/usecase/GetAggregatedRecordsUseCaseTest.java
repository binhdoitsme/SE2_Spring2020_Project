package com.hanu.domain.usecase;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.sql.SQLException;

import com.hanu.db.util.AggregationType;
import com.hanu.domain.repository.RecordRepository;
import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.mock.MockRecordRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GetAggregatedRecordsUseCaseTest {

    @Test
    public void testHandle() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
            SecurityException, SQLException, InvalidQueryTypeException {
        GetAggregatedRecordsUseCase uc = new GetAggregatedRecordsUseCase();
        RecordRepository repo = new MockRecordRepository();
        Field f = uc.getClass().getDeclaredField("repository");
        f.setAccessible(true);
        f.set(uc, repo);
        AggregationType type = new AggregationType();
        assertEquals(uc.handle(type), repo.getAggregatedRecords(type));
    }
}