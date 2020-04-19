package p021us.zoom.thirdparty.onedrive;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;

/* renamed from: us.zoom.thirdparty.onedrive.OneDriveDefaultCallback */
public class OneDriveDefaultCallback<T> implements ICallback<T> {
    private static final String SUCCESS_MUST_BE_IMPLEMENTED = "Success must be implemented";

    public void failure(ClientException clientException) {
    }

    public void success(T t) {
        throw new RuntimeException(SUCCESS_MUST_BE_IMPLEMENTED);
    }
}
