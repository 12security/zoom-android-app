package p021us.zoom.videomeetings;

import p021us.zoom.androidlib.app.preference.ZMBasePreferencesProvider;

/* renamed from: us.zoom.videomeetings.ZMPreferencesProvider */
public class ZMPreferencesProvider extends ZMBasePreferencesProvider {
    public String getAuthorities() {
        return getContext().getString(C4558R.string.zm_app_pref_provider);
    }
}
