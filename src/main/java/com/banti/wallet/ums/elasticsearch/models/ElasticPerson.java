package com.banti.wallet.ums.elasticsearch.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName="elastic_person", shards=1)
public class ElasticPerson {
	
		@Id
		private Long userId;
		@Field(type = FieldType.Text, name = "userNname")
		private String userName;
	    @Field(type = FieldType.Text, name = "firstName") 
		private String firstName;
	    @Field(type = FieldType.Text, name = "lastName") 
		private String lastName;
	    @Field(type = FieldType.Text, name = "address")
		private String address;
	    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", name="registerDate")
		private Date registerDate;
	    @Field(type = FieldType.Text, name = "email") 
		private String email;
	    @Field(type = FieldType.Text, name = "mobileNo")
	    private String mobileNo;
	    @Field(type = FieldType.Text, name = "status")
		private String status;
	    @Field(type = FieldType.Text, name = "password")
		private String password;
		
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public Date getRegisterDate() {
			return registerDate;
		}
		public void setRegisterDate(Date registerDate) {
			this.registerDate = registerDate;
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
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
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
		
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}	
		
		@Override
		public String toString() {
			return "User [userId=" + userId + ", userName=" + userName + ", firstName=" + firstName + ", lastName="
					+ lastName + ", address=" + address + ", registerDate=" + registerDate + ", email=" + email+", password="+password
					+ ", mobileNo=" + mobileNo + ", status=" + status + "]";
		}
	
		
}
