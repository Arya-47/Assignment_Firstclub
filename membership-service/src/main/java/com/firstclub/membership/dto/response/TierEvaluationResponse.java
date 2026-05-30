package com.firstclub.membership.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TierEvaluationResponse {
    private String userId;
    private String currentTier;
    private List<String> progressToNextTier;
    private UserMetricsResponse metrics;
}
