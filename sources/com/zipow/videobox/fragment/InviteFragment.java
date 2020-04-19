package com.zipow.videobox.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.InviteActivity;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.ContactsMatchHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTUI.IIMListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.ptapp.delegate.PTUIDelegation;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.MemCache;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.InviteBuddyItem;
import com.zipow.videobox.view.InviteBuddyItemSpan;
import com.zipow.videobox.view.InviteBuddyListView;
import com.zipow.videobox.view.InviteBuddyListView.Listener;
import com.zipow.videobox.view.ZMReplaceSpanMovementMethod;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMEditText;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class InviteFragment extends ZMTipFragment implements OnClickListener, Listener, IIMListener, IPTUIListener, KeyboardListener, IABContactsCacheListener {
    public static final String ARG_ANCHOR_ID = "anchorId";
    public static final String ARG_MEETING_ID = "meetingId";
    public static final String ARG_MEETING_NUMBER = "meetingNumber";
    public static final String ARG_SELECT_FROM_ADDRBOOK = "inviteAddrBook";
    public static final String ARG_SELECT_FROM_ZOOMROOMS = "inviteZoomRooms";
    private static final String TAG = "InviteFragment";
    private int mAnchorId = 0;
    @NonNull
    private MemCache<String, Bitmap> mAvatarCache = new MemCache<>(20);
    private Button mBtnInvite;
    /* access modifiers changed from: private */
    public ZMEditText mEdtSelected;
    /* access modifiers changed from: private */
    @Nullable
    public GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private boolean mInviteAddrBook = false;
    /* access modifiers changed from: private */
    public View mInviteMaxAlertPanel;
    private TextView mInviteMaxTxt;
    /* access modifiers changed from: private */
    public InviteBuddyListView mListView;
    @Nullable
    private String mMeetingId;
    private long mMeetingNumber;
    @Nullable
    private ProgressDialog mProgressDialog;
    @NonNull
    private SearchFilterRunnable mRunnableFilter = new SearchFilterRunnable();
    private IZoomMessengerUIListener mZoomMessengerUIListener;

    public static class GuestureListener extends SimpleOnGestureListener {
        private View mHolderView;
        private View mKeyboardView;

        public GuestureListener(View view, View view2) {
            this.mHolderView = view;
            this.mKeyboardView = view2;
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            View view = this.mHolderView;
            if (view != null) {
                view.requestFocus();
                UIUtil.closeSoftKeyboard(this.mHolderView.getContext(), this.mKeyboardView);
            }
            return super.onScroll(motionEvent, motionEvent2, f, f2);
        }
    }

    public static class InviteFailedDialog extends ZMDialogFragment {
        public InviteFailedDialog() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            return new Builder(getActivity()).setTitle(C4558R.string.zm_alert_invite_failed).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
        }
    }

    public class SearchFilterRunnable implements Runnable {
        @NonNull
        private String mKey = "";

        public SearchFilterRunnable() {
        }

        public void setKey(@Nullable String str) {
            if (str == null) {
                str = "";
            }
            this.mKey = str;
        }

        @NonNull
        public String getKey() {
            return this.mKey;
        }

        public void run() {
            InviteFragment.this.mListView.filter(this.mKey);
        }
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onIMLocalStatusChanged(int i) {
    }

    public void onIMReceived(IMMessage iMMessage) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public void onQueryIPLocation(int i, IPLocationInfo iPLocationInfo) {
    }

    public void onSubscriptionRequest() {
    }

    public void onSubscriptionUpdate() {
    }

    public static void show(@NonNull FragmentManager fragmentManager, int i, long j, String str) {
        show(fragmentManager, i, j, str, false);
    }

    public static void show(@NonNull FragmentManager fragmentManager, int i, long j, String str, boolean z) {
        show(fragmentManager, i, j, str, z, false);
    }

    public static void show(@NonNull FragmentManager fragmentManager, int i, long j, String str, boolean z, boolean z2) {
        InviteFragment inviteFragment = getInviteFragment(fragmentManager);
        if (inviteFragment == null) {
            InviteFragment inviteFragment2 = new InviteFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("anchorId", i);
            bundle.putLong(ARG_MEETING_NUMBER, j);
            bundle.putString(ARG_MEETING_ID, str);
            bundle.putBoolean(ARG_SELECT_FROM_ADDRBOOK, z);
            bundle.putBoolean(ARG_SELECT_FROM_ZOOMROOMS, z2);
            inviteFragment2.setArguments(bundle);
            inviteFragment2.show(fragmentManager, InviteFragment.class.getName());
            return;
        }
        Bundle arguments = inviteFragment.getArguments();
        if (arguments != null) {
            arguments.putLong(ARG_MEETING_NUMBER, j);
            arguments.putString(ARG_MEETING_ID, str);
        }
        inviteFragment.updateMeetingInfo(j, str);
        inviteFragment.setTipVisible(true);
    }

    @Nullable
    public static InviteFragment getInviteFragment(FragmentManager fragmentManager) {
        return (InviteFragment) fragmentManager.findFragmentByTag(InviteFragment.class.getName());
    }

    public static boolean hide(@NonNull FragmentManager fragmentManager) {
        InviteFragment inviteFragment = getInviteFragment(fragmentManager);
        if (inviteFragment != null) {
            if (!inviteFragment.getShowsTip()) {
                inviteFragment.dismiss();
                return true;
            } else if (inviteFragment.isTipVisible()) {
                inviteFragment.setTipVisible(false);
                return true;
            }
        }
        return false;
    }

    public static boolean dismiss(@NonNull FragmentManager fragmentManager) {
        InviteFragment inviteFragment = getInviteFragment(fragmentManager);
        if (inviteFragment == null) {
            return false;
        }
        inviteFragment.dismiss();
        return true;
    }

    public void onDestroy() {
        this.mHandler.removeCallbacks(this.mRunnableFilter);
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        if (!PTAppDelegation.getInstance().hasActiveCall()) {
            dismiss();
            return;
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            boolean z = arguments.getBoolean(ARG_SELECT_FROM_ADDRBOOK, false);
            boolean z2 = arguments.getBoolean(ARG_SELECT_FROM_ZOOMROOMS, false);
            this.mInviteAddrBook = z;
            this.mListView.setFilter(getFilter());
            if (z2) {
                this.mListView.setIsInviteZoomRooms(true);
            } else {
                this.mListView.setIsInviteAddrBook(z);
            }
            this.mListView.reloadAllBuddyItems();
            if (!z) {
                PTUIDelegation.getInstance().addPTUIListener(this);
                PTUIDelegation.getInstance().addIMListener(this);
            }
            InviteBuddyListView inviteBuddyListView = this.mListView;
            if (inviteBuddyListView != null) {
                inviteBuddyListView.onResume();
            }
            updateUIForConfCallStatus(PTAppDelegation.getInstance().getCallStatus());
            ABContactsCache.getInstance().addListener(this);
        }
    }

    public void onPause() {
        super.onPause();
        PTUIDelegation.getInstance().removePTUIListener(this);
        PTUIDelegation.getInstance().removeIMListener(this);
        ABContactsCache.getInstance().removeListener(this);
        this.mAvatarCache.clear();
    }

    public void onDestroyView() {
        if (this.mZoomMessengerUIListener != null) {
            ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        }
        super.onDestroyView();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isTipVisible", isTipVisible());
    }

    private void updateMeetingInfo(long j, String str) {
        this.mMeetingNumber = j;
        this.mMeetingId = str;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view;
        Bundle arguments = getArguments();
        this.mMeetingId = arguments.getString(ARG_MEETING_ID);
        this.mMeetingNumber = arguments.getLong(ARG_MEETING_NUMBER);
        if (UIMgr.isLargeMode(getActivity())) {
            view = layoutInflater.inflate(C4558R.layout.zm_invite, null);
        } else {
            view = layoutInflater.inflate(C4558R.layout.zm_invite_main_screen, null);
            ((ZMKeyboardDetector) view.findViewById(C4558R.C4560id.keyboardDetector)).setKeyboardListener(this);
        }
        this.mInviteMaxAlertPanel = view.findViewById(C4558R.C4560id.panelInviteMaxAlert);
        this.mInviteMaxTxt = (TextView) view.findViewById(C4558R.C4560id.txtInviteMaxAlert);
        this.mListView = (InviteBuddyListView) view.findViewById(C4558R.C4560id.buddyListView);
        this.mEdtSelected = (ZMEditText) view.findViewById(C4558R.C4560id.edtSelected);
        this.mBtnInvite = (Button) view.findViewById(C4558R.C4560id.btnInvite);
        Button button = (Button) view.findViewById(C4558R.C4560id.btnBack);
        updateMeetingInfo(this.mMeetingNumber, this.mMeetingId);
        this.mBtnInvite.setOnClickListener(this);
        button.setOnClickListener(this);
        this.mListView.setListener(this);
        this.mListView.setAvatarMemCache(this.mAvatarCache);
        this.mEdtSelected.setSelected(true);
        this.mEdtSelected.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (i3 < i2) {
                    final InviteBuddyItemSpan[] inviteBuddyItemSpanArr = (InviteBuddyItemSpan[]) InviteFragment.this.mEdtSelected.getText().getSpans(i3 + i, i + i2, InviteBuddyItemSpan.class);
                    if (inviteBuddyItemSpanArr.length > 0) {
                        InviteFragment.this.mHandler.post(new Runnable() {
                            public void run() {
                                if (InviteFragment.this.isResumed()) {
                                    InviteFragment.this.mInviteMaxAlertPanel.setVisibility(8);
                                    for (InviteBuddyItemSpan item : inviteBuddyItemSpanArr) {
                                        InviteBuddyItem item2 = item.getItem();
                                        if (item2 != null) {
                                            InviteFragment.this.mListView.unselectBuddy(item2);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }

            public void afterTextChanged(Editable editable) {
                InviteFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (InviteFragment.this.isResumed()) {
                            InviteFragment.this.startFilter(InviteFragment.this.getFilter());
                        }
                    }
                });
            }
        });
        this.mEdtSelected.setMovementMethod(ZMReplaceSpanMovementMethod.getInstance());
        this.mEdtSelected.setOnClickListener(this);
        updateButtonInvite(getSelectedBuddiesCount());
        this.mGestureDetector = new GestureDetector(getActivity(), new GuestureListener(this.mListView, this.mEdtSelected));
        this.mListView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return InviteFragment.this.mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        boolean z = arguments.getBoolean(ARG_SELECT_FROM_ADDRBOOK, false);
        boolean z2 = arguments.getBoolean(ARG_SELECT_FROM_ZOOMROOMS, false);
        this.mInviteAddrBook = z;
        if (z || z2) {
            if (this.mZoomMessengerUIListener == null) {
                this.mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
                    public void onIndicateBuddyInfoUpdated(String str) {
                        InviteFragment.this.onIndicateZoomMessengerBuddyInfoUpdated(str);
                    }

                    public void onIndicateBuddyListUpdated() {
                        InviteFragment.this.onIndicateZoomMessengerBuddyListUpdated();
                    }

                    public void onIndicateInfoUpdatedWithJID(@NonNull String str) {
                        InviteFragment.this.onIndicateZoomMessengerInfoUpdatedWithJID(str);
                    }

                    public void onConnectReturn(int i) {
                        InviteFragment.this.onIndicationZoomMessengerConnectReturn(i);
                    }

                    public void onSearchBuddyByKey(String str, int i) {
                        InviteFragment.this.onIndicationZoomMessengerSearchBuddyByKey(str, i);
                    }

                    public void onSearchBuddyByKeyV2(String str, String str2, String str3, int i) {
                        InviteFragment.this.onIndicationZoomMessengerSearchBuddyByKey(str, i);
                    }

                    public void Indicate_BuddyPresenceChanged(@NonNull String str) {
                        InviteFragment.this.onIndicateZoomMessengerInfoUpdatedWithJID(str);
                    }

                    public void Indicate_OnlineBuddies(List<String> list) {
                        InviteFragment.this.onIndicateZoomMessengerOnlineBuddies(list);
                    }

                    public void Indicate_GetContactsPresence(List<String> list, List<String> list2) {
                        InviteFragment.this.onIndicateZoomMessengerGetContactsPresence(list, list2);
                    }
                };
            }
            ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
            if (VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.READ_CONTACTS") != 0 && PTApp.getInstance().isPhoneNumberRegistered() && AppUtil.canRequestContactPermission()) {
                zm_requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 0);
                AppUtil.saveRequestContactPermissionTime();
            }
        }
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (InviteFragment.this.isResumed()) {
                    InviteFragment.this.mEdtSelected.requestFocus();
                    UIUtil.openSoftKeyboard(InviteFragment.this.getActivity(), InviteFragment.this.mEdtSelected);
                }
            }
        }, 100);
        return view;
    }

    public void onRequestPermissionsResult(final int i, @NonNull final String[] strArr, @NonNull final int[] iArr) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((InviteFragment) iUIElement).handleRequestPermissionResult(i, strArr, iArr);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.READ_CONTACTS".equals(strArr[i2]) && iArr[i2] == 0) {
                    ABContactsCache.getInstance().reloadAllContacts();
                }
            }
        }
    }

    @Nullable
    public ZMTip onCreateTip(@NonNull Context context, LayoutInflater layoutInflater, @Nullable Bundle bundle) {
        View view = getView();
        if (view == null) {
            return null;
        }
        int dip2px = UIUtil.dip2px(context, 400.0f);
        if (UIUtil.getDisplayWidth(context) < dip2px) {
            dip2px = UIUtil.getDisplayWidth(context);
        }
        view.setLayoutParams(new LayoutParams(dip2px, -2));
        ZMTip zMTip = new ZMTip(context);
        zMTip.setBackgroundColor(-263173);
        zMTip.setArrowSize(UIUtil.dip2px(context, 30.0f), UIUtil.dip2px(context, 11.0f));
        int i = 0;
        zMTip.setCornerArcSize(0);
        zMTip.addView(view);
        this.mAnchorId = getArguments().getInt("anchorId", 0);
        if (this.mAnchorId > 0) {
            View findViewById = getActivity().findViewById(this.mAnchorId);
            if (findViewById != null) {
                zMTip.setAnchor(findViewById, 1);
            }
        }
        if (bundle != null) {
            if (!bundle.getBoolean("isTipVisible", true)) {
                i = 4;
            }
            zMTip.setVisibility(i);
        }
        return zMTip;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnInvite) {
            onClickBtnInvite();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.edtSelected) {
            onClickEditSelectedView();
        }
    }

    private void onClickEditSelectedView() {
        this.mEdtSelected.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSelected);
    }

    private void onClickBtnBack() {
        if (getShowsTip()) {
            setTipVisible(false);
        } else {
            dismiss();
        }
    }

    private void onClickBtnInvite() {
        List selectedBuddies = this.mListView.getSelectedBuddies();
        if (selectedBuddies.size() == 0) {
            onClickBtnBack();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(activity, getView());
            String[] strArr = new String[selectedBuddies.size()];
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = ((InviteBuddyItem) selectedBuddies.get(i)).userId;
            }
            if (PTAppDelegation.getInstance().inviteBuddiesToConf(strArr, null, this.mMeetingId, this.mMeetingNumber, activity.getString(C4558R.string.zm_msg_invitation_message_template)) != 0) {
                onSentInvitationFailed();
            } else {
                onSentInvitationDone(selectedBuddies);
            }
        }
    }

    private void onSentInvitationFailed() {
        new InviteFailedDialog().show(getFragmentManager(), InviteFailedDialog.class.getName());
    }

    private void onSentInvitationDone(@NonNull List<InviteBuddyItem> list) {
        if (getShowsTip()) {
            Intent intent = new Intent();
            intent.putExtra("invitations_count", list.size());
            ConfActivity confActivity = (ConfActivity) getActivity();
            if (confActivity != null) {
                confActivity.onSentInvitationDone(intent);
            }
            setTipVisible(false);
            return;
        }
        InviteActivity inviteActivity = (InviteActivity) getActivity();
        if (inviteActivity != null) {
            inviteActivity.onSentInvitationDone(list.size());
        }
    }

    private void clearSelection() {
        this.mListView.clearSelection();
    }

    public void onSelectionChanged() {
        updateButtonInvite(getSelectedBuddiesCount());
    }

    private void updateButtonInvite(int i) {
        if (i <= 0) {
            this.mBtnInvite.setText(getResources().getString(C4558R.string.zm_btn_invite));
            this.mBtnInvite.setEnabled(false);
            return;
        }
        this.mBtnInvite.setText(getResources().getString(C4558R.string.zm_btn_invite));
        this.mBtnInvite.setEnabled(true);
    }

    private int getSelectedBuddiesCount() {
        return this.mListView.getSelectedBuddies().size();
    }

    public void onKeyboardOpen() {
        this.mEdtSelected.setCursorVisible(true);
        if (this.mEdtSelected.hasFocus()) {
            this.mEdtSelected.setCursorVisible(true);
        }
    }

    public void onKeyboardClosed() {
        this.mEdtSelected.setCursorVisible(false);
        this.mListView.setForeground(null);
        this.mHandler.post(new Runnable() {
            public void run() {
                InviteFragment.this.mListView.requestLayout();
            }
        });
    }

    public void onIMBuddyPresence(BuddyItem buddyItem) {
        this.mListView.updateBuddyItem(buddyItem);
    }

    public void onIMBuddyPic(BuddyItem buddyItem) {
        this.mListView.updateBuddyItem(buddyItem);
        getSelectedBuddiesCount();
    }

    public void onIMBuddySort() {
        this.mListView.sort();
        getSelectedBuddiesCount();
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 22) {
            updateUIForConfCallStatus((int) j);
        }
    }

    private void updateUIForConfCallStatus(int i) {
        switch (i) {
            case 0:
            case 1:
                dismiss();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateZoomMessengerBuddyInfoUpdated(String str) {
        InviteBuddyListView inviteBuddyListView = this.mListView;
        if (inviteBuddyListView != null) {
            inviteBuddyListView.notifyDataSetChanged(true);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateZoomMessengerBuddyListUpdated() {
        InviteBuddyListView inviteBuddyListView = this.mListView;
        if (inviteBuddyListView != null) {
            inviteBuddyListView.reloadAllBuddyItems();
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateZoomMessengerGetContactsPresence(List<String> list, List<String> list2) {
        InviteBuddyListView inviteBuddyListView = this.mListView;
        if (inviteBuddyListView != null) {
            inviteBuddyListView.onIndicateZoomMessengerGetContactsPresence(list, list2);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateZoomMessengerOnlineBuddies(List<String> list) {
        InviteBuddyListView inviteBuddyListView = this.mListView;
        if (inviteBuddyListView != null) {
            inviteBuddyListView.onIndicateZoomMessengerOnlineBuddies(list);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateZoomMessengerInfoUpdatedWithJID(@NonNull String str) {
        InviteBuddyListView inviteBuddyListView = this.mListView;
        if (inviteBuddyListView != null) {
            inviteBuddyListView.updateBuddyInfoWithJid(str);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicationZoomMessengerConnectReturn(int i) {
        InviteBuddyListView inviteBuddyListView = this.mListView;
        if (inviteBuddyListView != null) {
            inviteBuddyListView.notifyDataSetChanged(true);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicationZoomMessengerSearchBuddyByKey(String str, int i) {
        if (this.mListView != null) {
            ProgressDialog progressDialog = this.mProgressDialog;
            if (progressDialog != null && progressDialog.isShowing()) {
                this.mProgressDialog.dismiss();
                this.mListView.onIndicationZoomMessengerSearchBuddyByKey(str, i);
            }
        }
    }

    public void dismiss() {
        if (getShowsTip()) {
            super.dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public boolean onSearchRequested() {
        this.mEdtSelected.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSelected);
        return true;
    }

    private boolean isTipVisible() {
        ZMTip tip = getTip();
        boolean z = false;
        if (tip == null) {
            return false;
        }
        if (tip.getVisibility() == 0) {
            z = true;
        }
        return z;
    }

    private void setTipVisible(boolean z) {
        ZMTip tip = getTip();
        if (tip != null) {
            int i = 0;
            if ((tip.getVisibility() == 0) != z) {
                if (!z) {
                    i = 4;
                }
                tip.setVisibility(i);
                if (z) {
                    clearSelection();
                    tip.startAnimation(AnimationUtils.loadAnimation(getActivity(), C4558R.anim.zm_tip_fadein));
                    return;
                }
                ConfActivity confActivity = (ConfActivity) getActivity();
                if (confActivity != null) {
                    confActivity.onPListTipClosed();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    @NonNull
    public String getFilter() {
        String str = "";
        Editable text = this.mEdtSelected.getText();
        InviteBuddyItemSpan[] inviteBuddyItemSpanArr = (InviteBuddyItemSpan[]) text.getSpans(0, text.length(), InviteBuddyItemSpan.class);
        if (inviteBuddyItemSpanArr.length <= 0) {
            return text.toString();
        }
        int spanEnd = text.getSpanEnd(inviteBuddyItemSpanArr[inviteBuddyItemSpanArr.length - 1]);
        int length = text.length();
        return spanEnd < length ? text.subSequence(spanEnd, length).toString() : str;
    }

    /* access modifiers changed from: private */
    public void startFilter(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        if (!str.equals(this.mRunnableFilter.getKey())) {
            this.mRunnableFilter.setKey(str);
            this.mHandler.removeCallbacks(this.mRunnableFilter);
            this.mHandler.postDelayed(this.mRunnableFilter, 300);
        }
    }

    private boolean isSameItem(@Nullable InviteBuddyItemSpan inviteBuddyItemSpan, @Nullable InviteBuddyItem inviteBuddyItem) {
        if (inviteBuddyItemSpan == null || inviteBuddyItem == null) {
            return false;
        }
        InviteBuddyItem item = inviteBuddyItemSpan.getItem();
        if (item == null) {
            return false;
        }
        String str = inviteBuddyItem.userId;
        if (str == null || !str.equals(item.userId)) {
            return false;
        }
        return true;
    }

    public void onSelected(boolean z, @Nullable InviteBuddyItem inviteBuddyItem) {
        if (inviteBuddyItem != null) {
            Editable text = this.mEdtSelected.getText();
            InviteBuddyItemSpan[] inviteBuddyItemSpanArr = (InviteBuddyItemSpan[]) text.getSpans(0, text.length(), InviteBuddyItemSpan.class);
            InviteBuddyItemSpan inviteBuddyItemSpan = null;
            int length = inviteBuddyItemSpanArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                InviteBuddyItemSpan inviteBuddyItemSpan2 = inviteBuddyItemSpanArr[i];
                if (isSameItem(inviteBuddyItemSpan2, inviteBuddyItem)) {
                    inviteBuddyItemSpan = inviteBuddyItemSpan2;
                    break;
                }
                i++;
            }
            if (z) {
                if (inviteBuddyItemSpan != null) {
                    inviteBuddyItemSpan.setItem(inviteBuddyItem);
                    return;
                }
                int length2 = inviteBuddyItemSpanArr.length;
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    int groupInviteLimit = zoomMessenger.getGroupInviteLimit();
                    if (groupInviteLimit <= 0 || length2 < groupInviteLimit) {
                        this.mInviteMaxAlertPanel.setVisibility(8);
                    } else {
                        this.mInviteMaxAlertPanel.setVisibility(0);
                        this.mInviteMaxTxt.setText(getString(C4558R.string.zm_mm_information_barries_invite_max_115072, Integer.valueOf(length2)));
                    }
                }
                if (length2 > 0) {
                    int spanEnd = text.getSpanEnd(inviteBuddyItemSpanArr[length2 - 1]);
                    int length3 = text.length();
                    if (spanEnd < length3) {
                        text.delete(spanEnd, length3);
                    }
                } else {
                    text.clear();
                }
                InviteBuddyItemSpan inviteBuddyItemSpan3 = new InviteBuddyItemSpan(getActivity(), inviteBuddyItem);
                inviteBuddyItemSpan3.setInterval(UIUtil.dip2px(getActivity(), 2.0f));
                StringBuilder sb = new StringBuilder();
                sb.append(OAuth.SCOPE_DELIMITER);
                sb.append(inviteBuddyItem.screenName);
                sb.append(OAuth.SCOPE_DELIMITER);
                String sb2 = sb.toString();
                int length4 = text.length();
                int length5 = sb2.length() + length4;
                text.append(sb2);
                text.setSpan(inviteBuddyItemSpan3, length4, length5, 17);
                this.mEdtSelected.setSelection(length5);
                this.mEdtSelected.setCursorVisible(true);
            } else if (inviteBuddyItemSpan != null) {
                int spanStart = text.getSpanStart(inviteBuddyItemSpan);
                int spanEnd2 = text.getSpanEnd(inviteBuddyItemSpan);
                if (spanStart >= 0 && spanEnd2 >= 0 && spanEnd2 >= spanStart) {
                    text.delete(spanStart, spanEnd2);
                    text.removeSpan(inviteBuddyItemSpan);
                }
            }
        }
    }

    public void onViewMoreClick() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.isConnectionGood()) {
            ProgressDialog progressDialog = this.mProgressDialog;
            if (progressDialog != null && progressDialog.isShowing()) {
                this.mProgressDialog.dismiss();
            }
            boolean z = false;
            String str = null;
            if (this.mInviteAddrBook) {
                str = zoomMessenger.searchBuddyByKeyV2(getFilter(), "0,2", true);
            } else {
                z = zoomMessenger.searchBuddyByKey(getFilter());
            }
            if (z || !TextUtils.isEmpty(str)) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    this.mProgressDialog = UIUtil.showSimpleWaitingDialog((Activity) activity, C4558R.string.zm_msg_waiting);
                }
            }
        }
    }

    private int startABMatching() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return 11;
        }
        if (!PTApp.getInstance().isWebSignedOn()) {
            return 9;
        }
        return ContactsMatchHelper.getInstance().matchAllNumbers(activity);
    }

    private void matchNewNumbers() {
        if (PTApp.getInstance().isWebSignedOn()) {
            ContactsMatchHelper.getInstance().matchNewNumbers(getActivity());
        }
    }

    public void onContactsCacheUpdated() {
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            if (PTApp.getInstance().isWebSignedOn() && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()) && ABContactsHelper.isTimeToMatchPhoneNumbers()) {
                startABMatching();
            } else if (!StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber())) {
                matchNewNumbers();
            }
        }
    }
}
