package com.firstclub.membership.service;

import com.firstclub.membership.dto.response.TierEvaluationResponse;
import com.firstclub.membership.model.UserMetrics;
import com.firstclub.membership.model.enums.MembershipTier;

public interface TierEvaluationService {

    MembershipTier evaluateTier(UserMetrics metrics);

    TierEvaluationResponse buildEvaluationResult(
            String userId,
            MembershipTier currentTier,
            UserMetrics metrics
    );
}
