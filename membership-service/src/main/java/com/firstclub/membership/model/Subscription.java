package com.firstclub.membership.model;

import com.firstclub.membership.model.enums.MembershipTier;
import com.firstclub.membership.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions", indexes = {
        @Index(name = "idx_user_status", columnList = "userId,status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 50)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private MembershipPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MembershipTier tier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = SubscriptionStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE &&
                LocalDateTime.now().isBefore(endDate);
    }

    public void upgradeTier(MembershipTier newTier) {
        if (newTier.getLevel() <= this.tier.getLevel()) {
            throw new IllegalArgumentException("New tier must be higher than current tier");
        }
        this.tier = newTier;
    }

    public void downgradeTier(MembershipTier newTier) {
        if (newTier.getLevel() >= this.tier.getLevel()) {
            throw new IllegalArgumentException("New tier must be lower than current tier");
        }
        this.tier = newTier;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
    }
}