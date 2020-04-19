package com.zipow.annotate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.provider.Settings;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.annotate.AnnotateDrawingView.AnnoClearType;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.share.ColorAndLineWidthView;
import com.zipow.videobox.share.ColorSelectedImage;
import com.zipow.videobox.share.IColorChangedListener;
import com.zipow.videobox.view.ToolbarButton;
import com.zipow.videobox.view.ToolbarDragView;
import com.zipow.videobox.view.ToolbarDragView.ToolbarScrollListener;
import java.util.HashMap;
import java.util.Map;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.UiModeUtil;
import p021us.zoom.videomeetings.C4558R;

public class ShareScreenAnnoToolbar implements OnClickListener, IColorChangedListener {
    public static final int DRAGVIEW_STATUS_Annotation = 2;
    public static final int DRAGVIEW_STATUS_Folded = 3;
    public static final int DRAGVIEW_STATUS_Hidden = 4;
    public static final int DRAGVIEW_STATUS_Normal = 1;
    private static final String TAG = "ShareScreenAnnoToolbar";
    private boolean isAnnotationOff = false;
    /* access modifiers changed from: private */
    @NonNull
    public LocationParamForScreen loctParamNow = new LocationParamForScreen();
    @NonNull
    private Map<String, LocationParamForScreen> loctParams = new HashMap();
    private AnnoDataMgr mAnnoDataMgr = AnnoDataMgr.getInstance();
    private ToolbarButton mAnnotation;
    private View mClear;
    /* access modifiers changed from: private */
    @Nullable
    public ColorAndLineWidthView mColorAndLineWidthView;
    /* access modifiers changed from: private */
    public View mColorDismissView;
    private ColorSelectedImage mColorImage;
    private View mColorIndicator;
    @NonNull
    private final Context mContext = VideoBoxApplication.getNonNullInstance();
    /* access modifiers changed from: private */
    @Nullable
    public Display mDisplay;
    @Nullable
    private AnnotateView mDrawingView;
    private View mHighlight;
    private View mLine;
    @Nullable
    private Listener mListener;
    private int mMaxUnitWidth;
    private View mOval;
    private View mPen;
    private View mRectangle;
    private View mRedo;
    private Runnable mRunnableShowToolbar = new Runnable() {
        public void run() {
            ShareScreenAnnoToolbar.this.showToolbar();
        }
    };
    private View mSpotlight;
    private int mState = 1;
    private View mToggleToolbar;
    private ImageView mToggleToolbarArrow;
    /* access modifiers changed from: private */
    public View mToggleToolbarBg;
    private int mToolBarMargin;
    private View mToolbar;
    /* access modifiers changed from: private */
    public View mToolbarBg;
    @Nullable
    private Handler mToolbarDragViewHandler = null;
    /* access modifiers changed from: private */
    @NonNull
    public LayoutParams mToolbarLayoutParams = new LayoutParams();
    private View mUndo;
    /* access modifiers changed from: private */
    public ToolbarDragView mViewToolbar;
    /* access modifiers changed from: private */
    @Nullable
    public final WindowManager mWindowManager = ((WindowManager) this.mContext.getSystemService("window"));

    private class GuestureListener extends ToolbarScrollListener {
        float mLastRawX = -1.0f;
        float mLastRawY = -1.0f;

        public GuestureListener() {
        }

        public void onTouchEventUp() {
            this.mLastRawX = -1.0f;
            this.mLastRawY = -1.0f;
            ShareScreenAnnoToolbar.this.dragFinish();
        }

