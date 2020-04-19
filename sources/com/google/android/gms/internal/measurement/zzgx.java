package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.List;
import java.util.Map;

interface zzgx {
    int getTag();

    double readDouble() throws IOException;

    float readFloat() throws IOException;

    String readString() throws IOException;

    void readStringList(List<String> list) throws IOException;

    <T> T zza(zzgy<T> zzgy, zzem zzem) throws IOException;

    <T> void zza(List<T> list, zzgy<T> zzgy, zzem zzem) throws IOException;

    <K, V> void zza(Map<K, V> map, zzga<K, V> zzga, zzem zzem) throws IOException;

    @Deprecated
    <T> T zzb(zzgy<T> zzgy, zzem zzem) throws IOException;

    @Deprecated
    <T> void zzb(List<T> list, zzgy<T> zzgy, zzem zzem) throws IOException;

    void zzd(List<Double> list) throws IOException;

    void zze(List<Float> list) throws IOException;

    void zzf(List<Long> list) throws IOException;

    void zzg(List<Long> list) throws IOException;

    void zzh(List<Integer> list) throws IOException;

    void zzi(List<Long> list) throws IOException;

    void zzj(List<Integer> list) throws IOException;

    void zzk(List<Boolean> list) throws IOException;

    long zzkk() throws IOException;

    long zzkl() throws IOException;

    int zzkm() throws IOException;

    long zzkn() throws IOException;

    int zzko() throws IOException;

    boolean zzkp() throws IOException;

    String zzkq() throws IOException;

    zzdp zzkr() throws IOException;

    int zzks() throws IOException;

    int zzkt() throws IOException;

    int zzku() throws IOException;

    long zzkv() throws IOException;

    int zzkw() throws IOException;

    long zzkx() throws IOException;

    void zzl(List<String> list) throws IOException;

    int zzlh() throws IOException;

    boolean zzli() throws IOException;

    void zzm(List<zzdp> list) throws IOException;

    void zzn(List<Integer> list) throws IOException;

    void zzo(List<Integer> list) throws IOException;

    void zzp(List<Integer> list) throws IOException;

    void zzq(List<Long> list) throws IOException;

    void zzr(List<Integer> list) throws IOException;

    void zzs(List<Long> list) throws IOException;
}
