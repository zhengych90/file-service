package com.smc.file.repository;

import com.smc.file.entity.StockPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface StockPriceRepository extends JpaRepository<StockPriceEntity, Integer> {

}

