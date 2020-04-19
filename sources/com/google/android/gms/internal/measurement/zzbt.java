package com.google.android.gms.internal.measurement;

import java.util.List;

public final class zzbt {

    public static final class zza extends zzez<zza, C4567zza> implements zzgj {
        private static volatile zzgs<zza> zztq;
        /* access modifiers changed from: private */
        public static final zza zzut = new zza();
        private int zztj;
        private int zzup;
        private zzf zzuq;
        private zzf zzur;
        private boolean zzus;

        /* renamed from: com.google.android.gms.internal.measurement.zzbt$zza$zza reason: collision with other inner class name */
        public static final class C4567zza extends com.google.android.gms.internal.measurement.zzez.zza<zza, C4567zza> implements zzgj {
            private C4567zza() {
                super(zza.zzut);
            }

            public final C4567zza zzi(int i) {
                zzmn();
                ((zza) this.zzagr).zzh(i);
                return this;
            }

            public final zzf zzgw() {
                return ((zza) this.zzagr).zzgw();
            }

            public final C4567zza zzb(zza zza) {
                zzmn();
                ((zza) this.zzagr).zza(zza);
                return this;
            }

            public final boolean zzgx() {
                return ((zza) this.zzagr).zzgx();
            }

            public final zzf zzgy() {
                return ((zza) this.zzagr).zzgy();
            }

            public final C4567zza zzb(zzf zzf) {
                zzmn();
                ((zza) this.zzagr).zza(zzf);
                return this;
            }

            public final C4567zza zzl(boolean z) {
                zzmn();
                ((zza) this.zzagr).zzk(z);
                return this;
            }

            /* synthetic */ C4567zza(zzbu zzbu) {
                this();
            }
        }

        private zza() {
        }

        public final boolean zzgu() {
            return (this.zztj & 1) != 0;
        }

        public final int zzgv() {
            return this.zzup;
        }

        /* access modifiers changed from: private */
        public final void zzh(int i) {
            this.zztj |= 1;
            this.zzup = i;
        }

        public final zzf zzgw() {
            zzf zzf = this.zzuq;
            return zzf == null ? zzf.zzij() : zzf;
        }

        /* access modifiers changed from: private */
        public final void zza(zza zza) {
            this.zzuq = (zzf) ((zzez) zza.zzmr());
            this.zztj |= 2;
        }

        public final boolean zzgx() {
            return (this.zztj & 4) != 0;
        }

        public final zzf zzgy() {
            zzf zzf = this.zzur;
            return zzf == null ? zzf.zzij() : zzf;
        }

        /* access modifiers changed from: private */
        public final void zza(zzf zzf) {
            if (zzf != null) {
                this.zzur = zzf;
                this.zztj |= 4;
                return;
            }
            throw new NullPointerException();
        }

        public final boolean zzgz() {
            return (this.zztj & 8) != 0;
        }

        public final boolean zzha() {
            return this.zzus;
        }

        /* access modifiers changed from: private */
        public final void zzk(boolean z) {
            this.zztj |= 8;
            this.zzus = z;
        }

        public static C4567zza zzhb() {
            return (C4567zza) zzut.zzmg();
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbu.zzti[i - 1]) {
                case 1:
                    return new zza();
                case 2:
                    return new C4567zza(null);
                case 3:
                    Object[] objArr = {"zztj", "zzup", "zzuq", "zzur", "zzus"};
                    return zza((zzgh) zzut, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001\u0004\u0000\u0002\t\u0001\u0003\t\u0002\u0004\u0007\u0003", objArr);
                case 4:
                    return zzut;
                case 5:
                    zzgs<zza> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zza.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new com.google.android.gms.internal.measurement.zzez.zzb<>(zzut);
                                zztq = zzgs;
                            }
                        }
                    }
                    return zzgs;
                case 6:
                    return Byte.valueOf(1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public static zzgs<zza> zzgs() {
            return (zzgs) zzut.zza(com.google.android.gms.internal.measurement.zzez.zze.zzaha, (Object) null, (Object) null);
        }

        static {
            zzez.zza(zza.class, zzut);
        }
    }

    public static final class zzb extends zzez<zzb, zza> implements zzgj {
        private static volatile zzgs<zzb> zztq;
        /* access modifiers changed from: private */
        public static final zzb zzuw = new zzb();
        private int zztj;
        private int zzuu;
        private long zzuv;

