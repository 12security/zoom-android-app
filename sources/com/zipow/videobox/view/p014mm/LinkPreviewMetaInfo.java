package com.zipow.videobox.view.p014mm;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.CrawlLinkMetaInfo;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.view.mm.LinkPreviewMetaInfo */
public class LinkPreviewMetaInfo {
    private String desc;
    private String favicon;
    private String faviconPath;
    private String imagePath;
    private String imgUrl;
    private String msgGuid;
    private String sessionId;
    private String siteName;
    private String title;
    private String type;
    private String url;
    private String videoPath;
    private String videoUrl;

    @Nullable
    public static LinkPreviewMetaInfo createFromProto(@Nullable CrawlLinkMetaInfo crawlLinkMetaInfo, String str, String str2) {
        if (crawlLinkMetaInfo == null || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return null;
        }
        LinkPreviewMetaInfo linkPreviewMetaInfo = new LinkPreviewMetaInfo();
        linkPreviewMetaInfo.url = crawlLinkMetaInfo.getUrl();
        linkPreviewMetaInfo.siteName = crawlLinkMetaInfo.getSiteName();
        linkPreviewMetaInfo.title = crawlLinkMetaInfo.getTitle();
        linkPreviewMetaInfo.type = crawlLinkMetaInfo.getType();
        linkPreviewMetaInfo.desc = crawlLinkMetaInfo.getDesc();
        linkPreviewMetaInfo.imgUrl = crawlLinkMetaInfo.getImgUrl();
        linkPreviewMetaInfo.videoUrl = crawlLinkMetaInfo.getVideoUrl();
        linkPreviewMetaInfo.favicon = crawlLinkMetaInfo.getFavicon();
        linkPreviewMetaInfo.imagePath = crawlLinkMetaInfo.getImagePath();
        linkPreviewMetaInfo.videoPath = crawlLinkMetaInfo.getVideoPath();
        linkPreviewMetaInfo.faviconPath = crawlLinkMetaInfo.getFaviconPath();
        linkPreviewMetaInfo.sessionId = str;
        linkPreviewMetaInfo.msgGuid = str2;
        return linkPreviewMetaInfo;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getSiteName() {
        return this.siteName;
    }

    public void setSiteName(String str) {
        this.siteName = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String str) {
        this.imgUrl = str;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String str) {
        this.videoUrl = str;
    }

    public String getFavicon() {
        return this.favicon;
    }

    public void setFavicon(String str) {
        this.favicon = str;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String str) {
        this.imagePath = str;
    }

    public String getVideoPath() {
        return this.videoPath;
    }

    public void setVideoPath(String str) {
        this.videoPath = str;
    }

    public String getFaviconPath() {
        return this.faviconPath;
    }

    public void setFaviconPath(String str) {
        this.faviconPath = str;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String str) {
        this.sessionId = str;
    }

    public String getMsgGuid() {
        return this.msgGuid;
    }

    public void setMsgGuid(String str) {
        this.msgGuid = str;
    }
}
