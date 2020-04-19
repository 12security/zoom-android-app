package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.PullDownRefreshListView */
public class PullDownRefreshListView extends ListView implements OnTouchListener {
    private int mDeltaY = 0;
    private boolean mIsFingerReleased = true;
    private boolean mIsRefreshing = false;
    private int mLastX = 0;
    private int mLastY = 0;
    private PullDownRefreshListener mListener;
    private NotificationView mNotificationView;
    private int mOverScrollSize = 0;
    private boolean mPullDownRefreshEnabled = true;
    private int mYOnOverScroll = 0;
    private boolean mbFirstMove = false;
    private boolean mbOverScrolled = false;
    private boolean mbScrollingHorizontal = false;

    /* renamed from: us.zoom.androidlib.widget.PullDownRefreshListView$NotificationView */
    static class NotificationView extends LinearLayout {
        static final int MIN_SIZE_TO_REFRESH = 120;
        ImageView mImgIcon = null;
        View mItemContainer = null;
        float mOverScrollSize = 0.0f;
        View mProgressBar = null;
        int mTextLoading = C4409R.string.zm_lbl_pull_down_refresh_list_loading;
        int mTextPullDownToRefresh = C4409R.string.zm_lbl_pull_down_refresh_list_pull_down_to_refresh;
        int mTextReleaseToRefresh = C4409R.string.zm_lbl_pull_down_refresh_list_release_to_refresh;
        TextView mTxtMsg = null;

        public NotificationView(Context context) {
            super(context);
            View.inflate(context, C4409R.layout.zm_pull_down_refresh_message, this);
            this.mImgIcon = (ImageView) findViewById(C4409R.C4411id.imgIcon);
            this.mTxtMsg = (TextView) findViewById(C4409R.C4411id.txtMsg);
            this.mItemContainer = findViewById(C4409R.C4411id.itemContainer);
            this.mProgressBar = findViewById(C4409R.C4411id.progressBar1);
            this.mProgressBar.setVisibility(8);
        }

        public void clearStatus() {
            this.mOverScrollSize = 0.0f;
            this.mImgIcon.clearAnimation();
            this.mTxtMsg.setText(this.mTextPullDownToRefresh);
            this.mProgressBar.setVisibility(8);
            this.mImgIcon.setVisibility(0);
        }

        public void setTextResources(int i, int i2, int i3) {
            this.mTextReleaseToRefresh = i;
            this.mTextPullDownToRefresh = i2;
            this.mTextLoading = i3;
            updateUI(this.mOverScrollSize);
        }

        public void showLoading() {
            this.mImgIcon.clearAnimation();
            this.mImgIcon.setVisibility(8);
            this.mProgressBar.setVisibility(0);
            this.mTxtMsg.setText(this.mTextLoading);
            this.mItemContainer.setVisibility(0);
        }

