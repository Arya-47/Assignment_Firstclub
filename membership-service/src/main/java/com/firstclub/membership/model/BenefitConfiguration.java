package com.firstclub.membership.model;

import com.firstclub.membership.model.enums.BenefitType;
import com.firstclub.membership.model.enums.MembershipTier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "benefit_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefitConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BenefitType type;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MembershipTier tier;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "free_delivery")
    private Boolean freeDelivery;

    @ElementCollection
    @CollectionTable(name = "benefit_categories", joinColumns = @JoinColumn(name = "benefit_id"))
    @Column(name = "category")
    @Builder.Default
    private Set<String> applicableCategories = new HashSet<>();

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}