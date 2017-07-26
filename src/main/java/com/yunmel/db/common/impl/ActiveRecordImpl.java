/*
 * Copyright ©2016 四川云麦尔科技 Inc. All rights reserved.
 */
package com.yunmel.db.common.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunmel.db.anno.Table;
import com.yunmel.db.common.ARTemplate;
import com.yunmel.db.common.ConnectionCallback;
import com.yunmel.db.common.ConnectionFactory;
import com.yunmel.db.common.IActiveRecord;
import com.yunmel.db.common.Pagination;
import com.yunmel.db.common.jtx.TransactionManager;
import com.yunmel.db.orm.MappingCache;
import com.yunmel.db.orm.Model;
import com.yunmel.db.utils.Assert;
import com.yunmel.db.utils.JdbcUtils;
import com.yunmel.db.utils.StringUtils;

/**
 * TODO : 该类的描述信息
 * 
 * @author xuyq(pazsolr@gmail.com)
 * @time 2017年7月26日 - 下午5:15:02
 */
@SuppressWarnings("rawtypes")
public class ActiveRecordImpl extends ARTemplate implements IActiveRecord {
  private static final Logger logger = LoggerFactory.getLogger(ActiveRecordImpl.class);

  /**
   * @param connectionFactory
   */
  public ActiveRecordImpl(ConnectionFactory connectionFactory) {
    super(connectionFactory);
  }

  @Override
  public void beginTransation() throws SQLException {
    TransactionManager.startManagedConnection(getConnectionFactory(), null);
  }

  @Override
  public void commitTransation() throws SQLException {
    try {
      TransactionManager.commit();
    } finally {
      TransactionManager.closeManagedConnection();
    }
  }

