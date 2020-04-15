package com.hanu.base;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, ID extends Serializable> {
    List<T> getAll();
    T getById(ID id);
    int add(T item);
    int add(List<T> items);
    int remove(ID id);
    int remove(List<ID> ids);
    int update(T item);
    int update(List<T> items);
    long count();
    boolean exists(ID id);
}