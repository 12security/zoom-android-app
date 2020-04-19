package p021us.zoom.androidlib;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import p021us.zoom.androidlib.widget.ZMTextButton;

/* renamed from: us.zoom.androidlib.ZMMaterialActionButtonLayout */
public class ZMMaterialActionButtonLayout extends RelativeLayout implements OnClickListener {
    private MaterialActionButtonCallBack mMaterialActionButtonCallBack;
    ZMTextButton mTxtNegative;
    ZMTextButton mTxtNeutral;
    ZMTextButton mTxtPositive;

    /* renamed from: us.zoom.androidlib.ZMMaterialActionButtonLayout$MaterialActionButtonCallBack */
    public interface MaterialActionButtonCallBack {
        void onClickNegative();

        void onClickNeutral();

        void onClickPositive();
    }

    public ZMMaterialActionButtonLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    public ZMMaterialActionButtonLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public ZMMaterialActionButtonLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        if (context != null && attributeSet != null) {
            LayoutInflater.from(context).inflate(C4409R.layout.zm_material_action_button_layout, this, true);
            this.mTxtNeutral = (ZMTextButton) findViewById(C4409R.C4411id.txtNeutral);
            this.mTxtNegative = (ZMTextButton) findViewById(C4409R.C4411id.txtNegative);
            this.mTxtPositive = (ZMTextButton) findViewById(C4409R.C4411id.txtPositive);
            this.mTxtNeutral.setOnClickListener(this);
            this.mTxtNegative.setOnClickListener(this);
            this.mTxtPositive.setOnClickListener(this);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMMaterialActionButtonLayout);
            if (obtainStyledAttributes != null) {
                Resources resources = getResources();
                this.mTxtNeutral.setTextColor(obtainStyledAttributes.getColor(C4409R.styleable.ZMMaterialActionButtonLayout_zm_text_color_neutral, resources.getColor(C4409R.color.zm_white)));
                this.mTxtNegative.setTextColor(obtainStyledAttributes.getColor(C4409R.styleable.ZMMaterialActionButtonLayout_zm_text_color_negative, resources.getColor(C4409R.color.zm_white)));
                this.mTxtPositive.setTextColor(obtainStyledAttributes.getColor(C4409R.styleable.ZMMaterialActionButtonLayout_zm_text_color_positive, resources.getColor(C4409R.color.zm_white)));
                int i2 = 8;
                this.mTxtNeutral.setVisibility(obtainStyledAttributes.getBoolean(C4409R.styleable.ZMMaterialActionButtonLayout_zm_visible_neutral, false) ? 0 : 8);
                this.mTxtNegative.setVisibility(obtainStyledAttributes.getBoolean(C4409R.styleable.ZMMaterialActionButtonLayout_zm_visible_negative, true) ? 0 : 8);
                ZMTextButton zMTextButton = this.mTxtPositive;
                if (obtainStyledAttributes.getBoolean(C4409R.styleable.ZMMaterialActionButtonLayout_zm_visible_positive, true)) {
                    i2 = 0;
                }
                zMTextButton.setVisibility(i2);
                String string = obtainStyledAttributes.getString(C4409R.styleable.ZMMaterialActionButtonLayout_zm_text_neutral);
                if (string != null) {
                    this.mTxtNeutral.setText(string);
                }
                String string2 = obtainStyledAttributes.getString(C4409R.styleable.ZMMaterialActionButtonLayout_zm_text_negative);
                if (string2 != null) {
                    this.mTxtNegative.setText(string2);
                }
                String string3 = obtainStyledAttributes.getString(C4409R.styleable.ZMMaterialActionButtonLayout_zm_text_positive);
                if (string3 != null) {
                    this.mTxtPositive.setText(string3);
                }
                float dimensionPixelSize = (float) obtainStyledAttributes.getDimensionPixelSize(C4409R.styleable.ZMMaterialActionButtonLayout_zm_text_size, -1);
                if (dimensionPixelSize > 0.0f) {
                    this.mTxtNeutral.setTextSize(0, dimensionPixelSize);
                    this.mTxtNegative.setTextSize(0, dimensionPixelSize);
                    this.mTxtPositive.setTextSize(0, dimensionPixelSize);
                }
                obtainStyledAttributes.recycle();
            }
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C4409R.C4411id.txtNeutral) {
            MaterialActionButtonCallBack materialActionButtonCallBack = this.mMaterialActionButtonCallBack;
            if (materialActionButtonCallBack != null) {
                materialActionButtonCallBack.onClickNeutral();
            }
        } else if (id == C4409R.C4411id.txtNegative) {
            MaterialActionButtonCallBack materialActionButtonCallBack2 = this.mMaterialActionButtonCallBack;
            if (materialActionButtonCallBack2 != null) {
                materialActionButtonCallBack2.onClickNegative();
            }
        } else if (id == C4409R.C4411id.txtPositive) {
            MaterialActionButtonCallBack materialActionButtonCallBack3 = this.mMaterialActionButtonCallBack;
            if (materialActionButtonCallBack3 != null) {
                materialActionButtonCallBack3.onClickPositive();
            }
        }
    }

    public void setmMaterialActionButtonCallBack(MaterialActionButtonCallBack materialActionButtonCallBack) {
        this.mMaterialActionButtonCallBack = materialActionButtonCallBack;
    }
}
