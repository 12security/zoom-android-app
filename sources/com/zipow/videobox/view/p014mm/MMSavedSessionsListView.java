package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSavedSessionsListView */
public class MMSavedSessionsListView extends ListView implements OnItemClickListener {
    private SavedSessionsAdapter mAdapter;

    /* renamed from: com.zipow.videobox.view.mm.MMSavedSessionsListView$SavedSession */
    private static class SavedSession {
        private int mGroupNums;
        private boolean mIsE2E;
        private String mJid;
        private String mSessionName;

        private SavedSession() {
        }

        public String getSessionName() {
            return this.mSessionName;
        }

        public void setSessionName(String str) {
            this.mSessionName = str;
        }

        public int getGroupNums() {
            return this.mGroupNums;
        }

        public void setGroupNums(int i) {
            this.mGroupNums = i;
        }

        public String getJid() {
            return this.mJid;
        }

        public void setJid(String str) {
            this.mJid = str;
        }

        public boolean isE2E() {
            return this.mIsE2E;
        }

        public void setE2E(boolean z) {
            this.mIsE2E = z;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSavedSessionsListView$SavedSessionsAdapter */
    private static class SavedSessionsAdapter extends BaseAdapter {
        private Context mContext;
        @Nullable
        private List<SavedSession> mData;
        @Nullable
        private String mFilter;
        @NonNull
        private List<SavedSession> mVisiabelSessions = new ArrayList();

        public long getItemId(int i) {
            return 0;
        }

        public SavedSessionsAdapter(Context context, @Nullable List<SavedSession> list) {
            this.mContext = context;
            this.mData = list;
            if (list != null) {
                this.mVisiabelSessions.addAll(this.mData);
            }
        }

        public void addItem(@Nullable SavedSession savedSession) {
            if (savedSession != null) {
                if (this.mData == null) {
                    this.mData = new ArrayList();
                }
                this.mData.add(savedSession);
            }
        }

        public void addItems(List<SavedSession> list) {
            this.mData = list;
            filter(this.mFilter);
        }

        public void filter(@Nullable String str) {
            this.mVisiabelSessions.clear();
            Locale localDefault = CompatUtils.getLocalDefault();
            if (str != null) {
                str = str.toLowerCase(localDefault);
            }
            this.mFilter = str;
            if (this.mData != null) {
                if (!StringUtil.isEmptyOrNull(str)) {
                    for (SavedSession savedSession : this.mData) {
                        String sessionName = savedSession.getSessionName();
                        if (!StringUtil.isEmptyOrNull(sessionName) && sessionName.toLowerCase(localDefault).contains(str)) {
                            this.mVisiabelSessions.add(savedSession);
                        }
                    }
                } else {
                    this.mVisiabelSessions.addAll(this.mData);
                }
            }
            notifyDataSetChanged();
        }

        public int getCount() {
            return this.mVisiabelSessions.size();
        }

        @Nullable
        public SavedSession getItem(int i) {
            if (this.mVisiabelSessions.size() <= i || i < 0) {
                return null;
            }
            return (SavedSession) this.mVisiabelSessions.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(this.mContext, C4558R.layout.zm_addrbook_item, null);
            }
            SavedSession item = getItem(i);
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtGroupNums);
            view.findViewById(C4558R.C4560id.presenceStateView).setVisibility(8);
            view.findViewById(C4558R.C4560id.txtCustomMessage).setVisibility(8);
            int i2 = 0;
            textView.setVisibility(0);
            StringBuilder sb = new StringBuilder();
            sb.append(item.getGroupNums());
            sb.append("");
            textView.setText(sb.toString());
            ((AvatarView) view.findViewById(C4558R.C4560id.avatarView)).show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_ic_avatar_group, null));
            ((TextView) view.findViewById(C4558R.C4560id.txtScreenName)).setText(item.getSessionName());
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgE2EFlag);
            if (!item.isE2E()) {
                i2 = 8;
            }
            imageView.setVisibility(i2);
            return view;
        }
    }

    private static void startGroupChat(@NonNull ZMActivity zMActivity, String str) {
        MMChatActivity.showAsGroupChat(zMActivity, str);
    }

    public MMSavedSessionsListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public MMSavedSessionsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMSavedSessionsListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mAdapter = new SavedSessionsAdapter(getContext(), null);
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        reloadAll();
    }

    public void onGroupAction(int i, GroupAction groupAction, String str) {
        reloadAll();
    }

    public void onNotify_ChatSessionListUpdate() {
        reloadAll();
    }

    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        reloadAll();
    }

    public void reloadAll() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            List<String> savedSessionGetAll = zoomMessenger.savedSessionGetAll();
            ArrayList arrayList = new ArrayList();
            if (savedSessionGetAll != null) {
                for (String str : savedSessionGetAll) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(str);
                    if (groupById != null) {
                        SavedSession savedSession = new SavedSession();
                        savedSession.setGroupNums(groupById.getBuddyCount());
                        savedSession.setJid(str);
                        savedSession.setSessionName(groupById.getGroupDisplayName(getContext()));
                        savedSession.setE2E(groupById.isForceE2EGroup());
                        arrayList.add(savedSession);
                    }
                }
            }
            this.mAdapter.addItems(arrayList);
        }
    }

    public void filter(String str) {
        SavedSessionsAdapter savedSessionsAdapter = this.mAdapter;
        if (savedSessionsAdapter != null) {
            savedSessionsAdapter.filter(str);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        SavedSession item = this.mAdapter.getItem(i);
        if (item != null) {
            onSessionClick(item);
        }
    }

    private void onSessionClick(@Nullable SavedSession savedSession) {
        if (savedSession != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(savedSession.getJid());
                if (groupById != null) {
                    String groupID = groupById.getGroupID();
                    if (!StringUtil.isEmptyOrNull(groupID)) {
                        startGroupChat((ZMActivity) getContext(), groupID);
                    }
                }
            }
        }
    }
}
