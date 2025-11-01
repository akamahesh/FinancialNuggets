package com.maheshbhatt.financialnuggets.repository;

import com.maheshbhatt.financialnuggets.entity.AmcEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmcRepostiory extends JpaRepository<AmcEntity, Long> {
}
