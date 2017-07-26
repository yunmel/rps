package com.yunmel.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.yunmel.db.common.ConnectionFactory;
import com.yunmel.db.common.DatasourceConfig;
import com.yunmel.db.common.impl.DefaultConnectionFactory;

public class ConnectFactoryTest {

  public static void main(String[] args) throws SQLException {
    Map<String, String> poolPerperties = new HashMap<>();
    poolPerperties.put(DatasourceConfig._POOL_TYPE, DefaultConnectionFactory._POOL_TYPE_HIKARICP);
    DatasourceConfig datasourceConfig = new DatasourceConfig();
    datasourceConfig.setPoolPerperties(poolPerperties);
    ConnectionFactory cf = new DefaultConnectionFactory(datasourceConfig);
    Connection con = null;
    try {
      con = cf.openConnection();
      System.out.println(con);
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      if (null!=con) {
        con.close();
      }
    } 

  }
}
