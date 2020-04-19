package com.zipow.videobox.util;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.IPBXMessageSession;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

public class TextCommandHelper {
    public static final char CHANNEL_CMD_CHAR = '#';
    public static final char REPLY_AT_CHAR = '@';
    public static final char SLASH_CMD_CHAR = '/';
    public static final int TYPE_AT = 2;
    public static final int TYPE_CHANNEL = 3;
    public static final int TYPE_COMMON = 0;
    public static final int TYPE_SLASH = 1;
    private static TextCommandHelper mInstance;

    public static class AtSpan extends BackgroundColorSpan {
        public static final Creator<AtSpan> CREATOR = new Creator<AtSpan>() {
            public AtSpan createFromParcel(@NonNull Parcel parcel) {
                return new AtSpan(parcel);
            }

            public AtSpan[] newArray(int i) {
                return new AtSpan[i];
            }
        };
        @Nullable
        public String jId;
        @Nullable
        public String label;

        private AtSpan(Parcel parcel) {
            this();
            this.jId = parcel.readString();
            this.label = parcel.readString();
        }

        public AtSpan() {
            super(0);
        }

        public AtSpan(@Nullable SpanBean spanBean) {
            this();
            if (spanBean != null) {
                this.jId = spanBean.getJid();
                this.label = spanBean.getLabel();
            }
        }
    }

    public static class ChannelSpan extends BackgroundColorSpan {
        public static final Creator<ChannelSpan> CREATOR = new Creator<ChannelSpan>() {
            public ChannelSpan createFromParcel(@NonNull Parcel parcel) {
                return new ChannelSpan(parcel);
            }

            public ChannelSpan[] newArray(int i) {
                return new ChannelSpan[i];
            }
        };
        @Nullable
        public String groupId;
        @Nullable
        public String label;

        public ChannelSpan() {
            super(0);
        }

        public ChannelSpan(@NonNull Parcel parcel) {
            this();
            this.groupId = parcel.readString();
            this.label = parcel.readString();
        }

        public ChannelSpan(@Nullable String str, @Nullable String str2) {
            this();
            this.groupId = str;
            this.label = str2;
        }
    }

    public static class DraftBean {
        /* access modifiers changed from: private */
        public long draftTime;
        /* access modifiers changed from: private */
        public String label;
        @Nullable
        private List<SpanBean> spans;

        public DraftBean(String str, long j) {
            this(str, j, null);
        }

        public DraftBean(String str, long j, @Nullable List<SpanBean> list) {
            if (list != null && list.size() <= 0) {
                list = null;
            }
            this.label = str;
            this.draftTime = j;
            this.spans = list;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String str) {
            this.label = str;
        }

        public long getDraftTime() {
            return this.draftTime;
        }

        public void setDraftTime(long j) {
            this.draftTime = j;
        }

        @Nullable
        public List<SpanBean> getSpans() {
            return this.spans;
        }

        public void setSpans(@Nullable List<SpanBean> list) {
            this.spans = list;
        }
    }

    public static class ParamSpan extends BackgroundColorSpan {
        public ParamSpan(int i) {
            super(i);
        }

        public ParamSpan(@NonNull Parcel parcel) {
            super(parcel);
        }
    }

    public static class SlashSpan extends BackgroundColorSpan {
        public static final Creator<SlashSpan> CREATOR = new Creator<SlashSpan>() {
            public SlashSpan createFromParcel(@NonNull Parcel parcel) {
                return new SlashSpan(parcel);
            }

            public SlashSpan[] newArray(int i) {
                return new SlashSpan[i];
            }
        };
        @Nullable
        public String jId;
        @Nullable
        public String label;

        private SlashSpan(Parcel parcel) {
            this();
            this.jId = parcel.readString();
            this.label = parcel.readString();
        }

        public SlashSpan() {
            super(0);
        }

        public SlashSpan(@Nullable SpanBean spanBean) {
            this();
            if (spanBean != null) {
                this.jId = spanBean.getJid();
                this.label = spanBean.getLabel();
            }
        }
    }

    public static class SpanBean {
        private int end;
        private String jid;
        private String label;
        private int start;
        private int type;

        public SpanBean(int i, int i2, int i3, String str, String str2) {
            this.start = i2;
            this.end = i3;
            this.label = str;
            this.jid = str2;
            this.type = i;
        }

        public SpanBean(int i, int i2, @Nullable AtSpan atSpan) {
            this(2, i, i2, atSpan == null ? "" : atSpan.label, atSpan == null ? "" : atSpan.jId);
        }

