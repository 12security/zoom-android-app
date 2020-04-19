package p021us.zoom.thirdparty.login.facebook;

import android.os.Bundle;

/* renamed from: us.zoom.thirdparty.login.facebook.FBAppAuthCallBack */
public interface FBAppAuthCallBack {
    void forceAuthByBrowser();

    void onCancelAuth();

    void onComplete(Bundle bundle);

    void onError(String str);

    void onFaceBookError(FacebookError facebookError);
}
