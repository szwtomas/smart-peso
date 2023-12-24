package com.smartpeso.auth.dal;

import com.smartpeso.auth.model.User;
import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("userId"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("salt"),
                rs.getString("role"),
                rs.getString("firstname"),
                rs.getString("lastname")
        );
    }
}
