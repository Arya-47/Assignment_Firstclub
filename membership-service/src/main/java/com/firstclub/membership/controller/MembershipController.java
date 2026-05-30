package com.firstclub.membership.controller;

import com.firstclub.membership.dto.request.SubscribeRequest;
import com.firstclub.membership.dto.request.TierUpdateRequest;
import com.firstclub.membership.dto.response.PlanResponse;
import com.firstclub.membership.dto.response.SubscriptionResponse;
import com.firstclub.membership.dto.response.TierResponse;
import com.firstclub.membership.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipServiceImpl;

    @GetMapping("/plans")
    public ResponseEntity<List<PlanResponse>> getPlans() {
        List<PlanResponse> response = membershipServiceImpl.getAvailablePlans();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/tiers")
    public ResponseEntity<List<TierResponse>> getTiers() {
        List<TierResponse> response = membershipServiceImpl.getAvailableTiers();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionResponse> subscribe(
            @Valid @RequestBody SubscribeRequest request
    ) {
        SubscriptionResponse response = membershipServiceImpl.createSubscription(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/subscription/current")
    public ResponseEntity<SubscriptionResponse> getCurrentSubscription(
            @RequestParam String userId
    ) {
        SubscriptionResponse response = membershipServiceImpl.getCurrentSubscriptionDetails(userId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/subscription/history")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptionHistory(
            @RequestParam String userId
    ) {
        List<SubscriptionResponse> response = membershipServiceImpl.getSubscriptionHistory(userId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/tier/upgrade")
    public ResponseEntity<SubscriptionResponse> upgradeTier(
            @Valid @RequestBody TierUpdateRequest request
    ) {
        SubscriptionResponse response = membershipServiceImpl.upgradeTier(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/tier/downgrade")
    public ResponseEntity<SubscriptionResponse> downgradeTier(
            @Valid @RequestBody TierUpdateRequest request
    ) {
        SubscriptionResponse response = membershipServiceImpl.downgradeTier(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/subscription/cancel")
    public ResponseEntity<Void> cancelSubscription(@RequestParam String userId) {
        membershipServiceImpl.cancelSubscription(userId);
        return ResponseEntity.noContent().build();
    }
}