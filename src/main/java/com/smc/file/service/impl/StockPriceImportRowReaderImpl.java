package com.smc.file.service.impl;

import com.smc.file.entity.StockPriceEntity;
import com.smc.file.utils.excel2007.IRowReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName StockPriceImportRowReaderImpl
 * @Description TODO
 * @Author YuChaoZheng
 * @Date 12/3/2019 12:40 PM
 * @Version 1.0
 **/
public class StockPriceImportRowReaderImpl implements IRowReader {

	Logger logger = LoggerFactory.getLogger(StockPriceImportRowReaderImpl.class);
	// 返回的错误信息
	private String errorMessage;
	// 列数最大值
	private static final int COLOUMTOTALNUM = 5;
	// 特殊字符校验串
	private static String reg = "^[\\u4E00-\\u9FA5\\u3002\\uff1b\\uff0c\\uff1a\\"
			+ "u201c\\u201d\\uff08\\uff09\\u3001\\uff1fA-Za-z0-9_.;,\"'()]+$";

	private static Pattern pattern = Pattern.compile(reg);
	private static final String SHEET_ERROR = "张sheet表中第";
	private String dataTime;

	@Override
	public Object getRowData(int sheetNo, int curRow, List<String> rowData) {
		StockPriceEntity entity = new StockPriceEntity();
		int idx = 0;
		try {
			//公司编码
			String companyCode = rowData.get(idx);
			entity.setCompanyCode(companyCode);
			//证券交易所
			String stockExchange = rowData.get(++idx);
			entity.setStockExchange(stockExchange);
			// 最新价格
			String currentPrice = rowData.get(++idx);
			entity.setCurrentPrice(currentPrice);
			// 日期
			String date = rowData.get(++idx);

			Date StartDate = HSSFDateUtil.getJavaDate(Double.parseDouble(date));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			entity.setDate(sdf.format(StartDate));
			// 时间
			String time = rowData.get(++idx);
			entity.setTime(time);

			checkCellInfo(sheetNo, curRow, rowData);
		} catch (RuntimeException e) {
			logger.error("第" + (sheetNo + 1) + SHEET_ERROR + curRow + "行数据解析出错", e);
			this.errorMessage = "第" + (sheetNo + 1) + SHEET_ERROR + curRow + "行数据解析出错";
		}
		return entity;
	}


	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public void checkCellInfo(int sheetNo, int curRow, List<String> rowData) {
		if (curRow == 2 && (rowData.size() != COLOUMTOTALNUM)) {
			this.errorMessage = "文件不一致";
			return;
		}
	}

	@Override
	public String getDataTime() {
		return dataTime;
	}

	@Override
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
}
