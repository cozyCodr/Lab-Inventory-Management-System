package com.cbulims.cbulims.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Orders")
public class SentOrder {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer Id;
	
	@Column(name="ProuductId", nullable = false, length = 9)
	private String product;
	
	@Column(name="ChemQuantity")
	private int quantitychem;
	
	@Column(name="InsQuantity")
	private int quantityins;
	
	@Column(name="Issue", nullable = false, length = 11)
	private String issue;
	
	@Column(name="Comments", nullable = false, length = 120)
	private String comments;
	
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getQuantitychem() {
		return quantitychem;
	}

	public void setQuantitychem(int quantitychem) {
		this.quantitychem = quantitychem;
	}

	public int getQuantityins() {
		return quantityins;
	}

	public void setQuantityins(int quantityins) {
		this.quantityins = quantityins;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}
	
	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
