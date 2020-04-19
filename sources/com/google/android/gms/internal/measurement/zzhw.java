package com.google.android.gms.internal.measurement;

import com.box.androidsdk.content.models.BoxUser;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.util.logging.Level;
import java.util.logging.Logger;
import libcore.io.Memory;
import sun.misc.Unsafe;

final class zzhw {
    private static final Logger logger = Logger.getLogger(zzhw.class.getName());
    private static final Class<?> zzabq = zzdk.zzkc();
    private static final boolean zzacv = zzox();
    private static final Unsafe zzaiz = zzow();
    private static final boolean zzakw = zzk(Long.TYPE);
    private static final boolean zzakx = zzk(Integer.TYPE);
    private static final zzd zzaky;
    private static final boolean zzakz = zzoy();
    /* access modifiers changed from: private */
    public static final long zzala = ((long) zzi(byte[].class));
    private static final long zzalb = ((long) zzi(boolean[].class));
    private static final long zzalc = ((long) zzj(boolean[].class));
    private static final long zzald = ((long) zzi(int[].class));
    private static final long zzale = ((long) zzj(int[].class));
    private static final long zzalf = ((long) zzi(long[].class));
    private static final long zzalg = ((long) zzj(long[].class));
    private static final long zzalh = ((long) zzi(float[].class));
    private static final long zzali = ((long) zzj(float[].class));
    private static final long zzalj = ((long) zzi(double[].class));
    private static final long zzalk = ((long) zzj(double[].class));
    private static final long zzall = ((long) zzi(Object[].class));
    private static final long zzalm = ((long) zzj(Object[].class));
    private static final long zzaln;
    /* access modifiers changed from: private */
    public static final boolean zzalo = (ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN);

    static final class zza extends zzd {
        zza(Unsafe unsafe) {
            super(unsafe);
        }

        public final void zza(long j, byte b) {
            Memory.pokeByte((int) (j & -1), b);
        }

        public final byte zzy(Object obj, long j) {
            if (zzhw.zzalo) {
                return zzhw.zzq(obj, j);
            }
            return zzhw.zzr(obj, j);
        }

        public final void zze(Object obj, long j, byte b) {
            if (zzhw.zzalo) {
                zzhw.zza(obj, j, b);
            } else {
                zzhw.zzb(obj, j, b);
            }
        }

        public final boolean zzm(Object obj, long j) {
            if (zzhw.zzalo) {
                return zzhw.zzs(obj, j);
            }
            return zzhw.zzt(obj, j);
        }

        public final void zza(Object obj, long j, boolean z) {
            if (zzhw.zzalo) {
                zzhw.zzb(obj, j, z);
            } else {
                zzhw.zzc(obj, j, z);
            }
        }

        public final float zzn(Object obj, long j) {
            return Float.intBitsToFloat(zzk(obj, j));
        }

        public final void zza(Object obj, long j, float f) {
            zzb(obj, j, Float.floatToIntBits(f));
        }

        public final double zzo(Object obj, long j) {
            return Double.longBitsToDouble(zzl(obj, j));
        }

        public final void zza(Object obj, long j, double d) {
            zza(obj, j, Double.doubleToLongBits(d));
        }

        public final void zza(byte[] bArr, long j, long j2, long j3) {
            Memory.pokeByteArray((int) (j2 & -1), bArr, (int) j, (int) j3);
        }
    }

    static final class zzb extends zzd {
        zzb(Unsafe unsafe) {
            super(unsafe);
        }

        public final void zza(long j, byte b) {
            Memory.pokeByte(j, b);
        }

        public final byte zzy(Object obj, long j) {
            if (zzhw.zzalo) {
                return zzhw.zzq(obj, j);
            }
            return zzhw.zzr(obj, j);
        }

        public final void zze(Object obj, long j, byte b) {
            if (zzhw.zzalo) {
                zzhw.zza(obj, j, b);
            } else {
                zzhw.zzb(obj, j, b);
            }
        }

        public final boolean zzm(Object obj, long j) {
            if (zzhw.zzalo) {
                return zzhw.zzs(obj, j);
            }
            return zzhw.zzt(obj, j);
        }

