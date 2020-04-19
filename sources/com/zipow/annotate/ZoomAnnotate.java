package com.zipow.annotate;

import android.graphics.PointF;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.annotate.render.AnnotateTextData;
import java.util.ArrayList;
import p021us.zoom.androidlib.util.IListener;

public class ZoomAnnotate {
    @NonNull
    public String KEY_TOOL_ALPHA = "KEY_TOOL_ALPHA";
    @NonNull
    public String KEY_TOOL_BOLD = "KEY_TOOL_BOLD";
    @NonNull
    public String KEY_TOOL_COLOR = "KEY_TOOL_COLOR";
    @NonNull
    public String KEY_TOOL_END_X = "KEY_TOOL_END_X";
    @NonNull
    public String KEY_TOOL_END_Y = "KEY_TOOL_END_Y";
    @NonNull
    public String KEY_TOOL_FONT_SIZE = "KEY_TOOL_FONT_SIZE";
    @NonNull
    public String KEY_TOOL_ITALIC = "KEY_TOOL_ITALIC";
    @NonNull
    public String KEY_TOOL_LIST = "KEY_TOOL_LIST";
    @NonNull
    public String KEY_TOOL_START_X = "KEY_TOOL_START_X";
    @NonNull
    public String KEY_TOOL_START_Y = "KEY_TOOL_START_Y";
    @NonNull
    public String KEY_TOOL_TEXT = "KEY_TOOL_TEXT";
    @NonNull
    public String KEY_TOOL_TEXT_SHORT_LIST = "KEY_TOOL_TEXT_SHORT_LIST";
    @NonNull
    public String KEY_TOOL_TYPE = "KEY_TOOL_TYPE";
    @NonNull
    public String KEY_TOOL_WIDTH = "KEY_TOOL_WIDTH";
    @Nullable
    private IZoomAnnotateUIListener mListener;

    public interface IZoomAnnotateUIListener extends IListener {
        void addDrawObjToList(Bundle bundle);

        void beginEditing(int i, int i2);

        void beginPath();

        void clearAndDrawAllPath();

        void closePath();

        void curveToCubicAbs(float f, float f2, float f3, float f4, float f5, float f6);

        void curveToQuadAbs(float f, float f2, float f3, float f4);

        void endEditing();

        void lineToAbs(float f, float f2);

        void moveToAbs(float f, float f2);

        void savePageSnapshot(int i);

        void setAnnoWindow(int i, int i2, float f, float f2, float f3);
    }

    private native float getPrimaryDpiScaleImpl();

    private native void nativeInit();

    private native void setAnnoInfoToNativeImpl(boolean z, boolean z2, long j);

    private native void setIsPresenterImpl(boolean z);

    private native void setIsShareScreenImpl(boolean z);

    private native void setScreenSizeImpl(int i, int i2);

    private native void touchDownImpl(float f, float f2);

    private native void touchMoveImpl(float f, float f2);

    private native void touchUpImpl(float f, float f2);

    public native void editTextDidEndEditingImpl(short[] sArr, AnnotateTextData annotateTextData);

    public void saveContentToPath(String str) {
    }

    public ZoomAnnotate(long j) {
        nativeInit();
    }

    public void setListener(@Nullable IZoomAnnotateUIListener iZoomAnnotateUIListener) {
        this.mListener = iZoomAnnotateUIListener;
    }

    public float getPrimaryDpiScale() {
        return getPrimaryDpiScaleImpl();
    }

    public void setScreenSize(int i, int i2) {
        setScreenSizeImpl(i, i2);
    }

    public void setAnnoInfoToNative(boolean z, boolean z2, long j) {
        setAnnoInfoToNativeImpl(z, z2, j);
    }

    public void touchDown(float f, float f2) {
        touchDownImpl(f, f2);
    }

    public void touchMove(float f, float f2) {
        touchMoveImpl(f, f2);
    }

    public void touchUp(float f, float f2) {
        touchUpImpl(f, f2);
    }

    public void editTextDidEndEditing(short[] sArr, AnnotateTextData annotateTextData) {
        editTextDidEndEditingImpl(sArr, annotateTextData);
    }

