package p021us.zoom.thirdparty.onedrive;

import com.onedrive.sdk.authentication.AccountType;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.Item;
import java.util.ArrayList;

/* renamed from: us.zoom.thirdparty.onedrive.OneDriveLoadFolderTask */
public class OneDriveLoadFolderTask {
    private static final String EXPAND_OPTIONS_FOR_CHILDREN_AND_THUMBNAILS = "children(expand=thumbnails),thumbnails";
    private static final String EXPAND_OPTIONS_FOR_CHILDREN_AND_THUMBNAILS_LIMITED = "children,thumbnails";
    /* access modifiers changed from: private */
    public IODFoldLoaderListener mIODFoldLoaderListener;
    /* access modifiers changed from: private */
    public boolean mIsCancel;
    private String mItemId;
    /* access modifiers changed from: private */
    public OneDrive mOneDrive;

    /* renamed from: us.zoom.thirdparty.onedrive.OneDriveLoadFolderTask$2 */
    static /* synthetic */ class C45552 {
        static final /* synthetic */ int[] $SwitchMap$com$onedrive$sdk$authentication$AccountType = new int[AccountType.values().length];

        static {
            try {
                $SwitchMap$com$onedrive$sdk$authentication$AccountType[AccountType.MicrosoftAccount.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public OneDriveLoadFolderTask(OneDrive oneDrive, String str, IODFoldLoaderListener iODFoldLoaderListener) {
        this.mOneDrive = oneDrive;
        this.mItemId = str;
        this.mIODFoldLoaderListener = iODFoldLoaderListener;
    }

    private ICallback<Item> getItemCallback() {
        return new OneDriveDefaultCallback<Item>() {
            public void success(Item item) {
                OneDriveLoadFolderTask.this.mOneDrive.removeLoadFolderTask(OneDriveLoadFolderTask.this);
                if (!OneDriveLoadFolderTask.this.mIsCancel) {
                    ArrayList arrayList = new ArrayList();
                    if (item.children != null && !item.children.getCurrentPage().isEmpty()) {
                        for (Item item2 : item.children.getCurrentPage()) {
                            item2.setmPItemId(item.getItemId());
                            arrayList.add(item2);
                        }
                    }
                    OneDriveLoadFolderTask.this.mIODFoldLoaderListener.onLoadFoldCompleted(item, arrayList);
                }
            }

            public void failure(ClientException clientException) {
                OneDriveLoadFolderTask.this.mOneDrive.removeLoadFolderTask(OneDriveLoadFolderTask.this);
                if (!OneDriveLoadFolderTask.this.mIsCancel) {
                    OneDriveLoadFolderTask.this.mIODFoldLoaderListener.onError(clientException);
                }
            }
        };
    }

    public void cancel() {
        this.mIsCancel = true;
    }

    public boolean execute() {
        OneDrive oneDrive = this.mOneDrive;
        if (oneDrive != null && oneDrive.isAuthed()) {
            IOneDriveClient iOneDriveClient = this.mOneDrive.getmClient();
            if (iOneDriveClient != null) {
                iOneDriveClient.getDrive().getItems(this.mItemId).buildRequest().expand(getExpansionOptions(iOneDriveClient)).get(getItemCallback());
                return true;
            }
        }
        return false;
    }

    private String getExpansionOptions(IOneDriveClient iOneDriveClient) {
        return C45552.$SwitchMap$com$onedrive$sdk$authentication$AccountType[iOneDriveClient.getAuthenticator().getAccountInfo().getAccountType().ordinal()] != 1 ? EXPAND_OPTIONS_FOR_CHILDREN_AND_THUMBNAILS_LIMITED : EXPAND_OPTIONS_FOR_CHILDREN_AND_THUMBNAILS;
    }
}
