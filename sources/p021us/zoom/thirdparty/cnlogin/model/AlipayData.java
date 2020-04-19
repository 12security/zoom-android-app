package p021us.zoom.thirdparty.cnlogin.model;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* renamed from: us.zoom.thirdparty.cnlogin.model.AlipayData */
public class AlipayData implements IParseBundle {
    @Nullable
    private String mAppId;
    @Nullable
    private String mCode;
    @Nullable
    private String mResultCode;
    @Nullable
    private String mSession;

    public void parse(@NonNull Bundle bundle) {
        this.mAppId = bundle.getString("app_id");
        this.mSession = bundle.getString("session");
        this.mCode = bundle.getString("auth_code");
    }

    @Nullable
    public String getmAppId() {
        return this.mAppId;
    }

    @Nullable
    public String getmSession() {
        return this.mSession;
    }

    @Nullable
    public String getmResultCode() {
        return this.mResultCode;
    }

    @Nullable
    public String getmCode() {
        return this.mCode;
    }
}
