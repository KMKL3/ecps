package com.rl.ecps.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.rl.ecps.dao.EbShipAddrDao;
import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.service.EbShipAddrService;
import com.rl.ecps.util.ECPSUtils;

@Service
public class EbShipAddrServiceImpl implements EbShipAddrService {

	@Autowired
	private EbShipAddrDao addrDao;
	
	public List<EbShipAddr> selectAddrByUserId(Long userId) {
		return addrDao.selectAddrByUserId(userId);
	}

	public EbShipAddr selectAddrById(Long addrId) {
		return addrDao.selectAddrById(addrId);
	}

	public void saveOrUpdateAddr(EbShipAddr addr) {
		if(addr.getDefaultAddr() == 1){
			addrDao.updateDefAddr(addr.getPtlUserId());
		}
		if(addr.getShipAddrId() == null){
			addrDao.saveAddr(addr);
		}else{
			addrDao.updateAddr(addr);
		}
	}

	public List<EbShipAddr> selectAddrByUserIdWithRedis(Long userId) {
		Jedis jedis = new Jedis(ECPSUtils.readProp("redis_ip"), new Integer(ECPSUtils.readProp("redis_port")));
		List<EbShipAddr> addrList = new ArrayList<EbShipAddr>();
		List<String> addrIds = jedis.lrange("user:"+userId+":addrList", 0, -1);
		for(String addrId : addrIds){
			String shipName = jedis.hget("user:"+userId+":addr:" +addrId, "shipName");
			String province = jedis.hget("user:"+userId+":addr:" +addrId, "province");
			String city = jedis.hget("user:"+userId+":addr:" +addrId, "city");
			String district = jedis.hget("user:"+userId+":addr:" +addrId, "district");
			String addr = jedis.hget("user:"+userId+":addr:" +addrId, "addr");
			String zipCode = jedis.hget("user:"+userId+":addr:" +addrId, "zipCode");
			String phone = jedis.hget("user:"+userId+":addr:" +addrId, "phone");
			String defaultAddr = jedis.hget("user:"+userId+":addr:" +addrId, "defaultAddr");
			String provText = jedis.hget("user:"+userId+":addr:" +addrId, "provText");
			String cityText = jedis.hget("user:"+userId+":addr:" +addrId, "cityText");
			String distText = jedis.hget("user:"+userId+":addr:" +addrId, "distText");
			EbShipAddr addrObj = new EbShipAddr();
			addrObj.setShipAddrId(new Long(addrId));
			addrObj.setShipName(shipName);
			addrObj.setProvince(province);
			addrObj.setCity(city);
			addrObj.setDistrict(district);
			addrObj.setAddr(addr);
			addrObj.setZipCode(zipCode);
			addrObj.setPhone(phone);
			addrObj.setDefaultAddr(new Short(defaultAddr));
			addrObj.setProvText(provText);
			addrObj.setCityText(cityText);
			addrObj.setDistText(distText);
			addrList.add(addrObj);
			
		}
		
		
		return addrList;
	}

	public EbShipAddr selectAddrByIdWithRedis(Long userId, Long addrId) {
		Jedis jedis = new Jedis(ECPSUtils.readProp("redis_ip"), new Integer(ECPSUtils.readProp("redis_port")));
		String shipName = jedis.hget("user:"+userId+":addr:" +addrId, "shipName");
		String province = jedis.hget("user:"+userId+":addr:" +addrId, "province");
		String city = jedis.hget("user:"+userId+":addr:" +addrId, "city");
		String district = jedis.hget("user:"+userId+":addr:" +addrId, "district");
		String addr = jedis.hget("user:"+userId+":addr:" +addrId, "addr");
		String zipCode = jedis.hget("user:"+userId+":addr:" +addrId, "zipCode");
		String phone = jedis.hget("user:"+userId+":addr:" +addrId, "phone");
		String defaultAddr = jedis.hget("user:"+userId+":addr:" +addrId, "defaultAddr");
		String provText = jedis.hget("user:"+userId+":addr:" +addrId, "provText");
		String cityText = jedis.hget("user:"+userId+":addr:" +addrId, "cityText");
		String distText = jedis.hget("user:"+userId+":addr:" +addrId, "distText");
		EbShipAddr addrObj = new EbShipAddr();
		addrObj.setShipAddrId(new Long(addrId));
		addrObj.setShipName(shipName);
		addrObj.setProvince(province);
		addrObj.setCity(city);
		addrObj.setDistrict(district);
		addrObj.setAddr(addr);
		addrObj.setZipCode(zipCode);
		addrObj.setPhone(phone);
		addrObj.setDefaultAddr(new Short(defaultAddr));
		addrObj.setProvText(provText);
		addrObj.setCityText(cityText);
		addrObj.setDistText(distText);
		return addrObj;
	}

	
	
	
}
