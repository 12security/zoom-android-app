package p021us.zoom.thirdparty.cnlogin.model;

import android.os.Bundle;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.ZMLog;

/* renamed from: us.zoom.thirdparty.cnlogin.model.WeChatData */
public class WeChatData implements IParseBundle {
    private static final String TAG = "WeChatData";
    @Nullable
    private String mCode;
    @Nullable
    private String mOpenId;
    @Nullable
    private String mState;
    @Nullable
    private String mTransaction;

    public void parse(Bundle bundle) {
        this.mTransaction = bundle.getString("_wxapi_baseresp_transaction");
        this.mOpenId = bundle.getString("_wxapi_baseresp_openId");
        this.mCode = bundle.getString("_wxapi_sendauth_resp_token");
        this.mState = bundle.getString("_wxapi_sendauth_resp_state");
    }

    @Nullable
    public String getmTransaction() {
        return this.mTransaction;
    }

    @Nullable
    public String getmOpenId() {
        return this.mOpenId;
    }

    @Nullable
    public String getmCode() {
        return this.mCode;
    }

    @Nullable
    public String getmState() {
        return this.mState;
    }

    public boolean checkArgs() {
        String str = this.mState;
        if (str == null || str.length() <= 1024) {
            return true;
        }
        ZMLog.m280e(TAG, "checkArgs fail, state is invalid", new Object[0]);
        return false;
    }
}
