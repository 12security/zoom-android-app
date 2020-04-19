package com.zipow.videobox.fragment.p012mm;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.mm.ContactsAdapter */
/* compiled from: MMPhoneContactsInZoomFragment */
class ContactsAdapter extends QuickSearchListDataAdapter implements OnClickListener {
    private static final String TAG = "ContactsAdapter";
    @Nullable
    private Context mContext;
    @NonNull
    private List<ContactItem> mData = new ArrayList();
    private OnContactOPListener mOnContactOPListener;

    /* renamed from: com.zipow.videobox.fragment.mm.ContactsAdapter$OnContactOPListener */
    /* compiled from: MMPhoneContactsInZoomFragment */
    public interface OnContactOPListener {
        void onContactAddClick(ContactItem contactItem);
    }

    public long getItemId(int i) {
        return 0;
    }

    public ContactsAdapter(@Nullable Context context, OnContactOPListener onContactOPListener) {
        this.mOnContactOPListener = onContactOPListener;
        this.mContext = context;
    }

    @Nullable
    public String getItemSortKey(Object obj) {
        if (obj instanceof ContactItem) {
            return ((ContactItem) obj).getContact().sortKey;
        }
        return null;
    }

    public void setData(@Nullable List<ContactItem> list) {
        this.mData.clear();
        if (list != null) {
            this.mData.addAll(list);
        }
    }

    public int getCount() {
        return this.mData.size();
    }

    @Nullable
    public ContactItem getItem(int i) {
        if (this.mData.size() <= i) {
            return null;
        }
        return (ContactItem) this.mData.get(i);
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        return getContactView(i, view, viewGroup);
    }

    @Nullable
    private View getContactView(int i, @Nullable View view, ViewGroup viewGroup) {
        ContactItem item = getItem(i);
        if (item == null) {
            return view;
        }
        if (view == null || !"ContactItem".equals(view.getTag())) {
            view = View.inflate(this.mContext, C4558R.layout.zm_item_contacts_in_zoom, null);
        }
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtContactName);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtContactNumber);
        Button button = (Button) view.findViewById(C4558R.C4560id.btnAdd);
        View findViewById = view.findViewById(C4558R.C4560id.txtAdded);
        if (item.getContact() == null) {
            return view;
        }
        textView.setText(item.getContact().displayName);
        textView2.setText(item.getContact().normalizedNumber);
        if (item.getBuddy() == null || (!item.getBuddy().isMyContact() && !item.getBuddy().isPending())) {
            button.setVisibility(0);
            findViewById.setVisibility(8);
        } else {
            button.setVisibility(8);
            findViewById.setVisibility(0);
        }
        button.setOnClickListener(this);
        button.setTag(item);
        return view;
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnAdd) {
            ContactItem contactItem = (ContactItem) view.getTag();
            OnContactOPListener onContactOPListener = this.mOnContactOPListener;
            if (onContactOPListener != null) {
                onContactOPListener.onContactAddClick(contactItem);
            }
        }
    }
}
