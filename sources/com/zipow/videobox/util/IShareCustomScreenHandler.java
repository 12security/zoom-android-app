package com.zipow.videobox.util;

import android.content.Context;
import androidx.annotation.Nullable;

public interface IShareCustomScreenHandler {
    @Nullable
    String getShareCustomScreenName(Context context);

    void onStartedShareCustomScreen(Context context);
}
