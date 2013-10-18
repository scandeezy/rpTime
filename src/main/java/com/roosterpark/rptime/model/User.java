package com.roosterpark.rptime.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class User {
	@Id
	private String email;
	@Index
	private String firstName;
	@Index
	private String lastName;
	@Index
	private Boolean active;
	@Index
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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
