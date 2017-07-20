package com.yunmel.rps.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbConfig {
	public static final DataSource hikariPool() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(10);
        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        config.addDataSourceProperty("serverName", "localhost");
        config.addDataSourceProperty("databaseName", "rps");
        config.addDataSourceProperty("user", "root");
        config.addDataSourceProperty("password", "root");
        config.setInitializationFailFast(true);
        return new HikariDataSource(config);
    }
	
}
