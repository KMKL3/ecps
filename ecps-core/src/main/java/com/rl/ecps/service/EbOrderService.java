package com.rl.ecps.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rl.ecps.model.EbOrder;
import com.rl.ecps.model.EbOrderDetail;
import com.rl.ecps.model.TaskBean;


public interface EbOrderService {
	
	
	public String saveOrder(EbOrder order, List<EbOrderDetail> detailList,HttpServletRequest request, HttpServletResponse response);

	public void updatePayOrder(String processInstanceId, Long orderId);
	
	public List<TaskBean> selectTaskBeanByAssignee(String assignee, Short isCall);
	
	public List<TaskBean> selectTaskBeanByAssignee(String assignee);
	
	public TaskBean selectTBOrderDetail(Long orderId, String taskId);
	
	public void completeCall(Long orderId);
	
	public void updateCompeteTask(String taskId, Long orderId, String outcome);
}
