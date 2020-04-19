package com.dropbox.core.p004v1;

/* renamed from: com.dropbox.core.v1.DbxWriteMode */
public final class DbxWriteMode {
    private static final DbxWriteMode AddInstance = new DbxWriteMode(QueryParameters.OVERWRITE, "false");
    private static final DbxWriteMode ForceInstance = new DbxWriteMode(QueryParameters.OVERWRITE, "true");
    final String[] params;

    DbxWriteMode(String... strArr) {
        this.params = strArr;
    }

    public static DbxWriteMode add() {
        return AddInstance;
    }

    public static DbxWriteMode force() {
        return ForceInstance;
    }

    public static DbxWriteMode update(String str) {
        return new DbxWriteMode("parent_rev", str);
    }
}
