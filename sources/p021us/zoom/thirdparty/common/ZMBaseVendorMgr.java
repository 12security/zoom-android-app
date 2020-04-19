package p021us.zoom.thirdparty.common;

import android.content.Context;
import p021us.zoom.androidlib.app.IZMAppUtil;

/* renamed from: us.zoom.thirdparty.common.ZMBaseVendorMgr */
public abstract class ZMBaseVendorMgr {
    protected static IZMAppUtil mIZMAppUtil;

    public abstract boolean checkValid(Context context);

    public abstract int getmFileIntegrationType();

    public IZMAppUtil getmIZMAppUtil() {
        return mIZMAppUtil;
    }

    public void init(IZMAppUtil iZMAppUtil) {
        mIZMAppUtil = iZMAppUtil;
    }

    public boolean isZoomApp() {
        IZMAppUtil iZMAppUtil = mIZMAppUtil;
        return iZMAppUtil != null && iZMAppUtil.isZoomApp();
    }
}
