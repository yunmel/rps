package com.yunmel.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.yunmel.db.common.CommonDaoFactory;
import com.yunmel.db.common.ConnectionFactory;
import com.yunmel.db.common.DatasourceConfig;
import com.yunmel.db.common.ICommonDao;
import com.yunmel.db.common.impl.DefaultConnectionFactory;
import com.yunmel.rps.model.User;

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
     ICommonDao commonDao = CommonDaoFactory.createCommonDao(datasourceConfig);
//    List<User> users = commonDao.queryAllBusinessObjs(User.class);
    System.out.println(commonDao.queryBusinessObjByPk(User.class, 2));
//    User user = new User();
//    user.setName("asdad");
//    user.setLevel(11);
//    user.setPassword("asdasdasdasd");
//    user.setScore(100);
//    user.setEmail("asdad@yunmel.com");
//    System.out.println(commonDao.saveBusinessObjs(user));
  }
}
