package com.dropbox.core.p004v1;

import com.zipow.videobox.sip.CmmSIPCallFailReason;

/* renamed from: com.dropbox.core.v1.DbxThumbnailSize */
public class DbxThumbnailSize {
    public static final DbxThumbnailSize w1024h768 = new DbxThumbnailSize("xl", 1024, 768);
    public static final DbxThumbnailSize w128h128 = new DbxThumbnailSize("m", 128, 128);
    public static final DbxThumbnailSize w32h32 = new DbxThumbnailSize("xs", 32, 32);
    public static final DbxThumbnailSize w640h480 = new DbxThumbnailSize("l", 640, CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable);
    public static final DbxThumbnailSize w64h64 = new DbxThumbnailSize("s", 64, 64);
    public final int height;
    public final String ident;
    public final int width;

    public DbxThumbnailSize(String str, int i, int i2) {
        this.ident = str;
        this.width = i;
        this.height = i2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(this.ident);
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(this.width);
        sb.append("x");
        sb.append(this.height);
        sb.append(")");
        return sb.toString();
    }
}
