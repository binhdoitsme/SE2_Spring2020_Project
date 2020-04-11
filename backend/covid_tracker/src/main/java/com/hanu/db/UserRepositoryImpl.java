package com.hanu.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hanu.base.RepositoryImpl;
import com.hanu.domain.model.User;
import com.hanu.domain.repository.UserRepository;
import com.hanu.domain.repository.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepositoryImpl extends RepositoryImpl<User, String> implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    public UserRepositoryImpl() {
        super();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM user";
            ResultSet rs = this.getConnector().connect().executeSelect(sql);
            UserMapper mapper = new UserMapper();
            while (rs.next()) {
                users.add(mapper.forwardConvert(rs));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return users;
    }

    @Override
    public User getById(String id) {
        try {
            String sql = "SELECT * FROM user";
            ResultSet rs = this.getConnector().connect().executeSelect(sql);
            UserMapper mapper = new UserMapper();
            rs.next();
            return mapper.forwardConvert(rs);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean exists(String id) {
        return getById(id) != null;
    }

    @Override
    public void add(User item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(Iterable<User> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(User item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int remove(Iterable<User> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User update(User item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<User> update(Iterable<User> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }
}