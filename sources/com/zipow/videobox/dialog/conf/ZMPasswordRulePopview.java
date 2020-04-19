package com.zipow.videobox.dialog.conf;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.PopupWindow;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import com.zipow.videobox.confapp.meeting.PasswordItem;
import com.zipow.videobox.util.ZMScheduleUtil;
import com.zipow.videobox.view.adapter.ZMPasswordRuleAdapter;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ZMPasswordRulePopview {
    private ZMPasswordRuleAdapter mAdapter;
    /* access modifiers changed from: private */
    @NonNull
    public Context mContext;
    private OnGlobalLayoutListener mLayoutChangeListener = new OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            ZMPasswordRulePopview zMPasswordRulePopview = ZMPasswordRulePopview.this;
            if (zMPasswordRulePopview.isKeyboardClosed(zMPasswordRulePopview.mRootView)) {
                ZMPasswordRulePopview.this.dismissPopupWindow();
            }
        }
    };
    private List<PasswordItem> mPasswordItems;
    private PopupWindow mPopupWindow;
    private RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public View mRootView;

    public ZMPasswordRulePopview(@NonNull Context context) {
        this.mContext = context;
        initPopview();
    }

    private void initPopview() {
        View inflate = LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_schedule_password_popview, null);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.rv_PasswordList);
        this.mPasswordItems = new ArrayList();
        this.mPasswordItems = ZMScheduleUtil.getPasswordRules();
        this.mAdapter = new ZMPasswordRuleAdapter(this.mContext, this.mPasswordItems);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.mRecyclerView.addItemDecoration(new ItemDecoration() {
            public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView recyclerView, @NonNull State state) {
                rect.top = UIUtil.dip2px(ZMPasswordRulePopview.this.mContext, 15.0f);
            }
        });
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mPopupWindow = new PopupWindow(inflate, -2, -2, false);
        this.mPopupWindow.setBackgroundDrawable(this.mContext.getResources().getDrawable(C4558R.C4559drawable.zm_corner_bg_white_gray));
        this.mPopupWindow.setInputMethodMode(1);
        this.mPopupWindow.setSoftInputMode(16);
    }

    public boolean showAsDropDown(@NonNull View view) {
        if (this.mPopupWindow == null || this.mPasswordItems.size() <= 0 || this.mPopupWindow.isShowing()) {
            return false;
        }
        updatePopWindow();
        this.mPopupWindow.showAsDropDown(view, UIUtil.dip2px(this.mContext, 6.0f), UIUtil.dip2px(this.mContext, 6.0f));
        return true;
    }

    private void updatePopWindow() {
        int displayWidth = UIUtil.getDisplayWidth(this.mContext) - UIUtil.dip2px(this.mContext, 12.0f);
        if (displayWidth > 0) {
            this.mPopupWindow.setWidth(displayWidth);
        }
        int displayHeight = UIUtil.getDisplayHeight(this.mContext) / 3;
        if (this.mPopupWindow.getHeight() > displayHeight) {
            this.mPopupWindow.setHeight(displayHeight);
        }
        if (this.mContext instanceof ZMActivity) {
            this.mPopupWindow.setOutsideTouchable(false);
            removeKeyBoardListener();
            this.mRootView = ((ZMActivity) this.mContext).getWindow().getDecorView();
            this.mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this.mLayoutChangeListener);
            return;
        }
        this.mPopupWindow.setOutsideTouchable(true);
    }

    /* access modifiers changed from: private */
    public boolean isKeyboardClosed(@NonNull View view) {
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        return view.getBottom() - rect.bottom < view.getHeight() / 4;
    }

    public boolean isPopupWindowShowing() {
        PopupWindow popupWindow = this.mPopupWindow;
        return popupWindow != null && popupWindow.isShowing();
    }

    public void onPasswordChange(@NonNull String str) {
        ZMScheduleUtil.updateRulesItem(str, this.mPasswordItems);
        this.mAdapter.refresh(this.mPasswordItems);
        if (AccessibilityUtil.isSpokenFeedbackEnabled(this.mContext)) {
            notifyUnmetRules();
        }
    }

    private void notifyUnmetRules() {
        List<PasswordItem> list = this.mPasswordItems;
        if (list != null && list.size() != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for (PasswordItem passwordItem : this.mPasswordItems) {
                if (!passwordItem.isCorrect()) {
                    stringBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                    stringBuffer.append(passwordItem.getRuleTxt());
                }
            }
            if (stringBuffer.length() != 0) {
                stringBuffer.insert(0, this.mContext.getString(C4558R.string.zm_accessibility_password_not_met_136699));
                AccessibilityUtil.announceForAccessibilityCompat(this.mRootView, (CharSequence) stringBuffer);
            }
        }
    }

    public void dismissPopupWindow() {
        PopupWindow popupWindow = this.mPopupWindow;
        if (popupWindow != null && popupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }
        removeKeyBoardListener();
    }

    private void removeKeyBoardListener() {
        View view = this.mRootView;
        if (view != null) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this.mLayoutChangeListener);
        }
    }
}
