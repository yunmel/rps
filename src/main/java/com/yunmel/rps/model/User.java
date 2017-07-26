package com.yunmel.rps.model;

import java.util.Date;

import com.yunmel.db.anno.Table;
import com.yunmel.db.orm.Model;

@SuppressWarnings({"unused"})
@Table(name = "t_base_user")
public class User extends Model<User> {
  private static final long serialVersionUID = 2402641172009434025L;
  private Long id;
  private String username;
  private String password;
  private String name;
  private String email;
  private Integer level;
  private Integer score;
  private Date startDate;

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

  public String getUsername() {
    return this.getString("username");
  }

  public void setUsername(String username) {
    this.set("username", username);
  }

  public String getPassword() {
    return this.getString("password");
  }

  public void setPassword(String password) {
    this.set("password", password);
  }

  public String getName() {
    return this.getString("name");
  }

  public void setName(String name) {
    this.set("name", name);
  }

  public String getEmail() {
    return this.getString("email");
  }

  public void setEmail(String email) {
    this.set("email", email);
  }

  public Integer getLevel() {
    return this.getInteger("level");
  }

  public void setLevel(Integer level) {
    this.set("level", level);
  }

  public Integer getScore() {
    return this.getInteger("score");
  }

  public void setScore(Integer score) {
    this.set("score", score);
  }

  public Date getStartDate() {
    return this.getDate("startDate");
  }

  public void setStartDate(Date startDate) {
    this.set("startDate", startDate);
  }
  
  public void init() {
    this.setId(id);
    this.setLevel(level);
    this.setEmail(email);
    this.setName(name);
    this.setPassword(password);
    this.setScore(score);
    this.setStartDate(startDate);
    this.setUsername(username);
  }
}
