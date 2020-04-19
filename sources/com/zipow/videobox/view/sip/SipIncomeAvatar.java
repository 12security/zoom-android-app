package com.zipow.videobox.view.sip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.NosSIPCallItem;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.NameAbbrAvatarDrawable;
import com.zipow.videobox.util.RoundDrawable;
import com.zipow.videobox.util.ZMBitmapFactory;
import com.zipow.videobox.view.IMAddrBookItem;
import java.io.File;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class SipIncomeAvatar extends FrameLayout {
    private static final float BG1_SCALE_START = 0.58f;
    private static final float BG2_SCALE_START = 0.78f;
    private static final float BG_ALPHA_END = 1.0f;
    private static final float BG_ALPHA_START = 0.5f;
    private static final long DURATION = 800;
    public static final int REPEAT_COUNT = 1000;
    private Animation mAnimation1;
    private Animation mAnimation2;
    /* access modifiers changed from: private */
    public View mBg1;
    /* access modifiers changed from: private */
    public View mBg2;
    private String mCallID;
    /* access modifiers changed from: private */
    public int mContentSize = 0;
    /* access modifiers changed from: private */
    public ImageView mIvAvatar;

    public SipIncomeAvatar(@NonNull Context context) {
        super(context);
        initViews(context, null);
    }

    public SipIncomeAvatar(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews(context, attributeSet);
    }

    public SipIncomeAvatar(@NonNull Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context, attributeSet);
    }

    private void initViews(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        setLayerType(1, null);
        inflateLayout();
        this.mContentSize = (int) (((float) getResources().getDimensionPixelSize(C4558R.dimen.zm_sip_income_avatar_content_size)) * getScaleX());
        int i = 0;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4558R.styleable.SipIncomeAvatar);
            i = obtainStyledAttributes.getInt(C4558R.styleable.SipIncomeAvatar_backgroundStyle, 0);
            obtainStyledAttributes.recycle();
        }
        int i2 = C4558R.C4559drawable.zm_sip_income_avatar_onlight_bg1;
        int i3 = C4558R.C4559drawable.zm_sip_income_avatar_onlight_bg2;
        if (i != 0) {
            i2 = C4558R.C4559drawable.zm_sip_income_avatar_ondark_bg1;
            i3 = C4558R.C4559drawable.zm_sip_income_avatar_ondark_bg2;
        }
        this.mBg1 = findViewById(C4558R.C4560id.bg1);
        this.mBg2 = findViewById(C4558R.C4560id.bg2);
        this.mBg1.setBackgroundResource(i2);
        this.mBg2.setBackgroundResource(i3);
        this.mIvAvatar = (ImageView) findViewById(C4558R.C4560id.content);
        this.mAnimation1 = getAnimation1();
        this.mAnimation2 = getAnimation2();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_sip_income_avatar, this);
    }

    public void displayAvatar(@Nullable NosSIPCallItem nosSIPCallItem) {
        if (nosSIPCallItem != null) {
            String fromName = nosSIPCallItem.getFromName();
            ZoomBuddy zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getZoomBuddyByNumber(nosSIPCallItem.getFrom());
            if (zoomBuddyByNumber == null) {
                this.mIvAvatar.setImageDrawable(getEmptyAvatar());
            } else if (!displayAvatar(zoomBuddyByNumber)) {
                if (TextUtils.isEmpty(fromName) || fromName.equals(nosSIPCallItem.getFrom())) {
                    fromName = zoomBuddyByNumber.getScreenName();
                }
                if (TextUtils.isEmpty(fromName)) {
                    fromName = nosSIPCallItem.getFrom();
                }
                this.mIvAvatar.setImageDrawable(getEmptyAvatar(fromName, nosSIPCallItem.getFrom()));
            }
        }
    }

    public void displayAvatar(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mCallID = str;
            CmmSIPCallItem callItemByCallID = CmmSIPCallManager.getInstance().getCallItemByCallID(getCallID());
            if (callItemByCallID != null) {
                String displayName = CmmSIPCallManager.getInstance().getDisplayName(callItemByCallID);
                ZoomBuddy zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getZoomBuddyByNumber(callItemByCallID.getPeerURI());
                if (zoomBuddyByNumber == null) {
                    this.mIvAvatar.setImageDrawable(getEmptyAvatar());
                } else if (!displayAvatar(zoomBuddyByNumber)) {
                    if (TextUtils.isEmpty(displayName) || displayName.equals(callItemByCallID.getPeerURI())) {
                        displayName = zoomBuddyByNumber.getScreenName();
                    }
                    if (TextUtils.isEmpty(displayName)) {
                        displayName = callItemByCallID.getPeerURI();
                    }
                    this.mIvAvatar.setImageDrawable(getEmptyAvatar(displayName, callItemByCallID.getPeerURI()));
                }
            }
        }
    }

    private boolean displayAvatar(@Nullable ZoomBuddy zoomBuddy) {
        IMAddrBookItem fromZoomBuddy = zoomBuddy != null ? IMAddrBookItem.fromZoomBuddy(zoomBuddy) : null;
        if (fromZoomBuddy != null) {
            final Bitmap avatarBitmapFromContact = getAvatarBitmapFromContact(fromZoomBuddy);
            if (avatarBitmapFromContact != null) {
                this.mIvAvatar.post(new Runnable() {
                    public void run() {
                        RoundDrawable roundDrawable = new RoundDrawable(new BitmapDrawable(SipIncomeAvatar.this.getContext().getResources(), Bitmap.createScaledBitmap(avatarBitmapFromContact, SipIncomeAvatar.this.mContentSize, SipIncomeAvatar.this.mContentSize, false)), 0.32f, SipIncomeAvatar.this.getResources().getColor(C4558R.color.zm_white), true, SipIncomeAvatar.this.mContentSize, SipIncomeAvatar.this.mContentSize, SipIncomeAvatar.this.getResources().getDimensionPixelSize(C4558R.dimen.zm_sip_income_avatar_padding));
                        SipIncomeAvatar.this.mIvAvatar.setImageDrawable(roundDrawable);
                    }
                });
                return true;
            }
        }
        return false;
    }

    @Nullable
    private Bitmap getAvatarBitmapFromContact(@Nullable IMAddrBookItem iMAddrBookItem) {
        String str = null;
        if (iMAddrBookItem == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(iMAddrBookItem.getJid());
            if (buddyWithJID != null) {
                str = buddyWithJID.getLocalBigPicturePath();
            }
            if (ImageUtil.isValidImageFile(str)) {
                Bitmap decodeFile = ZMBitmapFactory.decodeFile(str);
                if (decodeFile != null) {
                    return decodeFile;
                }
            } else {
                if (!StringUtil.isEmptyOrNull(str)) {
                    File file = new File(str);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                if (buddyWithJID != null) {
                    str = buddyWithJID.getLocalPicturePath();
                }
                if (ImageUtil.isValidImageFile(str)) {
                    Bitmap decodeFile2 = ZMBitmapFactory.decodeFile(str);
                    if (decodeFile2 != null) {
                        return decodeFile2;
                    }
                }
            }
        }
        return iMAddrBookItem.getAvatarBitmap(getContext());
    }

    private String getCallID() {
        return this.mCallID;
    }

    private Drawable getEmptyAvatar() {
        Drawable drawable = getResources().getDrawable(C4558R.C4559drawable.zm_sip_income_no_avatar);
        int color = getResources().getColor(C4558R.color.zm_white);
        int i = this.mContentSize;
        RoundDrawable roundDrawable = new RoundDrawable(drawable, 0.32f, color, true, i, i, getResources().getDimensionPixelSize(C4558R.dimen.zm_sip_income_avatar_padding));
        return roundDrawable;
    }

    private Drawable getEmptyAvatar(String str, @Nullable String str2) {
        Drawable drawable;
        if (StringUtil.isEmptyOrNull(str)) {
            drawable = getResources().getDrawable(C4558R.C4559drawable.zm_sip_income_no_avatar);
        } else {
            drawable = new NameAbbrAvatarDrawable(str, str2);
        }
        int color = getResources().getColor(C4558R.color.zm_white);
        int i = this.mContentSize;
        RoundDrawable roundDrawable = new RoundDrawable(drawable, 0.32f, color, true, i, i, getResources().getDimensionPixelSize(C4558R.dimen.zm_sip_income_avatar_padding));
        return roundDrawable;
    }

    @NonNull
    private Animation getAnimation1() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setRepeatMode(2);
        animationSet.setDuration(DURATION);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ScaleAnimation scaleAnimation = new ScaleAnimation(BG1_SCALE_START, BG_ALPHA_END, BG1_SCALE_START, BG_ALPHA_END, 1, BG_ALPHA_START, 1, BG_ALPHA_START);
        scaleAnimation.setRepeatCount(1000);
        AlphaAnimation alphaAnimation = new AlphaAnimation(BG_ALPHA_START, BG_ALPHA_END);
        alphaAnimation.setRepeatCount(1000);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setAnimationListener(new AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                SipIncomeAvatar.this.mBg2.clearAnimation();
                SipIncomeAvatar.this.mBg1.clearAnimation();
            }
        });
        return animationSet;
    }

    @NonNull
    private Animation getAnimation2() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setRepeatMode(2);
        animationSet.setDuration(DURATION);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ScaleAnimation scaleAnimation = new ScaleAnimation(BG2_SCALE_START, BG_ALPHA_END, BG2_SCALE_START, BG_ALPHA_END, 1, BG_ALPHA_START, 1, BG_ALPHA_START);
        scaleAnimation.setRepeatCount(1000);
        AlphaAnimation alphaAnimation = new AlphaAnimation(BG_ALPHA_START, BG_ALPHA_END);
        alphaAnimation.setRepeatCount(1000);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setAnimationListener(new AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                SipIncomeAvatar.this.mBg2.clearAnimation();
                SipIncomeAvatar.this.mBg1.clearAnimation();
            }
        });
        return animationSet;
    }

    public void start() {
        this.mBg1.startAnimation(this.mAnimation1);
        this.mBg2.startAnimation(this.mAnimation2);
    }

    public void stop() {
        if (this.mBg1.getAnimation() != null) {
            this.mBg1.getAnimation().cancel();
        }
        if (this.mBg2.getAnimation() != null) {
            this.mBg2.getAnimation().cancel();
        }
    }
}
