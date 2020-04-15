package com.hanu.base;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, ID extends Serializable> {
    List<T> getAll();
    T getById(ID id);
    void add(T item);
    void add(Iterable<T> items);
    void remove(ID id);
    int remove(Iterable<ID> ids);
    T update(T item);
    Iterable<T> update(Iterable<T> items);
    long count();
    boolean exists(ID id);
}