package com.yunmel.rps.model;

import com.yunmel.db.anno.Table;
import com.yunmel.db.orm.Model;

/**
 * Task实体类
 * 
 * @author yunmel
 */
@SuppressWarnings({"unused"})
@Table(name = "t_core_task")
public class Task extends Model<Task> {
  private static final long serialVersionUID = 1138633338399224346L;
  private Long id;
  private Long project;
  private String label;
  private Long pusher;
  private Integer exp;
  private String result;
  private String description;
  private Integer classify;
  private Integer process;
  private Integer fettle;

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

  public Long getProject() {
    return this.getLong("project");
  }

  public void setProject(Long project) {
    this.set("project", project);
  }

  public String getLabel() {
    return this.getString("label");
  }

  public void setLabel(String label) {
    this.set("label", label);
  }

  public Long getPusher() {
    return this.getLong("pusher");
  }

  public void setPusher(Long pusher) {
    this.set("pusher", pusher);
  }

  public Integer getExp() {
    return this.getInteger("exp");
  }

  public void setExp(Integer exp) {
    this.set("exp", exp);
  }

  public String getResult() {
    return this.getString("result");
  }

  public void setResult(String result) {
    this.set("result", result);
  }

  public String getDescription() {
    return this.getString("description");
  }

  public void setDescription(String description) {
    this.set("description", description);
  }

  public Integer getClassify() {
    return this.getInteger("classify");
  }

  public void setClassify(Integer classify) {
    this.set("classify", classify);
  }

  public Integer getProcess() {
    return this.getInteger("process");
  }

  public void setProcess(Integer process) {
    this.set("process", process);
  }

  public Integer getFettle() {
    return this.getInteger("fettle");
  }

  public void setFettle(Integer fettle) {
    this.set("fettle", fettle);
  }
  
  public void init() {
    this.setId(id);
    this.setDescription(description);
    this.setClassify(classify);
    this.setResult(result);
    this.setLabel(label);
    this.setProject(project);
    this.setPusher(pusher);
    this.setProcess(process);
    this.setFettle(fettle);
  }
}
