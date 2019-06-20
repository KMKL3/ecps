package com.rl.ecps.service;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rl.ecps.model.EbBrand;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.util.FMutil;
@RunWith(value=SpringJUnit4ClassRunner.class)//使用spring和junit整合的运行器
@ContextConfiguration(locations={"classpath:beans.xml"})//指定spring的配置文件的位置
public class EbBrandServiceTest {

	@Autowired
	private EbBrandService brandService;
	
	@Autowired
	private EbIndexService indexService;
	
	@Autowired
	private EbItemService itemService;
	
	@Autowired
	private EbRedisService redisService;
	
	@Autowired
	private EbOrderFlowService flowService;
	
	@Test
	public void testSaveBrand() {
		EbBrand brand = new EbBrand();
		brand.setBrandName("康佳");
		brand.setBrandDesc("还行");
		brand.setImgs("http://xx");
		brand.setWebsite("http://kangjia");
		brand.setBrandSort(1);
		brandService.saveBrand(brand);
	}

	@Test
	public void testSelectBrandAll() {
		fail("Not yet implemented");
	}
	
	@Test
	public void importIndex() throws Exception{
		indexService.importIndex();
	}
	
	@Test
	public void testGeneratePage() throws Exception{
		EbItem item = itemService.selectItemDetailById(4494l);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("item", item);
		map.put("path", "http://localhost:8086/ecps-portal");
		map.put("request_file_path", "http://localhost:8088/ecps-file");
		FMutil.ouputFile("productDetail.ftl", item.getItemId()+".html", map);
		
	}
	
	@Test
	public void importSkuToRedis(){
		redisService.importEbSkuToRedids();
	}
	
	@Test
	public void importAddr(){
		redisService.importEbShipAddr();
	}
	
	@Test
	public void deployFlow(){
		flowService.deployOrderFlow();
	}
	
	

}
