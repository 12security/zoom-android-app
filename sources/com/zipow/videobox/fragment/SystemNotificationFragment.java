package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.SystemNotificationActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.view.p014mm.MMSystemNotificationListView;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.videomeetings.C4558R;

public class SystemNotificationFragment extends ZMDialogFragment implements OnClickListener {
    private MMSystemNotificationListView mBuddyInviteListView;
    private IZoomMessengerUIListener mZoomMessengerUIListener;

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SystemNotificationActivity.show(zMActivity, SystemNotificationFragment.class.getName(), bundle, i, true, 1);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_system_notification, viewGroup, false);
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        this.mBuddyInviteListView = (MMSystemNotificationListView) inflate.findViewById(C4558R.C4560id.systemNotificationListView);
        this.mBuddyInviteListView.setEmptyView(inflate.findViewById(C4558R.C4560id.panelNoItemMsg));
        this.mBuddyInviteListView.setParentFragment(this);
        return inflate;
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnCancel) {
            dismiss();
        }
    }

    public void onPause() {
        super.onPause();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void onResume() {
        super.onResume();
        if (this.mZoomMessengerUIListener == null) {
            this.mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
                public boolean onNotifySubscribeRequest(String str, String str2) {
                    SystemNotificationFragment.this.onZoomMessengerNotifySubscribeRequest();
                    return true;
                }

                public void onIndicateInfoUpdatedWithJID(String str) {
                    SystemNotificationFragment.this.onIndicateInfoUpdatedWithJID(str);
                }

                public boolean onNotifySubscriptionAccepted(String str) {
                    SystemNotificationFragment.this.onZoomMessengerNotifySubscribeRequest();
                    return true;
                }

                public void onIndicateBuddyListUpdated() {
                    SystemNotificationFragment.this.onIndicateBuddyListUpdated();
                }

                public boolean onNotifySubscriptionDenied(String str) {
                    SystemNotificationFragment.this.onZoomMessengerNotifySubscribeRequest();
                    return true;
                }

                public void onNotifySubscribeRequestUpdated(String str) {
                    SystemNotificationFragment.this.onNotifySubscribeRequestUpdated(str);
                }

                public void Indicate_BuddyAdded(String str, List<String> list) {
                    SystemNotificationFragment.this.onZoomMessengerNotifySubscribeRequest();
                }
            };
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (zoomMessenger.getUnreadRequestCount() > 0) {
                NotificationMgr.removeMessageNotificationMM(getActivity(), zoomMessenger.getContactRequestsSessionID());
            }
            this.mBuddyInviteListView.reloadAllItems();
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyListUpdated() {
        this.mBuddyInviteListView.onIndicateBuddyListUpdated();
    }

    /* access modifiers changed from: private */
    public void onIndicateInfoUpdatedWithJID(String str) {
        this.mBuddyInviteListView.onIndicateInfoUpdatedWithJID(str);
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerNotifySubscribeRequest() {
        this.mBuddyInviteListView.onZoomMessengerNotifySubscribeRequest();
    }

    /* access modifiers changed from: private */
    public void onNotifySubscribeRequestUpdated(String str) {
        this.mBuddyInviteListView.onNotifySubscribeRequestUpdated(str);
    }

    public void dismiss() {
        finishFragment(true);
    }
}
