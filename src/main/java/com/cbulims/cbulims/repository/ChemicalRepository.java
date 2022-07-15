package com.cbulims.cbulims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cbulims.cbulims.model.chemical;

@Repository
public interface ChemicalRepository extends JpaRepository<chemical, Integer> {
	
	List<chemical> findByOrderByExpiryDateAsc();
	
	//Find Chemical by Specific Condition
	List<chemical> findByChemConditionEquals(String condition);
	
	//Find Chemicals About to expire
	List<chemical> findByToExpireTrue();
	
	//Find Chemical By Chemical Name
	chemical findByChemName(String string);

	chemical findTopByToExpireTrueOrderByExpiryDateDesc();
}