        public SpanBean(int i, int i2, @Nullable SlashSpan slashSpan) {
            this(1, i, i2, slashSpan == null ? "" : slashSpan.label, slashSpan == null ? "" : slashSpan.jId);
        }

        public SpanBean(int i, int i2, @Nullable ChannelSpan channelSpan) {
            this(3, i, i2, channelSpan == null ? "" : channelSpan.label, channelSpan == null ? "" : channelSpan.groupId);
        }

        public int getStart() {
            return this.start;
        }

        public void setStart(int i) {
            this.start = i;
        }

        public int getEnd() {
            return this.end;
        }

        public void setEnd(int i) {
            this.end = i;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String str) {
            this.label = str;
        }

        public String getJid() {
            return this.jid;
        }

        public void setJid(String str) {
            this.jid = str;
        }

        public int getType() {
            return this.type;
        }

        public void setType(int i) {
            this.type = i;
        }
    }

    private TextCommandHelper() {
    }

    public boolean isSlashCommand(@Nullable Spanned spanned) {
        if (spanned == null) {
            return false;
        }
        SlashSpan[] slashSpanArr = (SlashSpan[]) spanned.getSpans(0, spanned.length(), SlashSpan.class);
        if (slashSpanArr == null || slashSpanArr.length <= 0) {
            return false;
        }
        return true;
    }

    public boolean hasCommandParam(@Nullable Spanned spanned) {
        if (spanned == null) {
            return false;
        }
        ParamSpan[] paramSpanArr = (ParamSpan[]) spanned.getSpans(0, spanned.length(), ParamSpan.class);
        if (paramSpanArr == null || paramSpanArr.length <= 0) {
            return false;
        }
        return true;
    }

