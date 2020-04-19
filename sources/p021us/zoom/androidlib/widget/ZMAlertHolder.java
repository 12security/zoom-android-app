package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ListAdapter;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMAlertHolder */
/* compiled from: ZMAlertDialog */
class ZMAlertHolder {
    public static final int ZM_CONTENT_TYPE_ITEMS = 3;
    public static final int ZM_CONTENT_TYPE_LIST = 2;
    public static final int ZM_CONTENT_TYPE_MSG = 1;
    public static final int ZM_CONTENT_TYPE_MULTIITEMS = 4;
    public static final int ZM_CONTENT_TYPE_NONE = 0;
    public static final int ZM_CONTENT_TYPE_VIEW = 5;
    private ListAdapter adater;
    private boolean cancelable;
    private int checkIndex = -1;
    private int[] checkedIndexs;
    private Context context;
    private ZMAlertDialog dialog;
    private OnDismissListener dismissListener;
    private Drawable icon;
    private boolean isHideBottomGap;
    private boolean isMultiChoice;
    private boolean isSingleChoice;
    private CharSequence[] items;
    private int listDividerHeight = 0;
    private OnClickListener listListener;
    private ContentPadding mContentPadding;
    private boolean mEnableAutoClickBtnDismiss = true;
    private String mNegativeButtonText;
    private String mNeutralButtonText;
    private String mPositiveButtonText;
    private View mTitleView;
    private View mView;
    private boolean mViewSpacingSpecified;
    private CharSequence msg;
    private OnClickListener negativeButtonListener;
    private OnClickListener neutralButtonListener;
    private OnClickListener positiveButtonListener;
    private boolean smallTitleMutlilineStyle = false;
    private int theme = C4409R.style.ZMDialog_Material;
    private CharSequence title;
    private float titleFontSize = 0.0f;
    private int titleTxtColor = 0;
    private int type;
    private boolean verticalOptionStyle = false;

    /* renamed from: us.zoom.androidlib.widget.ZMAlertHolder$ContentPadding */
    /* compiled from: ZMAlertDialog */
    public class ContentPadding {
        int bottom;
        int left;
        int right;
        int top;

        public ContentPadding() {
        }
    }

    public OnClickListener getNegativeButtonListener() {
        return this.negativeButtonListener;
    }

    public void setNegativeButtonListener(OnClickListener onClickListener) {
        this.negativeButtonListener = onClickListener;
    }

    public OnClickListener getPositiveButtonListener() {
        return this.positiveButtonListener;
    }

    public void setPositiveButtonListener(OnClickListener onClickListener) {
        this.positiveButtonListener = onClickListener;
    }

    public OnClickListener getNeutralButtonListener() {
        return this.neutralButtonListener;
    }

    public void setNeutralButtonListener(OnClickListener onClickListener) {
        this.neutralButtonListener = onClickListener;
    }

    public void setmPositiveButtonText(String str) {
        this.mPositiveButtonText = str;
    }

    public void setmNeutralButtonText(String str) {
        this.mNeutralButtonText = str;
    }

    public void setmNegativeButtonText(String str) {
        this.mNegativeButtonText = str;
    }

    ZMAlertHolder(Context context2) {
        this.context = context2;
        this.cancelable = true;
        this.type = 0;
    }

    public String getmPositiveButtonText() {
        return this.mPositiveButtonText;
    }

    public String getmNeutralButtonText() {
        return this.mNeutralButtonText;
    }

    public String getmNegativeButtonText() {
        return this.mNegativeButtonText;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context2) {
        this.context = context2;
    }

    public boolean isVerticalOptionStyle() {
        return this.verticalOptionStyle;
    }

    public void setVerticalOptionStyle(boolean z) {
        this.verticalOptionStyle = z;
    }

    public boolean isSmallTitleMutlilineStyle() {
        return this.smallTitleMutlilineStyle;
    }

    public void setSmallTitleMutlilineStyle(boolean z) {
        this.smallTitleMutlilineStyle = z;
    }

    public float getTitleFontSize() {
        return this.titleFontSize;
    }

