package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.MMChatInfoFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMChatBuddiesGridViewAdapter */
/* compiled from: MMChatBuddiesGridView */
class MMChatBuddiesGridViewAdapter extends BaseAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String BTN_ADD = "Add";
    private static final String BTN_REMOVE = "Remove";
    private static final int ITEM_TYPE_BTN_ADD = 1;
    private static final int ITEM_TYPE_BTN_REMOVE = 2;
    private static final int ITEM_TYPE_BUDDY = 0;
    private static final int ONLY_PLUS = 1;
    private static final int PLUS_MINUS = 2;
    private List<String> admins;
    private boolean isGroupOperatorable = false;
    @NonNull
    private List<MMBuddyItem> items = new ArrayList();
    /* access modifiers changed from: private */
    public MMChatBuddiesGridView mChatBuddiesGridView;
    @Nullable
    private Context mContext;
    private boolean mIsRemoveMode = false;
    private boolean mIsRobot = false;
    private int max = 0;

    public boolean areAllItemsEnabled() {
        return false;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getViewTypeCount() {
        return 3;
    }

    public MMChatBuddiesGridViewAdapter(@Nullable Context context, MMChatBuddiesGridView mMChatBuddiesGridView) {
        this.mContext = context;
        this.mChatBuddiesGridView = mMChatBuddiesGridView;
    }

    public void setGroupOperatorable(boolean z) {
        this.isGroupOperatorable = z;
    }

    public void setGroupAdmin(List<String> list) {
        this.admins = list;
    }

    public void setIsRemoveMode(boolean z) {
        this.mIsRemoveMode = z;
    }

    public boolean isRemoveMode() {
        return this.mIsRemoveMode;
    }

    public boolean ismIsRobot() {
        return this.mIsRobot;
    }

    public void setmIsRobot(boolean z) {
        this.mIsRobot = z;
    }

    @NonNull
    public List<MMBuddyItem> getBuddyItems() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.items);
        return arrayList;
    }

    public void clear() {
        this.items.clear();
    }

    public void addItem(@Nullable MMBuddyItem mMBuddyItem) {
        this.items.add(mMBuddyItem);
    }

    public void setMax(int i) {
        this.max = i;
    }

    public int getMax() {
        return this.max;
    }

    public int getCount() {
        if (this.max > 0) {
            if (this.isGroupOperatorable) {
                if (this.mIsRemoveMode || this.mIsRobot) {
                    return this.items.size() <= MMChatInfoFragment.MAX + -2 ? this.items.size() : MMChatInfoFragment.MAX - 2;
                } else if (this.items.size() == 1) {
                    return this.items.size() + 1;
                } else {
                    return this.items.size() <= MMChatInfoFragment.MAX + -2 ? this.items.size() + 2 : MMChatInfoFragment.MAX;
                }
            } else if (this.mIsRemoveMode || this.mIsRobot) {
                return this.items.size() <= MMChatInfoFragment.MAX - 1 ? this.items.size() : MMChatInfoFragment.MAX - 1;
            } else {
                return this.items.size() <= MMChatInfoFragment.MAX - 1 ? this.items.size() + 1 : MMChatInfoFragment.MAX;
            }
        } else if (this.mIsRemoveMode || this.mIsRobot) {
            return this.items.size();
        } else {
            if (!this.isGroupOperatorable) {
                return this.items.size() + 1;
            }
            if (this.items.size() == 1) {
                return this.items.size() + 1;
            }
            return this.items.size() + 2;
        }
    }

    @Nullable
    public Object getItem(int i) {
        int count = getCount();
        if (i < 0 || i >= count) {
            return null;
        }
        if (this.mIsRemoveMode || this.mIsRobot) {
            return this.items.get(i);
        }
        if (this.isGroupOperatorable) {
            if (count == 2) {
                if (i == count - 1) {
                    return BTN_ADD;
                }
                return this.items.get(i);
            } else if (i == count - 2) {
                return BTN_ADD;
            } else {
                if (i == count - 1) {
                    return BTN_REMOVE;
                }
                return this.items.get(i);
            }
        } else if (i == count - 1) {
            return BTN_ADD;
        } else {
            return this.items.get(i);
        }
    }

    public int getItemViewType(int i) {
        Object item = getItem(i);
        if (!(item instanceof MMBuddyItem)) {
            if (BTN_ADD.equals(item)) {
                return 1;
            }
            if (BTN_REMOVE.equals(item)) {
                return 2;
            }
        }
        return 0;
    }

    public boolean isEnabled(int i) {
        return getItemViewType(i) != 0;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i < 0 || i >= getCount()) {
            return null;
        }
        Object item = getItem(i);
        if (item instanceof MMBuddyItem) {
            return getBuddyView((MMBuddyItem) item, view);
        }
        if (BTN_ADD.equals(item)) {
            return getBtnAddView(view, viewGroup);
        }
        if (BTN_REMOVE.equals(item)) {
            return getBtnRemoveView(view, viewGroup);
        }
        return null;
    }

    public void sort() {
        Collections.sort(this.items, new MMBuddyItemComparator(CompatUtils.getLocalDefault()));
    }

    @NonNull
    private View getBuddyView(@NonNull final MMBuddyItem mMBuddyItem, View view) {
        MMChatBuddyItemView mMChatBuddyItemView;
        if (view instanceof MMChatBuddyItemView) {
            mMChatBuddyItemView = (MMChatBuddyItemView) view;
        } else {
            mMChatBuddyItemView = new MMChatBuddyItemView(this.mContext);
        }
        mMChatBuddyItemView.setScreenName(mMBuddyItem.getScreenName());
        List<String> list = this.admins;
        boolean z = true;
        mMChatBuddyItemView.setAdditionalInfo(list != null && list.size() > 0 && ((String) this.admins.get(0)).equals(mMBuddyItem.buddyJid), mMBuddyItem.getAccountStatus() == 1);
        String screenName = mMBuddyItem.getScreenName();
        List<String> list2 = this.admins;
        mMChatBuddyItemView.setContentDes(screenName, list2 != null && list2.size() > 0 && ((String) this.admins.get(0)).equals(mMBuddyItem.buddyJid));
        if (this.mIsRobot || !this.mIsRemoveMode || mMBuddyItem.isMySelf() || mMBuddyItem.isRobot) {
            z = false;
        }
        mMChatBuddyItemView.setRemoveEnabled(z);
        mMChatBuddyItemView.setOnButtonRemoveClickListener(new OnClickListener() {
            public void onClick(View view) {
                MMChatBuddiesGridViewAdapter.this.mChatBuddiesGridView.onClickBuddyRemoveBtn(mMBuddyItem);
            }
        });
        mMChatBuddyItemView.setOnAvatarClickListener(new OnClickListener() {
            public void onClick(View view) {
                MMChatBuddiesGridViewAdapter.this.mChatBuddiesGridView.onClickBuddyItem(mMBuddyItem);
            }
        });
        if (!mMChatBuddyItemView.isInEditMode()) {
            mMChatBuddyItemView.loadAvatar(this.mContext, mMBuddyItem);
        }
        return mMChatBuddyItemView;
    }

    @NonNull
    private View getBtnAddView(@Nullable View view, ViewGroup viewGroup) {
        if (view == null || !BTN_ADD.equals(view.getTag())) {
            view = LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_mm_chat_add_buddy_btn, viewGroup, false);
            view.setTag(BTN_ADD);
        }
        View findViewById = view.findViewById(C4558R.C4560id.imageButton);
        if (findViewById != null) {
            findViewById.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MMChatBuddiesGridViewAdapter.this.mChatBuddiesGridView.onClickAddBtn();
                }
            });
        }
        return view;
    }

    @NonNull
    private View getBtnRemoveView(@Nullable View view, ViewGroup viewGroup) {
        if (view == null || !BTN_REMOVE.equals(view.getTag())) {
            view = LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_mm_chat_remove_buddy_btn, viewGroup, false);
            view.setTag(BTN_REMOVE);
        }
        View findViewById = view.findViewById(C4558R.C4560id.imageButton);
        if (findViewById != null) {
            findViewById.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MMChatBuddiesGridViewAdapter.this.mChatBuddiesGridView.onClickRemoveBtn();
                }
            });
        }
        return view;
    }
}
