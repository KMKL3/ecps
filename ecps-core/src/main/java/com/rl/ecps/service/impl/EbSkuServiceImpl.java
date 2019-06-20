package com.rl.ecps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.service.EbSkuService;
import com.rl.ecps.util.ECPSUtils;

@Service
public class EbSkuServiceImpl implements EbSkuService {

	@Autowired
	private EbSkuDao skuDao;
	
	public EbSku getSkuById(Long skuId) {
		return skuDao.getSkuById(skuId);
	}

	public EbSku getSkuByIdWithRedis(Long skuId) {
		Jedis jedis = new Jedis(ECPSUtils.readProp("redis_ip"), new Integer(ECPSUtils.readProp("redis_port")));
		String skuPrice = jedis.hget("sku:"+skuId, "skuPrice");
		String stockInventory = jedis.hget("sku:"+skuId, "stockInventory");
		String marketPrice = jedis.hget("sku:"+skuId, "marketPrice");
		EbSku sku = new EbSku();
		sku.setSkuId(skuId);
		sku.setSkuPrice(new BigDecimal(skuPrice));
		sku.setMarketPrice(new BigDecimal(marketPrice));
		sku.setStockInventory(new Integer(stockInventory));
		
		return sku;
	}

	public EbSku getSkuDetailByIdWithRedis(Long skuId) {
		Jedis jedis = new Jedis(ECPSUtils.readProp("redis_ip"), new Integer(ECPSUtils.readProp("redis_port")));
		//从redis中去sku数据
		String skuPrice = jedis.hget("sku:"+skuId, "skuPrice");
		String stockInventory = jedis.hget("sku:"+skuId, "stockInventory");
		String marketPrice = jedis.hget("sku:"+skuId, "marketPrice");
		String itemId = jedis.hget("sku:"+skuId, "itemId");
		
		EbSku sku = new EbSku();
		sku.setSkuId(skuId);
		sku.setSkuPrice(new BigDecimal(skuPrice));
		sku.setMarketPrice(new BigDecimal(marketPrice));
		sku.setStockInventory(new Integer(stockInventory));
		sku.setItemId(new Long(itemId));
		//从redis中去sku所属的item的数据
		String itemName =  jedis.hget("sku:"+sku.getSkuId()+":item:"+itemId, "itemName");
		String itemNo =  jedis.hget("sku:"+sku.getSkuId()+":item:"+itemId, "itemNo");
		String imgs =  jedis.hget("sku:"+sku.getSkuId()+":item:"+itemId, "imgs");
		EbItem item = new EbItem();
		item.setItemId(new Long(itemId));
		item.setItemName(itemName);
		item.setItemNo(itemNo);
		item.setImgs(imgs);
		sku.setItem(item);
		//获得去规格集合的过程
		List<EbSpecValue> specList = new ArrayList<EbSpecValue>();
		List<String> specIds = jedis.lrange("sku:"+skuId + ":specList", 0, -1);
		for(String specId : specIds){
			EbSpecValue sv = new EbSpecValue();
			String specValue = jedis.hget("sku:"+skuId+":spec:"+specId, "specValue");
			sv.setSkuId(skuId);
			sv.setSpecId(new Long(specId));
			sv.setSpecValue(specValue);
			specList.add(sv);
		}
		sku.setSpecList(specList);
		return sku;
	}

	
	
	
}
