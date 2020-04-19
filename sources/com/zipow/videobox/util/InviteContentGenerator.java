package com.zipow.videobox.util;

import android.content.Context;
import androidx.annotation.Nullable;

public interface InviteContentGenerator {
    @Nullable
    String genCopyUrlText(Context context, long j, String str, String str2, String str3, String str4);

    @Nullable
    String genEmailContent(Context context, long j, String str, String str2, String str3, String str4);

    @Nullable
    String genEmailTopic(Context context, long j, String str, String str2, String str3, String str4);

    @Nullable
    String genSmsContent(Context context, long j, String str, String str2, String str3, String str4);
}
