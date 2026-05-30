package com.firstclub.membership.cron;

import com.firstclub.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionScheduler {

    private final MembershipService membershipServiceImpl;

    @Scheduled(cron = "0 0 2 * * ?") // Run at 2 AM daily
    public void expireSubscriptions() {
        log.info("Running scheduled task: expireSubscriptions");
        membershipServiceImpl.expireSubscriptions();
    }
}
