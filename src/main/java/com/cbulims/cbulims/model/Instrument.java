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
	private String insCondition;
	
	@Column(name="Damaged")
	private boolean damaged = false;
	
	@Column(name="damagedQty")
	private int damagedQty;
	
	@Column(name="instMin")
	private int instMin;

	@Column(name="instMax")
	private int instMax;

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

	public String getInsCondition() {
		return insCondition;
	}

	public void setInsCondition(String insCondition) {
		this.insCondition = insCondition;
	}

	public boolean isDamaged() {
		return damaged;
	}

	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
	}

	public int getDamagedQty() {
		return damagedQty;
	}

	public void setDamagedQty(int damagedQty) {
		this.damagedQty = damagedQty;
	}

	public int getInstMin() {
		return instMin;
	}

	public void setInstMin(int instMin) {
		this.instMin = instMin;
	}

	public int getInstMax() {
		return instMax;
	}

	public void setInstMax(int instMax) {
		this.instMax = instMax;
	}
}
