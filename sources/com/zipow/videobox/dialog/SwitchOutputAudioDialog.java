package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.AudioSessionMgr;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.util.ConfLocalHelper;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.HeadsetUtil.IHeadsetConnectionListener;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHItemClickListener;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHItemClickListener.OnItemClickListener;
import p021us.zoom.videomeetings.C4558R;

public class SwitchOutputAudioDialog extends ZMDialogFragment implements IHeadsetConnectionListener {
    /* access modifiers changed from: private */
    public boolean isStartingSco;
    /* access modifiers changed from: private */
    public boolean isStoppingSco;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mCheckCurrentStates = new Runnable() {
        public void run() {
            SwitchOutputAudioDialog.this.refreshAdapter();
            SwitchOutputAudioDialog.this.isStartingSco = HeadsetUtil.getInstance().ismIsStartingSco();
            SwitchOutputAudioDialog.this.isStoppingSco = HeadsetUtil.getInstance().ismIsStoppingSco();
            if (!SwitchOutputAudioDialog.this.isStartingSco && !SwitchOutputAudioDialog.this.isStoppingSco) {
                SwitchOutputAudioDialog.this.dismiss();
            }
        }
    };
    @Nullable
    private ConfActivity mConfActivity;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    @Nullable
    public MyAdapter myAdapter;

    static class AudioOutputItem {
        private int action;
        /* access modifiers changed from: private */
        public boolean isSelected;
        private String label;

        public AudioOutputItem(int i, String str, boolean z) {
            this.action = i;
            this.label = str;
            this.isSelected = z;
        }

        public int getAction() {
            return this.action;
        }

        public void setAction(int i) {
            this.action = i;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String str) {
            this.label = str;
        }

        public boolean isSelected() {
            return this.isSelected;
        }

        public void setSelected(boolean z) {
            this.isSelected = z;
        }
    }

    static class MyAdapter extends Adapter<MyViewHolder> {
        private final List<AudioOutputItem> mAudioOutputItems;

        static class MyViewHolder extends ViewHolder {
            final ImageView ivSelected;
            final View leftView;
            final ProgressBar progressBar;
            final TextView txtLabel;

            public MyViewHolder(@NonNull View view) {
                super(view);
                this.leftView = view.findViewById(C4558R.C4560id.fr_left);
                this.txtLabel = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
                this.ivSelected = (ImageView) view.findViewById(C4558R.C4560id.imgIcon);
                this.progressBar = (ProgressBar) view.findViewById(C4558R.C4560id.progressBar);
            }

            private boolean isStartingSco() {
                AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
                return audioObj != null && !audioObj.ismIsUseA2dpMode() && HeadsetUtil.getInstance().ismIsStartingSco();
            }

            private boolean isStoppingSco() {
                AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
                return audioObj != null && !audioObj.ismIsUseA2dpMode() && HeadsetUtil.getInstance().ismIsStoppingSco();
            }

            public void bind(@NonNull AudioOutputItem audioOutputItem) {
                this.txtLabel.setText(audioOutputItem.getLabel());
                if (audioOutputItem.isSelected) {
                    this.leftView.setVisibility(0);
                    if ((audioOutputItem.getAction() != 3 || !isStartingSco()) && (audioOutputItem.getAction() == 3 || !isStoppingSco())) {
                        this.progressBar.setVisibility(8);
                        this.ivSelected.setVisibility(0);
                        return;
                    }
                    this.progressBar.setVisibility(0);
                    this.ivSelected.setVisibility(8);
                    return;
                }
                this.leftView.setVisibility(4);
                this.progressBar.setVisibility(8);
            }
        }

        MyAdapter(List<AudioOutputItem> list) {
            this.mAudioOutputItems = list;
        }

        @Nullable
        public AudioOutputItem getItem(int i) {
            if (i >= getItemCount() || i < 0) {
                return null;
            }
            return (AudioOutputItem) this.mAudioOutputItems.get(i);
        }

        public void refresh(@NonNull List<AudioOutputItem> list) {
            this.mAudioOutputItems.clear();
            this.mAudioOutputItems.addAll(list);
            notifyDataSetChanged();
        }

        @NonNull
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_item_dialog_audio_output, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.bind((AudioOutputItem) this.mAudioOutputItems.get(i));
        }

