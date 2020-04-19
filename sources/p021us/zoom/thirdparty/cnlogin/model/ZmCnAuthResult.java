package p021us.zoom.thirdparty.cnlogin.model;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.provider.FontsContractCompat.Columns;
import p021us.zoom.androidlib.p022cn.login.CnLoginType;
import p021us.zoom.thirdparty.cnlogin.model.IParseBundle;

/* renamed from: us.zoom.thirdparty.cnlogin.model.ZmCnAuthResult */
public class ZmCnAuthResult<T extends IParseBundle> implements IParseBundle {
    @NonNull
    private final CnLoginType mAuthType;
    @NonNull
    private final T mData;
    private String mErrStr;
    private int mErrorCode;

    public ZmCnAuthResult(@NonNull CnLoginType cnLoginType, @NonNull T t) {
        this.mAuthType = cnLoginType;
        this.mData = t;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public CnLoginType getAuthType() {
        return this.mAuthType;
    }

    public String getErrStr() {
        return this.mErrStr;
    }

    public T getData() {
        return this.mData;
    }

    public void parse(@NonNull Bundle bundle) {
        if (this.mAuthType == CnLoginType.Wechat) {
            this.mErrorCode = bundle.getInt("_wxapi_baseresp_errcode");
            this.mErrStr = bundle.getString("_wxapi_baseresp_errstr");
        } else if (this.mAuthType == CnLoginType.Alipay) {
            String string = bundle.getString(Columns.RESULT_CODE);
            if ("SUCCESS".equals(string)) {
                this.mErrorCode = ZmCnAuthErrorCode.f522OK;
            } else {
                this.mErrorCode = 4000;
            }
            this.mErrStr = string;
        }
        this.mData.parse(bundle);
    }
}
