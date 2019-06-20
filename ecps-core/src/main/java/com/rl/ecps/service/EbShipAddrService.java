package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.Page;
import com.rl.ecps.model.QueryCondition;


public interface EbShipAddrService {
	
	public List<EbShipAddr> selectAddrByUserId(Long userId);
	
	public EbShipAddr selectAddrById(Long addrId);
	
	public EbShipAddr selectAddrByIdWithRedis(Long userId, Long addrId);
	
	public void saveOrUpdateAddr(EbShipAddr addr);
	
	public List<EbShipAddr> selectAddrByUserIdWithRedis(Long userId);
}
