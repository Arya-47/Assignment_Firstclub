package com.firstclub.membership.controller;

import com.firstclub.membership.dto.request.UpdatePlanPriceRequest;
import com.firstclub.membership.dto.request.UpdatePlanStatusRequest;
import com.firstclub.membership.service.MembershipPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/membership/plans")
@RequiredArgsConstructor
public class MembershipPlanAdminController {

    private final MembershipPlanService planService;

    @PatchMapping("/{code}/price")
    public ResponseEntity<Void> updatePrice(
            @PathVariable String code,
            @Valid @RequestBody UpdatePlanPriceRequest request
    ) {
        planService.updatePrice(code, request.getPrice());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{code}/status")
    public ResponseEntity<Map<String, String>> updateStatus(
            @PathVariable String code,
            @Valid @RequestBody UpdatePlanStatusRequest request
    ) {
        planService.updateStatus(code, request.isActive());
        return ResponseEntity.noContent().build();
    }
}
