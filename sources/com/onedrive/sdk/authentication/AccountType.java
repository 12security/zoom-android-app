package com.onedrive.sdk.authentication;

public enum AccountType {
    MicrosoftAccount("MSA"),
    ActiveDirectory("AAD");
    
    private final String[] mRepresentations;

    private AccountType(String... strArr) {
        this.mRepresentations = strArr;
    }

    public static AccountType fromRepresentation(String str) {
        AccountType[] values;
        for (AccountType accountType : values()) {
            for (String equalsIgnoreCase : accountType.mRepresentations) {
                if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                    return accountType;
                }
            }
        }
        throw new UnsupportedOperationException(String.format("Unable to find a representation for '%s", new Object[]{str}));
    }
}
