package com.zipow.videobox.util.zmurl.avatar;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Key;
import com.zipow.videobox.util.zmurl.BaseZMUrl;
import java.security.MessageDigest;

public class ZMAvatarUrl extends BaseZMUrl implements Key {
    private String bgColorSeedString;
    private String bgNameSeedString;
    private int draw;
    private ZMAvatarCornerParams zMAvatarCornerParams;

    public String getBgNameSeedString() {
        return this.bgNameSeedString;
    }

    public String getBgColorSeedString() {
        return this.bgColorSeedString;
    }

    public int getDrawIcon() {
        return this.draw;
    }

    public boolean isRoundCorner() {
        return this.zMAvatarCornerParams != null;
    }

    public ZMAvatarCornerParams getZMAvatarCornerParams() {
        return this.zMAvatarCornerParams;
    }

    public ZMAvatarUrl(String str, String str2, String str3, @DrawableRes int i, ZMAvatarCornerParams zMAvatarCornerParams2) {
        super(str);
        this.bgNameSeedString = str2;
        this.bgColorSeedString = str3;
        this.draw = i;
        this.zMAvatarCornerParams = zMAvatarCornerParams2;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ZMUrl{url=");
        sb.append(getUrl() != null ? getUrl() : "");
        sb.append(",draw=");
        sb.append(this.draw);
        sb.append(",bgNameSeedString=");
        String str = this.bgNameSeedString;
        if (str == null) {
            str = "";
        }
        sb.append(str);
        sb.append(",bgColorSeedString=");
        String str2 = this.bgColorSeedString;
        if (str2 == null) {
            str2 = "";
        }
        sb.append(str2);
        sb.append(", zMAvatarCornerParams=");
        ZMAvatarCornerParams zMAvatarCornerParams2 = this.zMAvatarCornerParams;
        sb.append(zMAvatarCornerParams2 != null ? zMAvatarCornerParams2.toString() : "");
        sb.append('}');
        return sb.toString();
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ZMAvatarUrl zMAvatarUrl = (ZMAvatarUrl) obj;
        if (getUrl() != null && getUrl().equals(zMAvatarUrl.getUrl())) {
            String str = this.bgColorSeedString;
            if (str != null) {
                z = str.equals(zMAvatarUrl.bgColorSeedString);
                return z;
            }
        }
        if (zMAvatarUrl.bgColorSeedString == null) {
            String str2 = this.bgNameSeedString;
            if (str2 != null) {
                z = str2.equals(zMAvatarUrl.bgNameSeedString);
                return z;
            }
        }
        if (zMAvatarUrl.bgNameSeedString == null) {
            ZMAvatarCornerParams zMAvatarCornerParams2 = this.zMAvatarCornerParams;
            if (zMAvatarCornerParams2 != null) {
                z = zMAvatarCornerParams2.equals(zMAvatarUrl.zMAvatarCornerParams);
                return z;
            }
        }
        if (!(zMAvatarUrl.zMAvatarCornerParams == null && this.draw == zMAvatarUrl.draw)) {
            z = false;
        }
        return z;
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(toString().getBytes(CHARSET));
    }
}
