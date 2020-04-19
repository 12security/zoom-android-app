package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor.Strength;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type;
import androidx.exifinterface.media.ExifInterface;
import java.util.ArrayList;

public class ConstraintWidget {
    protected static final int ANCHOR_BASELINE = 4;
    protected static final int ANCHOR_BOTTOM = 3;
    protected static final int ANCHOR_LEFT = 0;
    protected static final int ANCHOR_RIGHT = 1;
    protected static final int ANCHOR_TOP = 2;
    private static final boolean AUTOTAG_CENTER = false;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.5f;
    static final int DIMENSION_HORIZONTAL = 0;
    static final int DIMENSION_VERTICAL = 1;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    public static final int MATCH_CONSTRAINT_RATIO = 3;
    public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    private static final int WRAP = -2;
    protected ArrayList<ConstraintAnchor> mAnchors;
    ConstraintAnchor mBaseline;
    int mBaselineDistance;
    ConstraintWidgetGroup mBelongingGroup;
    ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private float mCircleConstraintAngle;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    protected float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    private int mDrawHeight;
    private int mDrawWidth;
    private int mDrawX;
    private int mDrawY;
    boolean mGroupsToSolver;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution;
    boolean mHorizontalWrapVisited;
    boolean mIsHeightWrapContent;
    boolean mIsWidthWrapContent;
    ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    protected ConstraintAnchor[] mListAnchors;
    protected DimensionBehaviour[] mListDimensionBehaviors;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    int mMatchConstraintDefaultHeight;
    int mMatchConstraintDefaultWidth;
    int mMatchConstraintMaxHeight;
    int mMatchConstraintMaxWidth;
    int mMatchConstraintMinHeight;
    int mMatchConstraintMinWidth;
    float mMatchConstraintPercentHeight;
    float mMatchConstraintPercentWidth;
    private int[] mMaxDimension;
    protected int mMinHeight;
    protected int mMinWidth;
    protected ConstraintWidget[] mNextChainWidget;
    protected int mOffsetX;
    protected int mOffsetY;
    boolean mOptimizerMeasurable;
    boolean mOptimizerMeasured;
    ConstraintWidget mParent;
    int mRelX;
    int mRelY;
    ResolutionDimension mResolutionHeight;
    ResolutionDimension mResolutionWidth;
    float mResolvedDimensionRatio;
    int mResolvedDimensionRatioSide;
    int[] mResolvedMatchConstraintDefault;
    ConstraintAnchor mRight;
    boolean mRightHasCentered;
    ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    float[] mWeight;
    int mWidth;
    private int mWrapHeight;
    private int mWrapWidth;

    /* renamed from: mX */
    protected int f14mX;

    /* renamed from: mY */
    protected int f15mY;

    public enum ContentAlignment {
        BEGIN,
        MIDDLE,
        END,
        TOP,
        VERTICAL_MIDDLE,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public void connectedTo(ConstraintWidget constraintWidget) {
    }

