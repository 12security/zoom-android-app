package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.server.CmmSIPLineCallItemBean;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSIPUserBean;
import com.zipow.videobox.view.sip.AbstractSharedLineItem.OnItemClickListener;
import com.zipow.videobox.view.sip.AbstractSharedLineItem.SharedLineItemType;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.BaseViewHolder;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.IPinnedSectionAdapter;

public class SharedLineAdapter extends BaseRecyclerViewAdapter<AbstractSharedLineItem> implements IPinnedSectionAdapter {
    public static final int UPDATE_MODE_ADD = 0;
    public static final int UPDATE_MODE_DELETE = 2;
    public static final int UPDATE_MODE_MODIFY = 1;
    private OnItemClickListener mOnItemClickListener;

    public boolean isPinnedSection(int i) {
        return false;
    }

    public void onChanged() {
    }

    public SharedLineAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context);
        this.mOnItemClickListener = onItemClickListener;
        initData();
    }

    public void initData() {
        this.mData.clear();
        List sharedUsers = CmmSIPLineManager.getInstance().getSharedUsers();
        if (!CollectionsUtil.isListEmpty(sharedUsers)) {
            int i = 0;
            while (i < sharedUsers.size()) {
                SharedLineUserItem sharedLineUserItem = new SharedLineUserItem((CmmSIPUserBean) sharedUsers.get(i), i == 0, i == 1);
                this.mData.add(sharedLineUserItem);
                List lineCallItems = sharedLineUserItem.getLineCallItems();
                if (!lineCallItems.isEmpty()) {
                    Iterator it = lineCallItems.iterator();
                    while (it.hasNext()) {
                        CmmSIPLineCallItemBean cmmSIPLineCallItemBean = (CmmSIPLineCallItemBean) it.next();
                        if (cmmSIPLineCallItemBean != null) {
                            this.mData.add(new SharedLineCallItem(cmmSIPLineCallItemBean, !it.hasNext()));
                        }
                    }
                }
                i++;
            }
        }
    }

    public int getItemViewType(int i) {
        AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) getItem(i);
        if (abstractSharedLineItem != null) {
            return abstractSharedLineItem.getViewType();
        }
        return SharedLineItemType.ITEM_SHARED_LINE.ordinal();
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return AbstractSharedLineItem.createViewHolder(viewGroup, i, this.mOnItemClickListener);
    }

    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        bindViewHolder(baseViewHolder, i, null);
    }

    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i, @NonNull List<Object> list) {
        if (list.isEmpty()) {
            onBindViewHolder(baseViewHolder, i);
        } else {
            bindViewHolder(baseViewHolder, i, list);
        }
    }

    private void bindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i, @Nullable List<Object> list) {
        AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) getItem(i);
        if (abstractSharedLineItem != null) {
            abstractSharedLineItem.bindViewHolder(baseViewHolder, list);
        }
    }

    public void updateCallDuration(int i) {
        notifyItemChanged(i, SharedLineCallItem.PAYLOAD_UPDATE_CALL_DURATION);
    }

    public void onSelfInfoUpdated() {
        if (!this.mData.isEmpty()) {
            notifyItemChanged(0);
        }
    }

    public void onLineUserUpdated(int i, String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (this.mData.isEmpty()) {
                initData();
            }
            int i2 = 0;
            if (i == 0) {
                CmmSIPUserBean sharedUserBeanById = CmmSIPLineManager.getInstance().getSharedUserBeanById(str);
                if (sharedUserBeanById != null) {
                    this.mData.add(new SharedLineUserItem(sharedUserBeanById, false, getAdjacentUserPosition(this.mData.size() - 1, false) == 0));
                    notifyItemInserted(this.mData.size() - 1);
                }
            } else if (i == 1) {
                while (true) {
                    if (i2 >= this.mData.size()) {
                        break;
                    }
                    AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) this.mData.get(i2);
                    if ((abstractSharedLineItem instanceof SharedLineUserItem) && StringUtil.isSameStringForNotAllowNull(str, ((SharedLineUserItem) abstractSharedLineItem).getUserId())) {
                        notifyItemChanged(i2);
                        break;
                    }
                    i2++;
                }
            } else if (i == 2) {
                SharedLineUserItem sharedLineUserItem = null;
                int i3 = 0;
                while (true) {
                    if (i3 >= this.mData.size()) {
                        i3 = 0;
                        break;
                    }
                    AbstractSharedLineItem abstractSharedLineItem2 = (AbstractSharedLineItem) this.mData.get(i3);
                    if (abstractSharedLineItem2 instanceof SharedLineUserItem) {
                        SharedLineUserItem sharedLineUserItem2 = (SharedLineUserItem) abstractSharedLineItem2;
                        if (StringUtil.isSameStringForNotAllowNull(str, sharedLineUserItem2.getUserId())) {
                            sharedLineUserItem = sharedLineUserItem2;
                            break;
                        }
                    }
                    i3++;
                }
                if (sharedLineUserItem != null) {
                    int lineCallCount = sharedLineUserItem.getLineCallCount();
                    if (lineCallCount > 0) {
                        while (i2 < lineCallCount) {
                            this.mData.remove(i3 + 1 + i2);
                            i2++;
                        }
                        notifyItemRangeRemoved(i3 + 1, lineCallCount);
                    }
                    if (sharedLineUserItem.isFirstSharedUser()) {
                        int i4 = i3 + 1;
                        if (i4 < this.mData.size()) {
                            AbstractSharedLineItem abstractSharedLineItem3 = (AbstractSharedLineItem) this.mData.get(i4);
                            if (abstractSharedLineItem3 instanceof SharedLineUserItem) {
                                ((SharedLineUserItem) abstractSharedLineItem3).setIsFirstSharedUser(true);
                            }
                            notifyItemChanged(i4);
                        }
                    }
                    this.mData.remove(i3);
                    notifyItemRemoved(i3);
                }
            }
        }
    }

    public void onLineUpdated(String str) {
        if (this.mData.size() != 0 && !StringUtil.isEmptyOrNull(str)) {
            for (int i = 0; i < this.mData.size(); i++) {
                AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) this.mData.get(i);
                if ((abstractSharedLineItem instanceof SharedLineCallItem) && StringUtil.isSameStringForNotAllowNull(str, ((SharedLineCallItem) abstractSharedLineItem).getLineId())) {
                    notifyItemChanged(i);
                }
            }
        }
    }

    public void onLineCallItemUpdated(int i, String str) {
        if (this.mData.size() != 0 && str != null) {
            if (i == 0) {
                addLineCallItem(str);
            } else if (i == 1) {
                updateLineCallItem(str);
            } else if (i == 2) {
                deleteLineCallItem(str);
            }
        }
    }

    public void onLineCallItemMerged(String str, String str2) {
        deleteLineCallItem(str2);
        updateLineCallItem(str);
    }

    private boolean addLineCallItem(String str) {
        CmmSIPLineCallItemBean lineCallItemBeanById = CmmSIPLineManager.getInstance().getLineCallItemBeanById(str);
        if (lineCallItemBeanById == null || lineCallItemBeanById.isMergedLineCallMember()) {
            return false;
        }
        if (lineCallItemBeanById.getStatus() == 0) {
            CmmSIPLineManager.getInstance().removeLineCallItem(str);
            return false;
        }
        if (this.mData.isEmpty()) {
            initData();
        }
        SharedLineUserItem sharedLineUserItem = null;
        Iterator it = this.mData.iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) it.next();
            if (abstractSharedLineItem instanceof SharedLineUserItem) {
                SharedLineUserItem sharedLineUserItem2 = (SharedLineUserItem) abstractSharedLineItem;
                if (sharedLineUserItem2.containsLineCallItem(str)) {
                    sharedLineUserItem = sharedLineUserItem2;
                    break;
                }
            }
            i++;
        }
        if (sharedLineUserItem == null) {
            return false;
        }
        SharedLineCallItem sharedLineCallItem = new SharedLineCallItem(lineCallItemBeanById, true);
        int lineCallCount = sharedLineUserItem.getLineCallCount() + i;
        if (lineCallCount < this.mData.size()) {
            this.mData.add(lineCallCount, sharedLineCallItem);
        } else {
            this.mData.add(sharedLineCallItem);
        }
        notifyItemInserted(lineCallCount);
        notifyItemChanged(i);
        updateLineItemUIState(lineCallCount - 1);
        return true;
    }

    private void updateLineCallItem(String str) {
        boolean z = false;
        SharedLineUserItem sharedLineUserItem = null;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i >= this.mData.size()) {
                break;
            }
            AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) this.mData.get(i);
            if (sharedLineUserItem == null && (abstractSharedLineItem instanceof SharedLineUserItem)) {
                SharedLineUserItem sharedLineUserItem2 = (SharedLineUserItem) abstractSharedLineItem;
                if (sharedLineUserItem2.containsLineCallItem(str)) {
                    i2 = i;
                    sharedLineUserItem = sharedLineUserItem2;
                }
            }
            if (sharedLineUserItem != null && (abstractSharedLineItem instanceof SharedLineCallItem) && StringUtil.isSameStringForNotAllowNull(str, ((SharedLineCallItem) abstractSharedLineItem).getLineCallId())) {
                notifyItemChanged(i);
                notifyItemChanged(i2);
                z = true;
                break;
            }
            i++;
        }
        if (!z) {
            addLineCallItem(str);
        }
    }

    private void deleteLineCallItem(String str) {
        Iterator it = this.mData.iterator();
        int i = 0;
        while (it.hasNext()) {
            AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) it.next();
            if (!(abstractSharedLineItem instanceof SharedLineCallItem) || !StringUtil.isSameStringForNotAllowNull(str, ((SharedLineCallItem) abstractSharedLineItem).getLineCallId())) {
                i++;
            } else {
                it.remove();
                notifyItemRemoved(i);
                int i2 = i - 1;
                int adjacentUserPosition = getAdjacentUserPosition(i2, false);
                if (adjacentUserPosition >= 0) {
                    notifyItemChanged(adjacentUserPosition);
                }
                updateLineItemUIState(i2);
                return;
            }
        }
    }

    @Nullable
    public int getAdjacentUserPosition(int i, boolean z) {
        while (i >= 0 && i < this.mData.size()) {
            if (((AbstractSharedLineItem) this.mData.get(i)) instanceof SharedLineUserItem) {
                return i;
            }
            i = z ? i + 1 : i - 1;
        }
        return -1;
    }

    public void onLocalCallStateUpdate(String str) {
        int i = 0;
        while (i < this.mData.size()) {
            AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) this.mData.get(i);
            if (!(abstractSharedLineItem instanceof SharedLineCallItem) || !StringUtil.isSameStringForNotAllowNull(((SharedLineCallItem) abstractSharedLineItem).getLocalCallId(), str)) {
                i++;
            } else {
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void notifyBuddyInfoChanged(String str) {
        if (this.mData != null && this.mData.size() > 0 && !StringUtil.isEmptyOrNull(str)) {
            int i = 0;
            while (true) {
                if (i >= this.mData.size()) {
                    break;
                }
                AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) this.mData.get(i);
                if ((abstractSharedLineItem instanceof SharedLineUserItem) && StringUtil.isSameStringForNotAllowNull(str, ((SharedLineUserItem) abstractSharedLineItem).getUserJid())) {
                    notifyItemChanged(i);
                    break;
                }
                i++;
            }
        }
    }

    public void notifyBuddyInfoChanged(List<String> list) {
        if (this.mData != null && this.mData.size() > 0 && !CollectionsUtil.isCollectionEmpty(list)) {
            for (int i = 0; i < this.mData.size(); i++) {
                AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) this.mData.get(i);
                if (abstractSharedLineItem instanceof SharedLineUserItem) {
                    String userJid = ((SharedLineUserItem) abstractSharedLineItem).getUserJid();
                    if (userJid != null && list.contains(userJid)) {
                        notifyItemChanged(i);
                    }
                }
            }
        }
    }

    public void clear() {
        if (this.mData != null) {
            this.mData.clear();
            notifyDataSetChanged();
        }
    }

    private void updateLineItemUIState(int i) {
        boolean z = true;
        if (i >= 1 && i < this.mData.size()) {
            AbstractSharedLineItem abstractSharedLineItem = (AbstractSharedLineItem) this.mData.get(i);
            if (abstractSharedLineItem instanceof SharedLineCallItem) {
                if (i < this.mData.size() - 1 && (((AbstractSharedLineItem) this.mData.get(i + 1)) instanceof SharedLineCallItem)) {
                    z = false;
                }
                SharedLineCallItem sharedLineCallItem = (SharedLineCallItem) abstractSharedLineItem;
                if (z != sharedLineCallItem.isLast()) {
                    sharedLineCallItem.setIsLast(z);
                    notifyItemChanged(i);
                }
            }
        }
    }
}
