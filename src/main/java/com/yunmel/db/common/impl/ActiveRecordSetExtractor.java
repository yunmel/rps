/**
 * Copyright 2010-2016 the original author or authors.
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.yunmel.db.common.impl;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.yunmel.db.common.ConverterFactory;
import com.yunmel.db.common.ResultSetExtractor;
import com.yunmel.db.orm.Model;
import com.yunmel.db.utils.StringUtils;

public class ActiveRecordSetExtractor<T> implements ResultSetExtractor<List<T>> {
  private Class<T> cls = null;
  private int offset = 0;
  private int max = Integer.MAX_VALUE;

  /**
   * @param objectReader
   * @param cls
   */
  public ActiveRecordSetExtractor( Class<T> cls) {
    super();
    this.cls = cls;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.uorm.dao.common.ResultSetExtractor#extractData(java.sql.ResultSet)
   */
  @Override
  public List<T> extractData(ResultSet rs) throws SQLException {
    List<T> results = new ArrayList<T>();
    ResultSetMetaData rsmd = rs.getMetaData();
    int pos = 0;
    while (rs.next()) {
      if (pos >= offset) {
        try {
          results.add(buildInstance(cls, rs, rsmd));
        } catch (Exception e) {
          throw new SQLException(e);
        }
      }
      if (results.size() >= max) {
        break;
      }
      pos++;
    }
    return results;
  }

  
  @SuppressWarnings({"rawtypes", "unchecked"})
  private T buildInstance(Class<T> cls, ResultSet rs, ResultSetMetaData rsmd) throws Exception {
    int count = rsmd.getColumnCount();
    if (cls.getSuperclass().getName().equals("com.yunmel.db.orm.Model")) {
      T instance = cls.newInstance();
      Model ins = (Model) instance;
      for (int i = 1; i <= count; i++) {
        String columnName = rsmd.getColumnLabel(i);// rsmd.getColumnName(i);
        if (null == columnName || 0 == columnName.length()) {
          columnName = rsmd.getColumnName(i);
        }
        Object val = rs.getObject(i);
        ins.set(StringUtils.getCamelName(columnName), val);
      }
      return (T) ins;
    } else {
      if (count != 1) {
        throw new RuntimeException("你想要干什么!!!");
      }
      Object val = rs.getObject(1);
      if (val == null) {
        return null;
      } else {
        if (ConverterFactory.getInstance().needConvert(val.getClass(), cls)) {
          try {
            return (T) getValue(rs, 1, cls);
          } catch (SQLException e) {
            throw e;
          }
        } else {
          return (T) val;
        }
      }
    }
  }

  
  protected Object getValue(ResultSet result, int columnIndex, Class<?> memberType) throws SQLException {
    if(Array.class.equals(memberType))         return result.getArray(columnIndex);
    if((Byte[]       .class.equals(memberType)) || (byte[].class.equals(memberType)))   return result.getBytes(columnIndex);
    if((Boolean      .class.equals(memberType)) || (boolean.class.equals(memberType)))  return result.getBoolean(columnIndex);
    if((Byte         .class.equals(memberType)) || (byte.class.equals(memberType)))     return result.getByte(columnIndex);
    if((Double       .class.equals(memberType)) || (double.class.equals(memberType)))   return result.getDouble(columnIndex);
    if((Float        .class.equals(memberType)) || (float.class.equals(memberType)))    return result.getFloat(columnIndex);
    if((Integer      .class.equals(memberType)) || (int.class.equals(memberType)))      return result.getInt(columnIndex);
    if((Long         .class.equals(memberType)) || (long.class.equals(memberType)))     return result.getLong(columnIndex);
    if((Short        .class.equals(memberType)) || (short.class.equals(memberType)))    return result.getShort(columnIndex);

    if(BigInteger   .class.equals(memberType)) {
        BigDecimal val = result.getBigDecimal(columnIndex);
        if(val != null){
            return val.toBigInteger();
        }else{
            return null;
        }
    }
    if(BigDecimal   .class.equals(memberType)) return result.getBigDecimal(columnIndex);
    if(InputStream  .class.equals(memberType)) return result.getBinaryStream(columnIndex);
    if(Blob         .class.equals(memberType)) return result.getBlob(columnIndex);
    if(Reader       .class.equals(memberType)) return result.getCharacterStream(columnIndex);
    if(Clob         .class.equals(memberType)) return result.getClob(columnIndex);
    if(java.sql.Date.class.equals(memberType)) return result.getDate(columnIndex);
    if(java.util.Date.class.equals(memberType)) return result.getTimestamp(columnIndex);
    if(Ref          .class.equals(memberType)) return result.getRef(columnIndex);
    if(String       .class.equals(memberType)) return result.getString(columnIndex);
    if(Time         .class.equals(memberType)) return result.getTime(columnIndex);
    if(Timestamp    .class.equals(memberType)) return result.getTimestamp(columnIndex);
    if(URL          .class.equals(memberType)) return result.getURL(columnIndex);
    if(Object       .class.equals(memberType)) return result.getObject(columnIndex);
    if(SQLXML       .class.equals(memberType)) return result.getSQLXML(columnIndex);
    return result.getObject(columnIndex);
  }
  
  /**
   * @return the offset
   */
  public int getOffset() {
    return offset;
  }

  /**
   * @param offset the offset to set
   */
  public void setOffset(int offset) {
    this.offset = offset;
  }

  /**
   * @return the max
   */
  public int getMax() {
    return max;
  }

  /**
   * @param max the max to set
   */
  public void setMax(int max) {
    this.max = max;
  }

}
