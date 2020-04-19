package com.zipow.videobox.confapp.component;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.component.sink.common.IConfActivityLifeCycle;
import com.zipow.videobox.confapp.component.sink.common.IConfUISink;
import com.zipow.videobox.view.video.AbsVideoSceneMgr;

class ZMBaseConfComponentMgr implements IConfActivityLifeCycle, IConfUISink {
    @Nullable
    protected AbsVideoSceneMgr mAbsVideoSceneMgr;
    @Nullable
    protected ConfActivity mContext;
    @Nullable
    protected ZmConfAudioComponent mZmConfAudioComponent;
    @Nullable
    protected ZmConfShareComponent mZmConfShareComponent;
    @Nullable
    protected ZmConfVideoComponent mZmConfVideoComponent;

    public void onActivityDestroy() {
    }

    ZMBaseConfComponentMgr() {
    }

    public void registerAllComponents(@NonNull ConfActivity confActivity) {
        this.mContext = confActivity;
        this.mZmConfAudioComponent = new ZmConfAudioComponent(confActivity);
        this.mZmConfVideoComponent = new ZmConfVideoComponent(confActivity);
        this.mZmConfShareComponent = new ZmConfShareComponent(confActivity);
    }

    public void unRegisterAllComponents() {
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null) {
            zmConfShareComponent.onActivityDestroy();
            this.mZmConfShareComponent = null;
        }
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null) {
            zmConfVideoComponent.onActivityDestroy();
            this.mZmConfVideoComponent = null;
        }
        ZmConfAudioComponent zmConfAudioComponent = this.mZmConfAudioComponent;
        if (zmConfAudioComponent != null) {
            zmConfAudioComponent.onActivityDestroy();
            this.mZmConfAudioComponent = null;
        }
        this.mContext = null;
    }

    public void setVideoSceneMgr(@Nullable AbsVideoSceneMgr absVideoSceneMgr) {
        this.mAbsVideoSceneMgr = absVideoSceneMgr;
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null) {
            zmConfShareComponent.setAbsVideoSceneMgr(absVideoSceneMgr);
        }
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null) {
            zmConfVideoComponent.setAbsVideoSceneMgr(absVideoSceneMgr);
        }
        ZmConfAudioComponent zmConfAudioComponent = this.mZmConfAudioComponent;
        if (zmConfAudioComponent != null) {
            zmConfAudioComponent.setAbsVideoSceneMgr(absVideoSceneMgr);
        }
    }

    public boolean isMbEditStatus() {
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null) {
            return zmConfShareComponent.isMbEditStatus();
        }
        return false;
    }

    @Nullable
    public View getFocusView() {
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null) {
            return zmConfVideoComponent.getmVideoView();
        }
        return null;
    }

    public void onActivityCreate(Bundle bundle) {
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null) {
            zmConfShareComponent.onActivityCreate(bundle);
        }
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null) {
            zmConfVideoComponent.onActivityCreate(bundle);
        }
        ZmConfAudioComponent zmConfAudioComponent = this.mZmConfAudioComponent;
        if (zmConfAudioComponent != null) {
            zmConfAudioComponent.onActivityCreate(bundle);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null) {
            zmConfShareComponent.onSaveInstanceState(bundle);
        }
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null) {
            zmConfVideoComponent.onSaveInstanceState(bundle);
        }
        ZmConfAudioComponent zmConfAudioComponent = this.mZmConfAudioComponent;
        if (zmConfAudioComponent != null) {
            zmConfAudioComponent.onSaveInstanceState(bundle);
        }
    }

    public void onActivityPause() {
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null) {
            zmConfShareComponent.onActivityPause();
        }
    }

    public void onActivityResume() {
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null) {
            zmConfVideoComponent.onActivityResume();
        }
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null) {
            zmConfShareComponent.onActivityResume();
        }
        ZmConfAudioComponent zmConfAudioComponent = this.mZmConfAudioComponent;
        if (zmConfAudioComponent != null) {
            zmConfAudioComponent.onActivityResume();
        }
    }

    public boolean onActivityResult(int i, int i2, @Nullable Intent intent) {
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null && zmConfShareComponent.onActivityResult(i, i2, intent)) {
            return true;
        }
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null && zmConfVideoComponent.onActivityResult(i, i2, intent)) {
            return true;
        }
        ZmConfAudioComponent zmConfAudioComponent = this.mZmConfAudioComponent;
        if (zmConfAudioComponent == null || !zmConfAudioComponent.onActivityResult(i, i2, intent)) {
            return false;
        }
        return true;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null && zmConfShareComponent.onKeyDown(i, keyEvent)) {
            return true;
        }
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null && zmConfVideoComponent.onKeyDown(i, keyEvent)) {
            return true;
        }
        ZmConfAudioComponent zmConfAudioComponent = this.mZmConfAudioComponent;
        if (zmConfAudioComponent == null || !zmConfAudioComponent.onKeyDown(i, keyEvent)) {
            return false;
        }
        return true;
    }

    public boolean handleRequestPermissionResult(int i, @NonNull String str, int i2) {
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null && zmConfShareComponent.handleRequestPermissionResult(i, str, i2)) {
            return true;
        }
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null && zmConfVideoComponent.handleRequestPermissionResult(i, str, i2)) {
            return true;
        }
        ZmConfAudioComponent zmConfAudioComponent = this.mZmConfAudioComponent;
        if (zmConfAudioComponent == null || !zmConfAudioComponent.handleRequestPermissionResult(i, str, i2)) {
            return false;
        }
        return true;
    }

    public void onConfReady() {
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null) {
            zmConfVideoComponent.onConfReady();
        }
    }

    public void onModeViewChanged(ZMConfEnumViewMode zMConfEnumViewMode) {
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null) {
            zmConfShareComponent.onModeViewChanged(zMConfEnumViewMode);
        }
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null) {
            zmConfVideoComponent.onModeViewChanged(zMConfEnumViewMode);
        }
        ZmConfAudioComponent zmConfAudioComponent = this.mZmConfAudioComponent;
        if (zmConfAudioComponent != null) {
            zmConfAudioComponent.onModeViewChanged(zMConfEnumViewMode);
        }
    }

    public boolean dispatchModeViewSwitch() {
        ZmConfShareComponent zmConfShareComponent = this.mZmConfShareComponent;
        if (zmConfShareComponent != null && zmConfShareComponent.dispatchModeViewSwitch()) {
            return true;
        }
        ZmConfVideoComponent zmConfVideoComponent = this.mZmConfVideoComponent;
        if (zmConfVideoComponent != null && zmConfVideoComponent.dispatchModeViewSwitch()) {
            return true;
        }
        ZmConfAudioComponent zmConfAudioComponent = this.mZmConfAudioComponent;
        if (zmConfAudioComponent == null || !zmConfAudioComponent.dispatchModeViewSwitch()) {
            return false;
        }
        return true;
    }
}
