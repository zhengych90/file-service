package com.smc.file.utils.excel2007;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @Author yuchaozh
 * @description
 * @version 1.0
 */
public class ExcelParseHandler<T> extends DefaultHandler {

	private int headCount = 0;
	//共享字符串表
	private SharedStringsTable sharedStringsTable;
	//上一次的内容
	private String lastContents;
	private boolean isNullCell;
	//读取行的索引
	private int rowIndex = 0;
	//是否重新开始了一行
	private boolean isCurrentRow = false;
	//行数据
	private List<String> cellData = new ArrayList<>();
	private IRowReader rowReader;
	//sheet索引
	private int sheetNo;
	private boolean nextIsString;

	private String lastColumn;


	//数据集
	private List<T> dataList = new ArrayList<>();

	public ExcelParseHandler(IRowReader rowReader, int headCount,
			SharedStringsTable sharedStringsTable,
			String provinceId) {
		this.rowReader = rowReader;
		this.headCount = headCount;
		this.sharedStringsTable = sharedStringsTable;
	}

	public ExcelParseHandler(IRowReader rowReader, int headCount,
			SharedStringsTable sharedStringsTable) {
		this.rowReader = rowReader;
		this.headCount = headCount;
		this.sharedStringsTable = sharedStringsTable;
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes)
			throws SAXException {
		//节点的类型
		if ("row".equals(name)) {
			rowIndex++;
		}
		//表头的行直接跳过
		if (rowIndex <= headCount) {
			return;
		}
		isCurrentRow = true;
		// c => cell
		if ("c".equals(name)) {
			String currentColumn = attributes.getValue("r");
			int n = countNullCell(currentColumn, lastColumn);
			for (int i = 0; i < n; i++) {
				cellData.add("");
			}

			String cellType = attributes.getValue("t");
			isNullCell = null == cellType;
			nextIsString = "s".equals(cellType);
			lastColumn = attributes.getValue("r");
		}
		lastContents = "";
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (rowIndex <= headCount) {
			return;
		}
		getCellData(name);
		//如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
		if ("row".equals(name)) {
			Object rowData = null;
			rowData = rowReader.getRowData(sheetNo, rowIndex, cellData);
			if (rowData == null) {
				throw new SAXException("The row data contains error: " + JSONArray.toJSONString(cellData));
			} else if (!StringUtils.isEmpty(rowReader.getErrorMessage())) {
				throw new SAXException(rowReader.getErrorMessage());
			}
			dataList.add((T) rowData);
			isCurrentRow = false;
			cellData.clear();
		}

	}

	/**
	 * 获取单元格的值
	 *
	 * @param name 标签名称
	 */
	public void getCellData(String name) {
		if (nextIsString) {
			int idx = Integer.parseInt(lastContents);
			lastContents = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx)).toString();
			nextIsString = false;
		}
		//如果是单元格的值
		if (isCurrentRow) {
			if ("c".equals(name) && !isNullCell) {
				cellData.add(lastContents);
			} else if ("c".equals(name) && isNullCell) {//如果是空值
				//是新行则new一行的对象来保存一行的值
				cellData.add("");
			}
		}
		isNullCell = false;
	}

	/**
	 * 计算两个单元格之间的单元格数目(同一行)
	 */
	private int countNullCell(String currentColumn, String lastColumn) {
		if (StringUtils.isEmpty(lastColumn) || StringUtils.isEmpty(currentColumn)) {
			return 0;
		}
		//excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
		String currentColumnTemp = currentColumn.replaceAll("\\d+", "");
		String lastColumnTemp = lastColumn.replaceAll("\\d+", "");

		currentColumnTemp = fillChar(currentColumnTemp);
		lastColumnTemp = fillChar(lastColumnTemp);

		char[] currentColumnLetters = currentColumnTemp.toCharArray();
		char[] lastColumnLetters = lastColumnTemp.toCharArray();
		int res = (currentColumnLetters[0] - lastColumnLetters[0]) * 26 * 26
				+ (currentColumnLetters[1] - lastColumnLetters[1]) * 26
				+ (currentColumnLetters[2] - lastColumnLetters[2]);
		return res - 1;
	}

	String fillChar(String str) {
		StringBuilder result = new StringBuilder();
		int strLength = str.length();
		int maxLen = 3;
		if (strLength < 3) {
			for (int i = 0; i < (maxLen - strLength); i++) {
				result.append('@');
			}
		}
		result.append(str);
		return result.toString();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		//得到单元格内容的值
		lastContents += new String(ch, start, length);
	}

	public List<T> getDataList() {
		return dataList;
	}

	public int getSheetNo() {
		return sheetNo;
	}

	public void setSheetNo(int sheetNo) {
		this.sheetNo = sheetNo;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public IRowReader getRowReader() {
		return rowReader;
	}

	public void setRowReader(IRowReader rowReader) {
		this.rowReader = rowReader;
	}

	public List<String> getCellData() {
		return cellData;
	}

	public void setCellData(List<String> cellData) {
		this.cellData = cellData;
	}
}
