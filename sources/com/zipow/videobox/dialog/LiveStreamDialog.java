package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.NormalMessageTip;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHItemClickListener;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHItemClickListener.OnItemClickListener;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHItemDividerDecoration;
import p021us.zoom.videomeetings.C4558R;

public class LiveStreamDialog extends ZMDialogFragment {
    @Nullable
    MyAdapter myAdapter;

    static class LiveStreamItem {
        final LiveStreamItemType liveStreamItemType;
        final int stringRes;

        public LiveStreamItem(LiveStreamItemType liveStreamItemType2, int i) {
            this.liveStreamItemType = liveStreamItemType2;
            this.stringRes = i;
        }
    }

    enum LiveStreamItemType {
        CopyLink,
        StopLiveStream
    }

    static class MyAdapter extends Adapter<MyViewHolder> {
        private final List<LiveStreamItem> mLiveStreamItems;

        class MyViewHolder extends ViewHolder {
            final TextView txtDialogItem;

            public MyViewHolder(@NonNull View view) {
                super(view);
                this.txtDialogItem = (TextView) view.findViewById(C4558R.C4560id.txtDialogItem);
            }

            public void bind(int i) {
                this.txtDialogItem.setText(i);
            }
        }

        MyAdapter(List<LiveStreamItem> list) {
            this.mLiveStreamItems = list;
        }

        @Nullable
        public LiveStreamItem getItem(int i) {
            if (i < getItemCount()) {
                return (LiveStreamItem) this.mLiveStreamItems.get(i);
            }
            return null;
        }

        public void refresh(@NonNull List<LiveStreamItem> list) {
            if (list.size() != this.mLiveStreamItems.size()) {
                this.mLiveStreamItems.clear();
                this.mLiveStreamItems.addAll(list);
                notifyDataSetChanged();
            }
        }

        @NonNull
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_item_dialog_livestream, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.bind(((LiveStreamItem) this.mLiveStreamItems.get(i)).stringRes);
        }

        public int getItemCount() {
            if (CollectionsUtil.isCollectionEmpty(this.mLiveStreamItems)) {
                return 0;
            }
            return this.mLiveStreamItems.size();
        }
    }

    public LiveStreamDialog() {
        setCancelable(true);
    }

    public static void show(@Nullable ConfActivity confActivity) {
        if (confActivity != null && confActivity.isActive() && shouldShow(confActivity.getSupportFragmentManager(), LiveStreamDialog.class.getName(), null)) {
            LiveStreamDialog liveStreamDialog = new LiveStreamDialog();
            if (!CollectionsUtil.isCollectionEmpty(liveStreamDialog.getLiveStreamItems(confActivity))) {
                liveStreamDialog.show(confActivity.getSupportFragmentManager(), LiveStreamDialog.class.getName());
            }
        }
    }

    public static void refresh(@Nullable ConfActivity confActivity) {
        if (confActivity != null && confActivity.isActive()) {
            LiveStreamDialog liveStreamDialog = (LiveStreamDialog) confActivity.getSupportFragmentManager().findFragmentByTag(LiveStreamDialog.class.getName());
            if (liveStreamDialog != null) {
                liveStreamDialog.update(confActivity);
            }
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        View createContent = createContent();
        if (createContent == null) {
            return createEmptyDialog();
        }
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setTheme(C4558R.style.ZMDialog_Material).setView(createContent, true).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    private void update(ConfActivity confActivity) {
        List liveStreamItems = getLiveStreamItems(confActivity);
        if (!CollectionsUtil.isCollectionEmpty(liveStreamItems)) {
            MyAdapter myAdapter2 = this.myAdapter;
            if (myAdapter2 != null) {
                myAdapter2.refresh(liveStreamItems);
            }
        }
    }

    private View createContent() {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity == null) {
            return null;
        }
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material);
        View inflate = View.inflate(contextThemeWrapper, C4558R.layout.zm_recyclerview_dialog, null);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(contextThemeWrapper));
        List liveStreamItems = getLiveStreamItems(confActivity);
        if (CollectionsUtil.isCollectionEmpty(liveStreamItems)) {
            return null;
        }
        this.myAdapter = new MyAdapter(liveStreamItems);
        recyclerView.setAdapter(this.myAdapter);
        recyclerView.addItemDecoration(new RVHItemDividerDecoration(contextThemeWrapper, 1, C4558R.C4559drawable.zm_list_divider));
        recyclerView.addOnItemTouchListener(new RVHItemClickListener(contextThemeWrapper, new OnItemClickListener() {
            public void onItemClick(View view, int i) {
                LiveStreamItem item = LiveStreamDialog.this.myAdapter.getItem(i);
                if (item != null) {
                    if (item.liveStreamItemType == LiveStreamItemType.StopLiveStream) {
                        LiveStreamDialog.this.showLiveStreamStopDialog();
                    } else {
                        LiveStreamDialog.this.copyLink();
                    }
                }
                LiveStreamDialog.this.dismissAllowingStateLoss();
            }
        }));
        return inflate;
    }

    @Nullable
    private List<LiveStreamItem> getLiveStreamItems(@Nullable ConfActivity confActivity) {
        if (confActivity == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null && (myself.isHost() || myself.isCoHost())) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isLiveOn()) {
                arrayList.add(new LiveStreamItem(LiveStreamItemType.StopLiveStream, C4558R.string.zm_btn_stop_streaming));
            }
        }
        if (!StringUtil.isEmptyOrNull(ConfLocalHelper.getLiveChannelStreamUrl())) {
            arrayList.add(new LiveStreamItem(LiveStreamItemType.CopyLink, C4558R.string.zm_live_stream_copy_link_30168));
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void showLiveStreamStopDialog() {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null && (myself.isHost() || myself.isCoHost())) {
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj != null && confStatusObj.isLiveOn()) {
                    new Builder(confActivity).setTitle((CharSequence) confActivity.getString(ConfLocalHelper.isWebinar() ? C4558R.string.zm_lbl_meeting_on_live : C4558R.string.zm_lbl_meeting_on_live_26196, new Object[]{ConfLocalHelper.getLiveChannelStreamName()})).setPositiveButton(C4558R.string.zm_btn_stop_streaming, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LiveStreamDialog.this.stopLiveStream();
                        }
                    }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).create().show();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void stopLiveStream() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isLiveOn()) {
            confStatusObj.stopLive();
        }
    }

    /* access modifiers changed from: private */
    public void copyLink() {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            String liveChannelStreamUrl = ConfLocalHelper.getLiveChannelStreamUrl();
            if (!StringUtil.isEmptyOrNull(liveChannelStreamUrl)) {
                ClipboardManager clipboardManager = (ClipboardManager) confActivity.getSystemService("clipboard");
                if (clipboardManager != null) {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("label", liveChannelStreamUrl));
                    NormalMessageTip.show((Context) confActivity, confActivity.getSupportFragmentManager(), TipMessageType.TIP_COPIED_STREAMING_LINK.name(), -1, C4558R.string.zm_live_stream_copyed_link_30168, 3000);
                }
            }
        }
    }
}
