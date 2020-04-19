package com.zipow.videobox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.ImageLoader;
import com.zipow.videobox.util.RoundDrawable;
import com.zipow.videobox.util.zmurl.avatar.ZMAvatarCornerParams;
import java.io.File;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class AvatarView extends LinearLayout {
    private static final float CORNER_RADIUS_RATIO_DEF_VALUE = 0.32f;
    private static final String TAG = "AvatarView";
    @Nullable
    private String mBgColorSeedString = null;
    private int mBorderColor;
    private int mBorderSize;
    private float mCornerRadiusRatio = 0.0f;
    private ImageView mImgAvatar;
    private boolean mIsEmptyAvatar = true;
    private boolean mIsRoom = false;
    private boolean mIsRoomDevice = false;
    @Nullable
    private String mName = null;
    private boolean mbContentDescriptionEnable = true;

    public static class ParamsBuilder {
        /* access modifiers changed from: private */
        public String bgColorSeedString;
        /* access modifiers changed from: private */
        public String localPath;
        /* access modifiers changed from: private */
        public String name;
        /* access modifiers changed from: private */
        public int resourceId = -1;

        public ParamsBuilder setPath(String str) {
            this.localPath = str;
            return this;
        }

        public ParamsBuilder setName(String str, String str2) {
            this.name = str;
            this.bgColorSeedString = str2;
            return this;
        }

        public ParamsBuilder setResource(int i, String str) {
            this.resourceId = i;
            this.bgColorSeedString = str;
            return this;
        }
    }

    public AvatarView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context, attributeSet);
    }

    public AvatarView(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        setLayerType(1, null);
        View.inflate(getContext(), C4558R.layout.zm_avatar, this);
        this.mImgAvatar = (ImageView) findViewById(C4558R.C4560id.imgAvator);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4558R.styleable.AvatarView);
        this.mCornerRadiusRatio = obtainStyledAttributes.getFloat(C4558R.styleable.AvatarView_zm_cornerRadiusRatio, CORNER_RADIUS_RATIO_DEF_VALUE);
        this.mBorderColor = obtainStyledAttributes.getColor(C4558R.styleable.AvatarView_zm_avatarBorderColor, getResources().getColor(C4558R.color.zm_ui_kit_color_gray_E5E5ED));
        this.mBorderSize = Float.valueOf(obtainStyledAttributes.getDimension(C4558R.styleable.AvatarView_zm_avatarBorderSize, (float) UIUtil.dip2px(VideoBoxApplication.getInstance(), 0.5f))).intValue();
        this.mbContentDescriptionEnable = obtainStyledAttributes.getBoolean(C4558R.styleable.AvatarView_zm_description_enable, true);
        obtainStyledAttributes.recycle();
    }

    public void setCornerRadiusRatio(float f) {
        this.mCornerRadiusRatio = f;
        if (isShown()) {
            invalidate();
        }
    }

    public void setBorderColor(int i) {
        this.mBorderColor = i;
        Drawable drawable = this.mImgAvatar.getDrawable();
        if (drawable instanceof RoundDrawable) {
            ((RoundDrawable) drawable).setBorderColor(this.mBorderColor);
        }
        if (isShown()) {
            invalidate();
        }
    }

    public void setBorderSize(int i) {
        this.mBorderSize = i;
        Drawable drawable = this.mImgAvatar.getDrawable();
        if (drawable instanceof RoundDrawable) {
            ((RoundDrawable) drawable).setBorderSize(i);
        }
        if (isShown()) {
            invalidate();
        }
    }

    private boolean hasRoundCorner() {
        return ((int) (this.mCornerRadiusRatio * 1000.0f)) > 0;
    }

    public void setNameIcon(@NonNull String str, String str2) {
        if (!hasRoundCorner()) {
            setCornerRadiusRatio(CORNER_RADIUS_RATIO_DEF_VALUE);
        }
        if (this.mbContentDescriptionEnable) {
            setContentDescription(str);
        }
        ImageLoader.getInstance().displayAvatar(this.mImgAvatar, str, str2, getCornerParams(), this.mImgAvatar.getDrawable());
    }

    private void setLocalImgPath(@NonNull String str, String str2, String str3) {
        if (!hasRoundCorner()) {
            setCornerRadiusRatio(CORNER_RADIUS_RATIO_DEF_VALUE);
        }
        File file = new File(str);
        String valueOf = file.exists() ? String.valueOf(file.lastModified()) : null;
        if (this.mbContentDescriptionEnable && !TextUtils.isEmpty(str2)) {
            setContentDescription(str2);
        }
        ImageLoader.getInstance().displayAvatar(this.mImgAvatar, str, str2, str3, getCornerParams(), this.mImgAvatar.getDrawable(), valueOf);
    }

    private void setResource(int i) {
        setResource(i, null, false);
    }

    private void setResource(int i, String str, boolean z) {
        if (z) {
            setCornerRadiusRatio(CORNER_RADIUS_RATIO_DEF_VALUE);
        } else {
            setCornerRadiusRatio(0.0f);
        }
        ImageLoader.getInstance().displayAvatar(this.mImgAvatar, i, str, getCornerParams(), this.mImgAvatar.getDrawable());
    }

    private ZMAvatarCornerParams getCornerParams() {
        if (!hasRoundCorner()) {
            return null;
        }
        int height = getHeight();
        int width = getWidth();
        ZMAvatarCornerParams zMAvatarCornerParams = new ZMAvatarCornerParams(this.mCornerRadiusRatio, this.mBorderColor, true, (width != 0 || getLayoutParams() == null) ? width : getLayoutParams().width, (height != 0 || getLayoutParams() == null) ? height : getLayoutParams().height, this.mBorderSize);
        return zMAvatarCornerParams;
    }

    public void show(ParamsBuilder paramsBuilder) {
        if (paramsBuilder == null) {
            setCornerRadiusRatio(0.0f);
            setResource(C4558R.C4559drawable.zm_no_avatar);
            return;
        }
        if (paramsBuilder.resourceId != -1) {
            if (!TextUtils.isEmpty(paramsBuilder.bgColorSeedString)) {
                setResource(paramsBuilder.resourceId, paramsBuilder.bgColorSeedString, true);
            } else {
                setCornerRadiusRatio(0.0f);
                setResource(paramsBuilder.resourceId);
            }
        } else if (!TextUtils.isEmpty(paramsBuilder.localPath)) {
            setLocalImgPath(paramsBuilder.localPath, paramsBuilder.name, paramsBuilder.bgColorSeedString);
        } else if (!TextUtils.isEmpty(paramsBuilder.name)) {
            setNameIcon(paramsBuilder.name, paramsBuilder.bgColorSeedString);
        } else {
            setCornerRadiusRatio(0.0f);
            setResource(C4558R.C4559drawable.zm_no_avatar);
        }
    }
}