        public static final class zza extends com.google.android.gms.internal.measurement.zzez.zza<zzb, zza> implements zzgj {
            private zza() {
                super(zzb.zzuw);
            }

            public final zza zzj(int i) {
                zzmn();
                ((zzb) this.zzagr).setIndex(i);
                return this;
            }

            public final zza zzag(long j) {
                zzmn();
                ((zzb) this.zzagr).zzaf(j);
                return this;
            }

            /* synthetic */ zza(zzbu zzbu) {
                this();
            }
        }

        private zzb() {
        }

        public final boolean zzhd() {
            return (this.zztj & 1) != 0;
        }

        public final int getIndex() {
            return this.zzuu;
        }

        /* access modifiers changed from: private */
        public final void setIndex(int i) {
            this.zztj |= 1;
            this.zzuu = i;
        }

        public final boolean zzhe() {
            return (this.zztj & 2) != 0;
        }

        public final long zzhf() {
            return this.zzuv;
        }

        /* access modifiers changed from: private */
        public final void zzaf(long j) {
            this.zztj |= 2;
            this.zzuv = j;
        }

        public static zza zzhg() {
            return (zza) zzuw.zzmg();
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbu.zzti[i - 1]) {
                case 1:
                    return new zzb();
                case 2:
                    return new zza(null);
                case 3:
                    Object[] objArr = {"zztj", "zzuu", "zzuv"};
                    return zza((zzgh) zzuw, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0004\u0000\u0002\u0002\u0001", objArr);
                case 4:
                    return zzuw;
                case 5:
                    zzgs<zzb> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zzb.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new com.google.android.gms.internal.measurement.zzez.zzb<>(zzuw);
                                zztq = zzgs;
                            }
                        }
                    }
                    return zzgs;
                case 6:
                    return Byte.valueOf(1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        static {
            zzez.zza(zzb.class, zzuw);
        }
    }

    public static final class zzc extends zzez<zzc, zza> implements zzgj {
        private static volatile zzgs<zzc> zztq;
        /* access modifiers changed from: private */
        public static final zzc zzuz = new zzc();
        private int zztj;
        private String zzux = "";
        private long zzuy;

        public static final class zza extends com.google.android.gms.internal.measurement.zzez.zza<zzc, zza> implements zzgj {
            private zza() {
                super(zzc.zzuz);
            }

            public final zza zzbu(String str) {
                zzmn();
                ((zzc) this.zzagr).setName(str);
                return this;
            }

            public final zza zzai(long j) {
                zzmn();
                ((zzc) this.zzagr).zzah(j);
                return this;
            }

            /* synthetic */ zza(zzbu zzbu) {
                this();
            }
        }

        private zzc() {
        }

