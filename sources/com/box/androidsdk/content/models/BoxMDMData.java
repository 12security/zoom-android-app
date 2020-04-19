package com.box.androidsdk.content.models;

import java.util.Map;

public class BoxMDMData extends BoxJsonObject {
    public static final String BILLING_ID = "billing_id";
    public static final String BOX_MDM_DATA = "box_mdm_data";
    public static final String BUNDLE_ID = "bundle_id";
    public static final String EMAIL_ID = "email_id";
    public static final String MANAGEMENT_ID = "management_id";
    public static final String PUBLIC_ID = "public_id";

    public BoxMDMData() {
    }

    public BoxMDMData(Map<String, Object> map) {
        super(map);
    }

    public Object getValue(String str) {
        return this.mProperties.get(str);
    }

    public void setValue(String str, String str2) {
        this.mProperties.put(str, str2);
    }

    public void setBundleId(String str) {
        setValue(BUNDLE_ID, str);
    }

    public void setPublicId(String str) {
        setValue(PUBLIC_ID, str);
    }

    public void setManagementId(String str) {
        setValue(MANAGEMENT_ID, str);
    }

    public void setEmailId(String str) {
        setValue(EMAIL_ID, str);
    }

    public void setBillingId(String str) {
        setValue(BILLING_ID, str);
    }

    public String getBundleId() {
        return (String) getValue(PUBLIC_ID);
    }

    public String getPublicId() {
        return (String) getValue(PUBLIC_ID);
    }

    public String getManagementId() {
        return (String) getValue(MANAGEMENT_ID);
    }

    public String getEmailId() {
        return (String) getValue(EMAIL_ID);
    }

    public String getBillingIdId() {
        return (String) getValue(BILLING_ID);
    }
}
