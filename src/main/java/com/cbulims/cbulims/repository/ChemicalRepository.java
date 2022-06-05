package com.cbulims.cbulims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cbulims.cbulims.model.chemical;

@Repository
public interface ChemicalRepository extends JpaRepository<chemical, Integer> {
	
	List<chemical> findByOrderByExpiryDateAsc();

}
