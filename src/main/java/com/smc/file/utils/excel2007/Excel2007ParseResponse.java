package com.smc.file.utils.excel2007;

import java.util.List;

/**
 * @version 1.0
 * @Author yuchaozh
 * @description excel 响应实体
 */
public class Excel2007ParseResponse<T> {

	private List<T> datas;
	private String message;
	private String dataTime;

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
