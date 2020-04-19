package com.zipow.videobox.view;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ConfNumberEditText extends EditText {
    public static final int FORMAT_TYPE_34X = 1;
    public static final int FORMAT_TYPE_43X = 2;
    public static final int FORMAT_TYPE_DEFAULT = 0;
    @NonNull
    private static MyKeyListener mKeyListener = new MyKeyListener();
    private int mFormatType = 0;
    private TextWatcher mTextChangedListener;

    private static class MyKeyListener extends DigitsKeyListener {
        private static final char[] mAcceptedChars = "0123456789 ".toCharArray();

        public MyKeyListener() {
            super(false, false);
        }

        /* access modifiers changed from: protected */
        @NonNull
        public char[] getAcceptedChars() {
            return mAcceptedChars;
        }
    }

    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public ConfNumberEditText(Context context) {
        super(context);
        init();
    }

    public ConfNumberEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public ConfNumberEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void setFormatType(int i) {
        this.mFormatType = i;
        TextWatcher textWatcher = this.mTextChangedListener;
        if (textWatcher != null) {
            removeTextChangedListener(textWatcher);
        }
        formatText(getEditableText());
        TextWatcher textWatcher2 = this.mTextChangedListener;
        if (textWatcher2 != null) {
            addTextChangedListener(textWatcher2);
        }
    }

    public int getFormatType() {
        return this.mFormatType;
    }

    private void init() {
        setKeyListener(mKeyListener);
        this.mTextChangedListener = new TextWatcher() {
            boolean bAppend = false;
            boolean bDelete = false;

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(@NonNull CharSequence charSequence, int i, int i2, int i3) {
                boolean z = true;
                this.bAppend = charSequence.length() == i && i2 == 0;
                if (i2 <= 0 || i3 != 0) {
                    z = false;
                }
                this.bDelete = z;
            }

            public void afterTextChanged(Editable editable) {
                ConfNumberEditText.this.removeTextChangedListener(this);
                int selectionEnd = Selection.getSelectionEnd(editable);
                Editable editableText = ConfNumberEditText.this.getEditableText();
                ConfNumberEditText.this.formatText(editableText);
                int length = editableText.length();
                if (this.bAppend || selectionEnd < 0 || selectionEnd > length) {
                    Selection.setSelection(editableText, length);
                } else {
                    if (!this.bDelete && selectionEnd > 0 && selectionEnd <= editableText.length() && editableText.charAt(selectionEnd - 1) == ' ') {
                        selectionEnd++;
                    }
                    Selection.setSelection(editableText, selectionEnd);
                }
                ConfNumberEditText.this.addTextChangedListener(this);
            }
        };
        addTextChangedListener(this.mTextChangedListener);
    }

    /* access modifiers changed from: private */
    public void formatText(@Nullable Editable editable) {
        if (editable != null) {
            int i = 0;
            int i2 = 0;
            while (i2 < editable.length()) {
                if (!isNumber(editable.charAt(i2))) {
                    editable.delete(i2, i2 + 1);
                    i2--;
                }
                i2++;
            }
            int i3 = 3;
            if (this.mFormatType == 2) {
                i3 = 4;
            }
            int i4 = this.mFormatType;
            int i5 = 8;
            if (!(i4 == 1 || i4 == 2 || editable.length() >= 11)) {
                i5 = 7;
            }
            while (true) {
                if (i >= editable.length()) {
                    break;
                }
                char charAt = editable.charAt(i);
                if (i == editable.length() - 1 && !isNumber(charAt)) {
                    editable.delete(i, i + 1);
                    break;
                }
                if (i == i3 || i == i5) {
                    if (isNumber(charAt)) {
                        editable.insert(i, OAuth.SCOPE_DELIMITER);
                    } else if (charAt != ' ') {
                        editable.replace(i, i + 1, OAuth.SCOPE_DELIMITER);
                    }
                } else if (!isNumber(charAt)) {
                    editable.delete(i, i + 1);
                    i--;
                }
                i++;
            }
        }
    }
}
