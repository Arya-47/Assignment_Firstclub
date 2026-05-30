package com.firstclub.membership.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMetricsRequest {
    @NotBlank
    private String userId;

    @Min(1)
    private int orderCount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal orderValue;

}
