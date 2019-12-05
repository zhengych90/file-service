package com.smc.file.service;

import com.smc.file.entity.StockPriceEntity;
import com.smc.file.repository.CompanyRepository;
import com.smc.file.repository.StockPriceRepository;
import com.smc.file.utils.CommonResult;
import com.smc.file.utils.ResponseCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName StockPriceService
 * @Description TODO
 * @Author YuChaoZheng
 * @Date 12/3/2019 9:40 AM
 * @Version 1.0
 **/
@Service
public class StockPriceService {

	@Autowired
	private StockPriceRepository stockPriceRepository;
	@Autowired
	private CompanyRepository companyRepository;

	/**
	 * insert excel data
	 */
	public CommonResult insert(List<StockPriceEntity> validDatas) {
		stockPriceRepository.saveAll(validDatas);
		String companyName = companyRepository
				.getCompanyNameByCode(validDatas.get(0).getCompanyCode());
		Map<String, String> map = new HashMap();
		map.put("record", String.valueOf(validDatas.size()));
		map.put("companyName", companyName);
		map.put("stockExchange", validDatas.get(0).getStockExchange());
		map.put("fromDate", validDatas.get(0).getDateTime());
		map.put("toDate",
				validDatas.get(validDatas.size() - 1).getDateTime());
		return CommonResult.build(ResponseCode.SUCCESS, "upload success!", map);
	}

}