        public final void zza(Object obj, long j, boolean z) {
            if (zzhw.zzalo) {
                zzhw.zzb(obj, j, z);
            } else {
                zzhw.zzc(obj, j, z);
            }
        }

        public final float zzn(Object obj, long j) {
            return Float.intBitsToFloat(zzk(obj, j));
        }

        public final void zza(Object obj, long j, float f) {
            zzb(obj, j, Float.floatToIntBits(f));
        }

        public final double zzo(Object obj, long j) {
            return Double.longBitsToDouble(zzl(obj, j));
        }

        public final void zza(Object obj, long j, double d) {
            zza(obj, j, Double.doubleToLongBits(d));
        }

        public final void zza(byte[] bArr, long j, long j2, long j3) {
            Memory.pokeByteArray(j2, bArr, (int) j, (int) j3);
        }
    }

    static final class zzc extends zzd {
        zzc(Unsafe unsafe) {
            super(unsafe);
        }

        public final void zza(long j, byte b) {
            this.zzalp.putByte(j, b);
        }

        public final byte zzy(Object obj, long j) {
            return this.zzalp.getByte(obj, j);
        }

        public final void zze(Object obj, long j, byte b) {
            this.zzalp.putByte(obj, j, b);
        }

        public final boolean zzm(Object obj, long j) {
            return this.zzalp.getBoolean(obj, j);
        }

        public final void zza(Object obj, long j, boolean z) {
            this.zzalp.putBoolean(obj, j, z);
        }

        public final float zzn(Object obj, long j) {
            return this.zzalp.getFloat(obj, j);
        }

        public final void zza(Object obj, long j, float f) {
            this.zzalp.putFloat(obj, j, f);
        }

        public final double zzo(Object obj, long j) {
            return this.zzalp.getDouble(obj, j);
        }

        public final void zza(Object obj, long j, double d) {
            this.zzalp.putDouble(obj, j, d);
        }

        public final void zza(byte[] bArr, long j, long j2, long j3) {
            this.zzalp.copyMemory(bArr, zzhw.zzala + j, null, j2, j3);
        }
    }

    static abstract class zzd {
        Unsafe zzalp;

        zzd(Unsafe unsafe) {
            this.zzalp = unsafe;
        }

        public abstract void zza(long j, byte b);

        public abstract void zza(Object obj, long j, double d);

        public abstract void zza(Object obj, long j, float f);

        public abstract void zza(Object obj, long j, boolean z);

        public abstract void zza(byte[] bArr, long j, long j2, long j3);

        public abstract void zze(Object obj, long j, byte b);

        public abstract boolean zzm(Object obj, long j);

        public abstract float zzn(Object obj, long j);

        public abstract double zzo(Object obj, long j);

        public abstract byte zzy(Object obj, long j);

        public final int zzk(Object obj, long j) {
            return this.zzalp.getInt(obj, j);
        }

        public final void zzb(Object obj, long j, int i) {
            this.zzalp.putInt(obj, j, i);
        }

        public final long zzl(Object obj, long j) {
            return this.zzalp.getLong(obj, j);
        }

        public final void zza(Object obj, long j, long j2) {
            this.zzalp.putLong(obj, j, j2);
        }
    }

    private zzhw() {
    }

    static boolean zzou() {
        return zzacv;
    }

    static boolean zzov() {
        return zzakz;
    }

