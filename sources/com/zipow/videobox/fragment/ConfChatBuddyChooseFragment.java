package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleInMeetingActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.IZoomQAUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.SimpleZoomQAUIListener;
import com.zipow.videobox.view.ConfChatAttendeeItem;
import com.zipow.videobox.view.ConfChatBuddyListView;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.videomeetings.C4558R;

public class ConfChatBuddyChooseFragment extends ZMDialogFragment implements OnItemClickListener, OnClickListener {
    public static final String EXTRA_WEBINAR_BUDDY = "EXTRA_WEBINAR_BUDDY";
    private IConfUIListener mConfUIListener;
    private IZoomQAUIListener mQAUIListener;
    private ConfChatBuddyListView mWebinarChatBuddyListView;

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SimpleInMeetingActivity.show(zMActivity, ConfChatBuddyChooseFragment.class.getName(), bundle, i, true, 2);
    }

    public static void showAsActivity(Fragment fragment, int i) {
        Bundle bundle = new Bundle();
        SimpleInMeetingActivity.show(fragment, ConfChatBuddyChooseFragment.class.getName(), bundle, i, true, 2);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_webinar_chat_buddychoose, viewGroup, false);
        this.mWebinarChatBuddyListView = (ConfChatBuddyListView) inflate.findViewById(C4558R.C4560id.webinarChatBuddyListView);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mWebinarChatBuddyListView.setOnItemClickListener(this);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && confContext.isWebinar()) {
            if (this.mQAUIListener == null) {
                this.mQAUIListener = new SimpleZoomQAUIListener() {
                    public void onUserAdded(String str) {
                        ConfChatBuddyChooseFragment.this.onUserAdded(str);
                    }

                    public void onUserRemoved(String str) {
                        ConfChatBuddyChooseFragment.this.onUserRemoved(str);
                    }

                    public void onChattedAttendeeUpdated(long j) {
                        ConfChatBuddyChooseFragment.this.onChattedAttendeeUpdated(j);
                    }

                    public void onWebinarAttendeeGuestStatusChanged(long j, boolean z) {
                        ConfChatBuddyChooseFragment.this.onChattedAttendeeUpdated(j);
                    }
                };
            }
            ZoomQAUI.getInstance().addListener(this.mQAUIListener);
        }
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onUserStatusChanged(int i, long j, int i2) {
                    if (i != 1) {
                        switch (i) {
                            case 44:
                            case 45:
                                break;
                            case 46:
                                ConfChatBuddyChooseFragment.this.userGuestStatusChanged();
                                break;
                        }
                    }
                    ConfChatBuddyChooseFragment.this.processOnHostOrCoHostChanged();
                    return true;
                }

                public boolean onConfStatusChanged2(int i, long j) {
                    if (i == 28) {
                        ConfChatBuddyChooseFragment.this.attendeeChatStatusChange();
                    }
                    return false;
                }

                public boolean onUserEvent(int i, long j, int i2) {
                    return ConfChatBuddyChooseFragment.this.onUserEvent(i, j, i2);
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        this.mWebinarChatBuddyListView.loadAllItems();
    }

    public void onPause() {
        if (this.mQAUIListener != null) {
            ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
        }
        if (this.mConfUIListener != null) {
            ConfUI.getInstance().removeListener(this.mConfUIListener);
        }
        super.onPause();
    }

    public boolean onUserEvent(int i, long j, int i2) {
        return this.mWebinarChatBuddyListView.onUserEvent(i, j, i2);
    }

    public void onUserAdded(String str) {
        this.mWebinarChatBuddyListView.onUserAdded(str);
    }

    public void onUserRemoved(String str) {
        this.mWebinarChatBuddyListView.onUserRemoved(str);
    }

    public void onChattedAttendeeUpdated(long j) {
        this.mWebinarChatBuddyListView.onChattedAttendeeUpdated(j);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = this.mWebinarChatBuddyListView.getItemAtPosition(i);
        if (itemAtPosition instanceof ConfChatAttendeeItem) {
            ConfChatAttendeeItem confChatAttendeeItem = (ConfChatAttendeeItem) itemAtPosition;
            if (confChatAttendeeItem.nodeID != -1) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_WEBINAR_BUDDY, confChatAttendeeItem);
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.setResult(-1, intent);
                    dismiss();
                }
            }
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    public void onClick(@Nullable View view) {
        if (view != null && view.getId() == C4558R.C4560id.btnBack) {
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void attendeeChatStatusChange() {
        this.mWebinarChatBuddyListView.onAttendeeStatusChange();
    }

    /* access modifiers changed from: private */
    public void processOnHostOrCoHostChanged() {
        this.mWebinarChatBuddyListView.onHostOrCoHostChanged();
    }

    /* access modifiers changed from: private */
    public void userGuestStatusChanged() {
        this.mWebinarChatBuddyListView.onUserGuestStatusChanged();
    }
}
