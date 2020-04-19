package com.alipay.sdk.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import androidx.annotation.Nullable;
import java.util.Iterator;
import org.json.JSONObject;
import p021us.zoom.androidlib.p022cn.login.CnLoginType;
import p021us.zoom.thirdparty.cnlogin.CnLoginProxy;
import p021us.zoom.thirdparty.cnlogin.model.AlipayData;
import p021us.zoom.thirdparty.cnlogin.model.ZmCnAuthResult;

public class AlipayResultActivity extends Activity {
    private static final String TAG = "AlipayResultActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            if (!handleIntent(getIntent())) {
                finish();
            }
        } catch (Exception unused) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        try {
            if (!handleIntent(intent)) {
                finish();
            }
        } catch (Exception unused) {
            finish();
        }
    }

    private boolean handleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return false;
        }
        try {
            String stringExtra = intent.getStringExtra("session");
            Bundle bundleExtra = intent.getBundleExtra("result");
            if (TextUtils.equals("mqpSchemePay", intent.getStringExtra("scene"))) {
                return false;
            }
            if ((TextUtils.isEmpty(stringExtra) || bundleExtra == null) && intent.getData() != null) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(Base64.decode(intent.getData().getQuery(), 2), "UTF-8"));
                    JSONObject jSONObject2 = jSONObject.getJSONObject("result");
                    stringExtra = jSONObject.getString("session");
                    Bundle bundle = new Bundle();
                    try {
                        Iterator keys = jSONObject2.keys();
                        while (keys.hasNext()) {
                            String str = (String) keys.next();
                            bundle.putString(str, jSONObject2.getString(str));
                        }
                        bundleExtra = bundle;
                    } catch (Throwable unused) {
                        bundleExtra = bundle;
                    }
                } catch (Throwable unused2) {
                }
            }
            if (!TextUtils.isEmpty(stringExtra) && bundleExtra != null) {
                try {
                    bundleExtra.putString("session", stringExtra);
                    ZmCnAuthResult zmCnAuthResult = new ZmCnAuthResult(CnLoginType.Alipay, new AlipayData());
                    zmCnAuthResult.parse(bundleExtra);
                    CnLoginProxy.getInstance().onResultCallBack(zmCnAuthResult);
                    finish();
                    return true;
                } catch (Exception unused3) {
                }
            }
            return false;
        } catch (Throwable unused4) {
            return false;
        }
    }
}
