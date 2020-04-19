package p021us.zoom.thirdparty.login.google;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.common.primitives.UnsignedBytes;
import java.security.SecureRandom;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.thirdparty.login.google.ZmBaseGoogleAuthActivity */
public abstract class ZmBaseGoogleAuthActivity extends ZMActivity {
    public static final String EXTRA_GOOGLE_CLIENT_ID = "EXTRA_GOOGLE_CLIENT_ID";
    public static final String EXTRA_GOOGLE_REDIRECT_URI = "EXTRA_GOOGLE_REDIRECT_URI";
    private static final String TAG = "ZmBaseGoogleAuthActivity";
    private String authStateNonce = null;
    private String clientId;
    private String redirectUri;

    /* access modifiers changed from: protected */
    public abstract void setAuthInfo(String str, String str2);

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        if (bundle == null) {
            this.authStateNonce = null;
        } else {
            this.authStateNonce = bundle.getString("authStateNonce");
        }
        Intent intent = getIntent();
        this.clientId = intent.getStringExtra(EXTRA_GOOGLE_CLIENT_ID);
        this.redirectUri = intent.getStringExtra(EXTRA_GOOGLE_REDIRECT_URI);
        setTheme(16973840);
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("authStateNonce", this.authStateNonce);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!isFinishing()) {
            if (this.authStateNonce != null || this.clientId == null) {
                authFinished();
                return;
            }
            String createStateNonce = createStateNonce();
            startWebAuth(createStateNonce);
            this.authStateNonce = createStateNonce;
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        if (this.authStateNonce == null) {
            finish();
            return;
        }
        Uri data = intent.getData();
        if (data == null) {
            finish();
            return;
        }
        String queryParameter = data.getQueryParameter("code");
        String queryParameter2 = data.getQueryParameter("state");
        String queryParameter3 = data.getQueryParameter("error");
        if (!StringUtil.isEmptyOrNull(queryParameter2)) {
            if (!this.authStateNonce.equals(queryParameter2)) {
                authFinished();
                return;
            }
            setAuthInfo(queryParameter, queryParameter3);
        }
        authFinished();
    }

    private void authFinished() {
        this.authStateNonce = null;
        finish();
    }

    private void startWebAuth(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/drive%20https://www.googleapis.com/auth/drive.file%20https://www.googleapis.com/auth/drive.readonly&redirect_uri=");
        sb.append(this.redirectUri);
        sb.append("&response_type=code&state=");
        sb.append(str);
        sb.append("&client_id=");
        sb.append(this.clientId);
        Uri parse = Uri.parse(sb.toString());
        if (parse != null) {
            Intent intent = new Intent("android.intent.action.VIEW", parse);
            intent.addFlags(268435456);
            intent.addCategory("android.intent.category.BROWSABLE");
            try {
                startActivity(intent);
            } catch (Exception unused) {
            }
        }
    }

    private String createStateNonce() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        StringBuilder sb = new StringBuilder();
        sb.append("oauth2:");
        for (int i = 0; i < 16; i++) {
            sb.append(String.format("%02x", new Object[]{Integer.valueOf(bArr[i] & UnsignedBytes.MAX_VALUE)}));
        }
        return sb.toString();
    }
}
