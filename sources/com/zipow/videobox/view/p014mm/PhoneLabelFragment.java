package com.zipow.videobox.view.p014mm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.ContactType;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.PhoneNumber;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ContactCloudSIP;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.sip.DescriptionUtil;
import com.zipow.videobox.view.sip.PBXDirectorySearchFragment;
import com.zipow.videobox.view.sip.sms.IPBXMessageSelectContact;
import com.zipow.videobox.view.sip.sms.PBXMessageContact;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMIntentUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.PhoneLabelFragment */
public class PhoneLabelFragment extends ZMDialogFragment {
    private static final String ARG_ADDRBOOKITEM = "addrBookItem";
    private static final int REQUEST_PERMISSION_CALL_PHONE = 12;
    private static final int REQUEST_PERMISSION_MIC = 11;
    public static final int REQUEST_SELECT_CONTACT = 1001;
    private static final String TAG = "PhoneLabelFragment";
    private PhoneLabelAdapter mAdapter;
    @Nullable
    private IMAddrBookItem mContact = null;
    private String mDailString;
    private TextView mNameTv;
    private RecyclerView mPhoneLv;
    @Nullable
    private String mSelectedPhoneNumber = null;

    /* renamed from: com.zipow.videobox.view.mm.PhoneLabelFragment$DetailItem */
    static class DetailItem {
        String label;
        OnClick onClick;
        int type;
        String value;

        /* renamed from: com.zipow.videobox.view.mm.PhoneLabelFragment$DetailItem$OnClick */
        interface OnClick {
            void onClick(DetailItem detailItem);
        }

        DetailItem() {
        }

        @NonNull
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.label);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(this.value);
            return sb.toString();
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.PhoneLabelFragment$PhoneLabelAdapter */
    static class PhoneLabelAdapter extends Adapter<ValueViewHolder> {
        private Context mContext;
        @NonNull
        List<DetailItem> mDatas = new ArrayList();

        public long getItemId(int i) {
            return (long) i;
        }

        public PhoneLabelAdapter(Context context, IMAddrBookItem iMAddrBookItem) {
            this.mContext = context;
        }

