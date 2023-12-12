package com.smartpeso.repositories;

import com.smartpeso.model.User;
import com.smartpeso.repositories.exceptions.UserAlreadyExistsException;
import com.smartpeso.repositories.exceptions.UserCreationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), email);
        if (!users.isEmpty()) {
            return Optional.of(users.get(0));
        } else {
            return Optional.empty();
        }
    }

    public void createUser(String email, String password, String salt, String role, String firstName, String lastName) {
        checkIfUserExistsByEmail(email);
        int insertedRows = insertUser(email, password, salt, role, firstName, lastName);
        if (insertedRows <= 0) throw new UserCreationException("failed to create user with email " + email);
    }

    private int insertUser(String email, String password, String salt, String role, String firstName, String lastName) {
        String sql = "INSERT INTO user (email, password, salt, role, firstname, lastname) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            return jdbcTemplate.update(sql, email, password, salt, role, firstName, lastName);
        } catch(Exception e) {
            throw new UserCreationException(e.getMessage());
        }
    }

    private void checkIfUserExistsByEmail(String email) {
        if (findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }
    }
}
