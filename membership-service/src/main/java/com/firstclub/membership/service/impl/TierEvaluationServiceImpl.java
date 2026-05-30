package com.firstclub.membership.service.impl;

import com.firstclub.membership.dto.response.TierEvaluationResponse;
import com.firstclub.membership.dto.response.UserMetricsResponse;
import com.firstclub.membership.model.TierCriteriaConfig;
import com.firstclub.membership.model.UserMetrics;
import com.firstclub.membership.model.enums.MembershipTier;
import com.firstclub.membership.repository.TierCriteriaRepository;
import com.firstclub.membership.service.TierEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TierEvaluationServiceImpl implements TierEvaluationService {

    private final TierCriteriaRepository criteriaRepository;

    @Override
    public MembershipTier evaluateTier(UserMetrics metrics) {
        if (evaluateTierCriteria(MembershipTier.PLATINUM, metrics)) {
            return MembershipTier.PLATINUM;
        }
        if (evaluateTierCriteria(MembershipTier.GOLD, metrics)) {
            return MembershipTier.GOLD;
        }
        return MembershipTier.SILVER;
    }

    @Override
    public TierEvaluationResponse buildEvaluationResult(
            String userId,
            MembershipTier currentTier,
            UserMetrics metrics
    ) {
        List<String> progress = getProgressToNextTier(currentTier, metrics);

        return TierEvaluationResponse.builder()
                .userId(userId)
                .currentTier(currentTier.name())
                .progressToNextTier(progress)
                .metrics(UserMetricsResponse.from(metrics))
                .build();
    }

    private boolean evaluateTierCriteria(MembershipTier tier, UserMetrics metrics) {
        List<TierCriteriaConfig> criteria = criteriaRepository.findByTierAndActiveTrue(tier);
        if (criteria.isEmpty()) {
            return tier == MembershipTier.SILVER;
        }

        return criteria.stream().anyMatch(c -> evaluateSingleCriteria(c, metrics));
    }

    private boolean evaluateSingleCriteria(TierCriteriaConfig criteria, UserMetrics metrics) {
        return switch (criteria.getCriteriaType()) {
            case ORDER_COUNT -> metrics.getOrderCount() >= criteria.getMinOrders();
            case ORDER_VALUE ->
                    metrics.getTotalOrderValue() != null &&
                            metrics.getTotalOrderValue().getAmount()
                                    .compareTo(criteria.getMinValue()) >= 0;
            case COHORT ->
                    metrics.getCohort() != null &&
                            criteria.getEligibleCohorts().contains(metrics.getCohort());
        };
    }

    private List<String> getProgressToNextTier(MembershipTier currentTier, UserMetrics metrics) {
        MembershipTier nextTier = getNextTier(currentTier);
        if (nextTier == null) {
            return Collections.singletonList("Already at highest tier");
        }

        List<TierCriteriaConfig> criteria = criteriaRepository.findByTierAndActiveTrue(nextTier);
        return criteria.stream()
                .filter(c -> !evaluateSingleCriteria(c, metrics))
                .map(TierCriteriaConfig::getDescription)
                .toList();
    }

    private MembershipTier getNextTier(MembershipTier current) {
        return switch (current) {
            case SILVER -> MembershipTier.GOLD;
            case GOLD -> MembershipTier.PLATINUM;
            case PLATINUM -> null;
        };
    }
}