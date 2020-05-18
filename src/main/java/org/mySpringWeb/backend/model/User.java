package org.mySpringWeb.backend.model;

public class User {
	private String userID;
	private String userName;
	private Integer age;
	private Integer gender;
	private String introduction;
	
	public String getUserID() {
		return this.userID;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setUserName(String s) {
		this.userName = s;
	}
	
	public Integer getAge() {
		return this.age;
	}
	
	public void setAge(Integer i) {
		this.age = i;
	}
	
	public Integer getGender() {
		return this.gender;
	}
	
	public void setGender(Integer i) {
		this.gender = i;
	}
	
	public String getIntroduction() {
		return this.introduction;
	}
	
	public void setIntroduction(String s) {
		this.introduction = s;
	}

}
