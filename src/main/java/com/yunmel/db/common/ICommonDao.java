package com.yunmel.db.common;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface ICommonDao {
  /**
   * 开始一个事务
   * @throws SQLException 
   */
  void beginTransation() throws SQLException;

  /**
   * 事务提交
   * 
   * @throws Exception
   */
  void commitTransation() throws SQLException;

  /**
   * 事务回滚
   * @throws SQLException 
   */
  void rollbackTransation() throws SQLException;
  
  /**
   * 获取是否自动管理事务
   * @return
   */
  boolean isAutoManagerTransaction();
  
  /**
   * 设置是否自动管理事务
   * @param autoManagerTransaction
   */
  void setAutoManagerTransaction(boolean autoManagerTransaction);
  
  /**
   * 根据主键查询
   * @param <T>
   * @param cls
   * @param pk 多个主键，按照 cls 中定义的keyOrder顺序给值
   * @return
   */
  public <T> T queryById(Class<T> cls, Serializable id) throws SQLException;
  
  /**
   * sql 查询 （可带参数，例： select * from tab a where a.fielda = ?）
   * @param <T>
   * @param cls
   * @param querySql 带参数的查询sql语句
   * @param params 参数，顺序很重要，和 @param query中参数 ? 的顺序对应; name:参数名，为ORM对应class变量名或表字段名
   * @return
   * @throws Exception
   */
  public <T> List<T> queryBySql(Class<T> cls, String querySql, SqlParameter... params) throws SQLException;
  
  /**
   * 查询表里所有数据
   * @param <T>
   * @param cls
   * @return
   * @throws Exception
   */
  public <T> List<T> queryAll(Class<T> cls) throws SQLException;
  
  /**
   * sql 查询
   * @param <T>
   * @param sql
   * @param rse
   * @param params 参数，顺序很重要，和@param query中参数?的顺序对应;name:参数名，为ORM对应class变量名或表字段名
   * @return
   * @throws Exception
   */
  public <T> T query(final String sql, final ResultSetExtractor<T> rse, final SqlParameter... params) throws SQLException; 
  
  /**
   * sql查询单个对象
   * @param <T>
   * @param cls
   * @param sql
   * @param params 参数，顺序很重要，和@param query中参数?的顺序对应;name:参数名，为ORM对应class变量名或表字段名
   * @return
   * @throws Exception
   */
  public <T> T querySingle(Class<T> cls, String sql, SqlParameter... params) throws SQLException;
  
  /**
   * sql查询
   * @param <T>
   * @param cls
   * @param sql
   * @param start 开始记录 start with 0
   * @param limit  取maxRecord条，取的条数 <= limit
   * @param params 参数，顺序很重要，和@param query中参数?的顺序对应;name:参数名，为ORM对应class变量名或表字段名
   * @return
   * @throws Exception
   */
  public <T> List<T> queryBySql(Class<T> cls, final String sql, final int start, final int limit, final SqlParameter... params) throws SQLException;
  
  /**
   * 进行分页查询
   * @param cls
   * @param sql
   * @param startPage  开始页数，页码从0开始
   * @param pageSize  每页大小
   * @param params 参数，顺序很重要，和@param query中参数?的顺序对应;name:参数名，为ORM对应class变量名或表字段名
   * @return
   * @throws Exception
   */
  public <T> Pagination<T> queryPagedBySql(Class<T> cls, final String sql, final int startPage, final int pageSize, final SqlParameter... params) throws SQLException;
  
  /**
   * 进行分页查询
   * @param cls
   * @param countsql 查询总记录sql，这个sql的条件参数必须和@param sql（查询记录sql）的条件参数一致，包括顺序
   * @param sql 查询记录sql，这个sql的条件参数必须和@param countsql（查询总记录sql）的条件参数一致，包括顺序
   * @param startPage
   * @param pageSize
   * @param params
   * @return
   * @throws SQLException
   */
  public <T> Pagination<T> queryPagedBySql(Class<T> cls, final String countsql, final String sql, final int startPage, final int pageSize, final SqlParameter... params) throws SQLException;

  
  /**
   * 保存数据到数据库
   * @param pojos
   * @return
   * @throws Exception
   */
  public int save(Object... pojos) throws SQLException;
  
  /**
   * 保存数据到数据库
   * @param pojos
   * @return
   * @throws Exception
   */
  public int save(Collection<? extends Serializable> pojos) throws SQLException;
}
