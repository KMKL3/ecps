package com.rl.ecps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbConsoleLogDao;
import com.rl.ecps.dao.EbItemClobDao;
import com.rl.ecps.dao.EbItemDao;
import com.rl.ecps.dao.EbParaValueDao;
import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbConsoleLog;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.Page;
import com.rl.ecps.model.QueryCondition;
import com.rl.ecps.service.EbItemService;
import com.rl.ecps.stub.EbWSItemService;
import com.rl.ecps.stub.EbWSItemServiceService;
import com.rl.ecps.util.ECPSUtils;

@Service
public class EbItemServiceImpl implements EbItemService {

	@Autowired
	private EbItemDao itemDao;
	
	@Autowired
	private EbItemClobDao itemClobDao;
	
	@Autowired
	private EbSkuDao skuDao;
	
	@Autowired
	private EbParaValueDao paraDao;
	
	@Autowired
	private EbConsoleLogDao logDao;
	
	public Page selectItemByQC(QueryCondition qc) {
		//查询当前的查询条件下的总记录数
		int totalCount = itemDao.selectItemByConditionCount(qc);
		//创建page对象
		Page page = new Page();
		page.setPageNo(qc.getPageNo());
		page.setTotalCount(totalCount);
		//计算startNum和endNum
		Integer startNum = page.getStartNum();
		Integer endNum = page.getEndNum();
		//把值设置给sql的查询对象
		qc.setStartNum(startNum);
		qc.setEndNum(endNum);
		//查询结果集
		List<EbItem> itemList = itemDao.selectItemByCondition(qc);
		page.setList(itemList);
		return page;
	}

	public void saveItem(EbItem item, EbItemClob itemClob,
			List<EbParaValue> paraList, List<EbSku> skuList) {
		itemDao.saveItem(item);
		itemClobDao.saveItemClob(itemClob, item.getItemId());
		paraDao.saveParaValue(paraList, item.getItemId());
		skuDao.saveSku(skuList, item.getItemId());
	}

	public void auditItem(Long itemId, Short auditStatus, String notes) {
		//修改auditStatus状态
		EbItem item = new EbItem();
		item.setItemId(itemId);
		item.setAuditStatus(auditStatus);
		itemDao.updateItem(item);
		
		EbConsoleLog log = new EbConsoleLog();
		log.setEntityId(itemId);
		log.setEntityName("商品表");
		log.setNotes(notes);
		log.setOpTime(new Date());
		log.setOpType("审核");
		log.setTableName("EB_ITEM");
		log.setUserId(1l);
		logDao.saveConsoleLog(log);
		
	}

	public void showItem(Long itemId, Short showStatus, String notes) {
		EbItem item = new EbItem();
		item.setItemId(itemId);
		item.setShowStatus(showStatus);
		itemDao.updateItem(item);
		
		EbConsoleLog log = new EbConsoleLog();
		log.setEntityId(itemId);
		log.setEntityName("商品表");
		log.setNotes(notes);
		log.setOpTime(new Date());
		log.setOpType("上下架");
		log.setTableName("EB_ITEM");
		log.setUserId(1l);
		logDao.saveConsoleLog(log);
	}

	public List<EbItem> selectItemByIndex(String price, Long brandId,
			String keyWords, String paraVals) throws Exception {
		List<EbItem> itemList = new ArrayList<EbItem>();
		SolrServer ss = ECPSUtils.getSolrServer();
		SolrQuery sq = new SolrQuery();
		//价钱筛选
		if(StringUtils.isNotBlank(price)){
			String [] priceArr = price.split("-");
			sq.set("fq", "sku_price:["+priceArr[0]+" TO "+priceArr[1]+" ]");
		}
		
		String queryStr = "*:*";
		//品牌不为空
		if(brandId != null){
			queryStr = "brand_id:"+brandId;
		}
		//如果关键字也不是空
		if(StringUtils.isNotBlank(keyWords)){
			if(StringUtils.equals(queryStr, "*:*")){
				queryStr = "item_keywords:"+keyWords;
			}else{
				queryStr = queryStr + " AND item_keywords:"+keyWords;
			}
		}
		
		if(StringUtils.isNotBlank(paraVals)){
			String [] paraValArr = paraVals.split(",");
			
			
			String paraValsQuery = "";
			for(String paraVal : paraValArr){
				paraValsQuery = paraValsQuery + "para_vals:"+paraVal +" AND ";
			}
			//去掉最后一个AND
			paraValsQuery = paraValsQuery.substring(0, paraValsQuery.lastIndexOf(" AND "));
			
			if(StringUtils.equals(queryStr, "*:*")){
				queryStr = paraValsQuery;
			}else{
				queryStr = queryStr + " AND "+paraValsQuery;
			}
			
		}
		sq.setQuery(queryStr);
		sq.setSort("id", ORDER.desc);
		sq.setHighlight(true);
		sq.addHighlightField("item_name");
		sq.addHighlightField("promotion");
		sq.setHighlightSimplePre("<font color='red'>");
		sq.setHighlightSimplePost("</font>");
		//查询索引库
		QueryResponse qr = ss.query(sq);
		//获得结果
		SolrDocumentList dList = qr.getResults();
		for(SolrDocument sd : dList){
			String itemId = (String) sd.getFieldValue("id");
			String itemName = (String) sd.getFieldValue("item_name");
			String promotion = (String) sd.getFieldValue("promotion");
			String imgs = (String) sd.getFieldValue("imgs");
			String skuPrice = sd.getFieldValue("sku_price").toString();
			//{1001:item_name:["<font>.."]}
			Map<String, Map<String, List<String>>>  hlMap = qr.getHighlighting();
			if(hlMap != null){
				Map<String, List<String>> listMap = hlMap.get(itemId);
				if(listMap != null){
					List<String> iList = listMap.get("item_name");
					if(iList != null && iList.size() > 0){
						String hlStr = "";
						for(String hl : iList){
							hlStr = hlStr + hl;
						}
						itemName = hlStr;
					}
					List<String> pList = listMap.get("promotion");
					if(pList != null && pList.size() > 0){
						String hlStr = "";
						for(String hl : pList){
							hlStr = hlStr + hl;
						}
						promotion = hlStr;
					}
				}
			}
			
			EbItem item = new EbItem();
			item.setItemId(new Long(itemId));
			item.setItemName(itemName);
			item.setPromotion(promotion);
			item.setImgs(imgs);
			item.setSkuPrice(new BigDecimal(skuPrice));
			itemList.add(item);
		}
		return itemList;
	}

	public EbItem selectItemDetailById(Long itemId) {
		return itemDao.selectItemDetailById(itemId);
	}

	public String publsihItem(Long itemId, String password) {
		//创建服务访问点集合的对象
		EbWSItemServiceService wSItemServiceService = new EbWSItemServiceService();
		//调用get加上服务访问点的name（EbWSItemServicePort），getEbWSItemServicePort获得到服务访问的类
		EbWSItemService service = wSItemServiceService.getEbWSItemServicePort();
		return service.publishItem(itemId, password);
	}
	
	
}
