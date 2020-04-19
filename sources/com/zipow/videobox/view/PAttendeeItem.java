package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmAttentionTrackMgr;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ZMConfUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class PAttendeeItem {
    public boolean audioOn;
    public long audioType = 2;
    @Nullable
    public String email;
    public boolean isAttentionMode;
    public boolean isRaisedHand;
    public boolean isShowGuest;
    public String name;
    public long nodeID;

    public PAttendeeItem(@Nullable CmmUser cmmUser) {
        if (cmmUser != null && cmmUser.isViewOnlyUserCanTalk()) {
            this.name = cmmUser.getScreenName();
            this.email = cmmUser.getEmail();
            this.nodeID = cmmUser.getNodeId();
            this.isShowGuest = ConfLocalHelper.isGuest(cmmUser) && !ConfLocalHelper.isGuestForMyself();
            ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(this.nodeID);
            if (zoomQABuddyByNodeId != null) {
                this.isRaisedHand = ConfLocalHelper.isHaisedHand(zoomQABuddyByNodeId.getJID());
            }
            this.isAttentionMode = cmmUser.isInAttentionMode();
            updateAudio(this.nodeID);
        }
    }

    @Nullable
    public ConfChatAttendeeItem getConfChatAttendeeItem() {
        ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(this.nodeID);
        if (zoomQABuddyByNodeId != null) {
            return new ConfChatAttendeeItem(zoomQABuddyByNodeId);
        }
        return null;
    }

    @Nullable
    public View getView(@NonNull Context context, @Nullable View view) {
        if (view == null || !"webinar".equals(view.getTag())) {
            view = View.inflate(context, C4558R.layout.zm_qa_webinar_attendee_email_item, null);
            view.setTag("webinar");
        }
        bindView(context, view);
        return view;
    }

    private void bindView(@NonNull Context context, @Nullable View view) {
        if (view != null) {
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtRole);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtEmail);
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgAudio);
            ImageView imageView2 = (ImageView) view.findViewById(C4558R.C4560id.imgRaiseHand);
            ImageView imageView3 = (ImageView) view.findViewById(C4558R.C4560id.imgAttention);
            ((TextView) view.findViewById(C4558R.C4560id.txtName)).setText(this.name);
            textView.setVisibility(8);
            view.setBackgroundResource(this.isShowGuest ? C4558R.C4559drawable.zm_list_selector_guest : C4558R.color.zm_transparent);
            if (StringUtil.isEmptyOrNull(this.email)) {
                ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(this.nodeID);
                if (zoomQABuddyByNodeId != null) {
                    this.email = zoomQABuddyByNodeId.getEmail();
                }
            }
            textView2.setText(this.email);
            textView2.setVisibility(StringUtil.isEmptyOrNull(this.email) ? 8 : 0);
            imageView2.setVisibility(this.isRaisedHand ? 0 : 8);
            CmmAttentionTrackMgr attentionTrackAPI = ConfMgr.getInstance().getAttentionTrackAPI();
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (!(shareObj != null && (shareObj.getShareStatus() == 3 || shareObj.getShareStatus() == 2)) || attentionTrackAPI == null || !attentionTrackAPI.isConfAttentionTrackEnabled()) {
                imageView3.setVisibility(8);
            } else {
                imageView3.setVisibility(this.isAttentionMode ? 4 : 0);
            }
            if (this.audioType != 2) {
                imageView.setVisibility(0);
                imageView.setContentDescription(context.getString(this.audioOn ? C4558R.string.zm_description_plist_status_audio_on : C4558R.string.zm_description_plist_status_audio_off));
                imageView.setImageResource(ZMConfUtil.getAudioImageResId(view.isInEditMode(), this.audioOn, this.audioType, this.nodeID));
                Drawable drawable = imageView.getDrawable();
                if (drawable instanceof AnimationDrawable) {
                    ((AnimationDrawable) drawable).start();
                }
            } else {
                imageView.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateAudio(long j) {
        CmmAudioStatus cmmAudioStatus = ZMConfUtil.getCmmAudioStatus(j);
        if (cmmAudioStatus != null) {
            this.audioOn = !cmmAudioStatus.getIsMuted();
            this.audioType = cmmAudioStatus.getAudiotype();
        }
    }
}
