package com.zipow.videobox.util.zmurl.avatar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;

public class ZMAvatarLoader implements ModelLoader<ZMAvatarUrl, InputStream> {
    @Nullable
    private final ModelCache<ZMAvatarUrl, ZMAvatarUrl> modelCache;

    public static class Factory implements ModelLoaderFactory<ZMAvatarUrl, InputStream> {
        private final ModelCache<ZMAvatarUrl, ZMAvatarUrl> modelCache = new ModelCache<>(500);

        public void teardown() {
        }

        @NonNull
        public ModelLoader<ZMAvatarUrl, InputStream> build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ZMAvatarLoader(this.modelCache);
        }
    }

    public boolean handles(@NonNull ZMAvatarUrl zMAvatarUrl) {
        return true;
    }

    public ZMAvatarLoader() {
        this(null);
    }

    public ZMAvatarLoader(@Nullable ModelCache<ZMAvatarUrl, ZMAvatarUrl> modelCache2) {
        this.modelCache = modelCache2;
    }

    public LoadData<InputStream> buildLoadData(@NonNull ZMAvatarUrl zMAvatarUrl, int i, int i2, @NonNull Options options) {
        ModelCache<ZMAvatarUrl, ZMAvatarUrl> modelCache2 = this.modelCache;
        if (modelCache2 != null) {
            ZMAvatarUrl zMAvatarUrl2 = (ZMAvatarUrl) modelCache2.get(zMAvatarUrl, i, i2);
            if (zMAvatarUrl2 == null) {
                this.modelCache.put(zMAvatarUrl, i, i2, zMAvatarUrl);
            } else {
                zMAvatarUrl = zMAvatarUrl2;
            }
        }
        return new LoadData<>(zMAvatarUrl, new ZMAvatarUrlFetcher(zMAvatarUrl, i, i2));
    }
}
