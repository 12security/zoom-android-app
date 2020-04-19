package p021us.zoom.androidlib.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.ref.WeakReference;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertHolder.ContentPadding;

/* renamed from: us.zoom.androidlib.widget.ZMAlertDialog */
public class ZMAlertDialog extends Dialog implements DialogInterface {
    private LinearLayout buttonPanel;
    private LinearLayout contentPanel;
    private FrameLayout customFrame;
    /* access modifiers changed from: private */
    public ZMAlertHolder holder;
    private ImageView mAlertIcon;
    OnClickListener mButtonHandler;
    /* access modifiers changed from: private */
    public Button mButtonNegative;
    /* access modifiers changed from: private */
    public Message mButtonNegativeMessage;
    /* access modifiers changed from: private */
    public Button mButtonNeutral;
    /* access modifiers changed from: private */
    public Message mButtonNeutralMessage;
    /* access modifiers changed from: private */
    public Button mButtonPositive;
    /* access modifiers changed from: private */
    public Message mButtonPositiveMessage;
    protected Context mContext;
    private View mCustomPanelBottomGap;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private LayoutInflater mInflater;
    private ScrollView mScrollView;
    private LinearLayout mTopPanel;
    private TextView msg;
    private TextView title;

    /* renamed from: us.zoom.androidlib.widget.ZMAlertDialog$Builder */
    public static class Builder {
        private ZMAlertHolder holder;

        public Builder(@NonNull Context context) {
            this.holder = new ZMAlertHolder(context);
        }

        public ZMAlertDialog create() {
            ZMAlertHolder zMAlertHolder = this.holder;
            ZMAlertDialog zMAlertDialog = new ZMAlertDialog(zMAlertHolder, zMAlertHolder.getTheme());
            this.holder.setDialog(zMAlertDialog);
            zMAlertDialog.setCancelable(this.holder.isCancelable());
            if (this.holder.getDismissListener() != null) {
                zMAlertDialog.setOnDismissListener(this.holder.getDismissListener());
            }
            return zMAlertDialog;
        }

        public Builder setMessage(String str) {
            this.holder.setMsg(str);
            return this;
        }

        public Builder setTheme(int i) {
            this.holder.setTheme(i);
            return this;
        }

        public Builder setTitleView(View view) {
            this.holder.setTitleView(view);
            return this;
        }

        public Builder setVerticalOptionStyle(boolean z) {
            this.holder.setVerticalOptionStyle(z);
            return this;
        }

        public Builder setSmallTitleMutlilineStyle(boolean z) {
            this.holder.setSmallTitleMutlilineStyle(z);
            return this;
        }

        public Builder setTitleFontSize(float f) {
            this.holder.setTitleFontSize(f);
            return this;
        }

        public Builder setTitleTxtColor(int i) {
            this.holder.setTitleTxtColor(i);
            return this;
        }

        public Builder setMessage(int i) {
            if (i > 0) {
                this.holder.setType(1);
                ZMAlertHolder zMAlertHolder = this.holder;
                zMAlertHolder.setMsg(zMAlertHolder.getContext().getString(i));
            } else {
                this.holder.setMsg(null);
            }
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.holder.setTitle(charSequence);
            return this;
        }

        public Builder setTitle(int i) {
            if (i > 0) {
                ZMAlertHolder zMAlertHolder = this.holder;
                zMAlertHolder.setTitle(zMAlertHolder.getContext().getString(i));
            } else {
                this.holder.setTitle(null);
            }
            return this;
        }

        public Builder setListDividerHeight(int i) {
            this.holder.setListDividerHeight(i);
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.holder.setIcon(drawable);
            return this;
        }

        public Builder setIcon(int i) {
            this.holder.setIcon(i);
            return this;
        }

