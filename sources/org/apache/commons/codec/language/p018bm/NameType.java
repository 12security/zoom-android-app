package org.apache.commons.codec.language.p018bm;

/* renamed from: org.apache.commons.codec.language.bm.NameType */
public enum NameType {
    ASHKENAZI("ash"),
    GENERIC("gen"),
    SEPHARDIC("sep");
    
    private final String name;

    private NameType(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
}
