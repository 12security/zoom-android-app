package com.zipow.videobox.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.TimeFormatUtil;
import com.zipow.videobox.util.ZMFacebookUtils;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class IMMessageView extends LinearLayout {
    private static final String TAG = "IMMessageView";
    private AvatarView mAvatarView;
    private boolean mIsIncomingMessage = true;
    private String mMessage;
    private TextView mTxtMessage;
    private TextView mTxtScreenName;
    private TextView mTxtTime;

    static class MessageContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 0;

        public MessageContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public IMMessageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public IMMessageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public IMMessageView(Context context) {
        super(context);
        initView();
    }

    public IMMessageView(Context context, boolean z) {
        super(context);
        this.mIsIncomingMessage = z;
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mTxtTime = (TextView) findViewById(C4558R.C4560id.txtTime);
        this.mTxtMessage = (TextView) findViewById(C4558R.C4560id.txtMessage);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mTxtMessage.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                IMMessageView.this.showMessageContextMenu();
                return true;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), this.mIsIncomingMessage ? C4558R.layout.zm_im_message_from : C4558R.layout.zm_im_message_to, this);
    }

    public void setScreenName(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setText(charSequence);
            }
        }
    }

    public void setTime(long j) {
        String str;
        if (isInEditMode()) {
            str = "00:00 am";
        } else {
            str = TimeFormatUtil.formatTime(getContext(), j);
        }
        TextView textView = this.mTxtTime;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void setMessage(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            this.mMessage = charSequence.toString();
            TextView textView = this.mTxtMessage;
            if (textView != null) {
                textView.setText(charSequence);
                this.mTxtMessage.setMovementMethod(new LinkMovementMethod() {
                    public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
                        try {
                            return super.onTouchEvent(textView, spannable, motionEvent);
                        } catch (Exception unused) {
                            return false;
                        }
                    }
                });
            }
            hookZoomURL(this.mTxtMessage);
        }
    }

    public void setMessageItem(@NonNull IMMessageItem iMMessageItem) {
        setScreenName(iMMessageItem.fromScreenName);
        setTime(iMMessageItem.messageTime);
        setMessage(iMMessageItem.message);
        if (!isInEditMode()) {
            this.mAvatarView.show(new ParamsBuilder().setPath(ZMFacebookUtils.getVCardFileName(PTApp.getInstance().getPTLoginType(), iMMessageItem.fromJid)));
        }
    }

    private void hookZoomURL(@Nullable TextView textView) {
        if (textView != null) {
            CharSequence text = textView.getText();
            if (text instanceof Spannable) {
                Spannable spannable = (Spannable) text;
                URLSpan[] urls = textView.getUrls();
                if (urls != null && urls.length >= 1) {
                    for (URLSpan uRLSpan : urls) {
                        final String url = uRLSpan.getURL();
                        if (url.startsWith("http://https://") || url.startsWith("http://http://")) {
                            url = url.substring(7);
                        }
                        if (isZoomURL(url)) {
                            C34983 r4 = new URLSpan(url) {
                                public void onClick(View view) {
                                    IMMessageView.this.joinByURL(url);
                                }
                            };
                            int spanStart = spannable.getSpanStart(uRLSpan);
                            int spanEnd = spannable.getSpanEnd(uRLSpan);
                            int spanFlags = spannable.getSpanFlags(uRLSpan);
                            if (spanStart >= 0 && spanEnd > spanStart) {
                                spannable.removeSpan(uRLSpan);
                                spannable.setSpan(r4, spanStart, spanEnd, spanFlags);
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void joinByURL(String str) {
        Intent intent = new Intent(getContext(), JoinByURLActivity.class);
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        ActivityStartHelper.startActivityForeground(getContext(), intent);
    }

    private boolean isZoomURL(String str) {
        return str.matches("https?://.+\\.zoom\\.us/[j|w]/.+");
    }

    /* access modifiers changed from: private */
    public void showMessageContextMenu() {
        Activity activity = (Activity) getContext();
        if (activity != null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(activity, false);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            boolean z = zoomMessenger != null ? zoomMessenger.msgCopyGetOption() == 1 : true;
            if (z) {
                zMMenuAdapter.addItem(new MessageContextMenuItem(activity.getString(C4558R.string.zm_mm_lbl_copy_message), 0));
            }
            ZMAlertDialog create = new Builder(activity).setAdapter(zMMenuAdapter, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    IMMessageView.this.onSelectContextMenuItem((MessageContextMenuItem) zMMenuAdapter.getItem(i));
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            if (zMMenuAdapter.getCount() != 0) {
                create.show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(MessageContextMenuItem messageContextMenuItem) {
        if (messageContextMenuItem.getAction() == 0 && !StringUtil.isEmptyOrNull(this.mMessage)) {
            AndroidAppUtil.copyText(getContext(), this.mMessage);
        }
    }
}
