package com.firstclub.membership.repository;

import com.firstclub.membership.model.UserMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMetricsRepository extends JpaRepository<UserMetrics, String> {
    Optional<UserMetrics> findByUserId(String userId);
}
