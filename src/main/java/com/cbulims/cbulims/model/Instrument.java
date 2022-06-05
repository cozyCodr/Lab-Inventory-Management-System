package com.cbulims.cbulims.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Instrument{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="insQuantity")
	private int insQuantity;
	
	@Column(name="insName")
	private String insName;
	
	@Column(name="insCondition")
	private boolean insCondition;
	
	@Column(name="instrumentThreshold")
	private int instrumentThreshold;

	public int getInstrumentThreshold() {
		return instrumentThreshold;
	}

	public void setInstrumentThreshold(int instrumentThreshold) {
		this.instrumentThreshold = instrumentThreshold;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getInsQuantity() {
		return insQuantity;
	}

	public void setInsQuantity(int insQuantity) {
		this.insQuantity = insQuantity;
	}

	public String getInsName() {
		return insName;
	}

	public void setInsName(String insName) {
		this.insName = insName;
	}

	public boolean getInsCondition() {
		return insCondition;
	}

	public void setInsCondition(boolean insCondition) {
		this.insCondition = insCondition;
	}
	
	
}
