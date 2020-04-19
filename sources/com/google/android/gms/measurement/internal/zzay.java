package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public final class zzay extends zzfs {
    private final SSLSocketFactory zzkj;

    public zzay(zzft zzft) {
        super(zzft);
        this.zzkj = VERSION.SDK_INT < 19 ? new zzge() : null;
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    public final boolean zzdl() {
        NetworkInfo networkInfo;
        zzah();
        try {
            networkInfo = ((ConnectivityManager) getContext().getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (SecurityException unused) {
            networkInfo = null;
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public static byte[] zza(HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            inputStream = httpURLConnection.getInputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            return byteArrayOutputStream.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    @VisibleForTesting
    public final HttpURLConnection zza(URL url) throws IOException {
        URLConnection openConnection = url.openConnection();
        if (openConnection instanceof HttpURLConnection) {
            SSLSocketFactory sSLSocketFactory = this.zzkj;
            if (sSLSocketFactory != null && (openConnection instanceof HttpsURLConnection)) {
                ((HttpsURLConnection) openConnection).setSSLSocketFactory(sSLSocketFactory);
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
            httpURLConnection.setDefaultUseCaches(false);
            httpURLConnection.setConnectTimeout(60000);
            httpURLConnection.setReadTimeout(61000);
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setDoInput(true);
            return httpURLConnection;
        }
        throw new IOException("Failed to obtain HTTP connection");
    }

    public final /* bridge */ /* synthetic */ zzfz zzdm() {
        return super.zzdm();
    }

    public final /* bridge */ /* synthetic */ zzo zzdn() {
        return super.zzdn();
    }

    public final /* bridge */ /* synthetic */ zzw zzdo() {
        return super.zzdo();
    }

    public final /* bridge */ /* synthetic */ zzbs zzdp() {
        return super.zzdp();
    }

    public final /* bridge */ /* synthetic */ void zzn() {
        super.zzn();
    }

    public final /* bridge */ /* synthetic */ void zzo() {
        super.zzo();
    }

    public final /* bridge */ /* synthetic */ void zzp() {
        super.zzp();
    }

    public final /* bridge */ /* synthetic */ void zzq() {
        super.zzq();
    }

    public final /* bridge */ /* synthetic */ zzad zzy() {
        return super.zzy();
    }

    public final /* bridge */ /* synthetic */ Clock zzz() {
        return super.zzz();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzas zzaa() {
        return super.zzaa();
    }

    public final /* bridge */ /* synthetic */ zzgd zzab() {
        return super.zzab();
    }

    public final /* bridge */ /* synthetic */ zzbt zzac() {
        return super.zzac();
    }

    public final /* bridge */ /* synthetic */ zzau zzad() {
        return super.zzad();
    }

    public final /* bridge */ /* synthetic */ zzbf zzae() {
        return super.zzae();
    }

    public final /* bridge */ /* synthetic */ zzt zzaf() {
        return super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zzq zzag() {
        return super.zzag();
    }
}
