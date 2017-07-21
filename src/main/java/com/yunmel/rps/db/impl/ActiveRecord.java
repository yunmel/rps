/*
 * Copyright ©2016 四川云麦尔科技 Inc. All rights reserved.
 */
package com.yunmel.rps.db.impl;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunmel.rps.db.IActiveRecord;
import com.yunmel.rps.db.SqlBuilder;
import com.yunmel.rps.db.annotation.Table;
import com.yunmel.rps.exception.RpsSqlException;
import com.yunmel.rps.utils.StringUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 数据库操作类
 * 
 * @author xuyq(pazsolr@gmail.com)
 * @time 2017年7月20日 - 下午11:10:20
 */
public class ActiveRecord implements IActiveRecord {
  private Logger LOG = LoggerFactory.getLogger(ActiveRecord.class);

  private Connection connection = null;
  private DataSource ds;
  private PreparedStatement pstmt = null;
  private ResultSet rs = null;

  public ActiveRecord() {
    HikariConfig config = new HikariConfig();
    config.setMaximumPoolSize(10);
    config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
    config.addDataSourceProperty("serverName", "localhost");
    config.addDataSourceProperty("databaseName", "rps");
    config.addDataSourceProperty("user", "root");
    config.addDataSourceProperty("password", "root");
    config.setInitializationFailFast(true);
    config.addDataSourceProperty("useUnicode", "true");
    config.addDataSourceProperty("characterEncoding", "utf8");
    this.ds = new HikariDataSource(config);
    try {
      this.connection = ds.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void createConnection() throws SQLException {
    this.connection = ds.getConnection();
  }

  private void close() {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (pstmt != null) {
      try {
        pstmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private int exec(PreparedStatement pstmt) {
    try {
      // 1、使用Statement对象发送SQL语句
      int affectedRows = pstmt.executeUpdate();
      // 2、返回结果
      return affectedRows;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  private ResultSet query(PreparedStatement pstmt) {
    try {
      // 1、使用Statement对象发送SQL语句
      rs = pstmt.executeQuery();
      // 2、返回结果
      return rs;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public <T> void save(T entity) throws Exception {
    try {
      if (connection.isClosed()) {
        createConnection();
      }
    } catch (SQLException e) {
      throw new RpsSqlException("Error creating connection", e);
    }

    // SQL语句,insert into table name (
    String sql = "insert into " + getTableName(entity.getClass()) + "(";

    // 获得带有字符串get的所有方法的对象
    List<Method> list = this.matchPojoMethods(entity, "get");

    Iterator<Method> iter = list.iterator();

    // 拼接字段顺序 insert into table name(id,name,email,
    while (iter.hasNext()) {
      Method method = iter.next();
      sql += method.getName().substring(3).toLowerCase() + ",";
    }

    // 去掉最后一个,符号insert insert into table name(id,name,email) values(
    sql = sql.substring(0, sql.lastIndexOf(",")) + ") values(";

    // 拼装预编译SQL语句insert insert into table name(id,name,email) values(?,?,?,
    for (int j = 0; j < list.size(); j++) {
      sql += "?,";
    }

    // 去掉SQL语句最后一个,符号insert insert into table name(id,name,email) values(?,?,?);
    sql = sql.substring(0, sql.lastIndexOf(",")) + ")";

    // 到此SQL语句拼接完成,打印SQL语句
    System.out.println(sql);

    // 获得预编译对象的引用
    PreparedStatement statement = connection.prepareStatement(sql);

    int i = 0;
    // 把指向迭代器最后一行的指针移到第一行.
    iter = list.iterator();
    while (iter.hasNext()) {
      Method method = iter.next();
      // 此初判断返回值的类型,因为存入数据库时有的字段值格式需要改变,比如String,SQL语句是'"+abc+"'
      if (method.getReturnType().getSimpleName().indexOf("String") != -1) {
        statement.setString(++i, this.getString(method, entity));
      } else if (method.getReturnType().getSimpleName().indexOf("Date") != -1) {
        statement.setDate(++i, this.getDate(method, entity));
      } else if (method.getReturnType().getSimpleName().indexOf("InputStream") != -1) {
        statement.setAsciiStream(++i, this.getBlob(method, entity), 1440);
      } else {
        statement.setInt(++i, this.getInt(method, entity));
      }
    }
    // 执行
    exec(statement);
    // 关闭连接
    close();
  }


  /**
   * 修改
   */
  public <T> void update(T entity) throws Exception {
    try {
      if (connection.isClosed()) {
        createConnection();
      }
    } catch (SQLException e) {
      throw new RpsSqlException("Error creating connection", e);
    }

    String sql = "update " + getTableName(entity.getClass()) + " set ";
    // 获得该类所有get方法对象集合
    List<Method> list = this.matchPojoMethods(entity, "get");
    // 临时Method对象,负责迭代时装method对象.
    Method tempMethod = null;
    // 由于修改时不需要修改ID,所以按顺序加参数则应该把Id移到最后.
    Method idMethod = null;
    Iterator<Method> iter = list.iterator();
    while (iter.hasNext()) {
      tempMethod = iter.next();
      // 如果方法名中带有ID字符串并且长度为2,则视为ID.
      if (tempMethod.getName().lastIndexOf("Id") != -1 && tempMethod.getName().substring(3).length() == 2) {
        // 把ID字段的对象存放到一个变量中,然后在集合中删掉.
        idMethod = tempMethod;
        iter.remove();
        // 如果方法名去掉set/get字符串以后与pojo + "id"想符合(大小写不敏感),则视为ID
      } else if ((entity.getClass().getSimpleName() + "Id").equalsIgnoreCase(tempMethod.getName().substring(3))) {
        idMethod = tempMethod;
        iter.remove();
      }
    }
    // 把迭代指针移到第一位
    iter = list.iterator();
    while (iter.hasNext()) {
      tempMethod = iter.next();
      sql += tempMethod.getName().substring(3).toLowerCase() + "= ?,";
    }

    // 去掉最后一个,符号
    sql = sql.substring(0, sql.lastIndexOf(","));

    // 添加条件
    sql += " where " + idMethod.getName().substring(3).toLowerCase() + " = ?";

    // SQL拼接完成,打印SQL语句
    System.out.println(sql);

    PreparedStatement statement = this.connection.prepareStatement(sql);

    int i = 0;
    iter = list.iterator();
    while (iter.hasNext()) {
      Method method = iter.next();
      // 此初判断返回值的类型,因为存入数据库时有的字段值格式需要改变,比如String,SQL语句是'"+abc+"'
      if (method.getReturnType().getSimpleName().indexOf("String") != -1) {
        statement.setString(++i, this.getString(method, entity));
      } else if (method.getReturnType().getSimpleName().indexOf("Date") != -1) {
        statement.setDate(++i, this.getDate(method, entity));
      } else if (method.getReturnType().getSimpleName().indexOf("InputStream") != -1) {
        statement.setAsciiStream(++i, this.getBlob(method, entity), 1440);
      } else {
        statement.setInt(++i, this.getInt(method, entity));
      }
    }
    // 为Id字段添加值
    if (idMethod.getReturnType().getSimpleName().indexOf("String") != -1) {
      statement.setString(++i, this.getString(idMethod, entity));
    } else {
      statement.setInt(++i, this.getInt(idMethod, entity));
    }
    // 执行SQL语句
    statement.executeUpdate();
    // 关闭预编译对象
    statement.close();
    // 关闭连接
    connection.close();
  }


  /**
   * 删除
   */
  public <T> void delete(T entity) throws Exception {
    try {
      if (connection.isClosed()) {
        createConnection();
      }
    } catch (SQLException e) {
      throw new RpsSqlException("Error creating connection", e);
    }

    String sql = "delete from " + getTableName(entity.getClass()) + " where ";

    // 存放字符串为"id"的字段对象
    Method idMethod = null;

    // 取得字符串为"id"的字段对象
    List<Method> list = this.matchPojoMethods(entity, "get");
    Iterator<Method> iter = list.iterator();
    while (iter.hasNext()) {
      Method tempMethod = iter.next();
      // 如果方法名中带有ID字符串并且长度为2,则视为ID.
      if (tempMethod.getName().lastIndexOf("Id") != -1 && tempMethod.getName().substring(3).length() == 2) {
        // 把ID字段的对象存放到一个变量中,然后在集合中删掉.
        idMethod = tempMethod;
        iter.remove();
        // 如果方法名去掉set/get字符串以后与pojo + "id"想符合(大小写不敏感),则视为ID
      } else if ((entity.getClass().getSimpleName() + "Id").equalsIgnoreCase(tempMethod.getName().substring(3))) {
        idMethod = tempMethod;
        iter.remove();
      }
    }

    sql += idMethod.getName().substring(3).toLowerCase() + " = ?";

    PreparedStatement statement = this.connection.prepareStatement(sql);

    // 为Id字段添加值
    int i = 0;
    if (idMethod.getReturnType().getSimpleName().indexOf("String") != -1) {
      statement.setString(++i, this.getString(idMethod, entity));
    } else {
      statement.setInt(++i, this.getInt(idMethod, entity));
    }

    // 执行
    exec(statement);
    // 关闭连接
    close();
  }


  /**
   * 通过ID查询
   */
  public <T> T findById(Class<T> clazz, Object object) throws Exception {
    try {
      if (connection.isClosed()) {
        createConnection();
      }
    } catch (SQLException e) {
      throw new RpsSqlException("Error creating connection", e);
    }

    String sql = "select * from " + getTableName(clazz) + " where " + getPKName(clazz) + " = ? ";;

    // 通过子类的构造函数,获得参数化类型的具体类型.比如BaseDAO<T>也就是获得T的具体类型
    T entity = clazz.newInstance();

    List<Method> list = this.matchPojoMethods(entity, "set");
    Iterator<Method> iter = list.iterator();

    // 封装语句完毕,打印sql语句
    LOG.info("query sql is : {}, param is : {} . ", sql, object);

    // 获得连接
    PreparedStatement statement = this.connection.prepareStatement(sql);

    // 判断id的类型
    if (object instanceof Integer) {
      statement.setInt(1, (Integer) object);
    } else if (object instanceof String) {
      statement.setString(1, (String) object);
    } else if (object instanceof Long) {
      statement.setLong(1, (Long) object);
    }

    // 执行sql,取得查询结果集.
    ResultSet rs = query(statement);
    // 把指针指向迭代器第一行
    iter = list.iterator();
    // 封装
    while (rs.next()) {
      while (iter.hasNext()) {
        Method method = iter.next();
        if (method.getParameterTypes()[0].getSimpleName().indexOf("Date") != -1) {
          this.setDate(method, entity, rs.getDate(method.getName().substring(3).toLowerCase()));
        } else if (method.getParameterTypes()[0].getSimpleName().indexOf("Long") != -1) {
          this.setLong(method, entity, rs.getLong(method.getName().substring(3).toLowerCase()));
        } else if (method.getParameterTypes()[0].getSimpleName().indexOf("Integer") != -1) {
          this.setInt(method, entity, rs.getInt(method.getName().substring(3).toLowerCase()));
        } else {
          this.setString(method, entity, rs.getString(method.getName().substring(3).toLowerCase()));
        }
        // else if (method.getParameterTypes()[0].getSimpleName().indexOf("InputStream") != -1) {
        // this.setBlob(method,
        // entity,rs.getBlob(method.getName().substring(3).toLowerCase()).getBinaryStream());
        // }
      }
    }
    // 关闭结果集
    rs.close();
    // 关闭预编译对象
    statement.close();
    return entity;
  }

  /**
   * 过滤当前Pojo类所有带传入字符串的Method对象,返回List集合.
   */
  private <T> List<Method> matchPojoMethods(T entity, String methodName) {
    // 获得当前Pojo所有方法对象
    Method[] methods = entity.getClass().getDeclaredMethods();

    // List容器存放所有带get字符串的Method对象
    List<Method> list = new ArrayList<Method>();

    // 过滤当前Pojo类所有带get字符串的Method对象,存入List容器
    for (int index = 0; index < methods.length; index++) {
      if (methods[index].getName().indexOf(methodName) != -1) {
        list.add(methods[index]);
      }
    }
    return list;
  }


  /**
   * 方法返回类型为int或Integer类型时,返回的SQL语句值.对应get
   */
  public <T> Integer getInt(Method method, T entity) throws Exception {
    return (Integer) method.invoke(entity, new Object[] {});
  }

  /**
   * 方法返回类型为String时,返回的SQL语句拼装值.比如'abc',对应get
   */
  public <T> String getString(Method method, T entity) throws Exception {
    return (String) method.invoke(entity, new Object[] {});
  }

  /**
   * 方法返回类型为Blob时,返回的SQL语句拼装值.对应get
   */
  public <T> InputStream getBlob(Method method, T entity) throws Exception {
    return (InputStream) method.invoke(entity, new Object[] {});
  }


  /**
   * 方法返回类型为Date时,返回的SQL语句拼装值,对应get
   */
  public <T> Date getDate(Method method, T entity) throws Exception {
    return (Date) method.invoke(entity, new Object[] {});
  }

  /**
   * 参数类型为Integer或int时,为entity字段设置参数,对应set
   */
  public <T> Long setLong(Method method, T entity, Long arg) throws Exception {
    return (Long) method.invoke(entity, new Object[] {arg});
  }

  /**
   * 参数类型为Integer或int时,为entity字段设置参数,对应set
   */
  public <T> Integer setInt(Method method, T entity, Integer arg) throws Exception {
    return (Integer) method.invoke(entity, new Object[] {arg});
  }

  /**
   * 参数类型为String时,为entity字段设置参数,对应set
   */
  public <T> String setString(Method method, T entity, String arg) throws Exception {
    return (String) method.invoke(entity, new Object[] {arg});
  }

  /**
   * 参数类型为InputStream时,为entity字段设置参数,对应set
   */
  public <T> InputStream setBlob(Method method, T entity, InputStream arg) throws Exception {
    return (InputStream) method.invoke(entity, new Object[] {arg});
  }


  /**
   * 参数类型为Date时,为entity字段设置参数,对应set
   */
  public <T> Date setDate(Method method, T entity, Date arg) throws Exception {
    return (Date) method.invoke(entity, new Object[] {arg});
  }

  public String getTableName(Class<?> clazz) {
    Table table = clazz.getAnnotation(Table.class);
    if (null != table) {
      return table.name();
    }
    // Java属性的骆驼命名法转换回数据库下划线“_”分隔的格式
    return StringUtils.getUnderlineName(clazz.getSimpleName());
  }

  /**
   * 根据表名获取主键名
   *
   * @param clazz
   * @return
   */
  public String getPKName(Class<?> clazz) {
    Table table = clazz.getAnnotation(Table.class);
    if (null != table) {
      return table.pk();
    }
    return "id";
  }

  @Override
  public <T> List<T> findAll(Class<T> clazz) throws Exception {

    try {
      if (connection.isClosed()) {
        createConnection();
      }
    } catch (SQLException e) {
      throw new RpsSqlException("Error creating connection", e);
    }

    String sql = "select * from " + getTableName(clazz);

    // 通过子类的构造函数,获得参数化类型的具体类型.比如BaseDAO<T>也就是获得T的具体类型
    T entity = clazz.newInstance();

    List<Method> list = this.matchPojoMethods(entity, "set");
    Iterator<Method> iter = list.iterator();

    // 封装语句完毕,打印sql语句
    LOG.info("query sql is : {} . ", sql);

    // 获得连接
    PreparedStatement statement = this.connection.prepareStatement(sql);

    // 执行sql,取得查询结果集.
    ResultSet rs = query(statement);
    // 把指针指向迭代器第一行
    iter = list.iterator();
    List<T> resultList = new ArrayList<>();
    // 封装
    while (rs.next()) {
      entity = clazz.newInstance();
      while (iter.hasNext()) {
        Method method = iter.next();
        if (method.getParameterTypes()[0].getSimpleName().indexOf("Date") != -1) {
          this.setDate(method, entity, rs.getDate(method.getName().substring(3).toLowerCase()));
        } else if (method.getParameterTypes()[0].getSimpleName().indexOf("Long") != -1) {
          this.setLong(method, entity, rs.getLong(method.getName().substring(3).toLowerCase()));
        } else if (method.getParameterTypes()[0].getSimpleName().indexOf("Integer") != -1) {
          this.setInt(method, entity, rs.getInt(method.getName().substring(3).toLowerCase()));
        } else {
          this.setString(method, entity, rs.getString(method.getName().substring(3).toLowerCase()));
        }
        // else if (method.getParameterTypes()[0].getSimpleName().indexOf("InputStream") != -1) {
        // this.setBlob(method,
        // entity,rs.getBlob(method.getName().substring(3).toLowerCase()).getBinaryStream());
        // }
      }
      resultList.add(entity);
    }
    // 关闭结果集
    rs.close();
    // 关闭预编译对象
    statement.close();
    return resultList;
  }

  @Override
  public <T> List<T> findByParams(Class<T> clazz, Map<String, Object> params) throws Exception {
    try {
      if (connection.isClosed()) {
        createConnection();
      }
    } catch (SQLException e) {
      throw new RpsSqlException("Error creating connection", e);
    }
    Object[] objs = new Object[params.size()];
    String sql = "select * from " + getTableName(clazz) + " where 1 = 1" + SqlBuilder.buildSql(params, objs);
    LOG.info("query sql is : {} . ", sql);
    PreparedStatement stmt = this.connection.prepareStatement(sql);
    for (int i = 1; i <= objs.length; i++) {
      stmt.setObject(i, objs[i - 1]);
    }
    ResultSet rs = stmt.executeQuery();
    List<T> list = new ArrayList<>();
    T entity = clazz.newInstance();
    List<Method> methods = this.matchPojoMethods(entity, "set");
    Iterator<Method> iter = methods.iterator();
    while (rs.next()) {
      entity = clazz.newInstance();
      while (iter.hasNext()) {
        Method method = iter.next();
        if (method.getParameterTypes()[0].getSimpleName().indexOf("Date") != -1) {
          this.setDate(method, entity, rs.getDate(method.getName().substring(3).toLowerCase()));
        } else if (method.getParameterTypes()[0].getSimpleName().indexOf("Long") != -1) {
          this.setLong(method, entity, rs.getLong(method.getName().substring(3).toLowerCase()));
        } else if (method.getParameterTypes()[0].getSimpleName().indexOf("Integer") != -1) {
          this.setInt(method, entity, rs.getInt(method.getName().substring(3).toLowerCase()));
        } else {
          this.setString(method, entity, rs.getString(method.getName().substring(3).toLowerCase()));
        }
      }
      list.add(entity);
    }
    return list;
  }

}
