/*
 * Copyright ©2016 四川云麦尔科技 Inc. All rights reserved.
 */
package com.yunmel.db.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunmel.db.DataBaseType;
import com.yunmel.db.common.impl.ActiveRecordSetExtractor;
import com.yunmel.db.common.jtx.TransactionManager;
import com.yunmel.db.dialect.DefaultDialect;
import com.yunmel.db.dialect.Dialect;
import com.yunmel.db.dialect.MySQLDialect;
import com.yunmel.db.utils.Assert;
import com.yunmel.db.utils.JdbcUtils;

/**
 * TODO : 该类的描述信息
 * 
 * @author xuyq(pazsolr@gmail.com)
 * @time 2017年7月26日 - 下午5:13:11
 */
public class ARTemplate {
  private static final Logger logger = LoggerFactory.getLogger(ARTemplate.class);
  private ConnectionFactory connectionFactory;
  /** 默认自动管理事务 */
  protected boolean autoManagerTransaction = true;
  private Dialect dialect = null;

  public ARTemplate(ConnectionFactory connectionFactory) {
    super();
    this.connectionFactory = connectionFactory;
  }

  public <T> T doExecute(StatementCallback<T> action) throws SQLException {
    Assert.notNull(action, "Callback object must not be null");
    Connection connection = TransactionManager.getConnection(connectionFactory);
    Statement stmt = null;
    try {
      stmt = connection.createStatement();
      T result = action.doInStatement(stmt);
      return result;
    } catch (SQLException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      throw ex;
    } finally {
      JdbcUtils.closeStatement(stmt);
      stmt = null;
      TransactionManager.closeConnection(connection);
    }
  }

  public <T> T doExecute(ConnectionCallback<T> action) throws SQLException {
    Assert.notNull(action, "Callback object must not be null");
    Connection connection = TransactionManager.getConnection(connectionFactory);
    try {
      T result = action.doInConnection(connection);
      return result;
    } catch (SQLException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      throw ex;
    } finally {
      TransactionManager.closeConnection(connection);
    }
  }


  /**
   * 带参数的
   * 
   * @param <T>
   * @param action
   * @param sql
   * @param paramClass: 参数@param params参考的ORM class，参数@param params中定义的优先,如参数@param
   *        params中的OrmClass都已经定义，则此参数可给null
   * @param params
   * @return
   * @throws SQLException
   */
  protected <T> T doExecute(StatementCallback<T> action, String sql, final Class<?> paramClass,
      Object... params) throws SQLException {
    Assert.notNull(action, "Callback object must not be null");
    Assert.notNull(sql, "sql must not be null");
    Connection connection = TransactionManager.getConnection(connectionFactory);
    PreparedStatement stmt = null;
    try {
      if (logger.isDebugEnabled()) {
        if (params != null && params.length > 0) {
          logger.debug("Executing SQL statement [" + sql + "] values: " + Arrays.asList(params));
        } else {
          logger.debug("Executing SQL statement [" + sql + "]");
        }
      }
      stmt = connection.prepareStatement(sql);
      int idx = 1;
      for (Object param : params) {
        stmt.setObject(idx, param);
        idx++;
      }
      T result = action.doInStatement(stmt);
      return result;
    } catch (SQLException ex) {
      throw ex;
    } catch (Exception e) {
      throw new SQLException(e);
    } finally {
      JdbcUtils.closeStatement(stmt);
      stmt = null;
      TransactionManager.closeConnection(connection);
    }
  }

  protected <T> T doExecuteInTransation(StatementCallback<T> action) throws SQLException {
    Assert.notNull(action, "Callback object must not be null");
    TransactionManager.startManagedConnection(connectionFactory, null);
    Connection connection = TransactionManager.getConnection(connectionFactory);
    Statement stmt = null;
    try {
      stmt = connection.createStatement();
      T result = action.doInStatement(stmt);
      TransactionManager.commit();
      return result;
    } catch (SQLException ex) {
      TransactionManager.rollback();
      throw ex;
    } catch (RuntimeException ex) {
      TransactionManager.rollback();
      throw ex;
    } finally {
      JdbcUtils.closeStatement(stmt);
      stmt = null;
      TransactionManager.closeManagedConnection();
    }
  }

