package com.cbulims.cbulims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cbulims.cbulims.model.Instrument;

public interface InstrumentRepository extends JpaRepository<Instrument, Integer> {

	//If Damaged	
	List<Instrument> findByDamagedTrue(); 
}
