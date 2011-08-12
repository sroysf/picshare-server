package com.codechronicle.messaging;

import java.io.Serializable;

public class AsyncMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int age;
	
	public AsyncMessage() {
	}
	
	public AsyncMessage(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		return name + " : " + age;
	}
	
}
