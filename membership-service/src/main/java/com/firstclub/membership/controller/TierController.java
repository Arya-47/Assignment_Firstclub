package com.firstclub.membership.controller;

import com.firstclub.membership.dto.response.TierEvaluationResponse;
import com.firstclub.membership.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tier")
@RequiredArgsConstructor
public class TierController {

    private final MembershipService membershipServiceImpl;

    @PostMapping("/evaluate")
    public ResponseEntity<TierEvaluationResponse> evaluateTier(
            @Valid @RequestParam String userId
    ) {
        TierEvaluationResponse response =
                membershipServiceImpl.evaluateTierForUser(userId);

        return ResponseEntity.ok(response);
    }
}