        @NonNull
        public ValueViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ValueViewHolder(LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_phone_label, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull ValueViewHolder valueViewHolder, int i) {
            final DetailItem detailItem = (DetailItem) this.mDatas.get(i);
            valueViewHolder.mLableType.setText(detailItem.label);
            valueViewHolder.mPhoneNumber.setText(detailItem.value);
            valueViewHolder.mPhoneNumber.setContentDescription(DescriptionUtil.getPhoneNumberContentDescription(detailItem.value));
            valueViewHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (detailItem.onClick != null) {
                        detailItem.onClick.onClick(detailItem);
                    }
                }
            });
        }

        public int getItemCount() {
            return this.mDatas.size();
        }

        public void updateData(@Nullable List<DetailItem> list) {
            this.mDatas.clear();
            if (list != null) {
                this.mDatas.addAll(list);
            }
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.PhoneLabelFragment$ValueViewHolder */
    static class ValueViewHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public TextView mLableType;
        /* access modifiers changed from: private */
        public TextView mPhoneNumber;

        public ValueViewHolder(@NonNull View view) {
            super(view);
            this.mPhoneNumber = (TextView) view.findViewById(C4558R.C4560id.phoneNumber);
            this.mLableType = (TextView) view.findViewById(C4558R.C4560id.lableType);
        }
    }

    public static void show(@Nullable FragmentManager fragmentManager, @Nullable IMAddrBookItem iMAddrBookItem) {
        show(fragmentManager, iMAddrBookItem, 0);
    }

    public static void show(@Nullable FragmentManager fragmentManager, @Nullable IMAddrBookItem iMAddrBookItem, int i) {
        if (iMAddrBookItem != null && fragmentManager != null && !CmmSIPCallManager.getInstance().isPBXInactive()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_ADDRBOOKITEM, iMAddrBookItem);
            bundle.putInt("requestCode", i);
            PhoneLabelFragment phoneLabelFragment = new PhoneLabelFragment();
            phoneLabelFragment.setArguments(bundle);
            phoneLabelFragment.show(fragmentManager, PhoneLabelFragment.class.getName());
        }
    }

    public PhoneLabelFragment() {
        setStyle(1, C4558R.style.ZMDialog_Material);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mContact = (IMAddrBookItem) arguments.getSerializable(ARG_ADDRBOOKITEM);
        }
        this.mAdapter = new PhoneLabelAdapter(getActivity(), this.mContact);
        View inflate = layoutInflater.inflate(C4558R.layout.zm_phone_label_list, null);
        this.mNameTv = (TextView) inflate.findViewById(C4558R.C4560id.nameTV);
        this.mPhoneLv = (RecyclerView) inflate.findViewById(C4558R.C4560id.phoneLV);
        updateUserInfo();
        this.mPhoneLv.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mPhoneLv.setAdapter(this.mAdapter);
        return inflate;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }

    public void updateUserInfo() {
        if (this.mContact != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mContact.getJid());
                if (buddyWithJID != null) {
                    IMAddrBookItem iMAddrBookItem = this.mContact;
                    this.mContact = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    if (this.mContact != null) {
                        if (iMAddrBookItem.isFromWebSearch()) {
                            this.mContact.setIsFromWebSearch(true);
                        }
                        IMAddrBookItem iMAddrBookItem2 = this.mContact;
                        if (iMAddrBookItem2 != null) {
                            iMAddrBookItem2.setContact(iMAddrBookItem.getContact());
                        }
                    } else {
                        return;
                    }
                }
            }
            this.mNameTv.setText(this.mContact.getScreenName());
            int i = 0;
            if (getArguments() != null) {
                i = getArguments().getInt("requestCode", 0);
            }
            ArrayList arrayList = new ArrayList();
            if (canAccessMoreDetails() || this.mContact.isFromPhoneContacts()) {
                LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
                if (!StringUtil.isEmptyOrNull(this.mContact.getBuddyPhoneNumber())) {
                    linkedHashSet.add(ZMPhoneUtils.formatPhoneNumber(this.mContact.getBuddyPhoneNumber()));
                }
                if (!StringUtil.isEmptyOrNull(this.mContact.getProfilePhoneNumber())) {
                    linkedHashSet.add(ZMPhoneUtils.formatPhoneNumber(this.mContact.getProfilePhoneNumber(), this.mContact.getProfileCountryCode(), "", true));
                }
                ContactCloudSIP iCloudSIPCallNumber = this.mContact.getICloudSIPCallNumber();
                if (CmmSIPCallManager.getInstance().isCloudPBXEnabled() && iCloudSIPCallNumber != null) {
                    String companyNumber = iCloudSIPCallNumber.getCompanyNumber();
                    String extension = iCloudSIPCallNumber.getExtension();
                    if (i != 1001 && ((CmmSIPCallManager.getInstance().isSameCompanyWithLoginUser(companyNumber) || this.mContact.isSharedGlobalDirectory()) && !StringUtil.isEmptyOrNull(extension))) {
                        DetailItem detailItem = new DetailItem();
                        detailItem.label = getString(C4558R.string.zm_title_extension_35373);
                        detailItem.value = extension;
                        detailItem.onClick = new OnClick() {
                            public void onClick(@NonNull DetailItem detailItem) {
                                PhoneLabelFragment.this.onClickCompanyNumber(detailItem.value);
                            }
                        };
                        arrayList.add(detailItem);
                    }
                    ArrayList<String> formattedDirectNumber = iCloudSIPCallNumber.getFormattedDirectNumber();
                    if (!CollectionsUtil.isCollectionEmpty(formattedDirectNumber)) {
                        for (String str : formattedDirectNumber) {
                            DetailItem detailItem2 = new DetailItem();
                            detailItem2.label = getString(C4558R.string.zm_title_direct_number_31439);
                            detailItem2.value = str;
                            detailItem2.onClick = new OnClick() {
                                public void onClick(@NonNull DetailItem detailItem) {
                                    PhoneLabelFragment.this.click2CallSip(detailItem.value);
                                }
                            };
                            arrayList.add(detailItem2);
                        }
                    }
                }
                if (linkedHashSet.size() > 0) {
                    for (String str2 : linkedHashSet) {
                        if (!TextUtils.isEmpty(str2)) {
                            DetailItem detailItem3 = new DetailItem();
                            detailItem3.label = getString(C4558R.string.zm_lbl_phone_number_19993);
                            detailItem3.value = ZMPhoneUtils.formatPhoneNumber(str2);
                            detailItem3.onClick = new OnClick() {
                                public void onClick(@NonNull DetailItem detailItem) {
                                    PhoneLabelFragment.this.onClickPhoneNumber(detailItem.value);
                                }
                            };
                            arrayList.add(detailItem3);
                        }
                    }
                }
                if (this.mContact.getContact() == null) {
                    ABContactsCache instance = ABContactsCache.getInstance();
                    IMAddrBookItem iMAddrBookItem3 = this.mContact;
                    iMAddrBookItem3.setContact(instance.getFirstContactByPhoneNumber(iMAddrBookItem3.getBuddyPhoneNumber()));
                }
                Contact contact = this.mContact.getContact();
                if (contact != null && !CollectionsUtil.isCollectionEmpty(contact.accounts)) {
                    Iterator it = contact.accounts.iterator();
                    while (it.hasNext()) {
                        ContactType contactType = (ContactType) it.next();
                        if (contactType != null && !CollectionsUtil.isCollectionEmpty(contactType.phoneNumbers)) {
                            Iterator it2 = contactType.phoneNumbers.iterator();
                            while (it2.hasNext()) {
                                PhoneNumber phoneNumber = (PhoneNumber) it2.next();
                                String displayPhoneNumber = phoneNumber.getDisplayPhoneNumber();
                                if (!StringUtil.isEmptyOrNull(displayPhoneNumber) && !linkedHashSet.contains(displayPhoneNumber)) {
                                    linkedHashSet.add(displayPhoneNumber);
                                    DetailItem detailItem4 = new DetailItem();
                                    detailItem4.label = phoneNumber.getLabel();
                                    detailItem4.value = displayPhoneNumber;
                                    detailItem4.onClick = new OnClick() {
                                        public void onClick(DetailItem detailItem) {
                                            PhoneLabelFragment.this.onClickPhoneNumber(detailItem.value);
                                        }
                                    };
                                    arrayList.add(detailItem4);
                                }
                            }
                        }
                    }
                }
                if (CmmSIPCallManager.getInstance().isSipCallEnabled() && !CmmSIPCallManager.getInstance().isCloudPBXEnabled() && this.mContact.isSIPAccount()) {
                    DetailItem detailItem5 = new DetailItem();
                    detailItem5.label = getString(C4558R.string.zm_lbl_internal_number_14480);
                    detailItem5.value = this.mContact.getSipPhoneNumber();
                    detailItem5.onClick = new OnClick() {
                        public void onClick(DetailItem detailItem) {
                            PhoneLabelFragment.this.onClickPanelSIIP();
                        }
                    };
                    arrayList.add(detailItem5);
                }
            }
            this.mAdapter.updateData(arrayList);
        }
    }

    /* access modifiers changed from: private */
    public void onClickPanelSIIP() {
        IMAddrBookItem iMAddrBookItem = this.mContact;
        if (iMAddrBookItem != null) {
            click2CallSip(iMAddrBookItem.getSipPhoneNumber());
        }
    }

    /* access modifiers changed from: private */
    public void onClickCompanyNumber(@Nullable String str) {
        click2CallSip(str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0033, code lost:
        if (r0.isSameCompany(r0.getICloudSIPCallNumber().getCompanyNumber()) != false) goto L_0x0035;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean canAccessMoreDetails() {
        /*
            r3 = this;
            com.zipow.videobox.view.IMAddrBookItem r0 = r3.mContact
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r0 = r0.getZoomMessenger()
            if (r0 != 0) goto L_0x0011
            return r1
        L_0x0011:
            com.zipow.videobox.view.IMAddrBookItem r2 = r3.mContact
            java.lang.String r2 = r2.getJid()
            boolean r0 = r0.isMyContact(r2)
            if (r0 != 0) goto L_0x0035
            com.zipow.videobox.view.IMAddrBookItem r0 = r3.mContact
            com.zipow.videobox.ptapp.mm.ContactCloudSIP r0 = r0.getICloudSIPCallNumber()
            if (r0 == 0) goto L_0x0036
            com.zipow.videobox.view.IMAddrBookItem r0 = r3.mContact
            com.zipow.videobox.ptapp.mm.ContactCloudSIP r2 = r0.getICloudSIPCallNumber()
            java.lang.String r2 = r2.getCompanyNumber()
            boolean r0 = r0.isSameCompany(r2)
            if (r0 == 0) goto L_0x0036
        L_0x0035:
            r1 = 1
        L_0x0036:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.PhoneLabelFragment.canAccessMoreDetails():boolean");
    }

    /* access modifiers changed from: private */
    public void click2CallSip(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (!NetworkUtil.hasDataNetwork(getContext())) {
                showSipUnavailable();
                return;
            }
            if (!StringUtil.isEmptyOrNull(str)) {
                callSip(str);
            }
        }
    }

    private void showSipUnavailable() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            SimpleMessageDialog.newInstance(getString(C4558R.string.zm_sip_error_network_unavailable_99728), false).show(activity.getSupportFragmentManager(), SimpleMessageDialog.class.getSimpleName());
        }
    }

    private void callSip(@Nullable String str) {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (instance.checkNetwork(getContext()) && instance.checkIsPbxActive(getContext())) {
            int i = 0;
            if (getArguments() != null) {
                i = getArguments().getInt("requestCode", 0);
            }
            if (i == 109) {
                pickNumberAsResult(str, this.mNameTv.getText().toString());
            } else if (i == 1001) {
                pickNumberAsPBXMessageContact(str);
            } else if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                instance.callPeer(str, this.mNameTv.getText().toString());
                dismiss();
            } else {
                this.mSelectedPhoneNumber = str;
                zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 11);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickPhoneNumber(@Nullable String str) {
        if (this.mContact != null && getActivity() != null) {
            if (CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
                click2CallSip(str);
            } else {
                doCallNumber(str);
            }
        }
    }

    private void doCallNumber(String str) {
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.CALL_PHONE") == 0) {
            callNumber(str);
            return;
        }
        zm_requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 12);
        this.mDailString = str;
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C38486 r2 = new EventAction("PhoneLabelFragmentPermissionResult") {
                public void run(IUIElement iUIElement) {
                    if (iUIElement instanceof PhoneLabelFragment) {
                        ((PhoneLabelFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
                    }
                }
            };
            eventTaskManager.pushLater("PhoneLabelFragmentPermissionResult", r2);
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            if (i == 11) {
                if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                    String str = this.mSelectedPhoneNumber;
                    if (str != null) {
                        callSip(str);
                    }
                    this.mSelectedPhoneNumber = null;
                }
            } else if (i == 12 && checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                callNumber(this.mDailString);
            }
        }
    }

    @SuppressLint({"MissingPermission"})
    private void callNumber(String str) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ZMIntentUtil.callNumber(zMActivity, str);
        }
    }

    private void pickNumberAsResult(String str, String str2) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent();
            intent.putExtra(PBXDirectorySearchFragment.RESULT_PHONE_NUMBER, str);
            intent.putExtra(PBXDirectorySearchFragment.RESULT_DISPLAY_NAME, str2);
            activity.setResult(-1, intent);
            activity.finish();
            UIUtil.closeSoftKeyboardInActivity((ZMActivity) getActivity());
        }
    }

    private void pickNumberAsPBXMessageContact(String str) {
        if (!TextUtils.isEmpty(str)) {
            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof IPBXMessageSelectContact) {
                ((IPBXMessageSelectContact) parentFragment).selectContact(new PBXMessageContact(str, this.mContact), true);
            }
            dismissAllowingStateLoss();
        }
    }
}
