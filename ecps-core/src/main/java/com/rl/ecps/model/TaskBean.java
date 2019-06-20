package com.rl.ecps.model;

import java.util.List;

import org.activiti.engine.task.Task;

public class TaskBean {
	
	private EbOrder order;
	
	private Task task;
	
	private String businessKey;
	
	/**
	 * 做动态按钮
	 */
	private List<String> outcome;
	
	private String income;

	public EbOrder getOrder() {
		return order;
	}

	public void setOrder(EbOrder order) {
		this.order = order;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public List<String> getOutcome() {
		return outcome;
	}

	public void setOutcome(List<String> outcome) {
		this.outcome = outcome;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}
	
	
	
}
