package com.pkpm.flink.dynamic;

public class User {
	private int id;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private float salary;

	public float getSalary() {
		return this.salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public String toString() {
		return "id=" + id + ",name=" + name + ",salary=" + salary;
	}
}