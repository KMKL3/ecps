package com.rl.ecps.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbItemDao;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.service.EbIndexService;
import com.rl.ecps.util.ECPSUtils;

@Service
public class EbIndexServiceImpl implements EbIndexService {

	@Autowired
	private EbItemDao itemDao;

	public void importIndex() throws Exception {
		List<EbItem> itemList = itemDao.selectIsSelectItemList();
		SolrServer ss = ECPSUtils.getSolrServer();
		for(EbItem item : itemList){
			SolrInputDocument sd = new SolrInputDocument();
			sd.addField("id", item.getItemId());
			sd.addField("item_name", item.getItemName());
			sd.addField("sku_price", item.getSkuPrice());
			sd.addField("item_keywords", item.getKeywords());
			sd.addField("imgs", item.getImgs());
			sd.addField("promotion", item.getPromotion());
			sd.addField("brand_id", item.getBrandId());
			
			String paraVals = "";
			for(EbParaValue para : item.getParaList()){
				paraVals = paraVals + para.getParaValue() + " ";
			}
			sd.addField("para_vals", paraVals);
			ss.add(sd);
		}
		ss.commit();
		
	}

	public void addIndex() {
		// TODO Auto-generated method stub
		
	}

	public void deleteIndex(String id) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
