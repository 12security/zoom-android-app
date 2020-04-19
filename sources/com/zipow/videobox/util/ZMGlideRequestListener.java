package com.zipow.videobox.util;

public interface ZMGlideRequestListener {
    void onError(String str, GifException gifException);

    void onSuccess(String str);
}
