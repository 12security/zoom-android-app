package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddySearchData;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.CompatUtils;

/* renamed from: com.zipow.videobox.view.mm.AddCompanyContactsListView */
public class AddCompanyContactsListView extends ListView implements OnItemClickListener {
    private static final String TAG = "AddCompanyContactsListView";
    private AddCompanyContactsListAdapter mAdapter;
    @Nullable
    private String mFilter;
    private int mLastTopPosition = 0;
    private Listener mListener;
    @Nullable
    private RetainedFragment mRetainedFragment;
    @NonNull
    private List<AddCompanyContactsItem> mSelectedItems = new ArrayList();

    /* renamed from: com.zipow.videobox.view.mm.AddCompanyContactsListView$Listener */
    public interface Listener {
        void onSelectionChanged();
    }

    /* renamed from: com.zipow.videobox.view.mm.AddCompanyContactsListView$RetainedFragment */
    public static class RetainedFragment extends ZMFragment {
        @Nullable
        private List<AddCompanyContactsItem> mSelectedItems = null;

        public RetainedFragment() {
            setRetainInstance(true);
        }

        public void saveSelectedItems(List<AddCompanyContactsItem> list) {
            this.mSelectedItems = list;
        }

        @Nullable
        public List<AddCompanyContactsItem> restoreSelectedItems() {
            return this.mSelectedItems;
        }
    }

    public void updateBuddy(String str) {
    }

