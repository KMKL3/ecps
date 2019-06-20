package com.rl.ecps.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.rl.ecps.dao.EbShipAddrDao;
import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.service.EbRedisService;
import com.rl.ecps.util.ECPSUtils;

@Service
public class EbRedisServiceImpl implements EbRedisService {

	@Autowired
	private EbSkuDao skuDao;
	
	@Autowired
	private EbShipAddrDao addrDao;
	
	public void importEbSkuToRedids() {
		Jedis jedis = new Jedis(ECPSUtils.readProp("redis_ip"), new Integer(ECPSUtils.readProp("redis_port")));
		List<EbSku> skuList = skuDao.selectSkuDetailList();
		for(EbSku sku : skuList){
			//把每个sku的id存储在list中
			jedis.lpush("skuList", sku.getSkuId()+"");
			jedis.hset("sku:"+sku.getSkuId(), "skuId", sku.getSkuId()+"");
			jedis.hset("sku:"+sku.getSkuId(), "skuPrice", sku.getSkuPrice()+"");
			jedis.hset("sku:"+sku.getSkuId(), "marketPrice", sku.getMarketPrice()+"");
			jedis.hset("sku:"+sku.getSkuId(), "stockInventory", sku.getStockInventory()+"");
			jedis.hset("sku:"+sku.getSkuId(), "itemId", sku.getItemId()+"");
			
			
			
			//同步商品信息
			jedis.hset("sku:"+sku.getSkuId()+":item:"+sku.getItem().getItemId(), "itemId", sku.getItemId()+"");
			jedis.hset("sku:"+sku.getSkuId()+":item:"+sku.getItem().getItemId(), "itemNo", sku.getItem().getItemNo()+"");
			jedis.hset("sku:"+sku.getSkuId()+":item:"+sku.getItem().getItemId(), "itemName", sku.getItem().getItemName()+"");
			jedis.hset("sku:"+sku.getSkuId()+":item:"+sku.getItem().getItemId(), "imgs", sku.getItem().getImgs()+"");
			//规格信息同步
			for(EbSpecValue spec : sku.getSpecList()){
				jedis.lpush("sku:"+sku.getSkuId()+":specList", spec.getSpecId()+"");
				jedis.hset("sku:"+sku.getSkuId()+":spec:"+spec.getSpecId(), "specValue", spec.getSpecValue()+"");
				jedis.hset("sku:"+sku.getSkuId()+":spec:"+spec.getSpecId(), "skuId", spec.getSkuId()+"");
				jedis.hset("sku:"+sku.getSkuId()+":spec:"+spec.getSpecId(), "specId", spec.getSpecId()+"");
			}
			
		}
		
		
		

	}

	
	/**
	 * list类型      user:3002:addrList  :[1002, 1003]	==============>   List
	 * hset	   user:3002:addr:1002 : [{shipAddrId,1002}, {shipName, "张三"}，、、、、]========java   Map<String, Map<String>>
	 */
	public void importEbShipAddr() {
		Jedis jedis = new Jedis(ECPSUtils.readProp("redis_ip"), new Integer(ECPSUtils.readProp("redis_port")));
		List<EbShipAddr> addrList = addrDao.selectAddrByUserId(3002l);
		for(EbShipAddr addr : addrList){
			jedis.lpush("user:3002:addrList", addr.getShipAddrId()+"");
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "shipAddrId", addr.getShipAddrId()+"");
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "shipName", addr.getShipName());
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "province", addr.getProvince());
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "city", addr.getCity());
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "district", addr.getDistrict());
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "addr", addr.getAddr());
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "zipCode", addr.getZipCode());
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "phone", addr.getPhone());
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "defaultAddr", addr.getDefaultAddr()+"");
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "provText", addr.getProvText()+"");
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "cityText", addr.getCityText()+"");
			jedis.hset("user:3002:addr:"+addr.getShipAddrId(), "distText", addr.getDistText()+"");
			
		}
	}

}
