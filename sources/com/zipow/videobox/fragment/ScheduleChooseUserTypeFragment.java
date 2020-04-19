package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem;
import com.zipow.videobox.util.ZMScheduleUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMChildListView;
import p021us.zoom.videomeetings.C4558R;

public class ScheduleChooseUserTypeFragment extends ZMDialogFragment implements OnClickListener, ExtListener {
    /* access modifiers changed from: private */
    public JoinMethodAdapter mAdapter;
    /* access modifiers changed from: private */
    public String mAuthId;
    /* access modifiers changed from: private */
    public ArrayList<LoginMeetingAuthItem> mAuths;
    /* access modifiers changed from: private */
    public String mDeletedAuthId;
    /* access modifiers changed from: private */
    public String mDomains;
    private ImageView mImgEveryone;
    private ImageView mImgSpecifiedDomains;
    /* access modifiers changed from: private */
    public boolean mIsAlreadyShowMethodDeletedTip = false;
    private boolean mIsLockOnlyAuthUsersCanJoin = false;
    private ZMChildListView mLvAuths;
    private View mOptEveryone;
    private View mOptSpecifiedDomains;
    private View mPanelEditDomains;
    /* access modifiers changed from: private */
    public LoginMeetingAuthItem mSelectAuthItem;
    @Nullable
    private ArrayList<CharSequence> mSpecifiedDomains;
    private TextView mTxtDomainsLabel;
    private TextView mTxtEditDomainsLabel;
    private int mUserType = 1;

    public static class JoinMethodAdapter extends BaseAdapter {
        private Context context;
        private String mDefaultSelectedAuthId;
        private List<LoginMeetingAuthItem> mList;

        public long getItemId(int i) {
            return (long) i;
        }

        public JoinMethodAdapter(@NonNull Context context2, @NonNull List<LoginMeetingAuthItem> list, @Nullable String str) {
            this.context = context2;
            this.mList = list;
            this.mDefaultSelectedAuthId = str;
        }

        public void clearAuthId() {
            if (!StringUtil.isEmptyOrNull(this.mDefaultSelectedAuthId)) {
                this.mDefaultSelectedAuthId = "";
            }
        }