  /**
   * 事务、带参数的
   * 
   * @param <T>
   * @param action
   * @param sql
   * @param paramClass: 参数@param params参考的ORM class，参数@param params中定义的优先,如参数@param
   *        params中的OrmClass都已经定义，则此参数可给null
   * @param params
   * @return
   * @throws SQLException
   */
  protected <T> T doExecuteInTransation(StatementCallback<T> action, String sql,
      final Class<T> paramClass, Object... params) throws SQLException {
    Assert.notNull(action, "Callback object must not be null");
    Assert.notNull(sql, "sql must not be null");
    TransactionManager.startManagedConnection(connectionFactory, null);
    Connection connection = TransactionManager.getConnection(connectionFactory);
    PreparedStatement stmt = null;
    try {
      if (logger.isDebugEnabled()) {
        if (params != null && params.length > 0) {
          logger.debug("Executing SQL statement [" + sql + "] values: " + Arrays.asList(params));
        } else {
          logger.debug("Executing SQL statement [" + sql + "]");
        }
      }
      stmt = connection.prepareStatement(sql);
      int idx = 1;
      for (Object param : params) {
        stmt.setObject(idx, param);
        idx++;
      }
      T result = action.doInStatement(stmt);
      TransactionManager.commit();
      return result;
    } catch (SQLException ex) {
      TransactionManager.rollback();
      throw ex;
    } catch (Exception ex) {
      TransactionManager.rollback();
      throw new SQLException(ex);
    } finally {
      JdbcUtils.closeStatement(stmt);
      stmt = null;
      TransactionManager.closeManagedConnection();
    }
  }
  
  protected <T> T doExecuteInTransation(ConnectionCallback<T> action) throws SQLException{
    Assert.notNull(action, "Callback object must not be null");
    TransactionManager.startManagedConnection(connectionFactory, null);
    Connection connection = TransactionManager.getConnection(connectionFactory);
    try {
        T result = action.doInConnection(connection);
        TransactionManager.commit();
        return result;
    } catch (SQLException ex) {
        TransactionManager.rollback();
        throw ex;
    } catch (RuntimeException ex) {
        TransactionManager.rollback();
        throw ex;
    } finally {
        TransactionManager.closeManagedConnection();
    }
}

  public boolean execute(final String sql, final Object... params) throws SQLException {
    class ExecuteStatementCallback implements StatementCallback<Boolean> {
      /*
       * (non-Javadoc)
       * 
       * @see org.uorm.dao.common.StatementCallback#doInStatement(java.sql.Statement)
       */
      @Override
      public Boolean doInStatement(Statement stmt) throws SQLException {
        if (logger.isDebugEnabled()) {
          if (params != null && params.length > 0) {
            logger.debug("Executing SQL statement [" + sql + "] values: " + Arrays.asList(params));
          } else {
            logger.debug("Executing SQL statement [" + sql + "]");
          }
        }
        if (stmt instanceof PreparedStatement) {
          return ((PreparedStatement) stmt).execute();
        } else {
          return stmt.execute(sql);
        }
      }
    }
    if (params == null || params.length == 0) {
      if (autoManagerTransaction) {
        return doExecuteInTransation(new ExecuteStatementCallback());
      } else {
        return doExecute(new ExecuteStatementCallback());
      }
    } else {
      if (autoManagerTransaction) {
        return doExecuteInTransation(new ExecuteStatementCallback(), sql, null, params);
      } else {
        return doExecute(new ExecuteStatementCallback(), sql, null, params);
      }
    }
  }

  public <T> T execute(ConnectionCallback<T> action) throws SQLException {
    if (autoManagerTransaction) {
      return doExecuteInTransation(action);
    } else {
      return doExecute(action);
    }
  }

  public <T> T execute(StatementCallback<T> action) throws SQLException {
    if (autoManagerTransaction) {
      return doExecuteInTransation(action);
    } else {
      return doExecute(action);
    }
  }

  /**
   * 更新
   * 
   * @param sql
   * @return
   * @throws SQLException
   */
  public int update(final String sql, final Object... params) throws SQLException {
    Assert.notNull(sql, "SQL must not be null");
    class UpdateStatementCallback implements StatementCallback<Integer> {
      @Override
      public Integer doInStatement(Statement stmt) throws SQLException {
        if (logger.isDebugEnabled()) {
          if (params != null && params.length > 0) {
            logger.debug("Executing SQL update [" + sql + "] values: " + Arrays.asList(params));
          } else {
            logger.debug("Executing SQL update [" + sql + "]");
          }
        }
        if (stmt instanceof PreparedStatement) {
          return ((PreparedStatement) stmt).executeUpdate();
        } else {
          return stmt.executeUpdate(sql);
        }
      }
    }

    if (params == null || params.length == 0) {
      if (autoManagerTransaction) {
        return doExecuteInTransation(new UpdateStatementCallback());
      } else {
        return doExecute(new UpdateStatementCallback());
      }
    } else {
      if (autoManagerTransaction) {
        return doExecuteInTransation(new UpdateStatementCallback(), sql, null, params);
      } else {
        return doExecute(new UpdateStatementCallback(), sql, null, params);
      }
    }
  }

