package com.rl.ecps.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbBrand;
import com.rl.ecps.model.EbFeature;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.service.EbBrandService;
import com.rl.ecps.service.EbFeatureService;
import com.rl.ecps.service.EbItemService;
import com.rl.ecps.service.EbSkuService;
import com.rl.ecps.util.ECPSUtils;

@Controller
@RequestMapping("/item")
public class EbItemController {

	@Autowired
	private EbBrandService brandService;
	
	@Autowired
	private EbFeatureService featureService;
	
	@Autowired
	private EbItemService itemService;
	
	@Autowired
	private EbSkuService skuService;
	
	@RequestMapping("/toIndex.do")
	public String toIndex(Model model){
		//查询品牌
		List<EbBrand> bList = brandService.selectBrandAll();
		model.addAttribute("bList", bList);
		//查询筛选属性
		List<EbFeature> fList = featureService.selectIsSelectFeature();
		model.addAttribute("fList", fList);
		return "index";
	}
	
	/**
	 * 
	 * @param price   1000-1999
	 * @param brandId	1003
	 * @param keyWords	S6
	 * @param paraVals  直板,IOS
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/listItem.do")
	public String listItem(String price, Long brandId, 
			String keyWords, String paraVals, Model model) throws Exception{
		List<EbItem> itemList = itemService.selectItemByIndex(price, brandId, keyWords, paraVals);
		model.addAttribute("itemList", itemList);
		
		return "phoneClassification";
	}
	
	/**
	 * 查询商品的详细信息
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping("/viewItemDetail.do")
	public String viewItemDetail(Long itemId, Model model){
		EbItem item = itemService.selectItemDetailById(itemId);
		model.addAttribute("item", item);
		return "productDetail";
	}
	
	@RequestMapping("/getSkuById.do")
	public void getSkuById(Long skuId, HttpServletResponse response){
		//EbSku sku = skuService.getSkuById(skuId);
		EbSku sku = skuService.getSkuByIdWithRedis(skuId);
		JSONObject jo = new JSONObject();
		jo.accumulate("sku", sku);
		String result = jo.toString();
		ECPSUtils.printAjax(response, result);
	}
}
