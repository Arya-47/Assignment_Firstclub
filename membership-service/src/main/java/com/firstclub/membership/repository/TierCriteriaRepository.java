package com.firstclub.membership.repository;

import com.firstclub.membership.model.TierCriteriaConfig;
import com.firstclub.membership.model.enums.MembershipTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TierCriteriaRepository extends JpaRepository<TierCriteriaConfig, Long> {
    List<TierCriteriaConfig> findByTierAndActiveTrue(MembershipTier tier);
}
