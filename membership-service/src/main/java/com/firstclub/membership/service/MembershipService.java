package com.firstclub.membership.service;

import com.firstclub.membership.dto.request.SubscribeRequest;
import com.firstclub.membership.dto.request.TierUpdateRequest;
import com.firstclub.membership.dto.response.PlanResponse;
import com.firstclub.membership.dto.response.SubscriptionResponse;
import com.firstclub.membership.dto.response.TierResponse;
import com.firstclub.membership.dto.response.TierEvaluationResponse;
import com.firstclub.membership.model.enums.MembershipTier;

import java.util.List;

public interface MembershipService {

    List<PlanResponse> getAvailablePlans();

    List<TierResponse> getAvailableTiers();

    SubscriptionResponse getCurrentSubscriptionDetails(String userId);

    List<SubscriptionResponse> getSubscriptionHistory(String userId);

    SubscriptionResponse createSubscription(SubscribeRequest request);

    SubscriptionResponse upgradeTier(TierUpdateRequest request);

    SubscriptionResponse downgradeTier(TierUpdateRequest request);

    void cancelSubscription(String userId);

    TierEvaluationResponse evaluateTierForUser(String userId);

    void expireSubscriptions();

    MembershipTier evaluateAndUpdateTier(String userId);
}