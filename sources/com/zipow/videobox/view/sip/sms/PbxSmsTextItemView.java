package com.zipow.videobox.view.sip.sms;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.util.ZMWebLinkFilter;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import com.zipow.videobox.view.sip.sms.AbsSmsView.OnClickMeetingNOListener;
import com.zipow.videobox.view.sip.sms.AbsSmsView.OnClickMessageListener;
import com.zipow.videobox.view.sip.sms.AbsSmsView.OnClickStatusImageListener;
import com.zipow.videobox.view.sip.sms.AbsSmsView.OnShowContextMenuListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMTextView;
import p021us.zoom.androidlib.widget.ZMTextView.LocalLinkMovementMethod;
import p021us.zoom.androidlib.widget.ZMTextView.OnClickLinkListener;
import p021us.zoom.videomeetings.C4558R;

public class PbxSmsTextItemView extends AbsSmsView implements OnClickLinkListener {
    @Nullable
    protected ImageView mImgStatus;
    @Nullable
    protected PBXMessageItem mMessageItem;
    @Nullable
    protected LinearLayout mPanelMessage;
    @Nullable
    protected ProgressBar mProgressBar;
    @Nullable
    protected TextView mTxtMessage;
    @Nullable
    protected TextView mTxtScreenName;

    static class MeetingNoHookInfo {
        public int end;
        public String lable;

        /* renamed from: no */
        public String f354no;
        public int start;

        MeetingNoHookInfo() {
        }
    }

