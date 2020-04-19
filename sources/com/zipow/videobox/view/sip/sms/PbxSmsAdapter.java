package com.zipow.videobox.view.sip.sms;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact;
import com.zipow.videobox.view.sip.sms.PbxSmsRecyleView.PbxSmsUICallBack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PbxSmsAdapter extends Adapter<ViewHolder> {
    @NonNull
    private Context mContext;
    @NonNull
    private List<PBXMessageItem> mDatas = new ArrayList();
    @NonNull
    private List<PBXMessageItem> mDisplayDatas = new ArrayList();
    @Nullable
    private String mSessionId;
    @Nullable
    private PbxSmsUICallBack mUICallBack;

    public PbxSmsAdapter(@NonNull Context context) {
        setHasStableIds(true);
        registerAdapterDataObserver(new AdapterDataObserver() {
            public void onChanged() {
                PbxSmsAdapter.this.rebuildListItems();
            }
        });
        this.mContext = context;
    }

    public void setUICallBack(@Nullable PbxSmsUICallBack pbxSmsUICallBack) {
        this.mUICallBack = pbxSmsUICallBack;
    }

    public void setSessionId(@Nullable String str) {
        this.mSessionId = str;
    }

    /* access modifiers changed from: private */
    public void rebuildListItems() {
        this.mDisplayDatas.clear();
        for (int i = 0; i < this.mDatas.size(); i++) {
            PBXMessageItem pBXMessageItem = (PBXMessageItem) this.mDatas.get(i);
            pBXMessageItem.setOnlyMessageShow(false);
            if (i != 0) {
                PBXMessageItem pBXMessageItem2 = (PBXMessageItem) this.mDatas.get(i - 1);
                PBXMessageContact fromContact = pBXMessageItem2.getFromContact();
                PBXMessageContact fromContact2 = pBXMessageItem.getFromContact();
                if (fromContact != null && fromContact2 != null && TextUtils.equals(fromContact.getPhoneNumber(), fromContact2.getPhoneNumber()) && !pBXMessageItem2.isSystemMsg()) {
                    pBXMessageItem.setOnlyMessageShow(true);
                }
            }
            addItemToListItems(pBXMessageItem);
        }
    }

    public void clearData() {
        this.mDatas.clear();
    }

    public void addMessages(@NonNull List<PBXMessageItem> list, boolean z) {
        if (list.size() > 1 && ((PBXMessageItem) list.get(0)).getTimestamp() > ((PBXMessageItem) list.get(list.size() - 1)).getTimestamp()) {
            Collections.reverse(list);
        }
        if (this.mDatas.isEmpty()) {
            this.mDatas.addAll(list);
        } else if (z) {
            this.mDatas.addAll(list);
        } else {
            this.mDatas.addAll(0, list);
        }
    }

    public void addOrUpdateMsg(@NonNull PBXMessageItem pBXMessageItem) {
        addOrUpdateMsg(pBXMessageItem, false);
    }

    public boolean addOrUpdateMsg(@NonNull PBXMessageItem pBXMessageItem, boolean z) {
        boolean z2 = false;
        for (int i = 0; i < this.mDatas.size(); i++) {
            if (TextUtils.equals(((PBXMessageItem) this.mDatas.get(i)).getId(), pBXMessageItem.getId())) {
                this.mDatas.set(i, pBXMessageItem);
                return true;
            }
        }
        if (z) {
            return false;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= this.mDatas.size()) {
                break;
            } else if (((PBXMessageItem) this.mDatas.get(i2)).getTimestamp() > pBXMessageItem.getTimestamp()) {
                this.mDatas.add(i2, pBXMessageItem);
                z2 = true;
                break;
            } else {
                i2++;
            }
        }
        if (!z2) {
            this.mDatas.add(pBXMessageItem);
        }
        return true;
    }

    public boolean deleteMsgs(@NonNull List<String> list) {
        boolean z = false;
        for (String str : list) {
            int i = 0;
            while (true) {
                if (i >= this.mDatas.size()) {
                    break;
                } else if (TextUtils.equals(((PBXMessageItem) this.mDatas.get(i)).getId(), str)) {
                    this.mDatas.remove(i);
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
        }
        return z;
    }

    private void addItemToListItems(@NonNull PBXMessageItem pBXMessageItem) {
        PBXMessageItem pBXMessageItem2;
        long timestamp;
        if (this.mDisplayDatas.size() > 0) {
            int lastTimeItemIndex = getLastTimeItemIndex();
            if (lastTimeItemIndex >= 0) {
                pBXMessageItem2 = (PBXMessageItem) this.mDisplayDatas.get(lastTimeItemIndex);
                timestamp = pBXMessageItem.getTimestamp();
                if (pBXMessageItem2 == null || timestamp - pBXMessageItem2.getTimestamp() > 300000 || 999 + timestamp < pBXMessageItem2.getTimestamp()) {
                    this.mDisplayDatas.add(PBXMessageItem.createTimeStampMsg(this.mSessionId, timestamp));
                    pBXMessageItem.setOnlyMessageShow(false);
                }
                this.mDisplayDatas.add(pBXMessageItem);
            }
        }
        pBXMessageItem2 = null;
        timestamp = pBXMessageItem.getTimestamp();
        this.mDisplayDatas.add(PBXMessageItem.createTimeStampMsg(this.mSessionId, timestamp));
        pBXMessageItem.setOnlyMessageShow(false);
        this.mDisplayDatas.add(pBXMessageItem);
    }

    private int getLastTimeItemIndex() {
        if (this.mDisplayDatas.size() == 0) {
            return -1;
        }
        for (int itemCount = getItemCount() - 1; itemCount >= 0; itemCount--) {
            if (((PBXMessageItem) this.mDisplayDatas.get(itemCount)).getMessageType() == 1) {
                return itemCount;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return this.mDatas.isEmpty();
    }

    public int findIndexInAdapter(@NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            if (TextUtils.equals(str, ((PBXMessageItem) this.mDisplayDatas.get(i)).getId())) {
                return i;
            }
        }
        return -1;
    }

    public int getItemViewType(int i) {
        PBXMessageItem item = getItem(i);
        if (item != null) {
            return item.getMessageType();
        }
        return 0;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        AbsSmsView createView = PBXMessageItem.createView(this.mContext, i);
        C41942 r4 = new ViewHolder(createView == 0 ? new View(this.mContext) : createView) {
        };
        if (createView != 0) {
            createView.setOnShowContextMenuListener(this.mUICallBack);
            createView.setOnClickMessageListener(this.mUICallBack);
            createView.setOnClickStatusImageListener(this.mUICallBack);
            createView.setOnClickMeetingNOListener(this.mUICallBack);
            createView.setOnClickLinkPreviewListener(this.mUICallBack);
        }
        return r4;
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PBXMessageItem item = getItem(i);
        if (item != null) {
            item.bindViewHolder(viewHolder);
            PbxSmsUICallBack pbxSmsUICallBack = this.mUICallBack;
            if (pbxSmsUICallBack != null) {
                pbxSmsUICallBack.onMessageShowed(item);
            }
        }
    }

    public int getItemCount() {
        return this.mDisplayDatas.size();
    }

    public long getItemId(int i) {
        PBXMessageItem item = getItem(i);
        if (item == null || item.getId() == null) {
            return -1;
        }
        return (long) item.getId().hashCode();
    }

    public PBXMessageItem getItem(int i) {
        if (i < 0 || i >= this.mDisplayDatas.size()) {
            return null;
        }
        return (PBXMessageItem) this.mDisplayDatas.get(i);
    }

    public PBXMessageItem getItemById(@NonNull String str) {
        for (PBXMessageItem pBXMessageItem : this.mDatas) {
            if (TextUtils.equals(pBXMessageItem.getId(), str)) {
                return pBXMessageItem;
            }
        }
        return null;
    }
}
