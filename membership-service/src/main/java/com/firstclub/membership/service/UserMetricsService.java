package com.firstclub.membership.service;

import com.firstclub.membership.dto.request.CohortUpdateRequest;
import com.firstclub.membership.dto.request.OrderMetricsRequest;
import com.firstclub.membership.dto.response.UserMetricsResponse;

public interface UserMetricsService {

    UserMetricsResponse updateOrderMetricsAndGet(OrderMetricsRequest request);

    UserMetricsResponse updateCohortAndGet(CohortUpdateRequest request);

    UserMetricsResponse getMetricsResponse(String userId);
}
