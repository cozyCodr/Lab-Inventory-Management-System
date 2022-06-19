package com.cbulims.cbulims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cbulims.cbulims.model.IDList;

@Repository
public interface IDListRepository extends JpaRepository<IDList, Integer> {
	
	IDList findByProductname(String string);

}
