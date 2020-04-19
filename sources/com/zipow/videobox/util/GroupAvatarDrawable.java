package com.zipow.videobox.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import com.zipow.videobox.VideoBoxApplication;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class GroupAvatarDrawable extends Drawable {
    private int mAlpha = -1;
    private ColorFilter mColorFilter;
    private int mCountMembers;
    @NonNull
    private List<Drawable> mDrawables = new ArrayList();
    @NonNull
    private List<GroupAvatarItem> mNoAvatarItems = new ArrayList();

    public static class GroupAvatarItem {
        public static final int INVALID_CONTACT_ID = -1;
        public String avatar;
        public int contactId = -1;
        public String jid;
        public String name;

        public GroupAvatarItem(String str, String str2, int i, String str3) {
            this.name = str;
            this.avatar = str2;
            this.contactId = i;
            this.jid = str3;
        }
    }

    public int getOpacity() {
        return 0;
    }

    public GroupAvatarDrawable(List<GroupAvatarItem> list) {
        this.mCountMembers = list.size();
        for (int i = 0; i < list.size(); i++) {
            GroupAvatarItem groupAvatarItem = (GroupAvatarItem) list.get(i);
            String str = groupAvatarItem.avatar;
            if ((str == null || str.length() == 0) && groupAvatarItem.contactId < 0) {
                this.mNoAvatarItems.add(groupAvatarItem);
            } else if (str == null || str.length() == 0) {
                Bitmap contactAvatar = ContactsAvatarCache.getInstance().getContactAvatar(VideoBoxApplication.getInstance(), groupAvatarItem.contactId);
                if (contactAvatar != null) {
                    this.mDrawables.add(new BitmapDrawable(VideoBoxApplication.getInstance().getResources(), contactAvatar));
                } else {
                    this.mNoAvatarItems.add(groupAvatarItem);
                }
            } else {
                File file = new File(str);
                if (!file.exists() || !file.isFile()) {
                    this.mNoAvatarItems.add(groupAvatarItem);
                } else {
                    this.mDrawables.add(new LazyLoadDrawable(str));
                    if (this.mDrawables.size() >= this.mCountMembers) {
                        return;
                    }
                }
            }
        }
    }

    public void draw(@NonNull Canvas canvas) {
        if (this.mCountMembers > 0) {
            Rect bounds = getBounds();
            int width = bounds.width();
            VideoBoxApplication instance = VideoBoxApplication.getInstance();
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            int dip2px = UIUtil.dip2px(instance, 1.0f);
            int i = this.mCountMembers;
            if (i == 1) {
                Rect rect = new Rect(0, 0, width, width);
                rect.inset(dip2px, dip2px);
                rect.offset(bounds.left, bounds.top);
                float width2 = ((float) rect.width()) / 2.0f;
                Path path = new Path();
                path.addCircle(((float) rect.left) + width2, ((float) rect.top) + width2, width2, Direction.CCW);
                drawMemberAvatar(instance, this.mDrawables, canvas, paint, 0, path, rect);
            } else if (i == 2) {
                int i2 = width / 4;
                int i3 = width / 2;
                double sqrt = Math.sqrt((double) ((i2 * i2) + (i3 * i3)));
                float f = (float) width;
                RectF rectF = new RectF(0.0f, 0.0f, f, f);
                float f2 = (float) dip2px;
                rectF.inset(f2, f2);
                rectF.offset((float) bounds.left, (float) bounds.top);
                float width3 = rectF.width() / 2.0f;
                Path path2 = new Path();
                path2.arcTo(rectF, 90.0f, 180.0f);
                path2.lineTo(rectF.left + width3, rectF.bottom);
                path2.close();
                double d = (double) (f / 4.0f);
                int i4 = i3;
                Path path3 = path2;
                double d2 = (double) (f / 2.0f);
                Rect rect2 = new Rect((int) (d - sqrt), (int) (d2 - sqrt), (int) (d + sqrt), (int) (d2 + sqrt));
                rect2.offset(bounds.left, bounds.top);
                Rect rect3 = rect2;
                Canvas canvas2 = canvas;
                int i5 = i4;
                Paint paint2 = paint;
                Rect rect4 = rect3;
                int drawMemberAvatar = drawMemberAvatar(instance, this.mDrawables, canvas2, paint2, 0, path3, rect4);
                Path path4 = new Path();
                path4.arcTo(rectF, 270.0f, 180.0f);
                path4.lineTo(rectF.left + width3, rectF.top);
                path4.close();
                rect3.offset(i5, 0);
                int i6 = drawMemberAvatar + 1;
                drawMemberAvatar(instance, this.mDrawables, canvas2, paint2, i6, path4, rect4);
            } else if (i == 3) {
                int i7 = width / 4;
                int i8 = i7 * i7;
                double sqrt2 = Math.sqrt((double) (i8 * 2));
                float f3 = (float) width;
                RectF rectF2 = new RectF(0.0f, 0.0f, f3, f3);
                float f4 = (float) dip2px;
                rectF2.inset(f4, f4);
                rectF2.offset((float) bounds.left, (float) bounds.top);
                float width4 = rectF2.width() / 2.0f;
                Path path5 = new Path();
                path5.arcTo(rectF2, 180.0f, 90.0f);
                path5.lineTo(rectF2.left + width4, rectF2.top + width4);
                path5.lineTo(rectF2.left, rectF2.top + width4);
                path5.close();
                Path path6 = path5;
                double d3 = (double) (f3 / 4.0f);
                int i9 = (int) (d3 - sqrt2);
                int i10 = (int) (d3 + sqrt2);
                Rect rect5 = new Rect(i9, i9, i10, i10);
                rect5.offset(bounds.left, bounds.top);
                Rect rect6 = rect5;
                Canvas canvas3 = canvas;
                Paint paint3 = paint;
                Path path7 = path6;
                float f5 = f3;
                RectF rectF3 = rectF2;
                Rect rect7 = rect6;
                drawMemberAvatar(instance, this.mDrawables, canvas3, paint3, 0, path7, rect7);
                Path path8 = new Path();
                path8.moveTo(rectF3.left, rectF3.top + width4);
                path8.lineTo(rectF3.left + width4, rectF3.top + width4);
                path8.lineTo(rectF3.left + width4, rectF3.bottom);
                path8.arcTo(rectF3, 90.0f, 90.0f);
                path8.close();
                int i11 = width / 2;
                rect6.offset(0, i11);
                int drawMemberAvatar2 = drawMemberAvatar(instance, this.mDrawables, canvas3, paint3, 1, path8, rect7);
                Path path9 = new Path();
                path9.arcTo(rectF3, 270.0f, 180.0f);
                path9.lineTo(rectF3.left + width4, rectF3.top);
                path9.close();
                double sqrt3 = Math.sqrt((double) (i8 + (i11 * i11)));
                double d4 = (double) ((3.0f * f5) / 4.0f);
                double d5 = (double) (f5 / 2.0f);
                VideoBoxApplication videoBoxApplication = instance;
                Rect rect8 = new Rect((int) (d4 - sqrt3), (int) (d5 - sqrt3), (int) (d4 + sqrt3), (int) (d5 + sqrt3));
                rect8.offset(bounds.left, bounds.top);
                int i12 = drawMemberAvatar2 + 1;
                drawMemberAvatar(videoBoxApplication, this.mDrawables, canvas, paint, i12, path9, rect8);
            } else {
                VideoBoxApplication videoBoxApplication2 = instance;
                if (i >= 4) {
                    int i13 = width / 4;
                    double sqrt4 = Math.sqrt((double) (i13 * i13 * 2));
                    float f6 = (float) width;
                    RectF rectF4 = new RectF(0.0f, 0.0f, f6, f6);
                    float f7 = (float) dip2px;
                    rectF4.inset(f7, f7);
                    rectF4.offset((float) bounds.left, (float) bounds.top);
                    float width5 = rectF4.width() / 2.0f;
                    Path path10 = new Path();
                    path10.arcTo(rectF4, 180.0f, 90.0f);
                    path10.lineTo(rectF4.left + width5, rectF4.top + width5);
                    path10.lineTo(rectF4.left, rectF4.top + width5);
                    path10.close();
                    double d6 = (double) (f6 / 4.0f);
                    Path path11 = path10;
                    int i14 = (int) (d6 - sqrt4);
                    int i15 = (int) (d6 + sqrt4);
                    Rect rect9 = new Rect(i14, i14, i15, i15);
                    rect9.offset(bounds.left, bounds.top);
                    Canvas canvas4 = canvas;
                    Paint paint4 = paint;
                    Rect rect10 = rect9;
                    int drawMemberAvatar3 = drawMemberAvatar(videoBoxApplication2, this.mDrawables, canvas4, paint4, 0, path11, rect9);
                    Path path12 = new Path();
                    path12.arcTo(rectF4, 270.0f, 90.0f);
                    path12.lineTo(rectF4.left + width5, rectF4.top + width5);
                    path12.lineTo(rectF4.left + width5, rectF4.top);
                    path12.close();
                    int i16 = width / 2;
                    Rect rect11 = rect10;
                    rect11.offset(i16, 0);
                    int i17 = drawMemberAvatar3 + 1;
                    Rect rect12 = rect11;
                    int i18 = i16;
                    int drawMemberAvatar4 = drawMemberAvatar(videoBoxApplication2, this.mDrawables, canvas4, paint4, i17, path12, rect12);
                    Path path13 = new Path();
                    path13.moveTo(rectF4.left, rectF4.top + width5);
                    path13.lineTo(rectF4.left + width5, rectF4.top + width5);
                    path13.lineTo(rectF4.left + width5, rectF4.bottom);
                    path13.arcTo(rectF4, 90.0f, 90.0f);
                    path13.close();
                    Rect rect13 = rect12;
                    rect13.offset((-width) / 2, i18);
                    int i19 = drawMemberAvatar4 + 1;
                    Rect rect14 = rect13;
                    int drawMemberAvatar5 = drawMemberAvatar(videoBoxApplication2, this.mDrawables, canvas4, paint4, i19, path13, rect14);
                    Path path14 = new Path();
                    path14.moveTo(rectF4.left + width5, rectF4.bottom);
                    path14.lineTo(rectF4.left + width5, rectF4.top + width5);
                    path14.lineTo(rectF4.right, rectF4.top + width5);
                    path14.arcTo(rectF4, 0.0f, 90.0f);
                    path14.close();
                    rect13.offset(i18, 0);
                    int i20 = drawMemberAvatar5 + 1;
                    drawMemberAvatar(videoBoxApplication2, this.mDrawables, canvas4, paint4, i20, path14, rect14);
                }
            }
        }
    }

    private int drawMemberAvatar(@NonNull Context context, List<Drawable> list, @NonNull Canvas canvas, Paint paint, int i, @NonNull Path path, @NonNull Rect rect) {
        Drawable drawable = null;
        if (list.size() > i) {
            Drawable drawable2 = (Drawable) list.get(i);
            if (!(drawable2 instanceof LazyLoadDrawable)) {
                drawable = drawable2;
            } else if (((LazyLoadDrawable) drawable2).isValidDrawable()) {
                drawable = drawable2;
            }
        }
        if (drawable == null) {
            int size = i - list.size();
            if (size < this.mNoAvatarItems.size()) {
                GroupAvatarItem groupAvatarItem = (GroupAvatarItem) this.mNoAvatarItems.get(size);
                drawable = new NameAbbrAvatarDrawable(groupAvatarItem.name, groupAvatarItem.jid);
            } else {
                drawable = context.getResources().getDrawable(C4558R.C4559drawable.zm_no_avatar);
            }
        }
        canvas.save();
        canvas.clipPath(path);
        if (drawable != null) {
            canvas.drawColor(-1);
            drawable.setBounds(rect);
            int i2 = this.mAlpha;
            if (i2 >= 0 && i2 <= 255) {
                drawable.setAlpha(i2);
            }
            ColorFilter colorFilter = this.mColorFilter;
            if (colorFilter != null) {
                drawable.setColorFilter(colorFilter);
            }
            drawable.draw(canvas);
        }
        canvas.restore();
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(-1);
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth((float) UIUtil.dip2px(VideoBoxApplication.getInstance(), 1.0f));
        canvas.drawPath(path, paint2);
        return i;
    }

    public void setAlpha(int i) {
        this.mAlpha = i;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
    }
}
