package com.zipow.videobox.thirdparty;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.ptapp.FBAuthHelper;
import com.zipow.videobox.ptapp.PTApp;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.googledrive.GoogleDriveMgr;

public class ThirdPartyHandler {
    public static boolean parseAuthResult(@NonNull JoinByURLActivity joinByURLActivity, int i, Uri uri) {
        AuthResult authResult = new AuthResult();
        authResult.setAction(i);
        authResult.setUrl(uri.toString());
        if (i == 8) {
            authResult.setCode(uri.getQueryParameter("token"));
            authResult.setErrorNo(uri.getQueryParameter(AuthResult.CMD_PARAM_ERROR_NO));
            authResult.setErrorMsg(uri.getQueryParameter(AuthResult.CMD_PARAM_ERROR_MSG));
            String queryParameter = uri.getQueryParameter("back");
            if (StringUtil.isEmptyOrNull(authResult.getCode()) && StringUtil.isEmptyOrNull(authResult.getErrorNo())) {
                return true;
            }
            int flags = joinByURLActivity.getIntent().getFlags();
            if (queryParameter != null && queryParameter.equals("googleDrive")) {
                if (!StringUtil.isEmptyOrNull(authResult.getCode())) {
                    FBAuthHelper fBAuthHelper = PTApp.getInstance().getFBAuthHelper();
                    if (fBAuthHelper == null) {
                        return true;
                    }
                    GoogleDriveMgr.getInstance().getGoogleDrive(joinByURLActivity).setAuthCode(fBAuthHelper.decryptGoogleAuthCode(authResult.getCode()));
                }
                if (!StringUtil.isEmptyOrNull(authResult.getErrorMsg())) {
                    GoogleDriveMgr.getInstance().getGoogleDrive(joinByURLActivity).setAuthErrorMsg(authResult.getErrorMsg());
                }
            } else if (!StringUtil.isEmptyOrNull(queryParameter) || PTApp.getInstance().isWebSignedOn() || PTApp.getInstance().autoSignin() || (flags & 1048576) != 0) {
                joinByURLActivity.showWelcomeUI();
            } else {
                LoginActivity.showForAuthUI(joinByURLActivity, authResult);
            }
        } else if (i == 10) {
            authResult.setCode(uri.getQueryParameter("token"));
            authResult.setErrorNo(uri.getQueryParameter(AuthResult.CMD_PARAM_ERROR_NO));
            authResult.setErrorMsg(uri.getQueryParameter(AuthResult.CMD_PARAM_ERROR_MSG));
            authResult.setExpire(uri.getQueryParameter(AuthResult.CMD_PARAM_ERROR_EXPIRE));
            if (StringUtil.isEmptyOrNull(authResult.getCode()) && StringUtil.isEmptyOrNull(authResult.getErrorNo())) {
                return true;
            }
            int flags2 = joinByURLActivity.getIntent().getFlags();
            if (PTApp.getInstance().isWebSignedOn() || PTApp.getInstance().autoSignin() || (flags2 & 1048576) != 0) {
                joinByURLActivity.showWelcomeUI();
            } else {
                LoginActivity.showForAuthUI(joinByURLActivity, authResult);
            }
        } else if (i == 11) {
            authResult.setCode(uri.getQueryParameter("token"));
            authResult.setExtraToken(uri.getQueryParameter(AuthResult.CMD_PARAM_ASTOKEN));
            authResult.setErrorNo(uri.getQueryParameter(AuthResult.CMD_PARAM_ERROR_NO));
            authResult.setErrorMsg(uri.getQueryParameter(AuthResult.CMD_PARAM_ERROR_MSG));
            if (!authResult.isValid() && StringUtil.isEmptyOrNull(authResult.getErrorNo())) {
                return true;
            }
            int flags3 = joinByURLActivity.getIntent().getFlags();
            if (PTApp.getInstance().isWebSignedOn() || PTApp.getInstance().autoSignin() || (flags3 & 1048576) != 0) {
                joinByURLActivity.showWelcomeUI();
            } else {
                LoginActivity.showForAuthUI(joinByURLActivity, authResult);
            }
        }
        return false;
    }
}
