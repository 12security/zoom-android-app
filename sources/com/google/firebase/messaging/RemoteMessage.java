package com.google.firebase.messaging;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.zipow.videobox.MMImageListActivity;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.Map.Entry;

@Class(creator = "RemoteMessageCreator")
@Reserved({1})
public final class RemoteMessage extends AbstractSafeParcelable {
    public static final Creator<RemoteMessage> CREATOR = new zzg();
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_NORMAL = 2;
    public static final int PRIORITY_UNKNOWN = 0;
    @Field(mo23591id = 2)
    Bundle zzeh;
    private Map<String, String> zzei;
    private Notification zzej;

    public static class Builder {
        private final Bundle zzeh = new Bundle();
        private final Map<String, String> zzei = new ArrayMap();

        public Builder(String str) {
            if (TextUtils.isEmpty(str)) {
                String str2 = "Invalid to: ";
                String valueOf = String.valueOf(str);
                throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            }
            this.zzeh.putString("google.to", str);
        }

        public RemoteMessage build() {
            Bundle bundle = new Bundle();
            for (Entry entry : this.zzei.entrySet()) {
                bundle.putString((String) entry.getKey(), (String) entry.getValue());
            }
            bundle.putAll(this.zzeh);
            this.zzeh.remove("from");
            return new RemoteMessage(bundle);
        }

        public Builder addData(String str, String str2) {
            this.zzei.put(str, str2);
            return this;
        }

        public Builder setData(Map<String, String> map) {
            this.zzei.clear();
            this.zzei.putAll(map);
            return this;
        }

        public Builder clearData() {
            this.zzei.clear();
            return this;
        }

        public Builder setMessageId(String str) {
            this.zzeh.putString("google.message_id", str);
            return this;
        }

        public Builder setMessageType(String str) {
            this.zzeh.putString("message_type", str);
            return this;
        }

        public Builder setTtl(@IntRange(from = 0, mo587to = 86400) int i) {
            this.zzeh.putString("google.ttl", String.valueOf(i));
            return this;
        }

