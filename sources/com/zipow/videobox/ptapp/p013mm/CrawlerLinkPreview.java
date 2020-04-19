package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.CrawlerLinkPreviewUI;
import com.zipow.videobox.ptapp.IMProtos.CrawlLinkMetaInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.ZMDomainUtil;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.CrawlerLinkPreview */
public class CrawlerLinkPreview {
    public static final int MAX_LINK_PREVIEWS = 4;
    private long mNativeHandle;

    @Nullable
    private native String CrawlLinkMetaInfoImpl(long j, String str, String str2, List<String> list);

    @Nullable
    private native String DownloadFaviconImpl(long j, String str, String str2);

    @Nullable
    private native String DownloadImageImpl(long j, String str, String str2);

    @Nullable
    private native byte[] FuzzyGetLinkMetaInfoImpl(long j, String str);

    private native boolean NeedDownloadFaviconImpl(long j, String str);

    private native boolean NeedDownloadImageImpl(long j, String str);

    private native void RegisterUICallbackImpl(long j, long j2);

    private native boolean sendLinkMetaInfoImpl(long j, String str, String str2, List<String> list);

    public CrawlerLinkPreview(long j) {
        this.mNativeHandle = j;
    }

    public void RegisterUICallback(@Nullable CrawlerLinkPreviewUI crawlerLinkPreviewUI) {
        long j = this.mNativeHandle;
        if (j != 0 && crawlerLinkPreviewUI != null) {
            RegisterUICallbackImpl(j, crawlerLinkPreviewUI.getNativeHandle());
        }
    }

    @Nullable
    public String CrawlLinkMetaInfo(String str, String str2, @NonNull List<String> list) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2) || CollectionsUtil.isListEmpty(list)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str3 : list) {
            if (isAvailableUrl(str3)) {
                arrayList.add(str3);
            }
        }
        return CrawlLinkMetaInfoImpl(this.mNativeHandle, str, str2, arrayList);
    }

    @Nullable
    public CrawlLinkMetaInfo FuzzyGetLinkMetaInfo(@NonNull String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || !isAvailableUrl(str)) {
            return null;
        }
        byte[] FuzzyGetLinkMetaInfoImpl = FuzzyGetLinkMetaInfoImpl(this.mNativeHandle, str);
        if (FuzzyGetLinkMetaInfoImpl == null) {
            return null;
        }
        try {
            return CrawlLinkMetaInfo.parseFrom(FuzzyGetLinkMetaInfoImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    private boolean isAvailableUrl(@NonNull String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append(ZMDomainUtil.getMainDomain());
            sb.append("/j/");
            if (!str.contains(sb.toString())) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(ZMDomainUtil.getMainDomain());
                sb2.append("/s/");
                if (!str.contains(sb2.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLinkPreviewEnable() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        return currentUserProfile != null && currentUserProfile.isEnableLinkPreview();
    }

    public boolean NeedDownloadImage(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return NeedDownloadImageImpl(j, str);
    }

    public boolean NeedDownloadFavicon(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return NeedDownloadFaviconImpl(j, str);
    }

    @Nullable
    public String DownloadImage(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return DownloadImageImpl(j, str, str2);
    }

    @Nullable
    public String DownloadFavicon(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return DownloadFaviconImpl(j, str, str2);
    }

    public boolean sendLinkMetaInfo(String str, String str2, List<String> list) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2) || CollectionsUtil.isListEmpty(list)) {
            return false;
        }
        return sendLinkMetaInfoImpl(this.mNativeHandle, str, str2, list);
    }
}
