package com.zipow.videobox.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMSendMessageFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class ABItemDetailsList extends ListView implements OnItemClickListener, OnItemLongClickListener {
    @NonNull
    private final ABItemDetailsListAdapter mAdapter;

    static class ABItemDetailsListAdapter extends BaseAdapter {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final int ITEM_TYPE_NORMAL = 0;
        @Nullable
        private Context mContext;
        @NonNull
        private List<DetailListItem> mItems = new ArrayList();

        public int getItemViewType(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        static {
            Class<ABItemDetailsList> cls = ABItemDetailsList.class;
        }

        public ABItemDetailsListAdapter(@Nullable Context context) {
            this.mContext = context;
        }

        public void clear() {
            this.mItems.clear();
        }

        public void addItem(@Nullable DetailListItem detailListItem) {
            this.mItems.add(detailListItem);
        }

        public int getCount() {
            return this.mItems.size();
        }

        @Nullable
        public DetailListItem getItem(int i) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            return (DetailListItem) this.mItems.get(i);
        }

        public long getItemId(int i) {
            DetailListItem item = getItem(i);
            if (item == null) {
                return 0;
            }
            return (long) item.hashCode();
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = null;
            if (i < 0 || i >= getCount()) {
                return null;
            }
            if (getItemViewType(i) == 0) {
                view2 = createNormalItemView(i, view, viewGroup);
            }
            return view2;
        }

        private View createNormalItemView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            DetailListItem item = getItem(i);
            if (view == null || !"ItemDetails".equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_abitem_details_list_item, null);
                view.setTag("ItemDetails");
            }
            ((TextView) view.findViewById(C4558R.C4560id.txtLabel)).setText(item.toString());
            return view;
        }
    }

    public static class DetailListItem {
        public static final int TYPE_EMAIL = 1;
        public static final int TYPE_NONE = 0;
        public static final int TYPE_PHONE = 2;
        private String displayPhoneNumber;
        private String emailAddress;
        private String formattedPhoneNumber;

        public DetailListItem(String str, String str2, String str3) {
            this.formattedPhoneNumber = str;
            this.displayPhoneNumber = str2;
            this.emailAddress = str3;
        }

        public String getFormattedPhoneNumber() {
            return this.formattedPhoneNumber;
        }

        public void setFormattedPhoneNumber(String str) {
            this.formattedPhoneNumber = str;
        }

        public String getDisplayPhoneNumber() {
            return this.displayPhoneNumber;
        }

        public void setDisplayPhoneNumber(String str) {
            this.displayPhoneNumber = str;
        }

        public void setEmailAddress(String str) {
            this.emailAddress = str;
        }

        public String getEmailAddress() {
            return this.emailAddress;
        }

        public int getType() {
            if (!StringUtil.isEmptyOrNull(this.displayPhoneNumber)) {
                return 2;
            }
            return !StringUtil.isEmptyOrNull(this.emailAddress) ? 1 : 0;
        }

        @NonNull
        public String toString() {
            if (!StringUtil.isEmptyOrNull(this.displayPhoneNumber)) {
                return this.displayPhoneNumber;
            }
            return !StringUtil.isEmptyOrNull(this.emailAddress) ? this.emailAddress : "";
        }
    }

    public static class SelectContextMenu extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 0;
        public static final int ACTION_PHONE_CALL = 1;
        public static final int ACTION_PHONE_MSG = 2;
        private String mValue;

        public SelectContextMenu(int i, String str, String str2) {
            super(i, str);
            this.mValue = str2;
        }

        public String getValue() {
            return this.mValue;
        }
    }

    public ABItemDetailsList(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAdapter = new ABItemDetailsListAdapter(context);
        initView(context);
    }

    public ABItemDetailsList(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAdapter = new ABItemDetailsListAdapter(context);
        initView(context);
    }

    public ABItemDetailsList(Context context) {
        super(context);
        this.mAdapter = new ABItemDetailsListAdapter(context);
        initView(context);
    }

    private void initView(Context context) {
        if (isInEditMode()) {
            this.mAdapter.addItem(new DetailListItem(null, null, "test@example.com"));
            this.mAdapter.addItem(new DetailListItem("+8613912345678", "+86 139 1234 5678", null));
            this.mAdapter.addItem(new DetailListItem("+8613987654321", "+86 139 8765 4321", null));
        }
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(UIUtil.dip2px(getContext(), 10000.0f), Integer.MIN_VALUE));
    }

    public void loadData(@Nullable IMAddrBookItem iMAddrBookItem) {
        this.mAdapter.clear();
        if (iMAddrBookItem == null) {
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        if (iMAddrBookItem.isZoomRoomContact()) {
            this.mAdapter.addItem(new DetailListItem(null, null, getContext().getString(C4558R.string.zm_title_zoom_room_prex)));
        } else {
            String accountEmail = iMAddrBookItem.getAccountEmail();
            if (!StringUtil.isEmptyOrNull(accountEmail)) {
                this.mAdapter.addItem(new DetailListItem(null, null, accountEmail));
            }
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        DetailListItem item = this.mAdapter.getItem(i);
        if (item != null) {
            switch (item.getType()) {
                case 1:
                    Context context = getContext();
                    if (context != null && AndroidAppUtil.hasEmailApp(context) && (context instanceof FragmentActivity)) {
                        ZMSendMessageFragment.show(context, ((FragmentActivity) context).getSupportFragmentManager(), new String[]{item.getEmailAddress()}, null, null, null, null, null, null, 1);
                        break;
                    }
                case 2:
                    showPhoneContextMenu(item.toString());
                    break;
            }
        }
    }

    private void showPhoneContextMenu(String str) {
        Context context = getContext();
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
        zMMenuAdapter.addItem(new SelectContextMenu(1, context.getString(C4558R.string.zm_msg_call_phonenum, new Object[]{str}), str));
        zMMenuAdapter.addItem(new SelectContextMenu(2, context.getString(C4558R.string.zm_msg_sms_phonenum, new Object[]{str}), str));
        ZMAlertDialog create = new Builder(context).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ABItemDetailsList.this.onClickSelectMenu((SelectContextMenu) zMMenuAdapter.getItem(i));
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
    }

    /* access modifiers changed from: private */
    public void onClickSelectMenu(@Nullable SelectContextMenu selectContextMenu) {
        if (selectContextMenu != null) {
            switch (selectContextMenu.getAction()) {
                case 0:
                    if (!StringUtil.isEmptyOrNull(selectContextMenu.getValue())) {
                        AndroidAppUtil.copyText(getContext(), selectContextMenu.getValue());
                        break;
                    }
                    break;
                case 1:
                    AndroidAppUtil.sendDial(getContext(), selectContextMenu.getValue());
                    break;
                case 2:
                    Context context = getContext();
                    if (context instanceof FragmentActivity) {
                        ZMSendMessageFragment.show(context, ((FragmentActivity) context).getSupportFragmentManager(), null, new String[]{selectContextMenu.getValue()}, null, null, null, null, null, 2);
                        break;
                    }
                    break;
            }
        }
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        DetailListItem item = this.mAdapter.getItem(i);
        if (item == null) {
            return false;
        }
        Context context = getContext();
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
        zMMenuAdapter.addItem(new SelectContextMenu(0, context.getString(C4558R.string.zm_btn_copy), item.toString()));
        ZMAlertDialog create = new Builder(context).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ABItemDetailsList.this.onClickSelectMenu((SelectContextMenu) zMMenuAdapter.getItem(i));
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
        return true;
    }
}
