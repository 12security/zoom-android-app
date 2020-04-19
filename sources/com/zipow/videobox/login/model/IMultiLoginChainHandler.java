package com.zipow.videobox.login.model;

import android.content.Intent;
import com.zipow.videobox.thirdparty.AuthResult;

public interface IMultiLoginChainHandler {
    boolean onActivityResult(int i, int i2, Intent intent);

    boolean onAuthResult(AuthResult authResult);

    boolean onIMLogin(long j, int i);

    boolean onWebLogin(long j);
}
