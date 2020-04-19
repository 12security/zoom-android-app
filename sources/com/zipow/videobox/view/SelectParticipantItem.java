package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class SelectParticipantItem {
    private static final String TAG = "SelectParticipantItem";
    private String mAvatarPath;
    private boolean mInSilentMode;
    private boolean mIsCohost;
    private boolean mIsHost;
    private String mScreenName;
    private String mSortKey;
    private long mUserId;

    public static class SelectParticipantItemComparator implements Comparator<SelectParticipantItem> {
        private Collator mCollator;

        public SelectParticipantItemComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull SelectParticipantItem selectParticipantItem, @NonNull SelectParticipantItem selectParticipantItem2) {
            if (selectParticipantItem == selectParticipantItem2) {
                return 0;
            }
            return this.mCollator.compare(StringUtil.safeString(selectParticipantItem.getSortKey()), StringUtil.safeString(selectParticipantItem2.getSortKey()));
        }
    }

    public SelectParticipantItem(CmmUser cmmUser) {
        this.mUserId = cmmUser.getNodeId();
        this.mScreenName = cmmUser.getScreenName();
        this.mIsHost = cmmUser.isHost();
        this.mIsCohost = cmmUser.isCoHost();
        this.mInSilentMode = cmmUser.inSilentMode();
        this.mAvatarPath = cmmUser.getSmallPicPath();
    }

    public String getSortKey() {
        return this.mSortKey;
    }

    public void setSortKey(String str) {
        this.mSortKey = str;
    }

    public View getView(Context context, View view, int i) {
        if (view == null || !TAG.equals(view.getTag())) {
            view = View.inflate(context, C4558R.layout.zm_select_participant_item, null);
            view.setTag(TAG);
        }
        AvatarView avatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtScreenName);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtRole);
        view.setBackgroundResource(C4558R.color.zm_transparent);
        ParamsBuilder paramsBuilder = new ParamsBuilder();
        String str = this.mScreenName;
        paramsBuilder.setName(str, str);
        if (i == 2) {
            paramsBuilder.setResource(C4558R.C4559drawable.avatar_phone_green, null);
        } else {
            paramsBuilder.setPath(this.mAvatarPath);
        }
        avatarView.show(paramsBuilder);
        textView.setText(this.mScreenName);
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null || !confStatusObj.isMyself(this.mUserId)) {
            if (this.mIsHost) {
                textView2.setVisibility(0);
                textView2.setText(context.getResources().getString(C4558R.string.zm_lbl_role_host));
            } else if (this.mIsCohost) {
                textView2.setVisibility(0);
                textView2.setText(context.getResources().getString(C4558R.string.zm_lbl_role_cohost));
            } else if (this.mInSilentMode) {
                textView2.setVisibility(0);
                textView2.setText(context.getResources().getString(C4558R.string.zm_lbl_role_in_silent_mode));
            } else {
                textView2.setVisibility(8);
            }
        } else if (this.mIsHost) {
            textView2.setVisibility(0);
            textView2.setText(context.getResources().getString(C4558R.string.zm_lbl_role_me_host));
        } else if (this.mIsCohost) {
            textView2.setVisibility(0);
            textView2.setText(context.getResources().getString(C4558R.string.zm_lbl_role_me_cohost));
        } else {
            textView2.setVisibility(8);
        }
        return view;
    }

    public long getUserId() {
        return this.mUserId;
    }

    public String getScreenName() {
        return this.mScreenName;
    }
}
