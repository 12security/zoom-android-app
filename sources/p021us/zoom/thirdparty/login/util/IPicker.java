package p021us.zoom.thirdparty.login.util;

import android.content.Intent;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFragment;

/* renamed from: us.zoom.thirdparty.login.util.IPicker */
public interface IPicker {
    IPickerResult getPickerResult(int i, int i2, Intent intent);

    void startPicking(ZMActivity zMActivity);

    void startPicking(ZMDialogFragment zMDialogFragment);

    void startPicking(ZMFragment zMFragment);
}
