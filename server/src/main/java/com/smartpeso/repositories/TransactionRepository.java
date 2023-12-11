package com.smartpeso.repositories;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.repositories.exceptions.DeleteTransactionException;
import com.smartpeso.repositories.exceptions.TransactionCreationException;
import com.smartpeso.services.transaction.TransactionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTransaction(TransactionDTO transaction, int userId) {
        int insertedRows = insertTransaction(transaction, userId);
        if (insertedRows <= 0) throw new TransactionCreationException("Failed creating transaction \"" + transaction.name() + "\"");
    }

    public Optional<Transaction> getTransactionById(int transactionId) {
        String sql = "SELECT * FROM transaction WHERE transactionId = ?";
        try {
            Transaction transaction = jdbcTemplate.queryForObject(sql, new TransactionRowMapper(), transactionId);
            return Optional.ofNullable(transaction);
        } catch(Exception e) {
            log.error("Failed fetching transaction with id %s: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Transaction> getUserTransactions(int userId) {
        String sql = "SELECT * FROM transaction WHERE userId = ?";
        return jdbcTemplate.query(sql, new TransactionRowMapper(), userId);
    }

    public void updateTransaction(Transaction transaction) {
        String sql = "UPDATE `transaction` SET name=?, `date`=?, type=?, currency=?, value=?, category=?, description=?, paymentMethod=? WHERE `transactionId`=?";
        jdbcTemplate.update(
                sql,
                transaction.getName(),
                Timestamp.valueOf(transaction.getDate()),
                transaction.getType(),
                transaction.getCurrency(),
                transaction.getValue(),
                transaction.getCategory(),
                transaction.getDescription(),
                transaction.getPaymentMethod(),
                transaction.getTransactionId()
        );
    }

    public void deleteTransaction(int transactionId) {
        int rowsDeleted = jdbcTemplate.update("DELETE FROM transaction WHERE transactionId = ?", transactionId);
        if (rowsDeleted <= 0) throw new DeleteTransactionException("Could not delete strategy with id " + transactionId);
    }

    private int insertTransaction(TransactionDTO transaction, int userId) {
        String sql = "INSERT INTO transaction (userId, name, date, type, currency, value, category, description, paymentMethod) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            return jdbcTemplate.update(
                    sql,
                    userId,
                    transaction.name(),
                    Timestamp.valueOf(transaction.date()),
                    transaction.type(),
                    transaction.currency(),
                    transaction.value(),
                    transaction.category(),
                    transaction.description(),
                    transaction.paymentMethod().orElse(null)
            );
        } catch(Exception e) {
            log.error("Failed crating trasaction: " + e.getMessage());
            throw new TransactionCreationException("Failed creating transaction");
        }
    }
}
