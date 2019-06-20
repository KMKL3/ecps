package com.rl.ecps.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbCart;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.util.ECPSUtils;

@Controller
@RequestMapping("/cart")
public class EbCartController {

	@Autowired
	private EbCartService cartService;
	
	@RequestMapping("/addCart.do")
	public String addCart(HttpServletRequest request, HttpServletResponse response
			, Long skuId, Integer quantity){
		cartService.addCart(request, response, skuId, quantity);
		return "redirect:listCart.do";
	}
	
	/**
	 * 查询购物车
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/listCart.do")
	public String listCart(HttpServletRequest request, HttpServletResponse response, Model model){
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
		return "shop/car";
	}
	
	@RequestMapping("/modifyCart.do")
	public String modifyCart(HttpServletRequest request, HttpServletResponse response, Long skuId, Integer modQuantity,  Model model){
		cartService.modifyCart(request, response, skuId, modQuantity);
		return "redirect:listCart.do";
	}
	@RequestMapping("/deleteCart.do")
	public String deleteCart(HttpServletRequest request, HttpServletResponse response, Long skuId,  Model model){
		cartService.removeCart(request, response, skuId);
		return "redirect:listCart.do";
	}
	
	
	@RequestMapping("/validCart.do")
	public void validCart(HttpServletRequest request, HttpServletResponse response){
		String result = cartService.validCart(request, response);
		ECPSUtils.printAjax(response, result);
	}
	
	
}
