package com.zipow.videobox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class ZMSearchBar extends LinearLayout implements OnClickListener {
    /* access modifiers changed from: private */
    public Button mClearSearch;
    private boolean mCursorVisible = true;
    @Nullable
    private EditText mEditText;
    private boolean mFocusable = true;
    private int mHintResId = 0;
    private int mImeOptions;
    /* access modifiers changed from: private */
    public OnSearchBarListener mListener;
    private boolean mNoMargin = false;

    public interface OnSearchBarListener {
        void afterTextChanged(@NonNull Editable editable);

        void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3);

        void onClearSearch();

        boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent);

        void onTextChanged(CharSequence charSequence, int i, int i2, int i3);
    }

    public ZMSearchBar(Context context) {
        super(context);
        initViews(context, null);
    }

    public ZMSearchBar(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews(context, attributeSet);
    }

    public ZMSearchBar(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context, attributeSet);
    }

    private void initViews(Context context, @Nullable AttributeSet attributeSet) {
        if (context != null) {
            View.inflate(context, C4558R.layout.zm_im_search_bar, this);
            this.mEditText = (EditText) findViewById(C4558R.C4560id.searchBarEditText);
            this.mClearSearch = (Button) findViewById(C4558R.C4560id.searchBarClearBtn);
            this.mClearSearch.setOnClickListener(this);
            if (attributeSet != null) {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4558R.styleable.ZMSearchBar);
                this.mHintResId = obtainStyledAttributes.getResourceId(C4558R.styleable.ZMSearchBar_editTextHint, 0);
                this.mFocusable = obtainStyledAttributes.getBoolean(C4558R.styleable.ZMSearchBar_focusable, true);
                this.mCursorVisible = obtainStyledAttributes.getBoolean(C4558R.styleable.ZMSearchBar_cursorVisible, true);
                this.mImeOptions = obtainStyledAttributes.getInt(C4558R.styleable.ZMSearchBar_imeOptions, 0);
                this.mNoMargin = obtainStyledAttributes.getBoolean(C4558R.styleable.ZMSearchBar_noMargin, false);
                obtainStyledAttributes.recycle();
                int i = this.mHintResId;
                if (i != 0) {
                    this.mEditText.setHint(i);
                }
                int i2 = this.mImeOptions;
                if (i2 != 0) {
                    this.mEditText.setImeOptions(i2);
                }
                this.mEditText.setCursorVisible(this.mCursorVisible);
            }
            this.mEditText.setOnEditorActionListener(new OnEditorActionListener() {
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (ZMSearchBar.this.mListener != null) {
                        return ZMSearchBar.this.mListener.onEditorAction(textView, i, keyEvent);
                    }
                    return false;
                }
            });
            this.mEditText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    if (ZMSearchBar.this.mListener != null) {
                        ZMSearchBar.this.mListener.beforeTextChanged(charSequence, i, i2, i3);
                    }
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    if (ZMSearchBar.this.mListener != null) {
                        ZMSearchBar.this.mListener.onTextChanged(charSequence, i, i2, i3);
                    }
                }

                public void afterTextChanged(Editable editable) {
                    ZMSearchBar.this.mClearSearch.setVisibility(editable.length() != 0 ? 0 : 8);
                    if (ZMSearchBar.this.mListener != null) {
                        ZMSearchBar.this.mListener.afterTextChanged(editable);
                    }
                }
            });
            this.mEditText.setFocusable(this.mFocusable);
            if (this.mFocusable) {
                this.mEditText.requestFocus();
            } else {
                this.mEditText.setOnClickListener(this);
            }
            if (this.mNoMargin) {
                View findViewById = findViewById(C4558R.C4560id.searchBarLayout);
                if (findViewById != null) {
                    LayoutParams layoutParams = (LayoutParams) findViewById.getLayoutParams();
                    layoutParams.topMargin = 0;
                    layoutParams.bottomMargin = 0;
                    layoutParams.gravity = 16;
                    findViewById.setLayoutParams(layoutParams);
                }
            }
        }
    }

    public boolean hasFocus() {
        EditText editText = this.mEditText;
        if (editText == null) {
            return false;
        }
        return editText.hasFocus();
    }

    public EditText getEditText() {
        return this.mEditText;
    }

    public String getText() {
        EditText editText = this.mEditText;
        if (editText == null) {
            return "";
        }
        return editText.getText().toString();
    }

    public CharSequence getHint() {
        EditText editText = this.mEditText;
        if (editText == null) {
            return "";
        }
        return editText.getHint();
    }

    public void setHint(CharSequence charSequence) {
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setHint(charSequence);
        }
    }

    public void setText(String str) {
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setText(str);
            EditText editText2 = this.mEditText;
            editText2.setSelection(editText2.getText().length());
        }
    }

    public void setImeOptions(int i) {
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setImeOptions(i);
        }
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setEnabled(z);
        }
    }

    public void onClick(View view) {
        if (view == this.mClearSearch) {
            this.mEditText.setText("");
            OnSearchBarListener onSearchBarListener = this.mListener;
            if (onSearchBarListener != null) {
                onSearchBarListener.onClearSearch();
            }
        } else if (view == this.mEditText && !this.mFocusable) {
            performClick();
        }
    }

    public void setOnSearchBarListener(OnSearchBarListener onSearchBarListener) {
        this.mListener = onSearchBarListener;
    }
}
