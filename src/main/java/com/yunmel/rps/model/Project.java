package com.yunmel.rps.model;

import java.util.Date;

import com.yunmel.db.anno.Table;
import com.yunmel.db.orm.Model;

/**
 * Project实体类
 * 
 * @author yunmel
 */
@SuppressWarnings({"unused"})
@Table(name = "t_core_project")
public class Project extends Model<Project> {
  private static final long serialVersionUID = 8125334082625168044L;
  private Long id;
  private String name;
  private String sn;
  private String describe;
  private Long mngr;
  private Date start;
  private Date end;

  /**
   * @return the id
   */
  public Long getId() {
    return this.getLong("id");
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.set("id", id);
  }

  public String getName() {
    return this.getString("name");
  }

  public void setName(String name) {
    this.set("name", name);
  }

  public String getSn() {
    return this.getString("sn");
  }

  public void setSn(String sn) {
    this.set("sn", sn);
  }

  public String getDescribe() {
    return this.getString("describe");
  }

  public void setDescribe(String describe) {
    this.set("describe", describe);
  }

  public Long getMngr() {
    return this.getLong("mngr");
  }

  public void setMngr(Long mngr) {
    this.set("mngr", mngr);
  }

  public Date getStart() {
    return this.getDate("start");
  }

  public void setStart(Date start) {
    this.set("start", start);
  }

  public Date getEnd() {
    return this.getDate("end");
  }

  public void setEnd(Date end) {
    this.set("end", end);
  }
}