    public void setTitleFontSize(float f) {
        this.titleFontSize = f;
    }

    public int getTitleTxtColor() {
        return this.titleTxtColor;
    }

    public void setTitleTxtColor(int i) {
        this.titleTxtColor = i;
    }

    public CharSequence getMsg() {
        return this.msg;
    }

    public void setMsg(CharSequence charSequence) {
        this.msg = charSequence;
        if (charSequence != null) {
            this.type = 1;
        } else if (this.type == 1) {
            this.type = 0;
        }
    }

    public CharSequence getTitle() {
        return this.title;
    }

    public void setTitle(CharSequence charSequence) {
        this.title = charSequence;
    }

    public void setIcon(Drawable drawable) {
        this.icon = drawable;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(int i) {
        this.icon = this.context.getResources().getDrawable(i);
    }

    public void setCancelable(boolean z) {
        this.cancelable = z;
    }

    public boolean isCancelable() {
        return this.cancelable;
    }

    public void setDialog(ZMAlertDialog zMAlertDialog) {
        this.dialog = zMAlertDialog;
    }

    public ZMAlertDialog getDialog() {
        return this.dialog;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public ListAdapter getAdater() {
        return this.adater;
    }

    public void setAdater(ListAdapter listAdapter) {
        this.adater = listAdapter;
        this.type = 2;
    }

    public void setListListener(OnClickListener onClickListener) {
        this.listListener = onClickListener;
    }

    public OnClickListener getListListener() {
        return this.listListener;
    }

    public void setDismissListener(OnDismissListener onDismissListener) {
        this.dismissListener = onDismissListener;
    }

    public OnDismissListener getDismissListener() {
        return this.dismissListener;
    }

    public void setItems(CharSequence[] charSequenceArr) {
        this.items = charSequenceArr;
    }

    public CharSequence[] getItems() {
        return this.items;
    }

    public boolean isMultiChoice() {
        return this.isMultiChoice;
    }

    public void setMultiChoice(boolean z) {
        this.isMultiChoice = z;
    }

    public boolean isHideBottomGap() {
        return this.isHideBottomGap;
    }

    public void setHideBottomGap(boolean z) {
        this.isHideBottomGap = z;
    }

    public int getCheckIndex() {
        return this.checkIndex;
    }

    public void setCheckIndex(int i) {
        this.checkIndex = i;
    }

    public int[] getCheckedIndexs() {
        return this.checkedIndexs;
    }

    public void setCheckedIndexs(int[] iArr) {
        this.checkedIndexs = iArr;
    }

    public void setSingleChoice(boolean z) {
        this.isSingleChoice = z;
    }

    public boolean isSingleChoice() {
        return this.isSingleChoice;
    }

    public View getmView() {
        return this.mView;
    }

    public void setmView(View view) {
        this.mView = view;
        this.type = 5;
    }

    public boolean ismViewSpacingSpecified() {
        return this.mViewSpacingSpecified;
    }

    public void setmViewSpacingSpecified(boolean z) {
        this.mViewSpacingSpecified = z;
    }

    public int getTheme() {
        return this.theme;
    }

    public void setTheme(int i) {
        this.theme = i;
    }

    public View getTitleView() {
        return this.mTitleView;
    }

    public void setTitleView(View view) {
        this.mTitleView = view;
    }

    public int getListDividerHeight() {
        return this.listDividerHeight;
    }

    public void setListDividerHeight(int i) {
        this.listDividerHeight = i;
    }

    public void setContentPadding(int i, int i2, int i3, int i4) {
        this.mContentPadding = new ContentPadding();
        ContentPadding contentPadding = this.mContentPadding;
        contentPadding.left = i;
        contentPadding.top = i2;
        contentPadding.right = i3;
        contentPadding.bottom = i4;
    }

    public ContentPadding getContentPadding() {
        return this.mContentPadding;
    }

    public boolean isEnableAutoClickBtnDismiss() {
        return this.mEnableAutoClickBtnDismiss;
    }

    public void setEnableAutoClickBtnDismiss(boolean z) {
        this.mEnableAutoClickBtnDismiss = z;
    }
}