    static <T> T zzh(Class<T> cls) {
        try {
            return zzaiz.allocateInstance(cls);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static int zzi(Class<?> cls) {
        if (zzacv) {
            return zzaky.zzalp.arrayBaseOffset(cls);
        }
        return -1;
    }

    private static int zzj(Class<?> cls) {
        if (zzacv) {
            return zzaky.zzalp.arrayIndexScale(cls);
        }
        return -1;
    }

    static int zzk(Object obj, long j) {
        return zzaky.zzk(obj, j);
    }

    static void zzb(Object obj, long j, int i) {
        zzaky.zzb(obj, j, i);
    }

    static long zzl(Object obj, long j) {
        return zzaky.zzl(obj, j);
    }

    static void zza(Object obj, long j, long j2) {
        zzaky.zza(obj, j, j2);
    }

    static boolean zzm(Object obj, long j) {
        return zzaky.zzm(obj, j);
    }

    static void zza(Object obj, long j, boolean z) {
        zzaky.zza(obj, j, z);
    }

    static float zzn(Object obj, long j) {
        return zzaky.zzn(obj, j);
    }

    static void zza(Object obj, long j, float f) {
        zzaky.zza(obj, j, f);
    }

    static double zzo(Object obj, long j) {
        return zzaky.zzo(obj, j);
    }

    static void zza(Object obj, long j, double d) {
        zzaky.zza(obj, j, d);
    }

    static Object zzp(Object obj, long j) {
        return zzaky.zzalp.getObject(obj, j);
    }

    static void zza(Object obj, long j, Object obj2) {
        zzaky.zzalp.putObject(obj, j, obj2);
    }

    static byte zza(byte[] bArr, long j) {
        return zzaky.zzy(bArr, zzala + j);
    }

    static void zza(byte[] bArr, long j, byte b) {
        zzaky.zze(bArr, zzala + j, b);
    }

    static void zza(byte[] bArr, long j, long j2, long j3) {
        zzaky.zza(bArr, j, j2, j3);
    }

    static void zza(long j, byte b) {
        zzaky.zza(j, b);
    }

    static long zzb(ByteBuffer byteBuffer) {
        return zzaky.zzl(byteBuffer, zzaln);
    }

    static Unsafe zzow() {
        try {
            return (Unsafe) AccessController.doPrivileged(new zzhx());
        } catch (Throwable unused) {
            return null;
        }
    }

    private static boolean zzox() {
        Unsafe unsafe = zzaiz;
        if (unsafe == null) {
            return false;
        }
        try {
            Class cls = unsafe.getClass();
            cls.getMethod("objectFieldOffset", new Class[]{Field.class});
            cls.getMethod("arrayBaseOffset", new Class[]{Class.class});
            cls.getMethod("arrayIndexScale", new Class[]{Class.class});
            cls.getMethod("getInt", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putInt", new Class[]{Object.class, Long.TYPE, Integer.TYPE});
            cls.getMethod("getLong", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putLong", new Class[]{Object.class, Long.TYPE, Long.TYPE});
            cls.getMethod("getObject", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putObject", new Class[]{Object.class, Long.TYPE, Object.class});
            if (zzdk.zzkb()) {
                return true;
            }
            cls.getMethod("getByte", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putByte", new Class[]{Object.class, Long.TYPE, Byte.TYPE});
            cls.getMethod("getBoolean", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putBoolean", new Class[]{Object.class, Long.TYPE, Boolean.TYPE});
            cls.getMethod("getFloat", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putFloat", new Class[]{Object.class, Long.TYPE, Float.TYPE});
            cls.getMethod("getDouble", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putDouble", new Class[]{Object.class, Long.TYPE, Double.TYPE});
            return true;
        } catch (Throwable th) {
            String valueOf = String.valueOf(th);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 71);
            sb.append("platform method missing - proto runtime falling back to safer methods: ");
            sb.append(valueOf);
            logger.logp(Level.WARNING, "com.google.protobuf.UnsafeUtil", "supportsUnsafeArrayOperations", sb.toString());
            return false;
        }
    }

    private static boolean zzoy() {
        Unsafe unsafe = zzaiz;
        if (unsafe == null) {
            return false;
        }
        try {
            Class cls = unsafe.getClass();
            cls.getMethod("objectFieldOffset", new Class[]{Field.class});
            cls.getMethod("getLong", new Class[]{Object.class, Long.TYPE});
            if (zzoz() == null) {
                return false;
            }
            if (zzdk.zzkb()) {
                return true;
            }
            cls.getMethod("getByte", new Class[]{Long.TYPE});
            cls.getMethod("putByte", new Class[]{Long.TYPE, Byte.TYPE});
            cls.getMethod("getInt", new Class[]{Long.TYPE});
            cls.getMethod("putInt", new Class[]{Long.TYPE, Integer.TYPE});
            cls.getMethod("getLong", new Class[]{Long.TYPE});
            cls.getMethod("putLong", new Class[]{Long.TYPE, Long.TYPE});
            cls.getMethod("copyMemory", new Class[]{Long.TYPE, Long.TYPE, Long.TYPE});
            cls.getMethod("copyMemory", new Class[]{Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE});
            return true;
        } catch (Throwable th) {
            String valueOf = String.valueOf(th);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 71);
            sb.append("platform method missing - proto runtime falling back to safer methods: ");
            sb.append(valueOf);
            logger.logp(Level.WARNING, "com.google.protobuf.UnsafeUtil", "supportsUnsafeByteBufferOperations", sb.toString());
            return false;
        }
    }

    private static boolean zzk(Class<?> cls) {
        if (!zzdk.zzkb()) {
            return false;
        }
        try {
            Class<?> cls2 = zzabq;
            cls2.getMethod("peekLong", new Class[]{cls, Boolean.TYPE});
            cls2.getMethod("pokeLong", new Class[]{cls, Long.TYPE, Boolean.TYPE});
            cls2.getMethod("pokeInt", new Class[]{cls, Integer.TYPE, Boolean.TYPE});
            cls2.getMethod("peekInt", new Class[]{cls, Boolean.TYPE});
            cls2.getMethod("pokeByte", new Class[]{cls, Byte.TYPE});
            cls2.getMethod("peekByte", new Class[]{cls});
            cls2.getMethod("pokeByteArray", new Class[]{cls, byte[].class, Integer.TYPE, Integer.TYPE});
            cls2.getMethod("peekByteArray", new Class[]{cls, byte[].class, Integer.TYPE, Integer.TYPE});
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static Field zzoz() {
        if (zzdk.zzkb()) {
            Field zzb2 = zzb(Buffer.class, "effectiveDirectAddress");
            if (zzb2 != null) {
                return zzb2;
            }
        }
        Field zzb3 = zzb(Buffer.class, BoxUser.FIELD_ADDRESS);
        if (zzb3 == null || zzb3.getType() != Long.TYPE) {
            return null;
        }
        return zzb3;
    }

    private static Field zzb(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static byte zzq(Object obj, long j) {
        return (byte) (zzk(obj, -4 & j) >>> ((int) (((~j) & 3) << 3)));
    }

    /* access modifiers changed from: private */
    public static byte zzr(Object obj, long j) {
        return (byte) (zzk(obj, -4 & j) >>> ((int) ((j & 3) << 3)));
    }

    /* access modifiers changed from: private */
    public static void zza(Object obj, long j, byte b) {
        long j2 = -4 & j;
        int i = ((~((int) j)) & 3) << 3;
        int i2 = (255 & b) << i;
        zzb(obj, j2, i2 | (zzk(obj, j2) & (~(255 << i))));
    }

    /* access modifiers changed from: private */
    public static void zzb(Object obj, long j, byte b) {
        long j2 = -4 & j;
        int i = (((int) j) & 3) << 3;
        zzb(obj, j2, ((255 & b) << i) | (zzk(obj, j2) & (~(255 << i))));
    }

    /* access modifiers changed from: private */
    public static boolean zzs(Object obj, long j) {
        return zzq(obj, j) != 0;
    }

    /* access modifiers changed from: private */
    public static boolean zzt(Object obj, long j) {
        return zzr(obj, j) != 0;
    }

    /* access modifiers changed from: private */
    public static void zzb(Object obj, long j, boolean z) {
        zza(obj, j, z ? (byte) 1 : 0);
    }

    /* access modifiers changed from: private */
    public static void zzc(Object obj, long j, boolean z) {
        zzb(obj, j, z ? (byte) 1 : 0);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x00f4  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00f6  */
    static {
        /*
            java.lang.Class<com.google.android.gms.internal.measurement.zzhw> r0 = com.google.android.gms.internal.measurement.zzhw.class
            java.lang.String r0 = r0.getName()
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)
            logger = r0
            sun.misc.Unsafe r0 = zzow()
            zzaiz = r0
            java.lang.Class r0 = com.google.android.gms.internal.measurement.zzdk.zzkc()
            zzabq = r0
            java.lang.Class r0 = java.lang.Long.TYPE
            boolean r0 = zzk(r0)
            zzakw = r0
            java.lang.Class r0 = java.lang.Integer.TYPE
            boolean r0 = zzk(r0)
            zzakx = r0
            sun.misc.Unsafe r0 = zzaiz
            r1 = 0
            if (r0 != 0) goto L_0x002e
            goto L_0x0053
        L_0x002e:
            boolean r0 = com.google.android.gms.internal.measurement.zzdk.zzkb()
            if (r0 == 0) goto L_0x004c
            boolean r0 = zzakw
            if (r0 == 0) goto L_0x0040
            com.google.android.gms.internal.measurement.zzhw$zzb r1 = new com.google.android.gms.internal.measurement.zzhw$zzb
            sun.misc.Unsafe r0 = zzaiz
            r1.<init>(r0)
            goto L_0x0053
        L_0x0040:
            boolean r0 = zzakx
            if (r0 == 0) goto L_0x0053
            com.google.android.gms.internal.measurement.zzhw$zza r1 = new com.google.android.gms.internal.measurement.zzhw$zza
            sun.misc.Unsafe r0 = zzaiz
            r1.<init>(r0)
            goto L_0x0053
        L_0x004c:
            com.google.android.gms.internal.measurement.zzhw$zzc r1 = new com.google.android.gms.internal.measurement.zzhw$zzc
            sun.misc.Unsafe r0 = zzaiz
            r1.<init>(r0)
        L_0x0053:
            zzaky = r1
            boolean r0 = zzoy()
            zzakz = r0
            boolean r0 = zzox()
            zzacv = r0
            java.lang.Class<byte[]> r0 = byte[].class
            int r0 = zzi(r0)
            long r0 = (long) r0
            zzala = r0
            java.lang.Class<boolean[]> r0 = boolean[].class
            int r0 = zzi(r0)
            long r0 = (long) r0
            zzalb = r0
            java.lang.Class<boolean[]> r0 = boolean[].class
            int r0 = zzj(r0)
            long r0 = (long) r0
            zzalc = r0
            java.lang.Class<int[]> r0 = int[].class
            int r0 = zzi(r0)
            long r0 = (long) r0
            zzald = r0
            java.lang.Class<int[]> r0 = int[].class
            int r0 = zzj(r0)
            long r0 = (long) r0
            zzale = r0
            java.lang.Class<long[]> r0 = long[].class
            int r0 = zzi(r0)
            long r0 = (long) r0
            zzalf = r0
            java.lang.Class<long[]> r0 = long[].class
            int r0 = zzj(r0)
            long r0 = (long) r0
            zzalg = r0
            java.lang.Class<float[]> r0 = float[].class
            int r0 = zzi(r0)
            long r0 = (long) r0
            zzalh = r0
            java.lang.Class<float[]> r0 = float[].class
            int r0 = zzj(r0)
            long r0 = (long) r0
            zzali = r0
            java.lang.Class<double[]> r0 = double[].class
            int r0 = zzi(r0)
            long r0 = (long) r0
            zzalj = r0
            java.lang.Class<double[]> r0 = double[].class
            int r0 = zzj(r0)
            long r0 = (long) r0
            zzalk = r0
            java.lang.Class<java.lang.Object[]> r0 = java.lang.Object[].class
            int r0 = zzi(r0)
            long r0 = (long) r0
            zzall = r0
            java.lang.Class<java.lang.Object[]> r0 = java.lang.Object[].class
            int r0 = zzj(r0)
            long r0 = (long) r0
            zzalm = r0
            java.lang.reflect.Field r0 = zzoz()
            if (r0 == 0) goto L_0x00e8
            com.google.android.gms.internal.measurement.zzhw$zzd r1 = zzaky
            if (r1 != 0) goto L_0x00e1
            goto L_0x00e8
        L_0x00e1:
            sun.misc.Unsafe r1 = r1.zzalp
            long r0 = r1.objectFieldOffset(r0)
            goto L_0x00ea
        L_0x00e8:
            r0 = -1
        L_0x00ea:
            zzaln = r0
            java.nio.ByteOrder r0 = java.nio.ByteOrder.nativeOrder()
            java.nio.ByteOrder r1 = java.nio.ByteOrder.BIG_ENDIAN
            if (r0 != r1) goto L_0x00f6
            r0 = 1
            goto L_0x00f7
        L_0x00f6:
            r0 = 0
        L_0x00f7:
            zzalo = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzhw.<clinit>():void");
    }
}
