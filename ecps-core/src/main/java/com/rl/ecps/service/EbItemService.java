package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.Page;
import com.rl.ecps.model.QueryCondition;


public interface EbItemService {
	
	
	public Page  selectItemByQC(QueryCondition qc);
	
	public void saveItem(EbItem item, EbItemClob itemClob,
			List<EbParaValue> paraList, List<EbSku> skuList);
	
	public void auditItem(Long itemId, Short auditStatus, String notes);
	
	public void showItem(Long itemId, Short showStatus, String notes);
	
	public EbItem selectItemDetailById(Long itemId);
	
	public List<EbItem> selectItemByIndex(String price, Long brandId, String keyWords, String paraVals) throws Exception;

	public String publsihItem(Long itemId, String password);
}
