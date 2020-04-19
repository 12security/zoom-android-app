package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMMessageRemoveHistory */
public class MMMessageRemoveHistory extends AbsMessageView {
    Context context;
    private TextView mTxtMessage;
    private ImageView timeChatPop;

    /* renamed from: com.zipow.videobox.view.mm.MMMessageRemoveHistory$TimedChatFragment */
    public static class TimedChatFragment extends ZMDialogFragment {
        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            ZMAlertDialog create = new Builder(getActivity()).setMessage(C4558R.string.zm_mm_msg_timed_chat3_33479).setPositiveButton(C4558R.string.zm_mm_msg_timed_chat_ok_33479, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setNegativeButton(C4558R.string.zm_mm_msg_timed_chat_learn_more_33479, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (TimedChatFragment.this.getActivity() != null) {
                        ActivityStartHelper.startActivityForeground(TimedChatFragment.this.getActivity(), new Intent("android.intent.action.VIEW", Uri.parse(TimedChatFragment.this.getString(C4558R.string.zm_zoom_learn_more))));
                    }
                }
            }).create();
            create.setCanceledOnTouchOutside(false);
            return create;
        }

        public void onDismiss(DialogInterface dialogInterface) {
            super.onDismiss(dialogInterface);
        }
    }

    public MMMessageItem getMessageItem() {
        return null;
    }

    public Rect getMessageLocationOnScreen() {
        return null;
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
    }

    public MMMessageRemoveHistory(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        this.context = context2;
        initView();
        this.timeChatPop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MMMessageRemoveHistory.this.show();
            }
        });
        this.mTxtMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MMMessageRemoveHistory.this.show();
            }
        });
    }

    public MMMessageRemoveHistory(Context context2) {
        this(context2, null);
    }

    private void initView() {
        View.inflate(getContext(), C4558R.layout.zm_mm_message_remove_history, this);
        this.mTxtMessage = (TextView) findViewById(C4558R.C4560id.txtMessage);
        this.timeChatPop = (ImageView) findViewById(C4558R.C4560id.timeChatPop);
    }

    /* access modifiers changed from: private */
    public void show() {
        new TimedChatFragment().show(((ZMActivity) getContext()).getSupportFragmentManager(), TimedChatFragment.class.getName());
    }

    public void setMessage(@Nullable CharSequence charSequence) {
        TextView textView = this.mTxtMessage;
        if (textView == null) {
            return;
        }
        if (charSequence != null) {
            textView.setText(charSequence);
        } else {
            textView.setText("");
        }
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        setMessage(mMMessageItem.message);
    }
}
