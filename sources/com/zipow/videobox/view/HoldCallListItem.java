package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.view.IZMListItemView.IActionClickListener;
import p021us.zoom.androidlib.widget.IZMSIPListItem;
import p021us.zoom.videomeetings.C4558R;

public class HoldCallListItem implements IZMSIPListItem, IZMListItemView {

    /* renamed from: id */
    private String f338id;
    @Nullable
    private IMAddrBookItem mAddrBookItem;
    @Nullable
    private String name;
    private boolean showAction;
    private String sublabel;

    public boolean isSelected() {
        return false;
    }

    public HoldCallListItem(String str) {
        this.f338id = str;
    }

    public void setShowAction(boolean z) {
        this.showAction = z;
    }

    public boolean isShowAction() {
        return this.showAction;
    }

    public void init(@NonNull Context context) {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(this.f338id);
        this.name = instance.getSipCallDisplayName(callItemByCallID);
        if (instance.isInJoinMeeingRequest(this.f338id)) {
            this.sublabel = context.getString(C4558R.string.zm_sip_tap_to_join_meeting_53992);
        } else {
            int thirdpartyType = callItemByCallID != null ? callItemByCallID.getThirdpartyType() : 0;
            if (callItemByCallID == null || thirdpartyType == 0) {
                this.sublabel = context.getString(C4558R.string.zm_sip_on_hold_to_tap_61381);
            } else if (thirdpartyType == 1 || thirdpartyType == 3) {
                this.sublabel = context.getString(C4558R.string.zm_sip_call_assistant_61383, new Object[]{context.getString(C4558R.string.zm_sip_call_on_hold_61381), callItemByCallID.getThirdpartyName()});
            } else if (thirdpartyType == 2) {
                this.sublabel = context.getString(C4558R.string.zm_sip_call_queue_61383, new Object[]{context.getString(C4558R.string.zm_sip_call_on_hold_61381), callItemByCallID.getThirdpartyName()});
            } else if (thirdpartyType == 4) {
                this.sublabel = context.getString(C4558R.string.zm_sip_call_transfer_61383, new Object[]{context.getString(C4558R.string.zm_sip_call_on_hold_61381), callItemByCallID.getThirdpartyName()});
            } else if (thirdpartyType == 6) {
                this.sublabel = context.getString(C4558R.string.zm_sip_forward_from_128889, new Object[]{context.getString(C4558R.string.zm_sip_call_on_hold_61381), callItemByCallID.getThirdpartyName()});
            }
        }
        if (callItemByCallID != null && this.mAddrBookItem == null) {
            ZoomBuddy zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getZoomBuddyByNumber(callItemByCallID.getThirdpartyNumber());
            if (zoomBuddyByNumber != null) {
                this.mAddrBookItem = IMAddrBookItem.fromZoomBuddy(zoomBuddyByNumber);
            }
        }
        setShowAction(true);
    }

    public String getId() {
        return this.f338id;
    }

    @Nullable
    public String getLabel() {
        return this.name;
    }

    public String getSubLabel() {
        return this.sublabel;
    }

    @Nullable
    public IMAddrBookItem getAddrBookItem() {
        return this.mAddrBookItem;
    }

    @Nullable
    public HoldCallListItemView getView(Context context, int i, @Nullable View view, ViewGroup viewGroup, IActionClickListener iActionClickListener) {
        HoldCallListItemView holdCallListItemView;
        if (view == null) {
            new HoldCallListItemView(context);
        }
        if (view instanceof HoldCallListItemView) {
            holdCallListItemView = (HoldCallListItemView) view;
        } else {
            holdCallListItemView = new HoldCallListItemView(context);
        }
        holdCallListItemView.bindView(this, iActionClickListener);
        return holdCallListItemView;
    }
}
