package com.rl.ecps.util;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class OrderFlowTaskListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7821917290950255357L;

	public void notify(DelegateTask delegateTask) {
		//获得任务节点定义的ID
		String taskKey = delegateTask.getTaskDefinitionKey();
		delegateTask.setAssignee(taskKey+"er");
	}

}