    public AddCompanyContactsListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public AddCompanyContactsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public AddCompanyContactsListView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        this.mAdapter = new AddCompanyContactsListAdapter(getContext());
        setOnItemClickListener(this);
        if (!isInEditMode()) {
            initRetainedFragment();
        }
    }

    private void _editmode_loadAllBuddyItems(@NonNull AddCompanyContactsListAdapter addCompanyContactsListAdapter) {
        for (int i = 0; i < 20; i++) {
            AddCompanyContactsItem addCompanyContactsItem = new AddCompanyContactsItem();
            addCompanyContactsItem.setJid(String.valueOf(i));
            StringBuilder sb = new StringBuilder();
            sb.append("Test ");
            sb.append(i);
            addCompanyContactsItem.setScreenName(sb.toString());
            addCompanyContactsItem.setChecked(i % 2 == 0);
            addCompanyContactsListAdapter.addItem(addCompanyContactsItem);
        }
    }

    private void loadAllBuddyItems(@NonNull AddCompanyContactsListAdapter addCompanyContactsListAdapter) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddySearchData buddySearchData = zoomMessenger.getBuddySearchData();
            if (buddySearchData != null) {
                int buddyCount = buddySearchData.getBuddyCount();
                for (int i = 0; i < buddyCount; i++) {
                    ZoomBuddy buddyAt = buddySearchData.getBuddyAt(i);
                    if (buddyAt != null) {
                        AddCompanyContactsItem addCompanyContactsItem = new AddCompanyContactsItem(buddyAt);
                        addCompanyContactsItem.setChecked(isItemSelected(addCompanyContactsItem.getJid()));
                        addCompanyContactsListAdapter.addItem(addCompanyContactsItem);
                    }
                }
                addCompanyContactsListAdapter.sort();
            }
        }
    }

    private boolean isItemSelected(@Nullable String str) {
        if (str == null) {
            return false;
        }
        for (AddCompanyContactsItem jid : this.mSelectedItems) {
            if (str.equals(jid.getJid())) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            _editmode_loadAllBuddyItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
        int i = this.mLastTopPosition;
        if (i >= 0) {
            setSelectionFromTop(i, 0);
        }
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            Parcelable parcelable2 = bundle.getParcelable("AddFavoriteListView.superState");
            this.mFilter = bundle.getString("AddFavoriteListView.mFilter");
            this.mLastTopPosition = bundle.getInt("AddFavoriteListView.topPosition", -1);
            parcelable = parcelable2;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("AddFavoriteListView.superState", onSaveInstanceState);
        bundle.putString("AddFavoriteListView.mFilter", this.mFilter);
        bundle.putInt("AddFavoriteListView.topPosition", pointToPosition(10, 10));
        return bundle;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void reloadAllBuddyItems() {
        System.currentTimeMillis();
        this.mAdapter.clear();
        loadAllBuddyItems(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    public void clearSelection() {
        this.mSelectedItems.clear();
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            AddCompanyContactsItem addCompanyContactsItem = (AddCompanyContactsItem) this.mAdapter.getItem(i);
            if (addCompanyContactsItem != null) {
                addCompanyContactsItem.setChecked(false);
            }
            this.mAdapter.notifyDataSetChanged();
        }
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onSelectionChanged();
        }
    }

    @Nullable
    public List<AddCompanyContactsItem> getSelectedBuddies() {
        return this.mSelectedItems;
    }

    public void unselectBuddy(@Nullable AddCompanyContactsItem addCompanyContactsItem) {
        if (addCompanyContactsItem != null) {
            AddCompanyContactsItem itemByJid = this.mAdapter.getItemByJid(addCompanyContactsItem.getJid());
            if (itemByJid != null) {
                itemByJid.setChecked(false);
                this.mAdapter.notifyDataSetChanged();
            }
            removeSelectedItem(addCompanyContactsItem);
            Listener listener = this.mListener;
            if (listener != null) {
                listener.onSelectionChanged();
            }
        }
    }

    public void filter(String str) {
        this.mFilter = str;
        reloadAllBuddyItems();
    }

    public void setFilter(@Nullable String str) {
        this.mFilter = str;
    }

    @Nullable
    public String getFilter() {
        return this.mFilter;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        AddCompanyContactsItem addCompanyContactsItem = (AddCompanyContactsItem) this.mAdapter.getItem(i);
        if (addCompanyContactsItem != null) {
            addCompanyContactsItem.setChecked(!addCompanyContactsItem.isChecked());
            this.mAdapter.notifyDataSetChanged();
            if (addCompanyContactsItem.isChecked()) {
                addSelectedItem(addCompanyContactsItem);
            } else {
                removeSelectedItem(addCompanyContactsItem);
            }
            Listener listener = this.mListener;
            if (listener != null) {
                listener.onSelectionChanged();
            }
        }
    }

    private void removeSelectedItem(@NonNull AddCompanyContactsItem addCompanyContactsItem) {
        int size = this.mSelectedItems.size() - 1;
        while (size >= 0) {
            AddCompanyContactsItem addCompanyContactsItem2 = (AddCompanyContactsItem) this.mSelectedItems.get(size);
            if (addCompanyContactsItem.getJid() == null || !addCompanyContactsItem.getJid().equals(addCompanyContactsItem2.getJid())) {
                size--;
            } else {
                this.mSelectedItems.remove(size);
                return;
            }
        }
    }

    private void addSelectedItem(AddCompanyContactsItem addCompanyContactsItem) {
        addCompanyContactsItem.setChecked(true);
        int size = this.mSelectedItems.size() - 1;
        while (size >= 0) {
            AddCompanyContactsItem addCompanyContactsItem2 = (AddCompanyContactsItem) this.mSelectedItems.get(size);
            if (addCompanyContactsItem.getJid() == null || !addCompanyContactsItem.getJid().equals(addCompanyContactsItem2.getJid())) {
                size--;
            } else {
                this.mSelectedItems.set(size, addCompanyContactsItem);
                Collections.sort(this.mSelectedItems, new AddCompanyContactsItemComparator(CompatUtils.getLocalDefault()));
                return;
            }
        }
        this.mSelectedItems.add(addCompanyContactsItem);
        Collections.sort(this.mSelectedItems, new AddCompanyContactsItemComparator(CompatUtils.getLocalDefault()));
    }

    private void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment == null) {
            this.mRetainedFragment = new RetainedFragment();
            this.mRetainedFragment.saveSelectedItems(this.mSelectedItems);
            ((ZMActivity) getContext()).getSupportFragmentManager().beginTransaction().add((Fragment) this.mRetainedFragment, RetainedFragment.class.getName()).commit();
            return;
        }
        List<AddCompanyContactsItem> restoreSelectedItems = retainedFragment.restoreSelectedItems();
        if (restoreSelectedItems != null) {
            this.mSelectedItems = restoreSelectedItems;
        }
    }

    @Nullable
    private RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        return (RetainedFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(RetainedFragment.class.getName());
    }
}
