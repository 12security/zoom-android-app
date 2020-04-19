package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzem;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class zzex<T extends zzem> {
    private static final Logger logger = Logger.getLogger(zzeg.class.getName());
    private static String zzagl = "com.google.protobuf.BlazeGeneratedExtensionRegistryLiteLoader";

    zzex() {
    }

    /* access modifiers changed from: protected */
    public abstract T zzme();

    static <T extends zzem> T zza(Class<T> cls) {
        String str;
        ClassLoader classLoader = zzex.class.getClassLoader();
        if (cls.equals(zzem.class)) {
            str = zzagl;
        } else if (cls.getPackage().equals(zzex.class.getPackage())) {
            str = String.format("%s.BlazeGenerated%sLoader", new Object[]{cls.getPackage().getName(), cls.getSimpleName()});
        } else {
            throw new IllegalArgumentException(cls.getName());
        }
        try {
            return (zzem) cls.cast(((zzex) Class.forName(str, true, classLoader).getConstructor(new Class[0]).newInstance(new Object[0])).zzme());
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        } catch (InstantiationException e2) {
            throw new IllegalStateException(e2);
        } catch (IllegalAccessException e3) {
            throw new IllegalStateException(e3);
        } catch (InvocationTargetException e4) {
            throw new IllegalStateException(e4);
        } catch (ClassNotFoundException unused) {
            Iterator it = ServiceLoader.load(zzex.class, classLoader).iterator();
            ArrayList arrayList = new ArrayList();
            while (it.hasNext()) {
                try {
                    arrayList.add((zzem) cls.cast(((zzex) it.next()).zzme()));
                } catch (ServiceConfigurationError e5) {
                    ServiceConfigurationError serviceConfigurationError = e5;
                    Logger logger2 = logger;
                    Level level = Level.SEVERE;
                    String str2 = "com.google.protobuf.GeneratedExtensionRegistryLoader";
                    String str3 = "load";
                    String str4 = "Unable to load ";
                    String valueOf = String.valueOf(cls.getSimpleName());
                    logger2.logp(level, str2, str3, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4), serviceConfigurationError);
                }
            }
            if (arrayList.size() == 1) {
                return (zzem) arrayList.get(0);
            }
            if (arrayList.size() == 0) {
                return null;
            }
            try {
                return (zzem) cls.getMethod("combine", new Class[]{Collection.class}).invoke(null, new Object[]{arrayList});
            } catch (NoSuchMethodException e6) {
                throw new IllegalStateException(e6);
            } catch (IllegalAccessException e7) {
                throw new IllegalStateException(e7);
            } catch (InvocationTargetException e8) {
                throw new IllegalStateException(e8);
            }
        }
    }
}
