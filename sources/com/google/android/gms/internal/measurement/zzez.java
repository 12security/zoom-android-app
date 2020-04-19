package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzez;
import com.google.android.gms.internal.measurement.zzez.zza;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class zzez<MessageType extends zzez<MessageType, BuilderType>, BuilderType extends zza<MessageType, BuilderType>> extends zzdg<MessageType, BuilderType> {
    private static Map<Object, zzez<?, ?>> zzagp = new ConcurrentHashMap();
    protected zzhr zzagn = zzhr.zzor();
    private int zzago = -1;

    public static abstract class zza<MessageType extends zzez<MessageType, BuilderType>, BuilderType extends zza<MessageType, BuilderType>> extends zzdh<MessageType, BuilderType> {
        private final MessageType zzagq;
        protected MessageType zzagr;
        private boolean zzags = false;

        protected zza(MessageType messagetype) {
            this.zzagq = messagetype;
            this.zzagr = (zzez) messagetype.zza(zze.zzagx, (Object) null, (Object) null);
        }

        /* access modifiers changed from: protected */
        public final void zzmn() {
            if (this.zzags) {
                MessageType messagetype = (zzez) this.zzagr.zza(zze.zzagx, (Object) null, (Object) null);
                zza(messagetype, this.zzagr);
                this.zzagr = messagetype;
                this.zzags = false;
            }
        }

        public final boolean isInitialized() {
            return zzez.zza(this.zzagr, false);
        }

        /* renamed from: zzmo */
        public MessageType zzmq() {
            if (this.zzags) {
                return this.zzagr;
            }
            this.zzagr.zzjz();
            this.zzags = true;
            return this.zzagr;
        }

        /* renamed from: zzmp */
        public final MessageType zzmr() {
            MessageType messagetype = (zzez) zzmq();
            if (messagetype.isInitialized()) {
                return messagetype;
            }
            throw new zzhp(messagetype);
        }

        public final BuilderType zza(MessageType messagetype) {
            zzmn();
            zza(this.zzagr, messagetype);
            return this;
        }

        private static void zza(MessageType messagetype, MessageType messagetype2) {
            zzgu.zznz().zzv(messagetype).zzc(messagetype, messagetype2);
        }

        public final /* synthetic */ zzdh zzjx() {
            return (zza) clone();
        }

        public final /* synthetic */ zzgh zzmm() {
            return this.zzagq;
        }

        public /* synthetic */ Object clone() throws CloneNotSupportedException {
            zza zza = (zza) ((zzez) this.zzagq).zza(zze.zzagy, (Object) null, (Object) null);
            zza.zza((MessageType) (zzez) zzmq());
            return zza;
        }
    }

    public static class zzb<T extends zzez<T, ?>> extends zzdi<T> {
        private final T zzagq;

        public zzb(T t) {
            this.zzagq = t;
        }

        public final /* synthetic */ Object zza(zzeb zzeb, zzem zzem) throws zzfh {
            return zzez.zza(this.zzagq, zzeb, zzem);
        }
    }

    public static abstract class zzc<MessageType extends zzc<MessageType, BuilderType>, BuilderType> extends zzez<MessageType, BuilderType> implements zzgj {
        protected zzeq<Object> zzagt = zzeq.zzlx();

        /* access modifiers changed from: 0000 */
        public final zzeq<Object> zzms() {
            if (this.zzagt.isImmutable()) {
                this.zzagt = (zzeq) this.zzagt.clone();
            }
            return this.zzagt;
        }
    }

    public static class zzd<ContainingType extends zzgh, Type> extends zzek<ContainingType, Type> {
    }

    /* 'enum' access flag removed */
    public static final class zze {
        public static final int zzagu = 1;
        public static final int zzagv = 2;
        public static final int zzagw = 3;
        public static final int zzagx = 4;
        public static final int zzagy = 5;
        public static final int zzagz = 6;
        public static final int zzaha = 7;
        private static final /* synthetic */ int[] zzahb = {zzagu, zzagv, zzagw, zzagx, zzagy, zzagz, zzaha};
        public static final int zzahc = 1;
        public static final int zzahd = 2;
        private static final /* synthetic */ int[] zzahe = {zzahc, zzahd};
        public static final int zzahf = 1;
        public static final int zzahg = 2;
        private static final /* synthetic */ int[] zzahh = {zzahf, zzahg};

        public static int[] zzmt() {
            return (int[]) zzahb.clone();
        }
    }

    /* access modifiers changed from: protected */
    public abstract Object zza(int i, Object obj, Object obj2);

    public String toString() {
        return zzgk.zza(this, super.toString());
    }

    public int hashCode() {
        if (this.zzabm != 0) {
            return this.zzabm;
        }
        this.zzabm = zzgu.zznz().zzv(this).hashCode(this);
        return this.zzabm;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!((zzez) zza(zze.zzagz, (Object) null, (Object) null)).getClass().isInstance(obj)) {
            return false;
        }
        return zzgu.zznz().zzv(this).equals(this, (zzez) obj);
    }

    /* access modifiers changed from: protected */
    public final void zzjz() {
        zzgu.zznz().zzv(this).zzi(this);
    }

    /* access modifiers changed from: protected */
    public final <MessageType extends zzez<MessageType, BuilderType>, BuilderType extends zza<MessageType, BuilderType>> BuilderType zzmg() {
        return (zza) zza(zze.zzagy, (Object) null, (Object) null);
    }

    public final boolean isInitialized() {
        return zza((T) this, Boolean.TRUE.booleanValue());
    }

    public final BuilderType zzmh() {
        BuilderType buildertype = (zza) zza(zze.zzagy, (Object) null, (Object) null);
        buildertype.zza(this);
        return buildertype;
    }

    /* access modifiers changed from: 0000 */
    public final int zzjw() {
        return this.zzago;
    }

    /* access modifiers changed from: 0000 */
    public final void zzn(int i) {
        this.zzago = i;
    }

    public final void zzb(zzeg zzeg) throws IOException {
        zzgu.zznz().zzf(getClass()).zza(this, zzei.zza(zzeg));
    }

    public final int zzly() {
        if (this.zzago == -1) {
            this.zzago = zzgu.zznz().zzv(this).zzs(this);
        }
        return this.zzago;
    }

    static <T extends zzez<?, ?>> T zzd(Class<T> cls) {
        T t = (zzez) zzagp.get(cls);
        if (t == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                t = (zzez) zzagp.get(cls);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class initialization cannot fail.", e);
            }
        }
        if (t == null) {
            t = (zzez) ((zzez) zzhw.zzh(cls)).zza(zze.zzagz, (Object) null, (Object) null);
            if (t != null) {
                zzagp.put(cls, t);
            } else {
                throw new IllegalStateException();
            }
        }
        return t;
    }

    protected static <T extends zzez<?, ?>> void zza(Class<T> cls, T t) {
        zzagp.put(cls, t);
    }

    protected static Object zza(zzgh zzgh, String str, Object[] objArr) {
        return new zzgw(zzgh, str, objArr);
    }

    static Object zza(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else if (cause instanceof Error) {
                throw ((Error) cause);
            } else {
                throw new RuntimeException("Unexpected exception thrown by generated accessor method.", cause);
            }
        }
    }

    protected static final <T extends zzez<T, ?>> boolean zza(T t, boolean z) {
        byte byteValue = ((Byte) t.zza(zze.zzagu, (Object) null, (Object) null)).byteValue();
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        boolean zzu = zzgu.zznz().zzv(t).zzu(t);
        if (z) {
            t.zza(zze.zzagv, (Object) zzu ? t : null, (Object) null);
        }
        return zzu;
    }

    protected static zzff zzmi() {
        return zzfv.zznk();
    }

    protected static zzff zza(zzff zzff) {
        int size = zzff.size();
        return zzff.zzav(size == 0 ? 10 : size << 1);
    }

    protected static <E> zzfg<E> zzmj() {
        return zzgv.zzoa();
    }

    protected static <E> zzfg<E> zza(zzfg<E> zzfg) {
        int size = zzfg.size();
        return zzfg.zzq(size == 0 ? 10 : size << 1);
    }

    static <T extends zzez<T, ?>> T zza(T t, zzeb zzeb, zzem zzem) throws zzfh {
        T t2 = (zzez) t.zza(zze.zzagx, (Object) null, (Object) null);
        try {
            zzgu.zznz().zzv(t2).zza(t2, zzee.zza(zzeb), zzem);
            t2.zzjz();
            return t2;
        } catch (IOException e) {
            if (e.getCause() instanceof zzfh) {
                throw ((zzfh) e.getCause());
            }
            throw new zzfh(e.getMessage()).zzg(t2);
        } catch (RuntimeException e2) {
            if (e2.getCause() instanceof zzfh) {
                throw ((zzfh) e2.getCause());
            }
            throw e2;
        }
    }

    private static <T extends zzez<T, ?>> T zza(T t, byte[] bArr, int i, int i2, zzem zzem) throws zzfh {
        T t2 = (zzez) t.zza(zze.zzagx, (Object) null, (Object) null);
        try {
            zzgu.zznz().zzv(t2).zza(t2, bArr, 0, i2, new zzdm(zzem));
            t2.zzjz();
            if (t2.zzabm == 0) {
                return t2;
            }
            throw new RuntimeException();
        } catch (IOException e) {
            if (e.getCause() instanceof zzfh) {
                throw ((zzfh) e.getCause());
            }
            throw new zzfh(e.getMessage()).zzg(t2);
        } catch (IndexOutOfBoundsException unused) {
            throw zzfh.zzmu().zzg(t2);
        }
    }

    protected static <T extends zzez<T, ?>> T zza(T t, byte[] bArr, zzem zzem) throws zzfh {
        T zza2 = zza(t, bArr, 0, bArr.length, zzem);
        if (zza2 == null || zza2.isInitialized()) {
            return zza2;
        }
        throw new zzfh(new zzhp(zza2).getMessage()).zzg(zza2);
    }

    public final /* synthetic */ zzgi zzmk() {
        zza zza2 = (zza) zza(zze.zzagy, (Object) null, (Object) null);
        zza2.zza(this);
        return zza2;
    }

    public final /* synthetic */ zzgi zzml() {
        return (zza) zza(zze.zzagy, (Object) null, (Object) null);
    }

    public final /* synthetic */ zzgh zzmm() {
        return (zzez) zza(zze.zzagz, (Object) null, (Object) null);
    }
}