    public void resolve() {
    }

    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }

    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }

    public void setMaxWidth(int i) {
        this.mMaxDimension[0] = i;
    }

    public void setMaxHeight(int i) {
        this.mMaxDimension[1] = i;
    }

    public boolean isSpreadWidth() {
        return this.mMatchConstraintDefaultWidth == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMaxWidth == 0 && this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public boolean isSpreadHeight() {
        return this.mMatchConstraintDefaultHeight == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinHeight == 0 && this.mMatchConstraintMaxHeight == 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f14mX = 0;
        this.f15mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mWrapWidth = 0;
        this.mWrapHeight = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        float[] fArr = this.mWeight;
        fArr[0] = -1.0f;
        fArr[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        int[] iArr = this.mMaxDimension;
        iArr[0] = Integer.MAX_VALUE;
        iArr[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        ResolutionDimension resolutionDimension = this.mResolutionWidth;
        if (resolutionDimension != null) {
            resolutionDimension.reset();
        }
        ResolutionDimension resolutionDimension2 = this.mResolutionHeight;
        if (resolutionDimension2 != null) {
            resolutionDimension2.reset();
        }
        this.mBelongingGroup = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
    }

    public void resetResolutionNodes() {
        for (int i = 0; i < 6; i++) {
            this.mListAnchors[i].getResolutionNode().reset();
        }
    }

    public void updateResolutionNodes() {
        for (int i = 0; i < 6; i++) {
            this.mListAnchors[i].getResolutionNode().update();
        }
    }

    public void analyze(int i) {
        Optimizer.analyze(i, this);
    }

    public boolean isFullyResolved() {
        if (this.mLeft.getResolutionNode().state == 1 && this.mRight.getResolutionNode().state == 1 && this.mTop.getResolutionNode().state == 1 && this.mBottom.getResolutionNode().state == 1) {
            return true;
        }
        return false;
    }

    public ResolutionDimension getResolutionWidth() {
        if (this.mResolutionWidth == null) {
            this.mResolutionWidth = new ResolutionDimension();
        }
        return this.mResolutionWidth;
    }

    public ResolutionDimension getResolutionHeight() {
        if (this.mResolutionHeight == null) {
            this.mResolutionHeight = new ResolutionDimension();
        }
        return this.mResolutionHeight;
    }

    public ConstraintWidget() {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mBelongingGroup = null;
        this.mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        this.mCircleConstraintAngle = 0.0f;
        this.mLeft = new ConstraintAnchor(this, Type.LEFT);
        this.mTop = new ConstraintAnchor(this, Type.TOP);
        this.mRight = new ConstraintAnchor(this, Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, Type.CENTER);
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter};
        this.mAnchors = new ArrayList<>();
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f14mX = 0;
        this.f15mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        addAnchors();
    }

    public ConstraintWidget(int i, int i2, int i3, int i4) {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mBelongingGroup = null;
        this.mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        this.mCircleConstraintAngle = 0.0f;
        this.mLeft = new ConstraintAnchor(this, Type.LEFT);
        this.mTop = new ConstraintAnchor(this, Type.TOP);
        this.mRight = new ConstraintAnchor(this, Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, Type.CENTER);
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter};
        this.mAnchors = new ArrayList<>();
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f14mX = 0;
        this.f15mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.f14mX = i;
        this.f15mY = i2;
        this.mWidth = i3;
        this.mHeight = i4;
        addAnchors();
        forceUpdateDrawPosition();
    }

    public ConstraintWidget(int i, int i2) {
        this(0, 0, i, i2);
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    public boolean isRoot() {
        return this.mParent == null;
    }

    public boolean isRootContainer() {
        if (this instanceof ConstraintWidgetContainer) {
            ConstraintWidget constraintWidget = this.mParent;
            if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInsideConstraintLayout() {
        ConstraintWidget parent = getParent();
        if (parent == null) {
            return false;
        }
        while (parent != null) {
            if (parent instanceof ConstraintWidgetContainer) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    public boolean hasAncestor(ConstraintWidget constraintWidget) {
        ConstraintWidget parent = getParent();
        if (parent == constraintWidget) {
            return true;
        }
        if (parent == constraintWidget.getParent()) {
            return false;
        }
        while (parent != null) {
            if (parent == constraintWidget || parent == constraintWidget.getParent()) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    public WidgetContainer getRootWidgetContainer() {
        ConstraintWidget constraintWidget = this;
        while (constraintWidget.getParent() != null) {
            constraintWidget = constraintWidget.getParent();
        }
        if (constraintWidget instanceof WidgetContainer) {
            return (WidgetContainer) constraintWidget;
        }
        return null;
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public void setParent(ConstraintWidget constraintWidget) {
        this.mParent = constraintWidget;
    }

    public void setWidthWrapContent(boolean z) {
        this.mIsWidthWrapContent = z;
    }

    public boolean isWidthWrapContent() {
        return this.mIsWidthWrapContent;
    }

    public void setHeightWrapContent(boolean z) {
        this.mIsHeightWrapContent = z;
    }

    public boolean isHeightWrapContent() {
        return this.mIsHeightWrapContent;
    }

    public void connectCircularConstraint(ConstraintWidget constraintWidget, float f, int i) {
        immediateConnect(Type.CENTER, constraintWidget, Type.CENTER, i, 0);
        this.mCircleConstraintAngle = f;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String str) {
        this.mType = str;
    }

    public void setVisibility(int i) {
        this.mVisibility = i;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public void setDebugName(String str) {
        this.mDebugName = str;
    }

    public void setDebugSolverName(LinearSystem linearSystem, String str) {
        this.mDebugName = str;
        SolverVariable createObjectVariable = linearSystem.createObjectVariable(this.mLeft);
        SolverVariable createObjectVariable2 = linearSystem.createObjectVariable(this.mTop);
        SolverVariable createObjectVariable3 = linearSystem.createObjectVariable(this.mRight);
        SolverVariable createObjectVariable4 = linearSystem.createObjectVariable(this.mBottom);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(".left");
        createObjectVariable.setName(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(".top");
        createObjectVariable2.setName(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(".right");
        createObjectVariable3.setName(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str);
        sb4.append(".bottom");
        createObjectVariable4.setName(sb4.toString());
        if (this.mBaselineDistance > 0) {
            SolverVariable createObjectVariable5 = linearSystem.createObjectVariable(this.mBaseline);
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str);
            sb5.append(".baseline");
            createObjectVariable5.setName(sb5.toString());
        }
    }

    public void createObjectVariables(LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }

    public String toString() {
        String str;
        String str2;
        StringBuilder sb = new StringBuilder();
        if (this.mType != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("type: ");
            sb2.append(this.mType);
            sb2.append(OAuth.SCOPE_DELIMITER);
            str = sb2.toString();
        } else {
            str = "";
        }
        sb.append(str);
        if (this.mDebugName != null) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("id: ");
            sb3.append(this.mDebugName);
            sb3.append(OAuth.SCOPE_DELIMITER);
            str2 = sb3.toString();
        } else {
            str2 = "";
        }
        sb.append(str2);
        sb.append("(");
        sb.append(this.f14mX);
        sb.append(", ");
        sb.append(this.f15mY);
        sb.append(") - (");
        sb.append(this.mWidth);
        sb.append(" x ");
        sb.append(this.mHeight);
        sb.append(") wrap: (");
        sb.append(this.mWrapWidth);
        sb.append(" x ");
        sb.append(this.mWrapHeight);
        sb.append(")");
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public int getInternalDrawX() {
        return this.mDrawX;
    }

    /* access modifiers changed from: 0000 */
    public int getInternalDrawY() {
        return this.mDrawY;
    }

    public int getInternalDrawRight() {
        return this.mDrawX + this.mDrawWidth;
    }

    public int getInternalDrawBottom() {
        return this.mDrawY + this.mDrawHeight;
    }

    public int getX() {
        return this.f14mX;
    }

    public int getY() {
        return this.f15mY;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getOptimizerWrapWidth() {
        int i;
        int i2 = this.mWidth;
        if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return i2;
        }
        if (this.mMatchConstraintDefaultWidth == 1) {
            i = Math.max(this.mMatchConstraintMinWidth, i2);
        } else {
            i = this.mMatchConstraintMinWidth;
            if (i > 0) {
                this.mWidth = i;
            } else {
                i = 0;
            }
        }
        int i3 = this.mMatchConstraintMaxWidth;
        return (i3 <= 0 || i3 >= i) ? i : i3;
    }

    public int getOptimizerWrapHeight() {
        int i;
        int i2 = this.mHeight;
        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return i2;
        }
        if (this.mMatchConstraintDefaultHeight == 1) {
            i = Math.max(this.mMatchConstraintMinHeight, i2);
        } else {
            i = this.mMatchConstraintMinHeight;
            if (i > 0) {
                this.mHeight = i;
            } else {
                i = 0;
            }
        }
        int i3 = this.mMatchConstraintMaxHeight;
        return (i3 <= 0 || i3 >= i) ? i : i3;
    }

    public int getWrapWidth() {
        return this.mWrapWidth;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public int getWrapHeight() {
        return this.mWrapHeight;
    }

    public int getLength(int i) {
        if (i == 0) {
            return getWidth();
        }
        if (i == 1) {
            return getHeight();
        }
        return 0;
    }

    public int getDrawX() {
        return this.mDrawX + this.mOffsetX;
    }

    public int getDrawY() {
        return this.mDrawY + this.mOffsetY;
    }

    public int getDrawWidth() {
        return this.mDrawWidth;
    }

    public int getDrawHeight() {
        return this.mDrawHeight;
    }

    public int getDrawBottom() {
        return getDrawY() + this.mDrawHeight;
    }

    public int getDrawRight() {
        return getDrawX() + this.mDrawWidth;
    }

    /* access modifiers changed from: protected */
    public int getRootX() {
        return this.f14mX + this.mOffsetX;
    }

    /* access modifiers changed from: protected */
    public int getRootY() {
        return this.f15mY + this.mOffsetY;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getLeft() {
        return getX();
    }

    public int getTop() {
        return getY();
    }

    public int getRight() {
        return getX() + this.mWidth;
    }

    public int getBottom() {
        return getY() + this.mHeight;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public float getBiasPercent(int i) {
        if (i == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (i == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }

    public boolean hasBaseline() {
        return this.mBaselineDistance > 0;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public void setX(int i) {
        this.f14mX = i;
    }

    public void setY(int i) {
        this.f15mY = i;
    }

    public void setOrigin(int i, int i2) {
        this.f14mX = i;
        this.f15mY = i2;
    }

    public void setOffset(int i, int i2) {
        this.mOffsetX = i;
        this.mOffsetY = i2;
    }

    public void setGoneMargin(Type type, int i) {
        switch (type) {
            case LEFT:
                this.mLeft.mGoneMargin = i;
                return;
            case TOP:
                this.mTop.mGoneMargin = i;
                return;
            case RIGHT:
                this.mRight.mGoneMargin = i;
                return;
            case BOTTOM:
                this.mBottom.mGoneMargin = i;
                return;
            default:
                return;
        }
    }

    public void updateDrawPosition() {
        int i = this.f14mX;
        int i2 = this.f15mY;
        int i3 = this.mWidth + i;
        int i4 = this.mHeight + i2;
        this.mDrawX = i;
        this.mDrawY = i2;
        this.mDrawWidth = i3 - i;
        this.mDrawHeight = i4 - i2;
    }

    public void forceUpdateDrawPosition() {
        int i = this.f14mX;
        int i2 = this.f15mY;
        int i3 = this.mWidth + i;
        int i4 = this.mHeight + i2;
        this.mDrawX = i;
        this.mDrawY = i2;
        this.mDrawWidth = i3 - i;
        this.mDrawHeight = i4 - i2;
    }

    public void setDrawOrigin(int i, int i2) {
        this.mDrawX = i - this.mOffsetX;
        this.mDrawY = i2 - this.mOffsetY;
        this.f14mX = this.mDrawX;
        this.f15mY = this.mDrawY;
    }

    public void setDrawX(int i) {
        this.mDrawX = i - this.mOffsetX;
        this.f14mX = this.mDrawX;
    }

    public void setDrawY(int i) {
        this.mDrawY = i - this.mOffsetY;
        this.f15mY = this.mDrawY;
    }

    public void setDrawWidth(int i) {
        this.mDrawWidth = i;
    }

    public void setDrawHeight(int i) {
        this.mDrawHeight = i;
    }

    public void setWidth(int i) {
        this.mWidth = i;
        int i2 = this.mWidth;
        int i3 = this.mMinWidth;
        if (i2 < i3) {
            this.mWidth = i3;
        }
    }

    public void setHeight(int i) {
        this.mHeight = i;
        int i2 = this.mHeight;
        int i3 = this.mMinHeight;
        if (i2 < i3) {
            this.mHeight = i3;
        }
    }

    public void setLength(int i, int i2) {
        if (i2 == 0) {
            setWidth(i);
        } else if (i2 == 1) {
            setHeight(i);
        }
    }

    public void setHorizontalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultWidth = i;
        this.mMatchConstraintMinWidth = i2;
        this.mMatchConstraintMaxWidth = i3;
        this.mMatchConstraintPercentWidth = f;
        if (f < 1.0f && this.mMatchConstraintDefaultWidth == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }

    public void setVerticalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultHeight = i;
        this.mMatchConstraintMinHeight = i2;
        this.mMatchConstraintMaxHeight = i3;
        this.mMatchConstraintPercentHeight = f;
        if (f < 1.0f && this.mMatchConstraintDefaultHeight == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }

    public void setDimensionRatio(String str) {
        float f;
        if (str == null || str.length() == 0) {
            this.mDimensionRatio = 0.0f;
            return;
        }
        int i = -1;
        int length = str.length();
        int indexOf = str.indexOf(44);
        int i2 = 0;
        if (indexOf > 0 && indexOf < length - 1) {
            String substring = str.substring(0, indexOf);
            if (substring.equalsIgnoreCase(ExifInterface.LONGITUDE_WEST)) {
                i = 0;
            } else if (substring.equalsIgnoreCase("H")) {
                i = 1;
            }
            i2 = indexOf + 1;
        }
        int indexOf2 = str.indexOf(58);
        if (indexOf2 < 0 || indexOf2 >= length - 1) {
            String substring2 = str.substring(i2);
            if (substring2.length() > 0) {
                try {
                    f = Float.parseFloat(substring2);
                } catch (NumberFormatException unused) {
                }
            }
            f = 0.0f;
        } else {
            String substring3 = str.substring(i2, indexOf2);
            String substring4 = str.substring(indexOf2 + 1);
            if (substring3.length() > 0 && substring4.length() > 0) {
                try {
                    float parseFloat = Float.parseFloat(substring3);
                    float parseFloat2 = Float.parseFloat(substring4);
                    f = (parseFloat <= 0.0f || parseFloat2 <= 0.0f) ? 0.0f : i == 1 ? Math.abs(parseFloat2 / parseFloat) : Math.abs(parseFloat / parseFloat2);
                } catch (NumberFormatException unused2) {
                }
            }
            f = 0.0f;
        }
        if (f > 0.0f) {
            this.mDimensionRatio = f;
            this.mDimensionRatioSide = i;
        }
    }

    public void setDimensionRatio(float f, int i) {
        this.mDimensionRatio = f;
        this.mDimensionRatioSide = i;
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public void setHorizontalBiasPercent(float f) {
        this.mHorizontalBiasPercent = f;
    }

    public void setVerticalBiasPercent(float f) {
        this.mVerticalBiasPercent = f;
    }

    public void setMinWidth(int i) {
        if (i < 0) {
            this.mMinWidth = 0;
        } else {
            this.mMinWidth = i;
        }
    }

    public void setMinHeight(int i) {
        if (i < 0) {
            this.mMinHeight = 0;
        } else {
            this.mMinHeight = i;
        }
    }

    public void setWrapWidth(int i) {
        this.mWrapWidth = i;
    }

    public void setWrapHeight(int i) {
        this.mWrapHeight = i;
    }

    public void setDimension(int i, int i2) {
        this.mWidth = i;
        int i3 = this.mWidth;
        int i4 = this.mMinWidth;
        if (i3 < i4) {
            this.mWidth = i4;
        }
        this.mHeight = i2;
        int i5 = this.mHeight;
        int i6 = this.mMinHeight;
        if (i5 < i6) {
            this.mHeight = i6;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002b, code lost:
        if (r5 < r3) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001c, code lost:
        if (r4 < r2) goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setFrame(int r2, int r3, int r4, int r5) {
        /*
            r1 = this;
            int r4 = r4 - r2
            int r5 = r5 - r3
            r1.f14mX = r2
            r1.f15mY = r3
            int r2 = r1.mVisibility
            r3 = 0
            r0 = 8
            if (r2 != r0) goto L_0x0012
            r1.mWidth = r3
            r1.mHeight = r3
            return
        L_0x0012:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r1.mListDimensionBehaviors
            r2 = r2[r3]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            if (r2 != r3) goto L_0x001f
            int r2 = r1.mWidth
            if (r4 >= r2) goto L_0x001f
            goto L_0x0020
        L_0x001f:
            r2 = r4
        L_0x0020:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r3 = r1.mListDimensionBehaviors
            r4 = 1
            r3 = r3[r4]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            if (r3 != r0) goto L_0x002e
            int r3 = r1.mHeight
            if (r5 >= r3) goto L_0x002e
            goto L_0x002f
        L_0x002e:
            r3 = r5
        L_0x002f:
            r1.mWidth = r2
            r1.mHeight = r3
            int r2 = r1.mHeight
            int r3 = r1.mMinHeight
            if (r2 >= r3) goto L_0x003b
            r1.mHeight = r3
        L_0x003b:
            int r2 = r1.mWidth
            int r3 = r1.mMinWidth
            if (r2 >= r3) goto L_0x0043
            r1.mWidth = r3
        L_0x0043:
            r1.mOptimizerMeasured = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.setFrame(int, int, int, int):void");
    }

    public void setFrame(int i, int i2, int i3) {
        if (i3 == 0) {
            setHorizontalDimension(i, i2);
        } else if (i3 == 1) {
            setVerticalDimension(i, i2);
        }
        this.mOptimizerMeasured = true;
    }

    public void setHorizontalDimension(int i, int i2) {
        this.f14mX = i;
        this.mWidth = i2 - i;
        int i3 = this.mWidth;
        int i4 = this.mMinWidth;
        if (i3 < i4) {
            this.mWidth = i4;
        }
    }

    public void setVerticalDimension(int i, int i2) {
        this.f15mY = i;
        this.mHeight = i2 - i;
        int i3 = this.mHeight;
        int i4 = this.mMinHeight;
        if (i3 < i4) {
            this.mHeight = i4;
        }
    }

    /* access modifiers changed from: 0000 */
    public int getRelativePositioning(int i) {
        if (i == 0) {
            return this.mRelX;
        }
        if (i == 1) {
            return this.mRelY;
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public void setRelativePositioning(int i, int i2) {
        if (i2 == 0) {
            this.mRelX = i;
        } else if (i2 == 1) {
            this.mRelY = i;
        }
    }

    public void setBaselineDistance(int i) {
        this.mBaselineDistance = i;
    }

    public void setCompanionWidget(Object obj) {
        this.mCompanionWidget = obj;
    }

    public void setContainerItemSkip(int i) {
        if (i >= 0) {
            this.mContainerItemSkip = i;
        } else {
            this.mContainerItemSkip = 0;
        }
    }

    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }

    public void setHorizontalWeight(float f) {
        this.mWeight[0] = f;
    }

    public void setVerticalWeight(float f) {
        this.mWeight[1] = f;
    }

    public void setHorizontalChainStyle(int i) {
        this.mHorizontalChainStyle = i;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public void setVerticalChainStyle(int i) {
        this.mVerticalChainStyle = i;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }

    public void immediateConnect(Type type, ConstraintWidget constraintWidget, Type type2, int i, int i2) {
        getAnchor(type).connect(constraintWidget.getAnchor(type2), i, i2, Strength.STRONG, 0, true);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i, int i2) {
        connect(constraintAnchor, constraintAnchor2, i, Strength.STRONG, i2);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i) {
        connect(constraintAnchor, constraintAnchor2, i, Strength.STRONG, 0);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i, Strength strength, int i2) {
        if (constraintAnchor.getOwner() == this) {
            connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), i, strength, i2);
        }
    }

    public void connect(Type type, ConstraintWidget constraintWidget, Type type2, int i) {
        connect(type, constraintWidget, type2, i, Strength.STRONG);
    }

    public void connect(Type type, ConstraintWidget constraintWidget, Type type2) {
        connect(type, constraintWidget, type2, 0, Strength.STRONG);
    }

    public void connect(Type type, ConstraintWidget constraintWidget, Type type2, int i, Strength strength) {
        connect(type, constraintWidget, type2, i, strength, 0);
    }

    public void connect(Type type, ConstraintWidget constraintWidget, Type type2, int i, Strength strength, int i2) {
        Strength strength2;
        boolean z;
        Type type3 = type;
        ConstraintWidget constraintWidget2 = constraintWidget;
        Type type4 = type2;
        int i3 = i2;
        int i4 = 0;
        if (type3 == Type.CENTER) {
            if (type4 == Type.CENTER) {
                ConstraintAnchor anchor = getAnchor(Type.LEFT);
                ConstraintAnchor anchor2 = getAnchor(Type.RIGHT);
                ConstraintAnchor anchor3 = getAnchor(Type.TOP);
                ConstraintAnchor anchor4 = getAnchor(Type.BOTTOM);
                boolean z2 = true;
                if ((anchor == null || !anchor.isConnected()) && (anchor2 == null || !anchor2.isConnected())) {
                    ConstraintWidget constraintWidget3 = constraintWidget;
                    Strength strength3 = strength;
                    int i5 = i2;
                    connect(Type.LEFT, constraintWidget3, Type.LEFT, 0, strength3, i5);
                    connect(Type.RIGHT, constraintWidget3, Type.RIGHT, 0, strength3, i5);
                    z = true;
                } else {
                    z = false;
                }
                if ((anchor3 == null || !anchor3.isConnected()) && (anchor4 == null || !anchor4.isConnected())) {
                    ConstraintWidget constraintWidget4 = constraintWidget;
                    Strength strength4 = strength;
                    int i6 = i2;
                    connect(Type.TOP, constraintWidget4, Type.TOP, 0, strength4, i6);
                    connect(Type.BOTTOM, constraintWidget4, Type.BOTTOM, 0, strength4, i6);
                } else {
                    z2 = false;
                }
                if (z && z2) {
                    getAnchor(Type.CENTER).connect(constraintWidget2.getAnchor(Type.CENTER), 0, i3);
                } else if (z) {
                    getAnchor(Type.CENTER_X).connect(constraintWidget2.getAnchor(Type.CENTER_X), 0, i3);
                } else if (z2) {
                    getAnchor(Type.CENTER_Y).connect(constraintWidget2.getAnchor(Type.CENTER_Y), 0, i3);
                }
            } else if (type4 == Type.LEFT || type4 == Type.RIGHT) {
                connect(Type.LEFT, constraintWidget, type2, 0, strength, i2);
                try {
                    connect(Type.RIGHT, constraintWidget, type2, 0, strength, i2);
                    getAnchor(Type.CENTER).connect(constraintWidget.getAnchor(type2), 0, i3);
                } catch (Throwable th) {
                    throw th;
                }
            } else if (type4 == Type.TOP || type4 == Type.BOTTOM) {
                ConstraintWidget constraintWidget5 = constraintWidget;
                Type type5 = type2;
                Strength strength5 = strength;
                int i7 = i2;
                connect(Type.TOP, constraintWidget5, type5, 0, strength5, i7);
                connect(Type.BOTTOM, constraintWidget5, type5, 0, strength5, i7);
                getAnchor(Type.CENTER).connect(constraintWidget.getAnchor(type2), 0, i3);
            }
        } else if (type3 == Type.CENTER_X && (type4 == Type.LEFT || type4 == Type.RIGHT)) {
            ConstraintAnchor anchor5 = getAnchor(Type.LEFT);
            ConstraintAnchor anchor6 = constraintWidget.getAnchor(type2);
            ConstraintAnchor anchor7 = getAnchor(Type.RIGHT);
            anchor5.connect(anchor6, 0, i3);
            anchor7.connect(anchor6, 0, i3);
            getAnchor(Type.CENTER_X).connect(anchor6, 0, i3);
        } else if (type3 == Type.CENTER_Y && (type4 == Type.TOP || type4 == Type.BOTTOM)) {
            ConstraintAnchor anchor8 = constraintWidget.getAnchor(type2);
            getAnchor(Type.TOP).connect(anchor8, 0, i3);
            getAnchor(Type.BOTTOM).connect(anchor8, 0, i3);
            getAnchor(Type.CENTER_Y).connect(anchor8, 0, i3);
        } else if (type3 == Type.CENTER_X && type4 == Type.CENTER_X) {
            getAnchor(Type.LEFT).connect(constraintWidget2.getAnchor(Type.LEFT), 0, i3);
            getAnchor(Type.RIGHT).connect(constraintWidget2.getAnchor(Type.RIGHT), 0, i3);
            getAnchor(Type.CENTER_X).connect(constraintWidget.getAnchor(type2), 0, i3);
        } else if (type3 == Type.CENTER_Y && type4 == Type.CENTER_Y) {
            getAnchor(Type.TOP).connect(constraintWidget2.getAnchor(Type.TOP), 0, i3);
            getAnchor(Type.BOTTOM).connect(constraintWidget2.getAnchor(Type.BOTTOM), 0, i3);
            getAnchor(Type.CENTER_Y).connect(constraintWidget.getAnchor(type2), 0, i3);
        } else {
            ConstraintAnchor anchor9 = getAnchor(type);
            ConstraintAnchor anchor10 = constraintWidget.getAnchor(type2);
            if (anchor9.isValidConnection(anchor10)) {
                if (type3 == Type.BASELINE) {
                    ConstraintAnchor anchor11 = getAnchor(Type.TOP);
                    ConstraintAnchor anchor12 = getAnchor(Type.BOTTOM);
                    if (anchor11 != null) {
                        anchor11.reset();
                    }
                    if (anchor12 != null) {
                        anchor12.reset();
                    }
                    strength2 = strength;
                } else {
                    if (type3 == Type.TOP || type3 == Type.BOTTOM) {
                        ConstraintAnchor anchor13 = getAnchor(Type.BASELINE);
                        if (anchor13 != null) {
                            anchor13.reset();
                        }
                        ConstraintAnchor anchor14 = getAnchor(Type.CENTER);
                        if (anchor14.getTarget() != anchor10) {
                            anchor14.reset();
                        }
                        ConstraintAnchor opposite = getAnchor(type).getOpposite();
                        ConstraintAnchor anchor15 = getAnchor(Type.CENTER_Y);
                        if (anchor15.isConnected()) {
                            opposite.reset();
                            anchor15.reset();
                        }
                    } else if (type3 == Type.LEFT || type3 == Type.RIGHT) {
                        ConstraintAnchor anchor16 = getAnchor(Type.CENTER);
                        if (anchor16.getTarget() != anchor10) {
                            anchor16.reset();
                        }
                        ConstraintAnchor opposite2 = getAnchor(type).getOpposite();
                        ConstraintAnchor anchor17 = getAnchor(Type.CENTER_X);
                        if (anchor17.isConnected()) {
                            opposite2.reset();
                            anchor17.reset();
                        }
                    }
                    i4 = i;
                    strength2 = strength;
                }
                anchor9.connect(anchor10, i4, strength2, i3);
                anchor10.getOwner().connectedTo(anchor9.getOwner());
            }
        }
    }

    public void resetAllConstraints() {
        resetAnchors();
        setVerticalBiasPercent(DEFAULT_BIAS);
        setHorizontalBiasPercent(DEFAULT_BIAS);
        if (!(this instanceof ConstraintWidgetContainer)) {
            if (getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                if (getWidth() == getWrapWidth()) {
                    setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
                } else if (getWidth() > getMinWidth()) {
                    setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
            }
            if (getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                if (getHeight() == getWrapHeight()) {
                    setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
                } else if (getHeight() > getMinHeight()) {
                    setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
            }
        }
    }

    public void resetAnchor(ConstraintAnchor constraintAnchor) {
        if (getParent() == null || !(getParent() instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            ConstraintAnchor anchor = getAnchor(Type.LEFT);
            ConstraintAnchor anchor2 = getAnchor(Type.RIGHT);
            ConstraintAnchor anchor3 = getAnchor(Type.TOP);
            ConstraintAnchor anchor4 = getAnchor(Type.BOTTOM);
            ConstraintAnchor anchor5 = getAnchor(Type.CENTER);
            ConstraintAnchor anchor6 = getAnchor(Type.CENTER_X);
            ConstraintAnchor anchor7 = getAnchor(Type.CENTER_Y);
            if (constraintAnchor == anchor5) {
                if (anchor.isConnected() && anchor2.isConnected() && anchor.getTarget() == anchor2.getTarget()) {
                    anchor.reset();
                    anchor2.reset();
                }
                if (anchor3.isConnected() && anchor4.isConnected() && anchor3.getTarget() == anchor4.getTarget()) {
                    anchor3.reset();
                    anchor4.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
                this.mVerticalBiasPercent = 0.5f;
            } else if (constraintAnchor == anchor6) {
                if (anchor.isConnected() && anchor2.isConnected() && anchor.getTarget().getOwner() == anchor2.getTarget().getOwner()) {
                    anchor.reset();
                    anchor2.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
            } else if (constraintAnchor == anchor7) {
                if (anchor3.isConnected() && anchor4.isConnected() && anchor3.getTarget().getOwner() == anchor4.getTarget().getOwner()) {
                    anchor3.reset();
                    anchor4.reset();
                }
                this.mVerticalBiasPercent = 0.5f;
            } else if (constraintAnchor == anchor || constraintAnchor == anchor2) {
                if (anchor.isConnected() && anchor.getTarget() == anchor2.getTarget()) {
                    anchor5.reset();
                }
            } else if ((constraintAnchor == anchor3 || constraintAnchor == anchor4) && anchor3.isConnected() && anchor3.getTarget() == anchor4.getTarget()) {
                anchor5.reset();
            }
            constraintAnchor.reset();
        }
    }

    public void resetAnchors() {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int size = this.mAnchors.size();
            for (int i = 0; i < size; i++) {
                ((ConstraintAnchor) this.mAnchors.get(i)).reset();
            }
        }
    }

    public void resetAnchors(int i) {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int size = this.mAnchors.size();
            for (int i2 = 0; i2 < size; i2++) {
                ConstraintAnchor constraintAnchor = (ConstraintAnchor) this.mAnchors.get(i2);
                if (i == constraintAnchor.getConnectionCreator()) {
                    if (constraintAnchor.isVerticalAnchor()) {
                        setVerticalBiasPercent(DEFAULT_BIAS);
                    } else {
                        setHorizontalBiasPercent(DEFAULT_BIAS);
                    }
                    constraintAnchor.reset();
                }
            }
        }
    }

    public void disconnectWidget(ConstraintWidget constraintWidget) {
        ArrayList anchors = getAnchors();
        int size = anchors.size();
        for (int i = 0; i < size; i++) {
            ConstraintAnchor constraintAnchor = (ConstraintAnchor) anchors.get(i);
            if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == constraintWidget) {
                constraintAnchor.reset();
            }
        }
    }

    public void disconnectUnlockedWidget(ConstraintWidget constraintWidget) {
        ArrayList anchors = getAnchors();
        int size = anchors.size();
        for (int i = 0; i < size; i++) {
            ConstraintAnchor constraintAnchor = (ConstraintAnchor) anchors.get(i);
            if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == constraintWidget && constraintAnchor.getConnectionCreator() == 2) {
                constraintAnchor.reset();
            }
        }
    }

    public ConstraintAnchor getAnchor(Type type) {
        switch (type) {
            case LEFT:
                return this.mLeft;
            case TOP:
                return this.mTop;
            case RIGHT:
                return this.mRight;
            case BOTTOM:
                return this.mBottom;
            case BASELINE:
                return this.mBaseline;
            case CENTER:
                return this.mCenter;
            case CENTER_X:
                return this.mCenterX;
            case CENTER_Y:
                return this.mCenterY;
            case NONE:
                return null;
            default:
                throw new AssertionError(type.name());
        }
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public DimensionBehaviour getDimensionBehaviour(int i) {
        if (i == 0) {
            return getHorizontalDimensionBehaviour();
        }
        if (i == 1) {
            return getVerticalDimensionBehaviour();
        }
        return null;
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
        if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            setWidth(this.mWrapWidth);
        }
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
        if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            setHeight(this.mWrapHeight);
        }
    }

    public boolean isInHorizontalChain() {
        return (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight);
    }

    public ConstraintWidget getHorizontalChainControlWidget() {
        ConstraintAnchor constraintAnchor;
        ConstraintWidget constraintWidget;
        ConstraintAnchor constraintAnchor2;
        if (!isInHorizontalChain()) {
            return null;
        }
        ConstraintWidget constraintWidget2 = this;
        ConstraintWidget constraintWidget3 = null;
        while (constraintWidget3 == null && constraintWidget2 != null) {
            ConstraintAnchor anchor = constraintWidget2.getAnchor(Type.LEFT);
            if (anchor == null) {
                constraintAnchor = null;
            } else {
                constraintAnchor = anchor.getTarget();
            }
            if (constraintAnchor == null) {
                constraintWidget = null;
            } else {
                constraintWidget = constraintAnchor.getOwner();
            }
            if (constraintWidget == getParent()) {
                return constraintWidget2;
            }
            if (constraintWidget == null) {
                constraintAnchor2 = null;
            } else {
                constraintAnchor2 = constraintWidget.getAnchor(Type.RIGHT).getTarget();
            }
            if (constraintAnchor2 == null || constraintAnchor2.getOwner() == constraintWidget2) {
                constraintWidget2 = constraintWidget;
            } else {
                constraintWidget3 = constraintWidget2;
            }
        }
        return constraintWidget3;
    }

    public boolean isInVerticalChain() {
        return (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom);
    }

    public ConstraintWidget getVerticalChainControlWidget() {
        ConstraintAnchor constraintAnchor;
        ConstraintWidget constraintWidget;
        ConstraintAnchor constraintAnchor2;
        if (!isInVerticalChain()) {
            return null;
        }
        ConstraintWidget constraintWidget2 = this;
        ConstraintWidget constraintWidget3 = null;
        while (constraintWidget3 == null && constraintWidget2 != null) {
            ConstraintAnchor anchor = constraintWidget2.getAnchor(Type.TOP);
            if (anchor == null) {
                constraintAnchor = null;
            } else {
                constraintAnchor = anchor.getTarget();
            }
            if (constraintAnchor == null) {
                constraintWidget = null;
            } else {
                constraintWidget = constraintAnchor.getOwner();
            }
            if (constraintWidget == getParent()) {
                return constraintWidget2;
            }
            if (constraintWidget == null) {
                constraintAnchor2 = null;
            } else {
                constraintAnchor2 = constraintWidget.getAnchor(Type.BOTTOM).getTarget();
            }
            if (constraintAnchor2 == null || constraintAnchor2.getOwner() == constraintWidget2) {
                constraintWidget2 = constraintWidget;
            } else {
                constraintWidget3 = constraintWidget2;
            }
        }
        return constraintWidget3;
    }

    private boolean isChainHead(int i) {
        int i2 = i * 2;
        if (this.mListAnchors[i2].mTarget != null) {
            ConstraintAnchor constraintAnchor = this.mListAnchors[i2].mTarget.mTarget;
            ConstraintAnchor[] constraintAnchorArr = this.mListAnchors;
            if (constraintAnchor != constraintAnchorArr[i2]) {
                int i3 = i2 + 1;
                if (constraintAnchorArr[i3].mTarget != null && this.mListAnchors[i3].mTarget.mTarget == this.mListAnchors[i3]) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:98:0x01c5, code lost:
        if (r1 == -1) goto L_0x01c9;
     */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x01f1  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x0259  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x026a A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x026b  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x028d  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x02c8  */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x02d2  */
    /* JADX WARNING: Removed duplicated region for block: B:150:0x02db  */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x02e1  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02e9  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x0320  */
    /* JADX WARNING: Removed duplicated region for block: B:161:0x0349  */
    /* JADX WARNING: Removed duplicated region for block: B:164:0x0353  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addToSolver(androidx.constraintlayout.solver.LinearSystem r39) {
        /*
            r38 = this;
            r15 = r38
            r14 = r39
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mLeft
            androidx.constraintlayout.solver.SolverVariable r21 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mRight
            androidx.constraintlayout.solver.SolverVariable r10 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mTop
            androidx.constraintlayout.solver.SolverVariable r6 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBottom
            androidx.constraintlayout.solver.SolverVariable r4 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            androidx.constraintlayout.solver.SolverVariable r3 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            r1 = 8
            r2 = 1
            r13 = 0
            if (r0 == 0) goto L_0x00b0
            if (r0 == 0) goto L_0x0036
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r0 = r0[r13]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r0 != r5) goto L_0x0036
            r0 = 1
            goto L_0x0037
        L_0x0036:
            r0 = 0
        L_0x0037:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r15.mParent
            if (r5 == 0) goto L_0x0045
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r5 = r5.mListDimensionBehaviors
            r5 = r5[r2]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r5 != r7) goto L_0x0045
            r5 = 1
            goto L_0x0046
        L_0x0045:
            r5 = 0
        L_0x0046:
            boolean r7 = r15.isChainHead(r13)
            if (r7 == 0) goto L_0x0055
            androidx.constraintlayout.solver.widgets.ConstraintWidget r7 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r7 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r7
            r7.addChain(r15, r13)
            r7 = 1
            goto L_0x0059
        L_0x0055:
            boolean r7 = r38.isInHorizontalChain()
        L_0x0059:
            boolean r8 = r15.isChainHead(r2)
            if (r8 == 0) goto L_0x0068
            androidx.constraintlayout.solver.widgets.ConstraintWidget r8 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r8 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r8
            r8.addChain(r15, r2)
            r8 = 1
            goto L_0x006c
        L_0x0068:
            boolean r8 = r38.isInVerticalChain()
        L_0x006c:
            if (r0 == 0) goto L_0x0089
            int r9 = r15.mVisibility
            if (r9 == r1) goto L_0x0089
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r15.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            if (r9 != 0) goto L_0x0089
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r15.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            if (r9 != 0) goto L_0x0089
            androidx.constraintlayout.solver.widgets.ConstraintWidget r9 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mRight
            androidx.constraintlayout.solver.SolverVariable r9 = r14.createObjectVariable(r9)
            r14.addGreaterThan(r9, r10, r13, r2)
        L_0x0089:
            if (r5 == 0) goto L_0x00aa
            int r9 = r15.mVisibility
            if (r9 == r1) goto L_0x00aa
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r15.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            if (r9 != 0) goto L_0x00aa
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r15.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mTarget
            if (r9 != 0) goto L_0x00aa
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r15.mBaseline
            if (r9 != 0) goto L_0x00aa
            androidx.constraintlayout.solver.widgets.ConstraintWidget r9 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r9.mBottom
            androidx.constraintlayout.solver.SolverVariable r9 = r14.createObjectVariable(r9)
            r14.addGreaterThan(r9, r4, r13, r2)
        L_0x00aa:
            r12 = r5
            r16 = r7
            r22 = r8
            goto L_0x00b6
        L_0x00b0:
            r0 = 0
            r12 = 0
            r16 = 0
            r22 = 0
        L_0x00b6:
            int r5 = r15.mWidth
            int r7 = r15.mMinWidth
            if (r5 >= r7) goto L_0x00bd
            r5 = r7
        L_0x00bd:
            int r7 = r15.mHeight
            int r8 = r15.mMinHeight
            if (r7 >= r8) goto L_0x00c4
            r7 = r8
        L_0x00c4:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r8 = r15.mListDimensionBehaviors
            r8 = r8[r13]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r8 == r9) goto L_0x00ce
            r8 = 1
            goto L_0x00cf
        L_0x00ce:
            r8 = 0
        L_0x00cf:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r9 = r15.mListDimensionBehaviors
            r9 = r9[r2]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r9 == r11) goto L_0x00d9
            r9 = 1
            goto L_0x00da
        L_0x00d9:
            r9 = 0
        L_0x00da:
            int r11 = r15.mDimensionRatioSide
            r15.mResolvedDimensionRatioSide = r11
            float r11 = r15.mDimensionRatio
            r15.mResolvedDimensionRatio = r11
            int r2 = r15.mMatchConstraintDefaultWidth
            int r13 = r15.mMatchConstraintDefaultHeight
            r18 = 0
            r19 = 4
            int r11 = (r11 > r18 ? 1 : (r11 == r18 ? 0 : -1))
            if (r11 <= 0) goto L_0x01aa
            int r11 = r15.mVisibility
            r1 = 8
            if (r11 == r1) goto L_0x01aa
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r11 = 0
            r1 = r1[r11]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r23 = r3
            r3 = 3
            if (r1 != r11) goto L_0x0103
            if (r2 != 0) goto L_0x0103
            r2 = 3
        L_0x0103:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r11 = 1
            r1 = r1[r11]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r1 != r11) goto L_0x010f
            if (r13 != 0) goto L_0x010f
            r13 = 3
        L_0x010f:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r11 = 0
            r1 = r1[r11]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r1 != r11) goto L_0x012a
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r11 = 1
            r1 = r1[r11]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r1 != r11) goto L_0x012a
            if (r2 != r3) goto L_0x012a
            if (r13 != r3) goto L_0x012a
            r15.setupDimensionRatio(r0, r12, r8, r9)
            goto L_0x019f
        L_0x012a:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r8 = 0
            r1 = r1[r8]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r1 != r9) goto L_0x015f
            if (r2 != r3) goto L_0x015f
            r15.mResolvedDimensionRatioSide = r8
            float r1 = r15.mResolvedDimensionRatio
            int r3 = r15.mHeight
            float r3 = (float) r3
            float r1 = r1 * r3
            int r1 = (int) r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r3 = r15.mListDimensionBehaviors
            r8 = 1
            r3 = r3[r8]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r3 == r5) goto L_0x0154
            r28 = r1
            r29 = r7
            r26 = r13
            r25 = 4
            r27 = 0
            goto L_0x01b6
        L_0x0154:
            r28 = r1
            r25 = r2
            r29 = r7
            r26 = r13
            r27 = 1
            goto L_0x01b6
        L_0x015f:
            r8 = 1
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r1 = r1[r8]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r1 != r9) goto L_0x019f
            if (r13 != r3) goto L_0x019f
            r15.mResolvedDimensionRatioSide = r8
            int r1 = r15.mDimensionRatioSide
            r3 = -1
            if (r1 != r3) goto L_0x0178
            r1 = 1065353216(0x3f800000, float:1.0)
            float r3 = r15.mResolvedDimensionRatio
            float r1 = r1 / r3
            r15.mResolvedDimensionRatio = r1
        L_0x0178:
            float r1 = r15.mResolvedDimensionRatio
            int r3 = r15.mWidth
            float r3 = (float) r3
            float r1 = r1 * r3
            int r1 = (int) r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r3 = r15.mListDimensionBehaviors
            r7 = 0
            r3 = r3[r7]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r3 == r7) goto L_0x0194
            r29 = r1
            r25 = r2
            r28 = r5
            r26 = 4
            r27 = 0
            goto L_0x01b6
        L_0x0194:
            r29 = r1
            r25 = r2
            r28 = r5
            r26 = r13
            r27 = 1
            goto L_0x01b6
        L_0x019f:
            r25 = r2
            r28 = r5
            r29 = r7
            r26 = r13
            r27 = 1
            goto L_0x01b6
        L_0x01aa:
            r23 = r3
            r25 = r2
            r28 = r5
            r29 = r7
            r26 = r13
            r27 = 0
        L_0x01b6:
            int[] r1 = r15.mResolvedMatchConstraintDefault
            r2 = 0
            r1[r2] = r25
            r2 = 1
            r1[r2] = r26
            if (r27 == 0) goto L_0x01cc
            int r1 = r15.mResolvedDimensionRatioSide
            if (r1 == 0) goto L_0x01c8
            r2 = -1
            if (r1 != r2) goto L_0x01cd
            goto L_0x01c9
        L_0x01c8:
            r2 = -1
        L_0x01c9:
            r24 = 1
            goto L_0x01cf
        L_0x01cc:
            r2 = -1
        L_0x01cd:
            r24 = 0
        L_0x01cf:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r3 = 0
            r1 = r1[r3]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r1 != r3) goto L_0x01df
            boolean r1 = r15 instanceof androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
            if (r1 == 0) goto L_0x01df
            r30 = 1
            goto L_0x01e1
        L_0x01df:
            r30 = 0
        L_0x01e1:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mCenter
            boolean r1 = r1.isConnected()
            r3 = 1
            r31 = r1 ^ 1
            int r1 = r15.mHorizontalResolution
            r13 = 2
            r32 = 0
            if (r1 == r13) goto L_0x0259
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r15.mParent
            if (r1 == 0) goto L_0x01fe
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mRight
            androidx.constraintlayout.solver.SolverVariable r1 = r14.createObjectVariable(r1)
            r20 = r1
            goto L_0x0200
        L_0x01fe:
            r20 = r32
        L_0x0200:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r15.mParent
            if (r1 == 0) goto L_0x020d
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mLeft
            androidx.constraintlayout.solver.SolverVariable r1 = r14.createObjectVariable(r1)
            r33 = r1
            goto L_0x020f
        L_0x020d:
            r33 = r32
        L_0x020f:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r17 = 0
            r5 = r1[r17]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r15.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r15.mRight
            int r9 = r15.f14mX
            int r11 = r15.mMinWidth
            int[] r1 = r15.mMaxDimension
            r1 = r1[r17]
            r34 = r12
            r12 = r1
            float r1 = r15.mHorizontalBiasPercent
            r13 = r1
            int r1 = r15.mMatchConstraintMinWidth
            r17 = r1
            int r1 = r15.mMatchConstraintMaxWidth
            r18 = r1
            float r1 = r15.mMatchConstraintPercentWidth
            r19 = r1
            r35 = r0
            r0 = r38
            r1 = r39
            r2 = r35
            r36 = r23
            r3 = r33
            r23 = r4
            r4 = r20
            r37 = r6
            r6 = r30
            r30 = r10
            r10 = r28
            r14 = r24
            r15 = r16
            r16 = r25
            r20 = r31
            r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20)
            r15 = r38
            goto L_0x0265
        L_0x0259:
            r37 = r6
            r30 = r10
            r34 = r12
            r36 = r23
            r23 = r4
            r15 = r38
        L_0x0265:
            int r0 = r15.mVerticalResolution
            r1 = 2
            if (r0 != r1) goto L_0x026b
            return
        L_0x026b:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r14 = 1
            r0 = r0[r14]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r0 != r1) goto L_0x027a
            boolean r0 = r15 instanceof androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
            if (r0 == 0) goto L_0x027a
            r6 = 1
            goto L_0x027b
        L_0x027a:
            r6 = 0
        L_0x027b:
            if (r27 == 0) goto L_0x0287
            int r0 = r15.mResolvedDimensionRatioSide
            if (r0 == r14) goto L_0x0284
            r1 = -1
            if (r0 != r1) goto L_0x0287
        L_0x0284:
            r16 = 1
            goto L_0x0289
        L_0x0287:
            r16 = 0
        L_0x0289:
            int r0 = r15.mBaselineDistance
            if (r0 <= 0) goto L_0x02c8
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            androidx.constraintlayout.solver.widgets.ResolutionAnchor r0 = r0.getResolutionNode()
            int r0 = r0.state
            if (r0 != r14) goto L_0x02a5
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            androidx.constraintlayout.solver.widgets.ResolutionAnchor r0 = r0.getResolutionNode()
            r10 = r39
            r0.addResolvedValue(r10)
            r4 = r37
            goto L_0x02cc
        L_0x02a5:
            r10 = r39
            int r0 = r38.getBaselineDistance()
            r1 = 6
            r2 = r36
            r4 = r37
            r10.addEquality(r2, r4, r0, r1)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x02cc
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            androidx.constraintlayout.solver.SolverVariable r0 = r10.createObjectVariable(r0)
            r3 = 0
            r10.addEquality(r2, r0, r3, r1)
            r20 = 0
            goto L_0x02ce
        L_0x02c8:
            r4 = r37
            r10 = r39
        L_0x02cc:
            r20 = r31
        L_0x02ce:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x02db
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r10.createObjectVariable(r0)
            r24 = r0
            goto L_0x02dd
        L_0x02db:
            r24 = r32
        L_0x02dd:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x02e9
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTop
            androidx.constraintlayout.solver.SolverVariable r0 = r10.createObjectVariable(r0)
            r3 = r0
            goto L_0x02eb
        L_0x02e9:
            r3 = r32
        L_0x02eb:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r5 = r0[r14]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r15.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r15.mBottom
            int r9 = r15.f15mY
            int r11 = r15.mMinHeight
            int[] r0 = r15.mMaxDimension
            r12 = r0[r14]
            float r13 = r15.mVerticalBiasPercent
            int r0 = r15.mMatchConstraintMinHeight
            r17 = r0
            int r0 = r15.mMatchConstraintMaxHeight
            r18 = r0
            float r0 = r15.mMatchConstraintPercentHeight
            r19 = r0
            r0 = r38
            r1 = r39
            r2 = r34
            r25 = r4
            r4 = r24
            r10 = r29
            r14 = r16
            r15 = r22
            r16 = r26
            r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20)
            if (r27 == 0) goto L_0x0349
            r6 = 6
            r7 = r38
            int r0 = r7.mResolvedDimensionRatioSide
            r1 = 1
            if (r0 != r1) goto L_0x0338
            float r5 = r7.mResolvedDimensionRatio
            r0 = r39
            r1 = r23
            r2 = r25
            r3 = r30
            r4 = r21
            r0.addRatio(r1, r2, r3, r4, r5, r6)
            goto L_0x034b
        L_0x0338:
            float r5 = r7.mResolvedDimensionRatio
            r6 = 6
            r0 = r39
            r1 = r30
            r2 = r21
            r3 = r23
            r4 = r25
            r0.addRatio(r1, r2, r3, r4, r5, r6)
            goto L_0x034b
        L_0x0349:
            r7 = r38
        L_0x034b:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.mCenter
            boolean r0 = r0.isConnected()
            if (r0 == 0) goto L_0x0373
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.mCenter
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.getTarget()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r0.getOwner()
            float r1 = r7.mCircleConstraintAngle
            r2 = 1119092736(0x42b40000, float:90.0)
            float r1 = r1 + r2
            double r1 = (double) r1
            double r1 = java.lang.Math.toRadians(r1)
            float r1 = (float) r1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r7.mCenter
            int r2 = r2.getMargin()
            r3 = r39
            r3.addCenterPoint(r7, r0, r1, r2)
        L_0x0373:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.addToSolver(androidx.constraintlayout.solver.LinearSystem):void");
    }

    public void setupDimensionRatio(boolean z, boolean z2, boolean z3, boolean z4) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (z3 && !z4) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!z3 && z4) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (z && !z2) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!z && z2) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1 && z && z2) {
            this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
            this.mResolvedDimensionRatioSide = 1;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:169:0x0357  */
    /* JADX WARNING: Removed duplicated region for block: B:179:0x0376  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x037c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyConstraints(androidx.constraintlayout.solver.LinearSystem r29, boolean r30, androidx.constraintlayout.solver.SolverVariable r31, androidx.constraintlayout.solver.SolverVariable r32, androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour r33, boolean r34, androidx.constraintlayout.solver.widgets.ConstraintAnchor r35, androidx.constraintlayout.solver.widgets.ConstraintAnchor r36, int r37, int r38, int r39, int r40, float r41, boolean r42, boolean r43, int r44, int r45, int r46, float r47, boolean r48) {
        /*
            r28 = this;
            r0 = r28
            r10 = r29
            r11 = r31
            r12 = r32
            r13 = r35
            r14 = r36
            r1 = r39
            r2 = r40
            androidx.constraintlayout.solver.SolverVariable r15 = r10.createObjectVariable(r13)
            androidx.constraintlayout.solver.SolverVariable r9 = r10.createObjectVariable(r14)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r35.getTarget()
            androidx.constraintlayout.solver.SolverVariable r8 = r10.createObjectVariable(r3)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r36.getTarget()
            androidx.constraintlayout.solver.SolverVariable r7 = r10.createObjectVariable(r3)
            boolean r3 = r10.graphOptimizer
            r6 = 1
            r4 = 6
            r5 = 0
            if (r3 == 0) goto L_0x0066
            androidx.constraintlayout.solver.widgets.ResolutionAnchor r3 = r35.getResolutionNode()
            int r3 = r3.state
            if (r3 != r6) goto L_0x0066
            androidx.constraintlayout.solver.widgets.ResolutionAnchor r3 = r36.getResolutionNode()
            int r3 = r3.state
            if (r3 != r6) goto L_0x0066
            androidx.constraintlayout.solver.Metrics r1 = androidx.constraintlayout.solver.LinearSystem.getMetrics()
            if (r1 == 0) goto L_0x0050
            androidx.constraintlayout.solver.Metrics r1 = androidx.constraintlayout.solver.LinearSystem.getMetrics()
            long r2 = r1.resolvedWidgets
            r6 = 1
            long r2 = r2 + r6
            r1.resolvedWidgets = r2
        L_0x0050:
            androidx.constraintlayout.solver.widgets.ResolutionAnchor r1 = r35.getResolutionNode()
            r1.addResolvedValue(r10)
            androidx.constraintlayout.solver.widgets.ResolutionAnchor r1 = r36.getResolutionNode()
            r1.addResolvedValue(r10)
            if (r43 != 0) goto L_0x0065
            if (r30 == 0) goto L_0x0065
            r10.addGreaterThan(r12, r9, r5, r4)
        L_0x0065:
            return
        L_0x0066:
            androidx.constraintlayout.solver.Metrics r3 = androidx.constraintlayout.solver.LinearSystem.getMetrics()
            if (r3 == 0) goto L_0x007b
            androidx.constraintlayout.solver.Metrics r3 = androidx.constraintlayout.solver.LinearSystem.getMetrics()
            r18 = r7
            long r6 = r3.nonresolvedWidgets
            r16 = 1
            long r6 = r6 + r16
            r3.nonresolvedWidgets = r6
            goto L_0x007d
        L_0x007b:
            r18 = r7
        L_0x007d:
            boolean r16 = r35.isConnected()
            boolean r17 = r36.isConnected()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r0.mCenter
            boolean r20 = r3.isConnected()
            if (r16 == 0) goto L_0x008f
            r3 = 1
            goto L_0x0090
        L_0x008f:
            r3 = 0
        L_0x0090:
            if (r17 == 0) goto L_0x0094
            int r3 = r3 + 1
        L_0x0094:
            if (r20 == 0) goto L_0x0098
            int r3 = r3 + 1
        L_0x0098:
            r7 = r3
            if (r42 == 0) goto L_0x009d
            r3 = 3
            goto L_0x009f
        L_0x009d:
            r3 = r44
        L_0x009f:
            int[] r21 = androidx.constraintlayout.solver.widgets.ConstraintWidget.C01341.f17xdde91696
            int r22 = r33.ordinal()
            r21 = r21[r22]
            r5 = 4
            switch(r21) {
                case 1: goto L_0x00bc;
                case 2: goto L_0x00b9;
                case 3: goto L_0x00b6;
                case 4: goto L_0x00ae;
                default: goto L_0x00ab;
            }
        L_0x00ab:
            r21 = 0
            goto L_0x00be
        L_0x00ae:
            if (r3 != r5) goto L_0x00b3
            r21 = 0
            goto L_0x00be
        L_0x00b3:
            r21 = 1
            goto L_0x00be
        L_0x00b6:
            r21 = 0
            goto L_0x00be
        L_0x00b9:
            r21 = 0
            goto L_0x00be
        L_0x00bc:
            r21 = 0
        L_0x00be:
            int r5 = r0.mVisibility
            r6 = 8
            if (r5 != r6) goto L_0x00c8
            r5 = 0
            r21 = 0
            goto L_0x00ca
        L_0x00c8:
            r5 = r38
        L_0x00ca:
            if (r48 == 0) goto L_0x00e3
            if (r16 != 0) goto L_0x00d8
            if (r17 != 0) goto L_0x00d8
            if (r20 != 0) goto L_0x00d8
            r6 = r37
            r10.addEquality(r15, r6)
            goto L_0x00e3
        L_0x00d8:
            if (r16 == 0) goto L_0x00e3
            if (r17 != 0) goto L_0x00e3
            int r6 = r35.getMargin()
            r10.addEquality(r15, r8, r6, r4)
        L_0x00e3:
            if (r21 != 0) goto L_0x0113
            if (r34 == 0) goto L_0x0100
            r4 = 3
            r6 = 0
            r10.addEquality(r9, r15, r6, r4)
            if (r1 <= 0) goto L_0x00f3
            r5 = 6
            r10.addGreaterThan(r9, r15, r1, r5)
            goto L_0x00f4
        L_0x00f3:
            r5 = 6
        L_0x00f4:
            r4 = 2147483647(0x7fffffff, float:NaN)
            if (r2 >= r4) goto L_0x00fe
            r10.addLowerThan(r9, r15, r2, r5)
            r4 = 6
            goto L_0x0104
        L_0x00fe:
            r4 = 6
            goto L_0x0104
        L_0x0100:
            r6 = 0
            r10.addEquality(r9, r15, r5, r4)
        L_0x0104:
            r13 = r45
            r34 = r3
            r14 = r7
            r19 = r8
            r27 = r18
            r22 = 4
            r8 = r46
            goto L_0x0226
        L_0x0113:
            r6 = 0
            r2 = -2
            r6 = r45
            if (r6 != r2) goto L_0x011d
            r6 = r46
            r4 = r5
            goto L_0x0120
        L_0x011d:
            r4 = r6
            r6 = r46
        L_0x0120:
            if (r6 != r2) goto L_0x0123
            r6 = r5
        L_0x0123:
            if (r4 <= 0) goto L_0x012e
            r2 = 6
            r10.addGreaterThan(r9, r15, r4, r2)
            int r5 = java.lang.Math.max(r5, r4)
            goto L_0x012f
        L_0x012e:
            r2 = 6
        L_0x012f:
            if (r6 <= 0) goto L_0x013a
            r10.addLowerThan(r9, r15, r6, r2)
            int r5 = java.lang.Math.min(r5, r6)
            r2 = 1
            goto L_0x013b
        L_0x013a:
            r2 = 1
        L_0x013b:
            if (r3 != r2) goto L_0x017f
            if (r30 == 0) goto L_0x0151
            r2 = 6
            r10.addEquality(r9, r15, r5, r2)
            r34 = r3
            r13 = r4
            r0 = r5
            r14 = r7
            r19 = r8
            r27 = r18
            r22 = 4
            r8 = r6
            goto L_0x0208
        L_0x0151:
            r2 = 6
            if (r43 == 0) goto L_0x0169
            r34 = r6
            r6 = 4
            r10.addEquality(r9, r15, r5, r6)
            r13 = r4
            r0 = r5
            r14 = r7
            r19 = r8
            r27 = r18
            r22 = 4
            r8 = r34
            r34 = r3
            goto L_0x0208
        L_0x0169:
            r34 = r6
            r2 = 1
            r6 = 4
            r10.addEquality(r9, r15, r5, r2)
            r13 = r4
            r0 = r5
            r14 = r7
            r19 = r8
            r27 = r18
            r22 = 4
            r8 = r34
            r34 = r3
            goto L_0x0208
        L_0x017f:
            r34 = r6
            r6 = 2
            if (r3 != r6) goto L_0x01fb
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = r35.getType()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r6 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            if (r2 == r6) goto L_0x01b3
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = r35.getType()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r6 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            if (r2 != r6) goto L_0x0195
            goto L_0x01b3
        L_0x0195:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r6 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.getAnchor(r6)
            androidx.constraintlayout.solver.SolverVariable r2 = r10.createObjectVariable(r2)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r6 = r0.mParent
            r40 = r2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r6.getAnchor(r2)
            androidx.constraintlayout.solver.SolverVariable r2 = r10.createObjectVariable(r2)
            r21 = r40
            r6 = r2
            goto L_0x01d0
        L_0x01b3:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r6 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.getAnchor(r6)
            androidx.constraintlayout.solver.SolverVariable r2 = r10.createObjectVariable(r2)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r6 = r0.mParent
            r40 = r2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r6.getAnchor(r2)
            androidx.constraintlayout.solver.SolverVariable r2 = r10.createObjectVariable(r2)
            r21 = r40
            r6 = r2
        L_0x01d0:
            androidx.constraintlayout.solver.ArrayRow r2 = r29.createRow()
            r24 = 6
            r25 = 1
            r14 = r3
            r3 = r9
            r13 = r4
            r0 = 6
            r23 = 3
            r4 = r15
            r0 = r5
            r19 = r8
            r8 = 0
            r22 = 4
            r5 = r6
            r8 = r34
            r34 = r14
            r14 = 2
            r6 = r21
            r14 = r7
            r27 = r18
            r7 = r47
            androidx.constraintlayout.solver.ArrayRow r2 = r2.createRowDimensionRatio(r3, r4, r5, r6, r7)
            r10.addConstraint(r2)
            r5 = 0
            goto L_0x020a
        L_0x01fb:
            r13 = r4
            r0 = r5
            r14 = r7
            r19 = r8
            r27 = r18
            r22 = 4
            r8 = r34
            r34 = r3
        L_0x0208:
            r5 = r21
        L_0x020a:
            if (r5 == 0) goto L_0x0224
            r2 = 2
            if (r14 == r2) goto L_0x0224
            if (r42 != 0) goto L_0x0224
            int r0 = java.lang.Math.max(r13, r0)
            if (r8 <= 0) goto L_0x021d
            int r0 = java.lang.Math.min(r8, r0)
            r2 = 6
            goto L_0x021e
        L_0x021d:
            r2 = 6
        L_0x021e:
            r10.addEquality(r9, r15, r0, r2)
            r21 = 0
            goto L_0x0226
        L_0x0224:
            r21 = r5
        L_0x0226:
            if (r48 == 0) goto L_0x0388
            if (r43 == 0) goto L_0x022c
            goto L_0x0388
        L_0x022c:
            r0 = 5
            if (r16 != 0) goto L_0x0243
            if (r17 != 0) goto L_0x0243
            if (r20 != 0) goto L_0x0243
            if (r30 == 0) goto L_0x023e
            r14 = 0
            r10.addGreaterThan(r12, r9, r14, r0)
            r0 = r9
            r1 = 0
            r2 = 6
            goto L_0x0382
        L_0x023e:
            r0 = r9
            r1 = 0
            r2 = 6
            goto L_0x0382
        L_0x0243:
            r14 = 0
            if (r16 == 0) goto L_0x0257
            if (r17 != 0) goto L_0x0257
            if (r30 == 0) goto L_0x0252
            r10.addGreaterThan(r12, r9, r14, r0)
            r0 = r9
            r1 = 0
            r2 = 6
            goto L_0x0382
        L_0x0252:
            r0 = r9
            r1 = 0
            r2 = 6
            goto L_0x0382
        L_0x0257:
            if (r16 != 0) goto L_0x0275
            if (r17 == 0) goto L_0x0275
            int r1 = r36.getMargin()
            int r1 = -r1
            r7 = r27
            r2 = 6
            r10.addEquality(r9, r7, r1, r2)
            if (r30 == 0) goto L_0x0270
            r10.addGreaterThan(r15, r11, r14, r0)
            r0 = r9
            r1 = 0
            r2 = 6
            goto L_0x0382
        L_0x0270:
            r0 = r9
            r1 = 0
            r2 = 6
            goto L_0x0382
        L_0x0275:
            r7 = r27
            if (r16 == 0) goto L_0x037f
            if (r17 == 0) goto L_0x037f
            if (r21 == 0) goto L_0x02f8
            if (r30 == 0) goto L_0x0286
            if (r1 != 0) goto L_0x0286
            r1 = 6
            r10.addGreaterThan(r9, r15, r14, r1)
            goto L_0x0287
        L_0x0286:
            r1 = 6
        L_0x0287:
            if (r34 != 0) goto L_0x02b5
            if (r8 > 0) goto L_0x0291
            if (r13 <= 0) goto L_0x028e
            goto L_0x0291
        L_0x028e:
            r2 = 6
            r6 = 0
            goto L_0x0293
        L_0x0291:
            r2 = 4
            r6 = 1
        L_0x0293:
            int r3 = r35.getMargin()
            r5 = r19
            r10.addEquality(r15, r5, r3, r2)
            int r3 = r36.getMargin()
            int r3 = -r3
            r10.addEquality(r9, r7, r3, r2)
            if (r8 > 0) goto L_0x02ab
            if (r13 <= 0) goto L_0x02a9
            goto L_0x02ab
        L_0x02a9:
            r2 = 0
            goto L_0x02ac
        L_0x02ab:
            r2 = 1
        L_0x02ac:
            r17 = r6
            r4 = 6
            r6 = r28
            r13 = 1
            r16 = 5
            goto L_0x0303
        L_0x02b5:
            r6 = r34
            r5 = r19
            r13 = 1
            if (r6 != r13) goto L_0x02c5
            r2 = 1
            r4 = 6
            r6 = r28
            r16 = 6
            r17 = 1
            goto L_0x0303
        L_0x02c5:
            r2 = 3
            if (r6 != r2) goto L_0x02ef
            if (r42 != 0) goto L_0x02d6
            r4 = 6
            r6 = r28
            int r1 = r6.mResolvedDimensionRatioSide
            r2 = -1
            if (r1 == r2) goto L_0x02d9
            if (r8 > 0) goto L_0x02d9
            r1 = 6
            goto L_0x02da
        L_0x02d6:
            r4 = 6
            r6 = r28
        L_0x02d9:
            r1 = 4
        L_0x02da:
            int r2 = r35.getMargin()
            r10.addEquality(r15, r5, r2, r1)
            int r2 = r36.getMargin()
            int r2 = -r2
            r10.addEquality(r9, r7, r2, r1)
            r2 = 1
            r16 = 5
            r17 = 1
            goto L_0x0303
        L_0x02ef:
            r4 = 6
            r6 = r28
            r2 = 0
            r16 = 5
            r17 = 0
            goto L_0x0303
        L_0x02f8:
            r5 = r19
            r4 = 6
            r6 = r28
            r13 = 1
            r2 = 1
            r16 = 5
            r17 = 0
        L_0x0303:
            if (r2 == 0) goto L_0x0348
            int r8 = r35.getMargin()
            int r18 = r36.getMargin()
            r1 = r29
            r2 = r15
            r3 = r5
            r0 = 6
            r4 = r8
            r8 = r5
            r5 = r41
            r6 = r7
            r13 = r7
            r7 = r9
            r14 = r8
            r0 = 0
            r8 = r18
            r0 = r9
            r9 = r16
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            r1 = r35
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r1.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r2.mOwner
            boolean r2 = r2 instanceof androidx.constraintlayout.solver.widgets.Barrier
            r3 = r36
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r3.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r4 = r4.mOwner
            boolean r4 = r4 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r2 == 0) goto L_0x033d
            if (r4 != 0) goto L_0x033d
            r26 = r30
            r2 = 1
            r4 = 5
            r5 = 6
            goto L_0x0355
        L_0x033d:
            if (r2 != 0) goto L_0x034f
            if (r4 == 0) goto L_0x034f
            r2 = r30
            r4 = 6
            r5 = 5
            r26 = 1
            goto L_0x0355
        L_0x0348:
            r14 = r5
            r13 = r7
            r0 = r9
            r1 = r35
            r3 = r36
        L_0x034f:
            r2 = r30
            r26 = r2
            r4 = 5
            r5 = 5
        L_0x0355:
            if (r17 == 0) goto L_0x0359
            r4 = 6
            r5 = 6
        L_0x0359:
            if (r21 != 0) goto L_0x035d
            if (r26 != 0) goto L_0x035f
        L_0x035d:
            if (r17 == 0) goto L_0x0366
        L_0x035f:
            int r1 = r35.getMargin()
            r10.addGreaterThan(r15, r14, r1, r4)
        L_0x0366:
            if (r21 != 0) goto L_0x036a
            if (r2 != 0) goto L_0x036c
        L_0x036a:
            if (r17 == 0) goto L_0x0374
        L_0x036c:
            int r1 = r36.getMargin()
            int r1 = -r1
            r10.addLowerThan(r0, r13, r1, r5)
        L_0x0374:
            if (r30 == 0) goto L_0x037c
            r1 = 0
            r2 = 6
            r10.addGreaterThan(r15, r11, r1, r2)
            goto L_0x0382
        L_0x037c:
            r1 = 0
            r2 = 6
            goto L_0x0382
        L_0x037f:
            r0 = r9
            r1 = 0
            r2 = 6
        L_0x0382:
            if (r30 == 0) goto L_0x0387
            r10.addGreaterThan(r12, r0, r1, r2)
        L_0x0387:
            return
        L_0x0388:
            r0 = r9
            r1 = 0
            r2 = 6
            r3 = 2
            if (r14 >= r3) goto L_0x0396
            if (r30 == 0) goto L_0x0396
            r10.addGreaterThan(r15, r11, r1, r2)
            r10.addGreaterThan(r12, r0, r1, r2)
        L_0x0396:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.applyConstraints(androidx.constraintlayout.solver.LinearSystem, boolean, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour, boolean, androidx.constraintlayout.solver.widgets.ConstraintAnchor, androidx.constraintlayout.solver.widgets.ConstraintAnchor, int, int, int, int, float, boolean, boolean, int, int, int, float, boolean):void");
    }

    public void updateFromSolver(LinearSystem linearSystem) {
        int objectVariableValue = linearSystem.getObjectVariableValue(this.mLeft);
        int objectVariableValue2 = linearSystem.getObjectVariableValue(this.mTop);
        int objectVariableValue3 = linearSystem.getObjectVariableValue(this.mRight);
        int objectVariableValue4 = linearSystem.getObjectVariableValue(this.mBottom);
        int i = objectVariableValue4 - objectVariableValue2;
        if (objectVariableValue3 - objectVariableValue < 0 || i < 0 || objectVariableValue == Integer.MIN_VALUE || objectVariableValue == Integer.MAX_VALUE || objectVariableValue2 == Integer.MIN_VALUE || objectVariableValue2 == Integer.MAX_VALUE || objectVariableValue3 == Integer.MIN_VALUE || objectVariableValue3 == Integer.MAX_VALUE || objectVariableValue4 == Integer.MIN_VALUE || objectVariableValue4 == Integer.MAX_VALUE) {
            objectVariableValue4 = 0;
            objectVariableValue = 0;
            objectVariableValue2 = 0;
            objectVariableValue3 = 0;
        }
        setFrame(objectVariableValue, objectVariableValue2, objectVariableValue3, objectVariableValue4);
    }
}
