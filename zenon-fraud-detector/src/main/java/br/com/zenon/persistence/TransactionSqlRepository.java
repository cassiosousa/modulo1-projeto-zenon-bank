package br.com.zenon.persistence;

import br.com.zenon.domain.TransactionType;
import br.com.zenon.persistence.entities.CustomerEntity;
import br.com.zenon.persistence.entities.TransactionEntity;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionSqlRepository implements TransactionRepository {
    private final Logger logger = Logger.getLogger(TransactionSqlRepository.class.getName());
    private Connection conn;

    public TransactionSqlRepository() {
        this.conn = getConnection();
    }

    @Override
    public Optional<TransactionEntity> findByNameOrigin(String name) {
        final String sql = "select * from transaction where name_orig ilike ?";
        return executeQuery(sql, Map.of(1, "%" + name + "%")).stream().findFirst();
    }

    @Override
    public TransactionEntity save(TransactionEntity transaction) {
        final String sql = """
                INSERT INTO transactions (
                    step,
                    type,
                    amount,
                    name_orig,
                    oldbalance_org,
                    newbalance_orig,
                    name_dest,
                    oldbalance_dest,
                    newbalance_dest,
                    is_fraud,
                    is_flagged_fraud
                ) VALUES (
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?
                         );
                
                """;
        executeUpdate(sql, transaction);
        return transaction;
    }

    @Override
    public List<TransactionEntity> saveAll(List<TransactionEntity> transactions) {
        final String sql = """
                INSERT INTO transactions (
                    step,
                    type,
                    amount,
                    name_orig,
                    oldbalance_org,
                    newbalance_orig,
                    name_dest,
                    oldbalance_dest,
                    newbalance_dest,
                    is_fraud,
                    is_flagged_fraud
                ) VALUES (
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?,
                             ?
                         );
                
                """;
        executeUpdate(sql, transactions);
        return transactions;
    }

    private List<TransactionEntity> executeQuery(String sql) {
        return executeQuery(sql, Map.of());
    }

    private List<TransactionEntity> executeQuery(String sql, Map<Integer, Object> params) {

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {

            try (ResultSet rs = ps.executeQuery()) {
                for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                    Integer key = entry.getKey();
                    Object value = entry.getValue();
                    ps.setInt(key, Integer.parseInt(value.toString()));
                }
                List<TransactionEntity> transactions = new ArrayList<>();
                while (rs.next()) {
                    TransactionEntity itemCardapio = readLine(rs);
                    transactions.add(itemCardapio);
                }
                return transactions;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private TransactionEntity readLine(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        long step = rs.getLong("step");
        TransactionType type = TransactionType.valueOf(rs.getString("type"));
        BigDecimal amount = rs.getBigDecimal("amount");
        String nameOrig = rs.getString("name_orig");
        BigDecimal oldbalanceOrg = rs.getBigDecimal("oldbalance_org");
        BigDecimal newbalanceorig = rs.getBigDecimal("newbalance_orig");

        CustomerEntity customerOrg = new CustomerEntity(nameOrig, oldbalanceOrg, newbalanceorig);

        String nameDest = rs.getString("name_dest");
        BigDecimal oldbalanceDest = rs.getBigDecimal("oldbalance_dest");
        BigDecimal newbalanceDest = rs.getBigDecimal("newbalance_dest");
        CustomerEntity customerDest = new CustomerEntity(nameDest, oldbalanceDest, newbalanceDest);

        Boolean isFraud = rs.getBoolean("is_fraud");
        Boolean isFlaggedFraud = rs.getBoolean("is_flagged_fraud");

        return new TransactionEntity(id, step, type, amount, customerOrg, customerDest,
                isFraud, isFlaggedFraud);

    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/db?reWriteBatchedInserts=true", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeUpdate(String sql, TransactionEntity entity) {

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {

            prepareStatement(ps, entity);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void executeUpdate(String sql, List<TransactionEntity> transactions) {

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            conn.setAutoCommit(false);
            int batchSize = 10_000;
            for (int i = 0; i < transactions.size(); i += batchSize) {

                int end = Math.min(i + batchSize, transactions.size());
                var chunkList = transactions.subList(i, end);

                for (TransactionEntity transaction : chunkList) {
                    prepareStatement(ps, transaction);
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void prepareStatement(PreparedStatement ps, TransactionEntity entity) throws SQLException {
        ps.setLong(1, entity.step());
        ps.setString(2, entity.type().name());
        ps.setBigDecimal(3, entity.amount());
        ps.setString(4, entity.customerOrigin().name());
        ps.setBigDecimal(5, entity.customerOrigin().oldBalance());
        ps.setBigDecimal(6, entity.customerOrigin().newBalance());

        ps.setString(7, entity.customerDestination().name());
        ps.setBigDecimal(8, entity.customerDestination().oldBalance());
        ps.setBigDecimal(9, entity.customerDestination().newBalance());

        ps.setBoolean(10, entity.isFraud());
        ps.setBoolean(11, entity.isFlaggedFraud());
    }
}
