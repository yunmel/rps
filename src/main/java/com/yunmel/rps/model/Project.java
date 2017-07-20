package com.yunmel.rps.model;

import java.io.Serializable;
import java.util.Date;

import com.yunmel.rps.config.annotation.Table;

@Table(name = "t_core_project", pk = "id")
public class Project implements Serializable{
	private static final long serialVersionUID = -6363421458919048278L;
	private Long id;
	private String name;
	private String sn;
	private String describe;
	private Long mngr;
	private Date start;
	private Date end;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public Long getMngr() {
		return mngr;
	}
	public void setMngr(Long mngr) {
		this.mngr = mngr;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	
}
