package com.cbulims.cbulims.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class chemical {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer chemId;
	
	@Column
	private Date expiryDate;
	
	@Column(name="toExpire")
	private boolean toExpire = false;
	
	@Column(name="chemQuantity")
	private int chemQuantity;
	
	@Column(name="chemName")
	private String chemName;
	
	@Column(name="chemCondition")
	private String chemCondition;
	
	@Column(name="chemMin")
	private int chemMin = 0;
	
	@Column(name="chemMax")
	private int chemMax = 0;

	public int getChemMax() {
		return chemMax;
	}

	public void setChemMax(int chemMax) {
		this.chemMax = chemMax;
	}

	public int getChemMin() {
		return chemMin;
	}

	public void setChemMin(int chemMin) {
		this.chemMin = chemMin;
	}

	public Integer getChemId() {
		return chemId;
	}

	public void setChemId(Integer chemId) {
		this.chemId = chemId;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getChemQuantity() {
		return chemQuantity;
	}

	public void setChemQuantity(int chemQuantity) {
		this.chemQuantity = chemQuantity;
	}

	public String getChemName() {
		return chemName;
	}

	public void setChemName(String chemName) {
		this.chemName = chemName;
	}

	public String getChemCondition() {
		return chemCondition;
	}

	public void setChemCondition(String chemCondition) {
		this.chemCondition = chemCondition;
	}

	public boolean isToExpire() {
		return toExpire;
	}

	public void setToExpire(boolean toexpire) {
		this.toExpire = toexpire;
	}
	
}
