package com.zipow.videobox.util;

public class GifException extends Exception {
    public GifException(Throwable th) {
        super(th);
    }

    public GifException(String str, Throwable th) {
        super(str, th);
    }
}
