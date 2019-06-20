package com.rl.ecps.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.exception.EbStockException;
import com.rl.ecps.model.EbCart;
import com.rl.ecps.model.EbOrder;
import com.rl.ecps.model.EbOrderDetail;
import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.model.TsPtlUser;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.service.EbOrderService;
import com.rl.ecps.service.EbShipAddrService;

@Controller
@RequestMapping("/order")
public class EbOrderController {

	@Autowired
	private EbCartService cartService;
	
	@Autowired
	private EbShipAddrService addrService;
	
	@Autowired
	private EbOrderService orderService;
	
	@RequestMapping("/toSubmitOrder.do")
	public String toSubmitOrder(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response){
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		//收货地址查询
		List<EbShipAddr> addrList = addrService.selectAddrByUserIdWithRedis(user.getPtlUserId());
		model.addAttribute("addrList", addrList);
		
		//购物车的数据查询
		List<EbCart> cartList = cartService.selectCartList(request, response);
		Integer itemNum = 0;
		BigDecimal totalPrice =  new BigDecimal(0);
		for(EbCart cart : cartList){
			itemNum = itemNum + cart.getQuantity();
			totalPrice = totalPrice.add(cart.getSku().getSkuPrice().multiply(new BigDecimal(cart.getQuantity())));
		}
		model.addAttribute("cartList", cartList);
		model.addAttribute("itemNum", itemNum);
		model.addAttribute("totalPrice", totalPrice);
		return "shop/confirmProductCase";
	}
	
	@RequestMapping("/submitOrder.do")
	public String submitOrder(EbOrder order, HttpServletRequest request, 
			HttpServletResponse response, Model model, HttpSession session, String address) throws Exception{
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		
		order.setUsername(user.getUsername());
		order.setOrderNum(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		if(!StringUtils.equals(address, "add")){
			//根据用户id和地址id在redis中查找到一条地址对象
			EbShipAddr addr = addrService.selectAddrByIdWithRedis(user.getPtlUserId(), new Long(address));
			BeanUtils.copyProperties(order, addr);
		}
		order.setPtlUserId(user.getPtlUserId());
		//查询购物车的数据来创建订单的明细
		List<EbCart> cartList = cartService.selectCartList(request, response);
		List<EbOrderDetail> detailList = new ArrayList<EbOrderDetail>();
		for(EbCart cart : cartList){
			EbOrderDetail detail = new EbOrderDetail();
			detail.setItemId(cart.getSku().getItem().getItemId());
			detail.setItemName(cart.getSku().getItem().getItemName());
			detail.setItemNo(cart.getSku().getItem().getItemNo());
			detail.setSkuId(cart.getSkuId());
			String specVals = "";
			for(EbSpecValue spc : cart.getSku().getSpecList()){
				specVals = specVals + spc.getSpecValue()+",";
			}
			specVals = specVals.substring(0, specVals.length() - 1);
			detail.setSkuSpec(specVals);
			detail.setMarketPrice(cart.getSku().getMarketPrice());
			detail.setSkuPrice(cart.getSku().getSkuPrice());
			detail.setQuantity(cart.getQuantity());
			detailList.add(detail);
		}
		
		try {
			String processInstanceId = orderService.saveOrder(order, detailList, request, response);
			model.addAttribute("order", order);
			session.setAttribute("processInstanceId", processInstanceId);
			session.setAttribute("orderId", order.getOrderId());
		} catch (Exception e) {
			if(e instanceof EbStockException){
				model.addAttribute("tip", "stock_error");
			}
			/*else if (e instanceof EbCurrException){
				orderService.saveOrder(order, detailList);
			}*/
			e.printStackTrace();
		}
		return "shop/confirmProductCase2";
	}
	
	/**
	 * 用户支付
	 * @param session
	 * @param out
	 */
	@RequestMapping("/payOrder.do")
	public void payOrder(HttpSession session, PrintWriter out){
		String processInstanceId = (String) session.getAttribute("processInstanceId");
		Long orderId = (Long) session.getAttribute("orderId");
		orderService.updatePayOrder(processInstanceId, orderId);
		out.write("success");
	}
	
}
