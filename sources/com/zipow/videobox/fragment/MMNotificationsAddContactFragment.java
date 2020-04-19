package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.MMSelectContactsActivity.SelectContactsParamter;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class MMNotificationsAddContactFragment extends ZMDialogFragment implements OnClickListener {
    private static final int REQUEST_NOTIFICATIONS_MEMEBERS = 1;
    private LinearLayout addContactLinear;
    private Button backBtn;
    @NonNull
    private List<ContactItem> contactList = new ArrayList();
    private TextView label;
    @NonNull
    private SimpleNotificationSettingUIListener listener = new SimpleNotificationSettingUIListener() {
        public void OnHLPersonSettingUpdated() {
            super.OnHLPersonSettingUpdated();
        }
    };
    private ContactAdapter mAdapter;
    private ListView mListView;
    private LinearLayout removeLinear;

    public static class ConfirmRemoveAllDialog extends ZMDialogFragment {
        private Button mBtnOK = null;

        public static void showConfirmRemoveAllDialog(FragmentManager fragmentManager) {
            new ConfirmRemoveAllDialog().show(fragmentManager, ConfirmRemoveAllDialog.class.getName());
        }

        public ConfirmRemoveAllDialog() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        public void onResume() {
            super.onResume();
            this.mBtnOK = ((ZMAlertDialog) getDialog()).getButton(-1);
            Button button = this.mBtnOK;
            if (button != null) {
                button.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        ConfirmRemoveAllDialog.this.onClickBtnOK();
                    }
                });
            }
        }

        /* access modifiers changed from: private */
        public void onClickBtnOK() {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    MMNotificationsAddContactFragment findMMNotificationsAddContactFragment = MMNotificationsAddContactFragment.findMMNotificationsAddContactFragment(supportFragmentManager);
                    if (findMMNotificationsAddContactFragment != null) {
                        findMMNotificationsAddContactFragment.removeAllContacts();
                        dismissAllowingStateLoss();
                    }
                }
            }
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            return new Builder(getActivity()).setTitle(C4558R.string.zm_lbl_receive_notifications_remove_all_31156).setMessage(C4558R.string.zm_lbl_receive_notification_remove_all_msg_31156).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).setPositiveButton(C4558R.string.zm_lbl_confirm, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
        }
    }

    private class ContactAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        /* access modifiers changed from: private */
        public List<ContactItem> mList;

        public long getItemId(int i) {
            return (long) i;
        }

        public ContactAdapter(@NonNull Context context, @NonNull List<ContactItem> list) {
            this.mInflater = LayoutInflater.from(context);
            this.mList = list;
        }

        public int getCount() {
            return this.mList.size();
        }

        public Object getItem(int i) {
            return this.mList.get(i);
        }

        @Nullable
        public View getView(final int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(C4558R.layout.zm_notification_contact_list_item, viewGroup, false);
            }
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.zm_notification_contact_list_item_delete_btn);
            ((TextView) view.findViewById(C4558R.C4560id.zm_notification_contact_list_item_name)).setText(((ContactItem) this.mList.get(i)).getDisplayName());
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                    if (notificationSettingMgr != null) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(((ContactItem) ContactAdapter.this.mList.get(i)).getJid());
                        notificationSettingMgr.applyPersonSetting(null, arrayList);
                    }
                    ContactAdapter.this.mList.remove(i);
                    ContactAdapter.this.notifyDataSetChanged();
                    MMNotificationsAddContactFragment.this.updateView();
                }
            });
            return view;
        }
    }

    private class ContactItem {
        /* access modifiers changed from: private */
        @Nullable
        public String displayName;
        private String jid;

        public ContactItem(@NonNull ZoomMessenger zoomMessenger, @NonNull String str) {
            this.jid = str;
            this.displayName = BuddyNameUtil.getBuddyDisplayName(zoomMessenger.getBuddyWithJID(str), null);
        }

        public String getJid() {
            return this.jid;
        }

        public void setJid(String str) {
            this.jid = str;
        }

        @Nullable
        public String getDisplayName() {
            return this.displayName;
        }

        public void setDisplayName(@Nullable String str) {
            this.displayName = str;
        }
    }

    public static void showAsActivity(Fragment fragment) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(fragment, MMNotificationsAddContactFragment.class.getName(), bundle, 0, true, 1);
    }

    @Nullable
    public static MMNotificationsAddContactFragment findMMNotificationsAddContactFragment(FragmentManager fragmentManager) {
        return (MMNotificationsAddContactFragment) fragmentManager.findFragmentByTag(MMNotificationsAddContactFragment.class.getName());
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_notification_add_contact, viewGroup, false);
        this.backBtn = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.addContactLinear = (LinearLayout) inflate.findViewById(C4558R.C4560id.panelAddContact);
        this.mListView = (ListView) inflate.findViewById(C4558R.C4560id.listView);
        this.removeLinear = (LinearLayout) inflate.findViewById(C4558R.C4560id.panelRemoveAll);
        this.label = (TextView) inflate.findViewById(C4558R.C4560id.notification_label);
        return inflate;
    }

    public void onStart() {
        super.onStart();
        NotificationSettingUI.getInstance().addListener(this.listener);
        this.backBtn.setOnClickListener(this);
        this.addContactLinear.setOnClickListener(this);
        this.removeLinear.setOnClickListener(this);
    }

    public void onStop() {
        super.onStop();
        NotificationSettingUI.getInstance().removeListener(this.listener);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                List<String> personSetting = notificationSettingMgr.getPersonSetting();
                if (personSetting != null) {
                    for (String contactItem : personSetting) {
                        ContactItem contactItem2 = new ContactItem(zoomMessenger, contactItem);
                        if (!TextUtils.isEmpty(contactItem2.displayName)) {
                            this.contactList.add(contactItem2);
                        }
                    }
                    sortContactList(this.contactList);
                    this.mAdapter = new ContactAdapter(getContext(), this.contactList);
                    this.mListView.setAdapter(this.mAdapter);
                }
            }
        }
        updateView();
    }

    private void sortContactList(@Nullable List<ContactItem> list) {
        if (list != null && list.size() >= 2) {
            final Collator instance = Collator.getInstance(CompatUtils.getLocalDefault());
            instance.setStrength(0);
            Collections.sort(list, new Comparator<ContactItem>() {
                public int compare(@NonNull ContactItem contactItem, @NonNull ContactItem contactItem2) {
                    return instance.compare(contactItem.getDisplayName(), contactItem2.getDisplayName());
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void updateView() {
        if (this.contactList.isEmpty()) {
            this.mListView.setVisibility(8);
            this.removeLinear.setVisibility(8);
            this.label.setVisibility(8);
            return;
        }
        this.mListView.setVisibility(0);
        this.removeLinear.setVisibility(0);
        this.label.setVisibility(0);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            finishFragment(true);
        } else if (id == C4558R.C4560id.panelAddContact) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
                selectContactsParamter.title = getString(C4558R.string.zm_lbl_receive_notifications_add_contacts_31156);
                selectContactsParamter.btnOkText = getString(C4558R.string.zm_btn_ok);
                selectContactsParamter.isAnimBottomTop = true;
                selectContactsParamter.isOnlySameOrganization = false;
                selectContactsParamter.isContainsAllInGroup = false;
                selectContactsParamter.includeRobot = false;
                selectContactsParamter.mFilterZoomRooms = true;
                selectContactsParamter.maxSelectCount = zoomMessenger.getGroupLimitCount(false);
                if (!this.contactList.isEmpty()) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (ContactItem jid : this.contactList) {
                        arrayList.add(jid.getJid());
                    }
                    selectContactsParamter.preSelectedItems = arrayList;
                }
                MMSelectContactsActivity.show((Fragment) this, selectContactsParamter, 1, (Bundle) null);
            }
        } else if (id == C4558R.C4560id.panelRemoveAll) {
            ConfirmRemoveAllDialog.showConfirmRemoveAllDialog(getFragmentManager());
        }
    }

    public void removeAllContacts() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            ArrayList arrayList = new ArrayList();
            for (ContactItem jid : this.contactList) {
                arrayList.add(jid.getJid());
            }
            notificationSettingMgr.applyPersonSetting(null, arrayList);
        }
        this.contactList.clear();
        this.mAdapter.notifyDataSetChanged();
        updateView();
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i == 1 && i2 == -1 && intent != null) {
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectedItems");
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (arrayList != null) {
                    ArrayList arrayList2 = new ArrayList();
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) it.next();
                        arrayList2.add(iMAddrBookItem.getJid());
                        ContactItem contactItem = new ContactItem(zoomMessenger, iMAddrBookItem.getJid());
                        if (!TextUtils.isEmpty(contactItem.displayName)) {
                            this.contactList.add(contactItem);
                        }
                    }
                    sortContactList(this.contactList);
                    NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                    if (notificationSettingMgr != null) {
                        notificationSettingMgr.applyPersonSetting(arrayList2, null);
                    }
                    this.mAdapter.notifyDataSetChanged();
                }
                updateView();
            }
        }
    }
}
