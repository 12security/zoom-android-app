package com.zipow.videobox.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class ToolbarButton extends LinearLayout {
    private ImageView mImgIcon;
    /* access modifiers changed from: private */
    public OnClickListener mOnClickListener;
    private TextView mTxtNoteBubble;
    private TextView mTxtTitle;

    public ToolbarButton(Context context) {
        this(context, null);
    }

    public ToolbarButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    @SuppressLint({"NewApi"})
    public ToolbarButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(@Nullable Context context, @Nullable AttributeSet attributeSet, int i) {
        View.inflate(getContext(), C4558R.layout.zm_toolbar_button, this);
        this.mImgIcon = (ImageView) findViewById(C4558R.C4560id.icon);
        this.mTxtTitle = (TextView) findViewById(C4558R.C4560id.title);
        this.mTxtNoteBubble = (TextView) findViewById(C4558R.C4560id.txtNoteBubble);
        if (context != null && attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4558R.styleable.ToolbarButton);
            int resourceId = obtainStyledAttributes.getResourceId(C4558R.styleable.ToolbarButton_zm_icon, 0);
            float dimension = obtainStyledAttributes.getDimension(C4558R.styleable.ToolbarButton_zm_textSize, 0.0f);
            if (dimension != 0.0f) {
                this.mTxtTitle.setTextSize(0, dimension);
            }
            float dimension2 = obtainStyledAttributes.getDimension(C4558R.styleable.ToolbarButton_zm_vertical_divide_icon_text, 0.0f);
            if (dimension2 != 0.0f) {
                LayoutParams layoutParams = (LayoutParams) this.mImgIcon.getLayoutParams();
                layoutParams.bottomMargin = (int) dimension2;
                this.mImgIcon.setLayoutParams(layoutParams);
            }
            setImageResource(resourceId);
            boolean z = obtainStyledAttributes.getBoolean(C4558R.styleable.ToolbarButton_zm_titleSingleLine, true);
            this.mTxtTitle.setSingleLine(z);
            if (!z) {
                this.mTxtTitle.setMaxLines(obtainStyledAttributes.getInteger(C4558R.styleable.ToolbarButton_zm_titleLines, 1));
            }
            float dimension3 = obtainStyledAttributes.getDimension(C4558R.styleable.ToolbarButton_zm_titlePaddingLeftRight, 0.0f);
            if (dimension3 != 0.0f) {
                int i2 = (int) dimension3;
                this.mTxtTitle.setPadding(i2, 0, i2, 0);
            }
            setText((CharSequence) obtainStyledAttributes.getString(C4558R.styleable.ToolbarButton_zm_text));
            if (obtainStyledAttributes.hasValue(C4558R.styleable.ToolbarButton_zm_textColor)) {
                ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(C4558R.styleable.ToolbarButton_zm_textColor);
                if (colorStateList != null) {
                    this.mTxtTitle.setTextColor(colorStateList);
                } else {
                    this.mTxtTitle.setTextColor(obtainStyledAttributes.getColor(C4558R.styleable.ToolbarButton_zm_textColor, 0));
                }
            }
            obtainStyledAttributes.recycle();
            super.setOnClickListener(new OnClickListener() {
                private long mLastClickTime = 0;

                public void onClick(View view) {
                    if (ToolbarButton.this.mOnClickListener != null) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (elapsedRealtime - this.mLastClickTime > 500) {
                            ToolbarButton.this.mOnClickListener.onClick(view);
                        }
                        this.mLastClickTime = elapsedRealtime;
                    }
                }
            });
            setContentDescription(getResources().getString(C4558R.string.zm_accessibility_button_99142, new Object[]{this.mTxtTitle.getText()}));
        }
    }

    public void setText(@Nullable CharSequence charSequence) {
        if (this.mTxtTitle == null) {
            return;
        }
        if (charSequence == null || charSequence.length() == 0) {
            this.mTxtTitle.setVisibility(8);
        } else {
            this.mTxtTitle.setText(charSequence);
        }
    }

    public void setText(int i) {
        TextView textView = this.mTxtTitle;
        if (textView == null) {
            return;
        }
        if (i <= 0) {
            textView.setVisibility(8);
        } else {
            textView.setText(i);
        }
    }

    public void setTextColor(ColorStateList colorStateList) {
        TextView textView = this.mTxtTitle;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public void setTextStyle(int i) {
        TextView textView = this.mTxtTitle;
        if (textView != null) {
            textView.setTypeface(null, i);
        }
    }

    public void setImageResource(int i) {
        ImageView imageView = this.mImgIcon;
        if (imageView != null) {
            imageView.setImageResource(i);
        }
    }

    public void setIconBackgroundResource(int i) {
        ImageView imageView = this.mImgIcon;
        if (imageView != null) {
            imageView.setBackgroundResource(i);
        }
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        ImageView imageView = this.mImgIcon;
        if (imageView != null) {
            imageView.setPadding(i, i2, i3, i4);
        }
    }

    public void setIconSize(int i, int i2) {
        ImageView imageView = this.mImgIcon;
        if (imageView != null) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = i;
            layoutParams.height = i2;
            this.mImgIcon.setLayoutParams(layoutParams);
        }
    }

    public void setIconScaleType(ScaleType scaleType) {
        ImageView imageView = this.mImgIcon;
        if (imageView != null) {
            imageView.setScaleType(scaleType);
        }
    }

    public void setNoteMessage(@Nullable CharSequence charSequence) {
        TextView textView = this.mTxtNoteBubble;
        if (textView != null) {
            textView.setText(charSequence);
            if (charSequence == null || charSequence.length() == 0) {
                this.mTxtNoteBubble.setVisibility(8);
            } else {
                this.mTxtNoteBubble.setVisibility(0);
                this.mTxtNoteBubble.setContentDescription(getContext().getResources().getString(C4558R.string.zm_accessibility_unread_message_19147, new Object[]{charSequence}));
            }
        }
    }

    public void setNoteMessage(int i) {
        TextView textView = this.mTxtNoteBubble;
        if (textView != null) {
            if (i <= 0) {
                textView.setVisibility(8);
            } else {
                textView.setVisibility(0);
                this.mTxtNoteBubble.setText(i > 99 ? "99+" : String.valueOf(i));
            }
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
