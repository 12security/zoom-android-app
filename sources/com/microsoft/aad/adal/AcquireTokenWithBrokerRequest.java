package com.microsoft.aad.adal;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Process;

final class AcquireTokenWithBrokerRequest {
    private static final String TAG = "AcquireTokenWithBrokerRequest";
    private final AuthenticationRequest mAuthRequest;
    private final IBrokerProxy mBrokerProxy;

    AcquireTokenWithBrokerRequest(AuthenticationRequest authenticationRequest, IBrokerProxy iBrokerProxy) {
        this.mAuthRequest = authenticationRequest;
        this.mBrokerProxy = iBrokerProxy;
    }

    /* access modifiers changed from: 0000 */
    public AuthenticationResult acquireTokenWithBrokerSilent() throws AuthenticationException {
        AuthenticationResult authenticationResult;
        this.mAuthRequest.setVersion(AuthenticationContext.getVersionName());
        AuthenticationRequest authenticationRequest = this.mAuthRequest;
        authenticationRequest.setBrokerAccountName(authenticationRequest.getLoginHint());
        BrokerEvent startBrokerTelemetryRequest = startBrokerTelemetryRequest("Microsoft.ADAL.broker_request_silent");
        logBrokerVersion(startBrokerTelemetryRequest);
        if (!StringExtensions.isNullOrBlank(this.mAuthRequest.getBrokerAccountName()) || !StringExtensions.isNullOrBlank(this.mAuthRequest.getUserId())) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":acquireTokenWithBrokerSilent");
            Logger.m236v(sb.toString(), "User is specified for background(silent) token request, trying to acquire token silently.");
            authenticationResult = this.mBrokerProxy.getAuthTokenInBackground(this.mAuthRequest, startBrokerTelemetryRequest);
            if (!(authenticationResult == null || authenticationResult.getCliTelemInfo() == null)) {
                startBrokerTelemetryRequest.setSpeRing(authenticationResult.getCliTelemInfo().getSpeRing());
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(TAG);
            sb2.append(":acquireTokenWithBrokerSilent");
            Logger.m236v(sb2.toString(), "User is not specified, skipping background(silent) token request.");
            authenticationResult = null;
        }
        Telemetry.getInstance().stopEvent(startBrokerTelemetryRequest.getTelemetryRequestId(), startBrokerTelemetryRequest, "Microsoft.ADAL.broker_request_silent");
        return authenticationResult;
    }

    /* access modifiers changed from: 0000 */
    public void acquireTokenWithBrokerInteractively(IWindowComponent iWindowComponent) throws AuthenticationException {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":acquireTokenWithBrokerInteractively");
        Logger.m236v(sb.toString(), "Launch activity for interactive authentication via broker.");
        BrokerEvent startBrokerTelemetryRequest = startBrokerTelemetryRequest("Microsoft.ADAL.broker_request_interactive");
        logBrokerVersion(startBrokerTelemetryRequest);
        Intent intentForBrokerActivity = this.mBrokerProxy.getIntentForBrokerActivity(this.mAuthRequest, startBrokerTelemetryRequest);
        if (iWindowComponent == null) {
            throw new AuthenticationException(ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED);
        } else if (intentForBrokerActivity != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(TAG);
            sb2.append(":acquireTokenWithBrokerInteractively");
            String sb3 = sb2.toString();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Calling activity. Pid:");
            sb4.append(Process.myPid());
            sb4.append(" tid:");
            sb4.append(Process.myTid());
            sb4.append("uid:");
            sb4.append(Process.myUid());
            Logger.m236v(sb3, sb4.toString());
            Telemetry.getInstance().stopEvent(startBrokerTelemetryRequest.getTelemetryRequestId(), startBrokerTelemetryRequest, "Microsoft.ADAL.broker_request_interactive");
            iWindowComponent.startActivityForResult(intentForBrokerActivity, 1001);
        } else {
            throw new AuthenticationException(ADALError.DEVELOPER_ACTIVITY_IS_NOT_RESOLVED);
        }
    }

    private void logBrokerVersion(BrokerEvent brokerEvent) {
        String str;
        String currentActiveBrokerPackageName = this.mBrokerProxy.getCurrentActiveBrokerPackageName();
        if (StringExtensions.isNullOrBlank(currentActiveBrokerPackageName)) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":logBrokerVersion");
            Logger.m234i(sb.toString(), "Broker app package name is empty.", "");
            return;
        }
        brokerEvent.setBrokerAppName(currentActiveBrokerPackageName);
        try {
            str = this.mBrokerProxy.getBrokerAppVersion(currentActiveBrokerPackageName);
        } catch (NameNotFoundException unused) {
            str = "N/A";
        }
        brokerEvent.setBrokerAppVersion(str);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Broker app is: ");
        sb2.append(currentActiveBrokerPackageName);
        sb2.append(";Broker app version: ");
        sb2.append(str);
        String sb3 = sb2.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(TAG);
        sb4.append(":logBrokerVersion");
        Logger.m234i(sb4.toString(), sb3, "");
    }

    private BrokerEvent startBrokerTelemetryRequest(String str) {
        BrokerEvent brokerEvent = new BrokerEvent(str);
        brokerEvent.setRequestId(this.mAuthRequest.getTelemetryRequestId());
        Telemetry.getInstance().startEvent(this.mAuthRequest.getTelemetryRequestId(), str);
        return brokerEvent;
    }
}
