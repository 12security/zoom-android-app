package p021us.zoom.videomeetings.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.p022cn.login.CnLoginType;
import p021us.zoom.thirdparty.cnlogin.CnLoginProxy;
import p021us.zoom.thirdparty.cnlogin.model.WeChatData;
import p021us.zoom.thirdparty.cnlogin.model.ZmCnAuthResult;

/* renamed from: us.zoom.videomeetings.wxapi.WXEntryActivity */
public class WXEntryActivity extends Activity {
    private static String TAG = "WXEntryActivity";

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
            if (!ZmWxApiUtil.isIntentFromWx(intent, "com.tencent.mm.openapi.token")) {
                return false;
            }
            String stringExtra = intent.getStringExtra("_mmessage_content");
            int intExtra = intent.getIntExtra("_mmessage_sdkVersion", 0);
            String stringExtra2 = intent.getStringExtra("_mmessage_appPackage");
            if (TextUtils.isEmpty(stringExtra2) || !checkSumConsistent(intent.getByteArrayExtra("_mmessage_checksum"), ZmWxApiUtil.checkSum(stringExtra, intExtra, stringExtra2))) {
                return false;
            }
            int intExtra2 = intent.getIntExtra("_wxapi_command_type", 0);
            Bundle extras = intent.getExtras();
            if (extras != null && intExtra2 == 1) {
                ZmCnAuthResult zmCnAuthResult = new ZmCnAuthResult(CnLoginType.Wechat, new WeChatData());
                zmCnAuthResult.parse(extras);
                CnLoginProxy.getInstance().onResultCallBack(zmCnAuthResult);
                finish();
                return true;
            }
            return false;
        } catch (Exception unused) {
        }
    }

    private boolean checkSumConsistent(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0 || bArr.length != bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }
}
