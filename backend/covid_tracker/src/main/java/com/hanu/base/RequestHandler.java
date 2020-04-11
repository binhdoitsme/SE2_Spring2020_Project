package com.hanu.base;

public interface RequestHandler<TRequest, TResponse> {
    TResponse handle(TRequest input) throws Exception;
}
