package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.IMSubscription;
import com.zipow.videobox.ptapp.PTApp;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.videomeetings.C4558R;

public class BuddyInviteListView extends ListView {
    private BuddyInviteListAdapter mAdapter;

    static class BuddyInviteListAdapter extends BaseAdapter {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final int ITEM_TYPE_NORMAL = 0;
        @Nullable
        private Context mContext;
        @NonNull
        private List<IMSubscription> mItems = new ArrayList();
        private BuddyInviteListView mListView;

        public long getItemId(int i) {
            return (long) i;
        }

        public int getItemViewType(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        static {
            Class<BuddyInviteListView> cls = BuddyInviteListView.class;
        }

        public BuddyInviteListAdapter(@Nullable Context context, BuddyInviteListView buddyInviteListView) {
            this.mContext = context;
            this.mListView = buddyInviteListView;
        }

        public void clear() {
            this.mItems.clear();
        }

        public void addItem(IMSubscription iMSubscription) {
            updateItem(iMSubscription);
        }

        public void updateItem(@Nullable IMSubscription iMSubscription) {
            int findItem = findItem(iMSubscription.getJid());
            if (findItem >= 0) {
                this.mItems.set(findItem, iMSubscription);
            } else {
                this.mItems.add(iMSubscription);
            }
        }

        public int findItem(@Nullable String str) {
            if (str == null) {
                return -1;
            }
            for (int i = 0; i < this.mItems.size(); i++) {
                if (str.equals(((IMSubscription) this.mItems.get(i)).getJid())) {
                    return i;
                }
            }
            return -1;
        }

        public void removeItem(String str) {
            int findItem = findItem(str);
            if (findItem >= 0) {
                removeItemAt(findItem);
            }
        }

        public void removeItemAt(int i) {
            if (i >= 0 && i < this.mItems.size()) {
                this.mItems.remove(i);
            }
        }

        public int getCount() {
            return this.mItems.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            return this.mItems.get(i);
        }

        @Nullable
        public IMSubscription getItemByJid(String str) {
            for (IMSubscription iMSubscription : this.mItems) {
                if (iMSubscription.getJid().equals(str)) {
                    return iMSubscription;
                }
            }
            return null;
        }

        @Nullable
        public View getView(final int i, @Nullable View view, ViewGroup viewGroup) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            IMSubscription iMSubscription = (IMSubscription) getItem(i);
            if (view == null || !"BuddyInviteItem".equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_buddy_invite_item, null);
                view.setTag("BuddyInviteItem");
            }
            Button button = (Button) view.findViewById(C4558R.C4560id.btnAccept);
            Button button2 = (Button) view.findViewById(C4558R.C4560id.btnDecline);
            ((TextView) view.findViewById(C4558R.C4560id.txtJid)).setText(iMSubscription.getJid());
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    BuddyInviteListAdapter.this.acknowledgeSubscription(i, true);
                }
            });
            button2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    BuddyInviteListAdapter.this.acknowledgeSubscription(i, false);
                }
            });
            return view;
        }

        /* access modifiers changed from: private */
        public void acknowledgeSubscription(int i, boolean z) {
            IMSubscription iMSubscription = (IMSubscription) getItem(i);
            if (iMSubscription != null) {
                IMHelper iMHelper = PTApp.getInstance().getIMHelper();
                if (iMHelper != null) {
                    iMHelper.acknowledgeSubscription(iMSubscription.getJid(), z);
                    this.mListView.reloadAllItems();
                }
            }
        }
    }

    public BuddyInviteListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public BuddyInviteListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public BuddyInviteListView(Context context) {
        super(context);
        initView();
    }

    public void reloadAllItems() {
        this.mAdapter.clear();
        loadAllItems(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
        setVisibility(this.mAdapter.isEmpty() ? 4 : 0);
    }

    private void initView() {
        this.mAdapter = new BuddyInviteListAdapter(getContext(), this);
        if (isInEditMode()) {
            _editmode_loadAllItems(this.mAdapter);
        } else {
            loadAllItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
    }

    private void _editmode_loadAllItems(@NonNull BuddyInviteListAdapter buddyInviteListAdapter) {
        for (int i = 0; i < 10; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("buddy");
            sb.append(i);
            sb.append("@zoom..us");
            buddyInviteListAdapter.addItem(new IMSubscription(sb.toString(), "test", 0));
        }
    }

    private void loadAllItems(@NonNull BuddyInviteListAdapter buddyInviteListAdapter) {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            IMSubscription[] unhandledSubscriptions = iMHelper.getUnhandledSubscriptions();
            if (unhandledSubscriptions != null) {
                for (IMSubscription addItem : unhandledSubscriptions) {
                    buddyInviteListAdapter.addItem(addItem);
                }
            }
        }
    }
}
