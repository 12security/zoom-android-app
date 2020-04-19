package p021us.zoom.thirdparty.login.facebook;

import android.os.Bundle;

/* renamed from: us.zoom.thirdparty.login.facebook.ServiceListener */
public interface ServiceListener {
    void onComplete(Bundle bundle);

    void onError(Error error);

    void onFacebookError(FacebookError facebookError);
}