    public void setAnnoWindow(int i, int i2, float f, float f2, float f3) {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.setAnnoWindow(i, i2, f, f2, f3);
        }
    }

    public void clear(int i, int i2) {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
    }

    public void drawAnnoPath(int i, int i2, int i3, int i4, ArrayList<PointF> arrayList, float f, float f2) {
        Bundle bundle = new Bundle();
        bundle.putInt(this.KEY_TOOL_TYPE, i4);
        bundle.putInt(this.KEY_TOOL_WIDTH, i);
        bundle.putInt(this.KEY_TOOL_COLOR, i2);
        bundle.putInt(this.KEY_TOOL_ALPHA, i3);
        bundle.putFloat(this.KEY_TOOL_START_X, f);
        bundle.putFloat(this.KEY_TOOL_START_Y, f2);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(arrayList);
        bundle.putParcelableArrayList("KEY_TOOL_LIST", arrayList2);
        notificationAllListener(bundle);
    }

    public void fillAnnoPath(int i, int i2, int i3) {
        Bundle bundle = new Bundle();
        bundle.putInt(this.KEY_TOOL_TYPE, i3);
        bundle.putInt(this.KEY_TOOL_COLOR, i);
        bundle.putInt(this.KEY_TOOL_ALPHA, i2);
        notificationAllListener(bundle);
    }

    public void drawAutoShape(int i, int i2, int i3, float f, float f2, float f3, float f4, int i4) {
        Bundle bundle = new Bundle();
        bundle.putInt(this.KEY_TOOL_TYPE, i4);
        bundle.putInt(this.KEY_TOOL_WIDTH, i);
        bundle.putInt(this.KEY_TOOL_COLOR, i2);
        bundle.putInt(this.KEY_TOOL_ALPHA, i3);
        bundle.putFloat(this.KEY_TOOL_START_X, f);
        bundle.putFloat(this.KEY_TOOL_START_Y, f2);
        bundle.putFloat(this.KEY_TOOL_END_X, f3);
        bundle.putFloat(this.KEY_TOOL_END_Y, f4);
        notificationAllListener(bundle);
    }

    public void drawArrow(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, String str, ArrayList<PointF> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putInt(this.KEY_TOOL_TYPE, AnnoToolType.ANNO_TOOL_TYPE_ARROW.ordinal());
        bundle.putInt(this.KEY_TOOL_WIDTH, i);
        bundle.putInt(this.KEY_TOOL_COLOR, i2);
        bundle.putInt(this.KEY_TOOL_ALPHA, i3);
        bundle.putFloat(this.KEY_TOOL_START_X, (float) i4);
        bundle.putFloat(this.KEY_TOOL_START_Y, (float) i5);
        bundle.putFloat(this.KEY_TOOL_END_X, (float) i6);
        bundle.putFloat(this.KEY_TOOL_END_Y, (float) i7);
        bundle.putInt(this.KEY_TOOL_FONT_SIZE, i8);
        bundle.putString(this.KEY_TOOL_TEXT, str);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(arrayList);
        bundle.putParcelableArrayList("KEY_TOOL_LIST", arrayList2);
        notificationAllListener(bundle);
    }

    public void drawAnnotatorName(int i, int i2, int i3, int i4, int i5, String str, int i6) {
        Bundle bundle = new Bundle();
        bundle.putInt(this.KEY_TOOL_TYPE, AnnoToolType.ANNO_TOOL_TYPE_AUTO_ANNOTATOR_NAME.ordinal());
        bundle.putFloat(this.KEY_TOOL_START_X, (float) i);
        bundle.putFloat(this.KEY_TOOL_START_Y, (float) i2);
        bundle.putFloat(this.KEY_TOOL_END_X, (float) i3);
        bundle.putFloat(this.KEY_TOOL_END_Y, (float) i4);
        bundle.putString(this.KEY_TOOL_TEXT, str);
        bundle.putInt(this.KEY_TOOL_FONT_SIZE, i6);
        bundle.putInt(this.KEY_TOOL_COLOR, i5);
        notificationAllListener(bundle);
    }

    public void drawText(short[] sArr, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, int i6) {
        Bundle bundle = new Bundle();
        bundle.putInt(this.KEY_TOOL_TYPE, AnnoToolType.ANNO_TOOL_TYPE_TEXTBOX.ordinal());
        bundle.putShortArray(this.KEY_TOOL_TEXT_SHORT_LIST, sArr);
        bundle.putInt(this.KEY_TOOL_COLOR, i);
        bundle.putFloat(this.KEY_TOOL_START_X, (float) i2);
        bundle.putFloat(this.KEY_TOOL_START_Y, (float) i3);
        bundle.putFloat(this.KEY_TOOL_END_X, (float) i4);
        bundle.putFloat(this.KEY_TOOL_END_Y, (float) i5);
        bundle.putBoolean(this.KEY_TOOL_BOLD, z);
        bundle.putBoolean(this.KEY_TOOL_ITALIC, z2);
        bundle.putInt(this.KEY_TOOL_FONT_SIZE, i6);
        notificationAllListener(bundle);
    }

    private void notificationAllListener(Bundle bundle) {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.addDrawObjToList(bundle);
        }
    }

    public void clearAndDrawAllPath() {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.clearAndDrawAllPath();
        }
    }

    public void beginEditing(int i, int i2, int i3) {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.beginEditing(i, i2);
        }
    }

    public void endEditing() {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.endEditing();
        }
    }

    public void beginPath() {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.beginPath();
        }
    }

    public void closePath() {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.closePath();
        }
    }

    public void moveToAbs(float f, float f2) {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.moveToAbs(f, f2);
        }
    }

    public void lineToAbs(float f, float f2) {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.lineToAbs(f, f2);
        }
    }

    public void curveToQuadAbs(float f, float f2, float f3, float f4) {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.curveToQuadAbs(f, f2, f3, f4);
        }
    }

    public void curveToCubicAbs(float f, float f2, float f3, float f4, float f5, float f6) {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.curveToCubicAbs(f, f2, f3, f4, f5, f6);
        }
    }

    public void savePageSnapshot(int i) {
        IZoomAnnotateUIListener iZoomAnnotateUIListener = this.mListener;
        if (iZoomAnnotateUIListener != null) {
            iZoomAnnotateUIListener.savePageSnapshot(i);
        }
    }
}
