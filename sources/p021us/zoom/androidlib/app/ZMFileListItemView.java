package p021us.zoom.androidlib.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.widget.ZMCheckedTextView;

/* renamed from: us.zoom.androidlib.app.ZMFileListItemView */
public class ZMFileListItemView extends LinearLayout {
    private boolean mChecked = false;
    private Context mContext;
    private TextView mCountOrSize;
    private TextView mDisplayName;
    private ZMCheckedTextView mFileIndicator;
    private ImageView mFolderIndicator;
    private ImageView mIcon;
    private TextView mLastModifiedDate;
    private TextView mSeparator;

    public ZMFileListItemView(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    public ZMFileListItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    @SuppressLint({"NewApi"})
    public ZMFileListItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, C4409R.layout.zm_file_list_item, this);
        this.mDisplayName = (TextView) findViewById(C4409R.C4411id.txtFileName);
        this.mIcon = (ImageView) findViewById(C4409R.C4411id.fileIcon);
        this.mFolderIndicator = (ImageView) findViewById(C4409R.C4411id.folderIndicator);
        this.mCountOrSize = (TextView) findViewById(C4409R.C4411id.txtFileSize);
        this.mLastModifiedDate = (TextView) findViewById(C4409R.C4411id.txtDate);
        this.mSeparator = (TextView) findViewById(C4409R.C4411id.separator);
        this.mFileIndicator = (ZMCheckedTextView) findViewById(C4409R.C4411id.check);
    }

    public void setDisplayName(String str) {
        if (str == null) {
            this.mDisplayName.setText("");
        } else {
            this.mDisplayName.setText(str);
        }
    }

    public void setIcon(int i) {
        this.mIcon.setImageResource(i);
    }

    public void setFolderIndicatorVisible(boolean z) {
        if (z) {
            this.mFolderIndicator.setVisibility(0);
        } else {
            this.mFolderIndicator.setVisibility(8);
        }
    }

    public void setChildrenCount(int i) {
        this.mCountOrSize.setVisibility(8);
        checkSeparator();
    }

    public void setFileSize(long j) {
        if (j < 0) {
            this.mCountOrSize.setVisibility(8);
        } else {
            this.mCountOrSize.setVisibility(0);
            this.mCountOrSize.setText(FileUtils.toFileSizeString(this.mContext, j));
        }
        checkSeparator();
    }

    public void setLastModified(long j) {
        if (j <= 0) {
            this.mLastModifiedDate.setVisibility(8);
            checkSeparator();
            return;
        }
        this.mLastModifiedDate.setText(TimeUtil.formatDateWithYear(this.mContext, j));
        this.mLastModifiedDate.setVisibility(0);
        checkSeparator();
    }

    private void checkSeparator() {
        if (this.mLastModifiedDate.getVisibility() == 0 && this.mCountOrSize.getVisibility() == 0) {
            this.mSeparator.setVisibility(0);
        } else {
            this.mSeparator.setVisibility(8);
        }
    }

    public void setItemChecked(boolean z) {
        this.mChecked = z;
        if (this.mChecked) {
            this.mFileIndicator.setVisibility(0);
            this.mFileIndicator.setChecked(this.mChecked);
            return;
        }
        this.mFileIndicator.setVisibility(8);
    }
}
