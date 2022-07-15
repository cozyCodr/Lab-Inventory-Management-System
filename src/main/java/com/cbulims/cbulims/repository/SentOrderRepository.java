package com.cbulims.cbulims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cbulims.cbulims.model.SentOrder;

import java.util.List;

@Repository
public interface SentOrderRepository extends JpaRepository<SentOrder, Integer> {

    List<SentOrder> findTop5ByOrderByIdDesc();
}
