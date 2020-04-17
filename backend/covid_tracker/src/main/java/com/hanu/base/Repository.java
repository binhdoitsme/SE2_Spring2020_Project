package com.hanu.base;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, ID extends Serializable> {
    List<T> getAll();
    T getById(ID id);
    int add(T item);
    int add(List<T> items);
    int remove(T item);
    int remove(List<T> items);
    int update(T item);
    int update(Iterable<T> items);
    long count();
    boolean exists(ID id);
}