package com.zipow.videobox.confapp.component.sink.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IConfActivityLifeCycle {
    void onActivityCreate(Bundle bundle);

    void onActivityDestroy();

    void onActivityPause();

    boolean onActivityResult(int i, int i2, @Nullable Intent intent);

    void onActivityResume();

    boolean onKeyDown(int i, KeyEvent keyEvent);

    void onSaveInstanceState(@NonNull Bundle bundle);
}
