package com.google.android.gms.measurement.internal;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.util.Clock;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class zzad extends zzcu {
    private long zzew;
    private String zzex;
    private Boolean zzey;
    private AccountManager zzez;
    private Boolean zzfa;
    private long zzfb;

    zzad(zzby zzby) {
        super(zzby);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        Calendar instance = Calendar.getInstance();
        this.zzew = TimeUnit.MINUTES.convert((long) (instance.get(15) + instance.get(16)), TimeUnit.MILLISECONDS);
        Locale locale = Locale.getDefault();
        String lowerCase = locale.getLanguage().toLowerCase(Locale.ENGLISH);
        String lowerCase2 = locale.getCountry().toLowerCase(Locale.ENGLISH);
        StringBuilder sb = new StringBuilder(String.valueOf(lowerCase).length() + 1 + String.valueOf(lowerCase2).length());
        sb.append(lowerCase);
        sb.append("-");
        sb.append(lowerCase2);
        this.zzex = sb.toString();
        return false;
    }

    public final long zzco() {
        zzah();
        return this.zzew;
    }

    public final String zzcp() {
        zzah();
        return this.zzex;
    }

    public final boolean zzj(Context context) {
        if (this.zzey == null) {
            zzag();
            this.zzey = Boolean.valueOf(false);
            try {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager != null) {
                    packageManager.getPackageInfo("com.google.android.gms", 128);
                    this.zzey = Boolean.valueOf(true);
                }
            } catch (NameNotFoundException unused) {
            }
        }
        return this.zzey.booleanValue();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final long zzcq() {
        zzq();
        return this.zzfb;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzcr() {
        zzq();
        this.zzfa = null;
        this.zzfb = 0;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzcs() {
        zzq();
        long currentTimeMillis = zzz().currentTimeMillis();
        if (currentTimeMillis - this.zzfb > 86400000) {
            this.zzfa = null;
        }
        Boolean bool = this.zzfa;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.GET_ACCOUNTS") != 0) {
            zzad().zzde().zzaq("Permission error checking for dasher/unicorn accounts");
            this.zzfb = currentTimeMillis;
            this.zzfa = Boolean.valueOf(false);
            return false;
        }
        if (this.zzez == null) {
            this.zzez = AccountManager.get(getContext());
        }
        try {
            Account[] accountArr = (Account[]) this.zzez.getAccountsByTypeAndFeatures("com.google", new String[]{"service_HOSTED"}, null, null).getResult();
            if (accountArr == null || accountArr.length <= 0) {
                Account[] accountArr2 = (Account[]) this.zzez.getAccountsByTypeAndFeatures("com.google", new String[]{"service_uca"}, null, null).getResult();
                if (accountArr2 != null && accountArr2.length > 0) {
                    this.zzfa = Boolean.valueOf(true);
                    this.zzfb = currentTimeMillis;
                    return true;
                }
                this.zzfb = currentTimeMillis;
                this.zzfa = Boolean.valueOf(false);
                return false;
            }
            this.zzfa = Boolean.valueOf(true);
            this.zzfb = currentTimeMillis;
            return true;
        } catch (AuthenticatorException | OperationCanceledException | IOException e) {
            zzad().zzdb().zza("Exception checking account types", e);
        }
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
