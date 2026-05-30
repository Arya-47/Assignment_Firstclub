package com.firstclub.membership.dto.response;

import com.firstclub.membership.model.UserMetrics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMetricsResponse {
    private String userId;
    private int orderCount;
    private BigDecimal totalOrderValue;
    private String cohort;
    private LocalDateTime lastUpdated;

    public static UserMetricsResponse from(UserMetrics metrics) {
        return UserMetricsResponse.builder()
                .userId(metrics.getUserId())
                .orderCount(metrics.getOrderCount())
                .totalOrderValue(metrics.getTotalOrderValue() != null ?
                        metrics.getTotalOrderValue().getAmount() : BigDecimal.ZERO)
                .cohort(metrics.getCohort())
                .lastUpdated(metrics.getLastUpdated())
                .build();
    }
}
