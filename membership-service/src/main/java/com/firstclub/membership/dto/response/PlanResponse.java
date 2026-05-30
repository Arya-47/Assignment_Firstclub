package com.firstclub.membership.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanResponse {
    private String name;
    private int durationMonths;
    private BigDecimal price;
    private String description;
}
