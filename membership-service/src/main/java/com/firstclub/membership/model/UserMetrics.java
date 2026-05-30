package com.firstclub.membership.model;

import com.firstclub.membership.model.valueObjects.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMetrics {
    @Id
    @Column(length = 50)
    private String userId;

    @Column(name = "order_count", nullable = false)
    @Builder.Default
    private Integer orderCount = 0;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_order_value")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency"))
    })
    private Money totalOrderValue;

    @Column(length = 50)
    private String cohort;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Version
    private Long version;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    public void incrementOrderMetrics(int orderDelta, BigDecimal valueDelta) {
        this.orderCount += orderDelta;
        if (this.totalOrderValue == null) {
            this.totalOrderValue = new Money(valueDelta, "INR");
        } else {
            this.totalOrderValue = this.totalOrderValue.add(new Money(valueDelta, "INR"));
        }
    }
}