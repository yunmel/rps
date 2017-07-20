package com.yunmel.rps.model;

import java.io.Serializable;

import com.blade.jdbc.annotation.Table;

@Table(name = "t_core_task", pk = "id")
public class Task implements Serializable{

	private static final long serialVersionUID = 4306340699127590757L;
	
	private Long id;
	private Long project;
	private String label;
	private Long pusher;
	private Integer exp;
	private String result;
	private String describe;
	private Integer classify;
	private Integer process;//完成进度
	private Integer fettle;//状态
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProject() {
		return project;
	}
	public void setProject(Long project) {
		this.project = project;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Long getPusher() {
		return pusher;
	}
	public void setPusher(Long pusher) {
		this.pusher = pusher;
	}
	public Integer getExp() {
		return exp;
	}
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Integer getClassify() {
		return classify;
	}
	public void setClassify(Integer classify) {
		this.classify = classify;
	}
	public Integer getProcess() {
		return process;
	}
	public void setProcess(Integer process) {
		this.process = process;
	}
	public Integer getFettle() {
		return fettle;
	}
	public void setFettle(Integer fettle) {
		this.fettle = fettle;
	}
	
	
}
