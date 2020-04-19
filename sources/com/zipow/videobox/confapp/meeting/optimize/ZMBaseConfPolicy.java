package com.zipow.videobox.confapp.meeting.optimize;

import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import java.util.Collection;
import java.util.Iterator;

public abstract class ZMBaseConfPolicy {
    private static final int MSG_CHECK_IDLE_MESSAGES = 1000;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what != 1000) {
                super.handleMessage(message);
                return;
            }
            ZMBaseConfPolicy.this.onIdle();
            ZMBaseConfPolicy.this.mHandler.sendEmptyMessageDelayed(1000, ZMBaseConfPolicy.this.mIntervalIdle);
        }
    };
    protected long mIntervalIdle = 200;

    public abstract void onIdle();

    public void start() {
        this.mHandler.removeMessages(1000);
        this.mHandler.sendEmptyMessage(1000);
    }

    public void end() {
        this.mHandler.removeCallbacksAndMessages(null);
    }

    @Nullable
    public ZMConfUserActionInfo getMySelfConfUserActionInfo(@NonNull Collection<ZMConfUserActionInfo> collection) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        ZMConfUserActionInfo zMConfUserActionInfo = null;
        if (confStatusObj == null) {
            return null;
        }
        Iterator it = collection.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ZMConfUserActionInfo zMConfUserActionInfo2 = (ZMConfUserActionInfo) it.next();
            if (confStatusObj.isMyself(zMConfUserActionInfo2.getUserId())) {
                zMConfUserActionInfo = zMConfUserActionInfo2;
                break;
            }
        }
        return zMConfUserActionInfo;
    }
}
