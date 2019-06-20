package com.rl.ecps.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rl.ecps.model.EbCart;


public interface EbCartService {
	
	

	
	public void addCart(HttpServletRequest request, HttpServletResponse  response, 
			Long skuId, Integer quantity);
	
	public List<EbCart> selectCartList(HttpServletRequest request, HttpServletResponse  response);
	
	public void removeCart(HttpServletRequest request, HttpServletResponse  response, 
			Long skuId);
	
	public void modifyCart(HttpServletRequest request, HttpServletResponse  response, 
			Long skuId, Integer modQuantity);
	
	public void clearCart(HttpServletRequest request, HttpServletResponse  response);
	
	public String validCart(HttpServletRequest request, HttpServletResponse  response);
}
