package com.zipow.videobox.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.ptapp.p013mm.CrawlerLinkPreview;
import com.zipow.videobox.view.p014mm.LinkPreviewMetaInfo;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class LinkPreviewHelper {
    private static final ArrayList<String> linkPreviewMessageIdCache = new ArrayList<>();
    private static final HashMap<String, Entry> linkPreviewUrlCache = new HashMap<>();

    static class Entry {
        long time;
        String xmppId;

        public long getTime() {
            return this.time;
        }

        public String getXmppId() {
            return this.xmppId;
        }

        public Entry(long j, String str) {
            this.time = j;
            this.xmppId = str;
        }

        public void setTime(long j) {
            this.time = j;
        }

        public void setXmppId(String str) {
            this.xmppId = str;
        }
    }

    public static void doCrawLinkPreview(String str, String str2, @Nullable CharSequence charSequence) {
        if (charSequence != null && !StringUtil.isEmptyOrNull(str2)) {
            CrawlerLinkPreview linkCrawler = PTApp.getInstance().getLinkCrawler();
            if (linkCrawler != null && linkCrawler.isLinkPreviewEnable()) {
                List urls = StringUtil.getUrls(charSequence);
                if (!CollectionsUtil.isListEmpty(urls) && urls.size() <= 4) {
                    boolean z = true;
                    Iterator it = urls.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (linkCrawler.FuzzyGetLinkMetaInfo((String) it.next()) == null) {
                                z = false;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (z) {
                        linkCrawler.sendLinkMetaInfo(str, str2, urls);
                    } else {
                        linkCrawler.CrawlLinkMetaInfo(str, str2, urls);
                    }
                }
            }
        }
    }

    public static List<String> downloadLinkPreview(@Nullable MMMessageItem mMMessageItem) {
        addLinkPreview(mMMessageItem);
        if (mMMessageItem == null || CollectionsUtil.isListEmpty(mMMessageItem.linkPreviewMetaInfos) || StringUtil.isEmptyOrNull(mMMessageItem.messageId)) {
            return null;
        }
        CrawlerLinkPreview linkCrawler = PTApp.getInstance().getLinkCrawler();
        if (linkCrawler == null) {
            return null;
        }
        boolean isImLlinkPreviewDescription = PTSettingHelper.isImLlinkPreviewDescription();
        ArrayList arrayList = new ArrayList();
        for (LinkPreviewMetaInfo linkPreviewMetaInfo : mMMessageItem.linkPreviewMetaInfos) {
            if (!new File(linkPreviewMetaInfo.getFaviconPath()).exists() && linkCrawler.NeedDownloadFavicon(linkPreviewMetaInfo.getUrl())) {
                String DownloadFavicon = linkCrawler.DownloadFavicon(linkPreviewMetaInfo.getUrl(), PendingFileDataHelper.getContenFileRandomPath());
                if (!StringUtil.isEmptyOrNull(DownloadFavicon)) {
                    arrayList.add(DownloadFavicon);
                }
            }
            if (isImLlinkPreviewDescription && !new File(linkPreviewMetaInfo.getImagePath()).exists() && linkCrawler.NeedDownloadImage(linkPreviewMetaInfo.getUrl())) {
                String DownloadImage = linkCrawler.DownloadImage(linkPreviewMetaInfo.getUrl(), PendingFileDataHelper.getContenFileRandomPath());
                if (!StringUtil.isEmptyOrNull(DownloadImage)) {
                    arrayList.add(DownloadImage);
                }
            }
        }
        return arrayList;
    }

    @NonNull
    private static String getUrlWithourSlash(@NonNull String str) {
        if (isZoomURL(str)) {
            str = ZMDomainUtil.getDefaultWebDomain();
        }
        return str.endsWith("/") ? str.substring(0, str.length() - 1) : str;
    }

    private static boolean isZoomURL(String str) {
        return str.matches("(https?://)?zoom\\.us/?");
    }

    public static void deleteLinkPreview(String str) {
        linkPreviewMessageIdCache.remove(str);
        Iterator it = linkPreviewUrlCache.entrySet().iterator();
        while (it.hasNext()) {
            if (((Entry) ((java.util.Map.Entry) it.next()).getValue()).getXmppId().equals(str)) {
                it.remove();
            }
        }
    }

    public static void editLinkPreview(@Nullable MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            deleteLinkPreview(mMMessageItem.messageId);
            addLinkPreview(mMMessageItem);
        }
    }

    @Nullable
    private static MMMessageItem addLinkPreview(@Nullable MMMessageItem mMMessageItem) {
        if (mMMessageItem == null) {
            return null;
        }
        int i = mMMessageItem.messageType;
        if (i == 34 || i == 35) {
            List<LinkPreviewMetaInfo> list = mMMessageItem.linkPreviewMetaInfos;
            long j = mMMessageItem.messageTime;
            if (!linkPreviewMessageIdCache.contains(mMMessageItem.messageXMPPId)) {
                linkPreviewMessageIdCache.add(mMMessageItem.messageXMPPId);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    String urlWithourSlash = getUrlWithourSlash(((LinkPreviewMetaInfo) it.next()).getUrl());
                    if (!linkPreviewUrlCache.containsKey(urlWithourSlash)) {
                        linkPreviewUrlCache.put(urlWithourSlash, new Entry(j, mMMessageItem.messageXMPPId));
                    } else if (j - ((Entry) linkPreviewUrlCache.get(urlWithourSlash)).getTime() > 3600000) {
                        linkPreviewUrlCache.put(urlWithourSlash, new Entry(j, mMMessageItem.messageXMPPId));
                    } else {
                        it.remove();
                    }
                }
            } else {
                HashSet hashSet = new HashSet();
                for (int size = list.size() - 1; size >= 0; size--) {
                    String urlWithourSlash2 = getUrlWithourSlash(((LinkPreviewMetaInfo) list.get(size)).getUrl());
                    if (!linkPreviewUrlCache.containsKey(urlWithourSlash2)) {
                        linkPreviewUrlCache.put(urlWithourSlash2, new Entry(j, mMMessageItem.messageXMPPId));
                    } else if (mMMessageItem.messageXMPPId != null && !mMMessageItem.messageXMPPId.equals(((Entry) linkPreviewUrlCache.get(urlWithourSlash2)).getXmppId())) {
                        list.remove(size);
                    } else if (!hashSet.contains(urlWithourSlash2)) {
                        hashSet.add(urlWithourSlash2);
                    } else {
                        list.remove(size);
                    }
                }
            }
        }
        return mMMessageItem;
    }
}
