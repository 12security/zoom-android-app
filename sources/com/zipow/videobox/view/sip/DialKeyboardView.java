package com.zipow.videobox.view.sip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class DialKeyboardView extends LinearLayout implements OnClickListener, OnLongClickListener {
    private OnKeyDialListener mOnKeyDialListener;

    public interface OnKeyDialListener {
        void onKeyDial(String str);
    }

    public DialKeyboardView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public DialKeyboardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public DialKeyboardView(Context context) {
        super(context);
        init();
    }

    public void setOnKeyDialListener(OnKeyDialListener onKeyDialListener) {
        this.mOnKeyDialListener = onKeyDialListener;
    }

    public void setOnDrakMode() {
        setOnDardMode(this);
    }

    private void setOnDardMode(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof DialPadNumView) {
                ((DialPadNumView) childAt).setOnDrakMode();
            } else if (childAt instanceof ViewGroup) {
                setOnDardMode((ViewGroup) childAt);
            }
        }
    }

    private void init() {
        View.inflate(getContext(), C4558R.layout.zm_dialpad, this);
        if (!isInEditMode()) {
            findViewById(C4558R.C4560id.btnKey1).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKey2).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKey3).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKey4).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKey5).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKey6).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKey7).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKey8).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKey9).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKey0).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKeyStar).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKeyNO).setOnClickListener(this);
            findViewById(C4558R.C4560id.btnKey0).setOnLongClickListener(this);
        }
    }

    public void setEnabled(boolean z) {
        findViewById(C4558R.C4560id.btnKey1).setEnabled(z);
        findViewById(C4558R.C4560id.btnKey2).setEnabled(z);
        findViewById(C4558R.C4560id.btnKey3).setEnabled(z);
        findViewById(C4558R.C4560id.btnKey4).setEnabled(z);
        findViewById(C4558R.C4560id.btnKey5).setEnabled(z);
        findViewById(C4558R.C4560id.btnKey6).setEnabled(z);
        findViewById(C4558R.C4560id.btnKey7).setEnabled(z);
        findViewById(C4558R.C4560id.btnKey8).setEnabled(z);
        findViewById(C4558R.C4560id.btnKey9).setEnabled(z);
        findViewById(C4558R.C4560id.btnKey0).setEnabled(z);
        findViewById(C4558R.C4560id.btnKeyStar).setEnabled(z);
        findViewById(C4558R.C4560id.btnKeyNO).setEnabled(z);
    }

    public void onClick(View view) {
        onClickBtnDefault(view);
    }

    private void onClickBtnDefault(@Nullable View view) {
        if (view instanceof DialPadNumView) {
            DialPadNumView dialPadNumView = (DialPadNumView) view;
            if (!(this.mOnKeyDialListener == null || dialPadNumView.getDialKey() == null)) {
                this.mOnKeyDialListener.onKeyDial(dialPadNumView.getDialKey());
            }
        }
    }

    public boolean onLongClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnKey0) {
            OnKeyDialListener onKeyDialListener = this.mOnKeyDialListener;
            if (onKeyDialListener != null) {
                onKeyDialListener.onKeyDial("+");
                return true;
            }
        }
        return false;
    }
}
