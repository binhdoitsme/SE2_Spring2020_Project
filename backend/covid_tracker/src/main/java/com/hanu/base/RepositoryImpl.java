package com.hanu.base;

import java.io.Serializable;

import com.hanu.util.db.DbConnector;
import com.hanu.util.di.Inject;

public abstract class RepositoryImpl<T, ID extends Serializable> 
    implements Repository<T, ID> {
    
    @Inject
    private DbConnector connector;

    public RepositoryImpl() { }

    protected DbConnector getConnector() {
        return connector;
    }
}