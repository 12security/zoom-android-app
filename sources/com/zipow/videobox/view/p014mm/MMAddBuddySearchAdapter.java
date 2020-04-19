package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMAddBuddySearchAdapter */
public class MMAddBuddySearchAdapter extends Adapter<ViewHolder> {
    public static final int CONTACT_TYPE_EXIST = 2;
    public static final int CONTACT_TYPE_NEW = 1;
    public static final int VIEW_TYPE_EMPTY = 3;
    public static final int VIEW_TYPE_HEADER = 1;
    public static final int VIEW_TYPE_ITEM = 2;
    /* access modifiers changed from: private */
    public int mAction = 1;
    protected Context mContext;
    protected List<IMAddrBookItem> mData;
    private boolean mInitialed = false;
    /* access modifiers changed from: private */
    @Nullable
    public String mKeyword;
    protected OnRecyclerViewListener mListener;

    /* renamed from: com.zipow.videobox.view.mm.MMAddBuddySearchAdapter$OnRecyclerViewListener */
    public interface OnRecyclerViewListener {
        void onActionButtonClick(View view, int i, int i2);

        void onInviteButtonClick(View view, String str);

        void onItemClick(View view, int i);
    }

    /* renamed from: com.zipow.videobox.view.mm.MMAddBuddySearchAdapter$ViewHolder */
    public static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View view) {
            super(view);
        }
    }

    public int getHeaderViewsCount() {
        return 0;
    }

    public MMAddBuddySearchAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList();
    }

    public void setKeyword(String str, int i) {
        if (!TextUtils.isEmpty(str)) {
            this.mKeyword = str;
            this.mInitialed = true;
            this.mAction = i;
        }
    }

    public void clearKeyword() {
        this.mKeyword = null;
        this.mInitialed = false;
        clear();
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutParams layoutParams;
        View view;
        if (i == 3) {
            view = View.inflate(viewGroup.getContext(), C4558R.layout.zm_mm_add_buddy_search_empty, null);
            layoutParams = new LayoutParams(-1, -1);
        } else if (i == 1) {
            view = View.inflate(viewGroup.getContext(), C4558R.layout.zm_mm_add_buddy_search_header, null);
            layoutParams = new LayoutParams(-1, -2);
        } else {
            view = View.inflate(viewGroup.getContext(), C4558R.layout.zm_mm_add_buddy_search_item, null);
            layoutParams = new LayoutParams(-1, -2);
        }
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x00b2  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x00b9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onBindViewHolder(@androidx.annotation.NonNull final com.zipow.videobox.view.p014mm.MMAddBuddySearchAdapter.ViewHolder r9, int r10) {
        /*
            r8 = this;
            int r0 = r9.getItemViewType()
            r1 = 1
            r2 = 0
            r3 = 3
            if (r0 != r3) goto L_0x0048
            android.view.View r10 = r9.itemView
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.text_message
            android.view.View r10 = r10.findViewById(r0)
            android.widget.TextView r10 = (android.widget.TextView) r10
            java.lang.String r0 = r8.mKeyword
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0034
            android.content.Context r0 = r8.mContext
            android.content.res.Resources r0 = r0.getResources()
            int r3 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_could_not_match_on_zoom_48295
            java.lang.String r0 = r0.getString(r3)
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.String r3 = r8.mKeyword
            r1[r2] = r3
            java.lang.String r0 = java.lang.String.format(r0, r1)
            r10.setText(r0)
        L_0x0034:
            android.view.View r9 = r9.itemView
            int r10 = p021us.zoom.videomeetings.C4558R.C4560id.btn_invite
            android.view.View r9 = r9.findViewById(r10)
            android.widget.TextView r9 = (android.widget.TextView) r9
            com.zipow.videobox.view.mm.MMAddBuddySearchAdapter$1 r10 = new com.zipow.videobox.view.mm.MMAddBuddySearchAdapter$1
            r10.<init>()
            r9.setOnClickListener(r10)
            goto L_0x0110
        L_0x0048:
            r3 = 2
            if (r0 != r3) goto L_0x0110
            com.zipow.videobox.view.IMAddrBookItem r10 = r8.getItem(r10)
            if (r10 == 0) goto L_0x0110
            android.view.View r0 = r9.itemView
            int r4 = p021us.zoom.videomeetings.C4558R.C4560id.avatar
            android.view.View r0 = r0.findViewById(r4)
            com.zipow.videobox.view.AvatarView r0 = (com.zipow.videobox.view.AvatarView) r0
            r8.loadAvatar(r10, r0)
            android.view.View r0 = r9.itemView
            int r4 = p021us.zoom.videomeetings.C4558R.C4560id.txtScreenName
            android.view.View r0 = r0.findViewById(r4)
            android.widget.TextView r0 = (android.widget.TextView) r0
            java.lang.String r4 = r10.getScreenName()
            r0.setText(r4)
            android.view.View r0 = r9.itemView
            int r4 = p021us.zoom.videomeetings.C4558R.C4560id.txtEmail
            android.view.View r0 = r0.findViewById(r4)
            android.widget.TextView r0 = (android.widget.TextView) r0
            java.lang.String r4 = r10.getAccountEmail()
            r0.setText(r4)
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r0 = r0.getZoomMessenger()
            if (r0 == 0) goto L_0x0099
            java.lang.String r4 = r10.getJid()
            com.zipow.videobox.ptapp.mm.ZoomBuddy r4 = r0.getBuddyWithJID(r4)
            if (r4 == 0) goto L_0x0099
            boolean r4 = r4.isPending()
            goto L_0x009a
        L_0x0099:
            r4 = 0
        L_0x009a:
            android.view.View r5 = r9.itemView
            int r6 = p021us.zoom.videomeetings.C4558R.C4560id.btnAdd
            android.view.View r5 = r5.findViewById(r6)
            android.widget.TextView r5 = (android.widget.TextView) r5
            android.view.View r6 = r9.itemView
            int r7 = p021us.zoom.videomeetings.C4558R.C4560id.waitApproval
            android.view.View r6 = r6.findViewById(r7)
            android.widget.TextView r6 = (android.widget.TextView) r6
            r7 = 8
            if (r4 == 0) goto L_0x00b9
            r6.setVisibility(r2)
            r5.setVisibility(r7)
            goto L_0x00f2
        L_0x00b9:
            r6.setVisibility(r7)
            r5.setVisibility(r2)
            int r4 = r8.mAction
            if (r4 != r3) goto L_0x00e3
            if (r0 == 0) goto L_0x00cc
            int r4 = r0.imChatGetOption()
            if (r4 != r3) goto L_0x00cc
            goto L_0x00cd
        L_0x00cc:
            r1 = 0
        L_0x00cd:
            if (r1 == 0) goto L_0x00d0
            r2 = 4
        L_0x00d0:
            r5.setVisibility(r2)
            android.content.Context r1 = r8.mContext
            android.content.res.Resources r1 = r1.getResources()
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_description_contact_request_chat
            java.lang.String r1 = r1.getString(r2)
            r5.setText(r1)
            goto L_0x00f2
        L_0x00e3:
            android.content.Context r1 = r8.mContext
            android.content.res.Resources r1 = r1.getResources()
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_btn_invite_buddy_favorite
            java.lang.String r1 = r1.getString(r2)
            r5.setText(r1)
        L_0x00f2:
            com.zipow.videobox.view.mm.MMAddBuddySearchAdapter$2 r1 = new com.zipow.videobox.view.mm.MMAddBuddySearchAdapter$2
            r1.<init>(r9)
            r5.setOnClickListener(r1)
            if (r0 == 0) goto L_0x0110
            java.lang.String r10 = r10.getJid()
            boolean r10 = r0.isMyContact(r10)
            if (r10 == 0) goto L_0x0110
            android.view.View r10 = r9.itemView
            com.zipow.videobox.view.mm.MMAddBuddySearchAdapter$3 r0 = new com.zipow.videobox.view.mm.MMAddBuddySearchAdapter$3
            r0.<init>(r9)
            r10.setOnClickListener(r0)
        L_0x0110:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMAddBuddySearchAdapter.onBindViewHolder(com.zipow.videobox.view.mm.MMAddBuddySearchAdapter$ViewHolder, int):void");
    }

    public int getItemCount() {
        if (!this.mInitialed) {
            return 0;
        }
        List<IMAddrBookItem> list = this.mData;
        if (list == null || list.size() <= 0) {
            return 1;
        }
        return this.mData.size() + 1;
    }

    public int getItemViewType(int i) {
        if (!hasHeader() || i != 0) {
            return isEmptyView() ? 3 : 2;
        }
        return 1;
    }

    public List<IMAddrBookItem> getData() {
        return this.mData;
    }

    public void setData(@Nullable List<IMAddrBookItem> list) {
        if (this.mData == null) {
            this.mData = new ArrayList();
        }
        this.mData.clear();
        if (list != null) {
            this.mData.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addAll(@Nullable List<IMAddrBookItem> list) {
        if (list != null) {
            this.mData.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void remove(int i) {
        List<IMAddrBookItem> list = this.mData;
        if (list != null && i >= 0 && i < list.size()) {
            this.mData.remove(i);
            notifyDataSetChanged();
        }
    }

    public void remove(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            List<IMAddrBookItem> list = this.mData;
            if (list != null) {
                list.remove(iMAddrBookItem);
                notifyDataSetChanged();
            }
        }
    }

    public void clear() {
        List<IMAddrBookItem> list = this.mData;
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public void add(int i, IMAddrBookItem iMAddrBookItem) {
        this.mData.add(i, iMAddrBookItem);
        notifyDataSetChanged();
    }

    public void add(IMAddrBookItem iMAddrBookItem) {
        this.mData.add(iMAddrBookItem);
        notifyDataSetChanged();
    }

    public void update(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            int i = -1;
            Iterator it = this.mData.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                IMAddrBookItem iMAddrBookItem2 = (IMAddrBookItem) it.next();
                if (iMAddrBookItem2.getJid().equals(iMAddrBookItem.getJid())) {
                    i = this.mData.indexOf(iMAddrBookItem2);
                    break;
                }
            }
            if (i <= this.mData.size() && i >= 0) {
                this.mData.set(i, iMAddrBookItem);
                notifyItemChanged(i);
            }
        }
    }

    public boolean hasHeader() {
        List<IMAddrBookItem> list = this.mData;
        return list != null && list.size() > 0;
    }

    public boolean isEmptyView() {
        return !hasHeader() && getItemCount() == 1;
    }

    @Nullable
    public IMAddrBookItem getItem(int i) {
        if (this.mData == null || i >= getItemCount()) {
            return null;
        }
        List<IMAddrBookItem> list = this.mData;
        if (hasHeader()) {
            i--;
        }
        return (IMAddrBookItem) list.get(i);
    }

    private void loadAvatar(@NonNull IMAddrBookItem iMAddrBookItem, @Nullable AvatarView avatarView) {
        if (avatarView != null && !avatarView.isInEditMode()) {
            avatarView.show(iMAddrBookItem.getAvatarParamsBuilder());
        }
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.mListener = onRecyclerViewListener;
    }
}
