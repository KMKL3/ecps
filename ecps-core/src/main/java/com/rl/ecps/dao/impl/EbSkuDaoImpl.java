package com.rl.ecps.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;

import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.util.ECPSUtils;
@Repository
public class EbSkuDaoImpl extends SqlSessionDaoSupport implements EbSkuDao {

	String ns = "com.rl.ecps.mapper.EbSkuMapper.";
	String ns1 = "com.rl.ecps.mapper.EbSpecValueMapper.";

	public void saveSku(List<EbSku> skuList, Long itemId) {
		SqlSession session = this.getSqlSession();
		for(EbSku sku : skuList){
			sku.setItemId(itemId);
			//插入数据，而且返回主键
			session.insert(ns+"insert", sku);
			List<EbSpecValue> specList = sku.getSpecList();
			for(EbSpecValue ev : specList){
				ev.setSkuId(sku.getSkuId());
				session.insert(ns1+"insert", ev);
			}
		} 
	}

	public EbSku getSkuById(Long skuId) {
		return this.getSqlSession().selectOne(ns+"selectByPrimaryKey", skuId);
	}

	public List<EbSku> selectSkuList() {
		return this.getSqlSession().selectList(ns+"selectSkuList");
	}

	public List<EbSku> selectSkuDetailList() {
		return this.getSqlSession().selectList(ns+"selectSkuDetailList");
	}

	public int updateStock(Map<String, Object> map) {
		return this.getSqlSession().update(ns+"updateStock", map);
	}

	public void updateStockRedis(Map<String, Object> map) {
		Jedis jedis = new Jedis(ECPSUtils.readProp("redis_ip"), new Integer(ECPSUtils.readProp("redis_port")));
		Long skuId = (Long) map.get("skuId");
		Integer quantity = (Integer) map.get("quantity");
		jedis.hset("sku:"+skuId, "stockInventory", (new Integer(jedis.hget("sku:"+skuId, "stockInventory")) - quantity)+"");
	}

	
	

}
