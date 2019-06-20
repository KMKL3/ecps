package com.rl.ecps.service.impl;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.model.EbCart;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.service.EbSkuService;
import com.rl.ecps.util.ECPSUtils;

@Service
public class EbCartServiceImpl implements EbCartService {

	@Autowired
	private EbSkuService  skuService;
	
	public void addCart(HttpServletRequest request,
			HttpServletResponse response, Long skuId, Integer quantity) {
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把json数组转换成java对象
		JsonConfig jc = new JsonConfig();
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		//获得当前项目的所有cookie
		Cookie [] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie 的key
				String cookieName = cookie.getName();
				if(StringUtils.equals(cookieName, ECPSUtils.readProp("cart_key"))){
					//获得购物车cookie对应的值
					String  cookieVal = cookie.getValue();
					//解码[{skuId:1001, quantity:2}, {skuId:1002, quantity:3},.......]
					cookieVal = URLDecoder.decode(cookieVal);
					//把字符串转换成json数组
					JSONArray ja = JSONArray.fromObject(cookieVal);
					
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					boolean isExist = false;
					for(EbCart cart : cartList){
						if(cart.getSkuId().longValue() == skuId.longValue()){
							cart.setQuantity(cart.getQuantity()+quantity);
							isExist = true;
							break;
						}
					}
					if(!isExist){
						EbCart cart = new EbCart();
						cart.setSkuId(skuId);
						cart.setQuantity(quantity);
						cartList.add(cart);
					}
				}else{
					EbCart cart = new EbCart();
					cart.setSkuId(skuId);
					cart.setQuantity(quantity);
					cartList.add(cart);
				}
			}
		}
		
		JSONArray ja = JSONArray.fromObject(cartList, jc);
		String result = ja.toString();
		result = URLEncoder.encode(result);
		Cookie cookie = new Cookie("cookie_cart_key", result);
		cookie.setPath("/");
		cookie.setMaxAge(Integer.MAX_VALUE);
		response.addCookie(cookie);
	}

	public List<EbCart> selectCartList(HttpServletRequest request,
			HttpServletResponse response) {
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把json数组转换成java对象
		JsonConfig jc = new JsonConfig();
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		//获得当前项目的所有cookie
		Cookie [] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie 的key
				String cookieName = cookie.getName();
				if(StringUtils.equals(cookieName, ECPSUtils.readProp("cart_key"))){
					//获得购物车cookie对应的值
					String  cookieVal = cookie.getValue();
					//解码[{skuId:1001, quantity:2}, {skuId:1002, quantity:3},.......]
					cookieVal = URLDecoder.decode(cookieVal);
					//把字符串转换成json数组
					JSONArray ja = JSONArray.fromObject(cookieVal);
					
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					for(EbCart cart : cartList){
						EbSku sku = skuService.getSkuDetailByIdWithRedis(cart.getSkuId());
						cart.setSku(sku);
					}
				}
			}
		}
		return cartList;
	}

	public void removeCart(HttpServletRequest request,
			HttpServletResponse response, Long skuId) {
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把json数组转换成java对象
		JsonConfig jc = new JsonConfig();
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		//获得当前项目的所有cookie
		Cookie [] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie 的key
				String cookieName = cookie.getName();
				if(StringUtils.equals(cookieName, ECPSUtils.readProp("cart_key"))){
					//获得购物车cookie对应的值
					String  cookieVal = cookie.getValue();
					//解码[{skuId:1001, quantity:2}, {skuId:1002, quantity:3},.......]
					cookieVal = URLDecoder.decode(cookieVal);
					//把字符串转换成json数组
					JSONArray ja = JSONArray.fromObject(cookieVal);
					
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					
					for(int i = 0; i < cartList.size(); i++){
						EbCart cart = cartList.get(i);
						if(cart.getSkuId().longValue() == skuId.longValue()){
							cartList.remove(cart);
						}
					}
				}
			}
		}
		JSONArray ja = JSONArray.fromObject(cartList, jc);
		String result = ja.toString();
		result = URLEncoder.encode(result);
		Cookie cookie = new Cookie("cookie_cart_key", result);
		cookie.setPath("/");
		cookie.setMaxAge(Integer.MAX_VALUE);
		response.addCookie(cookie);
	}

	public void modifyCart(HttpServletRequest request,
			HttpServletResponse response, Long skuId, Integer modQuantity) {
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把json数组转换成java对象
		JsonConfig jc = new JsonConfig();
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		//获得当前项目的所有cookie
		Cookie [] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie 的key
				String cookieName = cookie.getName();
				if(StringUtils.equals(cookieName, ECPSUtils.readProp("cart_key"))){
					//获得购物车cookie对应的值
					String  cookieVal = cookie.getValue();
					//解码[{skuId:1001, quantity:2}, {skuId:1002, quantity:3},.......]
					cookieVal = URLDecoder.decode(cookieVal);
					//把字符串转换成json数组
					JSONArray ja = JSONArray.fromObject(cookieVal);
					
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					for(EbCart cart : cartList){
						if(cart.getSkuId().longValue() == skuId.longValue()){
							cart.setQuantity(modQuantity);
						}
					}
				}
			}
		}
		JSONArray ja = JSONArray.fromObject(cartList, jc);
		String result = ja.toString();
		result = URLEncoder.encode(result);
		Cookie cookie = new Cookie("cookie_cart_key", result);
		cookie.setPath("/");
		cookie.setMaxAge(Integer.MAX_VALUE);
		response.addCookie(cookie);
		
	}

	public void clearCart(HttpServletRequest request,
			HttpServletResponse response) {
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把json数组转换成java对象
		JsonConfig jc = new JsonConfig();
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		//获得当前项目的所有cookie
		Cookie [] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie 的key
				String cookieName = cookie.getName();
				if(StringUtils.equals(cookieName, ECPSUtils.readProp("cart_key"))){
					//获得购物车cookie对应的值
					String  cookieVal = cookie.getValue();
					//解码[{skuId:1001, quantity:2}, {skuId:1002, quantity:3},.......]
					cookieVal = URLDecoder.decode(cookieVal);
					//把字符串转换成json数组
					JSONArray ja = JSONArray.fromObject(cookieVal);
					
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					cartList.clear();
					
				}
			}
		}
		JSONArray ja = JSONArray.fromObject(cartList, jc);
		String result = ja.toString();
		result = URLEncoder.encode(result);
		Cookie cookie = new Cookie("cookie_cart_key", result);
		cookie.setPath("/");
		cookie.setMaxAge(Integer.MAX_VALUE);
		response.addCookie(cookie);
		
	}

	public String validCart(HttpServletRequest request,
			HttpServletResponse response) {
		String result = "success";
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把json数组转换成java对象
		JsonConfig jc = new JsonConfig();
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		//获得当前项目的所有cookie
		Cookie [] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie 的key
				String cookieName = cookie.getName();
				if(StringUtils.equals(cookieName, ECPSUtils.readProp("cart_key"))){
					//获得购物车cookie对应的值
					String  cookieVal = cookie.getValue();
					//解码[{skuId:1001, quantity:2}, {skuId:1002, quantity:3},.......]
					cookieVal = URLDecoder.decode(cookieVal);
					//把字符串转换成json数组
					JSONArray ja = JSONArray.fromObject(cookieVal);
					
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					for(EbCart cart : cartList){
						EbSku sku = skuService.getSkuDetailByIdWithRedis(cart.getSkuId());
						if(sku.getStockInventory() < cart.getQuantity()){
							result = sku.getItem().getItemName();
							for(EbSpecValue spec : sku.getSpecList()){
								result = result + spec.getSpecValue();
							}
							result = result + "库存不足"+cart.getQuantity()+"个"+"实际库存是"+sku.getStockInventory()+"个";
							break;
						}
					}
				}
			}
		}
		
		
		return result;
	}

	
	
	
}
