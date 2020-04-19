package com.zipow.annotate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.annotate.ZoomAnnotate.IZoomAnnotateUIListener;
import com.zipow.annotate.render.AnnoDrawObj;
import com.zipow.annotate.render.AnnotateTextData;
import com.zipow.annotate.render.ArrowCtl;
import com.zipow.annotate.render.AutoArrow;
import com.zipow.annotate.render.AutoShapeCtl;
import com.zipow.annotate.render.EraserCtl;
import com.zipow.annotate.render.FillPathCtl;
import com.zipow.annotate.render.ISAnnotateDraw;
import com.zipow.annotate.render.PenCtl;
import com.zipow.annotate.render.PolygonCtl;
import com.zipow.annotate.render.TextCtl;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.share.IDrawingViewListener;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.UiModeUtil;
import p021us.zoom.videomeetings.C4558R;

public class AnnotateView extends SurfaceView implements Callback, IZoomAnnotateUIListener {
    private static final String TAG = "com.zipow.annotate.AnnotateView";
    private AnnoDataMgr mAnnoDataMgr = AnnoDataMgr.getInstance();
    @Nullable
    private Path mAnnoFillPath = null;
    private boolean mBEditStatus = false;
    @Nullable
    private Canvas mBackgroundCanvas;
    @Nullable
    private Bitmap mBackgroundImage = null;
    @NonNull
    private Paint mBitmapPaint = new Paint(1);
    @Nullable
    private Bitmap mCachedImage = null;
    private float mCurX = 0.0f;
    private float mCurY = 0.0f;
    @Nullable
    private Runnable mDrawRunnable;
    private float mEndPosX = 0.0f;
    private float mEndPosY = 0.0f;
    @Nullable
    private GestureDetector mGestureDetector;
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mIsUp = true;
    @Nullable
    private IDrawingViewListener mListener = null;
    @Nullable
    private ISAnnotateDraw mLocalAnnoTool = null;
    @Nullable
    private ISAnnotateDraw mRemoteTool = null;
    @Nullable
    private Bitmap mSpolightImage = null;
    private float mStartPosX = 0.0f;
    private float mStartPosY = 0.0f;
    private int mStatusBarHeight = 0;
    @Nullable
    private SurfaceHolder mSurfaceHolder = null;
    private float mZoomFactor = 1.0f;
    @NonNull
    private List<AnnoDrawObj> m_annoDrawObjList = new ArrayList();

    private class GuestureListener extends SimpleOnGestureListener {
        public GuestureListener() {
        }

        public void onLongPress(MotionEvent motionEvent) {
            AnnotateView.this.onLongPressed(true);
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return AnnotateView.this.onLongPressed(false);
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return AnnotateView.this.onLongPressed(false);
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        drawFaceView();
        onRepaint();
        onLongPressed(false);
    }

    public AnnotateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public AnnotateView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        initRunnable();
        this.mSurfaceHolder = getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setFormat(-2);
        this.mSpolightImage = BitmapFactory.decodeResource(getResources(), C4558R.C4559drawable.zm_share_spot);
        setZOrderOnTop(true);
        this.mGestureDetector = new GestureDetector(context, new GuestureListener());
        this.mGestureDetector.setIsLongpressEnabled(true);
        int max = Math.max(UIUtil.getDisplayWidth(context), UIUtil.getDisplayHeight(context));
        initBackgroundCanvasSize(max, max);
        ZoomAnnotate zoomAnnotateMgr = ConfMgr.getInstance().getZoomAnnotateMgr();
        if (zoomAnnotateMgr != null) {
            zoomAnnotateMgr.setListener(this);
        }
    }

    private void initRunnable() {
        this.mDrawRunnable = new Runnable() {
            public void run() {
                AnnotateView.this.clear();
            }
        };
    }

    private void startTimer(Runnable runnable, int i) {
        this.mHandler.removeCallbacks(runnable);
        this.mHandler.postDelayed(runnable, (long) i);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (UiModeUtil.isInDesktopMode(getContext())) {
            initBackgroundCanvasSize(i3 - i, i4 - i2);
        }
    }

    public void startAnnotation() {
        int max = Math.max(UIUtil.getDisplayWidth(getContext()), UIUtil.getDisplayHeight(getContext()));
        initBackgroundCanvasSize(max, max);
        ZoomAnnotate zoomAnnotateMgr = ConfMgr.getInstance().getZoomAnnotateMgr();
        if (zoomAnnotateMgr != null) {
            this.mStatusBarHeight = this.mAnnoDataMgr.isShareScreen() ? getStatusBarHeight() : 0;
            zoomAnnotateMgr.setListener(this);
            clear();
        }
    }

