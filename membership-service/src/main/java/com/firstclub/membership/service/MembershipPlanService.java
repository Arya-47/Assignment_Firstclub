package com.firstclub.membership.service;

import java.math.BigDecimal;

public interface MembershipPlanService {

    void updatePrice(String code, BigDecimal newPrice);

    void updateStatus(String code, boolean active);
}