        public Builder setCollapseKey(String str) {
            this.zzeh.putString("collapse_key", str);
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MessagePriority {
    }

    public static class Notification {
        private final String tag;
        private final String zzek;
        private final String zzel;
        private final String[] zzem;
        private final String zzen;
        private final String zzeo;
        private final String[] zzep;
        private final String zzeq;
        private final String zzer;
        private final String zzes;
        private final String zzet;
        private final String zzeu;
        private final String zzev;
        private final Uri zzew;

        private Notification(Bundle bundle) {
            this.zzek = zza.zza(bundle, "gcm.n.title");
            this.zzel = zza.zzd(bundle, "gcm.n.title");
            this.zzem = zzf(bundle, "gcm.n.title");
            this.zzen = zza.zza(bundle, "gcm.n.body");
            this.zzeo = zza.zzd(bundle, "gcm.n.body");
            this.zzep = zzf(bundle, "gcm.n.body");
            this.zzeq = zza.zza(bundle, "gcm.n.icon");
            this.zzes = zza.zzi(bundle);
            this.tag = zza.zza(bundle, "gcm.n.tag");
            this.zzet = zza.zza(bundle, "gcm.n.color");
            this.zzeu = zza.zza(bundle, "gcm.n.click_action");
            this.zzev = zza.zza(bundle, "gcm.n.android_channel_id");
            this.zzew = zza.zzj(bundle);
            this.zzer = zza.zza(bundle, "gcm.n.image");
        }

        private static String[] zzf(Bundle bundle, String str) {
            Object[] zzb = zza.zzb(bundle, str);
            if (zzb == null) {
                return null;
            }
            String[] strArr = new String[zzb.length];
            for (int i = 0; i < zzb.length; i++) {
                strArr[i] = String.valueOf(zzb[i]);
            }
            return strArr;
        }

        @Nullable
        public String getTitle() {
            return this.zzek;
        }

        @Nullable
        public String getTitleLocalizationKey() {
            return this.zzel;
        }

        @Nullable
        public String[] getTitleLocalizationArgs() {
            return this.zzem;
        }

        @Nullable
        public String getBody() {
            return this.zzen;
        }

        @Nullable
        public String getBodyLocalizationKey() {
            return this.zzeo;
        }

        @Nullable
        public String[] getBodyLocalizationArgs() {
            return this.zzep;
        }

        @Nullable
        public String getIcon() {
            return this.zzeq;
        }

        @Nullable
        public Uri getImageUrl() {
            String str = this.zzer;
            if (str != null) {
                return Uri.parse(str);
            }
            return null;
        }

        @Nullable
        public String getSound() {
            return this.zzes;
        }

        @Nullable
        public String getTag() {
            return this.tag;
        }

        @Nullable
        public String getColor() {
            return this.zzet;
        }

        @Nullable
        public String getClickAction() {
            return this.zzeu;
        }

        @Nullable
        public String getChannelId() {
            return this.zzev;
        }

        @Nullable
        public Uri getLink() {
            return this.zzew;
        }
    }

    @Constructor
    public RemoteMessage(@Param(mo23594id = 2) Bundle bundle) {
        this.zzeh = bundle;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, this.zzeh, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    @Nullable
    public final String getFrom() {
        return this.zzeh.getString("from");
    }

    @Nullable
    public final String getTo() {
        return this.zzeh.getString("google.to");
    }

    public final Map<String, String> getData() {
        if (this.zzei == null) {
            Bundle bundle = this.zzeh;
            ArrayMap arrayMap = new ArrayMap();
            for (String str : bundle.keySet()) {
                Object obj = bundle.get(str);
                if (obj instanceof String) {
                    String str2 = (String) obj;
                    if (!str.startsWith("google.") && !str.startsWith("gcm.") && !str.equals("from") && !str.equals("message_type") && !str.equals("collapse_key")) {
                        arrayMap.put(str, str2);
                    }
                }
            }
            this.zzei = arrayMap;
        }
        return this.zzei;
    }

    @Nullable
    public final String getCollapseKey() {
        return this.zzeh.getString("collapse_key");
    }

    @Nullable
    public final String getMessageId() {
        String string = this.zzeh.getString("google.message_id");
        return string == null ? this.zzeh.getString(MMImageListActivity.ARG_MESSAGE_ID) : string;
    }

    @Nullable
    public final String getMessageType() {
        return this.zzeh.getString("message_type");
    }

    public final long getSentTime() {
        Object obj = this.zzeh.get("google.sent_time");
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        }
        if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException unused) {
                String valueOf = String.valueOf(obj);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 19);
                sb.append("Invalid sent time: ");
                sb.append(valueOf);
                Log.w("FirebaseMessaging", sb.toString());
            }
        }
        return 0;
    }

    public final int getTtl() {
        Object obj = this.zzeh.get("google.ttl");
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException unused) {
                String valueOf = String.valueOf(obj);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 13);
                sb.append("Invalid TTL: ");
                sb.append(valueOf);
                Log.w("FirebaseMessaging", sb.toString());
            }
        }
        return 0;
    }

    public final int getOriginalPriority() {
        String string = this.zzeh.getString("google.original_priority");
        if (string == null) {
            string = this.zzeh.getString("google.priority");
        }
        return zzp(string);
    }

    public final int getPriority() {
        String string = this.zzeh.getString("google.delivered_priority");
        if (string == null) {
            if ("1".equals(this.zzeh.getString("google.priority_reduced"))) {
                return 2;
            }
            string = this.zzeh.getString("google.priority");
        }
        return zzp(string);
    }

    private static int zzp(String str) {
        if ("high".equals(str)) {
            return 1;
        }
        return "normal".equals(str) ? 2 : 0;
    }

    @Nullable
    public final Notification getNotification() {
        if (this.zzej == null && zza.zzh(this.zzeh)) {
            this.zzej = new Notification(this.zzeh);
        }
        return this.zzej;
    }

    @KeepForSdk
    public final Intent toIntent() {
        Intent intent = new Intent();
        intent.putExtras(this.zzeh);
        return intent;
    }
}
