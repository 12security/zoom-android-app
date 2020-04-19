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

public class MergeCallListItem implements IZMSIPListItem, IZMListItemView {

    /* renamed from: id */
    private String f339id;
    @Nullable
    private IMAddrBookItem mAddrBookItem;
    @Nullable
    private String name;
    private String sublabel;

    public boolean isSelected() {
        return false;
    }

    public MergeCallListItem(String str) {
        this.f339id = str;
    }

    public MergeCallListItem(@Nullable String str, String str2) {
        this.name = str;
        this.sublabel = str2;
    }

    public void init(@NonNull Context context) {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(this.f339id);
        if (callItemByCallID != null) {
            this.name = instance.getDisplayName(callItemByCallID);
            this.sublabel = context.getString(C4558R.string.zm_sip_merged_tap_to_end_call_93257, new Object[]{callItemByCallID.getPeerFormatNumber()});
            if (this.mAddrBookItem == null) {
                ZoomBuddy zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getZoomBuddyByNumber(callItemByCallID.getThirdpartyNumber());
                if (zoomBuddyByNumber != null) {
                    this.mAddrBookItem = IMAddrBookItem.fromZoomBuddy(zoomBuddyByNumber);
                }
            }
        }
    }

    public String getId() {
        return this.f339id;
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
    public MergeCallListItemView getView(Context context, int i, View view, ViewGroup viewGroup, IActionClickListener iActionClickListener) {
        MergeCallListItemView mergeCallListItemView;
        if (view instanceof MergeCallListItemView) {
            mergeCallListItemView = (MergeCallListItemView) view;
        } else {
            mergeCallListItemView = new MergeCallListItemView(context);
        }
        mergeCallListItemView.bindView(this, iActionClickListener);
        return mergeCallListItemView;
    }
}
