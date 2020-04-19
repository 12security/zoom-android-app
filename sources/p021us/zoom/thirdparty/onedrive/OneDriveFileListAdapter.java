package p021us.zoom.thirdparty.onedrive;

import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFileListListener;
import p021us.zoom.thirdparty.onedrive.OneDrive.Listener;

/* renamed from: us.zoom.thirdparty.onedrive.OneDriveFileListAdapter */
public class OneDriveFileListAdapter extends BaseOneDriveFileListAdapter implements Listener {
    public void init(ZMActivity zMActivity, ZMFileListListener zMFileListListener) {
        super.init(zMActivity, zMFileListListener);
        this.mOneDrive = OneDriveManager.getInstance().createOneDrive(false);
        this.mOneDrive.setListener(this);
        this.mOneDrive.initial();
    }
}