        public void updateUI(float f) {
            boolean z = false;
            boolean z2 = this.mOverScrollSize < ((float) UIUtil.dip2px(getContext(), 120.0f));
            this.mOverScrollSize = f;
            if (this.mOverScrollSize < ((float) UIUtil.dip2px(getContext(), 120.0f))) {
                z = true;
            }
            if (z2 != z) {
                if (z) {
                    this.mTxtMsg.setText(this.mTextPullDownToRefresh);
                    Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), C4409R.anim.zm_pull_down_refresh_rotate_to_down);
                    loadAnimation.setInterpolator(new LinearInterpolator());
                    loadAnimation.setFillAfter(true);
                    this.mImgIcon.startAnimation(loadAnimation);
                } else {
                    this.mTxtMsg.setText(this.mTextReleaseToRefresh);
                    Animation loadAnimation2 = AnimationUtils.loadAnimation(getContext(), C4409R.anim.zm_pull_down_refresh_rotate_to_up);
                    LinearInterpolator linearInterpolator = new LinearInterpolator();
                    loadAnimation2.setInterpolator(linearInterpolator);
                    loadAnimation2.setInterpolator(linearInterpolator);
                    loadAnimation2.setFillAfter(true);
                    this.mImgIcon.startAnimation(loadAnimation2);
                }
            }
        }

        public boolean needRefreshOnReleased() {
            return this.mOverScrollSize > ((float) UIUtil.dip2px(getContext(), 120.0f));
        }

        public boolean isNotificationVisible() {
            return this.mItemContainer.getVisibility() == 0;
        }

        public void setMessage(String str) {
            this.mTxtMsg.setText(str);
        }

        public void show(boolean z) {
            this.mItemContainer.setVisibility(z ? 0 : 8);
        }

        public int getNotificationHeight() {
            measure(MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, Integer.MIN_VALUE));
            return getMeasuredHeight();
        }
    }

    /* renamed from: us.zoom.androidlib.widget.PullDownRefreshListView$PullDownRefreshListener */
    public interface PullDownRefreshListener {
        void onPullDownRefresh();
    }

    /* access modifiers changed from: protected */
    public void onOverScrolled(int i, int i2, boolean z, boolean z2) {
    }

    /* access modifiers changed from: protected */
    public void onPullDownRefresh() {
    }

    public PullDownRefreshListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public PullDownRefreshListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public PullDownRefreshListView(Context context) {
        super(context);
        initView(context);
    }

    public void setPullDownRefreshListener(PullDownRefreshListener pullDownRefreshListener) {
        this.mListener = pullDownRefreshListener;
    }

    public void setTextResources(int i, int i2, int i3) {
        this.mNotificationView.setTextResources(i, i2, i3);
    }

    private void initView(Context context) {
        setOnTouchListener(this);
        this.mNotificationView = new NotificationView(getContext());
        addHeaderView(this.mNotificationView);
        this.mNotificationView.show(false);
    }

    public void setPullDownRefreshEnabled(boolean z) {
        this.mPullDownRefreshEnabled = z;
    }

    public boolean isPullDownRefreshEnabled() {
        return this.mPullDownRefreshEnabled;
    }

    /* access modifiers changed from: protected */
    public void ZM_onOverScrolled(int i, int i2, boolean z, boolean z2) {
        if (this.mPullDownRefreshEnabled && !this.mIsRefreshing) {
            scrollBy(0, this.mDeltaY / 2);
            if (this.mIsFingerReleased) {
                scrollTo(0, 0);
            }
            if (this.mYOnOverScroll == 0) {
                this.mYOnOverScroll = this.mLastY;
            }
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.mPullDownRefreshEnabled || this.mIsRefreshing) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 2 && this.mIsFingerReleased) {
            actionMasked = 0;
        }
        switch (actionMasked) {
            case 0:
                this.mLastX = (int) motionEvent.getX();
                this.mLastY = (int) motionEvent.getY();
                this.mIsFingerReleased = false;
                this.mbScrollingHorizontal = false;
                this.mbFirstMove = true;
                this.mOverScrollSize = 0;
                this.mYOnOverScroll = 0;
                break;
            case 1:
                this.mbFirstMove = true;
                if (!this.mbScrollingHorizontal) {
                    this.mLastY = 0;
                    this.mIsFingerReleased = true;
                    if (this.mNotificationView.isNotificationVisible() && this.mNotificationView.needRefreshOnReleased()) {
                        doRefresh();
                    } else if (this.mNotificationView.isNotificationVisible()) {
                        this.mNotificationView.show(false);
                        this.mNotificationView.clearStatus();
                    }
                    if (this.mbOverScrolled) {
                        scrollTo(0, 0);
                    }
                    this.mOverScrollSize = 0;
                    this.mYOnOverScroll = 0;
                    this.mbOverScrolled = false;
                    break;
                } else {
                    this.mbScrollingHorizontal = false;
                    return false;
                }
            case 2:
                if (this.mbScrollingHorizontal) {
                    return false;
                }
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                this.mDeltaY = this.mLastY - y;
                int i = this.mLastX - x;
                if (!this.mbFirstMove || Math.abs(this.mDeltaY) >= Math.abs(i)) {
                    this.mbFirstMove = false;
                    if (Math.abs(this.mDeltaY) < 4) {
                        return false;
                    }
                    this.mLastY = y;
                    boolean isOverScrolledTop = isOverScrolledTop(this.mDeltaY);
                    boolean z = isOverScrolledTop || isOverScrolledBottom(this.mDeltaY);
                    if (z) {
                        ZM_onOverScrolled(getScrollX(), getScrollY(), false, true);
                        this.mbOverScrolled = true;
                    }
                    int i2 = this.mYOnOverScroll;
                    if (i2 > 0) {
                        this.mOverScrollSize = y - i2;
                        this.mNotificationView.updateUI((float) this.mOverScrollSize);
                        if (isOverScrolledTop && !this.mNotificationView.isNotificationVisible()) {
                            this.mNotificationView.show(true);
                            scrollTo(0, this.mNotificationView.getNotificationHeight());
                        }
                        if (!z) {
                            scrollBy(0, this.mDeltaY / 2);
                        }
                    }
                    return false;
                }
                this.mbScrollingHorizontal = true;
                this.mbFirstMove = false;
                return false;
        }
        return false;
    }

    public void showRefreshing(boolean z) {
        if (z) {
            this.mIsRefreshing = true;
            this.mNotificationView.showLoading();
            setSelection(0);
            scrollTo(0, 0);
            return;
        }
        this.mIsRefreshing = false;
        this.mNotificationView.clearStatus();
        this.mNotificationView.show(false);
    }

    public boolean isRefreshing() {
        return this.mIsRefreshing;
    }

    public void notifyRefreshDone() {
        showRefreshing(false);
    }

    private boolean isOverScrolledTop(int i) {
        boolean z = false;
        if (i >= 0 || getChildCount() <= 0 || getFirstVisiblePosition() > 0) {
            return false;
        }
        if (getChildAt(0).getTop() >= 0) {
            z = true;
        }
        return z;
    }

    private boolean isOverScrolledBottom(int i) {
        boolean z = false;
        if (i <= 0 || getChildCount() <= 0 || getLastVisiblePosition() < getCount() - 1) {
            return false;
        }
        if (getChildAt(getChildCount() - 1).getBottom() <= getHeight()) {
            z = true;
        }
        return z;
    }

    private void doRefresh() {
        showRefreshing(true);
        PullDownRefreshListener pullDownRefreshListener = this.mListener;
        if (pullDownRefreshListener != null) {
            pullDownRefreshListener.onPullDownRefresh();
        }
        onPullDownRefresh();
    }
}
