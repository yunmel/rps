/*
 * Copyright ©2016 四川云麦尔科技 Inc. All rights reserved.
 */
package com.yunmel.db.orm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * TODO : 该类的描述信息
 * 
 * @author xuyq(pazsolr@gmail.com)
 * @time 2017年7月26日 - 下午4:21:34
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class Model<M extends Model> implements Serializable {
  private static final long serialVersionUID = 2579389571151035834L;
  private Map<String, Object> attrs = new HashMap<>();

  /**
   * Put key value pair to the model without check attribute name.
   */
  public M put(String key, Object value) {
    /*
     * if (checkPutKey) { Table table = getTable(); // table 为 null 时用于未启动 ActiveRecordPlugin 的场景 if
     * (table != null && table.hasColumnLabel(key)) { throw new
     * ActiveRecordException("The key can not be attribute name: \"" + key +
     * "\", using set(String, Object) for attribute value"); } }
     */
    attrs.put(key, value);
    return (M) this;
  }

  /**
   * Put map to the model without check attribute name.
   */
  public M put(Map<String, Object> map) {
    attrs.putAll(map);
    return (M) this;
  }

  /**
   * Put other model to the model without check attribute name.
   */
  public M put(Model model) {
    attrs.putAll(model.getAttrs());
    return (M) this;
  }

  /**
   * Get attribute of any mysql type
   */
  public <T> T get(String attr) {
    return (T) (attrs.get(attr));
  }

  /**
   * Get attribute of any mysql type. Returns defaultValue if null.
   */
  public <T> T get(String attr, Object defaultValue) {
    Object result = attrs.get(attr);
    return (T) (result != null ? result : defaultValue);
  }

  /**
   * Get attribute of mysql type: varchar, char, enum, set, text, tinytext, mediumtext, longtext
   */
  public String getString(String attr) {
    return (String) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: int, integer, tinyint(n) n > 1, smallint, mediumint
   */
  public Integer getInteger(String attr) {
    return (Integer) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: bigint, unsign int
   */
  public Long getLong(String attr) {
    return (Long) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: unsigned bigint
   */
  public java.math.BigInteger getBigInteger(String attr) {
    return (java.math.BigInteger) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: date, year
   */
  public java.util.Date getDate(String attr) {
    return (java.util.Date) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: time
   */
  public java.sql.Time getTime(String attr) {
    return (java.sql.Time) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: timestamp, datetime
   */
  public java.sql.Timestamp getTimestamp(String attr) {
    return (java.sql.Timestamp) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: real, double
   */
  public Double getDouble(String attr) {
    return (Double) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: float
   */
  public Float getFloat(String attr) {
    return (Float) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: bit, tinyint(1)
   */
  public Boolean getBoolean(String attr) {
    return (Boolean) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: decimal, numeric
   */
  public java.math.BigDecimal getBigDecimal(String attr) {
    return (java.math.BigDecimal) attrs.get(attr);
  }

  /**
   * Get attribute of mysql type: binary, varbinary, tinyblob, blob, mediumblob, longblob
   */
  public byte[] getBytes(String attr) {
    return (byte[]) attrs.get(attr);
  }

  /**
   * Get attribute of any type that extends from Number
   */
  public Number getNumber(String attr) {
    return (Number) attrs.get(attr);
  }

  /**
   * Return attribute Map.
   * <p>
   * Danger! The update method will ignore the attribute if you change it directly. You must use set
   * method to change attribute that update method can handle it.
   */
  protected Map<String, Object> getAttrs() {
    return attrs;
  }

  /**
   * Return attribute Set.
   */
  public Set<Entry<String, Object>> getAttrsEntrySet() {
    return attrs.entrySet();
  }

  public M set(String attr, Object value) {
    attrs.put(attr, value);
    return (M) this;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer buff = new StringBuffer(this.getClass().getName());
    buff.append(":[");
    for(Entry<String, Object> entry : getAttrsEntrySet()) {
      buff.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
    }
    buff.deleteCharAt(buff.length() - 1);
    buff.append("]");
    return buff.toString();
  }
}
