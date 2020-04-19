package com.zipow.videobox.view.video;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.view.NormalMessageTip;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class RCFloatView extends LinearLayout implements OnClickListener {
    private static final String RC_TAP_MESSAGE_TIP_TAG = "RC_TAP";
    private static final String TAG = "RCFloatView";
    private IRemoteControlButtonStatusListener iEnabledRemoteControlListener;
    private ConfActivityNormal mActivity;
    /* access modifiers changed from: private */
    public boolean mCleanHiddenEditText = false;
    private float mDownRawX = 0.0f;
    private float mDownRawY = 0.0f;
    private int mDraggingPointerIndex = -1;
    @NonNull
    Runnable mFirstFocusRunnabel = new Runnable() {
        public void run() {
            AccessibilityUtil.sendAccessibilityFocusEvent(RCFloatView.this.mIvRCControl);
        }
    };
    private GestureDetector mGestureDetector;
    @NonNull
    private Handler mHandler = new Handler();
    private Dialog mHelpDialog;
    private EditText mHiddenEditText;
    private ImageView mIvKeyboard;
    private ImageView mIvQuestion;
    /* access modifiers changed from: private */
    public ImageView mIvRCControl;
    private int mPosXStartDrag = 0;
    private int mPosYStartDrag = 0;
    private ViewGroup mVGContentSpan;
    private ViewGroup mVGFloatPanel;
    private boolean mbPosChanged = false;

    public interface IRemoteControlButtonStatusListener {
        void onEnabledRC(boolean z);
    }

    private class MoveGesture extends SimpleOnGestureListener {
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return true;
        }

        private MoveGesture() {
        }
    }

    public void setRemoteControlButtonStatusListener(IRemoteControlButtonStatusListener iRemoteControlButtonStatusListener) {
        this.iEnabledRemoteControlListener = iRemoteControlButtonStatusListener;
    }

    public RCFloatView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public RCFloatView(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(@NonNull Context context) {
        this.mActivity = (ConfActivityNormal) context;
        this.mHelpDialog = new Dialog(context);
        this.mHelpDialog.requestWindowFeature(1);
        this.mHelpDialog.setContentView(C4558R.layout.zm_rc_fingers_question);
        this.mHelpDialog.setCanceledOnTouchOutside(true);
        View inflate = LayoutInflater.from(context).inflate(C4558R.layout.zm_rc_float_view, this);
        this.mIvRCControl = (ImageView) inflate.findViewById(C4558R.C4560id.rc_control);
        this.mIvKeyboard = (ImageView) inflate.findViewById(C4558R.C4560id.rc_keyboard);
        this.mIvQuestion = (ImageView) inflate.findViewById(C4558R.C4560id.rc_question);
        this.mVGContentSpan = (ViewGroup) inflate.findViewById(C4558R.C4560id.rc_content_span);
        this.mHiddenEditText = (EditText) inflate.findViewById(C4558R.C4560id.rc_hidden_edit);
        this.mVGFloatPanel = (ViewGroup) inflate.findViewById(C4558R.C4560id.rc_float_panel);
        this.mGestureDetector = new GestureDetector(context, new MoveGesture());
        this.mIvRCControl.setOnClickListener(this);
        this.mIvRCControl.setImageResource(C4558R.C4559drawable.zm_rc_control);
        this.mIvQuestion.setOnClickListener(this);
        this.mVGContentSpan.setVisibility(4);
        this.mIvKeyboard.setOnClickListener(this);
        this.mHiddenEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(@NonNull Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(@NonNull CharSequence charSequence, int i, int i2, int i3) {
                if (!RCFloatView.this.mCleanHiddenEditText) {
                    ShareVideoScene shareVideoScene = ZMConfComponentMgr.getInstance().getShareVideoScene();
                    if (shareVideoScene != null) {
                        for (int i4 = 0; i4 < i2; i4++) {
                            shareVideoScene.remoteControlKeyInput(0);
                        }
                        CharSequence subSequence = charSequence.subSequence(i, charSequence.length());
                        int i5 = 0;
                        while (subSequence.toString().endsWith(FontStyleHelper.SPLITOR)) {
                            i5++;
                            subSequence = subSequence.subSequence(0, subSequence.length() - 1);
                        }
                        if (subSequence.length() > 0) {
                            shareVideoScene.remoteControlCharInput(subSequence.toString());
                        }
                        for (int i6 = 0; i6 < i5; i6++) {
                            shareVideoScene.remoteControlKeyInput(1);
                        }
                        if (i5 > 0) {
                            RCFloatView.this.clearHiddenEditText();
                        }
                    }
                }
            }
        });
        String message = getMessage();
        if (!StringUtil.isEmptyOrNull(message)) {
            this.mIvRCControl.setContentDescription(message);
        }
    }

    public void enableRC(boolean z) {
        if (z) {
            this.mHiddenEditText.requestFocus();
            this.mVGContentSpan.setVisibility(0);
            this.mIvRCControl.setImageResource(C4558R.C4559drawable.zm_rc_control_reverse_bg);
            this.mVGFloatPanel.setBackgroundResource(C4558R.C4559drawable.zm_rc_drawer);
            IRemoteControlButtonStatusListener iRemoteControlButtonStatusListener = this.iEnabledRemoteControlListener;
            if (iRemoteControlButtonStatusListener != null) {
                iRemoteControlButtonStatusListener.onEnabledRC(true);
            }
            String message = getMessage();
            if (!StringUtil.isEmptyOrNull(message)) {
                this.mIvRCControl.setContentDescription(message);
            }
        } else {
            this.mHiddenEditText.clearFocus();
            this.mVGContentSpan.setVisibility(4);
            this.mIvRCControl.setImageResource(C4558R.C4559drawable.zm_rc_control);
            this.mVGFloatPanel.setBackgroundResource(0);
            showKeyboard(false);
            if (this.mHelpDialog.isShowing()) {
                this.mHelpDialog.dismiss();
            }
            IRemoteControlButtonStatusListener iRemoteControlButtonStatusListener2 = this.iEnabledRemoteControlListener;
            if (iRemoteControlButtonStatusListener2 != null) {
                iRemoteControlButtonStatusListener2.onEnabledRC(false);
            }
        }
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        if (layoutParams != null) {
            correctPostion(layoutParams.leftMargin, layoutParams.topMargin);
        }
    }

    private void showRCTapMessageTip() {
        String message = getMessage();
        if (!StringUtil.isEmptyOrNull(message)) {
            this.mIvRCControl.setContentDescription(message);
            NormalMessageTip.show(this.mActivity.getSupportFragmentManager(), RC_TAP_MESSAGE_TIP_TAG, (String) null, message, C4558R.C4560id.rc_control, 3, false);
        }
    }

    public void showRCFloatView(boolean z, boolean z2) {
        if (z) {
            View view = (View) getParent();
            if (view != null) {
                if (!this.mbPosChanged) {
                    LayoutParams layoutParams = (LayoutParams) getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.topMargin = view.getHeight() - UIUtil.dip2px(this.mActivity, 150.0f);
                        layoutParams.leftMargin = UIUtil.dip2px(this.mActivity, 50.0f);
                        setLayoutParams(layoutParams);
                    }
                }
                setVisibility(0);
                if (z2) {
                    showRCTapMessageTip();
                }
                AbsVideoSceneMgr videoSceneMgr = this.mActivity.getVideoSceneMgr();
                if (videoSceneMgr instanceof VideoSceneMgr) {
                    ((VideoSceneMgr) videoSceneMgr).switchToDefaultScene();
                }
            } else {
                return;
            }
        } else {
            setVisibility(8);
            if (this.mHelpDialog.isShowing()) {
                this.mHelpDialog.dismiss();
            }
            NormalMessageTip.dismiss(this.mActivity.getSupportFragmentManager(), RC_TAP_MESSAGE_TIP_TAG);
        }
        ShareVideoScene shareVideoScene = ZMConfComponentMgr.getInstance().getShareVideoScene();
        enableRC(shareVideoScene != null && shareVideoScene.isInRemoteControlMode());
        showKeyboard(false);
        if (z2 && z) {
            this.mHandler.removeCallbacks(this.mFirstFocusRunnabel);
            this.mHandler.postDelayed(this.mFirstFocusRunnabel, 200);
        }
    }

    public boolean dispatchTouchEvent(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null) {
            return super.dispatchTouchEvent(null);
        }
        if (motionEvent.getActionMasked() == 0 && this.mDraggingPointerIndex < 0) {
            NormalMessageTip.dismiss(this.mActivity.getSupportFragmentManager(), RC_TAP_MESSAGE_TIP_TAG);
            int actionIndex = motionEvent.getActionIndex();
            this.mDownRawX = motionEvent.getRawX();
            this.mDownRawY = motionEvent.getRawY();
            LayoutParams layoutParams = (LayoutParams) getLayoutParams();
            if (layoutParams != null) {
                this.mPosXStartDrag = layoutParams.leftMargin;
                this.mPosYStartDrag = layoutParams.topMargin;
            }
            if (this.mVGContentSpan.getVisibility() != 4) {
                this.mDraggingPointerIndex = actionIndex;
            } else if (new Rect(this.mIvRCControl.getLeft(), this.mIvRCControl.getTop(), this.mIvRCControl.getRight(), this.mIvRCControl.getBottom()).contains((int) motionEvent.getX(actionIndex), (int) motionEvent.getY(actionIndex))) {
                this.mDraggingPointerIndex = actionIndex;
            }
        } else if (motionEvent.getAction() == 1 && motionEvent.getActionIndex() == this.mDraggingPointerIndex) {
            this.mDraggingPointerIndex = -1;
        } else if (motionEvent.getActionMasked() == 2 && this.mDraggingPointerIndex >= 0 && motionEvent.getActionIndex() == this.mDraggingPointerIndex && correctPostion((int) ((((float) this.mPosXStartDrag) + motionEvent.getRawX()) - this.mDownRawX), (int) ((((float) this.mPosYStartDrag) + motionEvent.getRawY()) - this.mDownRawY))) {
            this.mbPosChanged = true;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mGestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    private boolean correctPostion(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, Integer.MIN_VALUE);
        measure(makeMeasureSpec, makeMeasureSpec);
        int measuredWidth = this.mVGContentSpan.getVisibility() == 0 ? getMeasuredWidth() : this.mIvRCControl.getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        View view = (View) getParent();
        if (view == null) {
            return false;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (i < 0) {
            i = 0;
        }
        if (i2 < 0) {
            i2 = 0;
        }
        if (i + measuredWidth > width) {
            i = width - measuredWidth;
        }
        if (i2 + measuredHeight > height) {
            i2 = height - measuredHeight;
        }
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        if (layoutParams == null) {
            return false;
        }
        if (layoutParams.topMargin == i2 && layoutParams.leftMargin == i) {
            return false;
        }
        layoutParams.topMargin = i2;
        layoutParams.leftMargin = i;
        setLayoutParams(layoutParams);
        return true;
    }

    public void onClick(View view) {
        boolean z = true;
        if (view == this.mIvRCControl) {
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null) {
                ConfMgr instance = ConfMgr.getInstance();
                if (instance.getMyself() != null) {
                    if (shareObj.isRemoteController(instance.getMyself().getNodeId())) {
                        if (this.mVGContentSpan.getVisibility() == 0) {
                            z = false;
                        }
                        enableRC(z);
                    } else {
                        shareObj.grabRemoteControl(instance.getMyself().getNodeId());
                    }
                    NormalMessageTip.dismiss(this.mActivity.getSupportFragmentManager(), RC_TAP_MESSAGE_TIP_TAG);
                }
            }
        } else if (view == this.mIvKeyboard) {
            showKeyboard(true);
        } else if (view == this.mIvQuestion) {
            this.mHelpDialog.show();
        }
    }

    public void onConfLayoutChanged(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        if (layoutParams != null) {
            correctPostion(layoutParams.leftMargin, layoutParams.topMargin);
        }
    }

    private void showKeyboard(boolean z) {
        if (z) {
            this.mHiddenEditText.requestFocus();
            clearHiddenEditText();
            UIUtil.openSoftKeyboard(getContext(), this.mHiddenEditText);
            return;
        }
        UIUtil.closeSoftKeyboard(getContext(), this.mHiddenEditText);
    }

    /* access modifiers changed from: private */
    public void clearHiddenEditText() {
        this.mCleanHiddenEditText = true;
        this.mHiddenEditText.setText("");
        this.mCleanHiddenEditText = false;
    }

    public boolean isControlling() {
        return this.mVGContentSpan.getVisibility() == 0;
    }

    private String getMessage() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return null;
        }
        CmmUser userById = ConfMgr.getInstance().getUserById(shareObj.getActiveUserID());
        if (userById == null) {
            return null;
        }
        String screenName = userById.getScreenName();
        return String.format(this.mActivity.getString(C4558R.string.zm_rc_tap_notice), new Object[]{screenName});
    }
}
