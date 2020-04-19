package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.List;

final class zzee implements zzgx {
    private int tag;
    private final zzeb zzacr;
    private int zzacs;
    private int zzact = 0;

    public static zzee zza(zzeb zzeb) {
        if (zzeb.zzack != null) {
            return zzeb.zzack;
        }
        return new zzee(zzeb);
    }

    private zzee(zzeb zzeb) {
        this.zzacr = (zzeb) zzfb.zza(zzeb, "input");
        this.zzacr.zzack = this;
    }

    public final int zzlh() throws IOException {
        int i = this.zzact;
        if (i != 0) {
            this.tag = i;
            this.zzact = 0;
        } else {
            this.tag = this.zzacr.zzkj();
        }
        int i2 = this.tag;
        if (i2 == 0 || i2 == this.zzacs) {
            return Integer.MAX_VALUE;
        }
        return i2 >>> 3;
    }

    public final int getTag() {
        return this.tag;
    }

    public final boolean zzli() throws IOException {
        if (!this.zzacr.zzkz()) {
            int i = this.tag;
            if (i != this.zzacs) {
                return this.zzacr.zzv(i);
            }
        }
        return false;
    }

    private final void zzab(int i) throws IOException {
        if ((this.tag & 7) != i) {
            throw zzfh.zzmz();
        }
    }

    public final double readDouble() throws IOException {
        zzab(1);
        return this.zzacr.readDouble();
    }

    public final float readFloat() throws IOException {
        zzab(5);
        return this.zzacr.readFloat();
    }

    public final long zzkk() throws IOException {
        zzab(0);
        return this.zzacr.zzkk();
    }

    public final long zzkl() throws IOException {
        zzab(0);
        return this.zzacr.zzkl();
    }

    public final int zzkm() throws IOException {
        zzab(0);
        return this.zzacr.zzkm();
    }

    public final long zzkn() throws IOException {
        zzab(1);
        return this.zzacr.zzkn();
    }

    public final int zzko() throws IOException {
        zzab(5);
        return this.zzacr.zzko();
    }

    public final boolean zzkp() throws IOException {
        zzab(0);
        return this.zzacr.zzkp();
    }

    public final String readString() throws IOException {
        zzab(2);
        return this.zzacr.readString();
    }

    public final String zzkq() throws IOException {
        zzab(2);
        return this.zzacr.zzkq();
    }

    public final <T> T zza(zzgy<T> zzgy, zzem zzem) throws IOException {
        zzab(2);
        return zzc(zzgy, zzem);
    }

    public final <T> T zzb(zzgy<T> zzgy, zzem zzem) throws IOException {
        zzab(3);
        return zzd(zzgy, zzem);
    }

    private final <T> T zzc(zzgy<T> zzgy, zzem zzem) throws IOException {
        int zzks = this.zzacr.zzks();
        if (this.zzacr.zzach < this.zzacr.zzaci) {
            int zzx = this.zzacr.zzx(zzks);
            T newInstance = zzgy.newInstance();
            this.zzacr.zzach++;
            zzgy.zza(newInstance, this, zzem);
            zzgy.zzi(newInstance);
            this.zzacr.zzu(0);
            this.zzacr.zzach--;
            this.zzacr.zzy(zzx);
            return newInstance;
        }
        throw zzfh.zzna();
    }

    private final <T> T zzd(zzgy<T> zzgy, zzem zzem) throws IOException {
        int i = this.zzacs;
        this.zzacs = ((this.tag >>> 3) << 3) | 4;
        try {
            T newInstance = zzgy.newInstance();
            zzgy.zza(newInstance, this, zzem);
            zzgy.zzi(newInstance);
            if (this.tag == this.zzacs) {
                return newInstance;
            }
            throw zzfh.zznb();
        } finally {
            this.zzacs = i;
        }
    }

    public final zzdp zzkr() throws IOException {
        zzab(2);
        return this.zzacr.zzkr();
    }

    public final int zzks() throws IOException {
        zzab(0);
        return this.zzacr.zzks();
    }

    public final int zzkt() throws IOException {
        zzab(0);
        return this.zzacr.zzkt();
    }

    public final int zzku() throws IOException {
        zzab(5);
        return this.zzacr.zzku();
    }

