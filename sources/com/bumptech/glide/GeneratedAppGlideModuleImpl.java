package com.bumptech.glide;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.zipow.videobox.ZMGlideAppModule;
import java.util.Collections;
import java.util.Set;

final class GeneratedAppGlideModuleImpl extends GeneratedAppGlideModule {
    private final ZMGlideAppModule appGlideModule = new ZMGlideAppModule();

    GeneratedAppGlideModuleImpl() {
        if (Log.isLoggable("Glide", 3)) {
            Log.d("Glide", "Discovered AppGlideModule from annotation: com.zipow.videobox.ZMGlideAppModule");
        }
    }

    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder glideBuilder) {
        this.appGlideModule.applyOptions(context, glideBuilder);
    }

    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        this.appGlideModule.registerComponents(context, glide, registry);
    }

    public boolean isManifestParsingEnabled() {
        return this.appGlideModule.isManifestParsingEnabled();
    }

    @NonNull
    public Set<Class<?>> getExcludedModuleClasses() {
        return Collections.emptySet();
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public GeneratedRequestManagerFactory getRequestManagerFactory() {
        return new GeneratedRequestManagerFactory();
    }
}
