package com.rl.ecps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.TaskBean;
import com.rl.ecps.service.EbOrderService;

@Controller
@RequestMapping("/order")
public class EbOrderController {
	
	@Autowired
	private EbOrderService orderService;
	
	@RequestMapping("/toIndex.do")
	public String toIndex(){
		return "order/index";
	}
	
	@RequestMapping("/listPayOrder.do")
	public String listPayOrder(String assignee, Short isCall, Model model){
		List<TaskBean> tbList = orderService.selectTaskBeanByAssignee(assignee, isCall);
		model.addAttribute("tbList", tbList);
		model.addAttribute("isCall", isCall);
		return "order/orderPay/orderPay";
	}
	
	@RequestMapping("/listTaskByAssignee.do")
	public String listTaskByAssignee(String assignee, Model model, String dirName){
		List<TaskBean> tbList = orderService.selectTaskBeanByAssignee(assignee);
		model.addAttribute("tbList", tbList);
		return "order/"+dirName+"/orderCall";
	}
	
	@RequestMapping("/viewDetail.do")
	public String viewDetail(Long orderId , String taskId, String dirName, Model model){
		TaskBean tb = orderService.selectTBOrderDetail(orderId, taskId);
		model.addAttribute("tb",tb);
		return "order/"+dirName+"/detail";
	}
	
	@RequestMapping("/completeCall.do")
	public String completeCall(Long orderId, Model model){
		orderService.completeCall(orderId);
		return "redirect:listPayOrder.do?assignee=noPaidOrderer&isCall=0";
	}
	
	@RequestMapping("/completeTask.do")
	public String completeTask(String taskId, Long orderId, String outcome){
		orderService.updateCompeteTask(taskId, orderId, outcome);
		return "redirect:listTaskByAssignee.do?assignee=paidOrderer&dirName=orderCall";
	}
	
	
}
