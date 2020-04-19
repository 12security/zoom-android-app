package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZMSortUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class ZoomSipPhoneAdapter extends BaseAdapter {
    private static final int MAX_DISPLAY_NUM = 200;
    private Context mContext;
    private List<IMAddrBookItem> mData;
    @NonNull
    private List<IMAddrBookItem> mDisplayData = new ArrayList();
    private String mFilter;
    @Nullable
    private String mMySelfJID = null;

    public long getItemId(int i) {
        return 0;
    }

    public ZoomSipPhoneAdapter(Context context) {
        this.mContext = context;
        this.mMySelfJID = getMyselfJID();
    }

    private String getMyselfJID() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return null;
        }
        return myself.getJid();
    }

    public int getCount() {
        return this.mDisplayData.size();
    }

    @Nullable
    public IMAddrBookItem getItem(int i) {
        if (i < 0 || i >= this.mDisplayData.size()) {
            return null;
        }
        return (IMAddrBookItem) this.mDisplayData.get(i);
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        return getItem(i).getSipView(this.mContext, view, true, false);
    }

    public void setData(List<IMAddrBookItem> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public void filter(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mFilter = str.toLowerCase();
        } else {
            this.mFilter = "";
        }
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        updateDisplayData();
        super.notifyDataSetChanged();
    }

    private void updateDisplayData() {
        this.mDisplayData.clear();
        HashMap hashMap = new HashMap();
        if (!StringUtil.isEmptyOrNull(this.mFilter) && !CollectionsUtil.isListEmpty(this.mData)) {
            for (IMAddrBookItem iMAddrBookItem : this.mData) {
                if (hashMap.size() >= 200) {
                    break;
                }
                String screenName = iMAddrBookItem.getScreenName();
                if (StringUtil.isEmptyOrNull(screenName) || !screenName.toLowerCase().contains(this.mFilter)) {
                    String sipPhoneNumber = iMAddrBookItem.getSipPhoneNumber();
                    if (!StringUtil.isEmptyOrNull(sipPhoneNumber) && sipPhoneNumber.toLowerCase().contains(this.mFilter)) {
                        hashMap.put(iMAddrBookItem.getJid(), iMAddrBookItem);
                    }
                } else {
                    hashMap.put(iMAddrBookItem.getJid(), iMAddrBookItem);
                }
            }
        }
        hashMap.remove(this.mMySelfJID);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(hashMap.keySet());
        List<String> sortBuddies = ZMSortUtil.sortBuddies(arrayList, 0, this.mFilter);
        if (sortBuddies != null) {
            for (String str : sortBuddies) {
                this.mDisplayData.add(hashMap.get(str));
            }
        }
    }
}
