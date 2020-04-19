package com.zipow.videobox.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class BigRoundListDialog extends Dialog {
    private ZMListAdapter mAdapter;
    private TextView mBtnClose;
    private String mCloseText;
    private View mContentView;
    /* access modifiers changed from: private */
    public DialogCallback mDialogCallback;
    @Nullable
    private CharSequence mDialogTitle;
    private CharSequence mDianlogSubtitle;
    /* access modifiers changed from: private */
    public boolean mDismissOnItemClicked;
    private ListView mListView;
    /* access modifiers changed from: private */
    public int mMaxHeight;
    private TextView mSubTitleView;
    private TextView mTitleView;

    public interface DialogCallback {
        void onCancel();

        void onClose();

        void onItemSelected(int i);
    }

    public BigRoundListDialog(@NonNull Context context) {
        super(context, C4558R.style.ZMDialog_Material_RoundRect_BigCorners);
        this.mDismissOnItemClicked = true;
        this.mMaxHeight = 500;
        this.mMaxHeight = (int) (((double) getContext().getResources().getDisplayMetrics().heightPixels) * 0.67d);
    }

    public void setTitle(@Nullable CharSequence charSequence) {
        super.setTitle(charSequence);
        this.mDialogTitle = charSequence;
    }

    public void setSubtitle(String str) {
        this.mDianlogSubtitle = str;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().requestFeature(1);
        setContentView(C4558R.layout.zm_big_round_list_dialog);
        this.mContentView = findViewById(C4558R.C4560id.content);
        this.mTitleView = (TextView) findViewById(C4558R.C4560id.tv_title);
        this.mSubTitleView = (TextView) findViewById(C4558R.C4560id.tv_subtitle);
        this.mBtnClose = (TextView) findViewById(C4558R.C4560id.btn_close);
        String str = this.mCloseText;
        if (str != null) {
            this.mBtnClose.setText(str);
            this.mBtnClose.setContentDescription(this.mCloseText);
        }
        this.mBtnClose.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BigRoundListDialog.this.close();
            }
        });
        this.mListView = (ListView) findViewById(C4558R.C4560id.listview);
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (BigRoundListDialog.this.mDialogCallback != null) {
                    BigRoundListDialog.this.mDialogCallback.onItemSelected(i);
                }
                if (BigRoundListDialog.this.mDismissOnItemClicked) {
                    BigRoundListDialog.this.dismiss();
                }
            }
        });
        setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                if (BigRoundListDialog.this.mDialogCallback != null) {
                    BigRoundListDialog.this.mDialogCallback.onCancel();
                }
            }
        });
        this.mTitleView.setText(this.mDialogTitle);
        CharSequence charSequence = this.mDianlogSubtitle;
        if (charSequence == null) {
            this.mSubTitleView.setVisibility(8);
        } else {
            this.mSubTitleView.setText(charSequence);
            this.mSubTitleView.setContentDescription(StringUtil.digitJoin(this.mDianlogSubtitle.toString().split(""), PreferencesConstants.COOKIE_DELIMITER));
            this.mSubTitleView.setVisibility(0);
        }
        ZMListAdapter zMListAdapter = this.mAdapter;
        if (zMListAdapter != null) {
            this.mListView.setAdapter(zMListAdapter);
        }
        initWindow();
    }

    private void initWindow() {
        Window window = getWindow();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (((double) displayMetrics.widthPixels) * 0.9d);
        window.setAttributes(attributes);
        resolveDialogHeight();
    }

    private void resolveDialogHeight() {
        final Window window = getWindow();
        if (window != null) {
            final View decorView = getWindow().getDecorView();
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    int measuredHeight = decorView.getMeasuredHeight();
                    LayoutParams attributes = window.getAttributes();
                    if (measuredHeight > BigRoundListDialog.this.mMaxHeight) {
                        attributes.height = BigRoundListDialog.this.mMaxHeight;
                        window.setAttributes(attributes);
                    }
                    decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    public void setAdapter(ZMListAdapter zMListAdapter) {
        this.mAdapter = zMListAdapter;
    }

    @Nullable
    public ZMListAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setDialogCallback(DialogCallback dialogCallback) {
        this.mDialogCallback = dialogCallback;
    }

    public void setDismissOnItemClicked(boolean z) {
        this.mDismissOnItemClicked = z;
    }

    public void setCloseText(String str) {
        this.mCloseText = str;
        TextView textView = this.mBtnClose;
        if (textView != null) {
            textView.setText(str);
            this.mBtnClose.setContentDescription(str);
        }
    }

    public void setMaxHeight(int i) {
        this.mMaxHeight = i;
    }

    public void invalidate() {
        this.mContentView.postInvalidate();
    }

    public void close() {
        DialogCallback dialogCallback = this.mDialogCallback;
        if (dialogCallback != null) {
            dialogCallback.onClose();
        }
        dismiss();
    }
}
