package com.zipow.videobox.view.sip.sms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.ZMPhoneUtils;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;
import p021us.zoom.videomeetings.C4558R;

public class PBXMessageSessionInfoFragment extends ZMDialogFragment implements OnClickListener {
    private static final String ARG_LIST = "ARG_LIST";
    /* access modifiers changed from: private */
    public PBXMessageSessionMemberAdapter mAdapter;
    private Button mBackBtn;
    private RecyclerView mMembersRecyclerView;
    private SimpleSIPCallEventListener mSIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
            super.OnPBXFeatureOptionsChanged(list);
            if (list != null && list.size() != 0 && ZMPhoneUtils.isPBXFeatureOptionChanged(list, CmmSIPMessageManager.getInstance().getMessageEnabledBit()) && !CmmSIPMessageManager.getInstance().isMessageEnabled()) {
                PBXMessageSessionInfoFragment.this.finishFragment(true);
            }
        }
    };

    public static void show(@Nullable Fragment fragment, @NonNull ArrayList<PBXMessageContact> arrayList) {
        if (fragment != null && arrayList.size() != 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_LIST, arrayList);
            SimpleActivity.show(fragment, PBXMessageSessionInfoFragment.class.getName(), bundle, 0, false, 1);
        }
    }

    public void onClick(View view) {
        if (view == this.mBackBtn) {
            finishFragment(true);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_pbx_sms_conversation_info, viewGroup, false);
        this.mBackBtn = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mMembersRecyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.rv_members);
        this.mBackBtn.setOnClickListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            ArrayList arrayList = (ArrayList) arguments.getSerializable(ARG_LIST);
            if (!CollectionsUtil.isListEmpty(arrayList)) {
                this.mMembersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                this.mAdapter = new PBXMessageSessionMemberAdapter(getContext(), arrayList, new OnRecyclerViewListener() {
                    public boolean onItemLongClick(View view, int i) {
                        return false;
                    }

                    public void onItemClick(View view, int i) {
                        if (i != 0) {
                            PBXMessageContact pBXMessageContact = (PBXMessageContact) PBXMessageSessionInfoFragment.this.mAdapter.getItem(i);
                            if (!(pBXMessageContact == null || pBXMessageContact.getItem() == null)) {
                                AddrBookItemDetailsActivity.show((Fragment) PBXMessageSessionInfoFragment.this, pBXMessageContact.getItem(), 106);
                            }
                        }
                    }
                });
                this.mMembersRecyclerView.setAdapter(this.mAdapter);
            }
        }
        return inflate;
    }

    public void onStart() {
        super.onStart();
        CmmSIPCallManager.getInstance().addListener(this.mSIPCallEventListener);
    }

    public void onStop() {
        super.onStop();
        CmmSIPCallManager.getInstance().removeListener(this.mSIPCallEventListener);
    }
}
