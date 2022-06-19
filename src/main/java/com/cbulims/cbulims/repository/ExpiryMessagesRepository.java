package com.cbulims.cbulims.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cbulims.cbulims.model.ChemicalExpiryMessage;

public interface ExpiryMessagesRepository extends JpaRepository<ChemicalExpiryMessage, Integer> {

	ChemicalExpiryMessage findByChemicalName();
	void deleteByChemicalName();
}
