package p021us.zoom.videomeetings.wxapi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.common.base.Ascii;
import java.security.MessageDigest;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* renamed from: us.zoom.videomeetings.wxapi.ZmWxApiUtil */
public class ZmWxApiUtil {
    public static final int COMMAND_SENDAUTH = 1;
    private static final String TAG = ZmWxApi.class.getSimpleName();

    @NonNull
    public static byte[] checkSum(String str, int i, String str2) {
        StringBuilder sb = new StringBuilder();
        if (str != null) {
            sb.append(str);
        }
        sb.append(i);
        sb.append(str2);
        sb.append("mMcShCsTr");
        String md5 = getMD5(sb.toString().substring(1, 9).getBytes());
        return md5 == null ? new byte[0] : md5.getBytes();
    }

    @Nullable
    public static final String getMD5(byte[] bArr) {
        byte[] digest;
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            instance.update(bArr);
            char[] cArr2 = new char[(r1 * 2)];
            int i = 0;
            for (byte b : instance.digest()) {
                int i2 = i + 1;
                cArr2[i] = cArr[(b >>> 4) & 15];
                i = i2 + 1;
                cArr2[i2] = cArr[b & Ascii.f228SI];
            }
            return new String(cArr2);
        } catch (Exception unused) {
            return null;
        }
    }

    public static boolean validateAppSignatureForPackage(@Nullable Context context, @Nullable String str) {
        boolean z = false;
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 64);
            if (!(packageInfo == null || packageInfo.signatures == null || !validateAppSignature(packageInfo.signatures))) {
                z = true;
            }
            return z;
        } catch (NameNotFoundException unused) {
            return false;
        } catch (Exception unused2) {
            return false;
        }
    }

    public static boolean validateAppSignature(@NonNull Signature[] signatureArr) {
        for (Signature signature : signatureArr) {
            if (signature != null && signature.toCharsString().toLowerCase().equals("308202eb30820254a00302010202044d36f7a4300d06092a864886f70d01010505003081b9310b300906035504061302383631123010060355040813094775616e67646f6e673111300f060355040713085368656e7a68656e31353033060355040a132c54656e63656e7420546563686e6f6c6f6779285368656e7a68656e2920436f6d70616e79204c696d69746564313a3038060355040b133154656e63656e74204775616e677a686f7520526573656172636820616e6420446576656c6f706d656e742043656e7465723110300e0603550403130754656e63656e74301e170d3131303131393134333933325a170d3431303131313134333933325a3081b9310b300906035504061302383631123010060355040813094775616e67646f6e673111300f060355040713085368656e7a68656e31353033060355040a132c54656e63656e7420546563686e6f6c6f6779285368656e7a68656e2920436f6d70616e79204c696d69746564313a3038060355040b133154656e63656e74204775616e677a686f7520526573656172636820616e6420446576656c6f706d656e742043656e7465723110300e0603550403130754656e63656e7430819f300d06092a864886f70d010101050003818d0030818902818100c05f34b231b083fb1323670bfbe7bdab40c0c0a6efc87ef2072a1ff0d60cc67c8edb0d0847f210bea6cbfaa241be70c86daf56be08b723c859e52428a064555d80db448cdcacc1aea2501eba06f8bad12a4fa49d85cacd7abeb68945a5cb5e061629b52e3254c373550ee4e40cb7c8ae6f7a8151ccd8df582d446f39ae0c5e930203010001300d06092a864886f70d0101050500038181009c8d9d7f2f908c42081b4c764c377109a8b2c70582422125ce545842d5f520aea69550b6bd8bfd94e987b75a3077eb04ad341f481aac266e89d3864456e69fba13df018acdc168b9a19dfd7ad9d9cc6f6ace57c746515f71234df3a053e33ba93ece5cd0fc15f3e389a3f365588a9fcb439e069d3629cd7732a13fff7b891499")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIntentFromWx(@Nullable Intent intent, @NonNull String str) {
        boolean z = false;
        if (intent == null) {
            return false;
        }
        String stringExtra = intent.getStringExtra("wx_token_key");
        if (stringExtra != null && stringExtra.equals(str)) {
            z = true;
        }
        return z;
    }

    public static boolean checkArgs(@Nullable String str, @Nullable String str2) {
        boolean z = false;
        if (TextUtils.isEmpty(str) || str.length() > 1024) {
            return false;
        }
        if (str2 == null || str2.length() <= 1024) {
            z = true;
        }
        return z;
    }
}