    public final long zzkv() throws IOException {
        zzab(1);
        return this.zzacr.zzkv();
    }

    public final int zzkw() throws IOException {
        zzab(0);
        return this.zzacr.zzkw();
    }

    public final long zzkx() throws IOException {
        zzab(0);
        return this.zzacr.zzkx();
    }

    public final void zzd(List<Double> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzej) {
            zzej zzej = (zzej) list;
            switch (this.tag & 7) {
                case 1:
                    break;
                case 2:
                    int zzks = this.zzacr.zzks();
                    zzac(zzks);
                    int zzla = this.zzacr.zzla() + zzks;
                    do {
                        zzej.zzf(this.zzacr.readDouble());
                    } while (this.zzacr.zzla() < zzla);
                    return;
                default:
                    throw zzfh.zzmz();
            }
            do {
                zzej.zzf(this.zzacr.readDouble());
                if (!this.zzacr.zzkz()) {
                    zzkj2 = this.zzacr.zzkj();
                } else {
                    return;
                }
            } while (zzkj2 == this.tag);
            this.zzact = zzkj2;
            return;
        }
        switch (this.tag & 7) {
            case 1:
                break;
            case 2:
                int zzks2 = this.zzacr.zzks();
                zzac(zzks2);
                int zzla2 = this.zzacr.zzla() + zzks2;
                do {
                    list.add(Double.valueOf(this.zzacr.readDouble()));
                } while (this.zzacr.zzla() < zzla2);
                return;
            default:
                throw zzfh.zzmz();
        }
        do {
            list.add(Double.valueOf(this.zzacr.readDouble()));
            if (!this.zzacr.zzkz()) {
                zzkj = this.zzacr.zzkj();
            } else {
                return;
            }
        } while (zzkj == this.tag);
        this.zzact = zzkj;
    }

    public final void zze(List<Float> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzew) {
            zzew zzew = (zzew) list;
            int i = this.tag & 7;
            if (i == 2) {
                int zzks = this.zzacr.zzks();
                zzad(zzks);
                int zzla = this.zzacr.zzla() + zzks;
                do {
                    zzew.zzc(this.zzacr.readFloat());
                } while (this.zzacr.zzla() < zzla);
            } else if (i == 5) {
                do {
                    zzew.zzc(this.zzacr.readFloat());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 2) {
                int zzks2 = this.zzacr.zzks();
                zzad(zzks2);
                int zzla2 = this.zzacr.zzla() + zzks2;
                do {
                    list.add(Float.valueOf(this.zzacr.readFloat()));
                } while (this.zzacr.zzla() < zzla2);
            } else if (i2 == 5) {
                do {
                    list.add(Float.valueOf(this.zzacr.readFloat()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    public final void zzf(List<Long> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfv) {
            zzfv zzfv = (zzfv) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzfv.zzbb(this.zzacr.zzkk());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else if (i == 2) {
                int zzla = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    zzfv.zzbb(this.zzacr.zzkk());
                } while (this.zzacr.zzla() < zzla);
                zzae(zzla);
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Long.valueOf(this.zzacr.zzkk()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else if (i2 == 2) {
                int zzla2 = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    list.add(Long.valueOf(this.zzacr.zzkk()));
                } while (this.zzacr.zzla() < zzla2);
                zzae(zzla2);
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    public final void zzg(List<Long> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfv) {
            zzfv zzfv = (zzfv) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzfv.zzbb(this.zzacr.zzkl());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else if (i == 2) {
                int zzla = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    zzfv.zzbb(this.zzacr.zzkl());
                } while (this.zzacr.zzla() < zzla);
                zzae(zzla);
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Long.valueOf(this.zzacr.zzkl()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else if (i2 == 2) {
                int zzla2 = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    list.add(Long.valueOf(this.zzacr.zzkl()));
                } while (this.zzacr.zzla() < zzla2);
                zzae(zzla2);
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    public final void zzh(List<Integer> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfa) {
            zzfa zzfa = (zzfa) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzfa.zzau(this.zzacr.zzkm());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else if (i == 2) {
                int zzla = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    zzfa.zzau(this.zzacr.zzkm());
                } while (this.zzacr.zzla() < zzla);
                zzae(zzla);
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Integer.valueOf(this.zzacr.zzkm()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else if (i2 == 2) {
                int zzla2 = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    list.add(Integer.valueOf(this.zzacr.zzkm()));
                } while (this.zzacr.zzla() < zzla2);
                zzae(zzla2);
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    public final void zzi(List<Long> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfv) {
            zzfv zzfv = (zzfv) list;
            switch (this.tag & 7) {
                case 1:
                    break;
                case 2:
                    int zzks = this.zzacr.zzks();
                    zzac(zzks);
                    int zzla = this.zzacr.zzla() + zzks;
                    do {
                        zzfv.zzbb(this.zzacr.zzkn());
                    } while (this.zzacr.zzla() < zzla);
                    return;
                default:
                    throw zzfh.zzmz();
            }
            do {
                zzfv.zzbb(this.zzacr.zzkn());
                if (!this.zzacr.zzkz()) {
                    zzkj2 = this.zzacr.zzkj();
                } else {
                    return;
                }
            } while (zzkj2 == this.tag);
            this.zzact = zzkj2;
            return;
        }
        switch (this.tag & 7) {
            case 1:
                break;
            case 2:
                int zzks2 = this.zzacr.zzks();
                zzac(zzks2);
                int zzla2 = this.zzacr.zzla() + zzks2;
                do {
                    list.add(Long.valueOf(this.zzacr.zzkn()));
                } while (this.zzacr.zzla() < zzla2);
                return;
            default:
                throw zzfh.zzmz();
        }
        do {
            list.add(Long.valueOf(this.zzacr.zzkn()));
            if (!this.zzacr.zzkz()) {
                zzkj = this.zzacr.zzkj();
            } else {
                return;
            }
        } while (zzkj == this.tag);
        this.zzact = zzkj;
    }

    public final void zzj(List<Integer> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfa) {
            zzfa zzfa = (zzfa) list;
            int i = this.tag & 7;
            if (i == 2) {
                int zzks = this.zzacr.zzks();
                zzad(zzks);
                int zzla = this.zzacr.zzla() + zzks;
                do {
                    zzfa.zzau(this.zzacr.zzko());
                } while (this.zzacr.zzla() < zzla);
            } else if (i == 5) {
                do {
                    zzfa.zzau(this.zzacr.zzko());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 2) {
                int zzks2 = this.zzacr.zzks();
                zzad(zzks2);
                int zzla2 = this.zzacr.zzla() + zzks2;
                do {
                    list.add(Integer.valueOf(this.zzacr.zzko()));
                } while (this.zzacr.zzla() < zzla2);
            } else if (i2 == 5) {
                do {
                    list.add(Integer.valueOf(this.zzacr.zzko()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    public final void zzk(List<Boolean> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzdn) {
            zzdn zzdn = (zzdn) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzdn.addBoolean(this.zzacr.zzkp());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else if (i == 2) {
                int zzla = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    zzdn.addBoolean(this.zzacr.zzkp());
                } while (this.zzacr.zzla() < zzla);
                zzae(zzla);
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Boolean.valueOf(this.zzacr.zzkp()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else if (i2 == 2) {
                int zzla2 = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    list.add(Boolean.valueOf(this.zzacr.zzkp()));
                } while (this.zzacr.zzla() < zzla2);
                zzae(zzla2);
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    public final void readStringList(List<String> list) throws IOException {
        zza(list, false);
    }

    public final void zzl(List<String> list) throws IOException {
        zza(list, true);
    }

    private final void zza(List<String> list, boolean z) throws IOException {
        int zzkj;
        int zzkj2;
        if ((this.tag & 7) != 2) {
            throw zzfh.zzmz();
        } else if (!(list instanceof zzfq) || z) {
            do {
                list.add(z ? zzkq() : readString());
                if (!this.zzacr.zzkz()) {
                    zzkj = this.zzacr.zzkj();
                } else {
                    return;
                }
            } while (zzkj == this.tag);
            this.zzact = zzkj;
        } else {
            zzfq zzfq = (zzfq) list;
            do {
                zzfq.zzc(zzkr());
                if (!this.zzacr.zzkz()) {
                    zzkj2 = this.zzacr.zzkj();
                } else {
                    return;
                }
            } while (zzkj2 == this.tag);
            this.zzact = zzkj2;
        }
    }

    public final <T> void zza(List<T> list, zzgy<T> zzgy, zzem zzem) throws IOException {
        int zzkj;
        int i = this.tag;
        if ((i & 7) == 2) {
            do {
                list.add(zzc(zzgy, zzem));
                if (!this.zzacr.zzkz() && this.zzact == 0) {
                    zzkj = this.zzacr.zzkj();
                } else {
                    return;
                }
            } while (zzkj == i);
            this.zzact = zzkj;
            return;
        }
        throw zzfh.zzmz();
    }

    public final <T> void zzb(List<T> list, zzgy<T> zzgy, zzem zzem) throws IOException {
        int zzkj;
        int i = this.tag;
        if ((i & 7) == 3) {
            do {
                list.add(zzd(zzgy, zzem));
                if (!this.zzacr.zzkz() && this.zzact == 0) {
                    zzkj = this.zzacr.zzkj();
                } else {
                    return;
                }
            } while (zzkj == i);
            this.zzact = zzkj;
            return;
        }
        throw zzfh.zzmz();
    }

    public final void zzm(List<zzdp> list) throws IOException {
        int zzkj;
        if ((this.tag & 7) == 2) {
            do {
                list.add(zzkr());
                if (!this.zzacr.zzkz()) {
                    zzkj = this.zzacr.zzkj();
                } else {
                    return;
                }
            } while (zzkj == this.tag);
            this.zzact = zzkj;
            return;
        }
        throw zzfh.zzmz();
    }

    public final void zzn(List<Integer> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfa) {
            zzfa zzfa = (zzfa) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzfa.zzau(this.zzacr.zzks());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else if (i == 2) {
                int zzla = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    zzfa.zzau(this.zzacr.zzks());
                } while (this.zzacr.zzla() < zzla);
                zzae(zzla);
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Integer.valueOf(this.zzacr.zzks()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else if (i2 == 2) {
                int zzla2 = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    list.add(Integer.valueOf(this.zzacr.zzks()));
                } while (this.zzacr.zzla() < zzla2);
                zzae(zzla2);
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    public final void zzo(List<Integer> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfa) {
            zzfa zzfa = (zzfa) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzfa.zzau(this.zzacr.zzkt());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else if (i == 2) {
                int zzla = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    zzfa.zzau(this.zzacr.zzkt());
                } while (this.zzacr.zzla() < zzla);
                zzae(zzla);
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Integer.valueOf(this.zzacr.zzkt()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else if (i2 == 2) {
                int zzla2 = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    list.add(Integer.valueOf(this.zzacr.zzkt()));
                } while (this.zzacr.zzla() < zzla2);
                zzae(zzla2);
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    public final void zzp(List<Integer> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfa) {
            zzfa zzfa = (zzfa) list;
            int i = this.tag & 7;
            if (i == 2) {
                int zzks = this.zzacr.zzks();
                zzad(zzks);
                int zzla = this.zzacr.zzla() + zzks;
                do {
                    zzfa.zzau(this.zzacr.zzku());
                } while (this.zzacr.zzla() < zzla);
            } else if (i == 5) {
                do {
                    zzfa.zzau(this.zzacr.zzku());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 2) {
                int zzks2 = this.zzacr.zzks();
                zzad(zzks2);
                int zzla2 = this.zzacr.zzla() + zzks2;
                do {
                    list.add(Integer.valueOf(this.zzacr.zzku()));
                } while (this.zzacr.zzla() < zzla2);
            } else if (i2 == 5) {
                do {
                    list.add(Integer.valueOf(this.zzacr.zzku()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    public final void zzq(List<Long> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfv) {
            zzfv zzfv = (zzfv) list;
            switch (this.tag & 7) {
                case 1:
                    break;
                case 2:
                    int zzks = this.zzacr.zzks();
                    zzac(zzks);
                    int zzla = this.zzacr.zzla() + zzks;
                    do {
                        zzfv.zzbb(this.zzacr.zzkv());
                    } while (this.zzacr.zzla() < zzla);
                    return;
                default:
                    throw zzfh.zzmz();
            }
            do {
                zzfv.zzbb(this.zzacr.zzkv());
                if (!this.zzacr.zzkz()) {
                    zzkj2 = this.zzacr.zzkj();
                } else {
                    return;
                }
            } while (zzkj2 == this.tag);
            this.zzact = zzkj2;
            return;
        }
        switch (this.tag & 7) {
            case 1:
                break;
            case 2:
                int zzks2 = this.zzacr.zzks();
                zzac(zzks2);
                int zzla2 = this.zzacr.zzla() + zzks2;
                do {
                    list.add(Long.valueOf(this.zzacr.zzkv()));
                } while (this.zzacr.zzla() < zzla2);
                return;
            default:
                throw zzfh.zzmz();
        }
        do {
            list.add(Long.valueOf(this.zzacr.zzkv()));
            if (!this.zzacr.zzkz()) {
                zzkj = this.zzacr.zzkj();
            } else {
                return;
            }
        } while (zzkj == this.tag);
        this.zzact = zzkj;
    }

    public final void zzr(List<Integer> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfa) {
            zzfa zzfa = (zzfa) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzfa.zzau(this.zzacr.zzkw());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else if (i == 2) {
                int zzla = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    zzfa.zzau(this.zzacr.zzkw());
                } while (this.zzacr.zzla() < zzla);
                zzae(zzla);
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Integer.valueOf(this.zzacr.zzkw()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else if (i2 == 2) {
                int zzla2 = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    list.add(Integer.valueOf(this.zzacr.zzkw()));
                } while (this.zzacr.zzla() < zzla2);
                zzae(zzla2);
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    public final void zzs(List<Long> list) throws IOException {
        int zzkj;
        int zzkj2;
        if (list instanceof zzfv) {
            zzfv zzfv = (zzfv) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzfv.zzbb(this.zzacr.zzkx());
                    if (!this.zzacr.zzkz()) {
                        zzkj2 = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj2 == this.tag);
                this.zzact = zzkj2;
            } else if (i == 2) {
                int zzla = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    zzfv.zzbb(this.zzacr.zzkx());
                } while (this.zzacr.zzla() < zzla);
                zzae(zzla);
            } else {
                throw zzfh.zzmz();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Long.valueOf(this.zzacr.zzkx()));
                    if (!this.zzacr.zzkz()) {
                        zzkj = this.zzacr.zzkj();
                    } else {
                        return;
                    }
                } while (zzkj == this.tag);
                this.zzact = zzkj;
            } else if (i2 == 2) {
                int zzla2 = this.zzacr.zzla() + this.zzacr.zzks();
                do {
                    list.add(Long.valueOf(this.zzacr.zzkx()));
                } while (this.zzacr.zzla() < zzla2);
                zzae(zzla2);
            } else {
                throw zzfh.zzmz();
            }
        }
    }

    private static void zzac(int i) throws IOException {
        if ((i & 7) != 0) {
            throw zzfh.zznb();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0051, code lost:
        if (zzli() != false) goto L_0x0053;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005b, code lost:
        throw new com.google.android.gms.internal.measurement.zzfh("Unable to parse map entry.");
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x004d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K, V> void zza(java.util.Map<K, V> r6, com.google.android.gms.internal.measurement.zzga<K, V> r7, com.google.android.gms.internal.measurement.zzem r8) throws java.io.IOException {
        /*
            r5 = this;
            r0 = 2
            r5.zzab(r0)
            com.google.android.gms.internal.measurement.zzeb r0 = r5.zzacr
            int r0 = r0.zzks()
            com.google.android.gms.internal.measurement.zzeb r1 = r5.zzacr
            int r0 = r1.zzx(r0)
            K r1 = r7.zzait
            V r2 = r7.zzzw
        L_0x0014:
            int r3 = r5.zzlh()     // Catch:{ all -> 0x0065 }
            r4 = 2147483647(0x7fffffff, float:NaN)
            if (r3 == r4) goto L_0x005c
            com.google.android.gms.internal.measurement.zzeb r4 = r5.zzacr     // Catch:{ all -> 0x0065 }
            boolean r4 = r4.zzkz()     // Catch:{ all -> 0x0065 }
            if (r4 != 0) goto L_0x005c
            switch(r3) {
                case 1: goto L_0x003a;
                case 2: goto L_0x002d;
                default: goto L_0x0028;
            }
        L_0x0028:
            boolean r3 = r5.zzli()     // Catch:{ zzfi -> 0x004d }
            goto L_0x0042
        L_0x002d:
            com.google.android.gms.internal.measurement.zzif r3 = r7.zzaiu     // Catch:{ zzfi -> 0x004d }
            V r4 = r7.zzzw     // Catch:{ zzfi -> 0x004d }
            java.lang.Class r4 = r4.getClass()     // Catch:{ zzfi -> 0x004d }
            java.lang.Object r2 = r5.zza(r3, r4, r8)     // Catch:{ zzfi -> 0x004d }
            goto L_0x0014
        L_0x003a:
            com.google.android.gms.internal.measurement.zzif r3 = r7.zzais     // Catch:{ zzfi -> 0x004d }
            r4 = 0
            java.lang.Object r1 = r5.zza(r3, r4, r4)     // Catch:{ zzfi -> 0x004d }
            goto L_0x0014
        L_0x0042:
            if (r3 == 0) goto L_0x0045
            goto L_0x0014
        L_0x0045:
            com.google.android.gms.internal.measurement.zzfh r3 = new com.google.android.gms.internal.measurement.zzfh     // Catch:{ zzfi -> 0x004d }
            java.lang.String r4 = "Unable to parse map entry."
            r3.<init>(r4)     // Catch:{ zzfi -> 0x004d }
            throw r3     // Catch:{ zzfi -> 0x004d }
        L_0x004d:
            boolean r3 = r5.zzli()     // Catch:{ all -> 0x0065 }
            if (r3 == 0) goto L_0x0054
            goto L_0x0014
        L_0x0054:
            com.google.android.gms.internal.measurement.zzfh r6 = new com.google.android.gms.internal.measurement.zzfh     // Catch:{ all -> 0x0065 }
            java.lang.String r7 = "Unable to parse map entry."
            r6.<init>(r7)     // Catch:{ all -> 0x0065 }
            throw r6     // Catch:{ all -> 0x0065 }
        L_0x005c:
            r6.put(r1, r2)     // Catch:{ all -> 0x0065 }
            com.google.android.gms.internal.measurement.zzeb r6 = r5.zzacr
            r6.zzy(r0)
            return
        L_0x0065:
            r6 = move-exception
            com.google.android.gms.internal.measurement.zzeb r7 = r5.zzacr
            r7.zzy(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzee.zza(java.util.Map, com.google.android.gms.internal.measurement.zzga, com.google.android.gms.internal.measurement.zzem):void");
    }

    private final Object zza(zzif zzif, Class<?> cls, zzem zzem) throws IOException {
        switch (zzef.zzacu[zzif.ordinal()]) {
            case 1:
                return Boolean.valueOf(zzkp());
            case 2:
                return zzkr();
            case 3:
                return Double.valueOf(readDouble());
            case 4:
                return Integer.valueOf(zzkt());
            case 5:
                return Integer.valueOf(zzko());
            case 6:
                return Long.valueOf(zzkn());
            case 7:
                return Float.valueOf(readFloat());
            case 8:
                return Integer.valueOf(zzkm());
            case 9:
                return Long.valueOf(zzkl());
            case 10:
                zzab(2);
                return zzc(zzgu.zznz().zzf(cls), zzem);
            case 11:
                return Integer.valueOf(zzku());
            case 12:
                return Long.valueOf(zzkv());
            case 13:
                return Integer.valueOf(zzkw());
            case 14:
                return Long.valueOf(zzkx());
            case 15:
                return zzkq();
            case 16:
                return Integer.valueOf(zzks());
            case 17:
                return Long.valueOf(zzkk());
            default:
                throw new RuntimeException("unsupported field type.");
        }
    }

    private static void zzad(int i) throws IOException {
        if ((i & 3) != 0) {
            throw zzfh.zznb();
        }
    }

    private final void zzae(int i) throws IOException {
        if (this.zzacr.zzla() != i) {
            throw zzfh.zzmu();
        }
    }
}
