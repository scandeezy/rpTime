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
        private boolean lunchRequired;
	@Index
	private Integer startDayOfWeek;
	
	public Client()
	{}
	
	public Client(String name, Boolean lunchRequired, Integer startDayOfWeek)
	{
		this.name = name;
                this.lunchRequired = lunchRequired;
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
        
        public Boolean getLunchRequired() {
            return this.lunchRequired;
        }
        
        public void setLunchRequired(Boolean lunchRequired) {
            this.lunchRequired = lunchRequired;
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
		sb.append("  lunchRequired : " + lunchRequired + ",");
		sb.append("  startDayOfWeek : " + startDayOfWeek + ",");
		sb.append("}");
		
		return sb.toString();
	}
}
