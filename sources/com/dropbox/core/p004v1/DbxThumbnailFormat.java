package com.dropbox.core.p004v1;

/* renamed from: com.dropbox.core.v1.DbxThumbnailFormat */
public class DbxThumbnailFormat {
    public static final DbxThumbnailFormat JPEG = new DbxThumbnailFormat("jpeg");
    public static final DbxThumbnailFormat PNG = new DbxThumbnailFormat("png");
    public final String ident;

    public DbxThumbnailFormat(String str) {
        this.ident = str;
    }

    public static DbxThumbnailFormat bestForFileName(String str, DbxThumbnailFormat dbxThumbnailFormat) {
        String lowerCase = str.toLowerCase();
        if (lowerCase.endsWith(".png") || lowerCase.endsWith(".gif")) {
            return PNG;
        }
        if (lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpe")) {
            return JPEG;
        }
        return dbxThumbnailFormat;
    }
}
