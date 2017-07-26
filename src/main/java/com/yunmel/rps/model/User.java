package com.yunmel.rps.model;

import java.security.KeyStore.TrustedCertificateEntry;

import org.antlr.v4.runtime.Parser.TraceListener;

import com.yunmel.db.anno.Column;
import com.yunmel.db.anno.Table;

@Table(name = "t_base_user")
public class User{
  @Column(name="id",primary=true)
  private Long id;
  @Column(name="user_name")
  private String userName;
  @Column(name="password")
  private String password;
  @Column(name="name")
  private String name;
  @Column(name="email")
  private String email;
  @Column(name="level")
  private Integer level;
  @Column(name="score")
  private Integer score;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", name=" + name + ", email="
        + email + ", level=" + level + ", score=" + score + "]";
  }

 

}
