package com.firstclub.membership.model.enums;

public enum MembershipTier {
    SILVER(1),
    GOLD(2),
    PLATINUM(3);

    private final int level;

    MembershipTier(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
