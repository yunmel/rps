package com.yunmel.rps.model;

import java.io.Serializable;

import com.yunmel.rps.config.annotation.Table;

@Table(name = "t_base_user", pk = "id")
public class User implements Serializable{
	private static final long serialVersionUID = -7717096425419679694L;
	private Long id; 
    private String username;
    private String password;
    private String name;
    private String email;
    private Integer level;
    private Integer score;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name
        + ", email=" + email + ", level=" + level + ", score=" + score + "]";
  }
    
}
