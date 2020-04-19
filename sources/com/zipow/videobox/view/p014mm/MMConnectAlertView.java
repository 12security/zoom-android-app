package com.zipow.videobox.view.p014mm;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.ptapp.AutoStreamConflictChecker;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMActivity.GlobalActivityListener;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMConnectAlertView */
public class MMConnectAlertView extends LinearLayout implements OnClickListener {
    private static final String TAG = "MMConnectAlertView";
    private int mActionType = 0;
    @NonNull
    private GlobalActivityListener mGlobalActivityListener = new GlobalActivityListener() {
        public void onUIMoveToBackground() {
        }

        public void onUserActivityOnUI() {
        }

        public void onActivityMoveToFront(ZMActivity zMActivity) {
            MMConnectAlertView.this.checkConnectStatus();
        }
    };
    @NonNull
    private IPTUIListener mPtListener = new SimplePTUIListener() {
        public void onDataNetworkStatusChanged(boolean z) {
            MMConnectAlertView.this.onDataNetworkStatusChanged(z);
        }

        public void onPTAppEvent(int i, long j) {
            MMConnectAlertView.this.onPTAppEvent(i, j);
        }
    };
    private TextView mTxtNetworkAlert;
    @NonNull
    private IZoomMessengerUIListener messengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onBeginConnect() {
            MMConnectAlertView.this.onBeginConnect();
        }

        public void onConnectReturn(int i) {
            MMConnectAlertView.this.checkConnectStatus();
        }
    };

    /* renamed from: com.zipow.videobox.view.mm.MMConnectAlertView$ActionType */
    public interface ActionType {

        /* renamed from: IM */
        public static final int f343IM = 0;
        public static final int SIP = 1;
    }

    public MMConnectAlertView(Context context) {
        super(context);
        init();
    }

    public MMConnectAlertView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMConnectAlertView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    @TargetApi(21)
    public MMConnectAlertView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    private void init() {
        View.inflate(getContext(), C4558R.layout.zm_connect_alert, this);
        this.mTxtNetworkAlert = (TextView) findViewById(C4558R.C4560id.txtNetworkAlert);
        setOnClickListener(this);
        setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            checkConnectStatus();
        }
    }

    /* access modifiers changed from: private */
    public void onDataNetworkStatusChanged(boolean z) {
        checkConnectStatus();
    }

    /* access modifiers changed from: private */
    public void onBeginConnect() {
        setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void checkConnectStatus() {
        Context context = getContext();
        if (context != null) {
            if (!PTApp.getInstance().hasZoomMessenger()) {
                setVisibility(8);
                return;
            }
            if (PTApp.getInstance().isWebSignedOn()) {
                if (!NetworkUtil.hasDataNetwork(context)) {
                    if (this.mActionType == 1) {
                        setVisibility(8);
                    } else {
                        setVisibility(0);
                    }
                    this.mTxtNetworkAlert.setText(C4558R.string.zm_mm_msg_network_unavailable);
                } else if (ZoomMessengerUI.getInstance().getConnectionStatus() != 0) {
                    setVisibility(8);
                } else {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null && zoomMessenger.isStreamConflict()) {
                        setVisibility(0);
                        this.mTxtNetworkAlert.setText(C4558R.string.zm_mm_msg_stream_conflict);
                    }
                }
            } else if (PTApp.getInstance().isAuthenticating()) {
                setVisibility(8);
            } else {
                setVisibility(0);
                this.mTxtNetworkAlert.setText(C4558R.string.zm_mm_msg_service_unavailable_77078);
            }
        }
    }

    public void setActionType(int i) {
        this.mActionType = i;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ZoomMessengerUI.getInstance().addListener(this.messengerUIListener);
        PTUI.getInstance().addPTUIListener(this.mPtListener);
        ZMActivity.addGlobalActivityListener(this.mGlobalActivityListener);
        checkConnectStatus();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        ZoomMessengerUI.getInstance().removeListener(this.messengerUIListener);
        PTUI.getInstance().removePTUIListener(this.mPtListener);
        ZMActivity.removeGlobalActivityListener(this.mGlobalActivityListener);
        super.onDetachedFromWindow();
    }

    public void onClick(View view) {
        Context context = getContext();
        if (context != null) {
            if (!PTApp.getInstance().isWebSignedOn()) {
                PTApp.getInstance().autoSignin();
                setVisibility(8);
            } else if (context instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                if (!NetworkUtil.hasDataNetwork(context)) {
                    Toast.makeText(context, C4558R.string.zm_alert_network_disconnected, 1).show();
                    return;
                }
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (!zoomMessenger.isStreamConflict()) {
                        zoomMessenger.trySignon();
                    } else if (zoomMessenger.getStreamConflictReason() == 1) {
                        zoomMessenger.forceSignon();
                        setVisibility(8);
                    } else {
                        AutoStreamConflictChecker.getInstance().showStreamConflictMessage(fragmentActivity);
                    }
                }
            }
        }
    }
}
