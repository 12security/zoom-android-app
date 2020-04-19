package com.zipow.videobox.view.sip.sms;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.IPBXMessageAPI;
import com.zipow.videobox.sip.server.IPBXMessageSession;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.PinnedSectionRecyclerView;

public class PhonePBXMessageSessionRecyclerView extends PinnedSectionRecyclerView {
    public static final int PAGE_SIZE = 50;
    private static final String TAG = "PhonePBXMessageSessionRecyclerView";
    private PhonePBXMessageSessionAdapter mAdapter;

    public PhonePBXMessageSessionRecyclerView(Context context) {
        super(context);
        initView();
    }

    public PhonePBXMessageSessionRecyclerView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private void initView() {
        this.mAdapter = new PhonePBXMessageSessionAdapter(getContext());
        setAdapter(this.mAdapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
        initData();
    }

    public void initData() {
        CmmSIPMessageManager instance = CmmSIPMessageManager.getInstance();
        ArrayList arrayList = new ArrayList();
        List<String> allLocalSessionId = instance.getAllLocalSessionId();
        if (!CollectionsUtil.isListEmpty(allLocalSessionId)) {
            IPBXMessageAPI messageAPI = CmmSIPMessageManager.getInstance().getMessageAPI();
            if (messageAPI != null) {
                for (String fromMessageSession : allLocalSessionId) {
                    arrayList.add(IPBXMessageSessionItem.fromMessageSession(fromMessageSession, messageAPI));
                }
            }
        }
        int min = Math.min(instance.getCountOfSession(), 50);
        if (min > 0) {
            for (int i = 0; i < min; i++) {
                IPBXMessageSession sessionByIndex = instance.getSessionByIndex(i);
                if (sessionByIndex != null) {
                    arrayList.add(IPBXMessageSessionItem.fromMessageSession(sessionByIndex));
                }
            }
        }
        if (arrayList.size() > 0) {
            this.mAdapter.setData(arrayList);
        }
    }

    public boolean loadMore() {
        if (this.mAdapter.getItemCount() <= 0) {
            return false;
        }
        CmmSIPMessageManager instance = CmmSIPMessageManager.getInstance();
        if (instance.getCountOfSession() <= this.mAdapter.getItemCount()) {
            return false;
        }
        PhonePBXMessageSessionAdapter phonePBXMessageSessionAdapter = this.mAdapter;
        IPBXMessageSessionItem iPBXMessageSessionItem = (IPBXMessageSessionItem) phonePBXMessageSessionAdapter.getItem(phonePBXMessageSessionAdapter.getItemCount() - 1);
        if (iPBXMessageSessionItem == null || TextUtils.isEmpty(iPBXMessageSessionItem.getID())) {
            return false;
        }
        int nextPageSessions = instance.getNextPageSessions(iPBXMessageSessionItem.getID(), 50);
        int min = Math.min(instance.getCountOfSession() - nextPageSessions, 50);
        ArrayList arrayList = new ArrayList();
        for (int i = nextPageSessions; i < nextPageSessions + min; i++) {
            IPBXMessageSession sessionByIndex = instance.getSessionByIndex(i);
            if (sessionByIndex != null) {
                arrayList.add(IPBXMessageSessionItem.fromMessageSession(sessionByIndex));
            }
        }
        this.mAdapter.addAll(arrayList);
        return true;
    }

    public int getCount() {
        return this.mAdapter.getItemCount();
    }

    public void syncSessions(List<String> list, List<String> list2, List<String> list3) {
        this.mAdapter.syncSessions(list, list2, list3);
    }

    public void addNewSession(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mAdapter.addNewSession(str);
        }
    }

    public void addNewLocalSession(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mAdapter.addNewLocalSession(str);
        }
    }

    public void updateSession(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mAdapter.updateSession(str);
        }
    }

    public void updateLocalSession(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mAdapter.updateLocalSession(str);
        }
    }

    public void deleteSession(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mAdapter.deleteSession(str);
        }
    }

    public IPBXMessageSessionItem getItem(int i) {
        return (IPBXMessageSessionItem) this.mAdapter.getItem(i);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.mAdapter.setOnRecyclerViewListener(onRecyclerViewListener);
    }

    public void updateContactNames() {
        this.mAdapter.updateContactNames();
    }
}