        public boolean onScroll(@Nullable MotionEvent motionEvent, @Nullable MotionEvent motionEvent2, float f, float f2) {
            int i;
            int i2;
            if (ShareScreenAnnoToolbar.this.mViewToolbar == null || ShareScreenAnnoToolbar.this.mViewToolbar.getParent() == null || motionEvent == null || motionEvent2 == null || ShareScreenAnnoToolbar.this.mDisplay == null || ShareScreenAnnoToolbar.this.mWindowManager == null) {
                return true;
            }
            ShareScreenAnnoToolbar.this.dragView();
            if (ShareScreenAnnoToolbar.this.mColorAndLineWidthView == null) {
                return true;
            }
            if (ShareScreenAnnoToolbar.this.mColorAndLineWidthView.isShown()) {
                ShareScreenAnnoToolbar.this.mColorAndLineWidthView.dismiss();
                ShareScreenAnnoToolbar.this.mColorDismissView.setVisibility(8);
            }
            ShareScreenAnnoToolbar.this.mToolbarBg.setBackgroundResource(C4558R.C4559drawable.zm_screenshare_toolbar_bg_drag);
            ShareScreenAnnoToolbar.this.mToggleToolbarBg.setBackgroundResource(C4558R.C4559drawable.zm_screenshare_toolbar_bg_drag);
            if (((int) this.mLastRawX) == -1 || ((int) this.mLastRawY) == -1) {
                int rawX = (int) (motionEvent2.getRawX() - motionEvent.getRawX());
                i = (int) (motionEvent2.getRawY() - motionEvent.getRawY());
                i2 = rawX;
            } else {
                i2 = (int) (motionEvent2.getRawX() - this.mLastRawX);
                i = (int) (motionEvent2.getRawY() - this.mLastRawY);
            }
            this.mLastRawX = motionEvent2.getRawX();
            this.mLastRawY = motionEvent2.getRawY();
            int width = ShareScreenAnnoToolbar.this.mViewToolbar.getWidth();
            int height = ShareScreenAnnoToolbar.this.mViewToolbar.getHeight();
            if (ShareScreenAnnoToolbar.this.mToolbarLayoutParams.x + i2 < 0) {
                i2 = 0 - ShareScreenAnnoToolbar.this.mToolbarLayoutParams.x;
            }
            if (ShareScreenAnnoToolbar.this.mToolbarLayoutParams.x + i2 + width > ShareScreenAnnoToolbar.this.mDisplay.getWidth()) {
                i2 = (ShareScreenAnnoToolbar.this.mDisplay.getWidth() - ShareScreenAnnoToolbar.this.mToolbarLayoutParams.x) - width;
            }
            if (ShareScreenAnnoToolbar.this.mToolbarLayoutParams.y + i < 0) {
                i = ShareScreenAnnoToolbar.this.mToolbarLayoutParams.y;
            }
            if (ShareScreenAnnoToolbar.this.mToolbarLayoutParams.y + i + height > ShareScreenAnnoToolbar.this.mDisplay.getHeight()) {
                i = (ShareScreenAnnoToolbar.this.mDisplay.getHeight() - ShareScreenAnnoToolbar.this.mToolbarLayoutParams.y) - height;
            }
            ShareScreenAnnoToolbar.this.mToolbarLayoutParams.y += i;
            ShareScreenAnnoToolbar.this.mToolbarLayoutParams.x += i2;
            ShareScreenAnnoToolbar.this.mWindowManager.updateViewLayout(ShareScreenAnnoToolbar.this.mViewToolbar, ShareScreenAnnoToolbar.this.mToolbarLayoutParams);
            ShareScreenAnnoToolbar.this.loctParamNow.dragged = true;
            return true;
        }
    }

    public interface Listener {
        void onAnnoStatusChanged();

        void onClickStopShare();
    }

    class LocationParamForScreen {
        boolean dragged;
        int marginLeft;
        int marginTop;

        LocationParamForScreen() {
        }
    }

    public ShareScreenAnnoToolbar(@Nullable Listener listener) {
        if (this.mWindowManager != null) {
            this.mListener = listener;
            Resources resources = this.mContext.getResources();
            this.mMaxUnitWidth = (int) resources.getDimension(C4558R.dimen.zm_share_toolbar_unit_maxWidth);
            this.mDisplay = this.mWindowManager.getDefaultDisplay();
            if (this.mDisplay != null) {
                this.mToolBarMargin = (int) resources.getDimension(C4558R.dimen.zm_share_toolbar_margin);
                Map<String, LocationParamForScreen> map = this.loctParams;
                StringBuilder sb = new StringBuilder();
                sb.append(this.mDisplay.getWidth());
                sb.append(":");
                sb.append(this.mDisplay.getHeight());
                map.put(sb.toString(), this.loctParamNow);
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                if (confContext != null) {
                    this.isAnnotationOff = confContext.isAnnoationOff();
                }
                init();
            }
        }
    }

