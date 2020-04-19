package com.zipow.videobox.view.p014mm.contentfile;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZMSortUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.p014mm.MMChatsListItem;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.contentfile.ContentFileChatListAdapter */
public class ContentFileChatListAdapter extends Adapter<ContentFileChatListHolder> {
    @NonNull
    private static String TAG = ContentFileChatListFragment.class.getSimpleName();
    private Context mContext;
    /* access modifiers changed from: private */
    public List<MMChatsListItem> mDatas;
    /* access modifiers changed from: private */
    public OnRecyclerViewListener mListener;
    private ContentFileChatListFragment mParentFragment;

    /* renamed from: com.zipow.videobox.view.mm.contentfile.ContentFileChatListAdapter$OnRecyclerViewListener */
    public interface OnRecyclerViewListener {
        void onItemClick(String str, String str2);
    }

    public ContentFileChatListAdapter(Context context) {
        this.mContext = context;
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.mListener = onRecyclerViewListener;
    }

    public void setData(List<MMChatsListItem> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void setParentFragment(ContentFileChatListFragment contentFileChatListFragment) {
        this.mParentFragment = contentFileChatListFragment;
    }

    private boolean isParentFragmentResumed() {
        ContentFileChatListFragment contentFileChatListFragment = this.mParentFragment;
        if (contentFileChatListFragment == null) {
            return false;
        }
        return contentFileChatListFragment.isResumed();
    }

    public void updateSeesionInfo(String str) {
        if (!TextUtils.isEmpty(str) && !CollectionsUtil.isListEmpty(this.mDatas)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                boolean z = false;
                for (int i = 0; i < this.mDatas.size(); i++) {
                    MMChatsListItem mMChatsListItem = (MMChatsListItem) this.mDatas.get(i);
                    if (mMChatsListItem != null && StringUtil.isSameString(mMChatsListItem.getSessionId(), str)) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(mMChatsListItem.getSessionId());
                        if (sessionById != null) {
                            MMChatsListItem fromZoomChatSession = MMChatsListItem.fromZoomChatSession(sessionById, zoomMessenger, this.mContext, true);
                            if (fromZoomChatSession != null) {
                                this.mDatas.set(i, fromZoomChatSession);
                                z = true;
                            }
                        }
                    }
                }
                if (z) {
                    List<MMChatsListItem> sortSessions = ZMSortUtil.sortSessions(this.mDatas);
                    if (!CollectionsUtil.isListEmpty(sortSessions)) {
                        this.mDatas = sortSessions;
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Nullable
    public MMChatsListItem getItem(int i) {
        List<MMChatsListItem> list = this.mDatas;
        if (list == null || i < 0 || i > list.size() - 1) {
            return null;
        }
        return (MMChatsListItem) this.mDatas.get(i);
    }

    @NonNull
    public ContentFileChatListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContentFileChatListHolder(LayoutInflater.from(this.mContext).inflate(C4558R.layout.content_file_chat_list_item, viewGroup, false), this.mContext);
    }

    public void onBindViewHolder(@NonNull ContentFileChatListHolder contentFileChatListHolder, final int i) {
        contentFileChatListHolder.bindView((MMChatsListItem) this.mDatas.get(i));
        if (this.mListener != null) {
            contentFileChatListHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ContentFileChatListAdapter.this.mListener.onItemClick(((MMChatsListItem) ContentFileChatListAdapter.this.mDatas.get(i)).getSessionId(), ((MMChatsListItem) ContentFileChatListAdapter.this.mDatas.get(i)).getTitle());
                }
            });
        }
    }

    public int getItemCount() {
        List<MMChatsListItem> list = this.mDatas;
        if (list == null) {
            return 0;
        }
        return list.size();
    }
}
