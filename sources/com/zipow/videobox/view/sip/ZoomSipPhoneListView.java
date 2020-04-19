package com.zipow.videobox.view.sip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

public class ZoomSipPhoneListView extends ListView implements OnItemClickListener {
    private ZoomSipPhoneSelectListener mSelectListener;
    private ZoomSipPhoneAdapter mZoomSipPhoneAdapter;

    public ZoomSipPhoneListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public ZoomSipPhoneListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ZoomSipPhoneListView(Context context) {
        super(context);
        init();
    }

    public void setSelectListener(ZoomSipPhoneSelectListener zoomSipPhoneSelectListener) {
        this.mSelectListener = zoomSipPhoneSelectListener;
    }

    public void reloadAllData() {
        this.mZoomSipPhoneAdapter.setData(ZMBuddySyncInstance.getInsatance().getAllSipBuddies());
        if (isShown()) {
            this.mZoomSipPhoneAdapter.notifyDataSetChanged();
        }
    }

    public void onBuddyInfoUpdate(List<String> list) {
        if (isShown()) {
            this.mZoomSipPhoneAdapter.notifyDataSetChanged();
        }
    }

    private void init() {
        this.mZoomSipPhoneAdapter = new ZoomSipPhoneAdapter(getContext());
        this.mZoomSipPhoneAdapter.setData(ZMBuddySyncInstance.getInsatance().getAllSipBuddies());
        setAdapter(this.mZoomSipPhoneAdapter);
        setOnItemClickListener(this);
    }

    public void filter(@Nullable String str) {
        this.mZoomSipPhoneAdapter.filter(str);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        IMAddrBookItem item = this.mZoomSipPhoneAdapter.getItem(i);
        if (item != null && !StringUtil.isEmptyOrNull(item.getSipPhoneNumber())) {
            ZoomSipPhoneSelectListener zoomSipPhoneSelectListener = this.mSelectListener;
            if (zoomSipPhoneSelectListener != null) {
                zoomSipPhoneSelectListener.onSelected(item);
            }
        }
    }
}
