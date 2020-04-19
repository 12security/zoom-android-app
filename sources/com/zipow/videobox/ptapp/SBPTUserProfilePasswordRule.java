package com.zipow.videobox.ptapp;

public interface SBPTUserProfilePasswordRule {
    public static final long PASSWORD_RULE_HAS_ALPHABET = 4;
    public static final long PASSWORD_RULE_HAS_NUMBER = 8;
    public static final long PASSWORD_RULE_HAS_SPECIAL = 16;
    public static final long PASSWORD_RULE_MIN_LENGTH = 2;
    public static final long PASSWORD_RULE_NULL_ERROR = 255;
    public static final long PASSWORD_RULE_ONLY_NUMBER = 1;
    public static final long PASSWORD_RULE_PASS = 0;
}
