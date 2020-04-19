package com.zipow.videobox.ptapp;

import androidx.annotation.Nullable;
import java.io.Serializable;
import p021us.zoom.androidlib.util.StringUtil;

public class ZoomCertItem implements Serializable {
    private static final long serialVersionUID = 1;
    public String ca_finger_print_;
    public String dns_name_;
    public String finger_print_;
    public String host_name_;
    public String issuer_;
    public String serial_number_;

    public ZoomCertItem(String str, String str2, String str3, String str4, String str5, String str6) {
        this.serial_number_ = str;
        this.finger_print_ = str2;
        this.ca_finger_print_ = str3;
        this.dns_name_ = str4;
        this.issuer_ = str5;
        this.host_name_ = str6;
    }

    public boolean equalsIgnoreHostName(@Nullable ZoomCertItem zoomCertItem) {
        boolean z = false;
        if (zoomCertItem == null) {
            return false;
        }
        if (StringUtil.isSameString(this.serial_number_, zoomCertItem.serial_number_) && StringUtil.isSameString(this.finger_print_, zoomCertItem.finger_print_) && StringUtil.isSameString(this.ca_finger_print_, zoomCertItem.ca_finger_print_) && StringUtil.isSameString(this.dns_name_, zoomCertItem.dns_name_) && StringUtil.isSameString(this.issuer_, zoomCertItem.issuer_)) {
            z = true;
        }
        return z;
    }
}
