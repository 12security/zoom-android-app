package p021us.zoom.thirdparty.box;

import java.util.List;

/* renamed from: us.zoom.thirdparty.box.IBoxLoadFolderListener */
public interface IBoxLoadFolderListener {
    void onCancel(BoxAsyncLoadFolder boxAsyncLoadFolder, String str);

    void onCompeleted(BoxAsyncLoadFolder boxAsyncLoadFolder, boolean z, String str, BoxFileObject boxFileObject, List<BoxFileObject> list);

    void onError(BoxAsyncLoadFolder boxAsyncLoadFolder, String str, Exception exc);
}
