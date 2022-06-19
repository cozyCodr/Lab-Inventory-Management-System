package com.cbulims.cbulims.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ChemicalExpiryMessage {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name ="expiryDate")
	private String expiryDate;
	
	@Column(name ="chemicalName")
	private String chemicalName;
	
	@Column(name="chemicalCondition")
	private String chemicalCondition;
	
	public ChemicalExpiryMessage(String expiryDate, String chemicalName, String chemicalCondition) {
		super();
		this.expiryDate = expiryDate;
		this.chemicalName = chemicalName;
		this.chemicalCondition = chemicalCondition;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getChemicalName() {
		return chemicalName;
	}

	public void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName;
	}

	public String getChemicalCondition() {
		return chemicalCondition;
	}

	public void setChemicalCondition(String chemicalCondition) {
		this.chemicalCondition = chemicalCondition;
	}
	
	
}