    public PbxSmsTextItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public PbxSmsTextItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public PbxSmsTextItemView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void initView() {
        inflateLayout();
        this.mTxtMessage = (TextView) findViewById(C4558R.C4560id.txtMessage);
        this.mImgStatus = (ImageView) findViewById(C4558R.C4560id.imgStatus);
        this.mProgressBar = (ProgressBar) findViewById(C4558R.C4560id.progressBar1);
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mPanelMessage = (LinearLayout) findViewById(C4558R.C4560id.panel_textMessage);
        setStatusImage();
        updateProgressBar();
        LinearLayout linearLayout = this.mPanelMessage;
        if (linearLayout != null) {
            linearLayout.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnShowContextMenuListener onShowContextMenuListener = PbxSmsTextItemView.this.getOnShowContextMenuListener();
                    if (onShowContextMenuListener != null) {
                        return onShowContextMenuListener.onShowContextMenu(view, PbxSmsTextItemView.this.mMessageItem);
                    }
                    return false;
                }
            });
            this.mPanelMessage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickMessageListener onClickMessageListener = PbxSmsTextItemView.this.getOnClickMessageListener();
                    if (onClickMessageListener != null) {
                        onClickMessageListener.onClickMessage(PbxSmsTextItemView.this.mMessageItem);
                    }
                }
            });
        }
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickStatusImageListener onClickStatusImageListener = PbxSmsTextItemView.this.getOnClickStatusImageListener();
                    if (onClickStatusImageListener != null) {
                        onClickStatusImageListener.onClickStatusImage(PbxSmsTextItemView.this.mMessageItem);
                    }
                }
            });
        }
    }

    public void setStatusImage() {
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            PBXMessageItem pBXMessageItem = this.mMessageItem;
            if (pBXMessageItem == null) {
                imageView.setVisibility(8);
            } else if (pBXMessageItem.getDirection() == 1) {
                int sendStatus = this.mMessageItem.getSendStatus();
                if (sendStatus == 2 || sendStatus == 6) {
                    this.mImgStatus.setVisibility(0);
                    this.mImgStatus.setImageResource(C4558R.C4559drawable.zm_mm_msg_state_fail);
                    return;
                }
                this.mImgStatus.setVisibility(8);
            } else {
                this.mImgStatus.setVisibility(8);
            }
        }
    }

    public void updateProgressBar() {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            PBXMessageItem pBXMessageItem = this.mMessageItem;
            if (pBXMessageItem == null) {
                progressBar.setVisibility(8);
                return;
            }
            if (pBXMessageItem.getDirection() == 1) {
                switch (this.mMessageItem.getSendStatus()) {
                    case 0:
                    case 1:
                        this.mProgressBar.setVisibility(0);
                        break;
                    default:
                        this.mProgressBar.setVisibility(8);
                        break;
                }
            } else {
                this.mProgressBar.setVisibility(8);
            }
        }
    }

    public void setScreenName() {
        if (this.mTxtScreenName != null) {
            PBXMessageItem pBXMessageItem = this.mMessageItem;
            if (pBXMessageItem != null) {
                if (pBXMessageItem.isSentMsg()) {
                    this.mTxtScreenName.setText(C4558R.string.zm_lbl_content_you);
                } else {
                    PBXMessageContact fromContact = this.mMessageItem.getFromContact();
                    if (fromContact == null) {
                        this.mTxtScreenName.setText("");
                    } else if (!TextUtils.isEmpty(fromContact.getDisplayName())) {
                        this.mTxtScreenName.setText(fromContact.getDisplayName());
                    } else {
                        this.mTxtScreenName.setText(fromContact.getPhoneNumber());
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_pbx_sms_text_item, this);
    }

    public void setSmsItem(@NonNull PBXMessageItem pBXMessageItem) {
        this.mMessageItem = pBXMessageItem;
        setMessage(pBXMessageItem.getText());
        updateChatMsgBackground();
        LinearLayout linearLayout = (LinearLayout) findViewById(C4558R.C4560id.panelMsgLayout);
        if (pBXMessageItem.isOnlyMessageShow()) {
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setVisibility(8);
            }
            linearLayout.setPadding(linearLayout.getPaddingLeft(), 0, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
        } else {
            linearLayout.setPadding(linearLayout.getPaddingLeft(), UIUtil.dip2px(getContext(), 8.0f), linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
            TextView textView2 = this.mTxtScreenName;
            if (textView2 != null) {
                textView2.setVisibility(0);
                setScreenName();
            }
        }
        setStatusImage();
        updateProgressBar();
    }

    private void updateChatMsgBackground() {
        if (this.mPanelMessage != null) {
            PBXMessageItem pBXMessageItem = this.mMessageItem;
            this.mPanelMessage.setBackgroundResource((pBXMessageItem == null || !pBXMessageItem.isSentMsg()) ? C4558R.C4559drawable.zm_pbx_sms_receive_bg : C4558R.C4559drawable.zm_pbx_sms_sent_bg);
        }
    }

    private int getLinkTextColor() {
        return getResources().getColor(C4558R.color.zm_link_text_color);
    }

    /* access modifiers changed from: protected */
    public int getTextColor() {
        return getResources().getColor(C4558R.color.zm_text_on_light);
    }

    public boolean onLongClickLink(String str) {
        if (this.mPanelMessage == null) {
            return false;
        }
        OnShowContextMenuListener onShowContextMenuListener = getOnShowContextMenuListener();
        if (onShowContextMenuListener != null) {
            return onShowContextMenuListener.onShowLinkContextMenu(this.mPanelMessage, this.mMessageItem, str);
        }
        return false;
    }

    public boolean onLongClickWhole(String str) {
        if (this.mPanelMessage == null) {
            return false;
        }
        OnShowContextMenuListener onShowContextMenuListener = getOnShowContextMenuListener();
        if (onShowContextMenuListener != null) {
            return onShowContextMenuListener.onShowContextMenu(this.mPanelMessage, this.mMessageItem);
        }
        return false;
    }

    private void hookZoomURL(@Nullable TextView textView) {
        if (textView != null) {
            CharSequence text = textView.getText();
            Matcher matcher = Pattern.compile("(?<!\\d)(?:([0-9]{9,11}))(?!\\d)|(?<!\\d)(?:([0-9]{3})-([0-9]{3})-([0-9]{3,5}))(?!\\d)|(?<!\\d)(?:([0-9]{3}) ([0-9]{3}) ([0-9]{3,5}))(?!\\d)").matcher(text);
            ArrayList<MeetingNoHookInfo> arrayList = new ArrayList<>();
            while (matcher.find()) {
                MeetingNoHookInfo meetingNoHookInfo = new MeetingNoHookInfo();
                meetingNoHookInfo.end = matcher.end();
                meetingNoHookInfo.start = matcher.start();
                meetingNoHookInfo.lable = matcher.group();
                meetingNoHookInfo.f354no = meetingNoHookInfo.lable.replace("-", "").replace(OAuth.SCOPE_DELIMITER, "");
                arrayList.add(meetingNoHookInfo);
            }
            if (arrayList.size() > 0 && !(text instanceof Spannable)) {
                CharSequence spannableString = new SpannableString(text);
                textView.setText(spannableString);
                text = spannableString;
            }
            if (text instanceof Spannable) {
                Spannable spannable = (Spannable) text;
                URLSpan[] urls = textView.getUrls();
                if ((urls != null && urls.length >= 1) || arrayList.size() != 0) {
                    if (urls != null && urls.length > 0) {
                        for (URLSpan uRLSpan : urls) {
                            final String url = uRLSpan.getURL();
                            if (url.startsWith("http://https://") || url.startsWith("http://http://")) {
                                url = url.substring(7);
                            } else if (url.startsWith("tel:")) {
                                url = url.substring(4);
                            }
                            if (ZMIMUtils.isZoomURL(url)) {
                                C42204 r5 = new URLSpan(url) {
                                    public void onClick(View view) {
                                        PbxSmsTextItemView.this.joinByURL(url);
                                    }
                                };
                                int spanStart = spannable.getSpanStart(uRLSpan);
                                int spanEnd = spannable.getSpanEnd(uRLSpan);
                                int spanFlags = spannable.getSpanFlags(uRLSpan);
                                if (spanStart >= 0 && spanEnd > spanStart) {
                                    spannable.removeSpan(uRLSpan);
                                    spannable.setSpan(r5, spanStart, spanEnd, spanFlags);
                                }
                                removeMeetingNoHookInfoInArea(arrayList, spanStart, spanEnd);
                            } else if (ZMIMUtils.isZoomMeetingNo(url)) {
                                C42215 r52 = new URLSpan(url) {
                                    public void onClick(View view) {
                                        OnClickMeetingNOListener onClickMeetingNOListener = PbxSmsTextItemView.this.getOnClickMeetingNOListener();
                                        if (onClickMeetingNOListener != null) {
                                            onClickMeetingNOListener.onClickMeetingNO(url);
                                        }
                                    }
                                };
                                int spanStart2 = spannable.getSpanStart(uRLSpan);
                                int spanEnd2 = spannable.getSpanEnd(uRLSpan);
                                int spanFlags2 = spannable.getSpanFlags(uRLSpan);
                                if (spanStart2 >= 0 && spanEnd2 > spanStart2) {
                                    spannable.removeSpan(uRLSpan);
                                    spannable.setSpan(r52, spanStart2, spanEnd2, spanFlags2);
                                }
                                removeMeetingNoHookInfoInArea(arrayList, spanStart2, spanEnd2);
                            }
                        }
                    }
                    for (MeetingNoHookInfo meetingNoHookInfo2 : arrayList) {
                        final String str = meetingNoHookInfo2.f354no;
                        C42226 r3 = new URLSpan(str) {
                            public void onClick(View view) {
                                OnClickMeetingNOListener onClickMeetingNOListener = PbxSmsTextItemView.this.getOnClickMeetingNOListener();
                                if (onClickMeetingNOListener != null) {
                                    onClickMeetingNOListener.onClickMeetingNO(str);
                                }
                            }
                        };
                        if (meetingNoHookInfo2.start >= 0 && meetingNoHookInfo2.end > meetingNoHookInfo2.start) {
                            spannable.setSpan(r3, meetingNoHookInfo2.start, meetingNoHookInfo2.end, 33);
                        }
                    }
                }
            }
        }
    }

    private void removeMeetingNoHookInfoInArea(@Nullable List<MeetingNoHookInfo> list, int i, int i2) {
        if (!CollectionsUtil.isListEmpty(list) && i >= 0 && i < i2) {
            int i3 = 0;
            while (i3 < list.size()) {
                MeetingNoHookInfo meetingNoHookInfo = (MeetingNoHookInfo) list.get(i3);
                if (meetingNoHookInfo.start >= i && meetingNoHookInfo.end <= i2) {
                    list.remove(i3);
                    i3--;
                }
                i3++;
            }
        }
    }

    /* access modifiers changed from: private */
    public void joinByURL(String str) {
        Intent intent = new Intent(getContext(), JoinByURLActivity.class);
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        getContext().startActivity(intent);
    }

    public void setMessage(@Nullable CharSequence charSequence) {
        if (!(charSequence == null || this.mTxtMessage == null)) {
            CommonEmojiHelper.getInstance();
            this.mTxtMessage.setText(charSequence);
            this.mTxtMessage.setMovementMethod(LocalLinkMovementMethod.getInstance());
            this.mTxtMessage.setTextColor(getTextColor());
            this.mTxtMessage.setLinkTextColor(getLinkTextColor());
            TextView textView = this.mTxtMessage;
            if (textView instanceof ZMTextView) {
                ((ZMTextView) textView).setOnClickLinkListener(this);
            }
        }
        hookZoomURL(this.mTxtMessage);
        ZMWebLinkFilter.filter(this.mTxtMessage);
    }

    public PBXMessageItem getSmsItem() {
        return this.mMessageItem;
    }
}
