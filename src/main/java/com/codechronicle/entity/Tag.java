package com.codechronicle.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tag {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String value;

	public Tag() {
		// TODO Auto-generated constructor stub
	}
	
	public Tag(String value) {
		super();
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return getValue();
	}
}