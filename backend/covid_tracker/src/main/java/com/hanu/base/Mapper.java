package com.hanu.base;

import java.sql.ResultSet;
import java.util.function.Function;

public abstract class Mapper<T> extends Converter<ResultSet, T> {
    public Mapper(Function<ResultSet, T> forwardConverter) {
        super(forwardConverter, null);
    }

    @Override
    public ResultSet backwardConvert(T source) {
        throw new UnsupportedOperationException();
    }
}