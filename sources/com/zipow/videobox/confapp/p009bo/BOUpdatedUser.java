package com.zipow.videobox.confapp.p009bo;

import java.io.Serializable;

/* renamed from: com.zipow.videobox.confapp.bo.BOUpdatedUser */
public class BOUpdatedUser implements Serializable {
    private static final long serialVersionUID = 1;
    private int mActionType;
    private String mUserGUID;

    public BOUpdatedUser(String str, int i) {
        this.mUserGUID = str;
        this.mActionType = i;
    }

    public String getUserGUID() {
        return this.mUserGUID;
    }

    public int getActionType() {
        return this.mActionType;
    }
}
