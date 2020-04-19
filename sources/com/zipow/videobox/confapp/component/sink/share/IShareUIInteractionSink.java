package com.zipow.videobox.confapp.component.sink.share;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.meeting.confhelper.ShareOptionType;
import com.zipow.videobox.view.video.RCMouseView;
import com.zipow.videobox.view.video.ShareVideoScene;

public interface IShareUIInteractionSink {
    @Nullable
    RCMouseView getRCMouseView();

    @Nullable
    ShareVideoScene getShareVideoScene();

    boolean isAnnotationDrawingViewVisible();

    void onLayoutChange(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    void onSwitchToOrOutShare(boolean z);

    void onToolbarVisibilityChanged(boolean z);

    void refreshAudioSharing(boolean z);

    void selectShareType(@NonNull ShareOptionType shareOptionType);

    void setPaddingForTranslucentStatus(int i, int i2, int i3, int i4);

    void shareByPathExtension(@Nullable String str);

    void showAnnotateViewWhenSceneChanged();

    void showShareChoice();

    void showShareTip();

    void startShareImage(Uri uri, boolean z);

    void startSharePDF(Uri uri, boolean z);

    void startShareScreen(@Nullable Intent intent);

    void startShareWebview(@Nullable String str);

    void stopShare();

    void switchToSmallShare();
}
