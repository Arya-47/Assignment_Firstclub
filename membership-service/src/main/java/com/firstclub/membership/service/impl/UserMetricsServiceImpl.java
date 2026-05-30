package com.firstclub.membership.service.impl;

import com.firstclub.membership.dto.request.CohortUpdateRequest;
import com.firstclub.membership.dto.request.OrderMetricsRequest;
import com.firstclub.membership.dto.response.UserMetricsResponse;
import com.firstclub.membership.model.UserMetrics;
import com.firstclub.membership.model.valueObjects.Money;
import com.firstclub.membership.repository.UserMetricsRepository;
import com.firstclub.membership.service.UserMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserMetricsServiceImpl implements UserMetricsService {

    private final UserMetricsRepository metricsRepository;
    private final MembershipServiceImpl membershipServiceImpl;

    @Override
    @Transactional
    public UserMetricsResponse updateOrderMetricsAndGet(OrderMetricsRequest request) {
        UserMetrics metrics = metricsRepository.findByUserId(request.getUserId())
                .orElse(UserMetrics.builder()
                        .userId(request.getUserId())
                        .orderCount(0)
                        .totalOrderValue(new Money(BigDecimal.ZERO))
                        .build());

        metrics.incrementOrderMetrics(
                request.getOrderCount(),
                request.getOrderValue()
        );

        metricsRepository.save(metrics);

        try {
            membershipServiceImpl.evaluateAndUpdateTier(request.getUserId());
        } catch (Exception e) {
            log.warn("Tier evaluation failed for user {}", request.getUserId());
        }

        return UserMetricsResponse.from(metrics);
    }

    @Override
    @Transactional
    public UserMetricsResponse updateCohortAndGet(CohortUpdateRequest request) {
        UserMetrics metrics = metricsRepository.findByUserId(request.getUserId())
                .orElse(UserMetrics.builder()
                        .userId(request.getUserId())
                        .orderCount(0)
                        .totalOrderValue(new Money(BigDecimal.ZERO))
                        .build());

        metrics.setCohort(request.getCohort());
        metricsRepository.save(metrics);

        return UserMetricsResponse.from(metrics);
    }

    @Override
    @Transactional(readOnly = true)
    public UserMetricsResponse getMetricsResponse(String userId) {
        UserMetrics metrics = metricsRepository.findByUserId(userId)
                .orElseThrow();

        return UserMetricsResponse.from(metrics);
    }
}
