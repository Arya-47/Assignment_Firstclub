package com.firstclub.membership.repository;

import com.firstclub.membership.model.BenefitConfiguration;
import com.firstclub.membership.model.enums.MembershipTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitConfigurationRepository extends JpaRepository<BenefitConfiguration, Long> {

    @Query("""
        SELECT DISTINCT b
        FROM BenefitConfiguration b
        LEFT JOIN FETCH b.applicableCategories
        WHERE b.tier = :tier
          AND b.active = true
    """)
    List<BenefitConfiguration> findByTierAndActiveTrueWithCategories(
            MembershipTier tier
    );
}
