package com.firstclub.membership.config;

import com.firstclub.membership.model.*;
import com.firstclub.membership.model.enums.BenefitType;
import com.firstclub.membership.model.enums.MembershipTier;
import com.firstclub.membership.model.enums.SubscriptionStatus;
import com.firstclub.membership.model.enums.TierCriteriaType;
import com.firstclub.membership.model.valueObjects.Money;
import com.firstclub.membership.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final BenefitConfigurationRepository benefitRepository;
    private final TierCriteriaRepository criteriaRepository;
    private final MembershipPlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserMetricsRepository metricsRepository;

    @Bean
    public CommandLineRunner seedData() {
        return args -> seed();
    }

    @Transactional
    public void seed() {
        if (planRepository.count() == 0) {
            log.info("Seeding initial data...");
            seedPlans();
            seedBenefits();
            seedTierCriteria();
            seedTestUsers();
            log.info("Data seeding completed!");
        } else {
            log.info("Data already exists, skipping seeding");
        }
    }

    // -------------------- PLANS --------------------

    private void seedPlans() {
        List<MembershipPlan> plans = List.of(
                MembershipPlan.builder()
                        .code("MONTHLY")
                        .durationMonths(1)
                        .price(new BigDecimal("99.00"))
                        .description("Monthly membership")
                        .active(true)
                        .build(),

                MembershipPlan.builder()
                        .code("QUARTERLY")
                        .durationMonths(3)
                        .price(new BigDecimal("249.00"))
                        .description("Quarterly membership")
                        .active(true)
                        .build(),

                MembershipPlan.builder()
                        .code("YEARLY")
                        .durationMonths(12)
                        .price(new BigDecimal("899.00"))
                        .description("Yearly membership")
                        .active(true)
                        .build()
        );

        planRepository.saveAll(plans);
        log.info("Seeded {} membership plans", plans.size());
    }


    private void seedBenefits() {
        List<BenefitConfiguration> benefits = new ArrayList<>();

        // Silver tier
        benefits.add(BenefitConfiguration.builder()
                .type(BenefitType.FREE_DELIVERY)
                .description("Reduced delivery charges")
                .tier(MembershipTier.SILVER)
                .freeDelivery(false)
                .active(true)
                .build());

        benefits.add(BenefitConfiguration.builder()
                .type(BenefitType.PERCENTAGE_DISCOUNT)
                .description("5% discount on Electronics and Fashion")
                .tier(MembershipTier.SILVER)
                .discountPercentage(new BigDecimal("5.0"))
                .applicableCategories(Set.of("Electronics", "Fashion"))
                .active(true)
                .build());

        // Gold tier
        benefits.add(BenefitConfiguration.builder()
                .type(BenefitType.FREE_DELIVERY)
                .description("Free delivery on all eligible orders")
                .tier(MembershipTier.GOLD)
                .freeDelivery(true)
                .active(true)
                .build());

        benefits.add(BenefitConfiguration.builder()
                .type(BenefitType.PERCENTAGE_DISCOUNT)
                .description("10% discount on multiple categories")
                .tier(MembershipTier.GOLD)
                .discountPercentage(new BigDecimal("10.0"))
                .applicableCategories(Set.of("Electronics", "Fashion", "Home", "Books"))
                .active(true)
                .build());

        benefits.add(BenefitConfiguration.builder()
                .type(BenefitType.EARLY_ACCESS)
                .description("Early access to sales and launches")
                .tier(MembershipTier.GOLD)
                .active(true)
                .build());

        // Platinum tier
        benefits.add(BenefitConfiguration.builder()
                .type(BenefitType.FREE_DELIVERY)
                .description("Free express delivery on all orders")
                .tier(MembershipTier.PLATINUM)
                .freeDelivery(true)
                .active(true)
                .build());

        benefits.add(BenefitConfiguration.builder()
                .type(BenefitType.PERCENTAGE_DISCOUNT)
                .description("15% discount on all categories")
                .tier(MembershipTier.PLATINUM)
                .discountPercentage(new BigDecimal("15.0"))
                .applicableCategories(Set.of(
                        "Electronics", "Fashion", "Home", "Books", "Sports", "Beauty"))
                .active(true)
                .build());

        benefits.add(BenefitConfiguration.builder()
                .type(BenefitType.PRIORITY_SUPPORT)
                .description("24/7 priority customer support")
                .tier(MembershipTier.PLATINUM)
                .active(true)
                .build());

        benefits.add(BenefitConfiguration.builder()
                .type(BenefitType.EXCLUSIVE_COUPONS)
                .description("Monthly exclusive discount coupons")
                .tier(MembershipTier.PLATINUM)
                .active(true)
                .build());

        benefitRepository.saveAll(benefits);
        log.info("Seeded {} benefits", benefits.size());
    }


    private void seedTierCriteria() {
        List<TierCriteriaConfig> criteria = List.of(
                TierCriteriaConfig.builder()
                        .tier(MembershipTier.GOLD)
                        .criteriaType(TierCriteriaType.ORDER_COUNT)
                        .minOrders(5)
                        .description("Complete 5 orders to reach Gold Tier")
                        .active(true)
                        .build(),

                TierCriteriaConfig.builder()
                        .tier(MembershipTier.GOLD)
                        .criteriaType(TierCriteriaType.ORDER_VALUE)
                        .minValue(new BigDecimal("10000"))
                        .description("Spend ₹10,000 to reach Gold Tier")
                        .active(true)
                        .build(),

                TierCriteriaConfig.builder()
                        .tier(MembershipTier.PLATINUM)
                        .criteriaType(TierCriteriaType.ORDER_COUNT)
                        .minOrders(15)
                        .description("Complete 15 orders to reach Platinum Tier")
                        .active(true)
                        .build(),

                TierCriteriaConfig.builder()
                        .tier(MembershipTier.PLATINUM)
                        .criteriaType(TierCriteriaType.ORDER_VALUE)
                        .minValue(new BigDecimal("50000"))
                        .description("Spend ₹50,000 to reach Platinum Tier")
                        .active(true)
                        .build(),

                TierCriteriaConfig.builder()
                        .tier(MembershipTier.PLATINUM)
                        .criteriaType(TierCriteriaType.COHORT)
                        .eligibleCohorts(Set.of("VIP", "PREMIUM_CUSTOMER"))
                        .description("VIP cohort membership")
                        .active(true)
                        .build()
        );

        criteriaRepository.saveAll(criteria);
        log.info("Seeded {} tier criteria", criteria.size());
    }

    // -------------------- USERS & SUBSCRIPTIONS --------------------

    private void seedTestUsers() {

        MembershipPlan monthly =
                planRepository.findByCode("MONTHLY").orElseThrow();

        MembershipPlan yearly =
                planRepository.findByCode("YEARLY").orElseThrow();

        // User 1
        User user1 = userRepository.save(User.builder()
                .id("user123")
                .email("john.doe@example.com")
                .name("John Doe")
                .createdAt(LocalDateTime.now())
                .build());

        subscriptionRepository.save(Subscription.builder()
                .id(UUID.randomUUID().toString())
                .userId(user1.getId())
                .plan(monthly)
                .tier(MembershipTier.SILVER)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(monthly.getDurationMonths()))
                .build());

        metricsRepository.save(UserMetrics.builder()
                .userId(user1.getId())
                .orderCount(3)
                .totalOrderValue(new Money(new BigDecimal("5000")))
                .lastUpdated(LocalDateTime.now())
                .build());

        // User 2
        User user2 = userRepository.save(User.builder()
                .id("user456")
                .email("jane.smith@example.com")
                .name("Jane Smith")
                .createdAt(LocalDateTime.now())
                .build());

        subscriptionRepository.save(Subscription.builder()
                .id(UUID.randomUUID().toString())
                .userId(user2.getId())
                .plan(yearly)
                .tier(MembershipTier.GOLD)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(yearly.getDurationMonths()))
                .build());

        metricsRepository.save(UserMetrics.builder()
                .userId(user2.getId())
                .orderCount(8)
                .totalOrderValue(new Money(new BigDecimal("25000")))
                .lastUpdated(LocalDateTime.now())
                .build());

        log.info("Seeded 2 test users with subscriptions");
    }
}