  @Override
  public void rollbackTransation() throws SQLException {
    try {
      TransactionManager.rollback();
    } finally {
      TransactionManager.closeManagedConnection();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.yunmel.db.common.IActiveRecord#save(com.yunmel.db.orm.Model)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T extends Model> int save(T entity) throws Exception {
    Assert.notNull(entity, "pojos can not be null.");
    Table table = entity.getClass().getAnnotation(Table.class);
    String tableName = "";
    if (null != table) {
      tableName = table.name();
    }
    if (tableName == null) {
      tableName = entity.getClass().getSimpleName().toUpperCase();
    }
    Set<Entry<String, Object>> attrs = entity.getAttrsEntrySet();
    List<String> colums = new ArrayList<>();
    List<Object> params = new ArrayList<>();

    for (Entry<String, Object> entry : attrs) {
      Object value = entry.getValue();
      if (null != value) {
        colums.add(entry.getKey());
        params.add(value);
      }
    }

    if (colums.isEmpty() || params.isEmpty() || colums.size() != params.size()) {
      throw new RuntimeException("实体和参数映射错误");
    }

    StringBuffer sql = new StringBuffer("INSERT INTO ");
    sql.append(tableName).append('(');
    int count = 0;
    for (String field : colums) {
      field = StringUtils.getUnderlineName(field);
      if (count == 0) {
        sql.append(field);
      } else {
        sql.append(',').append(field);
      }
      count++;
    }
    sql.append(") VALUES(");
    for (int i = 0; i < count; i++) {
      if (i == 0) {
        sql.append('?');
      } else {
        sql.append(", ?");
      }
    }
    sql.append(')');

    if (autoManagerTransaction) {
      beginTransation();
    }

    try {
      int rtn = saveObj(params, sql.toString());
      if (autoManagerTransaction) {
        commitTransation();
      }
      return rtn;
    } catch (SQLException e) {
      if (autoManagerTransaction) {
        rollbackTransation();
      }
      throw e;
    } catch (Exception e) {
      if (autoManagerTransaction) {
        rollbackTransation();
      }
      throw new SQLException(e);
    }

  }

  private int saveObj(final List<Object> params, final String insertsql) throws Exception {
    Assert.notNull(params);
    Integer val = doExecute(new ConnectionCallback<Integer>() {
      @Override
      public Integer doInConnection(Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try {
          if (logger.isDebugEnabled()) {
            logger.debug("Executing SQL [" + insertsql + "] values:" + params);
          }
          stmt = connection.prepareStatement(insertsql);
          int idx = 1;
          for (Object obj : params) {
            stmt.setObject(idx, obj);
            idx++;
          }
          return stmt.executeUpdate();
        } catch (SQLException e) {
          throw e;
        } catch (Exception e) {
          throw new SQLException(e);
        } finally {
          JdbcUtils.closeStatement(stmt);
          stmt = null;
        }
      }
    });
    return val == null ? 0 : val.intValue();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.yunmel.db.common.IActiveRecord#update(com.yunmel.db.orm.Model)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T extends Model> int update(T entity) throws Exception {
    Assert.notNull(entity, "pojos can not be null.");
    Table table = entity.getClass().getAnnotation(Table.class);
    String tableName = "";
    if (null != table) {
      tableName = table.name();
    }
    if (tableName == null) {
      tableName = entity.getClass().getSimpleName().toUpperCase();
    }
    Set<Entry<String, Object>> attrs = entity.getAttrsEntrySet();
    List<String> colums = new ArrayList<>();
    List<Object> params = new ArrayList<>();
    Object idVal = null;
    for (Entry<String, Object> entry : attrs) {
      Object value = entry.getValue();
      if (null != value) {
        if ("id".equals(entry.getKey())) {
          idVal = value;
        } else {
          colums.add(entry.getKey());
          params.add(value);
        }
      }
    }
    if (null == idVal) {
      throw new RuntimeException("id is null.");
    }
    // add id value
    params.add(idVal);

    if (colums.isEmpty() || params.isEmpty() || (colums.size() + 1) != params.size()) {
      throw new RuntimeException("实体和参数映射错误");
    }

    StringBuffer sql = new StringBuffer("UPDATE ");
    sql.append(tableName);
    int count = 0;
    for (String field : colums) {
      field = StringUtils.getUnderlineName(field);
      if (count == 0) {
        sql.append(" SET ").append(field).append(" = ?");
      } else {
        sql.append(", ").append(field).append(" = ?");
      }
      count++;
    }
    sql.append(" WHERE id = ?");
    if (autoManagerTransaction) {
      beginTransation();
    }

    try {
      int rtn = updateObj(params, sql.toString());
      if (autoManagerTransaction) {
        commitTransation();
      }
      return rtn;
    } catch (SQLException e) {
      if (autoManagerTransaction) {
        rollbackTransation();
      }
      throw e;
    } catch (Exception e) {
      if (autoManagerTransaction) {
        rollbackTransation();
      }
      throw new SQLException(e);
    }
  }

  /**
   * @param params
   * @param string
   * @return
   */
  private int updateObj(final List<Object> params, final String sql) throws Exception {
    Integer val = doExecute(new ConnectionCallback<Integer>() {
      @Override
      public Integer doInConnection(Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try {
          if (logger.isDebugEnabled()) {
            logger.debug("Executing SQL [" + sql + "] values:" + params);
          }
          stmt = connection.prepareStatement(sql);
          int idx = 1;
          for (Object obj : params) {
            stmt.setObject(idx, obj);
            idx++;
          }
          return stmt.executeUpdate();
        } catch (Exception e) {
          throw new SQLException(e);
        } finally {
          JdbcUtils.closeStatement(stmt);
          stmt = null;
        }
      }
    });
    return val == null ? 0 : val.intValue();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.yunmel.db.common.IActiveRecord#findById(java.lang.Class, java.lang.Object)
   */
  @Override
  public <T extends Model> T findById(Class<T> clazz, Object id) throws Exception {
    Assert.notNull(id, "id can not be null.");
    Table table = clazz.getAnnotation(Table.class);
    String tableName = "";
    if (null != table) {
      tableName = table.name();
    }
    if (tableName == null) {
      tableName = clazz.getSimpleName().toUpperCase();
    }
    String sql = "select * from " + tableName + " where id = ?";
    return queryForObject(clazz, sql, id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.yunmel.db.common.IActiveRecord#deleteById(java.lang.Class, java.io.Serializable)
   */
  @Override
  public <T extends Model> int deleteById(Class<T> clazz, Object id) throws Exception {
    Assert.notNull(id, "id must not null or empty!");
    Table table = clazz.getAnnotation(Table.class);
    String tableName = "";
    if (null != table) {
      tableName = table.name();
    }
    if (tableName == null) {
      tableName = clazz.getSimpleName().toUpperCase();
    }
    
    StringBuffer sql = new StringBuffer("DELETE FROM ");
    sql.append(tableName).append(" WHERE id = ?");
    return update(sql.toString(),id);
  }

  /* (non-Javadoc)
   * @see com.yunmel.db.common.IActiveRecord#findAll(java.lang.Class)
   */
  @Override
  public <T extends Model> List<T> findAll(Class<T> clazz) throws Exception {
    String tableName = MappingCache.getIt().getTableName(clazz);
    String sql = "select * from " + tableName;
    return queryForList(clazz, sql);
  }

  /* (non-Javadoc)
   * @see com.yunmel.db.common.IActiveRecord#findByParams(java.lang.Class, java.util.Map)
   */
  @Override
  public <T extends Model> List<T> findByParams(Class<T> clazz, Map<String, Object> params)
      throws Exception {
    String tableName = MappingCache.getIt().getTableName(clazz);
    StringBuffer sql = new StringBuffer("SELECT * FROM ");
    sql.append(tableName).append(" WHERE 1 = 1");
    Object[] objs = new Object[params.size()];
    int idx = 0;
    for (Map.Entry<String, Object> en : params.entrySet()) {
      sql.append(" AND ").append(StringUtils.getUnderlineName(en.getKey())).append(" = ?");
      objs[idx] = en.getValue();
    }
    return queryForList(clazz, sql.toString(), objs);
  }

  /* (non-Javadoc)
   * @see com.yunmel.db.common.IActiveRecord#queryBySql(java.lang.Class, java.lang.String, int, int, java.lang.Object[])
   */
  @Override
  public <T> List<T> queryBySql(Class<T> cls, String sql, int start, int limit, Object... params)
      throws SQLException {
    Assert.notNull(sql, "sql can not be null");
    if (getDialect() == null) {
      throw new SQLException("can not find SQL Dialect.");
    }
    List<T> items = null;
    String sqlforLimit = null;
    if (getDialect().supportsOffset()) {
      sqlforLimit = getDialect().getLimitString(sql, start, limit);
      items = queryBySql(cls, sqlforLimit, params);
    } else {
      sqlforLimit = getDialect().getLimitString(sql, 0, start + limit);
      items = queryBySql(cls, sqlforLimit, start, params);
    }
    return items;
  }

  /* (non-Javadoc)
   * @see com.yunmel.db.common.IActiveRecord#queryPagedBySql(java.lang.Class, java.lang.String, int, int, java.lang.Object[])
   */
  @Override
  public <T> Pagination<T> queryPagedBySql(Class<T> cls, String sql, int startPage, int pageSize,
      Object... params) throws SQLException {
    Assert.notNull(sql, "sql can not be null");
    String coutsql = getDialect().getCountSql(sql);// "SELECT COUNT(0) " + removeSelect(sql);
    Long totalCount = queryForObject(Long.class, coutsql, params);
    if (totalCount != null) {
      int pagecount = (int) Math.ceil((double) totalCount / (double) pageSize);
      int curpage = startPage;
      if (curpage >= pagecount)
        curpage = pagecount - 1;
      int startRecord = curpage >= 0 ? curpage * pageSize : 0;
      List<T> items = null;
      if (0 == totalCount.longValue()) {
        items = new ArrayList<T>(0);
      } else {
        if (getDialect() == null) {
          throw new SQLException("can not find SQL Dialect.");
        }
        String sqlforLimit = null;
        if (getDialect().supportsOffset()) {
          sqlforLimit = getDialect().getLimitString(sql, startRecord, pageSize);
          items = queryBySql(cls, sqlforLimit, params);
        } else {
          int endRecord = (curpage + 1) * pageSize;
          if (endRecord > totalCount.intValue()) {
            endRecord = totalCount.intValue();
          }
          sqlforLimit = getDialect().getLimitString(sql, 0, endRecord);
          items = queryBySql(cls, sqlforLimit, startRecord, params);
        }
      }
      Pagination<T> ps = new Pagination<T>(pageSize, totalCount, curpage, items);
      return ps;
    }
    return null;
  }

  /* (non-Javadoc)
   * @see com.yunmel.db.common.IActiveRecord#queryPagedBySql(java.lang.Class, java.lang.String, java.lang.String, int, int, java.lang.Object[])
   */
  @Override
  public <T> Pagination<T> queryPagedBySql(Class<T> cls, String countsql, String sql, int startPage,
      int pageSize, Object... params) throws SQLException {
    Assert.notNull(countsql, "countsql can not be null");
    Assert.notNull(sql, "sql can not be null");
    Long totalCount = queryForObject(Long.class, countsql, params);
    if (totalCount != null) {
      int pagecount = (int) Math.ceil((double) totalCount / (double) pageSize);
      int curpage = startPage;
      if (curpage >= pagecount)
        curpage = pagecount - 1;
      int startRecord = curpage >= 0 ? curpage * pageSize : 0;
      List<T> items = null;
      if (0 == totalCount.longValue()) {
        items = new ArrayList<T>(0);
      } else {
        if (getDialect() == null) {
          throw new SQLException("can not find SQL Dialect.");
        }
        String sqlforLimit = null;
        if (getDialect().supportsOffset()) {
          sqlforLimit = getDialect().getLimitString(sql, startRecord, pageSize);
          items = queryBySql(cls, sqlforLimit, params);
        } else {
          int endRecord = (curpage + 1) * pageSize;
          if (endRecord > totalCount.intValue()) {
            endRecord = totalCount.intValue();
          }
          sqlforLimit = getDialect().getLimitString(sql, 0, endRecord);
          items = queryBySql(cls, sqlforLimit, startRecord, params);
        }
      }
      Pagination<T> ps = new Pagination<T>(pageSize, totalCount, curpage, items);
      return ps;
    }
    return null;
  }

  /* (non-Javadoc)
   * @see com.yunmel.db.common.IActiveRecord#queryBySql(java.lang.Class, java.lang.String, java.lang.Object[])
   */
  @Override
  public <T> List<T> queryBySql(Class<T> cls, String querySql, Object... params)
      throws SQLException {
    return queryForList(cls, querySql, params);
  }

}
