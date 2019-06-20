package com.rl.ecps.dao;

import java.util.List;

import com.rl.ecps.model.EbShipAddr;

public interface EbShipAddrDao {
	
	public List<EbShipAddr> selectAddrByUserId(Long userId);

	public EbShipAddr selectAddrById(Long addrId);
	
	public void saveAddr(EbShipAddr addr);
	
	public void updateAddr(EbShipAddr addr);
	
	public void updateDefAddr(Long userId);

}
