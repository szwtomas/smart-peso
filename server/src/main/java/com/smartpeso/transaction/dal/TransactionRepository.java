package com.smartpeso.transaction.dal;

import com.smartpeso.transaction.model.dto.TransactionDTO;
import com.smartpeso.transaction.exception.DeleteTransactionException;
import com.smartpeso.transaction.exception.TransactionCreationException;
import com.smartpeso.transaction.model.Transaction;
import com.smartpeso.transaction.model.TransactionWithPrices;
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

    public List<TransactionWithPrices> getUserTransactionWithPrices(int userId) {
        String sql = getUserTransactionsWithPricesSQL(userId);
        return jdbcTemplate.query(sql, new TransactionWithPricesRowMapper(), userId);
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
            log.error("Failed crating transaction: " + e.getMessage());
            throw new TransactionCreationException("Failed creating transaction");
        }
    }

    private String getUserTransactionsWithPricesSQL(int userId) {
        return """
            SELECT
                t.transactionId as transactionId,
                t.userId as userId,
                t.name as name,
                t.date AS date,
                t.type as type,
                t.currency as currency,
                t.value as value,
                t.category as category,
                t.description as description,
                t.paymentMethod as paymentMethod,
                grouped_cp.official as usdOfficial,
                grouped_cp.mep as usdMEP,
                grouped_cp.ccl as usdCCL,
                grouped_cp.blue as usdBlue
            FROM transaction t
            INNER JOIN (
                SELECT
                    DATE(cp.date) as date,
                    ROUND(AVG(cp.usdOfficial), 2) as official,
                    ROUND(AVG(cp.usdMEP), 2) as mep,
                    ROUND(AVG(cp.usdCCL), 2) as ccl,
                    ROUND(AVG(cp.usdBlue), 2) as blue
                FROM currencyPrices cp
                GROUP BY DATE(cp.date)
            ) AS grouped_cp ON (
                YEAR(t.date) = YEAR(grouped_cp.date)
                AND MONTH(t.date) = MONTH(grouped_cp.date)
                AND DAY(t.date) = DAY(grouped_cp.date)
            ) WHERE t.userId = ?
        """;
    }
}
