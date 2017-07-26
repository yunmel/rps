package com.yunmel.db.common;

import com.yunmel.db.common.impl.ActiveRecordImpl;
import com.yunmel.db.common.impl.DefaultConnectionFactory;

public class CommonDaoFactory {
  
  public static IActiveRecord createActiveRecord(DatasourceConfig config) {
    ConnectionFactory connectionFactory = new DefaultConnectionFactory(config);
    IActiveRecord activeRecord = new ActiveRecordImpl(connectionFactory);
    return activeRecord;
  }
}
