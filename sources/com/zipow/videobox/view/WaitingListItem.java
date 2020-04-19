package com.zipow.videobox.view;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmAttentionTrackMgr;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class WaitingListItem implements OnClickListener, OnLongClickListener {
    @Nullable
    public String avatar;
    private boolean isSetAvatar = false;
    private Button mBtnAdmin;
    private View mTxtJoining;
    public String screenName;
    public String userFBID;
    public long userId;

    public static class WaitingListActionMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_REMOVE = 0;

        public WaitingListActionMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public WaitingListItem(CmmUser cmmUser) {
        update(cmmUser);
    }

    @NonNull
    private WaitingListItem update(@Nullable CmmUser cmmUser) {
        if (cmmUser == null) {
            return this;
        }
        this.screenName = cmmUser.getScreenName();
        this.userFBID = cmmUser.getUserFBID();
        this.userId = cmmUser.getNodeId();
        this.isSetAvatar = false;
        return this;
    }

    public View getView(@NonNull Context context, View view) {
        if (view == null || !"waitinglist".equals(view.getTag())) {
            view = View.inflate(context, C4558R.layout.zm_waitinglist_item, null);
            view.setTag("waitinglist");
        }
        AvatarView avatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtScreenName);
        this.mBtnAdmin = (Button) view.findViewById(C4558R.C4560id.btnAdmin);
        this.mTxtJoining = view.findViewById(C4558R.C4560id.txtJoining);
        ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgAttention);
        textView.setText(this.screenName);
        if (!view.isInEditMode()) {
            ConfMgr instance = ConfMgr.getInstance();
            CmmUser userById = instance.getUserById(this.userId);
            ParamsBuilder paramsBuilder = new ParamsBuilder();
            String str = this.screenName;
            paramsBuilder.setName(str, str);
            boolean z = false;
            if (userById != null) {
                if (userById.isPureCallInUser()) {
                    paramsBuilder.setResource(C4558R.C4559drawable.avatar_phone_green, null);
                } else if (userById.isH323User()) {
                    paramsBuilder.setResource(C4558R.C4559drawable.zm_h323_avatar, null);
                } else {
                    paramsBuilder.setPath(this.avatar);
                }
                avatarView.show(paramsBuilder);
                if (userById.isLeavingSilentMode()) {
                    this.mBtnAdmin.setVisibility(8);
                    this.mTxtJoining.setVisibility(0);
                } else {
                    this.mBtnAdmin.setVisibility(0);
                    this.mTxtJoining.setVisibility(8);
                }
            }
            CmmConfContext confContext = instance.getConfContext();
            if (confContext == null || !confContext.isMeetingSupportSilentMode() || userById == null || userById.isLeavingSilentMode()) {
                this.mBtnAdmin.setVisibility(8);
            } else {
                this.mBtnAdmin.setVisibility(0);
                if (confContext.supportPutUserinWaitingListUponEntryFeature()) {
                    this.mBtnAdmin.setText(context.getString(C4558R.string.zm_btn_admit));
                } else {
                    this.mBtnAdmin.setText(context.getString(C4558R.string.zm_mi_leave_silent_mode));
                }
            }
            CmmUser myself = instance.getMyself();
            CmmAttentionTrackMgr attentionTrackAPI = instance.getAttentionTrackAPI();
            ShareSessionMgr shareObj = instance.getShareObj();
            if (shareObj != null && (shareObj.getShareStatus() == 3 || shareObj.getShareStatus() == 2)) {
                z = true;
            }
            if (attentionTrackAPI == null || !attentionTrackAPI.isConfAttentionTrackEnabled() || !z || myself == null || (!myself.isHost() && !myself.isCoHost() && !myself.isBOModerator())) {
                imageView.setVisibility(8);
            } else {
                imageView.setVisibility(4);
            }
        }
        view.setOnLongClickListener(this);
        this.mBtnAdmin.setOnClickListener(this);
        return view;
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnAdmin) {
            onClickAdmin();
        }
    }

    private void onClickAdmin() {
        doAdmitCmd();
    }

    /* access modifiers changed from: private */
    public void onClickRemove() {
        ConfMgr.getInstance().handleUserCmd(30, this.userId);
    }

    public void doAdmitCmd() {
        ConfMgr.getInstance().handleUserCmd(43, this.userId);
    }

    public boolean onLongClick(@Nullable View view) {
        if (view == null) {
            return false;
        }
        Context context = view.getContext();
        if (context == null) {
            return false;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if ((confContext != null && !confContext.isMeetingSupportSilentMode() && !confContext.supportPutUserinWaitingListUponEntryFeature()) || !ConfLocalHelper.isHostCoHost()) {
            return false;
        }
        ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
        zMMenuAdapter.addItem(new WaitingListActionMenuItem(context.getString(C4558R.string.zm_btn_remove), 0));
        ZMAlertDialog create = new Builder(context).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                WaitingListItem.this.onClickRemove();
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
        return false;
    }
}
