package com.hanu.base;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, ID extends Serializable> {
    List<T> getAll();
    T getById(ID id);
    void add(T item);
    int add(Iterable<T> items);
    void remove(T item);
    int remove(Iterable<T> items);
    T update(T item);
    Iterable<T> update(Iterable<T> items);
    long count();
    boolean exists(ID id);
}