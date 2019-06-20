package com.rl.ecps.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbBrand;
import com.rl.ecps.model.EbFeature;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.model.Page;
import com.rl.ecps.model.QueryCondition;
import com.rl.ecps.service.EbBrandService;
import com.rl.ecps.service.EbFeatureService;
import com.rl.ecps.service.EbItemService;
import com.rl.ecps.util.ECPSUtils;

@Controller
@RequestMapping("/item")
public class EbItemController {
	
	@Autowired
	private EbBrandService brandService;
	
	@Autowired
	private EbItemService itemService;
	
	@Autowired
	private EbFeatureService featureService;
	
	@RequestMapping("/toIndex.do")
	public String toIndex(){
		return "item/index";
	}
	
	/**
	 * 查询品牌
	 * @return
	 */
	@RequestMapping("/listBrand.do")
	public String listBrand(Model model){
		List<EbBrand> bList = brandService.selectBrandAll();
		model.addAttribute("bList", bList);
		return "item/listbrand";
	}
	
	
	/**
	 * 跳转到添加品牌的页面
	 * @return
	 */
	@RequestMapping("/toAddBrand.do")
	public String toAddBrand(){
		return "item/addbrand";
	}
	
	/**
	 * 验证品牌的名称的重复性
	 * @param brandName
	 * @param out
	 */
	@RequestMapping("/validBrandName.do")
	public void validBrandName(String brandName, PrintWriter out){
		List<EbBrand> bList = brandService.selectBrandByName(brandName);
		String flag = "yes";
		if(bList.size() > 0){
			flag = "no";
		}
		out.write(flag);
	}
	
	/**
	 * 品牌添加
	 * @param brand
	 * @return
	 */
	@RequestMapping("/addBrand.do")
	public String addBrand(EbBrand brand){
		brandService.saveBrand(brand);
		return "redirect:listBrand.do";
	}
	
	@RequestMapping("/listItem.do")
	public String listItem(QueryCondition qc, Model model){
		List<EbBrand> bList = brandService.selectBrandAll();
		model.addAttribute("bList", bList);
		if(qc.getPageNo() == null){
			qc.setPageNo(1);
		}
		Page page = itemService.selectItemByQC(qc);
		model.addAttribute("page", page);
		//把qc写回去，目的是回显
		model.addAttribute("qc", qc);
		return "item/list";
	}
	
	@RequestMapping("/toAddItem.do")
	public String toAddItem(Model model){
		//查询品牌
		List<EbBrand> bList = brandService.selectBrandAll();
		model.addAttribute("bList", bList);
		//查询普通属性
		List<EbFeature> commList = featureService.selectCommFeature();
		model.addAttribute("commList", commList);
		//查询出来特殊属性的集合
		List<EbFeature> specList = featureService.selectSpecFeature();
		model.addAttribute("specList", specList);
		return "item/addItem";
	}
	
