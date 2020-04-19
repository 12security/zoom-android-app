package com.davemorrissey.labs.subscaleview.decoder;

import android.graphics.Bitmap.Config;
import androidx.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;

public class CompatDecoderFactory<T> implements DecoderFactory<T> {
    private final Config bitmapConfig;
    private final Class<? extends T> clazz;

    public CompatDecoderFactory(@NonNull Class<? extends T> cls) {
        this(cls, null);
    }

    public CompatDecoderFactory(@NonNull Class<? extends T> cls, Config config) {
        this.clazz = cls;
        this.bitmapConfig = config;
    }

    @NonNull
    public T make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (this.bitmapConfig == null) {
            return this.clazz.newInstance();
        }
        return this.clazz.getConstructor(new Class[]{Config.class}).newInstance(new Object[]{this.bitmapConfig});
    }
}
