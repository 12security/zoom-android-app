package org.apache.commons.codec.language.p018bm;

/* renamed from: org.apache.commons.codec.language.bm.RuleType */
public enum RuleType {
    APPROX("approx"),
    EXACT("exact"),
    RULES("rules");
    
    private final String name;

    private RuleType(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
}