    public void onAnnotateStartedUp(boolean z, long j) {
        if (this.mDrawingView != null) {
            this.mAnnoDataMgr.startAnnotaion(z, true, j);
            this.mDrawingView.startAnnotation();
            this.mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_PEN);
        }
    }

    public void onAnnotateShutDown() {
        if (this.mDrawingView != null) {
            this.mAnnoDataMgr.stopAnnotation();
            this.mDrawingView.stopAnnotation();
            this.mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_PEN);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        destroy();
        init();
        ToolbarDragView toolbarDragView = this.mViewToolbar;
        if (toolbarDragView != null) {
            toolbarDragView.post(this.mRunnableShowToolbar);
        }
    }

    public void updateLayoutparameter() {
        if (this.mViewToolbar != null && this.mDisplay != null && this.mWindowManager != null) {
            setToolbarLayoutParamsWidth(this.mToolbarLayoutParams);
            StringBuilder sb = new StringBuilder();
            sb.append(this.mDisplay.getWidth());
            sb.append(":");
            sb.append(this.mDisplay.getHeight());
            String sb2 = sb.toString();
            LocationParamForScreen locationParamForScreen = (LocationParamForScreen) this.loctParams.get(sb2);
            if (locationParamForScreen == null) {
                locationParamForScreen = new LocationParamForScreen();
                locationParamForScreen.marginLeft = this.mToolBarMargin;
                locationParamForScreen.marginTop = (this.mDisplay.getHeight() - this.mViewToolbar.getHeight()) - this.mToolBarMargin;
                this.loctParams.put(sb2, locationParamForScreen);
            }
            if (this.loctParamNow.dragged) {
                this.loctParamNow.marginLeft = this.mToolbarLayoutParams.x;
                this.loctParamNow.marginTop = this.mToolbarLayoutParams.y;
            }
            this.loctParamNow.dragged = false;
            this.loctParamNow = locationParamForScreen;
            this.mToolbarLayoutParams.x = this.loctParamNow.marginLeft;
            this.mToolbarLayoutParams.y = this.loctParamNow.marginTop;
            if (this.mToolbarLayoutParams.x + this.mToolbarLayoutParams.width > this.mDisplay.getWidth()) {
                this.mToolbarLayoutParams.x = this.mDisplay.getWidth() - this.mToolbarLayoutParams.width;
            }
            if (this.mToolbarLayoutParams.y + this.mViewToolbar.getHeight() > this.mDisplay.getHeight()) {
                this.mToolbarLayoutParams.y = this.mDisplay.getHeight() - this.mViewToolbar.getHeight();
            }
            this.mWindowManager.updateViewLayout(this.mViewToolbar, this.mToolbarLayoutParams);
        }
    }

    private void setToolbarLayoutParamsWidth(@Nullable LayoutParams layoutParams) {
        if (layoutParams != null) {
            Display display = this.mDisplay;
            if (display != null) {
                int min = Math.min(display.getWidth(), this.mDisplay.getHeight());
                ViewGroup viewGroup = (ViewGroup) this.mAnnotation.getParent();
                int i = 0;
                for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
                    if (viewGroup.getChildAt(i2).getVisibility() == 0) {
                        i++;
                    }
                }
                int i3 = (min - (this.mToolBarMargin * 4)) / i;
                int i4 = this.mMaxUnitWidth;
                if (i3 > i4) {
                    i3 = i4;
                }
                this.mToolbarLayoutParams.width = (i * i3) + (this.mToolBarMargin * 2);
            }
        }
    }

    public void toggleAnnotation(boolean z) {
        if (this.mViewToolbar != null && !this.isAnnotationOff && this.mWindowManager != null && this.mDrawingView != null) {
            if (z) {
                if (!isAnnotationStart()) {
                    this.mSpotlight.setVisibility(0);
                    this.mPen.setVisibility(0);
                    this.mHighlight.setVisibility(0);
                    this.mUndo.setVisibility(0);
                    this.mRedo.setVisibility(0);
                    this.mClear.setVisibility(0);
                    this.mColorIndicator.setVisibility(0);
                    int i = UIUtil.isTablet(this.mContext) ? 0 : 8;
                    this.mLine.setVisibility(i);
                    this.mRectangle.setVisibility(i);
                    this.mOval.setVisibility(i);
                    this.mAnnotation.setText(C4558R.string.zm_btn_stop_annotation);
                    this.mAnnotation.setBackgroundResource(C4558R.C4559drawable.zm_toolbar_stopannotation_bgcolor);
                    this.mDrawingView.setEditModel(true);
                    showAnnotation();
                    setAnnotateDisable(false);
                    this.mToggleToolbar.setVisibility(8);
                    this.mState = 2;
                    this.mWindowManager.updateViewLayout(this.mViewToolbar, this.mToolbarLayoutParams);
                } else {
                    return;
                }
            } else if (isAnnotationStart()) {
                this.mSpotlight.setVisibility(8);
                this.mPen.setVisibility(8);
                this.mHighlight.setVisibility(8);
                this.mUndo.setVisibility(8);
                this.mRedo.setVisibility(8);
                this.mClear.setVisibility(8);
                this.mLine.setVisibility(8);
                this.mRectangle.setVisibility(8);
                this.mOval.setVisibility(8);
                this.mColorIndicator.setVisibility(8);
                this.mAnnotation.setText(C4558R.string.zm_btn_start_annotation);
                this.mAnnotation.setBackgroundResource(C4558R.C4559drawable.zm_toolbar_annotation_bgcolor);
                this.mDrawingView.setVisibility(8);
                this.mDrawingView.setEditModel(false);
                setAnnotateDisable(true);
                this.mToggleToolbar.setVisibility(0);
                this.mState = 1;
                LayoutParams layoutParams = this.mToolbarLayoutParams;
                layoutParams.x = this.mToolBarMargin;
                this.mWindowManager.updateViewLayout(this.mViewToolbar, layoutParams);
            } else {
                return;
            }
            checkStatus();
            onAnnoStatusChanged();
            updateLayoutparameter();
        }
    }

    private void checkStatus() {
        if (this.mToolbarDragViewHandler == null) {
            if (this.mViewToolbar != null) {
                this.mToolbarDragViewHandler = new Handler();
            } else {
                return;
            }
        }
        if (AccessibilityUtil.isSpokenFeedbackEnabled(this.mViewToolbar.getContext())) {
            activateView();
            this.mToggleToolbar.setVisibility(8);
            return;
        }
        this.mToolbarDragViewHandler.removeCallbacksAndMessages(null);
        int i = this.mState;
        if (i == 1) {
            this.mToolbarDragViewHandler.postDelayed(new Runnable() {
                public void run() {
                    ShareScreenAnnoToolbar.this.clickToolBar();
                }
            }, 5000);
        } else if (i == 3) {
            this.mToolbarDragViewHandler.postDelayed(new Runnable() {
                public void run() {
                    ShareScreenAnnoToolbar.this.hiddenView();
                }
            }, 3000);
        } else if (i == 4) {
            activateView();
        }
    }

    /* access modifiers changed from: private */
    public void clickToolBar() {
        if (this.mWindowManager != null) {
            if (this.mState == 4) {
                activateView();
            } else {
                int i = this.mToolbar.getVisibility() == 0 ? 8 : 0;
                this.mToolbarBg.setVisibility(i);
                this.mToolbar.setVisibility(i);
                if (i == 0) {
                    this.mState = 1;
                    this.mToggleToolbarArrow.setImageResource(C4558R.C4559drawable.zm_screenshare_toggle_left);
                } else {
                    this.mToggleToolbarArrow.setImageResource(C4558R.C4559drawable.zm_screenshare_toggle_right);
                    LayoutParams layoutParams = this.mToolbarLayoutParams;
                    layoutParams.x = 0;
                    this.mWindowManager.updateViewLayout(this.mViewToolbar, layoutParams);
                    this.mState = 3;
                }
            }
            checkStatus();
        }
    }

    /* access modifiers changed from: private */
    public void dragView() {
        if (this.mState == 4) {
            activateView();
            return;
        }
        Handler handler = this.mToolbarDragViewHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void activateView() {
        Handler handler = this.mToolbarDragViewHandler;
        if (handler != null && this.mWindowManager != null) {
            handler.removeCallbacksAndMessages(null);
            if (this.mState == 4) {
                this.mToolbarLayoutParams.flags &= -513;
                this.mViewToolbar.setAlpha(1.0f);
                this.mViewToolbar.setScaleX(1.0f);
                this.mViewToolbar.setScaleY(1.0f);
                LayoutParams layoutParams = this.mToolbarLayoutParams;
                layoutParams.x = this.mToolBarMargin;
                this.mWindowManager.updateViewLayout(this.mViewToolbar, layoutParams);
                this.mState = 3;
            }
            if (this.mState == 3) {
                clickToolBar();
            }
        }
    }

    /* access modifiers changed from: private */
    public void hiddenView() {
        if (this.mToolbarDragViewHandler != null && this.mWindowManager != null) {
            this.mToolbarLayoutParams.flags |= 512;
            this.mViewToolbar.setAlpha(0.4f);
            this.mViewToolbar.setScaleX(0.9f);
            this.mViewToolbar.setScaleY(0.9f);
            this.mToolbarLayoutParams.x = (int) (0.0d - (((double) this.mToggleToolbar.getWidth()) * 0.7d));
            this.mWindowManager.updateViewLayout(this.mViewToolbar, this.mToolbarLayoutParams);
            this.mState = 4;
        }
    }

    private void init() {
        try {
            if (!this.isAnnotationOff) {
                addAnnotation();
                addColorView();
            }
            addToolbar();
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null) {
                AnnoDataMgr.getInstance().setAttendeeAnnotateDisable(shareObj.isAttendeeAnnotationDisabledForMySharedContent());
            }
            setAnnotateDisable(true);
        } catch (Exception unused) {
            this.mDrawingView = null;
            this.mViewToolbar = null;
            this.mColorDismissView = null;
            this.mColorAndLineWidthView = null;
        }
    }

    public void showToolbar() {
        ToolbarDragView toolbarDragView = this.mViewToolbar;
        if (toolbarDragView != null && toolbarDragView.getParent() != null) {
            this.mViewToolbar.setVisibility(0);
            checkStatus();
        }
    }

    public static int getWindowLayoutParamsType(Context context) {
        if (!OsUtil.isAtLeastN() || (!Settings.canDrawOverlays(context) && !VideoBoxApplication.getInstance().isSDKMode())) {
            return CompatUtils.getSystemAlertWindowType(2005);
        }
        return CompatUtils.getSystemAlertWindowType(2003);
    }

    @SuppressLint({"RtlHardcoded"})
    private void addColorView() {
        if (this.mWindowManager != null) {
            this.mColorDismissView = new View(this.mContext);
            this.mColorDismissView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (!(ShareScreenAnnoToolbar.this.mColorAndLineWidthView == null || ShareScreenAnnoToolbar.this.mColorDismissView == null)) {
                        ShareScreenAnnoToolbar.this.mColorAndLineWidthView.dismiss();
                        ShareScreenAnnoToolbar.this.mColorDismissView.setVisibility(8);
                    }
                    return false;
                }
            });
            LayoutParams layoutParams = new LayoutParams();
            layoutParams.type = getWindowLayoutParamsType(this.mContext);
            layoutParams.format = 1;
            layoutParams.flags |= 1320;
            layoutParams.width = -1;
            layoutParams.height = -1;
            this.mWindowManager.addView(this.mColorDismissView, layoutParams);
            this.mColorAndLineWidthView = new ColorAndLineWidthView(this.mContext);
            this.mColorAndLineWidthView.showInWindowManager(this.mWindowManager);
            this.mColorAndLineWidthView.setListener(this);
            this.mColorDismissView.setVisibility(8);
        }
    }

    @SuppressLint({"RtlHardcoded"})
    private void addToolbar() {
        if (this.mDisplay != null && this.mWindowManager != null) {
            this.mViewToolbar = (ToolbarDragView) View.inflate(this.mContext, C4558R.layout.zm_share_toolbar, null);
            this.mToolbar = this.mViewToolbar.findViewById(C4558R.C4560id.tool_bar);
            this.mToolbarBg = this.mViewToolbar.findViewById(C4558R.C4560id.toolbar_bg);
            this.mAnnotation = (ToolbarButton) this.mViewToolbar.findViewById(C4558R.C4560id.btnAnnotation);
            this.mSpotlight = this.mViewToolbar.findViewById(C4558R.C4560id.btnSpotlight);
            this.mPen = this.mViewToolbar.findViewById(C4558R.C4560id.btnPen);
            this.mHighlight = this.mViewToolbar.findViewById(C4558R.C4560id.btnHighlight);
            this.mLine = this.mViewToolbar.findViewById(C4558R.C4560id.btnAutoLine);
            this.mRectangle = this.mViewToolbar.findViewById(C4558R.C4560id.btnRectangle);
            this.mOval = this.mViewToolbar.findViewById(C4558R.C4560id.btnOval);
            this.mUndo = this.mViewToolbar.findViewById(C4558R.C4560id.btnUndo);
            this.mRedo = this.mViewToolbar.findViewById(C4558R.C4560id.btnRedo);
            this.mClear = this.mViewToolbar.findViewById(C4558R.C4560id.btnClear);
            this.mColorIndicator = this.mViewToolbar.findViewById(C4558R.C4560id.btnColorIndicator);
            this.mColorImage = (ColorSelectedImage) this.mViewToolbar.findViewById(C4558R.C4560id.colorImage);
            this.mToggleToolbar = this.mViewToolbar.findViewById(C4558R.C4560id.btnToggleToolbar);
            this.mToggleToolbarBg = this.mViewToolbar.findViewById(C4558R.C4560id.btnToggleToolbarBg);
            this.mToggleToolbarArrow = (ImageView) this.mViewToolbar.findViewById(C4558R.C4560id.btnToggleToolbarArrow);
            this.mToggleToolbar.setVisibility(AccessibilityUtil.isSpokenFeedbackEnabled(this.mContext) ? 8 : 0);
            if (this.isAnnotationOff) {
                this.mAnnotation.setVisibility(8);
                this.mSpotlight.setVisibility(8);
                this.mPen.setVisibility(8);
                this.mHighlight.setVisibility(8);
                this.mLine.setVisibility(8);
                this.mRectangle.setVisibility(8);
                this.mOval.setVisibility(8);
                this.mUndo.setVisibility(8);
                this.mRedo.setVisibility(8);
                this.mClear.setVisibility(8);
                this.mColorIndicator.setVisibility(8);
                this.mColorImage.setVisibility(8);
            }
            this.mToolbarLayoutParams.type = getWindowLayoutParamsType(this.mContext);
            this.mToolbarLayoutParams.flags |= 1320;
            LayoutParams layoutParams = this.mToolbarLayoutParams;
            layoutParams.format = 1;
            setToolbarLayoutParamsWidth(layoutParams);
            LayoutParams layoutParams2 = this.mToolbarLayoutParams;
            layoutParams2.height = -2;
            layoutParams2.gravity = 51;
            int height = this.mViewToolbar.getHeight();
            if (height == 0) {
                this.mViewToolbar.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
                this.mViewToolbar.measure(this.mToolbarLayoutParams.width, 0);
                height = this.mViewToolbar.getMeasuredHeight();
            }
            LayoutParams layoutParams3 = this.mToolbarLayoutParams;
            layoutParams3.x = this.mToolBarMargin;
            layoutParams3.y = (this.mDisplay.getHeight() - height) - this.mToolBarMargin;
            this.loctParamNow.marginLeft = this.mToolbarLayoutParams.x;
            this.loctParamNow.marginTop = this.mToolbarLayoutParams.y;
            this.mToolbarLayoutParams.windowAnimations = 16973828;
            this.mViewToolbar.findViewById(C4558R.C4560id.btnAnnotation).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnSpotlight).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnHighlight).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnClear).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnColorIndicator).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnStopShare).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnPen).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnAutoLine).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnRectangle).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnOval).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnUndo).setOnClickListener(this);
            this.mViewToolbar.findViewById(C4558R.C4560id.btnRedo).setOnClickListener(this);
            this.mToggleToolbar.setOnClickListener(this);
            this.mViewToolbar.setGestureDetectorListener(new GuestureListener());
            this.mWindowManager.addView(this.mViewToolbar, this.mToolbarLayoutParams);
            this.mViewToolbar.setVisibility(8);
        }
    }

    private void addAnnotation() {
        if (this.mWindowManager != null) {
            this.mDrawingView = new AnnotateView(this.mContext);
            this.mDrawingView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            LayoutParams layoutParams = new LayoutParams();
            layoutParams.type = getWindowLayoutParamsType(this.mContext);
            layoutParams.format = 1;
            layoutParams.width = -1;
            layoutParams.height = -1;
            this.mDrawingView.setEnabled(true);
            this.mDrawingView.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(View view, int i, @NonNull KeyEvent keyEvent) {
                    if (i == 4 && keyEvent.getAction() == 1) {
                        ShareScreenAnnoToolbar.this.onBackPressed();
                    }
                    return false;
                }
            });
            this.mWindowManager.addView(this.mDrawingView, layoutParams);
            this.mDrawingView.setVisibility(8);
            this.mDrawingView.setEnabled(true);
        }
    }

    public void destroy() {
        if (this.mWindowManager != null) {
            if (this.mViewToolbar != null) {
                Handler handler = this.mToolbarDragViewHandler;
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                    this.mToolbarDragViewHandler = null;
                }
                this.mViewToolbar.removeCallbacks(this.mRunnableShowToolbar);
                this.mWindowManager.removeView(this.mViewToolbar);
            }
            AnnotateView annotateView = this.mDrawingView;
            if (annotateView != null) {
                annotateView.stopAnnotation();
                this.mWindowManager.removeView(this.mDrawingView);
            }
            View view = this.mColorDismissView;
            if (view != null) {
                this.mWindowManager.removeView(view);
            }
            ColorAndLineWidthView colorAndLineWidthView = this.mColorAndLineWidthView;
            if (colorAndLineWidthView != null) {
                this.mWindowManager.removeView(colorAndLineWidthView);
            }
        }
        this.mViewToolbar = null;
        this.mDrawingView = null;
        this.mColorDismissView = null;
        this.mColorAndLineWidthView = null;
        this.mState = 1;
        this.isAnnotationOff = false;
    }

    /* access modifiers changed from: private */
    public void dragFinish() {
        this.mToolbarBg.setBackgroundResource(C4558R.C4559drawable.zm_screenshare_toolbar_bg_normal);
        this.mToggleToolbarBg.setBackgroundResource(C4558R.C4559drawable.zm_screenshare_toolbar_bg_normal);
        checkStatus();
    }

    private void updateSelection() {
        if (!this.isAnnotationOff && this.mViewToolbar != null && this.mDrawingView != null) {
            this.mSpotlight.setSelected(false);
            this.mHighlight.setSelected(false);
            this.mPen.setSelected(false);
            this.mLine.setSelected(false);
            this.mRectangle.setSelected(false);
            this.mOval.setSelected(false);
            this.mUndo.setPressed(false);
            this.mRedo.setPressed(false);
            this.mClear.setPressed(false);
            switch (this.mAnnoDataMgr.getTool()) {
                case ANNO_TOOL_TYPE_SPOTLIGHT:
                    this.mSpotlight.setSelected(true);
                    break;
                case ANNO_TOOL_TYPE_HIGHLIGHTER:
                    this.mHighlight.setSelected(true);
                    break;
                case ANNO_TOOL_TYPE_PEN:
                    this.mPen.setSelected(true);
                    break;
                case ANNO_TOOL_TYPE_AUTO_LINE:
                    this.mLine.setSelected(true);
                    break;
                case ANNO_TOOL_TYPE_AUTO_RECTANGLE:
                    this.mRectangle.setSelected(true);
                    break;
                case ANNO_TOOL_TYPE_AUTO_ELLIPSE:
                    this.mOval.setSelected(true);
                    break;
            }
            this.mColorImage.setColor(this.mAnnoDataMgr.getColor(AnnoToolType.ANNO_TOOL_TYPE_PEN));
        }
    }

    private void onClickStopShare() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onClickStopShare();
        }
    }

    private void onAnnoStatusChanged() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onAnnoStatusChanged();
        }
    }

    private void showAnnotation() {
        if (this.mViewToolbar != null) {
            AnnotateView annotateView = this.mDrawingView;
            if (annotateView != null) {
                int displayWidth = UIUtil.getDisplayWidth(annotateView.getContext());
                int displayHeight = UIUtil.getDisplayHeight(this.mDrawingView.getContext());
                if (UiModeUtil.isInDesktopMode(this.mContext)) {
                    this.mDrawingView.initBackgroundCanvasSize(displayWidth, displayHeight);
                } else {
                    int max = Math.max(displayWidth, displayHeight);
                    this.mDrawingView.initBackgroundCanvasSize(max, max);
                }
                this.mDrawingView.setVisibility(0);
                ZoomAnnotate zoomAnnotateMgr = ConfMgr.getInstance().getZoomAnnotateMgr();
                if (zoomAnnotateMgr != null) {
                    Display display = this.mDisplay;
                    if (display != null) {
                        zoomAnnotateMgr.setScreenSize(display.getWidth() / 2, this.mDisplay.getHeight() / 2);
                        updateSelection();
                    }
                }
            }
        }
    }

    public boolean isAnnotationStart() {
        return this.mState == 2;
    }

    /* access modifiers changed from: private */
    public void onBackPressed() {
        ColorAndLineWidthView colorAndLineWidthView = this.mColorAndLineWidthView;
        if (colorAndLineWidthView == null || this.mColorDismissView == null || !colorAndLineWidthView.isShowing() || !this.mColorDismissView.isShown()) {
            AnnotateView annotateView = this.mDrawingView;
            if (annotateView != null && annotateView.isShown()) {
                toggleAnnotation(false);
                return;
            }
            return;
        }
        this.mColorAndLineWidthView.dismiss();
        this.mColorDismissView.setVisibility(8);
    }

    private void setAnnotateDisable(boolean z) {
        AnnoDataMgr annoDataMgr = this.mAnnoDataMgr;
        if (!AnnoDataMgr.getInstance().getAttendeeAnnotateDisable()) {
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null) {
                shareObj.DisableAttendeeAnnotationForMySharedContent(z);
            }
        }
    }

    public void setAnnoToolbarVisible(boolean z) {
        toggleAnnotation(z);
    }

    public void onClick(@NonNull View view) {
        if (this.isAnnotationOff || this.mDrawingView != null) {
            if (C4558R.C4560id.btnAnnotation == view.getId()) {
                toggleAnnotation(!isAnnotationStart());
            } else if (C4558R.C4560id.btnSpotlight == view.getId()) {
                this.mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_SPOTLIGHT);
            } else if (C4558R.C4560id.btnPen == view.getId()) {
                this.mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_PEN);
            } else if (C4558R.C4560id.btnHighlight == view.getId()) {
                this.mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_HIGHLIGHTER);
            } else if (C4558R.C4560id.btnAutoLine == view.getId()) {
                this.mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_AUTO_LINE);
            } else if (C4558R.C4560id.btnRectangle == view.getId()) {
                this.mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_AUTO_RECTANGLE);
            } else if (C4558R.C4560id.btnOval == view.getId()) {
                this.mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_AUTO_ELLIPSE);
            } else if (C4558R.C4560id.btnUndo == view.getId()) {
                this.mAnnoDataMgr.undo();
            } else if (C4558R.C4560id.btnRedo == view.getId()) {
                this.mAnnoDataMgr.redo();
            } else if (C4558R.C4560id.btnClear == view.getId()) {
                this.mAnnoDataMgr.eraser(AnnoClearType.ANNO_CLEAR_ALL.ordinal());
            } else if (C4558R.C4560id.btnColorIndicator == view.getId()) {
                ColorAndLineWidthView colorAndLineWidthView = this.mColorAndLineWidthView;
                if (colorAndLineWidthView != null) {
                    if (colorAndLineWidthView.isShowing()) {
                        this.mColorAndLineWidthView.dismiss();
                        this.mColorDismissView.setVisibility(8);
                    } else {
                        Display display = this.mDisplay;
                        if (display != null) {
                            int height = display.getHeight();
                            int i = this.mToolbarLayoutParams.y;
                            int i2 = this.mToolbarLayoutParams.x + this.mToolBarMargin;
                            int dip2px = UIUtil.dip2px(this.mContext, 2.0f);
                            if (i > height / 2) {
                                this.mColorAndLineWidthView.show(view, i2, ((i - this.mColorAndLineWidthView.mHeight) + this.mToolBarMargin) - dip2px, false);
                            } else {
                                this.mColorAndLineWidthView.show(view, i2, ((i + this.mViewToolbar.getHeight()) - this.mToolBarMargin) + dip2px, true);
                            }
                            this.mColorDismissView.setVisibility(0);
                        } else {
                            return;
                        }
                    }
                }
            } else if (C4558R.C4560id.btnStopShare == view.getId()) {
                ShareSessionMgr.setAnnotateDisableWhenStopShare();
                onClickStopShare();
                return;
            } else if (C4558R.C4560id.btnToggleToolbar == view.getId()) {
                clickToolBar();
            }
            if (C4558R.C4560id.btnColorIndicator != view.getId()) {
                ColorAndLineWidthView colorAndLineWidthView2 = this.mColorAndLineWidthView;
                if (colorAndLineWidthView2 != null && colorAndLineWidthView2.isShown()) {
                    this.mColorAndLineWidthView.setVisibility(8);
                    this.mColorDismissView.setVisibility(8);
                }
            }
            updateSelection();
            dragFinish();
        }
    }

    public void onColorPicked(int i) {
        if (this.mDrawingView != null) {
            this.mAnnoDataMgr.setColor(i);
            this.mColorImage.setColor(i);
        }
    }
}
