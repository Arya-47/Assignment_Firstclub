package com.firstclub.membership.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponse {
    private String id;
    private String userId;
    private String plan;
    private String tier;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
    private List<BenefitResponse> benefits;
}
