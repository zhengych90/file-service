package com.smc.file.utils.excel2007;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @version 1.0
 * @Author yuchaozh
 * @description 抽象Excel2007读取器，excel2007的底层数据结构是xml文件，采用SAX的事件驱动的方法解析 *
 * xml，需要继承DefaultHandler，在遇到文件内容时，事件会触发，这种做法可以大大降低 内存的耗费，特别使用于大数据量的文件。
 */
public class Excel2007Reader<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Excel2007Reader.class);

	private XMLReader fetchSheetParser(ExcelParseHandler parseHandler) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		parser.setContentHandler(parseHandler);
		return parser;
	}

	public Excel2007ParseResponse<T> process(InputStream is, IRowReader rowReader, int headCount,
			boolean oneSheet) throws IOException, InvalidFormatException {
		try (OPCPackage pkg = OPCPackage.open(is)) {
			return doProcess(pkg, rowReader, headCount, oneSheet);
		}
	}

	/**
	 * 遍历工作簿中所有的电子表格
	 *
	 * @param filePath 文件路径
	 * @param rowReader 行数据解析器
	 * @param headCount 表头开始行号
	 * @param oneSheet 只读第一个sheet；
	 */
	public Excel2007ParseResponse<T> process(String filePath, IRowReader rowReader, int headCount,
			boolean oneSheet, String provinceId) throws IOException, InvalidFormatException {
		try (OPCPackage pkg = OPCPackage.open(filePath)) {
			return doProcess(pkg, rowReader, headCount, oneSheet);
		}
	}

	/**
	 * 遍历工作簿中所有的电子表格
	 *
	 * @param pkg 数据源
	 * @param rowReader 行数据解析器
	 * @param headCount 表头开始行号
	 * @param oneSheet 只读第一个sheet；
	 */
	private Excel2007ParseResponse<T> doProcess(OPCPackage pkg, IRowReader rowReader, int headCount,
			boolean oneSheet) throws IOException {
		Excel2007ParseResponse response = new Excel2007ParseResponse();
		try {
			XSSFReader r = new XSSFReader(pkg);
			SharedStringsTable sst = r.getSharedStringsTable();
			ExcelParseHandler<T> parseHandler =
					new ExcelParseHandler(rowReader, headCount, sst);
			XMLReader parser = fetchSheetParser(parseHandler);
			Iterator<InputStream> sheets = r.getSheetsData();
			int sheetNo = 0;
			while (sheets.hasNext()) {
				try (InputStream sheet = sheets.next()) {
					parseHandler.setSheetNo(sheetNo);
					parseHandler.setRowIndex(0);
					parseHandler.setCellData(new ArrayList<>());
					InputSource sheetSource = new InputSource(sheet);
					parser.parse(sheetSource);
					sheetNo++;
				}
				if (oneSheet) {
					break;
				}
			}
			List<T> allDatas = parseHandler.getDataList();
			response.setDatas(allDatas);
			response.setDataTime(parseHandler.getRowReader().getDataTime());
		} catch (SAXException | IOException | OpenXML4JException e) {
			LOGGER.error("", e);
			response.setMessage(e.getMessage());
			response.setDatas(Collections.emptyList());
		}
		return response;
	}

	/**
	 * 遍历工作簿中所有的电子表格
	 */
	public List<T> process(String filePath, IRowReader rowReader, int headCount)
			throws IOException {
		OPCPackage pkg = null;
		Iterator<InputStream> sheets = null;
		try {
			pkg = OPCPackage.open(filePath);
			XSSFReader r = new XSSFReader(pkg);
			SharedStringsTable sst = r.getSharedStringsTable();
			ExcelParseHandler<T> parseHandler = new ExcelParseHandler(rowReader, headCount, sst);
			XMLReader parser = fetchSheetParser(parseHandler);
			sheets = r.getSheetsData();
			try (InputStream sheet = sheets.next()) {
				InputSource sheetSource = new InputSource(sheet);
				parser.parse(sheetSource);
				return parseHandler.getDataList();
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			if (sheets != null) {
				while (sheets.hasNext()) {
					sheets.next().close();
				}
			}
			if (pkg != null) {
				pkg.close();
			}
		}
		return Collections.emptyList();
	}
}