        public int getCount() {
            List<LoginMeetingAuthItem> list = this.mList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i >= 0) {
                return this.mList.get(i);
            }
            return null;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            int i2 = 0;
            if (view == null || !"joinMethodItem".equals(view.getTag())) {
                view = LayoutInflater.from(this.context).inflate(C4558R.layout.zm_schedule_join_method_item, viewGroup, false);
                view.setTag("joinMethodItem");
            }
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgSelect);
            LoginMeetingAuthItem loginMeetingAuthItem = (LoginMeetingAuthItem) this.mList.get(i);
            ((TextView) view.findViewById(C4558R.C4560id.txtContent)).setText(loginMeetingAuthItem.getAuthName());
            if (!StringUtil.isEmptyOrNull(this.mDefaultSelectedAuthId)) {
                if (!this.mDefaultSelectedAuthId.equalsIgnoreCase(loginMeetingAuthItem.getAuthId())) {
                    i2 = 8;
                }
                imageView.setVisibility(i2);
            } else {
                if (!loginMeetingAuthItem.isUiSelect()) {
                    i2 = 8;
                }
                imageView.setVisibility(i2);
            }
            return view;
        }
    }

    public void onKeyboardClosed() {
    }

    public void onKeyboardOpen() {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showInActivity(@Nullable Fragment fragment, int i, int i2, String str, String str2, ArrayList<LoginMeetingAuthItem> arrayList) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(ZMScheduleUtil.ARG_JOIN_USER_TYPE, i2);
            bundle.putString(ZMScheduleUtil.ARG_MEETING_AUTH_ID, str);
            bundle.putString(ZMScheduleUtil.ARG_DELETED_METHOD_AUTH_ID, str2);
            bundle.putParcelableArrayList(ZMScheduleUtil.ARG_MEETING_AUTH_LIST, arrayList);
            SimpleActivity.show(fragment, ScheduleChooseUserTypeFragment.class.getName(), bundle, i, false);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_schedule_choose_user_type, viewGroup, false);
        this.mOptEveryone = inflate.findViewById(C4558R.C4560id.optEveryone);
        this.mOptSpecifiedDomains = inflate.findViewById(C4558R.C4560id.optSpecifiedDomains);
        this.mLvAuths = (ZMChildListView) inflate.findViewById(C4558R.C4560id.lvAuths);
        this.mImgEveryone = (ImageView) inflate.findViewById(C4558R.C4560id.imgEveryone);
        this.mImgSpecifiedDomains = (ImageView) inflate.findViewById(C4558R.C4560id.imgSpecifiedDomains);
        this.mTxtDomainsLabel = (TextView) inflate.findViewById(C4558R.C4560id.txtDomainsLabel);
        this.mPanelEditDomains = inflate.findViewById(C4558R.C4560id.panelEditDomains);
        this.mTxtEditDomainsLabel = (TextView) inflate.findViewById(C4558R.C4560id.txtEditDomainsLabel);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mOptEveryone.setOnClickListener(this);
        this.mOptSpecifiedDomains.setOnClickListener(this);
        this.mPanelEditDomains.setOnClickListener(this);
        this.mSpecifiedDomains = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mUserType = arguments.getInt(ZMScheduleUtil.ARG_JOIN_USER_TYPE);
            this.mAuths = arguments.getParcelableArrayList(ZMScheduleUtil.ARG_MEETING_AUTH_LIST);
            this.mAuthId = arguments.getString(ZMScheduleUtil.ARG_MEETING_AUTH_ID);
            this.mDeletedAuthId = arguments.getString(ZMScheduleUtil.ARG_DELETED_METHOD_AUTH_ID);
        }
        if (bundle != null) {
            this.mUserType = bundle.getInt("mJoinUserType");
            this.mSpecifiedDomains = bundle.getCharSequenceArrayList("mJoinSpecifiedDomains");
            this.mAuths = bundle.getParcelableArrayList("mAuthsList");
            this.mAuthId = bundle.getString("mAuthId");
            this.mDeletedAuthId = bundle.getString("mDeletedAuthId");
            this.mIsAlreadyShowMethodDeletedTip = bundle.getBoolean("mIsAlreadyShowMethodDeletedTip", false);
        }
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            this.mIsLockOnlyAuthUsersCanJoin = currentUserProfile.isLockOnlyAuthUsersCanJoin();
        }
        initLoginMeetingAuthsList();
        if (!this.mIsLockOnlyAuthUsersCanJoin || this.mUserType != 2) {
            updateJoinUserType(this.mUserType);
        } else {
            onClickOptSpecifiedDomains();
        }
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("mJoinUserType", this.mUserType);
        bundle.putCharSequenceArrayList("mJoinSpecifiedDomains", this.mSpecifiedDomains);
        bundle.putParcelableArrayList("mAuthsList", this.mAuths);
        bundle.putString("mAuthId", this.mAuthId);
        bundle.putString("mDeletedAuthId", this.mDeletedAuthId);
        bundle.putBoolean("mIsAlreadyShowMethodDeletedTip", this.mIsAlreadyShowMethodDeletedTip);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2007 && intent != null && i2 == -1) {
            this.mDomains = intent.getStringExtra(ZMScheduleUtil.ARG_SPECIFIED_DOMAINS);
            this.mSelectAuthItem.setAuthDomain(this.mDomains);
            if (!StringUtil.isEmptyOrNull(this.mDomains)) {
                updatePanelEditDomainsStr(this.mDomains);
            }
        }
    }

    private void updateJoinUserType(int i) {
        this.mUserType = i;
        this.mImgEveryone.setVisibility(8);
        this.mImgSpecifiedDomains.setVisibility(8);
        switch (this.mUserType) {
            case 1:
                this.mPanelEditDomains.setVisibility(8);
                this.mImgEveryone.setVisibility(0);
                setQualifiedDomainPanelVisiable(false);
                if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                    AccessibilityUtil.announceForAccessibilityCompat(this.mOptEveryone, C4558R.string.zm_accessibility_everyone_select_120783);
                    return;
                }
                return;
            case 2:
                refreshPanelEditDomainsVisibility(this.mSelectAuthItem);
                this.mImgSpecifiedDomains.setVisibility(0);
                setQualifiedDomainPanelVisiable(true);
                if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                    AccessibilityUtil.announceForAccessibilityCompat(this.mOptSpecifiedDomains, C4558R.string.zm_accessibility_auth_join_select_120783);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void initLoginMeetingAuthsList() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (!(currentUserProfile == null || currentUserProfile.getMeetingAuths() == null)) {
            ArrayList<LoginMeetingAuthItem> arrayList = this.mAuths;
            if (arrayList == null || arrayList.size() == 0) {
                this.mAuths = ZMScheduleUtil.getAuthList(currentUserProfile);
            }
            this.mSelectAuthItem = ZMScheduleUtil.getAuthItemById(this.mAuths, this.mAuthId);
            LoginMeetingAuthItem loginMeetingAuthItem = this.mSelectAuthItem;
            if (loginMeetingAuthItem != null) {
                this.mDomains = loginMeetingAuthItem.getAuthDomain();
            }
            Context context = getContext();
            if (context != null) {
                this.mAdapter = new JoinMethodAdapter(context, this.mAuths, this.mAuthId);
                this.mLvAuths.setAdapter(this.mAdapter);
                this.mLvAuths.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        if (!ScheduleChooseUserTypeFragment.this.mIsAlreadyShowMethodDeletedTip && !StringUtil.isEmptyOrNull(ScheduleChooseUserTypeFragment.this.mDeletedAuthId) && StringUtil.isSameString(ScheduleChooseUserTypeFragment.this.mAuthId, ScheduleChooseUserTypeFragment.this.mDeletedAuthId) && !StringUtil.isSameString(((LoginMeetingAuthItem) ScheduleChooseUserTypeFragment.this.mAuths.get(i)).getAuthId(), ScheduleChooseUserTypeFragment.this.mDeletedAuthId)) {
                            ScheduleChooseUserTypeFragment scheduleChooseUserTypeFragment = ScheduleChooseUserTypeFragment.this;
                            scheduleChooseUserTypeFragment.showMethodDeleteTip(scheduleChooseUserTypeFragment.mSelectAuthItem.getAuthName());
                        }
                        ScheduleChooseUserTypeFragment scheduleChooseUserTypeFragment2 = ScheduleChooseUserTypeFragment.this;
                        scheduleChooseUserTypeFragment2.mSelectAuthItem = (LoginMeetingAuthItem) scheduleChooseUserTypeFragment2.mAuths.get(i);
                        ScheduleChooseUserTypeFragment scheduleChooseUserTypeFragment3 = ScheduleChooseUserTypeFragment.this;
                        scheduleChooseUserTypeFragment3.mAuthId = scheduleChooseUserTypeFragment3.mSelectAuthItem.getAuthId();
                        ScheduleChooseUserTypeFragment scheduleChooseUserTypeFragment4 = ScheduleChooseUserTypeFragment.this;
                        scheduleChooseUserTypeFragment4.mDomains = ((LoginMeetingAuthItem) scheduleChooseUserTypeFragment4.mAuths.get(i)).getAuthDomain();
                        Iterator it = ScheduleChooseUserTypeFragment.this.mAuths.iterator();
                        while (it.hasNext()) {
                            ((LoginMeetingAuthItem) it.next()).setUiSelect(false);
                        }
                        ((LoginMeetingAuthItem) ScheduleChooseUserTypeFragment.this.mAuths.get(i)).setUiSelect(true);
                        ScheduleChooseUserTypeFragment.this.mAdapter.clearAuthId();
                        ScheduleChooseUserTypeFragment.this.mAdapter.notifyDataSetChanged();
                        ScheduleChooseUserTypeFragment scheduleChooseUserTypeFragment5 = ScheduleChooseUserTypeFragment.this;
                        scheduleChooseUserTypeFragment5.refreshPanelEditDomainsVisibility(scheduleChooseUserTypeFragment5.mSelectAuthItem);
                    }
                });
                refreshPanelEditDomainsVisibility(this.mSelectAuthItem);
            }
        }
    }

    /* access modifiers changed from: private */
    public void refreshPanelEditDomainsVisibility(@NonNull LoginMeetingAuthItem loginMeetingAuthItem) {
        if (loginMeetingAuthItem.getAuthType() != 1 || StringUtil.isEmptyOrNull(loginMeetingAuthItem.getAuthDomain())) {
            this.mPanelEditDomains.setVisibility(8);
        } else {
            updatePanelEditDomainsStr(loginMeetingAuthItem.getAuthDomain());
        }
    }

    private void updatePanelEditDomainsStr(@NonNull String str) {
        int domainListSizeFromStr = ZMScheduleUtil.getDomainListSizeFromStr(str);
        this.mTxtEditDomainsLabel.setText(getResources().getQuantityString(C4558R.plurals.zm_lbl_view_all_domain_120783, domainListSizeFromStr, new Object[]{Integer.valueOf(domainListSizeFromStr)}));
        this.mPanelEditDomains.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void showMethodDeleteTip(@NonNull String str) {
        this.mIsAlreadyShowMethodDeletedTip = true;
        new Builder(getActivity()).setTitle((CharSequence) getString(C4558R.string.zm_msg_join_method_delete_120783, str)).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) null).create().show();
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnBack) {
                onClickBtnBack();
            } else if (id == C4558R.C4560id.optEveryone) {
                if (!this.mIsLockOnlyAuthUsersCanJoin) {
                    onClickOptEveryone();
                }
            } else if (id == C4558R.C4560id.optSpecifiedDomains) {
                if (!this.mIsLockOnlyAuthUsersCanJoin) {
                    onClickOptSpecifiedDomains();
                }
            } else if (id == C4558R.C4560id.panelEditDomains) {
                onClickEditDomains();
            }
        }
    }

    private void onClickBtnSave() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent();
            intent.putExtra(ZMScheduleUtil.ARG_JOIN_USER_TYPE, this.mUserType);
            if (this.mUserType == 2) {
                intent.putExtra(ZMScheduleUtil.ARG_MEETING_AUTH_ITEM, this.mSelectAuthItem);
            }
            activity.setResult(-1, intent);
            dismiss();
        }
    }

    private void onClickBtnBack() {
        onClickBtnSave();
    }

    private void onClickOptEveryone() {
        updateJoinUserType(1);
    }

    private void onClickOptSpecifiedDomains() {
        updateJoinUserType(2);
    }

    private void onClickEditDomains() {
        ScheduleDomainListFragment.showInActivity(this, ZMScheduleUtil.REQUEST_EDIT_DOMAINS, this.mDomains, this.mIsLockOnlyAuthUsersCanJoin);
    }

    private void setQualifiedDomainPanelVisiable(boolean z) {
        int i = 0;
        this.mTxtDomainsLabel.setVisibility(z ? 0 : 8);
        ZMChildListView zMChildListView = this.mLvAuths;
        if (!z) {
            i = 8;
        }
        zMChildListView.setVisibility(i);
    }

    public void dismiss() {
        finishFragment(true);
    }

    public boolean onBackPressed() {
        onClickBtnSave();
        return false;
    }
}
