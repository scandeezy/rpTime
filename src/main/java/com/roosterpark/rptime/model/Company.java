package com.roosterpark.rptime.model;

public class Company 
{
	private String name;
	private String header;
	private String phone;
	private Integer startDayOfWeek;
	
	public Company()
	{}
	
	public Company(String name, String header, String phone, Integer startDayOfWeek)
	{
		this.name = name;
		this.header = header;
		this.phone = phone;
		this.startDayOfWeek = startDayOfWeek;
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
