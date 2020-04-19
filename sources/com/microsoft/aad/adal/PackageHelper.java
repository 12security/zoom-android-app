package com.microsoft.aad.adal;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class PackageHelper {
    private static final String TAG = "CallerInfo";
    private final AccountManager mAcctManager = AccountManager.get(this.mContext);
    private Context mContext;

    PackageHelper(Context context) {
        this.mContext = context;
    }

    @SuppressLint({"PackageManagerGetSignatures"})
    public String getCurrentSignatureForPackage(String str) {
        try {
            PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(str, 64);
            if (!(packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0)) {
                Signature signature = packageInfo.signatures[0];
                MessageDigest instance = MessageDigest.getInstance("SHA");
                instance.update(signature.toByteArray());
                return Base64.encodeToString(instance.digest(), 2);
            }
        } catch (NameNotFoundException unused) {
            Logger.m231e(TAG, "Calling App's package does not exist in PackageManager. ", "", ADALError.APP_PACKAGE_NAME_NOT_FOUND);
        } catch (NoSuchAlgorithmException unused2) {
            Logger.m231e(TAG, "Digest SHA algorithm does not exists. ", "", ADALError.DEVICE_NO_SUCH_ALGORITHM);
        }
        return null;
    }

    public int getUIDForPackage(String str) {
        try {
            ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(str, 0);
            if (applicationInfo != null) {
                return applicationInfo.uid;
            }
            return 0;
        } catch (NameNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Package name: ");
            sb.append(str);
            Logger.m232e(TAG, "Package is not found. ", sb.toString(), ADALError.PACKAGE_NAME_NOT_FOUND, e);
            return 0;
        }
    }

    public static String getBrokerRedirectUrl(String str, String str2) {
        if (!StringExtensions.isNullOrBlank(str) && !StringExtensions.isNullOrBlank(str2)) {
            try {
                return String.format("%s://%s/%s", new Object[]{Broker.REDIRECT_PREFIX, URLEncoder.encode(str, "UTF_8"), URLEncoder.encode(str2, "UTF_8")});
            } catch (UnsupportedEncodingException e) {
                Logger.m232e(TAG, ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), "", ADALError.ENCODING_IS_NOT_SUPPORTED, e);
            }
        }
        return "";
    }
}