        /* access modifiers changed from: private */
        public final void setName(String str) {
            if (str != null) {
                this.zztj |= 1;
                this.zzux = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public final void zzah(long j) {
            this.zztj |= 2;
            this.zzuy = j;
        }

        public static zza zzhi() {
            return (zza) zzuz.zzmg();
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbu.zzti[i - 1]) {
                case 1:
                    return new zzc();
                case 2:
                    return new zza(null);
                case 3:
                    Object[] objArr = {"zztj", "zzux", "zzuy"};
                    return zza((zzgh) zzuz, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001\b\u0000\u0002\u0002\u0001", objArr);
                case 4:
                    return zzuz;
                case 5:
                    zzgs<zzc> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zzc.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new com.google.android.gms.internal.measurement.zzez.zzb<>(zzuz);
                                zztq = zzgs;
                            }
                        }
                    }
                    return zzgs;
                case 6:
                    return Byte.valueOf(1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        static {
            zzez.zza(zzc.class, zzuz);
        }
    }

    public static final class zzd extends zzez<zzd, zza> implements zzgj {
        private static volatile zzgs<zzd> zztq;
        /* access modifiers changed from: private */
        public static final zzd zzvd = new zzd();
        private int zztj;
        private String zzux = "";
        private long zzuy;
        private String zzva = "";
        private float zzvb;
        private double zzvc;

        public static final class zza extends com.google.android.gms.internal.measurement.zzez.zza<zzd, zza> implements zzgj {
            private zza() {
                super(zzd.zzvd);
            }

            public final String getName() {
                return ((zzd) this.zzagr).getName();
            }

            public final zza zzbw(String str) {
                zzmn();
                ((zzd) this.zzagr).setName(str);
                return this;
            }

            public final zza zzbx(String str) {
                zzmn();
                ((zzd) this.zzagr).zzbv(str);
                return this;
            }

            public final zza zzhv() {
                zzmn();
                ((zzd) this.zzagr).zzhm();
                return this;
            }

            public final zza zzaj(long j) {
                zzmn();
                ((zzd) this.zzagr).zzah(j);
                return this;
            }

            public final zza zzhw() {
                zzmn();
                ((zzd) this.zzagr).zzhp();
                return this;
            }

            public final zza zzb(double d) {
                zzmn();
                ((zzd) this.zzagr).zza(d);
                return this;
            }

            public final zza zzhx() {
                zzmn();
                ((zzd) this.zzagr).zzhs();
                return this;
            }

            /* synthetic */ zza(zzbu zzbu) {
                this();
            }
        }

        private zzd() {
        }

        public final String getName() {
            return this.zzux;
        }

        /* access modifiers changed from: private */
        public final void setName(String str) {
            if (str != null) {
                this.zztj |= 1;
                this.zzux = str;
                return;
            }
            throw new NullPointerException();
        }

        public final boolean zzhk() {
            return (this.zztj & 2) != 0;
        }

        public final String zzhl() {
            return this.zzva;
        }

        /* access modifiers changed from: private */
        public final void zzbv(String str) {
            if (str != null) {
                this.zztj |= 2;
                this.zzva = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public final void zzhm() {
            this.zztj &= -3;
            this.zzva = zzvd.zzva;
        }

        public final boolean zzhn() {
            return (this.zztj & 4) != 0;
        }

        public final long zzho() {
            return this.zzuy;
        }

        /* access modifiers changed from: private */
        public final void zzah(long j) {
            this.zztj |= 4;
            this.zzuy = j;
        }

        /* access modifiers changed from: private */
        public final void zzhp() {
            this.zztj &= -5;
            this.zzuy = 0;
        }

        public final boolean zzhq() {
            return (this.zztj & 16) != 0;
        }

        public final double zzhr() {
            return this.zzvc;
        }

        /* access modifiers changed from: private */
        public final void zza(double d) {
            this.zztj |= 16;
            this.zzvc = d;
        }

        /* access modifiers changed from: private */
        public final void zzhs() {
            this.zztj &= -17;
            this.zzvc = 0.0d;
        }

        public static zza zzht() {
            return (zza) zzvd.zzmg();
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbu.zzti[i - 1]) {
                case 1:
                    return new zzd();
                case 2:
                    return new zza(null);
                case 3:
                    Object[] objArr = {"zztj", "zzux", "zzva", "zzuy", "zzvb", "zzvc"};
                    return zza((zzgh) zzvd, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0000\u0000\u0001\b\u0000\u0002\b\u0001\u0003\u0002\u0002\u0004\u0001\u0003\u0005\u0000\u0004", objArr);
                case 4:
                    return zzvd;
                case 5:
                    zzgs<zzd> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zzd.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new com.google.android.gms.internal.measurement.zzez.zzb<>(zzvd);
                                zztq = zzgs;
                            }
                        }
                    }
                    return zzgs;
                case 6:
                    return Byte.valueOf(1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public static zzgs<zzd> zzgs() {
            return (zzgs) zzvd.zza(com.google.android.gms.internal.measurement.zzez.zze.zzaha, (Object) null, (Object) null);
        }

        static {
            zzez.zza(zzd.class, zzvd);
        }
    }

    public static final class zze extends zzez<zze, zza> implements zzgj {
        private static volatile zzgs<zze> zztq;
        /* access modifiers changed from: private */
        public static final zze zzvg = new zze();
        private int zztj;
        private int zzve = 1;
        private zzfg<zzc> zzvf = zzmj();

        public static final class zza extends com.google.android.gms.internal.measurement.zzez.zza<zze, zza> implements zzgj {
            private zza() {
                super(zze.zzvg);
            }

            public final zza zzb(zza zza) {
                zzmn();
                ((zze) this.zzagr).zza(zza);
                return this;
            }

            /* synthetic */ zza(zzbu zzbu) {
                this();
            }
        }

        public enum zzb implements zzfc {
            RADS(1),
            PROVISIONING(2);
            
            private static final zzfd<zzb> zztw = null;
            private final int value;

            public final int zzgp() {
                return this.value;
            }

            public static zzb zzk(int i) {
                switch (i) {
                    case 1:
                        return RADS;
                    case 2:
                        return PROVISIONING;
                    default:
                        return null;
                }
            }

            public static zzfe zzgq() {
                return zzbw.zzty;
            }

            private zzb(int i) {
                this.value = i;
            }

            static {
                zztw = new zzbv();
            }
        }

        private zze() {
        }

        /* access modifiers changed from: private */
        public final void zza(zza zza2) {
            if (!this.zzvf.zzjy()) {
                this.zzvf = zzez.zza(this.zzvf);
            }
            this.zzvf.add((zzc) ((zzez) zza2.zzmr()));
        }

        public static zza zzhy() {
            return (zza) zzvg.zzmg();
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbu.zzti[i - 1]) {
                case 1:
                    return new zze();
                case 2:
                    return new zza(null);
                case 3:
                    Object[] objArr = {"zztj", "zzve", zzb.zzgq(), "zzvf", zzc.class};
                    return zza((zzgh) zzvg, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001\f\u0000\u0002\u001b", objArr);
                case 4:
                    return zzvg;
                case 5:
                    zzgs<zze> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zze.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new com.google.android.gms.internal.measurement.zzez.zzb<>(zzvg);
                                zztq = zzgs;
                            }
                        }
                    }
                    return zzgs;
                case 6:
                    return Byte.valueOf(1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public static zzgs<zze> zzgs() {
            return (zzgs) zzvg.zza(com.google.android.gms.internal.measurement.zzez.zze.zzaha, (Object) null, (Object) null);
        }

        static {
            zzez.zza(zze.class, zzvg);
        }
    }

    public static final class zzf extends zzez<zzf, zza> implements zzgj {
        private static volatile zzgs<zzf> zztq;
        /* access modifiers changed from: private */
        public static final zzf zzvo = new zzf();
        private zzff zzvk = zzmi();
        private zzff zzvl = zzmi();
        private zzfg<zzb> zzvm = zzmj();
        private zzfg<zzg> zzvn = zzmj();

        public static final class zza extends com.google.android.gms.internal.measurement.zzez.zza<zzf, zza> implements zzgj {
            private zza() {
                super(zzf.zzvo);
            }

            public final zza zze(Iterable<? extends Long> iterable) {
                zzmn();
                ((zzf) this.zzagr).zza(iterable);
                return this;
            }

            public final zza zzf(Iterable<? extends Long> iterable) {
                zzmn();
                ((zzf) this.zzagr).zzb(iterable);
                return this;
            }

            public final zza zzg(Iterable<? extends zzb> iterable) {
                zzmn();
                ((zzf) this.zzagr).zzc(iterable);
                return this;
            }

            public final zza zzh(Iterable<? extends zzg> iterable) {
                zzmn();
                ((zzf) this.zzagr).zzd(iterable);
                return this;
            }

            /* synthetic */ zza(zzbu zzbu) {
                this();
            }
        }

        private zzf() {
        }

        public final List<Long> zzia() {
            return this.zzvk;
        }

        public final int zzib() {
            return this.zzvk.size();
        }

        /* access modifiers changed from: private */
        public final void zza(Iterable<? extends Long> iterable) {
            if (!this.zzvk.zzjy()) {
                this.zzvk = zzez.zza(this.zzvk);
            }
            zzdg.zza(iterable, this.zzvk);
        }

        public final List<Long> zzic() {
            return this.zzvl;
        }

        public final int zzid() {
            return this.zzvl.size();
        }

        /* access modifiers changed from: private */
        public final void zzb(Iterable<? extends Long> iterable) {
            if (!this.zzvl.zzjy()) {
                this.zzvl = zzez.zza(this.zzvl);
            }
            zzdg.zza(iterable, this.zzvl);
        }

        public final List<zzb> zzie() {
            return this.zzvm;
        }

        public final int zzif() {
            return this.zzvm.size();
        }

        /* access modifiers changed from: private */
        public final void zzc(Iterable<? extends zzb> iterable) {
            if (!this.zzvm.zzjy()) {
                this.zzvm = zzez.zza(this.zzvm);
            }
            zzdg.zza(iterable, this.zzvm);
        }

        public final List<zzg> zzig() {
            return this.zzvn;
        }

        public final int zzih() {
            return this.zzvn.size();
        }

        /* access modifiers changed from: private */
        public final void zzd(Iterable<? extends zzg> iterable) {
            if (!this.zzvn.zzjy()) {
                this.zzvn = zzez.zza(this.zzvn);
            }
            zzdg.zza(iterable, this.zzvn);
        }

        public static zzf zza(byte[] bArr, zzem zzem) throws zzfh {
            return (zzf) zzez.zza(zzvo, bArr, zzem);
        }

        public static zza zzii() {
            return (zza) zzvo.zzmg();
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbu.zzti[i - 1]) {
                case 1:
                    return new zzf();
                case 2:
                    return new zza(null);
                case 3:
                    Object[] objArr = {"zzvk", "zzvl", "zzvm", zzb.class, "zzvn", zzg.class};
                    return zza((zzgh) zzvo, "\u0001\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0004\u0000\u0001\u0015\u0002\u0015\u0003\u001b\u0004\u001b", objArr);
                case 4:
                    return zzvo;
                case 5:
                    zzgs<zzf> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zzf.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new com.google.android.gms.internal.measurement.zzez.zzb<>(zzvo);
                                zztq = zzgs;
                            }
                        }
                    }
                    return zzgs;
                case 6:
                    return Byte.valueOf(1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public static zzf zzij() {
            return zzvo;
        }

        static {
            zzez.zza(zzf.class, zzvo);
        }
    }

    public static final class zzg extends zzez<zzg, zza> implements zzgj {
        private static volatile zzgs<zzg> zztq;
        /* access modifiers changed from: private */
        public static final zzg zzvq = new zzg();
        private int zztj;
        private int zzuu;
        private zzff zzvp = zzmi();

        public static final class zza extends com.google.android.gms.internal.measurement.zzez.zza<zzg, zza> implements zzgj {
            private zza() {
                super(zzg.zzvq);
            }

            public final zza zzm(int i) {
                zzmn();
                ((zzg) this.zzagr).setIndex(i);
                return this;
            }

            public final zza zzal(long j) {
                zzmn();
                ((zzg) this.zzagr).zzak(j);
                return this;
            }

            public final zza zzj(Iterable<? extends Long> iterable) {
                zzmn();
                ((zzg) this.zzagr).zzi(iterable);
                return this;
            }

            public final zza zzir() {
                zzmn();
                ((zzg) this.zzagr).zzio();
                return this;
            }

            /* synthetic */ zza(zzbu zzbu) {
                this();
            }
        }

        private zzg() {
        }

        public final boolean zzhd() {
            return (this.zztj & 1) != 0;
        }

        public final int getIndex() {
            return this.zzuu;
        }

        /* access modifiers changed from: private */
        public final void setIndex(int i) {
            this.zztj |= 1;
            this.zzuu = i;
        }

        public final List<Long> zzil() {
            return this.zzvp;
        }

        public final int zzim() {
            return this.zzvp.size();
        }

        public final long zzl(int i) {
            return this.zzvp.getLong(i);
        }

        private final void zzin() {
            if (!this.zzvp.zzjy()) {
                this.zzvp = zzez.zza(this.zzvp);
            }
        }

        /* access modifiers changed from: private */
        public final void zzak(long j) {
            zzin();
            this.zzvp.zzbb(j);
        }

        /* access modifiers changed from: private */
        public final void zzi(Iterable<? extends Long> iterable) {
            zzin();
            zzdg.zza(iterable, this.zzvp);
        }

        /* access modifiers changed from: private */
        public final void zzio() {
            this.zzvp = zzmi();
        }

        public static zza zzip() {
            return (zza) zzvq.zzmg();
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbu.zzti[i - 1]) {
                case 1:
                    return new zzg();
                case 2:
                    return new zza(null);
                case 3:
                    Object[] objArr = {"zztj", "zzuu", "zzvp"};
                    return zza((zzgh) zzvq, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001\u0004\u0000\u0002\u0014", objArr);
                case 4:
                    return zzvq;
                case 5:
                    zzgs<zzg> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zzg.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new com.google.android.gms.internal.measurement.zzez.zzb<>(zzvq);
                                zztq = zzgs;
                            }
                        }
                    }
                    return zzgs;
                case 6:
                    return Byte.valueOf(1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        static {
            zzez.zza(zzg.class, zzvq);
        }
    }

    public static final class zzh extends zzez<zzh, zza> implements zzgj {
        private static volatile zzgs<zzh> zztq;
        /* access modifiers changed from: private */
        public static final zzh zzvs = new zzh();
        private int zztj;
        private String zzux = "";
        private long zzuy;
        private String zzva = "";
        private float zzvb;
        private double zzvc;
        private long zzvr;

        public static final class zza extends com.google.android.gms.internal.measurement.zzez.zza<zzh, zza> implements zzgj {
            private zza() {
                super(zzh.zzvs);
            }

            public final zza zzan(long j) {
                zzmn();
                ((zzh) this.zzagr).zzam(j);
                return this;
            }

            public final zza zzby(String str) {
                zzmn();
                ((zzh) this.zzagr).setName(str);
                return this;
            }

            public final zza zzbz(String str) {
                zzmn();
                ((zzh) this.zzagr).zzbv(str);
                return this;
            }

            public final zza zziw() {
                zzmn();
                ((zzh) this.zzagr).zzhm();
                return this;
            }

            public final zza zzao(long j) {
                zzmn();
                ((zzh) this.zzagr).zzah(j);
                return this;
            }

            public final zza zzix() {
                zzmn();
                ((zzh) this.zzagr).zzhp();
                return this;
            }

            public final zza zzc(double d) {
                zzmn();
                ((zzh) this.zzagr).zza(d);
                return this;
            }

            public final zza zziy() {
                zzmn();
                ((zzh) this.zzagr).zzhs();
                return this;
            }

            /* synthetic */ zza(zzbu zzbu) {
                this();
            }
        }

        private zzh() {
        }

        public final boolean zzis() {
            return (this.zztj & 1) != 0;
        }

        public final long zzit() {
            return this.zzvr;
        }

        /* access modifiers changed from: private */
        public final void zzam(long j) {
            this.zztj |= 1;
            this.zzvr = j;
        }

        public final String getName() {
            return this.zzux;
        }

        /* access modifiers changed from: private */
        public final void setName(String str) {
            if (str != null) {
                this.zztj |= 2;
                this.zzux = str;
                return;
            }
            throw new NullPointerException();
        }

        public final boolean zzhk() {
            return (this.zztj & 4) != 0;
        }

        public final String zzhl() {
            return this.zzva;
        }

        /* access modifiers changed from: private */
        public final void zzbv(String str) {
            if (str != null) {
                this.zztj |= 4;
                this.zzva = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public final void zzhm() {
            this.zztj &= -5;
            this.zzva = zzvs.zzva;
        }

        public final boolean zzhn() {
            return (this.zztj & 8) != 0;
        }

        public final long zzho() {
            return this.zzuy;
        }

        /* access modifiers changed from: private */
        public final void zzah(long j) {
            this.zztj |= 8;
            this.zzuy = j;
        }

        /* access modifiers changed from: private */
        public final void zzhp() {
            this.zztj &= -9;
            this.zzuy = 0;
        }

        public final boolean zzhq() {
            return (this.zztj & 32) != 0;
        }

        public final double zzhr() {
            return this.zzvc;
        }

        /* access modifiers changed from: private */
        public final void zza(double d) {
            this.zztj |= 32;
            this.zzvc = d;
        }

        /* access modifiers changed from: private */
        public final void zzhs() {
            this.zztj &= -33;
            this.zzvc = 0.0d;
        }

        public static zza zziu() {
            return (zza) zzvs.zzmg();
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbu.zzti[i - 1]) {
                case 1:
                    return new zzh();
                case 2:
                    return new zza(null);
                case 3:
                    Object[] objArr = {"zztj", "zzvr", "zzux", "zzva", "zzuy", "zzvb", "zzvc"};
                    return zza((zzgh) zzvs, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001\u0002\u0000\u0002\b\u0001\u0003\b\u0002\u0004\u0002\u0003\u0005\u0001\u0004\u0006\u0000\u0005", objArr);
                case 4:
                    return zzvs;
                case 5:
                    zzgs<zzh> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zzh.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new com.google.android.gms.internal.measurement.zzez.zzb<>(zzvs);
                                zztq = zzgs;
                            }
                        }
                    }
                    return zzgs;
                case 6:
                    return Byte.valueOf(1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public static zzgs<zzh> zzgs() {
            return (zzgs) zzvs.zza(com.google.android.gms.internal.measurement.zzez.zze.zzaha, (Object) null, (Object) null);
        }

        static {
            zzez.zza(zzh.class, zzvs);
        }
    }
}
