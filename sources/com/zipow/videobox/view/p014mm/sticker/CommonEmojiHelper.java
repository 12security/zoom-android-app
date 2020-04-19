package com.zipow.videobox.view.p014mm.sticker;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxFile;
import com.box.androidsdk.content.models.BoxList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.EmojiHelper;
import com.zipow.videobox.util.EmojiHelper.ZoomEmojiSpan;
import com.zipow.videobox.util.PreferenceUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.http.cookie.ClientCookie;
import org.json.JSONObject;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmojiHelper */
public class CommonEmojiHelper {
    private static final String EMOJI_ONE_PATH = "emoji_one_path";
    private static final Pattern EMOJI_PATTERN = Pattern.compile("[🀀-🏿]|[🐀-🟿]|[☀-⟿]", 66);
    private static final int MAX_FREQUENT_USED_EMOJI = 15;
    private static final Pattern SHORTNAME_PATTERN = Pattern.compile(":([-+\\w]+):");
    private static final String TAG = "CommonEmojiHelper";
    private static CommonEmojiHelper mInstance;
    private static final HashMap<String, String> mShortNameToUnicode = new HashMap<>();
    @NonNull
    private Runnable mCheckDownloadRunnable = new Runnable() {
        public void run() {
            CommonEmojiHelper.this.onDowloadChange();
        }
    };
    @NonNull
    private List<EmojiCategory> mEmojiCategories = new ArrayList();
    @NonNull
    private Map<String, CommonEmoji> mEmojiOneUnicodes = new HashMap();
    /* access modifiers changed from: private */
    public Typeface mEmojiTypeface;
    @NonNull
    private List<String> mFrequentUsedEmoji = new ArrayList();
    private EmojiCategory mFrequentlyUsedEmojiCategory;
    @NonNull
    private Handler mHandle = new Handler();
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    @NonNull
    private Map<Character, MatchEmojiBean> mMatchedEmojiOneUnicodes = new HashMap();
    @Nullable
    private String mUpgradeReqId;
    @NonNull
    private IZoomMessengerUIListener messengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_DownloadFileByUrlIml(String str, int i) {
            CommonEmojiHelper.this.Indicate_DownloadFileByUrlIml(str, i);
        }
    };

    /* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmojiHelper$CommonEmojiSpan */
    static class CommonEmojiSpan extends TypefaceSpan {
        public static final Creator<CommonEmojiSpan> CREATOR = new Creator<CommonEmojiSpan>() {
            public CommonEmojiSpan createFromParcel(@NonNull Parcel parcel) {
                return new CommonEmojiSpan(parcel);
            }

            public CommonEmojiSpan[] newArray(int i) {
                return new CommonEmojiSpan[i];
            }
        };

        public CommonEmojiSpan(@NonNull Parcel parcel) {
            super(parcel);
        }

        public CommonEmojiSpan() {
            super("CommonEomji");
        }

        public void updateDrawState(@NonNull TextPaint textPaint) {
            apply(textPaint);
        }

        public void updateMeasureState(@NonNull TextPaint textPaint) {
            apply(textPaint);
        }

        private static void apply(@NonNull Paint paint) {
            Typeface access$200 = CommonEmojiHelper.getInstance().mEmojiTypeface;
            if (access$200 != null) {
                paint.setTypeface(access$200);
            }
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmojiHelper$EmojiComparetor */
    private static class EmojiComparetor implements Comparator<CommonEmoji> {
        private EmojiComparetor() {
        }

        public int compare(@NonNull CommonEmoji commonEmoji, @NonNull CommonEmoji commonEmoji2) {
            return commonEmoji.getOrder() - commonEmoji2.getOrder();
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmojiHelper$MatchEmojiBean */
    private static class MatchEmojiBean {
        int emojiMaxLength;
        @NonNull
        Map<String, CommonEmoji> emojis;

        private MatchEmojiBean() {
            this.emojis = new HashMap();
            this.emojiMaxLength = 0;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmojiHelper$OnEmojiPackageInstallListener */
    public interface OnEmojiPackageInstallListener extends IListener {
        void onEmojiPkgDownload(int i);

        void onEmojiPkgDownloadFailed();

        void onEmojiPkgInstalled();
    }

    /* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmojiHelper$ZMEmojiSpannableStringBuilder */
    public static class ZMEmojiSpannableStringBuilder extends SpannableStringBuilder {
        public ZMEmojiSpannableStringBuilder(CharSequence charSequence) {
            super(charSequence);
        }
    }

    private void addZoomBuildInEmojis() {
    }

    public CommonEmoji getCommonEmojiByKey(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        return (CommonEmoji) this.mEmojiOneUnicodes.get(str);
    }

    public List<String> getFrequentUsedEmojiKeys() {
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.FREQUENTLY_USED_EMOJI, null);
        if (StringUtil.isEmptyOrNull(readStringValue)) {
            return null;
        }
        return (List) new Gson().fromJson(readStringValue, ArrayList.class);
    }

    private void getFrequentUsedEmoji() {
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.FREQUENTLY_USED_EMOJI, null);
        if (!StringUtil.isEmptyOrNull(readStringValue)) {
            List<String> list = (List) new Gson().fromJson(readStringValue, ArrayList.class);
            if (list != null) {
                this.mFrequentUsedEmoji = list;
            }
            updateEmojis4FrequentEmoji();
        }
    }

    public void addFrequentUsedEmoji(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mFrequentUsedEmoji.remove(str);
            this.mFrequentUsedEmoji.add(0, str);
            if (this.mFrequentUsedEmoji.size() > 15) {
                this.mFrequentUsedEmoji = this.mFrequentUsedEmoji.subList(0, 15);
            }
            PreferenceUtil.saveStringValue(PreferenceUtil.FREQUENTLY_USED_EMOJI, new Gson().toJson((Object) this.mFrequentUsedEmoji));
            updateEmojis4FrequentEmoji();
        }
    }

    private void updateEmojis4FrequentEmoji() {
        if (!CollectionsUtil.isCollectionEmpty(this.mFrequentUsedEmoji)) {
            if (this.mFrequentlyUsedEmojiCategory == null) {
                Context globalContext = VideoBoxApplication.getGlobalContext();
                if (globalContext != null) {
                    this.mFrequentlyUsedEmojiCategory = new EmojiCategory();
                    this.mFrequentlyUsedEmojiCategory.setIconResource(C4558R.C4559drawable.zm_mm_emoji_category_recent);
                    this.mFrequentlyUsedEmojiCategory.setLabel(globalContext.getResources().getString(C4558R.string.zm_lbl_frequently_used_88133));
                    this.mFrequentlyUsedEmojiCategory.setName(globalContext.getResources().getString(C4558R.string.zm_lbl_frequently_used_88133));
                    this.mEmojiCategories.add(0, this.mFrequentlyUsedEmojiCategory);
                } else {
                    return;
                }
            }
            List emojis = this.mFrequentlyUsedEmojiCategory.getEmojis();
            emojis.clear();
            for (String str : this.mFrequentUsedEmoji) {
                CommonEmoji commonEmoji = (CommonEmoji) this.mEmojiOneUnicodes.get(str);
                if (commonEmoji != null) {
                    emojis.add(commonEmoji);
                }
            }
            Context globalContext2 = VideoBoxApplication.getGlobalContext();
            if (globalContext2 != null) {
                int ceil = ((int) Math.ceil(((double) this.mFrequentUsedEmoji.size()) / 5.0d)) * (UIUtil.sp2px(globalContext2, 22.0f) + UIUtil.dip2px(globalContext2, 10.0f));
                TextPaint textPaint = new TextPaint();
                textPaint.setTextSize((float) UIUtil.sp2px(globalContext2, 12.0f));
                int ceil2 = ((int) Math.ceil((double) (((textPaint.measureText(globalContext2.getText(C4558R.string.zm_lbl_frequently_used_88133).toString()) + ((float) UIUtil.dip2px(globalContext2, 30.0f))) - ((float) ceil)) / ((float) UIUtil.dip2px(globalContext2, 10.0f))))) - 1;
                if (ceil2 > 0) {
                    for (int i = 0; i < ceil2 * 5; i++) {
                        emojis.add(new CommonEmoji());
                    }
                }
            }
        }
    }

    @NonNull
    public static String shortnameToUnicode(@NonNull String str) {
        Matcher matcher = SHORTNAME_PATTERN.matcher(str);
        while (matcher.find()) {
            String str2 = (String) mShortNameToUnicode.get(matcher.group(1));
            if (str2 != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(":");
                sb.append(matcher.group(1));
                sb.append(":");
                str = str.replace(sb.toString(), str2);
            }
        }
        return str;
    }

    private CommonEmojiHelper() {
        checkDownloadingPkg();
        parseConfigFile();
        checkUpgradePkg();
        getFrequentUsedEmoji();
    }

    public static synchronized CommonEmojiHelper getInstance() {
        CommonEmojiHelper commonEmojiHelper;
        synchronized (CommonEmojiHelper.class) {
            if (mInstance == null) {
                mInstance = new CommonEmojiHelper();
            }
            commonEmojiHelper = mInstance;
        }
        return commonEmojiHelper;
    }

    public synchronized Typeface getEmojiTypeFace() {
        return this.mEmojiTypeface;
    }

    public void addListener(@Nullable OnEmojiPackageInstallListener onEmojiPackageInstallListener) {
        if (onEmojiPackageInstallListener != null) {
            this.mListenerList.removeAll(onEmojiPackageInstallListener);
            this.mListenerList.add(onEmojiPackageInstallListener);
        }
    }

    public void removeListener(OnEmojiPackageInstallListener onEmojiPackageInstallListener) {
        this.mListenerList.remove(onEmojiPackageInstallListener);
    }

    public void parseConfigFile() {
        this.mEmojiCategories.clear();
        this.mEmojiOneUnicodes.clear();
        parseEmojiConfig();
        parseEmojiCategoryConfig();
        String commonEmojiTTFPath = getCommonEmojiTTFPath();
        if (OsUtil.isAtLeastKLP() && !StringUtil.isEmptyOrNull(commonEmojiTTFPath) && new File(commonEmojiTTFPath).exists()) {
            try {
                this.mEmojiTypeface = Typeface.createFromFile(commonEmojiTTFPath);
            } catch (Exception unused) {
                this.mEmojiCategories.clear();
                this.mEmojiOneUnicodes.clear();
            }
        }
    }

    @NonNull
    public List<EmojiCategory> getEmojiCategories() {
        return this.mEmojiCategories;
    }

    @Nullable
    public CharSequence formatImgEmojiSize(float f, CharSequence charSequence, boolean z) {
        ZMEmojiSpannableStringBuilder zMEmojiSpannableStringBuilder;
        if (TextUtils.isEmpty(charSequence)) {
            return null;
        }
        int i = (int) (f * 1.25f);
        if (z || !isTransferToEmoji(charSequence)) {
            zMEmojiSpannableStringBuilder = getInstance().tranToEmojiText(charSequence);
        } else {
            zMEmojiSpannableStringBuilder = (ZMEmojiSpannableStringBuilder) charSequence;
        }
        if (zMEmojiSpannableStringBuilder == null) {
            return null;
        }
        ZoomEmojiSpan[] zoomEmojiSpanArr = (ZoomEmojiSpan[]) zMEmojiSpannableStringBuilder.getSpans(0, zMEmojiSpannableStringBuilder.length(), ZoomEmojiSpan.class);
        if (zoomEmojiSpanArr != null) {
            for (ZoomEmojiSpan updateSize : zoomEmojiSpanArr) {
                updateSize.updateSize(i, i);
            }
        }
        return zMEmojiSpannableStringBuilder;
    }

    public boolean isAllEmojis(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return false;
        }
        if (!isTransferToEmoji(charSequence)) {
            charSequence = tranToEmojiText(charSequence);
        }
        if (charSequence == null) {
            return false;
        }
        boolean[] zArr = new boolean[charSequence.length()];
        SpannableString spannableString = new SpannableString(charSequence);
        CommonEmojiSpan[] commonEmojiSpanArr = (CommonEmojiSpan[]) spannableString.getSpans(0, charSequence.length(), CommonEmojiSpan.class);
        if (commonEmojiSpanArr != null) {
            for (CommonEmojiSpan commonEmojiSpan : commonEmojiSpanArr) {
                int spanEnd = spannableString.getSpanEnd(commonEmojiSpan);
                for (int spanStart = spannableString.getSpanStart(commonEmojiSpan); spanStart < spanEnd; spanStart++) {
                    zArr[spanStart] = true;
                }
            }
        }
        ZoomEmojiSpan[] zoomEmojiSpanArr = (ZoomEmojiSpan[]) spannableString.getSpans(0, spannableString.length(), ZoomEmojiSpan.class);
        if (commonEmojiSpanArr != null) {
            for (ZoomEmojiSpan zoomEmojiSpan : zoomEmojiSpanArr) {
                int spanEnd2 = spannableString.getSpanEnd(zoomEmojiSpan);
                for (int spanStart2 = spannableString.getSpanStart(zoomEmojiSpan); spanStart2 < spanEnd2; spanStart2++) {
                    zArr[spanStart2] = true;
                }
            }
        }
        for (boolean z : zArr) {
            if (!z) {
                return false;
            }
        }
        return true;
    }

    public boolean isTransferToEmoji(CharSequence charSequence) {
        return charSequence instanceof ZMEmojiSpannableStringBuilder;
    }

    @Nullable
    public ZMEmojiSpannableStringBuilder tranToEmojiText(@Nullable CharSequence charSequence) {
        if (charSequence == null || charSequence.length() == 0 || this.mEmojiTypeface == null) {
            return EmojiHelper.getInstance().tranToEmojiText(charSequence);
        }
        ZMEmojiSpannableStringBuilder zMEmojiSpannableStringBuilder = new ZMEmojiSpannableStringBuilder(charSequence);
        int i = 0;
        CommonEmojiSpan[] commonEmojiSpanArr = (CommonEmojiSpan[]) zMEmojiSpannableStringBuilder.getSpans(0, charSequence.length(), CommonEmojiSpan.class);
        if (commonEmojiSpanArr != null) {
            for (CommonEmojiSpan removeSpan : commonEmojiSpanArr) {
                zMEmojiSpannableStringBuilder.removeSpan(removeSpan);
            }
        }
        while (i < charSequence.length()) {
            MatchEmojiBean matchEmojiBean = (MatchEmojiBean) this.mMatchedEmojiOneUnicodes.get(Character.valueOf(charSequence.charAt(i)));
            if (matchEmojiBean != null) {
                int length = matchEmojiBean.emojiMaxLength > charSequence.length() - i ? charSequence.length() - i : matchEmojiBean.emojiMaxLength;
                while (true) {
                    if (length <= 0) {
                        break;
                    }
                    int i2 = i + length;
                    if (((CommonEmoji) matchEmojiBean.emojis.get(charSequence.subSequence(i, i2).toString())) != null) {
                        zMEmojiSpannableStringBuilder.setSpan(new CommonEmojiSpan(), i, i2, 33);
                        i += length - 1;
                        break;
                    }
                    length--;
                }
            }
            i++;
        }
        return EmojiHelper.getInstance().tranToEmojiText(zMEmojiSpannableStringBuilder);
    }

    private void checkUpgradePkg() {
        if (isEmojiInstalled()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                String emojiVersionGetJsonStr = zoomMessenger.emojiVersionGetJsonStr();
                if (!StringUtil.isEmptyOrNull(emojiVersionGetJsonStr)) {
                    try {
                        String optString = new JSONObject(emojiVersionGetJsonStr).getJSONObject("emojione").optString(ClientCookie.VERSION_ATTR);
                        if (!StringUtil.isEmptyOrNull(optString) && !StringUtil.isSameString(PreferenceUtil.readStringValue(PreferenceUtil.COMMON_EMOJI_VERSION, null), optString)) {
                            String downloadUrl = getDownloadUrl(optString);
                            StringBuilder sb = new StringBuilder();
                            sb.append(AppUtil.getCachePath());
                            sb.append(File.separator);
                            sb.append("emojione.zip");
                            this.mUpgradeReqId = zoomMessenger.downloadFileByUrl(downloadUrl, sb.toString(), true);
                            if (!StringUtil.isEmptyOrNull(this.mUpgradeReqId)) {
                                ZoomMessengerUI.getInstance().addListener(this.messengerUIListener);
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_DownloadFileByUrlIml(String str, int i) {
        if (StringUtil.isSameString(str, this.mUpgradeReqId)) {
            if (i == 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(AppUtil.getCachePath());
                sb.append(File.separator);
                sb.append("emojione.zip");
                if (parseEmojiPackage(sb.toString())) {
                    parseConfigFile();
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        String emojiVersionGetJsonStr = zoomMessenger.emojiVersionGetJsonStr();
                        if (!StringUtil.isEmptyOrNull(emojiVersionGetJsonStr)) {
                            try {
                                String optString = new JSONObject(emojiVersionGetJsonStr).getJSONObject("emojione").optString(ClientCookie.VERSION_ATTR);
                                if (!StringUtil.isEmptyOrNull(optString)) {
                                    PreferenceUtil.saveStringValue(PreferenceUtil.COMMON_EMOJI_VERSION, optString);
                                } else {
                                    return;
                                }
                            } catch (Exception unused) {
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
            ZoomMessengerUI.getInstance().removeListener(this.messengerUIListener);
        }
    }

    private void checkDownloadingPkg() {
        long readLongValue = PreferenceUtil.readLongValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID, -2);
        if (readLongValue != -2) {
            int downloadProcess = getDownloadProcess();
            if (downloadProcess < 0) {
                PreferenceUtil.removeValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID);
                return;
            }
            if (downloadProcess == 100) {
                onCommonEmojiPackageDownload(readLongValue);
            } else {
                this.mHandle.removeCallbacks(this.mCheckDownloadRunnable);
                this.mHandle.post(this.mCheckDownloadRunnable);
            }
        }
    }

    private void parseEmojiConfig() {
        InputStreamReader inputStreamReader;
        Throwable th;
        JsonParser jsonParser = new JsonParser();
        File commonEmojiConfFile = getCommonEmojiConfFile();
        if (commonEmojiConfFile.exists()) {
            try {
                inputStreamReader = new InputStreamReader(new FileInputStream(commonEmojiConfFile));
                for (Entry entry : jsonParser.parse((Reader) inputStreamReader).getAsJsonObject().entrySet()) {
                    String str = (String) entry.getKey();
                    JsonObject jsonObject = (JsonObject) entry.getValue();
                    CommonEmoji commonEmoji = new CommonEmoji();
                    commonEmoji.setKey(str);
                    commonEmoji.setCategory(jsonObject.get("category").getAsString());
                    commonEmoji.setOrder(jsonObject.get(BoxList.FIELD_ORDER).getAsInt());
                    commonEmoji.setName(jsonObject.get("name").getAsString());
                    commonEmoji.setShortName(jsonObject.get("shortname").getAsString());
                    if (!jsonObject.get("diversity").isJsonNull()) {
                        commonEmoji.setDiversity(jsonObject.get("diversity").getAsString());
                    }
                    JsonArray asJsonArray = jsonObject.get("diversities").getAsJsonArray();
                    if (asJsonArray.size() > 0) {
                        ArrayList arrayList = new ArrayList();
                        Iterator it = asJsonArray.iterator();
                        while (it.hasNext()) {
                            arrayList.add(((JsonElement) it.next()).getAsString());
                        }
                        commonEmoji.setDiversities(arrayList);
                    }
                    JsonElement jsonElement = jsonObject.get("genders");
                    if (!jsonElement.isJsonNull()) {
                        ArrayList arrayList2 = new ArrayList();
                        Iterator it2 = jsonElement.getAsJsonArray().iterator();
                        while (it2.hasNext()) {
                            arrayList2.add(((JsonElement) it2.next()).getAsString());
                        }
                        commonEmoji.setGenders(arrayList2);
                    }
                    JsonObject asJsonObject = jsonObject.getAsJsonObject("code_points");
                    String unicodeFromConfigStr = getUnicodeFromConfigStr(asJsonObject.get("output").getAsString());
                    if (!StringUtil.isEmptyOrNull(unicodeFromConfigStr)) {
                        commonEmoji.setOutput(unicodeFromConfigStr);
                        mShortNameToUnicode.put(commonEmoji.getShortName(), unicodeFromConfigStr);
                    }
                    JsonArray asJsonArray2 = asJsonObject.getAsJsonArray("default_matches");
                    ArrayList arrayList3 = new ArrayList();
                    Iterator it3 = asJsonArray2.iterator();
                    while (it3.hasNext()) {
                        String asString = ((JsonElement) it3.next()).getAsString();
                        arrayList3.add(asString);
                        String unicodeFromConfigStr2 = getUnicodeFromConfigStr(asString);
                        if (unicodeFromConfigStr2 != null) {
                            char[] charArray = unicodeFromConfigStr2.toCharArray();
                            if (charArray.length != 1 || charArray[0] >= 200) {
                                MatchEmojiBean matchEmojiBean = (MatchEmojiBean) this.mMatchedEmojiOneUnicodes.get(Character.valueOf(charArray[0]));
                                if (matchEmojiBean == null) {
                                    matchEmojiBean = new MatchEmojiBean();
                                    this.mMatchedEmojiOneUnicodes.put(Character.valueOf(charArray[0]), matchEmojiBean);
                                }
                                matchEmojiBean.emojis.put(unicodeFromConfigStr2, commonEmoji);
                                if (unicodeFromConfigStr2.length() > matchEmojiBean.emojiMaxLength) {
                                    matchEmojiBean.emojiMaxLength = unicodeFromConfigStr2.length();
                                }
                            }
                        }
                    }
                    commonEmoji.setMatches(arrayList3);
                    this.mEmojiOneUnicodes.put(str, commonEmoji);
                }
                inputStreamReader.close();
            } catch (Exception unused) {
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            parseSexAndColors();
            return;
        }
        return;
        throw th;
    }

    @Nullable
    private String getUnicodeFromConfigStr(@Nullable String str) {
        if (str == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (String parseInt : str.split("-")) {
            stringBuffer.append(new String(Character.toChars(Integer.parseInt(parseInt, 16))));
        }
        return stringBuffer.toString();
    }

    private void parseSexAndColors() {
        for (Entry value : this.mEmojiOneUnicodes.entrySet()) {
            CommonEmoji commonEmoji = (CommonEmoji) value.getValue();
            if (commonEmoji.getGenders() != null) {
                for (String str : commonEmoji.getGenders()) {
                    if (str.endsWith("2642")) {
                        commonEmoji.setManEmoji((CommonEmoji) this.mEmojiOneUnicodes.get(str));
                    } else {
                        commonEmoji.setWomanEmoji((CommonEmoji) this.mEmojiOneUnicodes.get(str));
                    }
                }
            }
            if (commonEmoji.getDiversities() != null) {
                for (String str2 : commonEmoji.getDiversities()) {
                    commonEmoji.addDiversityEmoji((CommonEmoji) this.mEmojiOneUnicodes.get(str2));
                }
            }
        }
    }

    private void parseEmojiCategoryConfig() {
        InputStreamReader inputStreamReader;
        Throwable th;
        Throwable th2;
        JsonParser jsonParser = new JsonParser();
        HashMap hashMap = new HashMap();
        File commonEmojiCategoryConfFile = getCommonEmojiCategoryConfFile();
        if (commonEmojiCategoryConfFile.exists()) {
            try {
                inputStreamReader = new InputStreamReader(new FileInputStream(commonEmojiCategoryConfFile));
                try {
                    JsonArray asJsonArray = jsonParser.parse((Reader) inputStreamReader).getAsJsonArray();
                    Context globalContext = VideoBoxApplication.getGlobalContext();
                    if (globalContext != null) {
                        Resources resources = globalContext.getResources();
                        Iterator it = asJsonArray.iterator();
                        while (it.hasNext()) {
                            JsonObject asJsonObject = ((JsonElement) it.next()).getAsJsonObject();
                            EmojiCategory emojiCategory = new EmojiCategory();
                            emojiCategory.setLabel(asJsonObject.get("category_label").getAsString());
                            emojiCategory.setName(asJsonObject.get("category").getAsString());
                            emojiCategory.setIconResource(resources.getIdentifier(String.format("zm_mm_emoji_category_%s", new Object[]{emojiCategory.getName()}), "drawable", globalContext.getPackageName()));
                            this.mEmojiCategories.add(emojiCategory);
                            hashMap.put(emojiCategory.getName(), emojiCategory);
                        }
                    }
                    inputStreamReader.close();
                    for (Entry value : this.mEmojiOneUnicodes.entrySet()) {
                        CommonEmoji commonEmoji = (CommonEmoji) value.getValue();
                        if ((commonEmoji.getManEmoji() == null || commonEmoji.getWomanEmoji() == null) && commonEmoji.getDiversity() == null) {
                            EmojiCategory emojiCategory2 = (EmojiCategory) hashMap.get(commonEmoji.getCategory());
                            if (emojiCategory2 != null) {
                                emojiCategory2.getEmojis().add(commonEmoji);
                            }
                        }
                    }
                    EmojiComparetor emojiComparetor = new EmojiComparetor();
                    for (EmojiCategory emojis : this.mEmojiCategories) {
                        Collections.sort(emojis.getEmojis(), emojiComparetor);
                    }
                    return;
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    th = r0;
                    th2 = th4;
                }
            } catch (Exception unused) {
            }
        } else {
            return;
        }
        throw th2;
        if (th != null) {
            try {
                inputStreamReader.close();
            } catch (Throwable th5) {
                th.addSuppressed(th5);
            }
        } else {
            inputStreamReader.close();
        }
        throw th2;
    }

    private File getCommonEmojiDir() {
        return new File(AppUtil.getDataPath(), EMOJI_ONE_PATH);
    }

    private String getCommonEmojiTTFPath() {
        File commonEmojiDir = getCommonEmojiDir();
        if (!commonEmojiDir.exists()) {
            return null;
        }
        if (!commonEmojiDir.isDirectory()) {
            commonEmojiDir.delete();
            return null;
        }
        File file = new File(commonEmojiDir, "emojione_android.ttf");
        if (!file.exists()) {
            return null;
        }
        return file.getAbsolutePath();
    }

    private File getCommonEmojiConfFile() {
        return new File(getCommonEmojiDir(), "common_emoji.json");
    }

    private File getCommonEmojiCategoryConfFile() {
        return new File(getCommonEmojiDir(), "common_emoji_category.json");
    }

    /* access modifiers changed from: private */
    public void onDowloadChange() {
        int i;
        long readLongValue = PreferenceUtil.readLongValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID, -2);
        if (readLongValue != -2) {
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                DownloadManager downloadManager = (DownloadManager) globalContext.getSystemService("download");
                if (downloadManager != null) {
                    Query query = new Query();
                    int i2 = 1;
                    int i3 = 0;
                    query.setFilterById(new long[]{readLongValue});
                    Cursor query2 = downloadManager.query(query);
                    if (query2 != null) {
                        if (query2.moveToFirst()) {
                            int i4 = query2.getInt(query2.getColumnIndex("status"));
                            if (i4 == 2 || i4 == 4) {
                                int columnIndex = query2.getColumnIndex("total_size");
                                int columnIndex2 = query2.getColumnIndex("bytes_so_far");
                                int i5 = query2.getInt(columnIndex);
                                int i6 = query2.getInt(columnIndex2);
                                if (i5 == 0) {
                                    PreferenceUtil.removeValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID);
                                    i = 0;
                                } else {
                                    i = (i6 * 100) / i5;
                                    i2 = 0;
                                }
                                IListener[] all = this.mListenerList.getAll();
                                if (all != null) {
                                    int length = all.length;
                                    while (i3 < length) {
                                        OnEmojiPackageInstallListener onEmojiPackageInstallListener = (OnEmojiPackageInstallListener) all[i3];
                                        if (i2 != 0) {
                                            onEmojiPackageInstallListener.onEmojiPkgDownloadFailed();
                                        } else {
                                            onEmojiPackageInstallListener.onEmojiPkgDownload(i);
                                        }
                                        i3++;
                                    }
                                }
                            } else if (i4 == 8) {
                                onCommonEmojiPackageDownload(readLongValue);
                                PreferenceUtil.removeValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID);
                            } else if (i4 != 16) {
                                i2 = 0;
                            } else {
                                IListener[] all2 = this.mListenerList.getAll();
                                if (all2 != null) {
                                    int length2 = all2.length;
                                    while (i3 < length2) {
                                        ((OnEmojiPackageInstallListener) all2[i3]).onEmojiPkgDownloadFailed();
                                        i3++;
                                    }
                                }
                                PreferenceUtil.removeValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID);
                            }
                            i3 = i2;
                        } else {
                            IListener[] all3 = this.mListenerList.getAll();
                            if (all3 != null) {
                                int length3 = all3.length;
                                while (i3 < length3) {
                                    ((OnEmojiPackageInstallListener) all3[i3]).onEmojiPkgDownloadFailed();
                                    i3++;
                                }
                            }
                            PreferenceUtil.removeValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID);
                            i3 = 1;
                        }
                        query2.close();
                    }
                    if (i3 == 0) {
                        this.mHandle.postDelayed(this.mCheckDownloadRunnable, 1000);
                    }
                }
            }
        }
    }

    private void onCommonEmojiPackageDownload(long j) {
        this.mHandle.removeCallbacks(this.mCheckDownloadRunnable);
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext != null) {
            DownloadManager downloadManager = (DownloadManager) globalContext.getSystemService("download");
            if (downloadManager != null) {
                Query query = new Query();
                int i = 0;
                query.setFilterById(new long[]{j});
                Cursor query2 = downloadManager.query(query);
                if (query2 != null) {
                    if (!query2.moveToFirst()) {
                        IListener[] all = this.mListenerList.getAll();
                        if (all != null) {
                            int length = all.length;
                            while (i < length) {
                                ((OnEmojiPackageInstallListener) all[i]).onEmojiPkgDownloadFailed();
                                i++;
                            }
                        }
                    } else if (parseEmojiPackage(query2.getString(query2.getColumnIndex("local_uri")))) {
                        parseConfigFile();
                        PreferenceUtil.saveStringValue(PreferenceUtil.COMMON_EMOJI_VERSION, PreferenceUtil.readStringValue(PreferenceUtil.COMMON_EMOJI_PENDING_VERSION, null));
                        IListener[] all2 = this.mListenerList.getAll();
                        if (all2 != null) {
                            int length2 = all2.length;
                            while (i < length2) {
                                ((OnEmojiPackageInstallListener) all2[i]).onEmojiPkgInstalled();
                                i++;
                            }
                        }
                    } else {
                        IListener[] all3 = this.mListenerList.getAll();
                        if (all3 != null) {
                            int length3 = all3.length;
                            while (i < length3) {
                                ((OnEmojiPackageInstallListener) all3[i]).onEmojiPkgDownloadFailed();
                                i++;
                            }
                        }
                    }
                    query2.close();
                }
                PreferenceUtil.removeValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID);
            }
        }
    }

    public boolean isEmojiInstalled() {
        return this.mEmojiTypeface != null;
    }

    public int getDownloadProcess() {
        long readLongValue = PreferenceUtil.readLongValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID, -2);
        int i = -1;
        if (readLongValue == -2) {
            return -1;
        }
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext == null) {
            return -1;
        }
        DownloadManager downloadManager = (DownloadManager) globalContext.getSystemService("download");
        if (downloadManager == null) {
            return -1;
        }
        Query query = new Query();
        query.setFilterById(new long[]{readLongValue});
        Cursor query2 = downloadManager.query(query);
        if (query2 != null && query2.moveToFirst()) {
            int i2 = query2.getInt(query2.getColumnIndex("status"));
            if (i2 == 2 || i2 == 4) {
                int columnIndex = query2.getColumnIndex("total_size");
                int columnIndex2 = query2.getColumnIndex("bytes_so_far");
                int i3 = query2.getInt(columnIndex);
                int i4 = query2.getInt(columnIndex2);
                if (i3 != 0) {
                    i = (i4 * 100) / i3;
                }
            } else if (i2 == 8) {
                i = 100;
            }
        }
        if (query2 != null) {
            query2.close();
        }
        return i;
    }

    public boolean containCommonEmoji(@Nullable CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        return EMOJI_PATTERN.matcher(charSequence).find();
    }

    public void cancelInstallEmoji() {
        long readLongValue = PreferenceUtil.readLongValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID, -2);
        if (readLongValue != -2) {
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                DownloadManager downloadManager = (DownloadManager) globalContext.getSystemService("download");
                if (downloadManager != null) {
                    downloadManager.remove(new long[]{readLongValue});
                }
                PreferenceUtil.removeValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID);
            }
        }
    }

    public void installEmoji() {
        int downloadProcess = getDownloadProcess();
        if (downloadProcess < 0 || downloadProcess >= 100) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                String emojiVersionGetJsonStr = zoomMessenger.emojiVersionGetJsonStr();
                if (!StringUtil.isEmptyOrNull(emojiVersionGetJsonStr)) {
                    try {
                        String optString = new JSONObject(emojiVersionGetJsonStr).getJSONObject("emojione").optString(ClientCookie.VERSION_ATTR);
                        if (!StringUtil.isEmptyOrNull(optString)) {
                            if (!StringUtil.isSameString(PreferenceUtil.readStringValue(PreferenceUtil.COMMON_EMOJI_VERSION, null), optString) || !isEmojiInstalled()) {
                                Context globalContext = VideoBoxApplication.getGlobalContext();
                                if (globalContext != null) {
                                    DownloadManager downloadManager = (DownloadManager) globalContext.getSystemService("download");
                                    if (downloadManager != null) {
                                        this.mHandle.removeCallbacks(this.mCheckDownloadRunnable);
                                        Request request = new Request(Uri.parse(getDownloadUrl(optString)));
                                        request.setDestinationInExternalFilesDir(VideoBoxApplication.getGlobalContext(), BoxFile.TYPE, "zoomEmojiPkg");
                                        request.setTitle(globalContext.getString(C4558R.string.zm_lbl_emoji_pkg_title_23626));
                                        long enqueue = downloadManager.enqueue(request);
                                        PreferenceUtil.saveStringValue(PreferenceUtil.COMMON_EMOJI_PENDING_VERSION, optString);
                                        PreferenceUtil.saveLongValue(PreferenceUtil.COMMON_EMOJI_DOWNLOAD_ID, enqueue);
                                        this.mHandle.post(this.mCheckDownloadRunnable);
                                    }
                                }
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    private String getDownloadUrl(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        return String.format("%s/emoji/%s/emojione_android_%s.zip", new Object[]{PTApp.getInstance().getZoomDomain(), str, str});
    }

    private boolean parseEmojiPackage(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        String path = Uri.parse(str).getPath();
        if (path != null && new File(path).exists() && unzipFile(path)) {
            return true;
        }
        return false;
    }

    public boolean unzipFile(String str) {
        ZipFile zipFile;
        if (StringUtil.isEmptyOrNull(str) || !new File(str).exists()) {
            return false;
        }
        File commonEmojiDir = getCommonEmojiDir();
        if (commonEmojiDir.exists() && !commonEmojiDir.isDirectory()) {
            commonEmojiDir.delete();
        }
        if ((!commonEmojiDir.exists() && !commonEmojiDir.mkdirs()) || !commonEmojiDir.isDirectory()) {
            return false;
        }
        try {
            zipFile = new ZipFile(str);
            Enumeration entries = zipFile.entries();
            if (entries != null) {
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                    File file = new File(commonEmojiDir, zipEntry.getName());
                    String canonicalPath = file.getCanonicalPath();
                    file.delete();
                    if (!canonicalPath.startsWith(commonEmojiDir.getCanonicalPath())) {
                        throw new IllegalStateException("File is outside extraction target directory.");
                    } else if (zipEntry.isDirectory()) {
                        file.mkdirs();
                    } else {
                        File parentFile = file.getParentFile();
                        if (parentFile != null) {
                            parentFile.mkdirs();
                        }
                        InputStream inputStream = zipFile.getInputStream(zipEntry);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] bArr = new byte[8192];
                        while (true) {
                            int read = inputStream.read(bArr);
                            if (read <= 0) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        inputStream.close();
                        fileOutputStream.close();
                    }
                }
            }
            zipFile.close();
            return true;
        } catch (Exception unused) {
            return false;
        } catch (Throwable th) {
            r9.addSuppressed(th);
        }
        throw th;
    }
}
