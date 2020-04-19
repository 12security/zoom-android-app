package com.zipow.videobox.view.sip;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class DialPadNumView extends LinearLayout {
    private String mDialKey;
    private ImageView mImgNum;
    private ImageView mImgNumDes;
    private boolean mIsOnDark;
    private TextView mTxtNum;
    private TextView mTxtNumDes;

    public DialPadNumView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public DialPadNumView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public DialPadNumView(Context context) {
        super(context);
        init(context, null);
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        findViewById(C4558R.C4560id.panelKey).setEnabled(z);
    }

    public void setOnDrakMode() {
        this.mIsOnDark = true;
        this.mTxtNum.setTextColor(getResources().getColor(C4558R.color.zm_white));
        this.mTxtNumDes.setTextColor(getResources().getColor(C4558R.color.zm_white));
        String str = this.mDialKey;
        if (str != null) {
            setDialKey(str);
        }
    }

    private void init(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        if (context != null) {
            View.inflate(getContext(), C4558R.layout.zm_sip_dialpad_num, this);
            this.mTxtNum = (TextView) findViewById(C4558R.C4560id.txtNum);
            this.mTxtNumDes = (TextView) findViewById(C4558R.C4560id.txtNumDes);
            this.mImgNum = (ImageView) findViewById(C4558R.C4560id.imgNum);
            this.mImgNumDes = (ImageView) findViewById(C4558R.C4560id.imgNumDes);
            if (!isInEditMode() && attributeSet != null) {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4558R.styleable.DialpadNum);
                setDialKey(obtainStyledAttributes.getString(C4558R.styleable.DialpadNum_zm_dial_num));
                setContentDescription(obtainStyledAttributes.getString(C4558R.styleable.DialpadNum_zm_dial_num));
                obtainStyledAttributes.recycle();
            }
        }
    }

    public String getDialKey() {
        return this.mDialKey;
    }

    public void setDialKey(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str) && str.length() == 1) {
            char charAt = str.charAt(0);
            StringBuilder sb = new StringBuilder();
            sb.append(charAt);
            sb.append("");
            this.mDialKey = sb.toString();
            this.mTxtNumDes.setVisibility(0);
            if (charAt == '#') {
                this.mTxtNum.setVisibility(8);
                this.mImgNum.setImageResource(C4558R.C4559drawable.zm_keyboard_digit_no);
                setContentDescription(getResources().getString(C4558R.string.zm_sip_accessbility_keypad_pound_61381));
                this.mImgNum.setVisibility(0);
                this.mImgNumDes.setVisibility(8);
                this.mImgNumDes.setVisibility(8);
            } else if (charAt != '*') {
                switch (charAt) {
                    case '0':
                        this.mTxtNum.setText("0");
                        this.mTxtNumDes.setVisibility(8);
                        this.mImgNumDes.setVisibility(0);
                        this.mImgNumDes.setImageResource(C4558R.C4559drawable.zm_keyboard_digit_add);
                        break;
                    case '1':
                        this.mTxtNum.setText("1");
                        this.mTxtNumDes.setText("");
                        break;
                    case '2':
                        this.mTxtNum.setText("2");
                        this.mTxtNumDes.setText("A B C");
                        break;
                    case '3':
                        this.mTxtNum.setText(ExifInterface.GPS_MEASUREMENT_3D);
                        this.mTxtNumDes.setText("D E F");
                        break;
                    case '4':
                        this.mTxtNum.setText("4");
                        this.mTxtNumDes.setText("G H I");
                        break;
                    case '5':
                        this.mTxtNum.setText("5");
                        this.mTxtNumDes.setText("J K L");
                        break;
                    case '6':
                        this.mTxtNum.setText("6");
                        this.mTxtNumDes.setText("M N O");
                        break;
                    case '7':
                        this.mTxtNum.setText("7");
                        this.mTxtNumDes.setText("P Q R S");
                        break;
                    case '8':
                        this.mTxtNum.setText("8");
                        this.mTxtNumDes.setText("T U V");
                        break;
                    case '9':
                        this.mTxtNum.setText("9");
                        this.mTxtNumDes.setText("W X Y Z");
                        break;
                }
            } else {
                this.mTxtNum.setVisibility(8);
                this.mImgNum.setImageResource(C4558R.C4559drawable.zm_keyboard_digit_star);
                setContentDescription(getResources().getString(C4558R.string.zm_sip_accessbility_keypad_star_61381));
                this.mImgNum.setVisibility(0);
                this.mImgNumDes.setVisibility(8);
                this.mImgNumDes.setVisibility(8);
            }
        }
    }
}
