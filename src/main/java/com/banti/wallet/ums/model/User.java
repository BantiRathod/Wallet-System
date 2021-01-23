package com.banti.wallet.ums.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User{
	@Id
	private Integer id;
	private String userName;
	private String fname;
	private String lname;
	private String add1;
	private String add2;
	private String email;
	private String mobileNo;
	
	public User()
	{}
	
	public User(Integer id,String userName, String fname, String lname, 
		String add1, String add2, String email, String mobileNo) {
		this.userName = userName;
		this.fname = fname;
		this.lname = lname;
		this.id=id;
		this.add1 = add1;
		this.add2 = add2;
		this.email = email;
		this.mobileNo = mobileNo;
	}
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	public Integer getId(){
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getAdd1() {
		return add1;
	}
	public void setAdd1(String add1) {
		this.add1 = add1;
	}
	public String getAdd2() {
		return add2;
	}
	public void setAdd2(String add2) {
		this.add2 = add2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
}
