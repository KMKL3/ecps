package com.rl.ecps.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbOrderDao;
import com.rl.ecps.dao.EbOrderDetailDao;
import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.exception.EbStockException;
import com.rl.ecps.model.EbOrder;
import com.rl.ecps.model.EbOrderDetail;
import com.rl.ecps.model.TaskBean;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.service.EbOrderFlowService;
import com.rl.ecps.service.EbOrderService;

@Service
public class EbOrderServiceImpl implements EbOrderService {

	
	@Autowired
	private EbSkuDao skuDao;
	
	@Autowired
	private EbOrderDao orderDao;
	
	@Autowired
	private EbOrderDetailDao orderDetailDao;
	
	@Autowired
	private EbCartService cartService;
	
	@Autowired
	private EbOrderFlowService flowService;

	public String saveOrder(EbOrder order, List<EbOrderDetail> detailList,HttpServletRequest request, HttpServletResponse response) {
		orderDao.saveOrder(order);
		Map<String,Object> map = new HashMap<String,Object>();
		for(EbOrderDetail detail: detailList){
			detail.setOrderId(order.getOrderId());
			orderDetailDao.saveOrderDetail(detail);
			map.put("skuId", detail.getSkuId());
			map.put("quantity", detail.getQuantity());
			int flag = skuDao.updateStock(map);
			if(flag == 0){
				throw new EbStockException();
			}
			skuDao.updateStockRedis(map);
			//扣减库存
			/**
			EbSku sku = skuDao.getSkuById(detail.getSkuId());
			sku.setStockInventory(sku.getStockInventory() - detail.getQuantity());
			int flag = skuDao.update(sku);
			if(flag == 0){
				EbSku sku = skuDao.getSkuById(detail.getSkuId());
				if(sku.getStock() < detail.getQuantity()){
					throw EbStockException();
				}else{
					throw EbCurrException();
				}
		
			}
			**/
		}
		
		cartService.clearCart(request, response);
		String processInstanceId = flowService.startInstance(order.getOrderId());
		return processInstanceId;
	}

	public void updatePayOrder(String processInstanceId, Long orderId) {
		EbOrder order = new EbOrder();
		order.setOrderId(orderId);
		order.setIsPaid((short)1);
		orderDao.updateOrder(order);
		flowService.completeTaskByPid(processInstanceId, "付款");
	}

	public List<TaskBean> selectTaskBeanByAssignee(String assignee, Short isCall) {
		List<TaskBean> tbList = flowService.selectTaskByAssignee(assignee);
		List<TaskBean> tbList1 = new ArrayList<TaskBean>();
		for(TaskBean tb : tbList){
			String businessKey = tb.getBusinessKey();
			EbOrder order = orderDao.getOrderById(new Long(businessKey));
			if(order.getIsCall().shortValue() == isCall.shortValue()){
				tb.setOrder(order);
				tbList1.add(tb);
			}
		}
		
		return tbList1;
	}

	public TaskBean selectTBOrderDetail(Long orderId, String taskId) {
		EbOrder order = orderDao.selectOrderDetailById(orderId);
		TaskBean tb = flowService.selectTaskBeanByTaskId(taskId);
		tb.setOrder(order);
		return tb;
	}

	public void completeCall(Long orderId) {

		EbOrder order = new EbOrder();
		order.setOrderId(orderId);
		order.setIsCall((short)1);
		orderDao.updateOrder(order);
	}

	public List<TaskBean> selectTaskBeanByAssignee(String assignee) {
		List<TaskBean> tbList = flowService.selectTaskByAssignee(assignee);
		for(TaskBean tb : tbList){
			String businessKey = tb.getBusinessKey();
			EbOrder order = orderDao.getOrderById(new Long(businessKey));
			tb.setOrder(order);
		}
		return tbList;
	}

	public void updateCompeteTask(String taskId, Long orderId, String outcome) {
		EbOrder order = new EbOrder();
		order.setOrderId(orderId);
		order.setUpdateTime(new Date());
		orderDao.updateOrder(order);
		flowService.completeTaskByTId(taskId, outcome);
	}
	
	
	
	
}
