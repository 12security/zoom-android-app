package com.zipow.videobox.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.ptapp.FavoriteMgr;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomContact;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class FavoriteListView extends QuickSearchListView implements OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = "FavoriteListView";
    private FavoriteListAdapter mAdapter;
    @Nullable
    private String mFilter;

    public static class ContextMenuFragment extends ZMDialogFragment {
        private static final String ARG_BUDDYITEM = "buddyItem";
        public static final int MI_PHONE_CALL = 1;
        public static final int MI_REMOVE_BUDDY = 2;
        public static final int MI_VIDEO_CALL = 0;
        @Nullable
        private ZMMenuAdapter<ContextMenuItem> mAdapter;

        public static void show(@Nullable FragmentManager fragmentManager, @NonNull FavoriteItem favoriteItem) {
            if (fragmentManager != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("buddyItem", favoriteItem);
                ContextMenuFragment contextMenuFragment = new ContextMenuFragment();
                contextMenuFragment.setArguments(bundle);
                contextMenuFragment.show(fragmentManager, ContextMenuFragment.class.getName());
            }
        }

        public ContextMenuFragment() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            if (getArguments() == null) {
                return createEmptyDialog();
            }
            FavoriteItem favoriteItem = (FavoriteItem) getArguments().getSerializable("buddyItem");
            this.mAdapter = createUpdateAdapter();
            String screenName = favoriteItem.getScreenName();
            if (StringUtil.isEmptyOrNull(screenName)) {
                screenName = favoriteItem.getEmail();
            }
            ZMAlertDialog create = new Builder(getActivity()).setTitle((CharSequence) screenName).setAdapter(this.mAdapter, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContextMenuFragment.this.onSelectItem(i);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            return create;
        }

        private ZMMenuAdapter<ContextMenuItem> createUpdateAdapter() {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return null;
            }
            ContextMenuItem[] contextMenuItemArr = isInCall() ? new ContextMenuItem[]{new ContextMenuItem(0, getText(getConfMenuItemText((FavoriteItem) arguments.getSerializable("buddyItem"))).toString()), new ContextMenuItem(2, getText(C4558R.string.zm_mi_remove_buddy).toString())} : new ContextMenuItem[]{new ContextMenuItem(0, getText(C4558R.string.zm_btn_video_call).toString()), new ContextMenuItem(1, getText(C4558R.string.zm_btn_audio_call).toString()), new ContextMenuItem(2, getText(C4558R.string.zm_mi_remove_buddy).toString())};
            ZMMenuAdapter<ContextMenuItem> zMMenuAdapter = this.mAdapter;
            if (zMMenuAdapter == null) {
                this.mAdapter = new ZMMenuAdapter<>(getActivity(), false);
            } else {
                zMMenuAdapter.clear();
            }
            this.mAdapter.addAll((MenuItemType[]) contextMenuItemArr);
            return this.mAdapter;
        }

        public void refresh() {
            ZMMenuAdapter createUpdateAdapter = createUpdateAdapter();
            if (createUpdateAdapter != null) {
                createUpdateAdapter.notifyDataSetChanged();
            }
        }

        private int getConfMenuItemText(@Nullable FavoriteItem favoriteItem) {
            switch (PTApp.getInstance().getCallStatus()) {
                case 1:
                    return C4558R.string.zm_mi_return_to_conf;
                case 2:
                    if (favoriteItem == null || !PTApp.getInstance().probeUserStatus(favoriteItem.getUserID())) {
                        return C4558R.string.zm_mi_invite_to_conf;
                    }
                    return C4558R.string.zm_mi_return_to_conf;
                default:
                    return C4558R.string.zm_mi_start_conf;
            }
        }

        private boolean isInCall() {
            switch (PTApp.getInstance().getCallStatus()) {
                case 1:
                case 2:
                    return true;
                default:
                    return false;
            }
        }

        /* access modifiers changed from: private */
        public void onSelectItem(int i) {
            ContextMenuItem contextMenuItem = (ContextMenuItem) this.mAdapter.getItem(i);
            Bundle arguments = getArguments();
            if (arguments != null) {
                FavoriteItem favoriteItem = (FavoriteItem) arguments.getSerializable("buddyItem");
                switch (contextMenuItem.getAction()) {
                    case 0:
                        onSelectVideoCall(favoriteItem);
                        break;
                    case 1:
                        onSelectPhoneCall(favoriteItem);
                        break;
                    case 2:
                        RemoveBuddyConfirmDialog.show(getFragmentManager(), favoriteItem);
                        break;
                }
            }
        }

        private void onSelectVideoCall(@Nullable FavoriteItem favoriteItem) {
            switch (PTApp.getInstance().getCallStatus()) {
                case 1:
                    returnToConf();
                    return;
                case 2:
                    if (favoriteItem == null || !PTApp.getInstance().probeUserStatus(favoriteItem.getUserID())) {
                        inviteToConf(favoriteItem);
                        return;
                    } else {
                        returnToConf();
                        return;
                    }
                default:
                    startConf(favoriteItem, true);
                    return;
            }
        }

        private void onSelectPhoneCall(@Nullable FavoriteItem favoriteItem) {
            switch (PTApp.getInstance().getCallStatus()) {
                case 1:
                    returnToConf();
                    return;
                case 2:
                    if (favoriteItem == null || !PTApp.getInstance().probeUserStatus(favoriteItem.getUserID())) {
                        inviteToConf(favoriteItem);
                        return;
                    } else {
                        returnToConf();
                        return;
                    }
                default:
                    startConf(favoriteItem, false);
                    return;
            }
        }

        private void startConf(@Nullable FavoriteItem favoriteItem, boolean z) {
            if (favoriteItem != null) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    int inviteToVideoCall = ConfActivity.inviteToVideoCall(activity, favoriteItem.getUserID(), z ? 1 : 0);
                    if (inviteToVideoCall != 0) {
                        StartHangoutFailedDialog.show(((ZMActivity) activity).getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), inviteToVideoCall);
                    }
                }
            }
        }

        private void inviteToConf(@Nullable FavoriteItem favoriteItem) {
            if (favoriteItem != null) {
                PTApp instance = PTApp.getInstance();
                String activeCallId = instance.getActiveCallId();
                if (!StringUtil.isEmptyOrNull(activeCallId)) {
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        if (instance.inviteBuddiesToConf(new String[]{favoriteItem.getUserID()}, null, activeCallId, 0, activity.getString(C4558R.string.zm_msg_invitation_message_template)) == 0) {
                            ConfLocalHelper.returnToConf(activity);
                        }
                    }
                }
            }
        }

        private void returnToConf() {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                ConfLocalHelper.returnToConf(activity);
            }
        }
    }

    static class ContextMenuItem extends ZMSimpleMenuItem {
        public ContextMenuItem(int i, String str) {
            super(i, str);
        }
    }

    public static class RemoveBuddyConfirmDialog extends ZMDialogFragment {
        private static final String ARG_BUDDYITEM = "buddyItem";

        public RemoveBuddyConfirmDialog() {
            setCancelable(true);
        }

        public static void show(@Nullable FragmentManager fragmentManager, @Nullable FavoriteItem favoriteItem) {
            if (fragmentManager != null && favoriteItem != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("buddyItem", favoriteItem);
                RemoveBuddyConfirmDialog removeBuddyConfirmDialog = new RemoveBuddyConfirmDialog();
                removeBuddyConfirmDialog.setArguments(bundle);
                removeBuddyConfirmDialog.show(fragmentManager, RemoveBuddyConfirmDialog.class.getName());
            }
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return createEmptyDialog();
            }
            FavoriteItem favoriteItem = (FavoriteItem) arguments.getSerializable("buddyItem");
            String screenName = favoriteItem.getScreenName();
            if (StringUtil.isEmptyOrNull(screenName)) {
                screenName = favoriteItem.getEmail();
            }
            return new Builder(getActivity()).setTitle((CharSequence) getString(C4558R.string.zm_msg_remove_favorite_confirm, screenName)).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    RemoveBuddyConfirmDialog.this.onClickOK();
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).create();
        }

        /* access modifiers changed from: private */
        public void onClickOK() {
            Bundle arguments = getArguments();
            if (arguments != null) {
                FavoriteItem favoriteItem = (FavoriteItem) arguments.getSerializable("buddyItem");
                FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
                if (!(favoriteMgr == null || favoriteItem == null)) {
                    favoriteMgr.removeFavorite(favoriteItem.getUserID());
                }
            }
        }
    }

    public FavoriteListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public FavoriteListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public FavoriteListView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        this.mAdapter = new FavoriteListAdapter(getContext());
        if (isInEditMode()) {
            _editmode_loadAllFavoriteItems(this.mAdapter);
        } else {
            loadAllFavoriteItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);
    }

    private void _editmode_loadAllFavoriteItems(@NonNull FavoriteListAdapter favoriteListAdapter) {
        for (int i = 0; i < 20; i++) {
            ZoomContact zoomContact = new ZoomContact();
            zoomContact.setFirstName("Buddy");
            zoomContact.setLastName(String.valueOf(i));
            zoomContact.setUserID(String.valueOf(i));
            favoriteListAdapter.addItem(new FavoriteItem(zoomContact));
        }
    }

    private void loadAllFavoriteItems(@NonNull FavoriteListAdapter favoriteListAdapter) {
        FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
        if (favoriteMgr != null) {
            String str = "";
            String str2 = this.mFilter;
            if (str2 != null && str2.length() > 0) {
                str = this.mFilter.toLowerCase(CompatUtils.getLocalDefault());
            }
            ArrayList<ZoomContact> arrayList = new ArrayList<>();
            favoriteListAdapter.clear();
            if (favoriteMgr.getFavoriteListWithFilter(null, arrayList)) {
                for (ZoomContact favoriteItem : arrayList) {
                    FavoriteItem favoriteItem2 = new FavoriteItem(favoriteItem);
                    String screenName = favoriteItem2.getScreenName();
                    if (screenName == null) {
                        screenName = "";
                    }
                    String email = favoriteItem2.getEmail();
                    if (email == null) {
                        email = "";
                    }
                    if (str.length() <= 0 || screenName.toLowerCase(CompatUtils.getLocalDefault()).indexOf(str) >= 0 || email.toLowerCase(CompatUtils.getLocalDefault()).indexOf(str) >= 0) {
                        favoriteListAdapter.addItem(favoriteItem2);
                    }
                }
            }
            favoriteListAdapter.sort(false);
        }
    }

    public void addZoomContact(@Nullable ZoomContact zoomContact) {
        if (zoomContact != null) {
            String str = this.mFilter;
            if (str == null || str.length() <= 0) {
                this.mAdapter.addItem(new FavoriteItem(zoomContact));
                this.mAdapter.sort(true);
                this.mAdapter.notifyDataSetChanged();
            } else {
                reloadFavoriteItems();
            }
        }
    }

    public void updateZoomContact(@Nullable ZoomContact zoomContact) {
        if (zoomContact != null) {
            String str = this.mFilter;
            if (str == null || str.length() <= 0) {
                this.mAdapter.updateItem(new FavoriteItem(zoomContact));
                this.mAdapter.sort(true);
                this.mAdapter.notifyDataSetChanged();
            } else {
                reloadFavoriteItems();
            }
        }
    }

    public void updateZoomContact(String str) {
        FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
        if (favoriteMgr != null) {
            ZoomContact zoomContact = new ZoomContact();
            if (favoriteMgr.getFavoriteByUserID(str, zoomContact)) {
                updateZoomContact(zoomContact);
            } else {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void reloadFavoriteItems() {
        this.mAdapter.clear();
        loadAllFavoriteItems(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (itemAtPosition instanceof FavoriteItem) {
            ContextMenuFragment.show(((ZMActivity) getContext()).getSupportFragmentManager(), (FavoriteItem) itemAtPosition);
        }
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (!(itemAtPosition instanceof FavoriteItem)) {
            return false;
        }
        ContextMenuFragment.show(((ZMActivity) getContext()).getSupportFragmentManager(), (FavoriteItem) itemAtPosition);
        return true;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            Parcelable parcelable2 = bundle.getParcelable("IMBuddyListView.superState");
            this.mFilter = bundle.getString("IMBuddyListView.mFilter");
            parcelable = parcelable2;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("IMBuddyListView.superState", onSaveInstanceState);
        bundle.putString("IMBuddyListView.mFilter", this.mFilter);
        return bundle;
    }

    public void setFilter(@Nullable String str) {
        this.mFilter = str;
    }

    @Nullable
    public String getFilter() {
        return this.mFilter;
    }

    public void filter(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        this.mFilter = str.trim().toLowerCase(CompatUtils.getLocalDefault());
        reloadFavoriteItems();
    }

    public void refreshContextMenu() {
        ContextMenuFragment contextMenuFragment = (ContextMenuFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(ContextMenuFragment.class.getName());
        if (contextMenuFragment != null) {
            contextMenuFragment.refresh();
        }
    }
}
