package com.prosvirnin.fmsparser.repository;

import com.prosvirnin.fmsparser.entity.FmsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FmsRepository extends JpaRepository<FmsEntity, Long> { }