  /**
   * query
   * 
   * @param <T>
   * @param sql
   * @param rse
   * @param params
   * @return
   * @throws SQLException
   */
  public <T> T query(final String sql, final ResultSetExtractor<T> rse, final Object... params)
      throws SQLException {
    Assert.notNull(sql, "SQL must not be null");
    Assert.notNull(rse, "ResultSetExtractor must not be null");
    class QueryStatementCallback implements StatementCallback<T> {
      @Override
      public T doInStatement(Statement stmt) throws SQLException {
        ResultSet rs = null;
        try {
          if (logger.isDebugEnabled()) {
            if (params != null && params.length > 0) {
              logger.debug("Executing SQL query [" + sql + "] values: " + Arrays.asList(params));
            } else {
              logger.debug("Executing SQL query [" + sql + "]");
            }
          }
          if (stmt instanceof PreparedStatement) {
            rs = ((PreparedStatement) stmt).executeQuery();
          } else {
            rs = stmt.executeQuery(sql);
          }
          return rse.extractData(rs);
        } finally {
          JdbcUtils.closeResultSet(rs);
        }
      }
    }
    if (params == null || params.length == 0) {
      return doExecute(new QueryStatementCallback());
    } else {
      return doExecute(new QueryStatementCallback(), sql, null, params);
    }
  }

  /**
   * query
   * 
   * @param <T>
   * @param sql
   * @param rse
   * @param paramClass: 参数@param params参考的ORM class，参数@param params中定义的优先
   * @param params
   * @return
   * @throws SQLException
   */
  public <T> T query(final String sql, final ResultSetExtractor<T> rse, Class<?> paramClass,
      final Object... params) throws SQLException {
    if (params == null || params.length == 0) {
      return query(sql, rse);
    } else {
      Assert.notNull(sql, "SQL must not be null");
      Assert.notNull(rse, "ResultSetExtractor must not be null");
      class QueryStatementCallback implements StatementCallback<T> {
        @Override
        public T doInStatement(Statement stmt) throws SQLException {
          ResultSet rs = null;
          try {
            if (stmt instanceof PreparedStatement) {
              rs = ((PreparedStatement) stmt).executeQuery();
            } else {
              rs = stmt.executeQuery(sql);
            }
            return rse.extractData(rs);
          } finally {
            JdbcUtils.closeResultSet(rs);
          }
        }
      }
      return doExecute(new QueryStatementCallback(), sql, paramClass, params);
    }
  }

  /**
   * 查询返回单个对象
   * 
   * @param <T>
   * @param cls
   * @param sql
   * @param params
   * @return
   * @throws SQLException
   */
  public <T> T queryForObject(Class<T> cls, String sql, final Object... params)
      throws SQLException {
    ActiveRecordSetExtractor<T> extractor = new ActiveRecordSetExtractor<T>(cls);
    extractor.setMax(1);
    List<T> results = null;
    if (params == null || params.length == 0) {
      results = query(sql, extractor);
    } else {
      results = query(sql, extractor, cls, params);
    }
    if (results != null && results.size() > 0) {
      return results.get(0);
    }
    return null;
  }

  /**
   * 查询
   * 
   * @param <T>
   * @param cls
   * @param sql：sql查询语句
   * @param params：参数
   * @return：List<T>
   * @throws SQLException
   */
  public <T> List<T> queryForList(final Class<T> cls, final String sql, final Object... params)
      throws SQLException {
    if (params == null || params.length == 0) {
      return query(sql, new ActiveRecordSetExtractor<T>(cls));
    } else {
      return query(sql, new ActiveRecordSetExtractor<T>(cls), cls, params);
    }
  }


  public ConnectionFactory getConnectionFactory() {
    return connectionFactory;
  }

  public void setConnectionFactory(ConnectionFactory connectionFactory) {
    this.connectionFactory = connectionFactory;
  }

  public boolean isAutoManagerTransaction() {
    return autoManagerTransaction;
  }

  public void setAutoManagerTransaction(boolean autoManagerTransaction) {
    this.autoManagerTransaction = autoManagerTransaction;
  }

  public Dialect getDialect() {
    if(dialect == null) {
      dialect = genDialect(this.connectionFactory.getConfiguration().getDatabasetype(), this.connectionFactory.getConfiguration().getDialectClass());
    }
    return dialect;
  }

  public void setDialect(Dialect dialect) {
    this.dialect = dialect;
  }
  
  
  /**
   * generate sql dialect
   * @param databasetype
   * @param dialectClass
   * @return
   */
  private Dialect genDialect(DataBaseType databasetype, String dialectClass) {
      Dialect dialect = null;
      if( dialectClass == null || dialectClass.length() == 0 ) {
          switch (databasetype) {
          case MYSQL:
              dialect = new MySQLDialect();
              break;
          default:
              dialect = new DefaultDialect();
              break;
          }
      } else {
          dialect = constructDialect(dialectClass);
      }
      return dialect;
  }
  
  /**
   * 初始化SQL方言类
   * @param typeClass
   * @return
   */
  private Dialect constructDialect(String typeClass) {
      Class<?> _class;
      try {
          _class = Class.forName(typeClass);
      } catch (ClassNotFoundException e) {
          logger.error("",e);
          return null;
      }
      try {
          Object insObject = _class.newInstance();
          if(insObject instanceof Dialect){
              return (Dialect)insObject;
          }
      } catch (InstantiationException e) {
          logger.error("",e);
      } catch (IllegalAccessException e) {
          logger.error("",e);
      }
      return null;
  }
}
