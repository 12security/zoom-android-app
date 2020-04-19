package com.zipow.videobox.ptapp;

import java.io.Serializable;

public class VerifyCertEvent implements Serializable {
    private static final long serialVersionUID = 1;
    public ZoomCertItem cert_item_;
    public String pending_requestid_;

    public VerifyCertEvent(String str, ZoomCertItem zoomCertItem) {
        this.pending_requestid_ = str;
        this.cert_item_ = zoomCertItem;
    }
}