        public Builder setNegativeButton(int i, DialogInterface.OnClickListener onClickListener) {
            ZMAlertHolder zMAlertHolder = this.holder;
            zMAlertHolder.setmNegativeButtonText(zMAlertHolder.getContext().getString(i));
            this.holder.setNegativeButtonListener(onClickListener);
            return this;
        }

        public Builder setNeutralButton(int i, DialogInterface.OnClickListener onClickListener) {
            ZMAlertHolder zMAlertHolder = this.holder;
            zMAlertHolder.setmNeutralButtonText(zMAlertHolder.getContext().getString(i));
            this.holder.setNeutralButtonListener(onClickListener);
            return this;
        }

        public Builder setPositiveButton(int i, DialogInterface.OnClickListener onClickListener) {
            this.holder.setPositiveButtonListener(onClickListener);
            ZMAlertHolder zMAlertHolder = this.holder;
            zMAlertHolder.setmPositiveButtonText(zMAlertHolder.getContext().getString(i));
            return this;
        }

        public Builder setNegativeButton(String str, DialogInterface.OnClickListener onClickListener) {
            this.holder.setmNegativeButtonText(str);
            this.holder.setNegativeButtonListener(onClickListener);
            return this;
        }

        public Builder setNeutralButton(String str, DialogInterface.OnClickListener onClickListener) {
            this.holder.setmNeutralButtonText(str);
            this.holder.setNeutralButtonListener(onClickListener);
            return this;
        }

        public Builder setPositiveButton(String str, DialogInterface.OnClickListener onClickListener) {
            this.holder.setPositiveButtonListener(onClickListener);
            this.holder.setmPositiveButtonText(str);
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.holder.setDismissListener(onDismissListener);
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.holder.setCancelable(z);
            return this;
        }

        public Builder setView(@NonNull View view) {
            this.holder.setmView(view);
            this.holder.setmViewSpacingSpecified(false);
            return this;
        }

        public Builder setView(View view, boolean z) {
            this.holder.setmView(view);
            this.holder.setmViewSpacingSpecified(false);
            this.holder.setHideBottomGap(z);
            return this;
        }

        public Button getButton(int i) {
            return this.holder.getDialog().getButton(i);
        }

        public void show() {
            if (this.holder.getDialog() == null) {
                create();
            }
            this.holder.getDialog().show();
        }

