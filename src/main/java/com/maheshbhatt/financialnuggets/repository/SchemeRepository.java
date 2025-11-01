package com.maheshbhatt.financialnuggets.repository;

import com.maheshbhatt.financialnuggets.entity.SchemeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemeRepository extends JpaRepository<SchemeEntity, Long> {
}
