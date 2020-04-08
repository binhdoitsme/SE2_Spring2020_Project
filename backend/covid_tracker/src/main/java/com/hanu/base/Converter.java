package com.hanu.base;

import java.util.function.Function;

public abstract class Converter<S, T> {
    private Function<S, T> forwardConverter;
    private Function<T, S> backwardConverter;

    public Converter(Function<S, T> forwardConverter, Function<T, S> backwardConverter) {
        this.forwardConverter = forwardConverter;
        this.backwardConverter = backwardConverter;
    }

    public T forwardConvert(S source) {
        return forwardConverter.apply(source);
    }

    public S backwardConvert(T source) {
        return backwardConverter.apply(source);
    }
}
