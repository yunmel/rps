package com.yunmel.db.common.pool;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.yunmel.db.common.IDataSourceCreator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSourceCreator implements IDataSourceCreator {
  private String jdbcUrl;
  private String username;
  private String password;
  private boolean autoCommit = true;
  private long connectionTimeout = 30000;
  private long idleTimeout = 600000;
  private long maxLifetime = 1800000;
  private String connectionTestQuery = null;
  private int maximumPoolSize = 10;
  private String poolName = null;
  private boolean readOnly = false;
  private String catalog = null;
  private String connectionInitSql = null;
  private String driverClass = null;
  private String transactionIsolation = null;
  private long validationTimeout = 5000;
  private long leakDetectionThreshold = 0;


  public HikariCPDataSourceCreator(String jdbcUrl, String username, String password) {
    this.jdbcUrl = jdbcUrl;
    this.username = username;
    this.password = password;
  }

  public HikariCPDataSourceCreator(String jdbcUrl, String username, String password, String driverClass) {
    this.jdbcUrl = jdbcUrl;
    this.username = username;
    this.password = password;
    this.driverClass = driverClass;
  }

  // TODO ： replace config file.
  @Override
  public DataSource createDatasource() throws SQLException {
    HikariConfig config = new HikariConfig();
    // 设定基本参数
    config.setJdbcUrl(jdbcUrl);
    config.setUsername(username);
    config.setPassword(password);

    // 设定额外参数
    config.setAutoCommit(autoCommit);
    config.setReadOnly(readOnly);

    config.setConnectionTimeout(connectionTimeout);
    config.setIdleTimeout(idleTimeout);
    config.setMaxLifetime(maxLifetime);
    config.setMaximumPoolSize(maximumPoolSize);
    config.setValidationTimeout(validationTimeout);

    // config.setTransactionIsolation(datasourcecfg.get);

    if (this.leakDetectionThreshold != 0) {
      config.setLeakDetectionThreshold(leakDetectionThreshold);
    }

    if (jdbcUrl.toLowerCase().contains(":mysql:")) {
      config.addDataSourceProperty("cachePrepStmts", "true");
      config.addDataSourceProperty("useServerPrepStmts", "true");
      config.addDataSourceProperty("prepStmtCacheSize", "256");
      config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    if (jdbcUrl.toLowerCase().contains(":postgresql:")) {
      if (this.readOnly) {
        config.addDataSourceProperty("readOnly", "true");
      }
      config.setConnectionTimeout(0);
      config.addDataSourceProperty("prepareThreshold", "3");
      config.addDataSourceProperty("preparedStatementCacheQueries", "128");
      config.addDataSourceProperty("preparedStatementCacheSizeMiB", "4");
    }

    return new HikariDataSource(config);

    /*
     * HikariConfig config = new HikariConfig(); config.setMaximumPoolSize(10);
     * config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
     * config.addDataSourceProperty("serverName", "localhost");
     * config.addDataSourceProperty("databaseName", "rps"); config.addDataSourceProperty("user",
     * "root"); config.addDataSourceProperty("password", "root");
     * config.setInitializationFailFast(true); config.addDataSourceProperty("useUnicode", "true");
     * config.addDataSourceProperty("characterEncoding", "utf8"); return new
     * HikariDataSource(config);
     */
  }

  public String getJdbcUrl() {
    return jdbcUrl;
  }

  public void setJdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isAutoCommit() {
    return autoCommit;
  }

  public void setAutoCommit(boolean autoCommit) {
    this.autoCommit = autoCommit;
  }

  public long getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(long connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public long getIdleTimeout() {
    return idleTimeout;
  }

  public void setIdleTimeout(long idleTimeout) {
    this.idleTimeout = idleTimeout;
  }

  public long getMaxLifetime() {
    return maxLifetime;
  }

  public void setMaxLifetime(long maxLifetime) {
    this.maxLifetime = maxLifetime;
  }

  public String getConnectionTestQuery() {
    return connectionTestQuery;
  }

  public void setConnectionTestQuery(String connectionTestQuery) {
    this.connectionTestQuery = connectionTestQuery;
  }

  public int getMaximumPoolSize() {
    return maximumPoolSize;
  }

  public void setMaximumPoolSize(int maximumPoolSize) {
    this.maximumPoolSize = maximumPoolSize;
  }

  public String getPoolName() {
    return poolName;
  }

  public void setPoolName(String poolName) {
    this.poolName = poolName;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  public String getCatalog() {
    return catalog;
  }

  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  public String getConnectionInitSql() {
    return connectionInitSql;
  }

  public void setConnectionInitSql(String connectionInitSql) {
    this.connectionInitSql = connectionInitSql;
  }

  public String getDriverClass() {
    return driverClass;
  }

  public void setDriverClass(String driverClass) {
    this.driverClass = driverClass;
  }

  public String getTransactionIsolation() {
    return transactionIsolation;
  }

  public void setTransactionIsolation(String transactionIsolation) {
    this.transactionIsolation = transactionIsolation;
  }

  public long getValidationTimeout() {
    return validationTimeout;
  }

  public void setValidationTimeout(long validationTimeout) {
    this.validationTimeout = validationTimeout;
  }

  public long getLeakDetectionThreshold() {
    return leakDetectionThreshold;
  }

  public void setLeakDetectionThreshold(long leakDetectionThreshold) {
    this.leakDetectionThreshold = leakDetectionThreshold;
  }



}
