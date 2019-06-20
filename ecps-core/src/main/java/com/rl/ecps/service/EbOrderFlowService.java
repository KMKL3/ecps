package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.TaskBean;

public interface EbOrderFlowService {
	
	public void deployOrderFlow();
	
	public String startInstance(Long orderId);
	
	public void completeTaskByPid(String processInstanceId, String outcome);
	
	public List<TaskBean> selectTaskByAssignee(String assignee);
	
	public TaskBean selectTaskBeanByTaskId(String taskId);
	
	public void completeTaskByTId(String taskId, String outcome);

}