    public void stopAnnotation() {
        setEditModel(false);
        clear();
        destroyBackgroundCanvas();
        this.m_annoDrawObjList.clear();
    }

    private void destroyBackgroundCanvas() {
        Bitmap bitmap = this.mBackgroundImage;
        if (bitmap != null) {
            bitmap.recycle();
            this.mBackgroundImage = null;
        }
        this.mBackgroundCanvas = null;
    }

    public void registerUpdateListener(@Nullable IDrawingViewListener iDrawingViewListener) {
        this.mListener = iDrawingViewListener;
    }

    public void unRegisterUpdateListener(@Nullable IDrawingViewListener iDrawingViewListener) {
        if (iDrawingViewListener != null) {
            this.mListener = null;
        }
    }

    public void initBackgroundCanvasSize(int i, int i2) {
        if (i > 0 && i <= 5000 && i2 > 0 && i2 <= 5000) {
            Bitmap bitmap = this.mBackgroundImage;
            if (bitmap != null) {
                if (bitmap.getWidth() != i || this.mBackgroundImage.getHeight() != i2) {
                    destroyBackgroundCanvas();
                } else {
                    return;
                }
            }
            if (this.mBackgroundImage == null) {
                try {
                    this.mBackgroundImage = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    this.mBackgroundCanvas = new Canvas(this.mBackgroundImage);
                } catch (OutOfMemoryError unused) {
                }
            }
        }
    }

    public void editTextDidEndEditing(short[] sArr, AnnotateTextData annotateTextData) {
        ZoomAnnotate zoomAnnotateMgr = ConfMgr.getInstance().getZoomAnnotateMgr();
        if (zoomAnnotateMgr != null) {
            zoomAnnotateMgr.editTextDidEndEditing(sArr, annotateTextData);
        }
    }

    public void setCachedImage(Bitmap bitmap) {
        this.mCachedImage = bitmap;
    }

