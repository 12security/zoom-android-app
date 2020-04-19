package p021us.zoom.thirdparty.onedrive;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Item;
import java.util.List;

/* renamed from: us.zoom.thirdparty.onedrive.IODFoldLoaderListener */
public interface IODFoldLoaderListener {
    void onCanceled(String str);

    void onError(ClientException clientException);

    void onLoadFoldCompleted(Item item, List<Item> list);
}
