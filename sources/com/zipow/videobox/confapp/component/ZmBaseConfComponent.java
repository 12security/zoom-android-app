package com.zipow.videobox.confapp.component;

import android.content.Intent;
import android.view.KeyEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.component.sink.common.IConfActivityLifeCycle;
import com.zipow.videobox.confapp.component.sink.common.IConfUISink;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.video.AbsVideoSceneMgr;

public abstract class ZmBaseConfComponent implements IConfActivityLifeCycle, IConfUISink {
    @Nullable
    protected AbsVideoSceneMgr mAbsVideoSceneMgr;
    @Nullable
    protected ConfActivity mContext;

    public boolean handleRequestPermissionResult(int i, @NonNull String str, int i2) {
        return false;
    }

    public boolean onActivityResult(int i, int i2, @Nullable Intent intent) {
        return false;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return false;
    }

    public ZmBaseConfComponent(@NonNull ConfActivity confActivity) {
        this.mContext = confActivity;
    }

    public void setAbsVideoSceneMgr(@Nullable AbsVideoSceneMgr absVideoSceneMgr) {
        this.mAbsVideoSceneMgr = absVideoSceneMgr;
    }

    /* access modifiers changed from: protected */
    public boolean isInShareVideoScene() {
        AbsVideoSceneMgr absVideoSceneMgr = this.mAbsVideoSceneMgr;
        return absVideoSceneMgr != null && absVideoSceneMgr.isInShareVideoScene();
    }

    /* access modifiers changed from: protected */
    public boolean isInNormalVideoScene() {
        AbsVideoSceneMgr absVideoSceneMgr = this.mAbsVideoSceneMgr;
        return absVideoSceneMgr != null && absVideoSceneMgr.isInNormalVideoScene();
    }

    public void onActivityDestroy() {
        this.mContext = null;
    }

    public boolean dispatchModeViewSwitch() {
        if (this.mContext == null) {
            return false;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && confContext.inSilentMode()) {
            this.mContext.switchViewTo(ZMConfEnumViewMode.SILENT_VIEW);
            return true;
        } else if (!ConfLocalHelper.isDirectShareClient()) {
            return false;
        } else {
            this.mContext.switchViewTo(ZMConfEnumViewMode.PRESENT_ROOM_LAYER);
            return true;
        }
    }
}
