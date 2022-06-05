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
	
	@Column(name="chemQuantity")
	private int chemQuantity;
	
	@Column(name="chemName")
	private String chemName;
	
	@Column(name="chemCondition")
	private String chemCondition;
	
	@Column(name="chemicalThreshold")
	private int chemicalThreshold;

	public int getChemicalThreshold() {
		return chemicalThreshold;
	}

	public void setChemicalThreshold(int chemicalThreshold) {
		this.chemicalThreshold = chemicalThreshold;
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
	
}
