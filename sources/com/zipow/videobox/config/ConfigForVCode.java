package com.zipow.videobox.config;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zipow.videobox.util.PreferenceUtil;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class ConfigForVCode {
    private List<String> mInstallResFileNameList = new ArrayList();
    private int mVersionCode;

    public int getmVersionCode() {
        return this.mVersionCode;
    }

    public void setmVersionCode(int i) {
        this.mVersionCode = i;
    }

    public List<String> getmInstallResFileNameList() {
        return this.mInstallResFileNameList;
    }

    public void setmInstallResFileNameList(List<String> list) {
        this.mInstallResFileNameList = list;
    }

    public void save() {
        PreferenceUtil.saveStringValue(PreferenceUtil.ZOOM_CONFIG_FOR_VCODE, new Gson().toJson((Object) this, new TypeToken<ConfigForVCode>() {
        }.getType()));
    }

    public static ConfigForVCode readCurrentConfig(@NonNull Context context) {
        ConfigForVCode configForVCode = new ConfigForVCode();
        int appVersionCode = AndroidAppUtil.getAppVersionCode(context);
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.ZOOM_CONFIG_FOR_VCODE, null);
        if (!StringUtil.isEmptyOrNull(readStringValue)) {
            configForVCode = (ConfigForVCode) new Gson().fromJson(readStringValue, new TypeToken<ConfigForVCode>() {
            }.getType());
            if (configForVCode != null && configForVCode.getmVersionCode() == appVersionCode) {
                return configForVCode;
            }
        }
        if (configForVCode == null) {
            configForVCode = new ConfigForVCode();
        }
        configForVCode.setmVersionCode(appVersionCode);
        configForVCode.setmInstallResFileNameList(null);
        return configForVCode;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<String> list = this.mInstallResFileNameList;
        if (list != null) {
            for (String append : list) {
                sb.append(append);
                sb.append(", ");
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("ConfigForVCode{mVersionCode=");
        sb2.append(this.mVersionCode);
        sb2.append(", mInstallResFileNameList=");
        sb2.append(sb.toString());
        sb2.append('}');
        return sb2.toString();
    }
}
