package com.firstclub.membership.controller;

import com.firstclub.membership.dto.request.CohortUpdateRequest;
import com.firstclub.membership.dto.request.OrderMetricsRequest;
import com.firstclub.membership.dto.response.UserMetricsResponse;
import com.firstclub.membership.service.UserMetricsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final UserMetricsService metricsService;

    @PostMapping("/order")
    public ResponseEntity<Map<String, Object>> updateOrderMetrics(
            @Valid @RequestBody OrderMetricsRequest request
    ) {
        UserMetricsResponse response = metricsService.updateOrderMetricsAndGet(request);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Metrics updated successfully",
                        "data", response
                )
        );
    }

    @PostMapping("/cohort")
    public ResponseEntity<Map<String, Object>> updateCohort(
            @Valid @RequestBody CohortUpdateRequest request
    ) {
        UserMetricsResponse response = metricsService.updateCohortAndGet(request);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Cohort updated successfully",
                        "data", response
                )
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserMetricsResponse> getMetrics(
            @PathVariable String userId
    ) {
        UserMetricsResponse response = metricsService.getMetricsResponse(userId);

        return ResponseEntity.ok(response);
    }
}