	@RequestMapping("/addItem.do")
	public String addItem(EbItem item, EbItemClob itemClob, Integer divNum,
			HttpServletRequest request ){
		List<EbParaValue> paraList = new ArrayList<EbParaValue>();
		//生成商品的编号
		item.setItemNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		//从后台来查询普通属性的集合，这个集合正是tab_3中所展示属性
		List<EbFeature> commList = featureService.selectCommFeature();
		for(EbFeature feature: commList){
			//获得属性id（tab3文本域的name）
			Long featureId = feature.getFeatureId();
			if(feature.getInputType() == 3){
				String [] paraVals = request.getParameterValues(featureId+"");
				if(paraVals != null && paraVals.length > 0){
					String paraValues = "";
					for(String paraVal : paraVals){
						paraValues = paraValues + paraVal + ",";
					}
					paraValues = paraValues.substring(0, paraValues.length() -1);
					EbParaValue pv = new EbParaValue();
					pv.setParaValue(paraValues);
					pv.setFeatureId(featureId);
					paraList.add(pv);
				}
				
			}else{
				//获得单选和下拉的值
				String paraVal = request.getParameter(featureId+"");
				if(StringUtils.isNotBlank(paraVal)){
					//创建商品参数值的表的对象
					EbParaValue pv = new EbParaValue();
					pv.setParaValue(paraVal);
					pv.setFeatureId(featureId);
					paraList.add(pv);
				}
			}
		}
		List<EbSku> skuList = new ArrayList<EbSku>();
		List<EbFeature> specList = featureService.selectSpecFeature();
		for(int i = 1; i <=divNum; i++){
			//skuType1   showStatus1  sort1 skuPrice1 marketPrice1 stockInventory1  skuUpperLimit1 sku1  location1 
			String skuPrice = request.getParameter("skuPrice"+i);
			String stockInventory = request.getParameter("stockInventory"+i);
			//判断div没有断档的情况
			if(StringUtils.isNotBlank(skuPrice) && StringUtils.isNotBlank(stockInventory)){
				String skuType = request.getParameter("skuType"+i);
				String showStatus = request.getParameter("showStatus"+i);
				String sort = request.getParameter("sort"+i);
				String marketPrice = request.getParameter("marketPrice"+i);
				String skuUpperLimit = request.getParameter("skuUpperLimit"+i);
				String location = request.getParameter("location"+i);
				String sku = request.getParameter("sku"+i);
				//创建sku对象
				EbSku skuObj = new EbSku();
				//设置对象的值的时候一定要判断
				skuObj.setSkuPrice(new BigDecimal(skuPrice));
				skuObj.setStockInventory(new Integer(stockInventory));
				if(StringUtils.isNotBlank(skuType)){
					skuObj.setSkuType(new Short(skuType));
				}
				if(StringUtils.isNotBlank(showStatus)){
					skuObj.setShowStatus(new Short(showStatus));
				}
				if(StringUtils.isNotBlank(sort)){
					skuObj.setSkuSort(new Integer(sort));
				}
				if(StringUtils.isNotBlank(marketPrice)){
					skuObj.setMarketPrice(new BigDecimal(marketPrice));
				}
				if(StringUtils.isNotBlank(skuUpperLimit)){
					skuObj.setSkuUpperLimit(new Integer(skuUpperLimit));
				}
				skuObj.setLocation(location);
				skuObj.setSku(sku);
				List<EbSpecValue> evList = new ArrayList<EbSpecValue>();
				//遍历特殊属性
				for(EbFeature feature : specList){
					//获得到特殊属性的Id"
					Long featureId = feature.getFeatureId();
					String specVal = request.getParameter(featureId+"specradio"+i);
					EbSpecValue ev = new EbSpecValue();
					ev.setFeatureId(featureId);
					ev.setSpecValue(specVal);
					evList.add(ev);
				}
				skuObj.setSpecList(evList);
				skuList.add(skuObj);
			}
		}
		itemService.saveItem(item, itemClob, paraList, skuList);
		return "redirect:listItem.do?showStatus=1&auditStatus=1";
	}
	
	/**
	 * 查询商品审核的列表
	 * @param qc
	 * @param model
	 * @return
	 */
	@RequestMapping("/listAuditItem.do")
	public String listAuditItem(QueryCondition qc, Model model){
		List<EbBrand> bList = brandService.selectBrandAll();
		model.addAttribute("bList", bList);
		if(qc.getPageNo() == null){
			qc.setPageNo(1);
		}
		Page page = itemService.selectItemByQC(qc);
		model.addAttribute("page", page);
		//把qc写回去，目的是回显
		model.addAttribute("qc", qc);
		return "item/listAudit";
	}
	
	/**
	 * 商品审核
	 * @param itemId
	 * @param auditStatus
	 * @param notes
	 * @return
	 */
	@RequestMapping("/auditItem.do")
	public String auditItem(Long itemId, Short auditStatus, String notes){
		itemService.auditItem(itemId, auditStatus, notes);
		return "redirect:listAuditItem.do?showStatus=1&auditStatus=0";
	}
	/**
	 * 商品上下架
	 * @param itemId
	 * @param showStatus
	 * @param notes
	 * @return
	 */
	@RequestMapping("/showItem.do")
	public String showItem(Long itemId, Short showStatus, String notes){
		itemService.showItem(itemId, showStatus, notes);
		String flag = "1";
		if(showStatus == 1){
			flag = "0";
		}
		return "redirect:listItem.do?showStatus="+flag+"&auditStatus=1";
	}

	@RequestMapping("/publishItem.do")
	public void publishItem(Long itemId, PrintWriter out){
		String result = itemService.publsihItem(itemId, ECPSUtils.readProp("ws_pass"));
		out.write(result);
	}
}
