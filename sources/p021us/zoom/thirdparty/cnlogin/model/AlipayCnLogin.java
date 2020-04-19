package p021us.zoom.thirdparty.cnlogin.model;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;
import p021us.zoom.androidlib.p022cn.login.CnLoginCallBack;
import p021us.zoom.androidlib.p022cn.login.CnLoginType;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMIntentUtil;
import p021us.zoom.thirdparty.cnlogin.CnLoginConstants;
import p021us.zoom.thirdparty.cnlogin.CnLoginProxy;

/* renamed from: us.zoom.thirdparty.cnlogin.model.AlipayCnLogin */
public class AlipayCnLogin extends AbstractCnLogin {
    private static final String AUTH_TYPE = "PURE_OAUTH_SDK";
    private static final String PACKAGE_NAME = "com.eg.android.AlipayGphone";
    private static final String SCOPE = "auth_user";
    private static final String STATE = "init";
    private static final String URL = "https://authweb.alipay.com/auth?auth_type=%1$s&app_id=%2$s&scope=%3$s&state=%4$s";
    @Nullable
    private String localSession;
    @Nullable
    private Activity mActivity;
    @Nullable
    private WeakReference<CnLoginCallBack> mCnLoginCallBackWR;

    public void registerApp() {
    }

    public void init(@NonNull Activity activity) {
        this.mActivity = activity;
    }

    public void onResultCallBack(@NonNull ZmCnAuthResult zmCnAuthResult) {
        CnLoginCallBack cnLoginCallBack = getCnLoginCallBack();
        if (cnLoginCallBack != null) {
            if (zmCnAuthResult.getErrorCode() == 9000) {
                AlipayData alipayData = (AlipayData) zmCnAuthResult.getData();
                String str = alipayData.getmCode();
                if (StringUtil.isEmptyOrNull(str)) {
                    cnLoginCallBack.onLoginFail(CnLoginType.Alipay, -10, null);
                    return;
                }
                String str2 = alipayData.getmSession();
                if (TextUtils.isEmpty(str2) || !str2.equals(this.localSession)) {
                    cnLoginCallBack.onLoginFail(CnLoginType.Alipay, -11, null);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(CnLoginConstants.KEY_LOGIN_RESULT_CODE, str);
                cnLoginCallBack.onLoginSuccess(CnLoginType.Alipay, bundle);
            } else if (zmCnAuthResult.getErrorCode() == 4001) {
                cnLoginCallBack.onNotInstalled(CnLoginType.Alipay, "com.eg.android.AlipayGphone");
            } else {
                cnLoginCallBack.onLoginFail(CnLoginType.Alipay, zmCnAuthResult.getErrorCode(), zmCnAuthResult.getErrStr());
            }
        }
    }

    public void unInit() {
        this.mActivity = null;
        WeakReference<CnLoginCallBack> weakReference = this.mCnLoginCallBackWR;
        if (weakReference != null) {
            weakReference.clear();
        }
        this.mCnLoginCallBackWR = null;
    }

    @Nullable
    private CnLoginCallBack getCnLoginCallBack() {
        WeakReference<CnLoginCallBack> weakReference = this.mCnLoginCallBackWR;
        if (weakReference != null) {
            return (CnLoginCallBack) weakReference.get();
        }
        return null;
    }

    public void login(@NonNull CnLoginCallBack cnLoginCallBack) {
        Activity activity = this.mActivity;
        if (activity == null) {
            cnLoginCallBack.onLoginFail(CnLoginType.Alipay, -1, null);
        } else if (!ZMIntentUtil.isInstalled(activity, Uri.parse("alipays://platformapi/startApp"))) {
            cnLoginCallBack.onNotInstalled(CnLoginType.Alipay, "com.eg.android.AlipayGphone");
        } else {
            this.mCnLoginCallBackWR = new WeakReference<>(cnLoginCallBack);
            if (!startReq()) {
                cnLoginCallBack.onLoginFail(CnLoginType.Alipay, -1, null);
            }
        }
    }

    private boolean startReq() {
        if (this.mActivity == null) {
            return false;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.localSession = generateSession(32);
        try {
            String payload = getPayload(elapsedRealtime, this.localSession, buildMqpURL());
            if (StringUtil.isEmptyOrNull(payload)) {
                return false;
            }
            Intent intent = new Intent("android.intent.action.VIEW", new Builder().scheme("alipays").authority("platformapi").path("startapp").appendQueryParameter("appId", "20001129").appendQueryParameter("payload", payload).build());
            intent.addFlags(268435456);
            intent.setPackage("com.eg.android.AlipayGphone");
            this.mActivity.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException | JSONException unused) {
            return false;
        }
    }

    @Nullable
    private String getPayload(long j, String str, String str2) throws JSONException {
        if (this.mActivity == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("startTime", String.valueOf(j));
        jSONObject.put("session", str);
        jSONObject.put("package", this.mActivity.getPackageName());
        jSONObject.put("appId", "20000067");
        jSONObject.put("sdkVersion", "h.a.3.6.4");
        jSONObject.put("mqpURL", str2);
        return Base64.encodeToString(jSONObject.toString().getBytes(CompatUtils.getStardardCharSetUTF8()), 2);
    }

    @Nullable
    private String buildMqpURL() {
        if (this.mActivity == null) {
            return null;
        }
        Builder builder = new Builder();
        builder.scheme("alipays").authority("platformapi").path("startapp");
        builder.appendQueryParameter("appId", "20000067");
        builder.appendQueryParameter("url", String.format(URL, new Object[]{AUTH_TYPE, CnLoginProxy.ALIPAY_CURRENT_APPID, SCOPE, STATE}));
        builder.appendQueryParameter("mqpPkgName", this.mActivity.getPackageName());
        builder.appendQueryParameter("mqpScene", "sdk");
        return builder.build().toString();
    }

    private String generateSession(int i) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < i; i2++) {
            switch (random.nextInt(3)) {
                case 0:
                    sb.append((char) ((int) Math.round((Math.random() * 25.0d) + 65.0d)));
                    break;
                case 1:
                    sb.append((char) ((int) Math.round((Math.random() * 25.0d) + 97.0d)));
                    break;
                case 2:
                    sb.append(new Random().nextInt(10));
                    break;
            }
        }
        return sb.toString();
    }
}
