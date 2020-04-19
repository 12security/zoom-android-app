package com.zipow.annotate;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.annotate.annoDialog.AnnotateClearView;
import com.zipow.annotate.annoDialog.AnnotateColorView;
import com.zipow.annotate.annoDialog.AnnotateDialog;
import com.zipow.annotate.annoDialog.AnnotateLineView;
import com.zipow.annotate.annoDialog.AnnotateMoreView;
import com.zipow.annotate.annoMultiPage.AnnoMultiPagesFragment;
import com.zipow.annotate.render.AnnotateTextData;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.param.ZMConfRequestConstant;
import com.zipow.videobox.dialog.PermissionUnableAccessDialog;
import com.zipow.videobox.share.ColorAndLineWidthView;
import com.zipow.videobox.share.ColorSelectedImage;
import com.zipow.videobox.share.IColorChangedListener;
import com.zipow.videobox.share.IDrawingViewListener;
import com.zipow.videobox.share.ShareBaseView;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.view.ToolbarButton;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class AnnotateDrawingView extends ShareBaseView implements IColorChangedListener, IDrawingViewListener, OnClickListener {
    private static final String TAG = AnnotateDrawingView.class.getName();
    /* access modifiers changed from: private */
    public static final AnnoDataMgr mAnnoDataMgr = AnnoDataMgr.getInstance();
    @Nullable
    private AnnoMultiPagesFragment mAnnoMultiPagesFragment;
    @Nullable
    private AnnotateClearView mAnnotateClearView;
    @Nullable
    private AnnotateColorView mAnnotateColorView;
    @Nullable
    private AnnotateLineView mAnnotateLineView;
    @Nullable
    private AnnotateMoreView mAnnotateMoreView;
    private ToolbarButton mArrowBtn;
    private TextView mBlackBtn;
    private TextView mBlackColor;
    private TextView mBlueBtn;
    private TextView mBlueColor;
    private TextView mBoldBtn;
    private TextView mClean;
    private ToolbarButton mCleanBtn;
    private ImageView mCloseBtn;
    @Nullable
    private ColorAndLineWidthView mColorAndLineWidthView;
    private ColorSelectedImage mColorImage;
    private View mColorIndicator;
    private AnnotateView mDrawingView;
    private EditText mEditText;
    private int mEditTextCurPosX = 0;
    private int mEditTextCurPosY = 0;
    private int mEditTextLastPosX = 0;
    private int mEditTextLastPosY = 0;
    private TextView mErase;
    private ToolbarButton mEraseBtn;
    private TextView mExtend;
    private TextView mGreenBtn;
    private TextView mGreenColor;
    private ToolbarButton mHighlightBtn;
    private TextView mItalicBtn;
    private int mLeft = 0;
    private ToolbarButton mLineBtn;
    private TextView mLineWidth;
    private View mMobileClean;
    private View mMobileClose;
    private View mMobileMore;
    private View mMobileRedo;
    private View mMobileTopBar;
    private View mMobileUndo;
    private TextView mMore;
    private TextView mNewPage;
    private float mOffsetX = 0.0f;
    private float mOffsetY = 0.0f;
    private ToolbarButton mOvalBtn;
    private TextView mPageManagement;
    private RelativeLayout mPageManagementLayout;
    private TextView mPageNum;
    private TextView mPen;
    private ToolbarButton mPenBtn;
    private ToolbarButton mRectangleBtn;
    private TextView mRedBtn;
    private TextView mRedColor;
    private TextView mRedo;
    private ToolbarButton mRedoBtn;
    private long mRequestPermissionTime;
    private PopupWindow mSaveTableView;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    private TextView mShrink;
    private TextView mSpotlight;
    private ToolbarButton mSpotlightBtn;
    private TextView mText;
    private ToolbarButton mTextBtn;
    private AnnotateTextData mTextData;
    private SeekBar mTextSizeSeekBar;
    private View mTextToolbars;
    private int mTop = 0;
    private TextView mUndo;
    private ToolbarButton mUndoBtn;
    private View mView;
    private View mViewToolbars;
    private View mWbToolbar;
    private TextView mYellowBtn;
    private TextView mYellowColor;
    private float mZoomFactor = 1.0f;
    private boolean mbEditModle;

    public enum AnnoClearType {
        ANNO_CLEAR_MY,
        ANNO_CLEAR_ALL,
        ANNO_CLEAR_OTHERS
    }

    enum AnnoParagraphAlignment {
        ANNO_PARAGRAPH_ALIGNMENT_LEFT,
        ANNO_PARAGRAPH_ALIGNMENT_CENTER,
        ANNO_PARAGRAPH_ALIGNMENT_RIGHT,
        ANNO_PARAGRAPH_ALIGNMENT_JUSTIFY,
        ANNO_PARAGRAPH_ALIGNMENT_JUSTIFY_LOW,
        ANNO_PARAGRAPH_ALIGNMENT_NATURAL,
        ANNO_PARAGRAPH_ALIGNMENT_DISTRIBUTE,
        ANNO_PARAGRAPH_ALIGNMENT_THAI_DISTRIBUTE
    }

    public enum AnnoTipType {
        ANNO_LINE_TIP,
        ANNO_COLOR_TIP,
        ANNO_CLEAR_TIP,
        ANNO_MORE_TIP,
        ANNO_SAVE_TIP,
        ANNO_CREATE_PAGE_TIP,
        ANNO_EDIT_TIP,
        ANNO_CHECK_TIP
    }

    public void onSavePageSnapshot(int i) {
    }

    public AnnotateDrawingView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AnnotateDrawingView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public AnnotateDrawingView(@NonNull Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        mAnnoDataMgr.registerUpdateListener(this);
        this.mView = LayoutInflater.from(context).inflate(C4558R.layout.zm_share_draw_view, null, false);
        initAnnotateView(context);
        initTextBox();
        addView(this.mView);
    }

    private void initAnnotateView(Context context) {
        boolean isTablet = UIUtil.isTablet(this.mView.getContext());
        this.mDrawingView = (AnnotateView) this.mView.findViewById(C4558R.C4560id.annotateView);
        this.mDrawingView.registerUpdateListener(this);
        this.mDrawingView.setEnabled(true);
        this.mMobileTopBar = this.mView.findViewById(C4558R.C4560id.id_mobile_top_bar);
        this.mMobileClose = this.mView.findViewById(C4558R.C4560id.id_mobile_close);
        this.mMobileUndo = this.mView.findViewById(C4558R.C4560id.id_mobile_undo);
        this.mMobileRedo = this.mView.findViewById(C4558R.C4560id.id_mobile_redo);
        this.mMobileClean = this.mView.findViewById(C4558R.C4560id.id_mobile_clean);
        this.mMobileMore = this.mView.findViewById(C4558R.C4560id.id_mobile_more);
        this.mMobileTopBar.setVisibility(8);
        this.mMobileClose.setOnClickListener(this);
        this.mMobileUndo.setOnClickListener(this);
        this.mMobileRedo.setOnClickListener(this);
        this.mMobileClean.setOnClickListener(this);
        this.mMobileMore.setOnClickListener(this);
        this.mUndo = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_undo);
        this.mRedo = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_redo);
        this.mPen = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_pen);
        this.mErase = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_erase);
        this.mBlackColor = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_black_color);
        this.mRedColor = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_red_color);
        this.mYellowColor = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_yellow_color);
        this.mGreenColor = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_green_color);
        this.mBlueColor = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_blue_color);
        this.mText = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_text);
        this.mLineWidth = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_lineWidth);
        this.mSpotlight = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_Spotlight);
        this.mExtend = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_extend);
        this.mShrink = (TextView) this.mView.findViewById(C4558R.C4560id.id_wb_shrink);
        this.mClean = (TextView) this.mView.findViewById(C4558R.C4560id.cleanBtn);
        this.mPageManagementLayout = (RelativeLayout) this.mView.findViewById(C4558R.C4560id.pageManagementLayout);
        this.mPageManagement = (TextView) this.mView.findViewById(C4558R.C4560id.pageManagementBtn);
        this.mPageNum = (TextView) this.mView.findViewById(C4558R.C4560id.pageNumTextView);
        this.mNewPage = (TextView) this.mView.findViewById(C4558R.C4560id.newPageBtn);
        this.mMore = (TextView) this.mView.findViewById(C4558R.C4560id.moreBtn);
        this.mUndo.setOnClickListener(this);
        this.mRedo.setOnClickListener(this);
        this.mPen.setOnClickListener(this);
        this.mErase.setOnClickListener(this);
        this.mBlackColor.setOnClickListener(this);
        this.mRedColor.setOnClickListener(this);
        this.mYellowColor.setOnClickListener(this);
        this.mGreenColor.setOnClickListener(this);
        this.mBlueColor.setOnClickListener(this);
        this.mText.setOnClickListener(this);
        this.mLineWidth.setOnClickListener(this);
        this.mSpotlight.setOnClickListener(this);
        this.mExtend.setOnClickListener(this);
        this.mShrink.setOnClickListener(this);
        this.mClean.setOnClickListener(this);
        this.mPageManagement.setOnClickListener(this);
        this.mNewPage.setOnClickListener(this);
        this.mMore.setOnClickListener(this);
        this.mUndo.setVisibility(isTablet ? 0 : 8);
        this.mRedo.setVisibility(isTablet ? 0 : 8);
        this.mWbToolbar = this.mView.findViewById(C4558R.C4560id.annotate_bar);
        this.mWbToolbar.setVisibility(8);
        extendAnnoTool(false);
        this.mCloseBtn = (ImageView) this.mView.findViewById(C4558R.C4560id.shareEditBtn);
        this.mCloseBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnnotateDrawingView.this.closeAnnoToolbar();
            }
        });
        this.mViewToolbars = this.mView.findViewById(C4558R.C4560id.drawingtools);
        this.mViewToolbars.setVisibility(8);
        this.mSpotlightBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnSpotlight);
        this.mHighlightBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnHighlight);
        this.mPenBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnPen);
        this.mEraseBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnErase);
        this.mColorIndicator = this.mView.findViewById(C4558R.C4560id.btnColorIndicator);
        this.mColorImage = (ColorSelectedImage) this.mColorIndicator.findViewById(C4558R.C4560id.colorImage);
        this.mLineBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnAutoLine);
        this.mArrowBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnArrow);
        this.mRectangleBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnRectangle);
        this.mOvalBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnOval);
        this.mTextBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnText);
        this.mUndoBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnUndo);
        this.mRedoBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnRedo);
        this.mCleanBtn = (ToolbarButton) this.mView.findViewById(C4558R.C4560id.btnClear);
        this.mLineBtn.setVisibility(isTablet ? 0 : 8);
        this.mRectangleBtn.setVisibility(isTablet ? 0 : 8);
        this.mOvalBtn.setVisibility(isTablet ? 0 : 8);
        this.mTextBtn.setVisibility(isTablet ? 0 : 8);
        this.mUndoBtn.setVisibility(isTablet ? 0 : 8);
        this.mRedoBtn.setVisibility(isTablet ? 0 : 8);
        this.mCleanBtn.setVisibility(0);
        this.mArrowBtn.setVisibility(8);
        this.mSpotlightBtn.setVisibility(8);
        this.mSpotlightBtn.setOnClickListener(this);
        this.mHighlightBtn.setOnClickListener(this);
        this.mPenBtn.setOnClickListener(this);
        this.mEraseBtn.setOnClickListener(this);
        this.mColorIndicator.setOnClickListener(this);
        this.mLineBtn.setOnClickListener(this);
        this.mArrowBtn.setOnClickListener(this);
        this.mRectangleBtn.setOnClickListener(this);
        this.mOvalBtn.setOnClickListener(this);
        this.mTextBtn.setOnClickListener(this);
        this.mUndoBtn.setOnClickListener(this);
        this.mRedoBtn.setOnClickListener(this);
        this.mCleanBtn.setOnClickListener(this);
        View inflate = inflate(getContext(), C4558R.layout.zm_share_savebtn, null);
        Button button = (Button) inflate.findViewById(C4558R.C4560id.savePhotoBtn);
        this.mSaveTableView = new PopupWindow(inflate, -1, UIUtil.dip2px(context, 100.0f));
        this.mSaveTableView.setBackgroundDrawable(getResources().getDrawable(C4558R.C4559drawable.zm_transparent));
        this.mSaveTableView.setContentView(inflate);
        this.mSaveTableView.setFocusable(true);
        this.mSaveTableView.setOutsideTouchable(true);
        button.setOnClickListener(this);
    }

    public void onClick(@NonNull View view) {
        onDismissAllTip();
        if (view.getId() == C4558R.C4560id.savePhotoBtn) {
            notifySavePhoto();
            this.mSaveTableView.dismiss();
            return;
        }
        if (view == this.mMobileClose) {
            this.mMobileTopBar.setVisibility(8);
            closeAnnoToolbar();
            EditText editText = this.mEditText;
            if (!(editText == null || this.mTextToolbars == null || editText.getVisibility() != 0)) {
                onEndEditing();
                this.mTextToolbars.setVisibility(8);
            }
        } else if (view == this.mUndo || view == this.mMobileUndo) {
            mAnnoDataMgr.undo();
            return;
        } else if (view == this.mRedo || view == this.mMobileRedo) {
            mAnnoDataMgr.redo();
            return;
        } else if (view == this.mPen) {
            checkAndSetAnnoPen();
        } else if (view == this.mErase) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_ERASER);
        } else {
            TextView textView = this.mBlackColor;
            if (view == textView) {
                onColorBtnClicked(textView, mAnnoDataMgr.getColorByIndex(0));
                return;
            }
            TextView textView2 = this.mRedColor;
            if (view == textView2) {
                onColorBtnClicked(textView2, mAnnoDataMgr.getColorByIndex(1));
                return;
            }
            TextView textView3 = this.mYellowColor;
            if (view == textView3) {
                onColorBtnClicked(textView3, mAnnoDataMgr.getColorByIndex(2));
                return;
            }
            TextView textView4 = this.mGreenColor;
            if (view == textView4) {
                onColorBtnClicked(textView4, mAnnoDataMgr.getColorByIndex(3));
                return;
            }
            TextView textView5 = this.mBlueColor;
            if (view == textView5) {
                onColorBtnClicked(textView5, mAnnoDataMgr.getColorByIndex(4));
                return;
            } else if (view == this.mText) {
                mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_TEXTBOX);
            } else if (view == this.mLineWidth) {
                onShowAnnoTip(AnnoTipType.ANNO_LINE_TIP, this.mLineWidth.getId());
            } else if (view == this.mSpotlight) {
                mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_SPOTLIGHT);
            } else if (view == this.mExtend) {
                extendAnnoTool(true);
                return;
            } else if (view == this.mShrink) {
                extendAnnoTool(false);
                return;
            } else if (view == this.mClean || view == this.mMobileClean) {
                onShowAnnoTip(AnnoTipType.ANNO_CLEAR_TIP, view.getId());
                return;
            } else if (view == this.mPageManagement) {
                onPageManagementClicked();
                return;
            } else if (view == this.mNewPage) {
                onNewPageClicked();
                return;
            } else if (view == this.mMore || view == this.mMobileMore) {
                onShowAnnoTip(AnnoTipType.ANNO_MORE_TIP, view.getId());
                return;
            }
        }
        if (view == this.mSpotlightBtn) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_SPOTLIGHT);
        } else if (view == this.mPenBtn) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_PEN);
        } else if (view == this.mHighlightBtn) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_HIGHLIGHTER);
        } else if (view == this.mEraseBtn) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_ERASER);
        } else if (view == this.mColorIndicator) {
            ColorAndLineWidthView colorAndLineWidthView = this.mColorAndLineWidthView;
            if (colorAndLineWidthView != null) {
                if (colorAndLineWidthView.isShowing()) {
                    this.mColorAndLineWidthView.dismiss();
                } else {
                    this.mColorAndLineWidthView.show(this.mColorIndicator);
                }
            }
            return;
        } else if (view == this.mLineBtn) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_AUTO_LINE);
        } else if (view == this.mArrowBtn) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_AUTO_ARROW2);
        } else if (view == this.mRectangleBtn) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_AUTO_RECTANGLE);
        } else if (view == this.mOvalBtn) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_AUTO_ELLIPSE);
        } else if (view == this.mTextBtn) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_TEXTBOX);
        } else if (view == this.mUndoBtn) {
            mAnnoDataMgr.undo();
            return;
        } else if (view == this.mRedoBtn) {
            mAnnoDataMgr.redo();
            return;
        } else if (view == this.mCleanBtn) {
            if (mAnnoDataMgr.isPresenter()) {
                mAnnoDataMgr.eraser(AnnoClearType.ANNO_CLEAR_ALL.ordinal());
            } else {
                mAnnoDataMgr.eraser(AnnoClearType.ANNO_CLEAR_MY.ordinal());
            }
            return;
        } else {
            TextView textView6 = this.mBlackBtn;
            if (view == textView6) {
                updateTextColor(textView6, mAnnoDataMgr.getColorByIndex(0));
            } else {
                TextView textView7 = this.mRedBtn;
                if (view == textView7) {
                    updateTextColor(textView7, mAnnoDataMgr.getColorByIndex(1));
                } else {
                    TextView textView8 = this.mYellowBtn;
                    if (view == textView8) {
                        updateTextColor(textView8, mAnnoDataMgr.getColorByIndex(2));
                    } else {
                        TextView textView9 = this.mGreenBtn;
                        if (view == textView9) {
                            updateTextColor(textView9, mAnnoDataMgr.getColorByIndex(3));
                        } else {
                            TextView textView10 = this.mBlueBtn;
                            if (view == textView10) {
                                updateTextColor(textView10, mAnnoDataMgr.getColorByIndex(4));
                            } else if (view == this.mBoldBtn) {
                                setTextBold(true);
                            } else if (view == this.mItalicBtn) {
                                setTextItalic(true);
                            }
                        }
                    }
                }
            }
        }
        updateSelection(view);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            this.mScreenWidth = i3 - i;
            this.mScreenHeight = i4 - i2;
            updateAnnotateWndSize();
            onDismissAllTip();
            updateAnnoToolbarStatus(false);
        }
    }

    private void updateAnnoToolbarStatus(boolean z) {
        boolean isSharingWhiteboard = mAnnoDataMgr.isSharingWhiteboard();
        boolean isLandscapeMode = UIUtil.isLandscapeMode(getContext());
        boolean isPresenter = mAnnoDataMgr.isPresenter();
        boolean isTablet = UIUtil.isTablet(this.mView.getContext());
        if (!mAnnoDataMgr.isPresenter() || !mAnnoDataMgr.isSharingWhiteboard()) {
            this.mMobileTopBar.setVisibility(8);
            this.mWbToolbar.setVisibility(8);
            updateSelection(this.mPenBtn);
        } else {
            this.mViewToolbars.setVisibility(8);
            updateWBAnnoToolSelectedStatus();
        }
        if (z || (isSharingWhiteboard && (!isLandscapeMode || !isTablet))) {
            checkAnnoColorView();
        }
        int i = 0;
        if (isSharingWhiteboard) {
            this.mNewPage.setVisibility((!isLandscapeMode || !isTablet) ? 8 : 0);
            AnnoPageInfo annoPageNum = mAnnoDataMgr.getAnnoPageNum();
            this.mPageManagementLayout.setVisibility((annoPageNum == null || annoPageNum.mTotalPageNum <= 1 || !isLandscapeMode || !isTablet) ? 8 : 0);
            this.mMore.setVisibility(isTablet ? 0 : 8);
            this.mClean.setVisibility(isTablet ? 0 : 8);
            this.mMobileRedo.setVisibility(isTablet ? 8 : 0);
            this.mMobileUndo.setVisibility(isTablet ? 8 : 0);
            if (z) {
                this.mText.setVisibility(8);
                this.mLineWidth.setVisibility(8);
                this.mSpotlight.setVisibility(8);
                this.mShrink.setVisibility(8);
                this.mExtend.setVisibility(0);
            }
            if (!isTablet) {
                this.mExtend.setVisibility(8);
                this.mShrink.setVisibility(8);
                this.mNewPage.setVisibility(8);
                this.mMobileClean.setVisibility(0);
                this.mMobileMore.setVisibility(0);
                this.mLineWidth.setVisibility(isPresenter ? 0 : 8);
                this.mSpotlight.setVisibility(isPresenter ? 0 : 8);
            }
            updateLineWidthDrawable();
        }
        this.mArrowBtn.setVisibility(isPresenter ? 8 : 0);
        ToolbarButton toolbarButton = this.mSpotlightBtn;
        if (!isPresenter) {
            i = 8;
        }
        toolbarButton.setVisibility(i);
    }

    private void updateSelection(@Nullable View view) {
        if (view != null) {
            this.mPen.setSelected(false);
            this.mErase.setSelected(false);
            this.mText.setSelected(false);
            this.mLineWidth.setSelected(false);
            this.mSpotlight.setSelected(false);
            this.mSpotlightBtn.setSelected(false);
            this.mHighlightBtn.setSelected(false);
            this.mPenBtn.setSelected(false);
            this.mEraseBtn.setSelected(false);
            this.mLineBtn.setSelected(false);
            this.mArrowBtn.setSelected(false);
            this.mRectangleBtn.setSelected(false);
            this.mOvalBtn.setSelected(false);
            this.mTextBtn.setSelected(false);
            view.setSelected(true);
            this.mColorImage.setColor(mAnnoDataMgr.getColor(AnnoToolType.ANNO_TOOL_TYPE_PEN));
        }
    }

    private void checkAnnoColorView() {
        int curToolColor = mAnnoDataMgr.getCurToolColor();
        if (mAnnoDataMgr.isSharingWhiteboard()) {
            TextView textView = curToolColor == mAnnoDataMgr.getColorByIndex(0) ? this.mBlackColor : curToolColor == mAnnoDataMgr.getColorByIndex(1) ? this.mRedColor : curToolColor == mAnnoDataMgr.getColorByIndex(2) ? this.mYellowColor : curToolColor == mAnnoDataMgr.getColorByIndex(3) ? this.mGreenColor : curToolColor == mAnnoDataMgr.getColorByIndex(4) ? this.mBlueColor : null;
            if (textView == null) {
                textView = this.mBlackColor;
            }
            updateColorSelection(textView, 8);
            return;
        }
        this.mColorImage.setColor(curToolColor);
    }

    private void onColorBtnClicked(@NonNull View view, int i) {
        mAnnoDataMgr.setColor(i);
        if (!UIUtil.isLandscapeMode(getContext()) || !UIUtil.isTablet(this.mView.getContext())) {
            onShowAnnoTip(AnnoTipType.ANNO_COLOR_TIP, view.getId());
        } else {
            updateColorSelection(view, 0);
            checkAndSetAnnoPen();
        }
        this.mColorImage.setColor(i);
    }

    /* access modifiers changed from: private */
    public void checkAndSetAnnoPen() {
        AnnotateMoreView annotateMoreView = this.mAnnotateMoreView;
        if (annotateMoreView == null || !annotateMoreView.isShapeRecognitionChecked()) {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_PEN);
        } else {
            mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_MULTI_SHAPE_DETECTOR);
        }
        updateSelection(this.mPen);
    }

    private void setColorVisibility(int i) {
        this.mBlackColor.setVisibility(i);
        this.mRedColor.setVisibility(i);
        this.mYellowColor.setVisibility(i);
        this.mGreenColor.setVisibility(i);
        this.mBlueColor.setVisibility(i);
    }

    private void updateColorSelection(View view, int i) {
        setColorVisibility(i);
        this.mBlackColor.setSelected(false);
        this.mRedColor.setSelected(false);
        this.mYellowColor.setSelected(false);
        this.mGreenColor.setSelected(false);
        this.mBlueColor.setSelected(false);
        view.setSelected(true);
        view.setVisibility(0);
    }

    private void extendAnnoTool(boolean z) {
        this.mExtend.setVisibility(z ? 8 : 0);
        this.mShrink.setVisibility(z ? 0 : 8);
        this.mText.setVisibility(z ? 0 : 8);
        this.mLineWidth.setVisibility(z ? 0 : 8);
        this.mSpotlight.setVisibility((!z || !mAnnoDataMgr.isPresenter()) ? 8 : 0);
        if (mAnnoDataMgr.getCompserVersion() < 410 || !UIUtil.isTablet(this.mView.getContext()) || !z) {
            this.mText.setVisibility(8);
        } else {
            this.mText.setVisibility(0);
        }
    }

    private void updateTextColor(@Nullable View view, int i) {
        if (view != null) {
            this.mEditText.setTextColor(i);
            this.mBlackBtn.setSelected(false);
            this.mRedBtn.setSelected(false);
            this.mYellowBtn.setSelected(false);
            this.mGreenBtn.setSelected(false);
            this.mBlueBtn.setSelected(false);
            view.setSelected(true);
            PreferenceUtil.saveIntValue(mAnnoDataMgr.isSharingWhiteboard() ? PreferenceUtil.WHITEBOARD_TEXT_COLOR : PreferenceUtil.ANNOTATE_TEXT_COLOR, i);
        }
    }

    /* access modifiers changed from: private */
    public void closeAnnoToolbar() {
        this.mMobileTopBar.setVisibility(8);
        if (mAnnoDataMgr.isSharingWhiteboard()) {
            this.mWbToolbar.setVisibility(8);
        } else {
            this.mViewToolbars.setVisibility(8);
        }
        notifyCloseView();
    }

    private void checkEditTextPosition(int i, int i2) {
        if (this.mViewToolbars != null) {
            int displayWidth = UIUtil.getDisplayWidth(getContext());
            int width = this.mEditText.getWidth() + this.mEditText.getPaddingLeft();
            if (displayWidth - i < width) {
                i = displayWidth - width;
            }
            int i3 = 0;
            int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (identifier > 0) {
                i3 = getResources().getDimensionPixelSize(identifier);
            }
            int displayHeight = UIUtil.getDisplayHeight(getContext()) - this.mViewToolbars.getMeasuredHeight();
            int height = this.mEditText.getHeight() + (this.mEditText.getPaddingTop() * 2);
            if ((displayHeight - i2) - i3 < height) {
                i2 = (displayHeight - height) - i3;
            }
            moveEditText(i + this.mLeft, i2 + this.mTop);
        }
    }

    private void initTextBox() {
        this.mTextData = new AnnotateTextData();
        this.mTextToolbars = this.mView.findViewById(C4558R.C4560id.drawingTexttools);
        this.mTextToolbars.setVisibility(8);
        this.mEditText = (EditText) this.mView.findViewById(C4558R.C4560id.editText);
        this.mEditText.setVisibility(8);
        this.mRedBtn = (TextView) this.mView.findViewById(C4558R.C4560id.id_anno_text_red);
        this.mGreenBtn = (TextView) this.mView.findViewById(C4558R.C4560id.id_anno_text_green);
        this.mBlueBtn = (TextView) this.mView.findViewById(C4558R.C4560id.id_anno_text_blue);
        this.mYellowBtn = (TextView) this.mView.findViewById(C4558R.C4560id.id_anno_text_yellow);
        this.mBlackBtn = (TextView) this.mView.findViewById(C4558R.C4560id.id_anno_text_black);
        this.mBoldBtn = (TextView) this.mView.findViewById(C4558R.C4560id.id_anno_text_bold);
        this.mItalicBtn = (TextView) this.mView.findViewById(C4558R.C4560id.id_anno_text_italic);
        this.mTextSizeSeekBar = (SeekBar) this.mView.findViewById(C4558R.C4560id.id_anno_text_font_size_seekbar);
        this.mRedBtn.setOnClickListener(this);
        this.mGreenBtn.setOnClickListener(this);
        this.mBlueBtn.setOnClickListener(this);
        this.mBlueBtn.setSelected(true);
        this.mYellowBtn.setOnClickListener(this);
        this.mBlackBtn.setOnClickListener(this);
        this.mBoldBtn.setOnClickListener(this);
        this.mItalicBtn.setOnClickListener(this);
        this.mTextSizeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                AnnotateDrawingView.this.setTextFontSize(i);
            }
        });
        updateTextColor(this.mBlueBtn, mAnnoDataMgr.getColorByIndex(4));
        initEditText();
        this.mEditText.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                AnnotateDrawingView.this.onEditTextTouch(view, motionEvent);
                return false;
            }
        });
    }

    /* access modifiers changed from: private */
    public void onEditTextTouch(View view, MotionEvent motionEvent) {
        int i;
        int i2;
        if (this.mViewToolbars != null) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mEditTextLastPosX = (int) motionEvent.getRawX();
                this.mEditTextLastPosY = (int) motionEvent.getRawY();
            } else if (action == 2) {
                int rawX = ((int) motionEvent.getRawX()) - this.mEditTextLastPosX;
                int rawY = ((int) motionEvent.getRawY()) - this.mEditTextLastPosY;
                int left = view.getLeft() + rawX;
                int bottom = view.getBottom() + rawY;
                int right = view.getRight() + rawX;
                int top = view.getTop() + rawY;
                int i3 = this.mLeft;
                if (left < i3) {
                    right = i3 + view.getWidth();
                } else {
                    i3 = left;
                }
                int i4 = this.mTop;
                if (top < i4) {
                    bottom = i4 + view.getHeight();
                    top = i4;
                }
                if (right > (this.mScreenWidth - this.mLeft) - mAnnoDataMgr.mVideoGalleryWidth) {
                    i3 = ((this.mScreenWidth - this.mLeft) - mAnnoDataMgr.mVideoGalleryWidth) - view.getWidth();
                }
                if (mAnnoDataMgr.isPresenter()) {
                    i2 = this.mScreenHeight - this.mTop;
                    i = this.mViewToolbars.getMeasuredHeight();
                } else {
                    i2 = this.mScreenHeight;
                    i = this.mTop;
                }
                float f = (float) (i2 - i);
                if (((float) bottom) > f) {
                    top = ((int) f) - view.getHeight();
                }
                moveEditText(i3, top);
                this.mEditTextLastPosX = (int) motionEvent.getRawX();
                this.mEditTextLastPosY = (int) motionEvent.getRawY();
            }
        }
    }

    private void initEditText() {
        this.mBoldBtn.setVisibility(0);
        this.mItalicBtn.setVisibility(0);
        this.mEditText.setText("");
        this.mEditText.setTypeface(Typeface.defaultFromStyle(0));
        if (PreferenceUtil.readBooleanValue(mAnnoDataMgr.isSharingWhiteboard() ? PreferenceUtil.WHITEBOARD_TEXT_BOLD : PreferenceUtil.ANNOTATE_TEXT_BOLD, false)) {
            setTextBold(false);
        }
        if (PreferenceUtil.readBooleanValue(mAnnoDataMgr.isSharingWhiteboard() ? PreferenceUtil.WHITEBOARD_TEXT_ITALIC : PreferenceUtil.ANNOTATE_TEXT_ITALIC, false)) {
            setTextItalic(false);
        }
        int readIntValue = PreferenceUtil.readIntValue(mAnnoDataMgr.isSharingWhiteboard() ? PreferenceUtil.WHITEBOARD_TEXT_SIZE : PreferenceUtil.ANNOTATE_TEXT_SIZE, mAnnoDataMgr.mVideoGalleryHeight);
        if (!mAnnoDataMgr.isPresenter()) {
            readIntValue = (int) (((float) readIntValue) * this.mZoomFactor);
        }
        SeekBar seekBar = this.mTextSizeSeekBar;
        AnnoDataMgr annoDataMgr = mAnnoDataMgr;
        seekBar.setProgress(readIntValue - 48);
        this.mEditText.setTextSize(0, (float) readIntValue);
        int readIntValue2 = PreferenceUtil.readIntValue(mAnnoDataMgr.isSharingWhiteboard() ? PreferenceUtil.WHITEBOARD_TEXT_COLOR : PreferenceUtil.ANNOTATE_TEXT_COLOR, mAnnoDataMgr.getColorByIndex(4));
        TextView textView = this.mBlueBtn;
        if (readIntValue2 == mAnnoDataMgr.getColorByIndex(0)) {
            textView = this.mBlackBtn;
        } else if (readIntValue2 == mAnnoDataMgr.getColorByIndex(1)) {
            textView = this.mRedBtn;
        } else if (readIntValue2 == mAnnoDataMgr.getColorByIndex(2)) {
            textView = this.mYellowBtn;
        } else if (readIntValue2 == mAnnoDataMgr.getColorByIndex(3)) {
            textView = this.mGreenBtn;
        } else if (readIntValue2 == mAnnoDataMgr.getColorByIndex(4)) {
            textView = this.mBlueBtn;
        }
        updateTextColor(textView, readIntValue2);
    }

    private void moveEditText(int i, int i2) {
        LayoutParams layoutParams = (LayoutParams) this.mEditText.getLayoutParams();
        layoutParams.setMargins(i, i2, 0, 0);
        this.mEditText.setLayoutParams(layoutParams);
        float f = ((float) (i - this.mLeft)) - this.mOffsetX;
        float f2 = this.mZoomFactor;
        this.mEditTextCurPosX = (int) (f / f2);
        this.mEditTextCurPosY = (int) ((((float) (i2 - this.mTop)) - this.mOffsetY) / f2);
    }

    private void setTextViewVisible(boolean z) {
        if (this.mTextToolbars != null && this.mEditText != null) {
            int i = 8;
            (mAnnoDataMgr.isSharingWhiteboard() ? this.mWbToolbar : this.mViewToolbars).setVisibility((!this.mbEditModle || z) ? 8 : 0);
            this.mTextToolbars.setVisibility((!z || !this.mbEditModle) ? 8 : 0);
            EditText editText = this.mEditText;
            if (z && this.mbEditModle) {
                i = 0;
            }
            editText.setVisibility(i);
            InputMethodManager inputMethodManager = (InputMethodManager) this.mEditText.getContext().getSystemService("input_method");
            if (inputMethodManager != null) {
                if (z) {
                    this.mEditText.requestFocus();
                    inputMethodManager.toggleSoftInput(0, 2);
                } else {
                    inputMethodManager.hideSoftInputFromWindow(this.mEditText.getWindowToken(), 0);
                }
            }
        }
    }

    private void setTextBold(boolean z) {
        Typeface typeface = this.mEditText.getTypeface();
        boolean z2 = true;
        if (typeface.isBold() || !typeface.isItalic()) {
            if (typeface.isBold() && typeface.isItalic()) {
                this.mEditText.setTypeface(Typeface.defaultFromStyle(2));
            } else if (typeface.isBold()) {
                this.mEditText.setTypeface(Typeface.defaultFromStyle(0));
            } else {
                this.mEditText.setTypeface(Typeface.defaultFromStyle(1));
            }
            z2 = false;
        } else {
            this.mEditText.setTypeface(Typeface.defaultFromStyle(3));
        }
        if (z) {
            PreferenceUtil.saveBooleanValue(mAnnoDataMgr.isSharingWhiteboard() ? PreferenceUtil.WHITEBOARD_TEXT_BOLD : PreferenceUtil.ANNOTATE_TEXT_BOLD, z2);
        }
        this.mBoldBtn.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(z2 ? C4558R.C4559drawable.zm_anno_text_bold_pressed : C4558R.C4559drawable.zm_anno_text_bold_default), null, null, null);
    }

    private void setTextItalic(boolean z) {
        Typeface typeface = this.mEditText.getTypeface();
        boolean z2 = true;
        if (!typeface.isBold() || typeface.isItalic()) {
            if (typeface.isBold() && typeface.isItalic()) {
                this.mEditText.setTypeface(Typeface.defaultFromStyle(1));
            } else if (typeface.isItalic()) {
                this.mEditText.setTypeface(Typeface.defaultFromStyle(0));
            } else {
                this.mEditText.setTypeface(Typeface.defaultFromStyle(2));
            }
            z2 = false;
        } else {
            this.mEditText.setTypeface(Typeface.defaultFromStyle(3));
        }
        if (z) {
            PreferenceUtil.saveBooleanValue(mAnnoDataMgr.isSharingWhiteboard() ? PreferenceUtil.WHITEBOARD_TEXT_ITALIC : PreferenceUtil.ANNOTATE_TEXT_ITALIC, z2);
        }
        this.mItalicBtn.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(z2 ? C4558R.C4559drawable.zm_anno_text_italic_pressed : C4558R.C4559drawable.zm_anno_text_italic_default), null, null, null);
    }

    /* access modifiers changed from: private */
    public void setTextFontSize(int i) {
        int i2 = i + 48;
        this.mEditText.setTextSize(0, !mAnnoDataMgr.isPresenter() ? ((float) i2) * this.mZoomFactor : (float) i2);
        PreferenceUtil.saveIntValue(mAnnoDataMgr.isSharingWhiteboard() ? PreferenceUtil.WHITEBOARD_TEXT_SIZE : PreferenceUtil.ANNOTATE_TEXT_SIZE, i2);
    }

    @NonNull
    private AnnoParagraphAlignment getTextAlignmentAndroid2Anno(int i) {
        AnnoParagraphAlignment annoParagraphAlignment = AnnoParagraphAlignment.ANNO_PARAGRAPH_ALIGNMENT_LEFT;
        if (i == 5) {
            return AnnoParagraphAlignment.ANNO_PARAGRAPH_ALIGNMENT_RIGHT;
        }
        if (i != 17) {
            return annoParagraphAlignment;
        }
        return AnnoParagraphAlignment.ANNO_PARAGRAPH_ALIGNMENT_CENTER;
    }

    public void onAnnotateStartedUp(boolean z, long j) {
        mAnnoDataMgr.startAnnotaion(z, false, j);
        updateAnnoToolbarStatus(true);
        updateAnnotateWndSize();
        this.mDrawingView.startAnnotation();
        initAnnotateModalViews();
        checkAndSetAnnoPen();
        closeAnnoToolbar();
    }

    public void onAnnotateShutDown() {
        mAnnoDataMgr.stopAnnotation();
        this.mDrawingView.stopAnnotation();
        this.mLeft = 0;
        this.mTop = 0;
        this.mOffsetX = 0.0f;
        this.mOffsetY = 0.0f;
        mAnnoDataMgr.setTool(AnnoToolType.ANNO_TOOL_TYPE_NONE);
        updateSelection(this.mPen);
        onDismissAllTip();
        this.mWbToolbar.setVisibility(8);
        this.mViewToolbars.setVisibility(8);
    }

    private void initAnnotateModalViews() {
        Context context = getContext();
        if (mAnnoDataMgr.isSharingWhiteboard() && context != null) {
            this.mAnnotateLineView = new AnnotateLineView(context);
            this.mAnnotateLineView.registerUpdateListener(this);
            this.mAnnotateMoreView = new AnnotateMoreView(context);
            this.mAnnotateMoreView.registerUpdateListener(this);
            this.mAnnotateColorView = new AnnotateColorView(context);
            this.mAnnotateColorView.registerUpdateListener(this);
            this.mAnnotateClearView = new AnnotateClearView(context);
            this.mAnnotateClearView.registerUpdateListener(this);
        }
        this.mColorAndLineWidthView = new ColorAndLineWidthView(getContext());
        this.mColorAndLineWidthView.showAsPopupWindow();
        this.mColorAndLineWidthView.setListener(this);
    }

    private void requestWriteStoragePermission(int i) {
        ConfActivity confActivity = (ConfActivity) getContext();
        if (confActivity != null) {
            this.mRequestPermissionTime = System.currentTimeMillis();
            confActivity.requestPermission("android.permission.WRITE_EXTERNAL_STORAGE", i, 0);
        }
    }

    private boolean hasWriteStoragePermission() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        boolean z = false;
        if (zMActivity == null) {
            return false;
        }
        if (zMActivity.zm_checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            z = true;
        }
        return z;
    }

    public boolean handleRequestPermissionResult(int i, @NonNull String str, int i2) {
        if (!"android.permission.WRITE_EXTERNAL_STORAGE".equals(str) || i2 != 0) {
            long currentTimeMillis = System.currentTimeMillis() - this.mRequestPermissionTime;
            this.mRequestPermissionTime = 0;
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity == null || currentTimeMillis > 1000 || ActivityCompat.shouldShowRequestPermissionRationale(zMActivity, str)) {
                return false;
            }
            PermissionUnableAccessDialog.showDialog(zMActivity.getSupportFragmentManager(), str);
            return true;
        }
        switch (i) {
            case ZMConfRequestConstant.REQUEST_ANNOTATE_WRITE_STORAGE_BY_NEW /*1023*/:
                onNewPageClicked();
                break;
            case 1024:
                onPageManagementClicked();
                break;
            case 1025:
                onSaveWbClicked();
                break;
            default:
                return false;
        }
        return true;
    }

    public void setEditModel(boolean z) {
        this.mbEditModle = z;
        int i = 0;
        this.mMobileTopBar.setVisibility((!mAnnoDataMgr.isSharingWhiteboard() || !z) ? 8 : 0);
        View view = mAnnoDataMgr.isSharingWhiteboard() ? this.mWbToolbar : this.mViewToolbars;
        if (!z) {
            i = 8;
        }
        view.setVisibility(i);
        this.mDrawingView.setEditModel(z);
        refreshAnnotateWndSize();
        if (z) {
            AnnoDataMgr annoDataMgr = mAnnoDataMgr;
            annoDataMgr.setTool(annoDataMgr.getTool());
        }
    }

    public void closeAnnotateView() {
        this.mMobileTopBar.setVisibility(8);
        this.mWbToolbar.setVisibility(8);
        this.mViewToolbars.setVisibility(8);
        notifyCloseView();
    }

    public void pause() {
        if (AnnoToolType.ANNO_TOOL_TYPE_TEXTBOX == mAnnoDataMgr.getTool()) {
            onEndEditing();
        }
    }

    public void resume() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
            if (supportFragmentManager != null) {
                Fragment findFragmentByTag = supportFragmentManager.findFragmentByTag(AnnotateDialog.class.getName());
                if (findFragmentByTag != null) {
                    ((AnnotateDialog) findFragmentByTag).dismiss();
                }
            }
        }
    }

    public void unregisterAnnotateListener() {
        AnnotateView annotateView = this.mDrawingView;
        if (annotateView != null) {
            annotateView.unRegisterUpdateListener(this);
        }
    }

    public void onAnnotateViewSizeChanged() {
        if (this.mDrawingView != null) {
            refreshAnnotateWndSize();
        }
    }

    public void updateWBPageNum(int i, int i2, int i3, int i4) {
        mAnnoDataMgr.pageNumChanged(i3, i4);
        updateAnnoToolbarStatus(false);
        this.mPageNum.setText(String.valueOf(i2));
        AnnotateMoreView annotateMoreView = this.mAnnotateMoreView;
        if (annotateMoreView != null) {
            annotateMoreView.onWBPageNumChanged(i2);
        }
        AnnoMultiPagesFragment annoMultiPagesFragment = this.mAnnoMultiPagesFragment;
        if (annoMultiPagesFragment != null) {
            annoMultiPagesFragment.onWBPageNumChanged(i2);
        }
    }

    public void setCachedImage(Bitmap bitmap) {
        this.mDrawingView.setCachedImage(bitmap);
    }

    public void drawShareContent(@Nullable Canvas canvas) {
        if (canvas != null) {
            this.mDrawingView.drawShareContent(canvas);
        }
    }

    public int getShareContentWidth() {
        return this.mDrawingView.getWidth();
    }

    public int getShareContentHeight() {
        return this.mDrawingView.getHeight();
    }

    public void onColorPicked(int i) {
        ColorAndLineWidthView colorAndLineWidthView = this.mColorAndLineWidthView;
        if (colorAndLineWidthView != null) {
            colorAndLineWidthView.dismiss();
        }
        mAnnoDataMgr.setColor(i);
        if (mAnnoDataMgr.isSharingWhiteboard()) {
            TextView textView = null;
            setColorVisibility(8);
            if (i == mAnnoDataMgr.getColorByIndex(0)) {
                textView = this.mBlackColor;
            } else if (i == mAnnoDataMgr.getColorByIndex(1)) {
                textView = this.mRedColor;
            } else if (i == mAnnoDataMgr.getColorByIndex(2)) {
                textView = this.mYellowColor;
            } else if (i == mAnnoDataMgr.getColorByIndex(3)) {
                textView = this.mGreenColor;
            } else if (i == mAnnoDataMgr.getColorByIndex(4)) {
                textView = this.mBlueColor;
            }
            if (textView != null) {
                textView.setVisibility(0);
                textView.setSelected(true);
            }
        }
        this.mColorImage.setColor(i);
        if (mAnnoDataMgr.isSharingWhiteboard()) {
            checkAndSetAnnoPen();
        }
    }

    public void onRepaint() {
        notifyRefresh();
    }

    public void setAnnoWindow(int i, int i2, float f, float f2, float f3) {
        this.mLeft = i;
        this.mTop = i2;
        this.mOffsetX = f;
        this.mOffsetY = f2;
        this.mZoomFactor = f3;
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            public void run() {
                AnnotateDrawingView.this.updateAnnotateWndSize();
                if (AnnoToolType.ANNO_TOOL_TYPE_NONE == AnnotateDrawingView.mAnnoDataMgr.getTool()) {
                    AnnotateDrawingView.this.checkAndSetAnnoPen();
                }
            }
        });
    }

    private void refreshAnnotateWndSize() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            public void run() {
                AnnotateDrawingView.this.updateAnnotateWndSize();
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateAnnotateWndSize() {
        if (this.mViewToolbars != null) {
            LayoutParams layoutParams = (LayoutParams) this.mDrawingView.getLayoutParams();
            layoutParams.height = getHeight();
            layoutParams.width = getWidth();
            int i = this.mLeft;
            int i2 = this.mTop;
            if (!mAnnoDataMgr.isPresenter()) {
                i2 = (this.mTop - this.mViewToolbars.getMeasuredHeight()) + mAnnoDataMgr.mVideoGalleryHeight;
                i += mAnnoDataMgr.mVideoGalleryWidth;
                if (UIUtil.isPortraitMode(getContext())) {
                    if (i2 < 0 && mAnnoDataMgr.mVideoGalleryWidth == 0 && mAnnoDataMgr.mVideoGalleryHeight == 0) {
                        i2 = 0;
                    }
                } else if (i2 < 0) {
                    i2 = 0;
                }
            }
            layoutParams.setMargins(this.mLeft, this.mTop, i, i2);
            this.mDrawingView.setLayoutParams(layoutParams);
            ZoomAnnotate zoomAnnotateMgr = ConfMgr.getInstance().getZoomAnnotateMgr();
            if (zoomAnnotateMgr != null) {
                zoomAnnotateMgr.setScreenSize(this.mScreenWidth, this.mScreenHeight - this.mViewToolbars.getMeasuredHeight());
            }
        }
    }

    public void onLongPressed(boolean z) {
        if (mAnnoDataMgr.isSharingWhiteboard()) {
            return;
        }
        if (z) {
            this.mSaveTableView.showAsDropDown(this.mViewToolbars);
        } else {
            this.mSaveTableView.dismiss();
        }
    }

    public void onBitmapChanged(Canvas canvas) {
        notifyRefresh();
    }

    public void onBeginEditing(int i, int i2) {
        setTextViewVisible(true);
        initEditText();
        checkEditTextPosition(i, i2);
    }

    public void onEndEditing() {
        setTextViewVisible(false);
        String obj = this.mEditText.getText().toString();
        if (this.mTextData != null) {
            if (obj.isEmpty()) {
                this.mDrawingView.editTextDidEndEditing(new short[1], this.mTextData);
                return;
            }
            char[] charArray = obj.toCharArray();
            short[] sArr = new short[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                sArr[i] = (short) charArray[i];
            }
            int currentTextColor = this.mEditText.getCurrentTextColor();
            int argb = Color.argb(255, Color.blue(currentTextColor), Color.green(currentTextColor), Color.red(currentTextColor));
            this.mTextData.setPadding(this.mEditText.getPaddingLeft());
            if (mAnnoDataMgr.isPresenter()) {
                AnnotateTextData annotateTextData = this.mTextData;
                annotateTextData.setPosX(this.mEditTextCurPosX + (annotateTextData.getPadding() / 2));
                AnnotateTextData annotateTextData2 = this.mTextData;
                annotateTextData2.setPosY(this.mEditTextCurPosY - (annotateTextData2.getPadding() / 2));
            } else {
                AnnotateTextData annotateTextData3 = this.mTextData;
                annotateTextData3.setPosX(this.mEditTextCurPosX + (annotateTextData3.getPadding() / 2));
                AnnotateTextData annotateTextData4 = this.mTextData;
                annotateTextData4.setPosY(this.mEditTextCurPosY + (annotateTextData4.getPadding() / 2));
            }
            this.mTextData.setWidth((int) (((float) this.mEditText.getWidth()) / this.mZoomFactor));
            this.mTextData.setHeight((int) (((float) this.mEditText.getHeight()) / this.mZoomFactor));
            this.mTextData.setTextWidth((int) this.mEditText.getPaint().measureText(obj));
            this.mTextData.setTextHeight(this.mEditText.getLineHeight() * this.mEditText.getLineCount());
            this.mTextData.setTextAlignment(getTextAlignmentAndroid2Anno(this.mEditText.getGravity()).ordinal());
            AnnotateTextData annotateTextData5 = this.mTextData;
            int progress = this.mTextSizeSeekBar.getProgress();
            AnnoDataMgr annoDataMgr = mAnnoDataMgr;
            annotateTextData5.setTextSize(progress + 48);
            this.mTextData.setTextLength(obj.length());
            this.mTextData.setBold(this.mEditText.getTypeface().isBold());
            this.mTextData.setItalic(this.mEditText.getTypeface().isItalic());
            this.mTextData.setLineCount(this.mEditText.getLineCount());
            this.mTextData.setFontColor(argb);
            setTextViewVisible(false);
            this.mDrawingView.editTextDidEndEditing(sArr, this.mTextData);
        }
    }

    public void onDismissAllTip() {
        if (((ZMActivity) getContext()) != null) {
            TextView textView = this.mLineWidth;
            if (textView != null) {
                textView.setSelected(false);
            }
            this.mSaveTableView.dismiss();
            AnnotateColorView annotateColorView = this.mAnnotateColorView;
            if (annotateColorView != null) {
                annotateColorView.dismiss();
            }
            AnnotateClearView annotateClearView = this.mAnnotateClearView;
            if (annotateClearView != null) {
                annotateClearView.dismiss();
            }
            AnnotateMoreView annotateMoreView = this.mAnnotateMoreView;
            if (annotateMoreView != null) {
                annotateMoreView.dismiss();
            }
            AnnotateLineView annotateLineView = this.mAnnotateLineView;
            if (annotateLineView != null) {
                annotateLineView.dismiss();
            }
            ColorAndLineWidthView colorAndLineWidthView = this.mColorAndLineWidthView;
            if (colorAndLineWidthView != null) {
                colorAndLineWidthView.dismiss();
            }
        }
    }

    public void onShapeRecognitionChecked(boolean z) {
        if (this.mPen.isSelected() && ConfMgr.getInstance().getZoomAnnotateMgr() != null) {
            mAnnoDataMgr.setTool(z ? AnnoToolType.ANNO_TOOL_TYPE_MULTI_SHAPE_DETECTOR : AnnoToolType.ANNO_TOOL_TYPE_PEN);
        }
    }

    public void onNewPageClicked() {
        if (!hasWriteStoragePermission()) {
            requestWriteStoragePermission(ZMConfRequestConstant.REQUEST_ANNOTATE_WRITE_STORAGE_BY_NEW);
            return;
        }
        onDismissAllTip();
        AnnoPageInfo annoPageNum = mAnnoDataMgr.getAnnoPageNum();
        if (annoPageNum == null || annoPageNum.mTotalPageNum < 12) {
            mAnnoDataMgr.newPage();
        } else {
            onShowAnnoTip(AnnoTipType.ANNO_CREATE_PAGE_TIP, 0);
        }
    }

    public void onPageManagementClicked() {
        if (!hasWriteStoragePermission()) {
            requestWriteStoragePermission(1024);
            return;
        }
        onDismissAllTip();
        mAnnoDataMgr.saveCurPageSnapahot();
        onShowAnnoTip(AnnoTipType.ANNO_EDIT_TIP, 0);
    }

    public void onSaveWbClicked() {
        if (!hasWriteStoragePermission()) {
            requestWriteStoragePermission(1025);
            return;
        }
        onDismissAllTip();
        AnnoPageInfo annoPageNum = mAnnoDataMgr.getAnnoPageNum();
        if (annoPageNum == null || annoPageNum.mTotalPageNum <= 1) {
            mAnnoDataMgr.setIsCopyToAlbum(true);
            mAnnoDataMgr.saveCurPageSnapahot();
        } else {
            mAnnoDataMgr.saveCurPageSnapahot();
            mAnnoDataMgr.resetSaveStatus();
            onShowAnnoTip(AnnoTipType.ANNO_CHECK_TIP, 0);
        }
    }

    public void onAnnoWidthChanged(int i) {
        if (i > 0) {
            mAnnoDataMgr.setLineWidth(i);
            updateLineWidthDrawable();
        }
        updateWBAnnoToolSelectedStatus();
    }

    private void updateLineWidthDrawable() {
        int lineWidth = mAnnoDataMgr.getLineWidth(AnnoToolType.ANNO_TOOL_TYPE_PEN);
        Drawable drawable = lineWidth != 2 ? lineWidth != 4 ? lineWidth != 8 ? lineWidth != 12 ? null : getContext().getResources().getDrawable(C4558R.C4559drawable.zm_anno_line_12_wb) : getContext().getResources().getDrawable(C4558R.C4559drawable.zm_anno_line_8_wb) : getContext().getResources().getDrawable(C4558R.C4559drawable.zm_anno_line_4_wb) : getContext().getResources().getDrawable(C4558R.C4559drawable.zm_anno_line_2_wb);
        if (drawable != null) {
            this.mLineWidth.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            this.mLineWidth.setSelected(false);
        }
    }

    private void updateWBAnnoToolSelectedStatus() {
        TextView textView = this.mPen;
        switch (mAnnoDataMgr.getTool()) {
            case ANNO_TOOL_TYPE_TEXTBOX:
                textView = this.mText;
                break;
            case ANNO_TOOL_TYPE_ERASER:
                textView = this.mErase;
                break;
            case ANNO_TOOL_TYPE_SPOTLIGHT:
                textView = this.mSpotlight;
                break;
        }
        updateSelection(textView);
    }

    public void onClearClicked(@NonNull AnnoClearType annoClearType) {
        this.mDrawingView.onClearClicked();
        mAnnoDataMgr.eraser(annoClearType.ordinal());
        onDismissAllTip();
    }

    public void onShowAnnoTip(AnnoTipType annoTipType, int i) {
        onDismissAllTip();
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
            boolean z = true;
            switch (annoTipType) {
                case ANNO_MORE_TIP:
                    AnnotateMoreView annotateMoreView = this.mAnnotateMoreView;
                    if (annotateMoreView != null) {
                        annotateMoreView.show(findViewById(i));
                        break;
                    }
                    break;
                case ANNO_LINE_TIP:
                    AnnotateLineView annotateLineView = this.mAnnotateLineView;
                    if (annotateLineView != null) {
                        annotateLineView.show(findViewById(i));
                        break;
                    }
                    break;
                case ANNO_COLOR_TIP:
                    AnnotateColorView annotateColorView = this.mAnnotateColorView;
                    if (annotateColorView != null) {
                        annotateColorView.setCurColor(mAnnoDataMgr.getCurToolColor());
                        this.mAnnotateColorView.show(findViewById(i));
                        break;
                    }
                    break;
                case ANNO_CLEAR_TIP:
                    AnnotateClearView annotateClearView = this.mAnnotateClearView;
                    if (annotateClearView != null) {
                        annotateClearView.show(findViewById(i));
                        break;
                    }
                    break;
                case ANNO_SAVE_TIP:
                case ANNO_CREATE_PAGE_TIP:
                    AnnotateDialog instance = AnnotateDialog.getInstance(supportFragmentManager);
                    if (annoTipType != AnnoTipType.ANNO_CREATE_PAGE_TIP) {
                        z = false;
                    }
                    instance.setIsShowErrowDialog(z);
                    instance.showNow(supportFragmentManager, AnnotateDialog.class.getName());
                    break;
                case ANNO_EDIT_TIP:
                case ANNO_CHECK_TIP:
                    if (this.mAnnoMultiPagesFragment == null) {
                        this.mAnnoMultiPagesFragment = new AnnoMultiPagesFragment();
                    }
                    AnnoMultiPagesFragment annoMultiPagesFragment = this.mAnnoMultiPagesFragment;
                    FragmentManager supportFragmentManager2 = ((ZMActivity) getContext()).getSupportFragmentManager();
                    String name = AnnoMultiPagesFragment.class.getName();
                    if (annoTipType != AnnoTipType.ANNO_CHECK_TIP) {
                        z = false;
                    }
                    annoMultiPagesFragment.showNow(supportFragmentManager2, name, z);
                    break;
            }
        }
    }
}
