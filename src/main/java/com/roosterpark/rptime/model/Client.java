package com.roosterpark.rptime.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Client 
{
	@Id
	private Long id;
	@Index
	private String name;
	@Index
	private String header;
	@Index
	private String phone;
	@Index
	private Integer startDayOfWeek;
	
	public Client()
	{}
	
	public Client(String name, String header, String phone, Integer startDayOfWeek)
	{
		this.name = name;
		this.header = header;
		this.phone = phone;
		this.startDayOfWeek = startDayOfWeek;
	}
	
	public Long getId() {
		return this.id;
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

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStartDayOfWeek() {
		return startDayOfWeek;
	}

	public void setStartDayOfWeek(Integer startDayOfWeek) {
		this.startDayOfWeek = startDayOfWeek;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Company : {");
		sb.append("  name : " + name + ",");
		sb.append("  header : " + header + ",");
		sb.append("  phone : " + phone + ",");
		sb.append("  startDayOfWeek : " + startDayOfWeek + ",");
		sb.append("}");
		
		return sb.toString();
	}
}