        public int getItemCount() {
            if (CollectionsUtil.isCollectionEmpty(this.mAudioOutputItems)) {
                return 0;
            }
            return this.mAudioOutputItems.size();
        }
    }

    public static void showDialog(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            new SwitchOutputAudioDialog().show(fragmentManager, SwitchOutputAudioDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        ConfActivity confActivity = getConfActivity();
        if (confActivity == null) {
            return createEmptyDialog();
        }
        View createContent = createContent();
        if (createContent == null) {
            return createEmptyDialog();
        }
        ZMAlertDialog create = new Builder(confActivity).setTheme(C4558R.style.ZMDialog_Material_RoundRect).setView(createContent).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    private View createContent() {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material);
        View inflate = View.inflate(contextThemeWrapper, C4558R.layout.zm_recyclerview_dialog_switch_audio, null);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(contextThemeWrapper));
        ArrayList buildMenuItems = buildMenuItems();
        if (CollectionsUtil.isCollectionEmpty(buildMenuItems)) {
            return null;
        }
        this.myAdapter = new MyAdapter(buildMenuItems);
        recyclerView.setAdapter(this.myAdapter);
        recyclerView.addOnItemTouchListener(new RVHItemClickListener(contextThemeWrapper, new OnItemClickListener() {
            public void onItemClick(View view, int i) {
                AudioOutputItem item = SwitchOutputAudioDialog.this.myAdapter.getItem(i);
                if (item != null && item.getAction() != ConfUI.getInstance().getCurrentAudioSourceType()) {
                    ConfActivity access$100 = SwitchOutputAudioDialog.this.getConfActivity();
                    if (access$100 != null) {
                        ConfLocalHelper.switchAudioSource(access$100, ConfLocalHelper.getMyAudioType(), item.getAction());
                        SwitchOutputAudioDialog.this.mHandler.postDelayed(SwitchOutputAudioDialog.this.mCheckCurrentStates, 200);
                    }
                }
            }
        }));
        return inflate;
    }

    private ArrayList<AudioOutputItem> buildMenuItems() {
        String str;
        ArrayList<AudioOutputItem> arrayList = new ArrayList<>();
        if (ConfMgr.getInstance().getMyself() == null || ConfMgr.getInstance().getAudioObj() == null) {
            return null;
        }
        HeadsetUtil instance = HeadsetUtil.getInstance();
        int currentAudioSourceType = ConfUI.getInstance().getCurrentAudioSourceType();
        if (instance.isBTAndWiredHeadsetsOn()) {
            String connectedBTName = instance.getConnectedBTName();
            if (connectedBTName == null) {
                str = getString(C4558R.string.zm_mi_bluetooth);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(connectedBTName);
                sb.append("(");
                sb.append(getString(C4558R.string.zm_mi_bluetooth));
                sb.append(")");
                str = sb.toString();
            }
            boolean z = true;
            arrayList.add(new AudioOutputItem(3, str, currentAudioSourceType == 3));
            arrayList.add(new AudioOutputItem(2, getString(C4558R.string.zm_mi_wired_headset), currentAudioSourceType == 2));
            String string = getString(C4558R.string.zm_lbl_speaker);
            if (currentAudioSourceType != 0) {
                z = false;
            }
            arrayList.add(new AudioOutputItem(0, string, z));
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    @Nullable
    public ConfActivity getConfActivity() {
        if (this.mConfActivity == null) {
            this.mConfActivity = (ConfActivity) getActivity();
        }
        return this.mConfActivity;
    }

    public void onResume() {
        super.onResume();
        HeadsetUtil.getInstance().addListener(this);
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            if (!confActivity.canSwitchAudioSource()) {
                dismiss();
            } else {
                refreshAdapter();
            }
        }
    }

    public void onStop() {
        super.onStop();
        this.mHandler.removeCallbacks(this.mCheckCurrentStates);
    }

    /* access modifiers changed from: private */
    public void refreshAdapter() {
        if (this.myAdapter != null) {
            ArrayList buildMenuItems = buildMenuItems();
            if (!CollectionsUtil.isCollectionEmpty(buildMenuItems)) {
                this.myAdapter.refresh(buildMenuItems);
            } else {
                dismiss();
            }
        }
    }

    public void onPause() {
        super.onPause();
        HeadsetUtil.getInstance().removeListener(this);
    }

    public void onHeadsetStatusChanged(boolean z, boolean z2) {
        refreshAdapter();
    }

    public void onBluetoothScoAudioStatus(boolean z) {
        refreshAdapter();
        if ((this.isStartingSco && z) || (this.isStoppingSco && !z)) {
            dismiss();
        }
    }
}