    public void drawShareContent(@Nullable Canvas canvas) {
        if (canvas != null) {
            Bitmap bitmap = this.mBackgroundImage;
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
                if (this.mAnnoDataMgr.getTool() == AnnoToolType.ANNO_TOOL_TYPE_SPOTLIGHT) {
                    float f = this.mCurX;
                    if (f != 0.0f) {
                        float f2 = this.mCurY;
                        if (f2 != 0.0f) {
                            Bitmap bitmap2 = this.mSpolightImage;
                            if (bitmap2 != null) {
                                canvas.drawBitmap(bitmap2, f, f2, null);
                            }
                        }
                    }
                }
            }
        }
    }

    public void onClearClicked() {
        this.mCurX = 0.0f;
        this.mCurY = 0.0f;
    }

    private void onRepaint() {
        if (this.mListener != null && this.mAnnoDataMgr.isPresenter()) {
            this.mListener.onRepaint();
        }
    }

    public void setEditModel(boolean z) {
        this.mBEditStatus = z;
        if (!this.mBEditStatus && !this.mAnnoDataMgr.isPresenter()) {
            clear();
        }
    }

    /* access modifiers changed from: private */
    public boolean onLongPressed(boolean z) {
        if (AnnoToolType.ANNO_TOOL_TYPE_TEXTBOX != this.mAnnoDataMgr.getTool()) {
            IDrawingViewListener iDrawingViewListener = this.mListener;
            if (iDrawingViewListener != null) {
                iDrawingViewListener.onLongPressed(z);
                return true;
            }
        }
        return false;
    }

    private void dismissAllTip() {
        IDrawingViewListener iDrawingViewListener = this.mListener;
        if (iDrawingViewListener != null) {
            iDrawingViewListener.onDismissAllTip();
        }
    }

    private int getStatusBarHeight() {
        int identifier = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return getContext().getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        getDrawingRect(new Rect(0, 0, 0, 0));
        if (!this.mBEditStatus) {
            return false;
        }
        this.mCurX = motionEvent.getX();
        this.mCurY = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                touchDown();
                break;
            case 1:
                touchUp();
                break;
            case 2:
                touchMove(motionEvent);
                break;
        }
        drawFaceView();
        GestureDetector gestureDetector = this.mGestureDetector;
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(motionEvent);
        }
        return true;
    }

    private void touchDown() {
        dismissAllTip();
        AnnoToolType tool = this.mAnnoDataMgr.getTool();
        ZoomAnnotate zoomAnnotateMgr = ConfMgr.getInstance().getZoomAnnotateMgr();
        if (zoomAnnotateMgr != null && AnnoToolType.ANNO_TOOL_TYPE_SPOTLIGHT != tool) {
            if (this.mBackgroundCanvas == null) {
                int max = Math.max(UIUtil.getDisplayWidth(getContext()), UIUtil.getDisplayHeight(getContext()));
                initBackgroundCanvasSize(max, max);
            }
            updateLocalTool(zoomAnnotateMgr.getPrimaryDpiScale());
            float f = this.mCurX;
            this.mStartPosX = f;
            float f2 = this.mCurY;
            this.mStartPosY = f2;
            this.mEndPosX = f;
            this.mEndPosY = f2;
            if (tool != AnnoToolType.ANNO_TOOL_TYPE_ERASER && this.mAnnoDataMgr.isShareScreen()) {
                this.mAnnoDataMgr.saveLocalDrawing(this.mStartPosX, this.mStartPosY);
            }
            zoomAnnotateMgr.touchDown(this.mStartPosX, this.mStartPosY);
            if (!(this.mLocalAnnoTool == null || AnnoToolType.ANNO_TOOL_TYPE_TEXTBOX == tool)) {
                this.mLocalAnnoTool.touchDown(this.mCurX, this.mCurY);
            }
        }
    }

    private void touchMove(@NonNull MotionEvent motionEvent) {
        AnnoToolType tool = this.mAnnoDataMgr.getTool();
        ZoomAnnotate zoomAnnotateMgr = ConfMgr.getInstance().getZoomAnnotateMgr();
        if (zoomAnnotateMgr != null && this.mLocalAnnoTool != null && AnnoToolType.ANNO_TOOL_TYPE_SPOTLIGHT != tool) {
            if (!this.mBEditStatus) {
                touchUp();
            }
            if (this.mAnnoDataMgr.isPresenter()) {
                float dip2px = (float) UIUtil.dip2px(getContext(), 10.0f);
                if (Math.abs(this.mCurX - this.mStartPosX) >= dip2px || Math.abs(this.mCurY - this.mStartPosY) >= dip2px) {
                    onLongPressed(false);
                }
            }
            int historySize = motionEvent.getHistorySize();
            for (int i = 0; i < historySize; i++) {
                float historicalX = motionEvent.getHistoricalX(0, i);
                float historicalY = motionEvent.getHistoricalY(0, i);
                this.mLocalAnnoTool.touchMove(historicalX, historicalY);
                zoomAnnotateMgr.touchMove(historicalX, historicalY);
                this.mEndPosX = historicalX;
                this.mEndPosY = historicalY;
            }
            this.mIsUp = false;
        }
    }

    private void touchUp() {
        this.mIsUp = true;
        AnnoToolType tool = this.mAnnoDataMgr.getTool();
        ZoomAnnotate zoomAnnotateMgr = ConfMgr.getInstance().getZoomAnnotateMgr();
        if (zoomAnnotateMgr == null || this.mBackgroundCanvas == null || AnnoToolType.ANNO_TOOL_TYPE_SPOTLIGHT == tool) {
            onRepaint();
            return;
        }
        ISAnnotateDraw iSAnnotateDraw = this.mLocalAnnoTool;
        if (iSAnnotateDraw != null) {
            iSAnnotateDraw.touchUp(this.mEndPosX, this.mEndPosY);
            this.mLocalAnnoTool.draw(this.mBackgroundCanvas);
        }
        zoomAnnotateMgr.touchUp(this.mEndPosX, this.mEndPosY);
        this.mCurX = 0.0f;
        this.mCurY = 0.0f;
        if (tool != AnnoToolType.ANNO_TOOL_TYPE_ERASER && !this.mAnnoDataMgr.isPresenter()) {
            startTimer(this.mDrawRunnable, 2500);
        }
    }

    private void updateLocalTool(float f) {
        AnnoToolType tool = this.mAnnoDataMgr.getTool();
        this.mLocalAnnoTool = getAnnoTool(tool, ((float) this.mAnnoDataMgr.getLineWidth(tool)) * this.mZoomFactor * f, this.mAnnoDataMgr.getColor(tool), this.mAnnoDataMgr.getTool() == AnnoToolType.ANNO_TOOL_TYPE_HIGHLIGHTER ? 97 : 255);
    }

    public void setAnnoWindow(int i, int i2, float f, float f2, float f3) {
        this.mZoomFactor = f3;
        if (this.mListener != null && !this.mAnnoDataMgr.isShareScreen()) {
            this.mListener.setAnnoWindow(i, i2, f, f2, f3);
        }
    }

    public void addDrawObjToList(@NonNull Bundle bundle) {
        boolean z;
        ZoomAnnotate zoomAnnotateMgr = ConfMgr.getInstance().getZoomAnnotateMgr();
        if (!bundle.isEmpty() && zoomAnnotateMgr != null) {
            AnnoDrawObj annoDrawObj = new AnnoDrawObj();
            ArrayList parcelableArrayList = bundle.getParcelableArrayList(zoomAnnotateMgr.KEY_TOOL_LIST);
            int i = 1;
            if (parcelableArrayList == null || parcelableArrayList.size() <= 0) {
                z = false;
            } else {
                annoDrawObj.annoPoints = (List) parcelableArrayList.get(0);
                z = true;
            }
            annoDrawObj.toolType = bundle.getInt(zoomAnnotateMgr.KEY_TOOL_TYPE);
            annoDrawObj.width = bundle.getInt(zoomAnnotateMgr.KEY_TOOL_WIDTH);
            annoDrawObj.color = bundle.getInt(zoomAnnotateMgr.KEY_TOOL_COLOR);
            annoDrawObj.alpha = bundle.getInt(zoomAnnotateMgr.KEY_TOOL_ALPHA);
            annoDrawObj.startX = bundle.getFloat(zoomAnnotateMgr.KEY_TOOL_START_X);
            annoDrawObj.startY = bundle.getFloat(zoomAnnotateMgr.KEY_TOOL_START_Y);
            annoDrawObj.endX = bundle.getFloat(zoomAnnotateMgr.KEY_TOOL_END_X);
            annoDrawObj.endY = bundle.getFloat(zoomAnnotateMgr.KEY_TOOL_END_Y);
            annoDrawObj.text = bundle.getString(zoomAnnotateMgr.KEY_TOOL_TEXT);
            annoDrawObj.isBold = bundle.getBoolean(zoomAnnotateMgr.KEY_TOOL_BOLD);
            annoDrawObj.isItalic = bundle.getBoolean(zoomAnnotateMgr.KEY_TOOL_ITALIC);
            annoDrawObj.fontSize = bundle.getInt(zoomAnnotateMgr.KEY_TOOL_FONT_SIZE);
            if (this.mAnnoDataMgr.isShareScreen()) {
                float f = z ? ((PointF) annoDrawObj.annoPoints.get(0)).x : annoDrawObj.startX;
                float f2 = z ? ((PointF) annoDrawObj.annoPoints.get(0)).y : annoDrawObj.startY;
                if (annoDrawObj.annoPoints != null && annoDrawObj.annoPoints.size() > 0) {
                    f = ((PointF) annoDrawObj.annoPoints.get(0)).x;
                    f2 = ((PointF) annoDrawObj.annoPoints.get(0)).y;
                }
                if (!this.mAnnoDataMgr.isLocalDrawing(f, f2)) {
                    if (this.mAnnoDataMgr.getIsHDPI()) {
                        i = 2;
                    }
                    annoDrawObj.transition(i, this.mStatusBarHeight);
                    if (AnnoToolType.ANNO_TOOL_TYPE_AUTO_ANNOTATOR_NAME.ordinal() != annoDrawObj.toolType) {
                        annoDrawObj.fontSize *= i;
                    }
                }
            }
            short[] shortArray = bundle.getShortArray(zoomAnnotateMgr.KEY_TOOL_TEXT_SHORT_LIST);
            if (shortArray != null && shortArray.length > 0) {
                String str = "";
                for (short s : shortArray) {
                    if (s == 13) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(FontStyleHelper.SPLITOR);
                        str = sb.toString();
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append((char) s);
                        str = sb2.toString();
                    }
                    annoDrawObj.text = adjustText(str, annoDrawObj.startX, annoDrawObj.endX, annoDrawObj.fontSize);
                }
            }
            if (AnnoToolType.ANNO_TOOL_TYPE_MULTI_THICKNESS_PEN.ordinal() == annoDrawObj.toolType) {
                Path path = this.mAnnoFillPath;
                if (path != null) {
                    annoDrawObj.path = path;
                }
            }
            this.m_annoDrawObjList.add(annoDrawObj);
        }
    }

    public void clearAndDrawAllPath() {
        if (this.mZoomFactor > 0.0f) {
            refreshAnnotateDrawPath();
        }
        onRepaint();
    }

    public void endEditing() {
        IDrawingViewListener iDrawingViewListener = this.mListener;
        if (iDrawingViewListener != null) {
            iDrawingViewListener.onEndEditing();
        }
    }

    public void beginEditing(int i, int i2) {
        IDrawingViewListener iDrawingViewListener = this.mListener;
        if (iDrawingViewListener != null) {
            iDrawingViewListener.onBeginEditing((int) this.mStartPosX, (int) this.mStartPosY);
        }
    }

    public void beginPath() {
        this.mAnnoFillPath = new Path();
    }

    public void closePath() {
        Path path = this.mAnnoFillPath;
        if (path != null) {
            path.close();
        }
    }

    public void moveToAbs(float f, float f2) {
        Path path = this.mAnnoFillPath;
        if (path != null) {
            path.moveTo(f, f2);
        }
    }

    public void lineToAbs(float f, float f2) {
        Path path = this.mAnnoFillPath;
        if (path != null) {
            path.lineTo(f, f2);
        }
    }

    public void curveToQuadAbs(float f, float f2, float f3, float f4) {
        Path path = this.mAnnoFillPath;
        if (path != null) {
            path.quadTo(f, f2, f3, f4);
        }
    }

    public void curveToCubicAbs(float f, float f2, float f3, float f4, float f5, float f6) {
        Path path = this.mAnnoFillPath;
        if (path != null) {
            path.cubicTo(f, f2, f3, f4, f5, f6);
        }
    }

    public void savePageSnapshot(int i) {
        this.mAnnoDataMgr.onSavePageSnapshot(i, this.mCachedImage);
    }

    public void clear() {
        if (this.mIsUp || AnnoToolType.ANNO_TOOL_TYPE_ERASER == this.mAnnoDataMgr.getTool()) {
            Canvas canvas = this.mBackgroundCanvas;
            if (canvas != null) {
                canvas.drawColor(0, Mode.CLEAR);
                this.mCurX = 0.0f;
                this.mCurY = 0.0f;
                drawFaceView();
                onRepaint();
            }
        }
    }

    @Nullable
    public ISAnnotateDraw getAnnoTool(@NonNull AnnoToolType annoToolType, float f, int i, int i2) {
        switch (annoToolType) {
            case ANNO_TOOL_TYPE_SPOTLIGHT:
                this.mRemoteTool = null;
                break;
            case ANNO_TOOL_TYPE_AUTO_LINE:
            case ANNO_TOOL_TYPE_AUTO_ELLIPSE:
            case ANNO_TOOL_TYPE_AUTO_ELLIPSE_SEMI_FILL:
            case ANNO_TOOL_TYPE_AUTO_ELLIPSE_FILL:
            case ANNO_TOOL_TYPE_AUTO_RECTANGLE:
            case ANNO_TOOL_TYPE_AUTO_RECTANGLE_SEMI_FILL:
            case ANNO_TOOL_TYPE_AUTO_RECTANGLE_FILL:
            case ANNO_TOOL_TYPE_AUTO_ROUNDEDRECTANGLE_FILL:
            case ANNO_TOOL_TYPE_AUTO_ANNOTATOR_NAME:
                this.mRemoteTool = new AutoShapeCtl(f, i, i2);
                break;
            case ANNO_TOOL_TYPE_ARROW:
                this.mRemoteTool = new ArrowCtl(f, i, i2);
                break;
            case ANNO_TOOL_TYPE_AUTO_STAMP_ARROW:
            case ANNO_TOOL_TYPE_AUTO_STAMP_X:
            case ANNO_TOOL_TYPE_AUTO_STAMP_CHECK:
            case ANNO_TOOL_TYPE_AUTO_DIAMOND:
            case ANNO_TOOL_TYPE_AUTO_STAMP_STAR:
            case ANNO_TOOL_TYPE_AUTO_STAMP_HEART:
            case ANNO_TOOL_TYPE_AUTO_STAMP_QM:
                this.mRemoteTool = new PolygonCtl(f, i, i2);
                break;
            case ANNO_TOOL_TYPE_MULTI_THICKNESS_PEN:
                this.mRemoteTool = new FillPathCtl(f, i, i2);
                break;
            case ANNO_TOOL_TYPE_AUTO_ARROW2:
                this.mRemoteTool = new AutoArrow(f, i, 128);
                break;
            case ANNO_TOOL_TYPE_AUTO_ARROW1:
            case ANNO_TOOL_TYPE_AUTO_DOUBLE_ARROW:
                this.mRemoteTool = new AutoArrow(f, i, i2);
                break;
            case ANNO_TOOL_TYPE_ERASER:
                this.mRemoteTool = new EraserCtl(f, i, i2);
                break;
            case ANNO_TOOL_TYPE_TEXTBOX:
                this.mRemoteTool = new TextCtl(f, i, i2);
                break;
            default:
                this.mRemoteTool = new PenCtl(f, i, i2);
                break;
        }
        ISAnnotateDraw iSAnnotateDraw = this.mRemoteTool;
        if (iSAnnotateDraw != null) {
            iSAnnotateDraw.setToolType(annoToolType);
            this.mRemoteTool.setCanvas(this.mBackgroundCanvas);
        }
        return this.mRemoteTool;
    }

    private void refreshAnnotateDrawPath() {
        AnnoToolType tool = this.mAnnoDataMgr.getTool();
        if (!this.mAnnoDataMgr.isPresenter() || this.mBackgroundCanvas == null || !(this.mIsUp || tool == AnnoToolType.ANNO_TOOL_TYPE_ERASER || tool == AnnoToolType.ANNO_TOOL_TYPE_SPOTLIGHT)) {
            this.m_annoDrawObjList.clear();
            return;
        }
        this.mBackgroundCanvas.drawColor(0, Mode.CLEAR);
        for (int i = 0; i < this.m_annoDrawObjList.size(); i++) {
            AnnoDrawObj annoDrawObj = (AnnoDrawObj) this.m_annoDrawObjList.get(i);
            AnnoToolType annoToolType = AnnoToolType.values()[annoDrawObj.toolType];
            int argb = Color.argb(annoDrawObj.alpha, Color.blue(annoDrawObj.color), Color.green(annoDrawObj.color), Color.red(annoDrawObj.color));
            switch (annoToolType) {
                case ANNO_TOOL_TYPE_AUTO_LINE:
                case ANNO_TOOL_TYPE_AUTO_ELLIPSE:
                case ANNO_TOOL_TYPE_AUTO_ELLIPSE_SEMI_FILL:
                case ANNO_TOOL_TYPE_AUTO_ELLIPSE_FILL:
                case ANNO_TOOL_TYPE_AUTO_RECTANGLE:
                case ANNO_TOOL_TYPE_AUTO_RECTANGLE_SEMI_FILL:
                case ANNO_TOOL_TYPE_AUTO_RECTANGLE_FILL:
                case ANNO_TOOL_TYPE_AUTO_ROUNDEDRECTANGLE_FILL:
                case ANNO_TOOL_TYPE_AUTO_ANNOTATOR_NAME:
                    drawAutoShape(annoDrawObj.width, argb, annoDrawObj.alpha, annoDrawObj.startX, annoDrawObj.startY, annoDrawObj.endX, annoDrawObj.endY, annoDrawObj.toolType, annoDrawObj.text, annoDrawObj.fontSize);
                    continue;
                case ANNO_TOOL_TYPE_ARROW:
                    drawArrow(annoDrawObj.width, argb, annoDrawObj.alpha, (int) annoDrawObj.startX, (int) annoDrawObj.startY, (int) annoDrawObj.endX, (int) annoDrawObj.endY, annoDrawObj.text, annoDrawObj.fontSize, annoDrawObj.annoPoints);
                    continue;
                case ANNO_TOOL_TYPE_AUTO_STAMP_ARROW:
                case ANNO_TOOL_TYPE_AUTO_STAMP_X:
                case ANNO_TOOL_TYPE_AUTO_STAMP_CHECK:
                case ANNO_TOOL_TYPE_AUTO_DIAMOND:
                case ANNO_TOOL_TYPE_AUTO_STAMP_STAR:
                case ANNO_TOOL_TYPE_AUTO_STAMP_HEART:
                case ANNO_TOOL_TYPE_AUTO_STAMP_QM:
                    drawPolygon(annoDrawObj.width, argb, annoDrawObj.alpha, annoDrawObj.toolType, annoDrawObj.annoPoints, annoDrawObj.startX, annoDrawObj.startY);
                    continue;
                case ANNO_TOOL_TYPE_MULTI_THICKNESS_PEN:
                    fillAnnoPath(annoDrawObj.path, argb, annoDrawObj.alpha, annoDrawObj.toolType);
                    continue;
                case ANNO_TOOL_TYPE_AUTO_ARROW2:
                    strokeAutoShapeArrow(2, argb, 128, annoDrawObj.toolType, annoDrawObj.annoPoints);
                    continue;
                case ANNO_TOOL_TYPE_AUTO_ARROW1:
                case ANNO_TOOL_TYPE_AUTO_DOUBLE_ARROW:
                    strokeAutoShapeArrow(annoDrawObj.width, argb, annoDrawObj.alpha, annoDrawObj.toolType, annoDrawObj.annoPoints);
                    break;
                case ANNO_TOOL_TYPE_TEXTBOX:
                    break;
                case ANNO_TOOL_TYPE_HIGHLIGHTER:
                case ANNO_TOOL_TYPE_PEN:
                case ANNO_TOOL_TYPE_MULTI_SHAPE_DETECTOR:
                    drawPolyline(annoDrawObj.width, argb, annoDrawObj.alpha, annoDrawObj.annoPoints);
                    continue;
            }
            drawText(annoDrawObj.text, (int) annoDrawObj.startX, (int) annoDrawObj.startY, argb, annoDrawObj.isBold, annoDrawObj.isItalic, annoDrawObj.fontSize);
        }
        drawFaceView();
        this.m_annoDrawObjList.clear();
    }

    public void drawPolyline(int i, int i2, int i3, @NonNull List<PointF> list) {
        getAnnoTool(i3 < 255 ? AnnoToolType.ANNO_TOOL_TYPE_HIGHLIGHTER : AnnoToolType.ANNO_TOOL_TYPE_PEN, (float) i, i2, i3);
        if (this.mRemoteTool != null) {
            for (int i4 = 0; i4 < list.size(); i4++) {
                float f = ((PointF) list.get(i4)).x;
                float f2 = ((PointF) list.get(i4)).y;
                if (i4 == 0) {
                    this.mRemoteTool.touchDown(f, f2);
                    if (list.size() == 1) {
                        this.mRemoteTool.touchUp(f, f2);
                    }
                } else if (list.size() - 1 != i4) {
                    this.mRemoteTool.touchMove(f, f2);
                } else {
                    this.mRemoteTool.touchMove(f, f2);
                    this.mRemoteTool.touchUp(f, f2);
                    this.mRemoteTool.draw(this.mBackgroundCanvas);
                }
            }
        }
    }

    public void drawAutoShape(int i, int i2, int i3, float f, float f2, float f3, float f4, int i4, String str, int i5) {
        AnnoToolType annoToolType = AnnoToolType.values()[i4];
        getAnnoTool(annoToolType, (float) i, i2, i3);
        ISAnnotateDraw iSAnnotateDraw = this.mRemoteTool;
        if (iSAnnotateDraw != null) {
            iSAnnotateDraw.setContext(getContext());
            this.mRemoteTool.setToolType(annoToolType);
            this.mRemoteTool.touchDown(f, f2);
            this.mRemoteTool.touchMove(f3, f4);
            this.mRemoteTool.touchUp(f3, f4);
            this.mRemoteTool.setTextData(str, (int) f, (int) f2, false, false, i5);
            this.mRemoteTool.draw(this.mBackgroundCanvas);
        }
    }

    public void strokeAutoShapeArrow(int i, int i2, int i3, int i4, List<PointF> list) {
        AnnoToolType annoToolType = AnnoToolType.values()[i4];
        getAnnoTool(annoToolType, (float) i, i2, i3);
        ISAnnotateDraw iSAnnotateDraw = this.mRemoteTool;
        if (iSAnnotateDraw != null) {
            iSAnnotateDraw.setToolType(annoToolType);
            this.mRemoteTool.setAnnoPoints(list, 0.0f, 0.0f);
            this.mRemoteTool.draw(this.mBackgroundCanvas);
        }
    }

    public void drawPolygon(int i, int i2, int i3, int i4, List<PointF> list, float f, float f2) {
        AnnoToolType annoToolType = AnnoToolType.values()[i4];
        getAnnoTool(annoToolType, (float) i, i2, i3);
        ISAnnotateDraw iSAnnotateDraw = this.mRemoteTool;
        if (iSAnnotateDraw != null) {
            iSAnnotateDraw.setToolType(annoToolType);
            this.mRemoteTool.setAnnoPoints(list, f, f2);
            this.mRemoteTool.draw(this.mBackgroundCanvas);
        }
    }

    public void fillAnnoPath(Path path, int i, int i2, int i3) {
        getAnnoTool(AnnoToolType.values()[i3], 4.0f, i, i2);
        ISAnnotateDraw iSAnnotateDraw = this.mRemoteTool;
        if (iSAnnotateDraw != null) {
            iSAnnotateDraw.draw(this.mBackgroundCanvas, path);
        }
    }

    public void drawArrow(int i, int i2, int i3, int i4, int i5, int i6, int i7, String str, int i8, List<PointF> list) {
        int i9 = i2;
        int i10 = i3;
        getAnnoTool(AnnoToolType.ANNO_TOOL_TYPE_ARROW, (float) i, i2, i3);
        ISAnnotateDraw iSAnnotateDraw = this.mRemoteTool;
        if (iSAnnotateDraw != null) {
            iSAnnotateDraw.setIsShareScreen(this.mAnnoDataMgr.isShareScreen());
            this.mRemoteTool.setArrowData(i4, i5, i6, i7, str);
            this.mRemoteTool.setAnnoPoints(list, 0.0f, 0.0f);
            this.mRemoteTool.setTextSize(i8);
            this.mRemoteTool.draw(this.mBackgroundCanvas);
        }
    }

    public void drawText(@Nullable String str, int i, int i2, int i3, boolean z, boolean z2, int i4) {
        if (str != null) {
            getAnnoTool(AnnoToolType.ANNO_TOOL_TYPE_TEXTBOX, 2.0f, i3, 255);
            ISAnnotateDraw iSAnnotateDraw = this.mRemoteTool;
            if (iSAnnotateDraw != null) {
                iSAnnotateDraw.setTextData(str, i, i2, z, z2, i4);
                this.mRemoteTool.draw(this.mBackgroundCanvas);
            }
        }
    }

    public String adjustText(@NonNull String str, float f, float f2, int i) {
        String[] split = str.split(FontStyleHelper.SPLITOR);
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < split.length; i2++) {
            String str2 = "";
            String str3 = split[i2];
            int i3 = 0;
            while (i3 < str3.length()) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(str3.charAt(i3));
                str2 = sb.toString();
                float textWidth = ((float) getTextWidth(str2, (float) i)) + f;
                float displayWidth = (float) UIUtil.getDisplayWidth(getContext());
                if (displayWidth > f2) {
                    displayWidth = f2;
                }
                if (textWidth > displayWidth) {
                    arrayList.add(str2.substring(0, i3));
                    str2 = "";
                    str3 = str3.substring(i3, str3.length());
                    i3 = -1;
                }
                i3++;
            }
            if (!str2.trim().isEmpty()) {
                arrayList.add(str2);
            }
        }
        String str4 = "";
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            if (i4 != arrayList.size() - 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str4);
                sb2.append((String) arrayList.get(i4));
                sb2.append(FontStyleHelper.SPLITOR);
                str4 = sb2.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str4);
                sb3.append((String) arrayList.get(i4));
                str4 = sb3.toString();
            }
        }
        return str4;
    }

    private int getTextWidth(String str, float f) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(f);
        return (int) textPaint.measureText(str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0058, code lost:
        if (r1 != null) goto L_0x0067;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0065, code lost:
        if (r1 == null) goto L_0x006c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0067, code lost:
        r5.mSurfaceHolder.unlockCanvasAndPost(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void drawFaceView() {
        /*
            r5 = this;
            android.graphics.Bitmap r0 = r5.mBackgroundImage
            if (r0 == 0) goto L_0x006d
            android.view.SurfaceHolder r0 = r5.mSurfaceHolder
            if (r0 != 0) goto L_0x0009
            goto L_0x006d
        L_0x0009:
            r1 = 0
            android.graphics.Canvas r1 = r0.lockCanvas()     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            if (r1 != 0) goto L_0x0018
            if (r1 == 0) goto L_0x0017
            android.view.SurfaceHolder r0 = r5.mSurfaceHolder
            r0.unlockCanvasAndPost(r1)
        L_0x0017:
            return
        L_0x0018:
            r0 = 0
            android.graphics.PorterDuff$Mode r2 = android.graphics.PorterDuff.Mode.CLEAR     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            r1.drawColor(r0, r2)     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            android.graphics.Bitmap r0 = r5.mBackgroundImage     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            android.graphics.Paint r2 = r5.mBitmapPaint     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            r3 = 0
            r1.drawBitmap(r0, r3, r3, r2)     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            android.graphics.Bitmap r0 = r5.mSpolightImage     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            if (r0 == 0) goto L_0x004b
            com.zipow.annotate.AnnoToolType r0 = com.zipow.annotate.AnnoToolType.ANNO_TOOL_TYPE_SPOTLIGHT     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            com.zipow.annotate.AnnoDataMgr r2 = r5.mAnnoDataMgr     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            com.zipow.annotate.AnnoToolType r2 = r2.getTool()     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            if (r0 != r2) goto L_0x004b
            float r0 = r5.mCurX     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 == 0) goto L_0x004b
            float r0 = r5.mCurY     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 == 0) goto L_0x004b
            android.graphics.Bitmap r0 = r5.mSpolightImage     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            float r2 = r5.mCurX     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            float r3 = r5.mCurY     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            android.graphics.Paint r4 = r5.mBitmapPaint     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            r1.drawBitmap(r0, r2, r3, r4)     // Catch:{ Exception -> 0x0064, all -> 0x005b }
        L_0x004b:
            boolean r0 = r5.mIsUp     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            if (r0 != 0) goto L_0x0058
            com.zipow.annotate.render.ISAnnotateDraw r0 = r5.mLocalAnnoTool     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            if (r0 == 0) goto L_0x0058
            com.zipow.annotate.render.ISAnnotateDraw r0 = r5.mLocalAnnoTool     // Catch:{ Exception -> 0x0064, all -> 0x005b }
            r0.draw(r1)     // Catch:{ Exception -> 0x0064, all -> 0x005b }
        L_0x0058:
            if (r1 == 0) goto L_0x006c
            goto L_0x0067
        L_0x005b:
            r0 = move-exception
            if (r1 == 0) goto L_0x0063
            android.view.SurfaceHolder r2 = r5.mSurfaceHolder
            r2.unlockCanvasAndPost(r1)
        L_0x0063:
            throw r0
        L_0x0064:
            if (r1 == 0) goto L_0x006c
        L_0x0067:
            android.view.SurfaceHolder r0 = r5.mSurfaceHolder
            r0.unlockCanvasAndPost(r1)
        L_0x006c:
            return
        L_0x006d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.annotate.AnnotateView.drawFaceView():void");
    }
}
