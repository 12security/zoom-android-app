package com.zipow.videobox.pdf;

public class PDFPageErrorException extends Exception {
    private static final long serialVersionUID = -8888918295071067995L;

    public PDFPageErrorException() {
    }

    public PDFPageErrorException(String str) {
        super(str);
    }
}