        public Builder setAdapter(ListAdapter listAdapter, DialogInterface.OnClickListener onClickListener) {
            this.holder.setType(2);
            this.holder.setAdater(listAdapter);
            this.holder.setListListener(onClickListener);
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] charSequenceArr, int i, DialogInterface.OnClickListener onClickListener) {
            this.holder.setType(3);
            this.holder.setSingleChoice(true);
            this.holder.setMultiChoice(false);
            this.holder.setItems(charSequenceArr);
            this.holder.setCheckIndex(i);
            this.holder.setListListener(onClickListener);
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] charSequenceArr, int i, DialogInterface.OnClickListener onClickListener) {
            this.holder.setType(3);
            this.holder.setMultiChoice(true);
            this.holder.setListListener(onClickListener);
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter listAdapter, int i, DialogInterface.OnClickListener onClickListener) {
            this.holder.setSingleChoice(true);
            this.holder.setType(2);
            this.holder.setAdater(listAdapter);
            this.holder.setListListener(onClickListener);
            return this;
        }

        public Builder setContentPadding(int i, int i2, int i3, int i4) {
            this.holder.setContentPadding(i, i2, i3, i4);
            return this;
        }

        public Builder setEnableAutoClickBtnDismiss(boolean z) {
            this.holder.setEnableAutoClickBtnDismiss(z);
            return this;
        }
    }

    /* renamed from: us.zoom.androidlib.widget.ZMAlertDialog$ButtonHandler */
    private static final class ButtonHandler extends Handler {
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialogInterface) {
            this.mDialog = new WeakReference<>(dialogInterface);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 1) {
                switch (i) {
                    case -3:
                    case -2:
                    case -1:
                        ((DialogInterface.OnClickListener) message.obj).onClick((DialogInterface) this.mDialog.get(), message.what);
                        return;
                    default:
                        return;
                }
            } else {
                ((DialogInterface) message.obj).dismiss();
            }
        }
    }

    public ZMAlertDialog(ZMAlertHolder zMAlertHolder) {
        this(zMAlertHolder, C4409R.style.ZMDialog_Material);
    }

    public ZMAlertDialog(ZMAlertHolder zMAlertHolder, int i) {
        super(zMAlertHolder.getContext(), i);
        this.mButtonHandler = new OnClickListener() {
            public void onClick(View view) {
                Message message = (view != ZMAlertDialog.this.mButtonPositive || ZMAlertDialog.this.mButtonPositiveMessage == null) ? (view != ZMAlertDialog.this.mButtonNegative || ZMAlertDialog.this.mButtonNegativeMessage == null) ? (view != ZMAlertDialog.this.mButtonNeutral || ZMAlertDialog.this.mButtonNeutralMessage == null) ? null : Message.obtain(ZMAlertDialog.this.mButtonNeutralMessage) : Message.obtain(ZMAlertDialog.this.mButtonNegativeMessage) : Message.obtain(ZMAlertDialog.this.mButtonPositiveMessage);
                if (message != null) {
                    message.sendToTarget();
                }
                if (ZMAlertDialog.this.holder.isEnableAutoClickBtnDismiss()) {
                    ZMAlertDialog.this.mHandler.obtainMessage(1, ZMAlertDialog.this).sendToTarget();
                }
            }
        };
        this.holder = zMAlertHolder;
        this.mContext = zMAlertHolder.getContext();
        this.mHandler = new ButtonHandler(this);
    }

    public ZMAlertDialog(Context context, int i) {
        super(context, C4409R.style.ZMDialog_Material);
        this.mButtonHandler = new OnClickListener() {
            public void onClick(View view) {
                Message message = (view != ZMAlertDialog.this.mButtonPositive || ZMAlertDialog.this.mButtonPositiveMessage == null) ? (view != ZMAlertDialog.this.mButtonNegative || ZMAlertDialog.this.mButtonNegativeMessage == null) ? (view != ZMAlertDialog.this.mButtonNeutral || ZMAlertDialog.this.mButtonNeutralMessage == null) ? null : Message.obtain(ZMAlertDialog.this.mButtonNeutralMessage) : Message.obtain(ZMAlertDialog.this.mButtonNegativeMessage) : Message.obtain(ZMAlertDialog.this.mButtonPositiveMessage);
                if (message != null) {
                    message.sendToTarget();
                }
                if (ZMAlertDialog.this.holder.isEnableAutoClickBtnDismiss()) {
                    ZMAlertDialog.this.mHandler.obtainMessage(1, ZMAlertDialog.this).sendToTarget();
                }
            }
        };
        this.holder = new ZMAlertHolder(context);
        this.mContext = context;
        this.mHandler = new ButtonHandler(this);
    }

    @Nullable
    public Button getButton(int i) {
        switch (i) {
            case -3:
                return this.mButtonNeutral;
            case -2:
                return this.mButtonNegative;
            case -1:
                return this.mButtonPositive;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().requestFeature(1);
        if (this.holder.getmView() == null || !canTextInput(this.holder.getmView())) {
            getWindow().setFlags(131072, 131072);
        }
        setContentView(C4409R.layout.zm_alert_layout);
        this.customFrame = (FrameLayout) findViewById(C4409R.C4411id.customPanel);
        this.mInflater = (LayoutInflater) this.holder.getContext().getSystemService("layout_inflater");
        this.contentPanel = (LinearLayout) findViewById(C4409R.C4411id.contentPanel);
        this.mScrollView = (ScrollView) findViewById(C4409R.C4411id.scrollView);
        this.buttonPanel = (LinearLayout) findViewById(C4409R.C4411id.buttonPanel);
        this.mTopPanel = (LinearLayout) findViewById(C4409R.C4411id.topPanel);
        if (17 == VERSION.SDK_INT) {
            setupLayoutBackground();
        }
        if (this.holder.getType() == 0 && !TextUtils.isEmpty(this.holder.getTitle()) && TextUtils.isEmpty(this.holder.getMsg())) {
            CharSequence title2 = this.holder.getTitle();
            this.holder.setTitle(null);
            this.holder.setMsg(title2);
        }
        if (this.holder.getTitle() == null) {
            this.mTopPanel.setVisibility(8);
            View titleView = this.holder.getTitleView();
            if (titleView != null) {
                LinearLayout linearLayout = (LinearLayout) findViewById(C4409R.C4411id.customTopPanel);
                linearLayout.addView(titleView, new LayoutParams(-1, -2));
                linearLayout.setVisibility(0);
                LinearLayout linearLayout2 = this.contentPanel;
                linearLayout2.setPadding(0, 0, 0, linearLayout2.getPaddingBottom());
            }
        } else {
            this.title = (TextView) findViewById(C4409R.C4411id.alertTitle);
            this.title.setText(this.holder.getTitle());
        }
        if (this.holder.getContentPadding() != null) {
            ContentPadding contentPadding = this.holder.getContentPadding();
            this.contentPanel.setPadding(contentPadding.left, contentPadding.top, contentPadding.right, contentPadding.bottom);
        }
        this.msg = (TextView) findViewById(C4409R.C4411id.alertdialogmsg);
        this.mCustomPanelBottomGap = findViewById(C4409R.C4411id.customPanelBottomGap);
        this.mAlertIcon = (ImageView) findViewById(C4409R.C4411id.alertIcon);
        if (Build.MANUFACTURER == null || !Build.MANUFACTURER.toLowerCase().contains("blu")) {
            this.mAlertIcon.setImageResource(17301543);
        } else {
            this.mAlertIcon.setImageResource(C4409R.C4410drawable.ic_dialog_alert);
        }
        setupButtons();
        setupContent();
        super.setCancelable(this.holder.isCancelable());
    }

    private void setupLayoutBackground() {
        View findViewById = findViewById(C4409R.C4411id.dialog_root_layout);
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(16842836, typedValue, true);
        findViewById.setBackgroundResource(typedValue.resourceId);
        getWindow().setBackgroundDrawableResource(17170445);
    }

    static boolean canTextInput(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        while (childCount > 0) {
            childCount--;
            if (canTextInput(viewGroup.getChildAt(childCount))) {
                return true;
            }
        }
        return false;
    }

    private boolean setupButtons() {
        boolean z;
        if (this.holder.isVerticalOptionStyle()) {
            findViewById(C4409R.C4411id.buttonPanelHorizontal).setVisibility(8);
            findViewById(C4409R.C4411id.buttonPanelVertical).setVisibility(0);
            this.mButtonPositive = (Button) findViewById(C4409R.C4411id.buttonV1);
            this.mButtonNeutral = (Button) findViewById(C4409R.C4411id.buttonV2);
            this.mButtonNegative = (Button) findViewById(C4409R.C4411id.buttonV3);
        } else {
            this.mButtonPositive = (Button) findViewById(C4409R.C4411id.button1);
            this.mButtonNegative = (Button) findViewById(C4409R.C4411id.button2);
            this.mButtonNeutral = (Button) findViewById(C4409R.C4411id.button3);
        }
        this.mButtonPositive.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.holder.getmPositiveButtonText())) {
            this.mButtonPositive.setVisibility(8);
            z = false;
        } else {
            this.mButtonPositive.setText(this.holder.getmPositiveButtonText());
            this.mButtonPositive.setVisibility(0);
            z = true;
        }
        this.mButtonNegative.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.holder.getmNegativeButtonText())) {
            this.mButtonNegative.setVisibility(8);
        } else {
            this.mButtonNegative.setText(this.holder.getmNegativeButtonText());
            this.mButtonNegative.setVisibility(0);
            z |= true;
        }
        this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.holder.getmNeutralButtonText())) {
            this.mButtonNeutral.setVisibility(8);
        } else {
            this.mButtonNeutral.setText(this.holder.getmNeutralButtonText());
            this.mButtonNeutral.setVisibility(0);
            z |= true;
        }
        if (z) {
            if (this.holder.getmPositiveButtonText() != null) {
                setButtonMessage(-1, this.holder.getmPositiveButtonText(), this.holder.getPositiveButtonListener(), null);
            }
            if (this.holder.getmNegativeButtonText() != null) {
                setButtonMessage(-2, this.holder.getmNegativeButtonText(), this.holder.getNegativeButtonListener(), null);
            }
            if (this.holder.getmNeutralButtonText() != null) {
                setButtonMessage(-3, this.holder.getmNeutralButtonText(), this.holder.getNeutralButtonListener(), null);
            }
            if (!this.holder.isVerticalOptionStyle()) {
                int childCount = this.buttonPanel.getChildCount() - 1;
                while (true) {
                    if (childCount < 0) {
                        break;
                    }
                    View childAt = this.buttonPanel.getChildAt(childCount);
                    if (childAt.getVisibility() == 0) {
                        ((LayoutParams) childAt.getLayoutParams()).rightMargin = 0;
                        break;
                    }
                    childCount--;
                }
            }
        } else {
            this.buttonPanel.setVisibility(8);
        }
        if (z) {
            return true;
        }
        return false;
    }

    private void setButtonMessage(int i, CharSequence charSequence, DialogInterface.OnClickListener onClickListener, Message message) {
        if (message == null && onClickListener != null) {
            message = this.mHandler.obtainMessage(i, onClickListener);
        }
        switch (i) {
            case -3:
                this.mButtonNeutralMessage = message;
                return;
            case -2:
                this.mButtonNegativeMessage = message;
                return;
            case -1:
                this.mButtonPositiveMessage = message;
                return;
            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    public void setButton(int i, CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        switch (i) {
            case -3:
                this.holder.setmNeutralButtonText(charSequence.toString());
                this.holder.setNeutralButtonListener(onClickListener);
                return;
            case -2:
                this.holder.setmNegativeButtonText(charSequence.toString());
                this.holder.setNegativeButtonListener(onClickListener);
                return;
            case -1:
                this.holder.setmPositiveButtonText(charSequence.toString());
                this.holder.setPositiveButtonListener(onClickListener);
                return;
            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    private void hideContentPanel() {
        this.msg.setVisibility(8);
        this.mScrollView.setVisibility(8);
        this.contentPanel.removeView(this.mScrollView);
        this.contentPanel.setVisibility(8);
    }

    private void setupContent() {
        int i = 0;
        this.mScrollView.setFocusable(false);
        if (this.holder.getType() == 0) {
            hideContentPanel();
        } else if (this.holder.getType() == 1) {
            CharSequence msg2 = this.holder.getMsg();
            Drawable icon = this.holder.getIcon();
            if (msg2 == null && icon == null) {
                hideContentPanel();
                return;
            }
            if (msg2 == null) {
                msg2 = "";
            }
            this.msg.setText(msg2);
            if (this.holder.getTitle() == null) {
                this.msg.setPadding(0, UIUtil.dip2px(this.mContext, 20.0f), 0, 0);
                this.msg.setTextAppearance(this.mContext, C4409R.style.ZMTextView_Medium_DialogMsg);
            }
            if (icon == null) {
                this.mAlertIcon.setVisibility(8);
                return;
            }
            this.mAlertIcon.setVisibility(0);
            this.mAlertIcon.setImageDrawable(icon);
        } else if (this.holder.getType() == 2 || this.holder.getType() == 3) {
            this.msg.setVisibility(8);
            this.mScrollView.setVisibility(8);
            this.contentPanel.removeView(this.mScrollView);
            this.mScrollView = null;
            this.contentPanel.addView(createListView(), new LayoutParams(-1, -1));
            this.contentPanel.setLayoutParams(new LayoutParams(-1, 0, 1.0f));
            this.mCustomPanelBottomGap.setVisibility(8);
            if (this.holder.getTitle() != null) {
                this.title.setVisibility(8);
                TextView textView = (TextView) findViewById(C4409R.C4411id.alertOptionTitle);
                textView.setText(this.holder.getTitle());
                textView.setVisibility(0);
                if (this.holder.getTitleTxtColor() != 0) {
                    textView.setTextColor(this.holder.getTitleTxtColor());
                }
                if (this.holder.getTitleFontSize() != 0.0f) {
                    textView.setTextSize(this.holder.getTitleFontSize());
                }
                textView.setSingleLine(true ^ this.holder.isSmallTitleMutlilineStyle());
                this.mTopPanel.setPadding(0, 0, 0, 0);
            }
        } else if (this.holder.getType() == 5) {
            hideContentPanel();
            this.customFrame.setVisibility(0);
            FrameLayout frameLayout = (FrameLayout) findViewById(C4409R.C4411id.customView);
            this.holder.ismViewSpacingSpecified();
            View view = this.mCustomPanelBottomGap;
            if (this.holder.isHideBottomGap()) {
                i = 8;
            }
            view.setVisibility(i);
            frameLayout.addView(this.holder.getmView(), new ViewGroup.LayoutParams(-1, -1));
        }
    }

    public void setCancelable(boolean z) {
        this.holder.setCancelable(z);
        super.setCancelable(z);
    }

    public void setView(View view) {
        this.holder.setmView(view);
    }

    public boolean onKeyDown(int i, @NonNull KeyEvent keyEvent) {
        ScrollView scrollView = this.mScrollView;
        if (scrollView == null || !scrollView.executeKeyEvent(keyEvent)) {
            return super.onKeyDown(i, keyEvent);
        }
        return true;
    }

    public boolean onKeyUp(int i, @NonNull KeyEvent keyEvent) {
        ScrollView scrollView = this.mScrollView;
        if (scrollView == null || !scrollView.executeKeyEvent(keyEvent)) {
            return super.onKeyUp(i, keyEvent);
        }
        return true;
    }

    @Nullable
    private ListView createListView() {
        final ListView listView = (ListView) this.mInflater.inflate(C4409R.layout.zm_select_dialog, null);
        if (this.holder.getAdater() == null && this.holder.getType() == 3) {
            SingleItemAdapter singleItemAdapter = new SingleItemAdapter(this.holder.getItems(), this.holder.getContext());
            singleItemAdapter.setIndex(this.holder.getCheckIndex());
            listView.setAdapter(singleItemAdapter);
        } else if (this.holder.getAdater() != null) {
            listView.setAdapter(this.holder.getAdater());
        } else if (this.holder.getType() == 1) {
            return null;
        }
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                ZMAlertDialog.this.holder.getListListener().onClick(ZMAlertDialog.this, i);
                if (ZMAlertDialog.this.holder.getType() == 3) {
                    ((SingleItemAdapter) listView.getAdapter()).setIndex(i);
                    ((SingleItemAdapter) listView.getAdapter()).notifyDataSetChanged();
                } else if (ZMAlertDialog.this.holder.getType() == 2) {
                    ZMAlertDialog.this.dismiss();
                }
            }
        });
        int listDividerHeight = this.holder.getListDividerHeight();
        if (listDividerHeight >= 0) {
            listView.setDividerHeight(listDividerHeight);
        }
        return listView;
    }

    public void setTitle(CharSequence charSequence) {
        this.holder.setTitle(charSequence.toString());
        TextView textView = this.title;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }
}
