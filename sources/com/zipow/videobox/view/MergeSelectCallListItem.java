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

public class MergeSelectCallListItem implements IZMSIPListItem, IZMListItemView {

    /* renamed from: id */
    private String f340id;
    @Nullable
    private IMAddrBookItem mAddrBookItem;
    @Nullable
    private String name;
    private String sublabel;

    public boolean isSelected() {
        return false;
    }

    public MergeSelectCallListItem(String str) {
        this.f340id = str;
    }

    public void init(@NonNull Context context) {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(this.f340id);
        this.name = instance.getSipCallDisplayName(callItemByCallID);
        this.sublabel = context.getString(C4558R.string.zm_sip_call_on_hold_tap_to_merge_68975);
        if (callItemByCallID != null && this.mAddrBookItem == null) {
            ZoomBuddy zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getZoomBuddyByNumber(callItemByCallID.getThirdpartyNumber());
            if (zoomBuddyByNumber != null) {
                this.mAddrBookItem = IMAddrBookItem.fromZoomBuddy(zoomBuddyByNumber);
            }
        }
    }

    public String getId() {
        return this.f340id;
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
    public MergeSelectCallListItemView getView(Context context, int i, View view, ViewGroup viewGroup, IActionClickListener iActionClickListener) {
        MergeSelectCallListItemView mergeSelectCallListItemView;
        if (view instanceof MergeSelectCallListItemView) {
            mergeSelectCallListItemView = (MergeSelectCallListItemView) view;
        } else {
            mergeSelectCallListItemView = new MergeSelectCallListItemView(context);
        }
        mergeSelectCallListItemView.bindView(this, iActionClickListener);
        return mergeSelectCallListItemView;
    }
}