    public void clearCommandParam(CharSequence charSequence, int i, int i2, int i3, @Nullable Editable editable) {
        if (editable != null) {
            int i4 = 0;
            ParamSpan[] paramSpanArr = (ParamSpan[]) editable.getSpans(0, editable.length(), ParamSpan.class);
            if (paramSpanArr != null && paramSpanArr.length > 0) {
                SlashSpan[] slashSpanArr = (SlashSpan[]) editable.getSpans(0, editable.length(), SlashSpan.class);
                if (slashSpanArr == null || slashSpanArr.length <= 0) {
                    int length = paramSpanArr.length;
                    while (i4 < length) {
                        editable.removeSpan(paramSpanArr[i4]);
                        i4++;
                    }
                    return;
                }
                ArrayList<SlashSpan> arrayList = new ArrayList<>();
                for (SlashSpan slashSpan : slashSpanArr) {
                    int spanEnd = editable.getSpanEnd(slashSpan);
                    int spanStart = editable.getSpanStart(slashSpan);
                    if (spanStart >= 0 && spanEnd >= 0 && spanStart == 0 && editable.charAt(spanStart) == '/' && editable.charAt(spanEnd - 1) == ' ') {
                        arrayList.add(slashSpan);
                    }
                }
                if (arrayList.isEmpty()) {
                    int length2 = paramSpanArr.length;
                    while (i4 < length2) {
                        editable.removeSpan(paramSpanArr[i4]);
                        i4++;
                    }
                } else {
                    for (SlashSpan spanEnd2 : arrayList) {
                        int spanEnd3 = editable.getSpanEnd(spanEnd2);
                        if (i >= spanEnd3) {
                            int indexOf = editable.toString().indexOf(91, spanEnd3);
                            for (ParamSpan removeSpan : paramSpanArr) {
                                editable.removeSpan(removeSpan);
                            }
                            if (i < indexOf) {
                                editable.replace(indexOf, editable.length(), "");
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean channelCommandAction(@NonNull CharSequence charSequence, int i, int i2, int i3, Spanned spanned, int i4) {
        if (charSequence.length() <= i4 || i3 - i2 != 1 || isSlashCommand(spanned) || (charSequence.charAt(i) != '#' && charSequence.charAt(charSequence.length() - 1) != '#')) {
            return false;
        }
        return true;
    }

    public boolean isChannelCommand(@Nullable Spanned spanned) {
        if (spanned == null) {
            return false;
        }
        ChannelSpan[] channelSpanArr = (ChannelSpan[]) spanned.getSpans(0, spanned.length(), ChannelSpan.class);
        if (channelSpanArr == null || channelSpanArr.length <= 0) {
            return false;
        }
        return true;
    }

    public boolean slashCommandAction(@NonNull CharSequence charSequence, int i, int i2, int i3, Spanned spanned) {
        if (charSequence.length() == 1 && i3 == 1 && !isSlashCommand(spanned) && charSequence.charAt(i) == '/') {
            return true;
        }
        return false;
    }

    public boolean isAtCommand(@Nullable Spanned spanned) {
        if (spanned == null) {
            return false;
        }
        AtSpan[] atSpanArr = (AtSpan[]) spanned.getSpans(0, spanned.length(), AtSpan.class);
        if (atSpanArr == null || atSpanArr.length <= 0) {
            return false;
        }
        return true;
    }

    public boolean atCommandAction(@NonNull CharSequence charSequence, int i, int i2, int i3, Spanned spanned, int i4) {
        if (charSequence.length() <= i4 || i3 - i2 != 1 || isSlashCommand(spanned) || (charSequence.charAt(i) != '@' && charSequence.charAt(charSequence.length() - 1) != '@')) {
            return false;
        }
        return true;
    }

    public void checkAndStoreSlashCommand(String str, @Nullable Editable editable) {
        checkAndStoreSlashCommand(str, null, editable);
    }

    public void checkAndStoreSlashCommand(String str, String str2, @Nullable Editable editable) {
        if (editable != null && editable.length() > 0) {
            SlashSpan[] slashSpanArr = (SlashSpan[]) editable.getSpans(0, editable.length(), SlashSpan.class);
            if (slashSpanArr != null && slashSpanArr.length > 0) {
                ArrayList arrayList = new ArrayList();
                for (SlashSpan slashSpan : slashSpanArr) {
                    int spanEnd = editable.getSpanEnd(slashSpan);
                    int spanStart = editable.getSpanStart(slashSpan);
                    if (spanStart < 0 || spanEnd < 0) {
                        editable.removeSpan(slashSpan);
                    } else {
                        arrayList.add(new SpanBean(spanStart, spanEnd, slashSpan));
                        if (spanStart != 0) {
                            editable.removeSpan(slashSpan);
                        } else if (editable.charAt(spanStart) != '/') {
                            editable.removeSpan(slashSpan);
                        } else if (editable.charAt(spanEnd - 1) != ' ') {
                            editable.removeSpan(slashSpan);
                            editable.delete(spanStart, spanEnd);
                        }
                    }
                }
                storeTextCommand(false, str, str2, editable.toString(), arrayList);
            }
        }
    }

    public void checkAndStoreNonSlashCommand(String str, @Nullable Editable editable) {
        checkAndStoreNonSlashCommand(str, null, editable);
    }

    public void checkAndStoreNonSlashCommand(String str, String str2, @Nullable Editable editable) {
        if (editable != null && editable.length() > 0) {
            AtSpan[] atSpanArr = (AtSpan[]) editable.getSpans(0, editable.length(), AtSpan.class);
            ChannelSpan[] channelSpanArr = (ChannelSpan[]) editable.getSpans(0, editable.length(), ChannelSpan.class);
            ArrayList arrayList = new ArrayList();
            if (atSpanArr != null && atSpanArr.length > 0) {
                for (AtSpan atSpan : atSpanArr) {
                    int spanEnd = editable.getSpanEnd(atSpan);
                    int spanStart = editable.getSpanStart(atSpan);
                    if (spanStart < 0 || spanEnd < 0) {
                        editable.removeSpan(atSpan);
                    } else {
                        arrayList.add(new SpanBean(spanStart, spanEnd, atSpan));
                        if (editable.charAt(spanStart) != '@') {
                            editable.removeSpan(atSpan);
                        } else if (editable.charAt(spanEnd - 1) != ' ') {
                            editable.removeSpan(atSpan);
                            editable.delete(spanStart, spanEnd);
                        }
                    }
                }
            }
            if (channelSpanArr != null && channelSpanArr.length > 0) {
                for (ChannelSpan channelSpan : channelSpanArr) {
                    int spanEnd2 = editable.getSpanEnd(channelSpan);
                    int spanStart2 = editable.getSpanStart(channelSpan);
                    if (spanStart2 < 0 || spanEnd2 < 0) {
                        editable.removeSpan(channelSpan);
                    } else {
                        arrayList.add(new SpanBean(spanStart2, spanEnd2, channelSpan));
                        if (editable.charAt(spanStart2) != '#') {
                            editable.removeSpan(channelSpan);
                        } else if (editable.charAt(spanEnd2 - 1) != ' ') {
                            editable.removeSpan(channelSpan);
                            editable.delete(spanStart2, spanEnd2);
                        }
                    }
                }
            }
            storeTextCommand(false, str, str2, editable.toString(), arrayList);
        }
    }

    public static synchronized TextCommandHelper getInstance() {
        synchronized (TextCommandHelper.class) {
            if (mInstance == null) {
                TextCommandHelper textCommandHelper = new TextCommandHelper();
                return textCommandHelper;
            }
            TextCommandHelper textCommandHelper2 = mInstance;
            return textCommandHelper2;
        }
    }

    public void clearTextCommand(boolean z, String str) {
        storeTextCommand(z, str, null, null);
    }

    public void clearTextCommand(boolean z, String str, String str2) {
        storeTextCommand(z, str, str2, null);
    }

    public void storeText(boolean z, String str, String str2) {
        storeTextCommand(z, str, null, str2, null);
    }

    public void storeText(boolean z, String str, String str2, String str3) {
        storeTextCommand(z, str, str2, str3, null);
    }

    public void storeTextCommand(boolean z, String str, @Nullable String str2, @Nullable String str3, List<SpanBean> list) {
        if (str3 != null && !TextUtils.isEmpty(str3)) {
            storeTextCommand(z, str, str2, new DraftBean(str3, CmmTime.getMMNow(), list));
        }
    }

    public void storeTextCommand(boolean z, String str, @Nullable String str2, @Nullable DraftBean draftBean) {
        String str3;
        String str4;
        long j;
        String str5;
        if (!TextUtils.isEmpty(str)) {
            if (!(draftBean == null || draftBean.label == null || draftBean.label.trim().length() != 0)) {
                draftBean.label = "";
            }
            if (z) {
                IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(str);
                if (sessionById != null) {
                    if (draftBean == null) {
                        str5 = "";
                    } else {
                        str5 = new Gson().toJson((Object) draftBean);
                    }
                    sessionById.setDraftText(str5);
                    CmmSIPMessageManager.getInstance().notifySessionUpdated(str);
                }
            } else {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (StringUtil.isEmptyOrNull(str2)) {
                        ZoomChatSession sessionById2 = zoomMessenger.getSessionById(str);
                        if (sessionById2 != null) {
                            if (draftBean == null) {
                                str4 = null;
                            } else {
                                str4 = new Gson().toJson((Object) draftBean);
                            }
                            sessionById2.storeMessageDraft(str4);
                            if (draftBean == null) {
                                j = 0;
                            } else {
                                j = draftBean.draftTime / 1000;
                            }
                            sessionById2.storeMessageDraftTime(j);
                        }
                    } else {
                        ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                        if (threadDataProvider != null) {
                            if (draftBean == null) {
                                str3 = "";
                            } else {
                                str3 = new Gson().toJson((Object) draftBean);
                            }
                            threadDataProvider.setThreadReplyDraft(str, str2, str3);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    public DraftBean restoreTextCommand(boolean z, String str) {
        return restoreTextCommand(z, str, null);
    }

    @Nullable
    public DraftBean restoreTextCommand(boolean z, String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (z) {
            IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(str);
            if (sessionById == null) {
                return null;
            }
            String draftText = sessionById.getDraftText();
            if (TextUtils.isEmpty(draftText)) {
                return null;
            }
            try {
                return (DraftBean) new Gson().fromJson(draftText, DraftBean.class);
            } catch (Exception unused) {
                return null;
            }
        } else {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return null;
            }
            if (StringUtil.isEmptyOrNull(str2)) {
                ZoomChatSession sessionById2 = zoomMessenger.getSessionById(str);
                if (sessionById2 == null) {
                    return null;
                }
                String messageDraft = sessionById2.getMessageDraft();
                if (TextUtils.isEmpty(messageDraft)) {
                    return null;
                }
                if (sessionById2.getMessageDraftTime() != 0) {
                    return (DraftBean) new Gson().fromJson(messageDraft, DraftBean.class);
                }
                clearTextCommand(false, str);
                return null;
            }
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider == null) {
                return null;
            }
            String threadReplyDraft = threadDataProvider.getThreadReplyDraft(str, str2);
            if (StringUtil.isEmptyOrNull(threadReplyDraft)) {
                return null;
            }
            return (DraftBean) new Gson().fromJson(threadReplyDraft, DraftBean.class);
        }
    }
}
