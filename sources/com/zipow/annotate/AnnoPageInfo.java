package com.zipow.annotate;

public class AnnoPageInfo {
    public int mCurrentPageNum;
    public int mPageId;
    public String mSavePath;
    public int mTotalPageNum;
    public boolean mbSaveSnapahot = false;

    public AnnoPageInfo(int i, int i2) {
        this.mCurrentPageNum = i;
        this.mTotalPageNum = i2;
    }
}
