package com.firstclub.membership.model;

import com.firstclub.membership.model.enums.MembershipTier;
import com.firstclub.membership.model.enums.TierCriteriaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tier_criteria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TierCriteriaConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MembershipTier tier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TierCriteriaType criteriaType;

    @Column(name = "min_orders")
    private Integer minOrders;

    @Column(name = "min_value", precision = 19, scale = 2)
    private BigDecimal minValue;

    @ElementCollection
    @CollectionTable(name = "tier_cohorts", joinColumns = @JoinColumn(name = "criteria_id"))
    @Column(name = "cohort")
    @Builder.Default
    private Set<String> eligibleCohorts = new HashSet<>();

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(length = 500)
    private String description;
}