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
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ZMConfUtil;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfChatAttendeeItem extends BaseAttendeeItem {
    public static final int BUDDY_TYPE_ALL_WAITING_PEOPLE = 2;
    public static final int BUDDY_TYPE_EVERYONE = 0;
    public static final int BUDDY_TYPE_LABEL = -1;
    public static final int BUDDY_TYPE_PANELIST = 1;
    public static final int FAKE_ITEM_TYPE_EVERYONE = 1;
    private static final long serialVersionUID = 1;
    @Nullable
    public String email;
    public int fakeItemType;
    public boolean isAttentionMode = false;
    public boolean isPlaceholder = false;
    public boolean isShowGuest;
    public boolean isTelephone = false;
    @Nullable
    public String jid;
    @Nullable
    public String name;
    public long nodeID;
    public int role;
    private String sortKey;

    public static class WebinarAttendeeItemComparator implements Comparator<ConfChatAttendeeItem> {
        private Collator mCollator;

        public WebinarAttendeeItemComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull ConfChatAttendeeItem confChatAttendeeItem, @NonNull ConfChatAttendeeItem confChatAttendeeItem2) {
            if (confChatAttendeeItem == confChatAttendeeItem2) {
                return 0;
            }
            return this.mCollator.compare(confChatAttendeeItem.getSortKey(), confChatAttendeeItem2.getSortKey());
        }
    }

    public ConfChatAttendeeItem(String str, String str2, long j, int i) {
        set(str, str2, null, j, i, false);
    }

    public ConfChatAttendeeItem(@Nullable ZoomQABuddy zoomQABuddy, String str) {
        boolean z = false;
        if (zoomQABuddy != null) {
            this.name = zoomQABuddy.getName();
            this.jid = zoomQABuddy.getJID();
            this.email = zoomQABuddy.getEmail();
            this.nodeID = zoomQABuddy.getNodeID();
            this.role = zoomQABuddy.getRole();
            if (ConfLocalHelper.isGuest(zoomQABuddy) && !ConfLocalHelper.isGuestForMyself()) {
                z = true;
            }
            this.isShowGuest = z;
            this.isAttentionMode = zoomQABuddy.isInAttentionMode();
            this.isSupportTempTalk = zoomQABuddy.isAttendeeSupportTemporarilyFeature();
            this.isTelephone = zoomQABuddy.isTelephone();
            this.sortKey = str;
            if (this.isSupportTempTalk) {
                this.isAllowTalked = zoomQABuddy.isAttendeeCanTalk();
                updateAudio(this.nodeID);
            }
        }
    }

    public ConfChatAttendeeItem(@Nullable ZoomQABuddy zoomQABuddy) {
        boolean z = false;
        if (zoomQABuddy != null) {
            if (ConfLocalHelper.isGuest(zoomQABuddy) && !ConfLocalHelper.isGuestForMyself()) {
                z = true;
            }
            this.isShowGuest = z;
            set(zoomQABuddy.getName(), zoomQABuddy.getJID(), zoomQABuddy.getEmail(), zoomQABuddy.getNodeID(), zoomQABuddy.getRole(), zoomQABuddy.isInAttentionMode(), zoomQABuddy.isTelephone());
            this.isSupportTempTalk = zoomQABuddy.isAttendeeSupportTemporarilyFeature();
            if (this.isSupportTempTalk) {
                this.isAllowTalked = zoomQABuddy.isAttendeeCanTalk();
                updateAudio(this.nodeID);
            }
        }
    }

    public ConfChatAttendeeItem(@Nullable CmmUser cmmUser) {
        boolean z = false;
        if (cmmUser != null) {
            if (ConfLocalHelper.isGuest(cmmUser) && !ConfLocalHelper.isGuestForMyself()) {
                z = true;
            }
            this.isShowGuest = z;
            set(cmmUser.getScreenName(), null, cmmUser.getEmail(), cmmUser.getNodeId(), -1, cmmUser.isInAttentionMode());
        }
    }

    private void set(String str, String str2, String str3, long j, int i, boolean z, boolean z2) {
        set(str, str2, str3, j, i, z);
        this.isTelephone = z2;
    }

    private void set(String str, String str2, String str3, long j, int i, boolean z) {
        this.name = str;
        this.jid = str2;
        this.email = str3;
        this.nodeID = j;
        this.role = i;
        this.isAttentionMode = z;
        this.sortKey = SortUtil.getSortKey(str, CompatUtils.getLocalDefault());
    }

    public String getSortKey() {
        return this.sortKey;
    }

    public void setSortKey(String str) {
        this.sortKey = str;
    }

    public static ConfChatAttendeeItem getWebinarAttendeeItemByNodeId(long j) {
        ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(j);
        if (zoomQABuddyByNodeId != null && zoomQABuddyByNodeId.getRole() == 0) {
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

    @NonNull
    public String getSendContentDescription(@NonNull Context context) {
        long j = this.nodeID;
        if (j == 0) {
            return context.getString(C4558R.string.zm_lbl_content_send_to, new Object[]{context.getString(C4558R.string.zm_mi_everyone_122046)});
        } else if (j == 1) {
            return context.getString(C4558R.string.zm_lbl_content_send_to, new Object[]{context.getString(C4558R.string.zm_webinar_txt_all_panelists)});
        } else if (StringUtil.isEmptyOrNull(this.name)) {
            return context.getString(C4558R.string.zm_btn_send);
        } else {
            return context.getString(C4558R.string.zm_lbl_content_send_to, new Object[]{this.name});
        }
    }

    private void bindView(@NonNull Context context, @Nullable View view) {
        if (view != null) {
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtRole);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtEmail);
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgAudio);
            ImageView imageView2 = (ImageView) view.findViewById(C4558R.C4560id.imgRaiseHand);
            ImageView imageView3 = (ImageView) view.findViewById(C4558R.C4560id.imgAttention);
            ((TextView) view.findViewById(C4558R.C4560id.txtName)).setText(this.name);
            if (StringUtil.isEmptyOrNull(this.email)) {
                ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(this.nodeID);
                if (zoomQABuddyByNodeId != null) {
                    this.email = zoomQABuddyByNodeId.getEmail();
                }
            }
            textView2.setText(this.email);
            textView2.setVisibility(StringUtil.isEmptyOrNull(this.email) ? 8 : 0);
            view.setBackgroundResource(this.isShowGuest ? C4558R.C4559drawable.zm_list_selector_guest : C4558R.color.zm_transparent);
            textView.setVisibility(8);
            imageView2.setVisibility(ConfLocalHelper.isHaisedHand(this.jid) ? 0 : 8);
            CmmAttentionTrackMgr attentionTrackAPI = ConfMgr.getInstance().getAttentionTrackAPI();
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (!(shareObj != null && (shareObj.getShareStatus() == 3 || shareObj.getShareStatus() == 2)) || attentionTrackAPI == null || !attentionTrackAPI.isConfAttentionTrackEnabled()) {
                imageView3.setVisibility(8);
            } else {
                imageView3.setVisibility(this.isAttentionMode ? 4 : 0);
            }
            if (!this.isSupportTempTalk || this.audioType == 2) {
                imageView.setVisibility(8);
            } else {
                imageView.setVisibility(0);
                imageView.setContentDescription(context.getString(this.audioOn ? C4558R.string.zm_description_plist_status_audio_on : C4558R.string.zm_description_plist_status_audio_off));
                imageView.setImageResource(ZMConfUtil.getAudioImageResId(view.isInEditMode(), this.audioOn, this.audioType, this.nodeID));
                Drawable drawable = imageView.getDrawable();
                if (drawable instanceof AnimationDrawable) {
                    ((AnimationDrawable) drawable).start();
                }
            }
        }
    }

    public void setFakeItemType(int i) {
        this.fakeItemType = i;
    }
}
