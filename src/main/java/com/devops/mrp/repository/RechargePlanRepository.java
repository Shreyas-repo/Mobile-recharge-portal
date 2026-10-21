package com.devops.mrp.repository;

import com.devops.mrp.model.RechargePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargePlanRepository extends JpaRepository<RechargePlan, Long> {
    List<RechargePlan> findByOperatorIgnoreCase(String operator);
    List<RechargePlan> findByCategoryIgnoreCase(String category);
    List<RechargePlan> findByOperatorIgnoreCaseAndCategoryIgnoreCase(String operator, String category);
}
