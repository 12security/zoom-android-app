package com.zipow.videobox.view.sip;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.view.sip.SipInCallPanelView.OnInCallPanelListener;
import com.zipow.videobox.view.sip.SipInCallPanelView.PanelButtonItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SipMoreActionFragment extends ZMDialogFragment {
    /* access modifiers changed from: private */
    public ArrayList<Integer> mActionList;
    private String mCallId;

    private static class MoreActionAdapter extends BaseAdapter {
        private List<PanelButtonItem> actionList;
        private LayoutInflater mInflater;

        private class ViewHolder {
            /* access modifiers changed from: private */
            public ImageView imgView;
            /* access modifiers changed from: private */
            public TextView txtView;

            private ViewHolder() {
            }
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public MoreActionAdapter(Context context, List<PanelButtonItem> list) {
            this.mInflater = LayoutInflater.from(context);
            this.actionList = list;
            if (this.actionList == null) {
                this.actionList = new ArrayList(3);
            }
        }

        public int getCount() {
            List<PanelButtonItem> list = this.actionList;
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        public Object getItem(int i) {
            List<PanelButtonItem> list = this.actionList;
            if (list != null && i >= 0 && i <= list.size()) {
                return this.actionList.get(i);
            }
            return null;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.mInflater.inflate(C4558R.layout.zm_sip_more_action_item, null);
                viewHolder = new ViewHolder();
                viewHolder.imgView = (ImageView) view.findViewById(C4558R.C4560id.f528iv);
                viewHolder.txtView = (TextView) view.findViewById(C4558R.C4560id.txt);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            PanelButtonItem panelButtonItem = (PanelButtonItem) this.actionList.get(i);
            viewHolder.txtView.setText(panelButtonItem.getLabel());
            viewHolder.imgView.setImageDrawable(panelButtonItem.getIcon());
            viewHolder.txtView.setEnabled(!panelButtonItem.isDisable());
            viewHolder.imgView.setEnabled(!panelButtonItem.isDisable());
            return view;
        }
    }

    public static void show(ZMActivity zMActivity, @Nullable String str, @Nullable ArrayList<Integer> arrayList) {
        if (zMActivity != null && !TextUtils.isEmpty(str) && arrayList != null) {
            String name = SipMoreActionFragment.class.getName();
            if (zMActivity.getSupportFragmentManager().findFragmentByTag(name) == null) {
                Bundle bundle = new Bundle();
                bundle.putString("callId", str);
                bundle.putIntegerArrayList("actionList", arrayList);
                SipMoreActionFragment sipMoreActionFragment = new SipMoreActionFragment();
                sipMoreActionFragment.setArguments(bundle);
                zMActivity.getSupportFragmentManager().beginTransaction().add((Fragment) sipMoreActionFragment, name).commitAllowingStateLoss();
            }
        }
    }

    public static void dismiss(ZMActivity zMActivity) {
        if (zMActivity != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(SipMoreActionFragment.class.getName());
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mCallId = getArguments().getString("callId");
            this.mActionList = getArguments().getIntegerArrayList("actionList");
        }
        if (this.mActionList == null) {
            finishFragment(false);
        } else if (CmmSIPCallManager.getInstance().getCallItemByCallID(this.mCallId) == null) {
            finishFragment(false);
        }
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        boolean z;
        String str;
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem callItemByCallID = CmmSIPCallManager.getInstance().getCallItemByCallID(this.mCallId);
        if (callItemByCallID != null) {
            str = callItemByCallID.getLineId();
            z = callItemByCallID.isInConference();
        } else {
            str = null;
            z = false;
        }
        boolean z2 = (instance.isInCall(callItemByCallID) || instance.isInRemoteHold(callItemByCallID) || instance.isInLocalHold(callItemByCallID) || instance.isAccepted(callItemByCallID)) && ((TextUtils.isEmpty(str) ? instance.isSipRegistered() : CmmSIPLineManager.getInstance().isLineRegistered(str)) && NetworkUtil.hasDataNetwork(getContext()));
        final ArrayList arrayList = new ArrayList(this.mActionList.size());
        Iterator it = this.mActionList.iterator();
        while (it.hasNext()) {
            Integer num = (Integer) it.next();
            PanelButtonItem generateMoreButtonItem = SipInCallPanelView.generateMoreButtonItem(getActivity(), num.intValue());
            if (generateMoreButtonItem != null) {
                if (num.intValue() == 10) {
                    generateMoreButtonItem.setEnable(z2 && !instance.isTransferring(callItemByCallID) && !z && !instance.isInSwitchingToCarrier(this.mCallId) && instance.isEnableHasCallingPlan());
                }
                arrayList.add(generateMoreButtonItem);
            }
        }
        return new Builder(getActivity()).setAdapter(new MoreActionAdapter(getActivity(), arrayList), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i >= 0 && i < SipMoreActionFragment.this.mActionList.size()) {
                    PanelButtonItem panelButtonItem = (PanelButtonItem) arrayList.get(i);
                    if ((panelButtonItem.isClickableInDisabled() || !panelButtonItem.isDisable()) && (SipMoreActionFragment.this.getActivity() instanceof OnInCallPanelListener)) {
                        ((OnInCallPanelListener) SipMoreActionFragment.this.getActivity()).onPanelItemClick(panelButtonItem.getAction());
                    }
                }
            }
        }).setTitle(C4558R.string.zm_pbx_action_more_102668).create();
    }
}
