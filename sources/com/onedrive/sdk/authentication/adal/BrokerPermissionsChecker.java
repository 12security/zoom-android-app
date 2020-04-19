package com.onedrive.sdk.authentication.adal;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.microsoft.aad.adal.AuthenticationSettings;
import com.onedrive.sdk.authentication.ClientAuthenticatorException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.logger.ILogger;

public class BrokerPermissionsChecker {
    private final String mAdalProjectUrl = "https://github.com/AzureAD/azure-activedirectory-library-for-android";
    private final String[] mBrokerRequirePermissions = {"android.permission.GET_ACCOUNTS", "android.permission.MANAGE_ACCOUNTS", "android.permission.USE_CREDENTIALS"};
    private final Context mContext;
    private final ILogger mLogger;

    public BrokerPermissionsChecker(Context context, ILogger iLogger) {
        this.mContext = context;
        this.mLogger = iLogger;
    }

    public void check() throws ClientAuthenticatorException {
        if (!AuthenticationSettings.INSTANCE.getSkipBroker()) {
            this.mLogger.logDebug("Checking permissions for use with the ADAL Broker.");
            String[] strArr = this.mBrokerRequirePermissions;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                String str = strArr[i];
                if (ContextCompat.checkSelfPermission(this.mContext, str) != -1) {
                    i++;
                } else {
                    String format = String.format("Required permissions to use the Broker are denied: %s, see %s for more details.", new Object[]{str, "https://github.com/AzureAD/azure-activedirectory-library-for-android"});
                    this.mLogger.logDebug(format);
                    throw new ClientAuthenticatorException(format, OneDriveErrorCodes.AuthenicationPermissionsDenied);
                }
            }
            this.mLogger.logDebug("All required permissions found.");
        }
    }
}
