package com.zipow.videobox.view;

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
import com.zipow.videobox.ptapp.FavoriteMgr;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomContact;
import com.zipow.videobox.util.FavoriteItemComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.CompatUtils;

public class AddFavoriteListView extends ListView implements OnItemClickListener {
    private static final String TAG = "AddFavoriteListView";
    private AddFavoriteListAdapter mAdapter;
    @Nullable
    private String mFilter;
    private int mLastTopPosition = 0;
    private Listener mListener;
    @Nullable
    private RetainedFragment mRetainedFragment;
    @NonNull
    private List<AddFavoriteItem> mSelectedItems = new ArrayList();

    public interface Listener {
        void onSelectionChanged();
    }

    public static class RetainedFragment extends ZMFragment {
        @Nullable
        private List<AddFavoriteItem> mSelectedItems = null;

        public RetainedFragment() {
            setRetainInstance(true);
        }

        public void saveSelectedItems(List<AddFavoriteItem> list) {
            this.mSelectedItems = list;
        }

        @Nullable
        public List<AddFavoriteItem> restoreSelectedItems() {
            return this.mSelectedItems;
        }
    }

    public AddFavoriteListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public AddFavoriteListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public AddFavoriteListView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        this.mAdapter = new AddFavoriteListAdapter(getContext());
        setOnItemClickListener(this);
        if (!isInEditMode()) {
            initRetainedFragment();
        }
    }

    private void _editmode_loadAllBuddyItems(@NonNull AddFavoriteListAdapter addFavoriteListAdapter) {
        for (int i = 0; i < 20; i++) {
            ZoomContact zoomContact = new ZoomContact();
            zoomContact.setFirstName("Buddy");
            zoomContact.setLastName(String.valueOf(i));
            zoomContact.setUserID(String.valueOf(i));
            AddFavoriteItem addFavoriteItem = new AddFavoriteItem(zoomContact);
            addFavoriteItem.setChecked(i % 2 == 0);
            addFavoriteListAdapter.addItem(addFavoriteItem);
        }
    }

    private void loadAllBuddyItems(@NonNull AddFavoriteListAdapter addFavoriteListAdapter) {
        System.currentTimeMillis();
        FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
        if (favoriteMgr != null) {
            ArrayList<ZoomContact> arrayList = new ArrayList<>();
            if (favoriteMgr.getDomainUsersWithFilter("", arrayList)) {
                String str = "";
                String str2 = this.mFilter;
                if (str2 != null && str2.length() > 0) {
                    str = this.mFilter.toLowerCase();
                }
                for (ZoomContact addFavoriteItem : arrayList) {
                    AddFavoriteItem addFavoriteItem2 = new AddFavoriteItem(addFavoriteItem);
                    if (str.length() <= 0 || addFavoriteItem2.getScreenName().toLowerCase().indexOf(str) >= 0 || addFavoriteItem2.getEmail().toLowerCase().indexOf(str) >= 0) {
                        addFavoriteItem2.setChecked(isItemSelected(addFavoriteItem2.getUserID()));
                        addFavoriteListAdapter.addItem(addFavoriteItem2);
                    }
                }
            }
            addFavoriteListAdapter.sort();
        }
    }

    public void loadZoomContactsFromList(@NonNull List<ZoomContact> list) {
        this.mAdapter.clear();
        for (ZoomContact addFavoriteItem : list) {
            AddFavoriteItem addFavoriteItem2 = new AddFavoriteItem(addFavoriteItem);
            addFavoriteItem2.setChecked(isItemSelected(addFavoriteItem2.getUserID()));
            this.mAdapter.addItem(addFavoriteItem2);
        }
        this.mAdapter.sort();
        this.mAdapter.notifyDataSetChanged();
    }

    public void updateZoomContact(ZoomContact zoomContact) {
        this.mAdapter.notifyDataSetChanged();
    }

    public void updateZoomContact(String str) {
        this.mAdapter.notifyDataSetChanged();
    }

    private boolean isItemSelected(@Nullable String str) {
        if (str == null) {
            return false;
        }
        for (AddFavoriteItem userID : this.mSelectedItems) {
            if (str.equals(userID.getUserID())) {
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
            AddFavoriteItem addFavoriteItem = (AddFavoriteItem) this.mAdapter.getItem(i);
            if (addFavoriteItem != null) {
                addFavoriteItem.setChecked(false);
            }
            this.mAdapter.notifyDataSetChanged();
        }
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onSelectionChanged();
        }
    }

    @Nullable
    public List<AddFavoriteItem> getSelectedBuddies() {
        return this.mSelectedItems;
    }

    public void unselectBuddy(@Nullable AddFavoriteItem addFavoriteItem) {
        if (addFavoriteItem != null) {
            AddFavoriteItem itemByJid = this.mAdapter.getItemByJid(addFavoriteItem.getUserID());
            if (itemByJid != null) {
                itemByJid.setChecked(false);
                this.mAdapter.notifyDataSetChanged();
            }
            removeSelectedItem(addFavoriteItem);
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
        AddFavoriteItem addFavoriteItem = (AddFavoriteItem) this.mAdapter.getItem(i);
        if (addFavoriteItem != null) {
            addFavoriteItem.setChecked(!addFavoriteItem.isChecked());
            this.mAdapter.notifyDataSetChanged();
            if (addFavoriteItem.isChecked()) {
                addSelectedItem(addFavoriteItem);
            } else {
                removeSelectedItem(addFavoriteItem);
            }
            Listener listener = this.mListener;
            if (listener != null) {
                listener.onSelectionChanged();
            }
        }
    }

    private void removeSelectedItem(@NonNull AddFavoriteItem addFavoriteItem) {
        int size = this.mSelectedItems.size() - 1;
        while (size >= 0) {
            AddFavoriteItem addFavoriteItem2 = (AddFavoriteItem) this.mSelectedItems.get(size);
            if (addFavoriteItem.getUserID() == null || !addFavoriteItem.getUserID().equals(addFavoriteItem2.getUserID())) {
                size--;
            } else {
                this.mSelectedItems.remove(size);
                return;
            }
        }
    }

    private void addSelectedItem(AddFavoriteItem addFavoriteItem) {
        addFavoriteItem.setChecked(true);
        int size = this.mSelectedItems.size() - 1;
        while (size >= 0) {
            AddFavoriteItem addFavoriteItem2 = (AddFavoriteItem) this.mSelectedItems.get(size);
            if (addFavoriteItem.getUserID() == null || !addFavoriteItem.getUserID().equals(addFavoriteItem2.getUserID())) {
                size--;
            } else {
                this.mSelectedItems.set(size, addFavoriteItem);
                Collections.sort(this.mSelectedItems, new FavoriteItemComparator(CompatUtils.getLocalDefault()));
                return;
            }
        }
        this.mSelectedItems.add(addFavoriteItem);
        Collections.sort(this.mSelectedItems, new FavoriteItemComparator(CompatUtils.getLocalDefault()));
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
        List<AddFavoriteItem> restoreSelectedItems = retainedFragment.restoreSelectedItems();
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
