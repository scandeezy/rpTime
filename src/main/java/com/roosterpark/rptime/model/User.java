package com.roosterpark.rptime.model;

import java.util.Date;

public class User {
	private String firstName;
	private String lastName;
	private String email;
	private Date start;
	
	public User() {}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("User : {");
		sb.append("  First : " + firstName + ",");
		sb.append("  Last : " + lastName + ",");
		sb.append("  Email : " + email + ",");
		sb.append("  Start : " + start + ",");
		sb.append("}");
		
		return sb.toString();
	}
}
