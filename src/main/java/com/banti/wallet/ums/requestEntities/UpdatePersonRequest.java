package com.banti.wallet.ums.requestEntities;


public class UpdatePersonRequest {

	private String userName;
	private String firstName;
	private String lastName;
	private String address; 
	private String password;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "UpdatePersonRequest [userName=" + userName + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", address=" + address + ", password=" + password
				+ "]";
	}
	
}
