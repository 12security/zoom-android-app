package com.zipow.videobox;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.zipow.videobox.util.zmurl.avatar.ZMAvatarLoader.Factory;
import com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl;
import java.io.InputStream;
import p021us.zoom.androidlib.util.FileUtils;

public class ZMGlideAppModule extends AppGlideModule {
    private static final String TAG = "ZMGlideAppModule";

    public boolean isManifestParsingEnabled() {
        return false;
    }

    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder glideBuilder) {
        super.applyOptions(context, glideBuilder);
        glideBuilder.setMemoryCache(new LruResourceCache((long) ((int) (((float) Runtime.getRuntime().maxMemory()) * 0.3f))));
        if (!Build.MANUFACTURER.equals("ViewSonic")) {
            glideBuilder.setDiskCache(new DiskLruCacheFactory(FileUtils.getTempPath(VideoBoxApplication.getInstance()), "imgcache", (long) 314572800));
        }
        glideBuilder.setLogLevel(6);
    }

    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.append(ZMAvatarUrl.class, InputStream.class, (ModelLoaderFactory<Model, Data>) new Factory<Model,Data>());
    }
}
