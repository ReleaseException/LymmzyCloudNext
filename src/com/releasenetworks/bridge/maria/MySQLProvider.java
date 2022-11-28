package com.releasenetworks.bridge.maria;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class MySQLProvider {

    private final HikariDataSource dataSource;

    public MySQLProvider(String host, int port, String user, String databace, String password) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s",
                host,
                port,
                databace));
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        hikariConfig.setPoolName("LymmzyCloud MySQLPool");

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void sendRequest(String sql, Object... args) {
        try (Connection connection = this.createConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeQuery(Consumer<ResultSet> consumer, String sql, Object... args) {
        try (Connection connection = this.createConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            consumer.accept(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}