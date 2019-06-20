package com.rl.ecps.service;

import com.rl.ecps.model.EbSku;


public interface EbSkuService {
	
	

	public EbSku getSkuById(Long skuId);
	
	public EbSku getSkuByIdWithRedis(Long skuId);
	
	public EbSku getSkuDetailByIdWithRedis(Long skuId);
}
