package com.firstclub.membership.repository;

import com.firstclub.membership.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    @Query("SELECT s FROM Subscription s WHERE s.userId = :userId AND s.status = 'ACTIVE' " +
            "AND s.endDate > CURRENT_TIMESTAMP")
    Optional<Subscription> findActiveByUserId(String userId);

    List<Subscription> findByUserId(String userId);

    @Query("SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' " +
            "AND s.endDate < CURRENT_TIMESTAMP")
    List<Subscription> findExpiredSubscriptions();
}
