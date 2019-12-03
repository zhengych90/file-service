package com.smc.file.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class CompanyEntity extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer companyid;

	@Column(name = "companycode")
	private String companyCode;

	@Column(name = "companyname")
	private String companyName;

	@Column(name = "turnover")
	private Long turnover;

	@Column(name = "ceo")
	private String ceo;

	@Column(name = "boardofdirectors")
	private String boardofdirectors;

	@Column(name = "listedinskex")
	private String listedinskex;

	@Column(name = "sectorname")
	private String sectorName;

	@Column(name = "brifewriteup")
	private String brifewriteup;

	@Column(name = "stockcode")
	private String stockCode;

	@Column(name = "companystatus")
	private String companyStatus;


	public CompanyEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Integer companyid) {
		this.companyid = companyid;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Long getTurnover() {
		return turnover;
	}

	public void setTurnover(Long turnover) {
		this.turnover = turnover;
	}

	public String getCeo() {
		return ceo;
	}

	public void setCeo(String ceo) {
		this.ceo = ceo;
	}

	public String getBoardofdirectors() {
		return boardofdirectors;
	}

	public void setBoardofdirectors(String boardofdirectors) {
		this.boardofdirectors = boardofdirectors;
	}

	public String getListedinskex() {
		return listedinskex;
	}

	public void setListedinskex(String listedinskex) {
		this.listedinskex = listedinskex;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public String getBrifewriteup() {
		return brifewriteup;
	}

	public void setBrifewriteup(String brifewriteup) {
		this.brifewriteup = brifewriteup;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(String companyStatus) {
		this.companyStatus = companyStatus;
	}
}
