package com.hanu.domain.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hanu.base.Mapper;
import com.hanu.domain.model.User;

public class UserMapper extends Mapper<User> {
    public UserMapper() {
        super(UserMapper::fromResultSet);
    }

    private static User fromResultSet(ResultSet rs) {
        try {
            return new User(rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("salt"));
        } catch (SQLException e) {
            return null;
        }
    }
}