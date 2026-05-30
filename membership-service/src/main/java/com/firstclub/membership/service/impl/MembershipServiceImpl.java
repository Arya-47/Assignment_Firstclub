package com.firstclub.membership.service.impl;

import com.firstclub.membership.dto.request.SubscribeRequest;
import com.firstclub.membership.dto.request.TierUpdateRequest;
import com.firstclub.membership.dto.response.*;
import com.firstclub.membership.model.BenefitConfiguration;
import com.firstclub.membership.model.MembershipPlan;
import com.firstclub.membership.model.Subscription;
import com.firstclub.membership.model.UserMetrics;
import com.firstclub.membership.model.enums.MembershipTier;
import com.firstclub.membership.model.enums.SubscriptionStatus;
import com.firstclub.membership.model.valueObjects.Money;
import com.firstclub.membership.repository.BenefitConfigurationRepository;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.repository.SubscriptionRepository;
import com.firstclub.membership.repository.UserMetricsRepository;
import com.firstclub.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembershipServiceImpl implements MembershipService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserMetricsRepository metricsRepository;
    private final BenefitConfigurationRepository benefitRepository;
    private final TierEvaluationServiceImpl tierEvaluationServiceImpl;
    private final MembershipPlanRepository planRepository;

    @Transactional(readOnly = true)
    @Override
    public List<PlanResponse> getAvailablePlans() {
        return planRepository.findByActiveTrue()
                .stream()
                .map(this::toPlanDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TierResponse> getAvailableTiers() {
        return List.of(MembershipTier.values())
                .stream()
                .map(this::toTierDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SubscriptionResponse getCurrentSubscriptionDetails(String userId) {
        Subscription subscription = fetchActiveSubscription(userId);
        List<BenefitConfiguration> benefits =
                benefitRepository.findByTierAndActiveTrueWithCategories(subscription.getTier());

        return toSubscriptionResponse(subscription, benefits);

    }

    @Transactional(readOnly = true)
    @Override
    public List<SubscriptionResponse> getSubscriptionHistory(String userId) {
        return subscriptionRepository.findByUserId(userId)
                .stream()
                .map(sub -> toSubscriptionResponse(sub, Collections.emptyList()))
                .toList();
    }

    @Transactional
    @Override
    public SubscriptionResponse createSubscription(SubscribeRequest request) {
        MembershipTier tier = parseTier(request.getTier());

        Subscription subscription = createSubscriptionInternal(
                request.getUserId(),
                request.getPlan(),
                tier
        );

        List<BenefitConfiguration> benefits =
                benefitRepository.findByTierAndActiveTrueWithCategories(subscription.getTier());

        return toSubscriptionResponse(subscription, benefits);

    }

    @Transactional
    @Override
    public SubscriptionResponse upgradeTier(TierUpdateRequest request) {
        MembershipTier newTier = parseTier(request.getNewTier());

        Subscription subscription = fetchActiveSubscription(request.getUserId());
        subscription.upgradeTier(newTier);
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        List<BenefitConfiguration> benefits =
                benefitRepository.findByTierAndActiveTrueWithCategories(savedSubscription.getTier());

        return toSubscriptionResponse(savedSubscription, benefits);

    }

    @Transactional
    @Override
    public SubscriptionResponse downgradeTier(TierUpdateRequest request) {
        MembershipTier newTier = parseTier(request.getNewTier());

        Subscription subscription = fetchActiveSubscription(request.getUserId());
        subscription.downgradeTier(newTier);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        List<BenefitConfiguration> benefits =
                benefitRepository.findByTierAndActiveTrueWithCategories(savedSubscription.getTier());

        return toSubscriptionResponse(savedSubscription, benefits);

    }

    @Transactional
    @Override
    public void cancelSubscription(String userId) {
        Subscription subscription = fetchActiveSubscription(userId);
        subscription.cancel();
        subscriptionRepository.save(subscription);
    }

    @Transactional
    @Override
    public TierEvaluationResponse evaluateTierForUser(String userId) {
        Subscription subscription = fetchActiveSubscription(userId);

        UserMetrics metrics = metricsRepository.findByUserId(userId)
                .orElse(UserMetrics.builder()
                        .userId(userId)
                        .orderCount(0)
                        .totalOrderValue(new Money(BigDecimal.ZERO))
                        .build());

        MembershipTier evaluatedTier =
                tierEvaluationServiceImpl.evaluateTier(metrics);

        if (evaluatedTier.getLevel() > subscription.getTier().getLevel()) {
            subscription.upgradeTier(evaluatedTier);
            subscriptionRepository.save(subscription);
        }

        return tierEvaluationServiceImpl.buildEvaluationResult(
                userId,
                subscription.getTier(),
                metrics
        );
    }

    @Transactional
    @Override
    public void expireSubscriptions() {
        List<Subscription> expired = subscriptionRepository.findExpiredSubscriptions();
        expired.forEach(Subscription::expire);
        subscriptionRepository.saveAll(expired);
    }

    @Transactional
    @Override
    public MembershipTier evaluateAndUpdateTier(String userId) {
        Subscription subscription = fetchActiveSubscription(userId);

        UserMetrics metrics = metricsRepository.findByUserId(userId)
                .orElse(UserMetrics.builder()
                        .userId(userId)
                        .orderCount(0)
                        .totalOrderValue(new Money(BigDecimal.ZERO))
                        .build());

        MembershipTier newTier = tierEvaluationServiceImpl.evaluateTier(metrics);

        if (newTier.getLevel() > subscription.getTier().getLevel()) {
            subscription.upgradeTier(newTier);
            subscriptionRepository.save(subscription);
        }

        return newTier;
    }

    private Subscription createSubscriptionInternal(
            String userId,
            String planCode,
            MembershipTier tier
    ) {
        if (subscriptionRepository.findActiveByUserId(userId).isPresent()) {
            throw new IllegalStateException("User already has an active subscription");
        }

        MembershipPlan plan = planRepository.findByCode(planCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan"));

        return subscriptionRepository.save(
                Subscription.builder()
                        .id(UUID.randomUUID().toString())
                        .userId(userId)
                        .plan(plan)
                        .tier(tier)
                        .status(SubscriptionStatus.ACTIVE)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusMonths(plan.getDurationMonths()))
                        .build()
        );
    }

    private Subscription fetchActiveSubscription(String userId) {
        return subscriptionRepository.findActiveByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("No active subscription found"));
    }

    private MembershipTier parseTier(String tier) {
        return MembershipTier.valueOf(tier.toUpperCase());
    }

    private PlanResponse toPlanDto(MembershipPlan plan) {
        return PlanResponse.builder()
                .name(plan.getCode())
                .durationMonths(plan.getDurationMonths())
                .price(plan.getPrice())
                .description(plan.getDescription())
                .build();
    }

    private TierResponse toTierDto(MembershipTier tier) {
        return TierResponse.builder()
                .code(tier.name())
                .level(tier.getLevel())
                .build();
    }

    private SubscriptionResponse toSubscriptionResponse(
            Subscription subscription,
            List<BenefitConfiguration> benefits
    ) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .plan(subscription.getPlan().getCode())
                .tier(subscription.getTier().name())
                .status(subscription.getStatus().name())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .isActive(subscription.isActive())
                .benefits(benefits.stream().map(this::toBenefitResponse).toList())
                .build();
    }

    private BenefitResponse toBenefitResponse(BenefitConfiguration config) {
        Map<String, Object> details = new HashMap<>();

        if (config.getDiscountPercentage() != null) {
            details.put("discountPercentage", config.getDiscountPercentage());
        }
        if (config.getFreeDelivery() != null) {
            details.put("freeDelivery", config.getFreeDelivery());
        }
        if (config.getApplicableCategories() != null &&
                !config.getApplicableCategories().isEmpty()) {
            details.put("categories", config.getApplicableCategories());
        }

        return BenefitResponse.builder()
                .type(config.getType().name())
                .description(config.getDescription())
                .details(details)
                .build();
    }

}
