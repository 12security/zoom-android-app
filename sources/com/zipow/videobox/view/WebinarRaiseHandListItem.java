package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ZMConfUtil;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.videomeetings.C4558R;

public class WebinarRaiseHandListItem extends BaseAttendeeItem {
    public static int ITEM_TYPE_ATTENDEE = 2;
    public static int ITEM_TYPE_PANELIST = 1;
    private static final long serialVersionUID = 4804252551400086512L;
    private boolean mIsShowGuest;
    private String mJid;
    private String mName;
    private long mRaiseHandTimestamp;
    private int mType;
    private long mUserId;
    private String sortKey;

    public static class WebinarAttendeeItemComparator implements Comparator<WebinarRaiseHandListItem> {
        private Collator mCollator;

        public WebinarAttendeeItemComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull WebinarRaiseHandListItem webinarRaiseHandListItem, @NonNull WebinarRaiseHandListItem webinarRaiseHandListItem2) {
            if (webinarRaiseHandListItem == webinarRaiseHandListItem2) {
                return 0;
            }
            int i = ((webinarRaiseHandListItem.getmRaiseHandTimestamp() - webinarRaiseHandListItem2.getmRaiseHandTimestamp()) > 0 ? 1 : ((webinarRaiseHandListItem.getmRaiseHandTimestamp() - webinarRaiseHandListItem2.getmRaiseHandTimestamp()) == 0 ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            if (i < 0) {
                return -1;
            }
            return this.mCollator.compare(webinarRaiseHandListItem.getSortKey(), webinarRaiseHandListItem2.getSortKey());
        }
    }

    @Nullable
    public ConfChatAttendeeItem getConfChatAttendeeItem() {
        ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(this.mUserId);
        if (zoomQABuddyByNodeId != null) {
            return new ConfChatAttendeeItem(zoomQABuddyByNodeId);
        }
        return null;
    }

    public WebinarRaiseHandListItem(String str, String str2, long j, int i) {
        set(str, str2, j, i);
    }

    public WebinarRaiseHandListItem(@Nullable ZoomQABuddy zoomQABuddy) {
        if (zoomQABuddy != null) {
            set(zoomQABuddy.getName(), zoomQABuddy.getJID(), zoomQABuddy.getNodeID(), ITEM_TYPE_ATTENDEE);
            this.mRaiseHandTimestamp = zoomQABuddy.getRaiseHandTimestamp();
            this.mIsShowGuest = ConfLocalHelper.isGuest(zoomQABuddy) && !ConfLocalHelper.isGuestForMyself();
            this.isSupportTempTalk = zoomQABuddy.isAttendeeSupportTemporarilyFeature();
            if (this.isSupportTempTalk) {
                this.isAllowTalked = zoomQABuddy.isAttendeeCanTalk();
                updateAudio(this.mUserId);
            }
        }
    }

    public WebinarRaiseHandListItem(@Nullable CmmUser cmmUser) {
        if (cmmUser != null) {
            this.mIsShowGuest = ConfLocalHelper.isGuest(cmmUser) && !ConfLocalHelper.isGuestForMyself();
            this.mRaiseHandTimestamp = cmmUser.getRaiseHandTimestamp();
            set(cmmUser.getScreenName(), null, cmmUser.getNodeId(), ITEM_TYPE_PANELIST);
        }
    }

    private void set(String str, String str2, long j, int i) {
        this.mName = str;
        this.mJid = str2;
        this.mUserId = j;
        this.mType = i;
        this.sortKey = SortUtil.getSortKey(str, CompatUtils.getLocalDefault());
    }

    public String getSortKey() {
        return this.sortKey;
    }

    public String getName() {
        return this.mName;
    }

    public String getJid() {
        return this.mJid;
    }

    public long getUserId() {
        return this.mUserId;
    }

    public int getItemType() {
        return this.mType;
    }

    public long getmRaiseHandTimestamp() {
        return this.mRaiseHandTimestamp;
    }

    public static WebinarRaiseHandListItem getWebinarAttendeeItemByNodeId(long j) {
        ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(j);
        if (zoomQABuddyByNodeId != null && zoomQABuddyByNodeId.getRole() == 0) {
            return new WebinarRaiseHandListItem(zoomQABuddyByNodeId);
        }
        return null;
    }

    @Nullable
    private View createViewByType(Context context) {
        int i = this.mType;
        if (i == ITEM_TYPE_PANELIST) {
            View inflate = View.inflate(context, C4558R.layout.zm_plist_item, null);
            inflate.setTag("panelist");
            return inflate;
        } else if (i != ITEM_TYPE_ATTENDEE) {
            return null;
        } else {
            View inflate2 = View.inflate(context, C4558R.layout.zm_qa_webinar_attendee_item, null);
            inflate2.setTag("attendeeList");
            return inflate2;
        }
    }

    @Nullable
    public View getView(@NonNull Context context, @Nullable View view) {
        boolean z = true;
        if (view != null) {
            int i = this.mType;
            if (i != ITEM_TYPE_PANELIST ? i != ITEM_TYPE_ATTENDEE || "attendeeList".equals(view.getTag()) : "panelist".equals(view.getTag())) {
                z = false;
            }
        }
        if (z) {
            view = createViewByType(context);
        }
        if (view != null) {
            bindView(context, view);
        }
        return view;
    }

    private void bindView(@NonNull Context context, @NonNull View view) {
        view.setBackgroundResource(this.mIsShowGuest ? C4558R.C4559drawable.zm_list_selector_guest : C4558R.color.zm_transparent);
        int i = this.mType;
        if (i == ITEM_TYPE_PANELIST) {
            AvatarView avatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtScreenName);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtRole);
            view.findViewById(C4558R.C4560id.imgArrow).setVisibility(8);
            view.findViewById(C4558R.C4560id.txtUnreadMessageCount).setVisibility(8);
            view.findViewById(C4558R.C4560id.imgAudio).setVisibility(8);
            view.findViewById(C4558R.C4560id.imgVideo).setVisibility(8);
            view.findViewById(C4558R.C4560id.imgRecording).setVisibility(8);
            view.findViewById(C4558R.C4560id.imgCMRRecording).setVisibility(8);
            View findViewById = view.findViewById(C4558R.C4560id.imgAttention);
            if (findViewById != null) {
                findViewById.setVisibility(8);
            }
            textView.setText(this.mName);
            ParamsBuilder paramsBuilder = new ParamsBuilder();
            String str = this.mName;
            avatarView.show(paramsBuilder.setName(str, str));
            textView2.setVisibility(8);
        } else if (i == ITEM_TYPE_ATTENDEE) {
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgAudio);
            TextView textView3 = (TextView) view.findViewById(C4558R.C4560id.txtRole);
            ((TextView) view.findViewById(C4558R.C4560id.txtName)).setText(this.mName);
            textView3.setVisibility(8);
            if (!this.isSupportTempTalk || this.audioType == 2) {
                imageView.setVisibility(8);
                return;
            }
            imageView.setVisibility(0);
            imageView.setContentDescription(context.getString(this.audioOn ? C4558R.string.zm_description_plist_status_audio_on : C4558R.string.zm_description_plist_status_audio_off));
            imageView.setImageResource(ZMConfUtil.getAudioImageResId(view.isInEditMode(), this.audioOn, this.audioType, this.mUserId));
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                ((AnimationDrawable) drawable).start();
            }
        }
    }
}
