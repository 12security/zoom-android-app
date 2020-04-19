package com.zipow.videobox.confapp.component;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.component.sink.audio.IAudioSink;
import com.zipow.videobox.view.video.AbsVideoSceneMgr;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;

public class ZMAudioConfComponentMgr extends ZMBaseConfComponentMgr implements IAudioSink {
    public /* bridge */ /* synthetic */ boolean dispatchModeViewSwitch() {
        return super.dispatchModeViewSwitch();
    }

    @Nullable
    public /* bridge */ /* synthetic */ View getFocusView() {
        return super.getFocusView();
    }

    public /* bridge */ /* synthetic */ boolean handleRequestPermissionResult(int i, @NonNull String str, int i2) {
        return super.handleRequestPermissionResult(i, str, i2);
    }

    public /* bridge */ /* synthetic */ boolean isMbEditStatus() {
        return super.isMbEditStatus();
    }

    public /* bridge */ /* synthetic */ void onActivityCreate(Bundle bundle) {
        super.onActivityCreate(bundle);
    }

    public /* bridge */ /* synthetic */ void onActivityDestroy() {
        super.onActivityDestroy();
    }

    public /* bridge */ /* synthetic */ void onActivityPause() {
        super.onActivityPause();
    }

    public /* bridge */ /* synthetic */ boolean onActivityResult(int i, int i2, @Nullable Intent intent) {
        return super.onActivityResult(i, i2, intent);
    }

    public /* bridge */ /* synthetic */ void onActivityResume() {
        super.onActivityResume();
    }

    public /* bridge */ /* synthetic */ void onConfReady() {
        super.onConfReady();
    }

    public /* bridge */ /* synthetic */ boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }

    public /* bridge */ /* synthetic */ void onModeViewChanged(ZMConfEnumViewMode zMConfEnumViewMode) {
        super.onModeViewChanged(zMConfEnumViewMode);
    }

    public /* bridge */ /* synthetic */ void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public /* bridge */ /* synthetic */ void registerAllComponents(@NonNull ConfActivity confActivity) {
        super.registerAllComponents(confActivity);
    }

    public /* bridge */ /* synthetic */ void setVideoSceneMgr(@Nullable AbsVideoSceneMgr absVideoSceneMgr) {
        super.setVideoSceneMgr(absVideoSceneMgr);
    }

    public /* bridge */ /* synthetic */ void unRegisterAllComponents() {
        super.unRegisterAllComponents();
    }

    public void sinkConfKmsKeyNotReady() {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkConfKmsKeyNotReady") {
                public void run(@NonNull IUIElement iUIElement) {
                    if (ZMAudioConfComponentMgr.this.mZmConfAudioComponent != null) {
                        ZMAudioConfComponentMgr.this.mZmConfAudioComponent.sinkConfKmsKeyNotReady();
                    }
                }
            });
        }
    }

    public void sinkPreemptionAudio(final int i) {
        if (this.mContext != null && i != 2) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkPreemptionAudio") {
                public void run(@NonNull IUIElement iUIElement) {
                    if (ZMAudioConfComponentMgr.this.mZmConfAudioComponent != null) {
                        ZMAudioConfComponentMgr.this.mZmConfAudioComponent.sinkPreemptionAudio(i);
                    }
                }
            });
        }
    }
}
