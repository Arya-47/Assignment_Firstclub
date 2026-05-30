package com.firstclub.membership.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TierUpdateRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String newTier;
}
