package com.firstclub.membership.service.impl;

import com.firstclub.membership.model.MembershipPlan;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.service.MembershipPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository planRepository;

    @Transactional
    @Override
    public void updatePrice(String code, BigDecimal newPrice) {

        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        MembershipPlan plan = planRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan code"));

        plan.setPrice(newPrice);
    }

    @Transactional
    @Override
    public void updateStatus(String code, boolean active) {

        MembershipPlan plan = planRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan code"));

        plan.setActive(active);
    }
}

