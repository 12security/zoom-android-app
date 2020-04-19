package p021us.zoom.androidlib.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.provider.MediaStore.Images.Media;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxUser;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.File;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.apache.http.protocol.HTTP;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.data.CalendarResult;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;

/* renamed from: us.zoom.androidlib.util.AndroidAppUtil */
public class AndroidAppUtil {
    private static final String ACTION_MEETING_INVITE = ".intent.action.MeetingInvite";
    private static final String[] DAY_OF_WEEK = {"SU", "MO", "TU", "WE", "TH", "FR", "SA"};
    public static final int DEF_REMINDER_MINUTES = 15;
    public static String EXTRA_DATE = "meetingDate";
    public static String EXTRA_IS_REPEAT = "meetingIsRepeat";
    public static String EXTRA_MEETING_ID = "meetingId";
    public static String EXTRA_MEETING_PSW = "meetingPassword";
    public static String EXTRA_MEETING_RAW_PSW = "meetingRawPassword";
    public static String EXTRA_SUBJECT = "android.intent.extra.SUBJECT";
    public static String EXTRA_TEXT = "android.intent.extra.TEXT";
    public static String EXTRA_TIME = "meetingTime";
    public static String EXTRA_TOPIC = "meetingTopic";
    public static final int FILE_TYPE_APK = 0;
    public static final int FILE_TYPE_AUDIO = 6;
    public static final int FILE_TYPE_DOC = 4;
    public static final int FILE_TYPE_FOLDER = 100;
    public static final int FILE_TYPE_HTML = 2;
    public static final int FILE_TYPE_ICS = 8;
    public static final int FILE_TYPE_IMAGE = 3;
    public static final int FILE_TYPE_TEXT = 1;
    public static final int FILE_TYPE_UNKNOWN = -1;
    public static final int FILE_TYPE_VIDEO = 5;
    public static final int FILE_TYPE_ZIP = 7;
    private static final String GSF_PACKAGE = "com.google.android.gsf";
    public static final String IMAGE_MIME_TYPE_GIF = "image/gif";
    public static final String IMAGE_MIME_TYPE_JPG = "image/jpeg";
    public static final String IMAGE_MIME_TYPE_PNG = "image/png";
    public static final String IMAGE_MIME_TYPE_UNKNOW = "unknow";
    private static final String MIMTYPE_FOLDER = "application/vnd.google-apps.folder";
    private static final String TAG = "AndroidAppUtil";
    private static final Object[][] mimeTypesTable = {new Object[]{".apk", "application/vnd.android.package-archive", Integer.valueOf(0), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_apk)}, new Object[]{".c", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".conf", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".cpp", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".cxx", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".php", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".perl", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".py", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".vbs", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".h", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".java", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".s", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".S", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".log", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".prop", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".rc", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".xml", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".sh", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".bat", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".cmd", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".txt", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".js", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".lrc", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".ini", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".inf", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".properties", HTTP.PLAIN_TEXT_TYPE, Integer.valueOf(1), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".htm", "text/html", Integer.valueOf(2), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_html)}, new Object[]{".html", "text/html", Integer.valueOf(2), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_html)}, new Object[]{".ics", "text/calendar", Integer.valueOf(8), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_txt)}, new Object[]{".bmp", "image/bmp", Integer.valueOf(3), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_image)}, new Object[]{".gif", IMAGE_MIME_TYPE_GIF, Integer.valueOf(3), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_image)}, new Object[]{".jpeg", IMAGE_MIME_TYPE_JPG, Integer.valueOf(3), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_image)}, new Object[]{".jpg", IMAGE_MIME_TYPE_JPG, Integer.valueOf(3), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_image)}, new Object[]{".png", IMAGE_MIME_TYPE_PNG, Integer.valueOf(3), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_image)}, new Object[]{".3gp", "video/3gpp", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".asf", "video/x-ms-asf", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".avi", "video/x-msvideo", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".m4u", "video/vnd.mpegurl", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".m4v", "video/x-m4v", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".mov", "video/quicktime", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".mp4", "video/mp4", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".mpe", "video/mpeg", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".mpeg", "video/mpeg", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".mpg", "video/mpeg", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".mpg4", "video/mp4", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".wmv", "video/x-ms-wmv", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".rmvb", "video/x-pn-realaudio", Integer.valueOf(5), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_video)}, new Object[]{".m3u", "audio/x-mpegurl", Integer.valueOf(6), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_audio)}, new Object[]{".m4a", "audio/mp4a-latm", Integer.valueOf(6), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_audio)}, new Object[]{".m4b", "audio/mp4a-latm", Integer.valueOf(6), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_audio)}, new Object[]{".m4p", "audio/mp4a-latm", Integer.valueOf(6), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_audio)}, new Object[]{".mp2", "audio/x-mpeg", Integer.valueOf(6), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_audio)}, new Object[]{".mp3", "audio/x-mpeg", Integer.valueOf(6), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_audio)}, new Object[]{".mpga", "audio/mpeg", Integer.valueOf(6), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_audio)}, new Object[]{".ogg", "audio/ogg", Integer.valueOf(6), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_audio)}, new Object[]{".wav", "audio/x-wav", Integer.valueOf(6), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_audio)}, new Object[]{".wma", "audio/x-ms-wma", Integer.valueOf(6), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_audio)}, new Object[]{".doc", "application/msword", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_doc)}, new Object[]{".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_doc)}, new Object[]{".xls", "application/vnd.ms-excel", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_xls)}, new Object[]{".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_xls)}, new Object[]{".msg", "application/vnd.ms-outlook", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_unknown)}, new Object[]{".pdf", "application/pdf", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_pdf)}, new Object[]{".pps", "application/vnd.ms-powerpoint", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_ppt)}, new Object[]{".ppt", "application/vnd.ms-powerpoint", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_ppt)}, new Object[]{".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_ppt)}, new Object[]{".rtf", "application/rtf", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_doc)}, new Object[]{".wps", "application/vnd.ms-works", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_doc)}, new Object[]{".epub", "application/epub+zip", Integer.valueOf(4), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_epud)}, new Object[]{".gtar", "application/x-gtar", Integer.valueOf(7), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_zip)}, new Object[]{".gz", "application/x-gzip", Integer.valueOf(7), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_zip)}, new Object[]{".jar", "application/java-archive", Integer.valueOf(7), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_zip)}, new Object[]{".tar", "application/x-tar", Integer.valueOf(7), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_zip)}, new Object[]{".tgz", "application/x-compressed", Integer.valueOf(7), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_zip)}, new Object[]{".z", "application/x-compress", Integer.valueOf(7), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_zip)}, new Object[]{".zip", "application/x-zip-compressed", Integer.valueOf(7), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_zip)}, new Object[]{".rar", "application/x-rar-compressed", Integer.valueOf(7), Integer.valueOf(C4409R.C4410drawable.zm_ic_filetype_zip)}};

    /* renamed from: us.zoom.androidlib.util.AndroidAppUtil$AppItem */
    static class AppItem {
        ResolveInfo appInfo = null;

        AppItem(ResolveInfo resolveInfo) {
            this.appInfo = resolveInfo;
        }
    }

    /* renamed from: us.zoom.androidlib.util.AndroidAppUtil$AppListAdapter */
    static class AppListAdapter extends BaseAdapter {
        private ZMActivity mActivity;
        private List<AppItem> mList;

        public long getItemId(int i) {
            return (long) i;
        }

        public AppListAdapter(ZMActivity zMActivity, List<AppItem> list) {
            this.mActivity = zMActivity;
            this.mList = list;
        }

        public int getCount() {
            return this.mList.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i < 0 || i >= this.mList.size()) {
                return null;
            }
            return this.mList.get(i);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            Object item = getItem(i);
            if (view == null) {
                view = View.inflate(this.mActivity, C4409R.layout.zm_app_item, null);
            }
            ImageView imageView = (ImageView) view.findViewById(C4409R.C4411id.imgIcon);
            TextView textView = (TextView) view.findViewById(C4409R.C4411id.txtLabel);
            if (item instanceof AppItem) {
                AppItem appItem = (AppItem) item;
                if (appItem.appInfo != null) {
                    imageView.setVisibility(0);
                    imageView.setImageDrawable(AndroidAppUtil.getApplicationIcon((Context) this.mActivity, appItem.appInfo));
                    textView.setText(AndroidAppUtil.getApplicationLabel((Context) this.mActivity, appItem.appInfo));
                }
            }
            return view;
        }
    }

    /* renamed from: us.zoom.androidlib.util.AndroidAppUtil$EventRepeatType */
    public enum EventRepeatType {
        NONE,
        DAILY,
        WORKDAY,
        WEEKLY,
        BIWEEKLY,
        MONTHLY,
        YEARLY,
        UNKNOWN
    }

    /* renamed from: us.zoom.androidlib.util.AndroidAppUtil$MimeType */
    public static class MimeType {
        public int fileType = -1;
        public String mimeType;

        public MimeType(String str, int i) {
            this.mimeType = str;
            this.fileType = i;
        }
    }

    /* renamed from: us.zoom.androidlib.util.AndroidAppUtil$ResolbeInfoComparator */
    static class ResolbeInfoComparator implements Comparator<ResolveInfo> {
        private Collator mCollator;

        public ResolbeInfoComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
            return this.mCollator.compare(resolveInfo.activityInfo.name, resolveInfo2.activityInfo.name);
        }
    }

    @Nullable
    public static Drawable getApplicationIcon(Context context, ResolveInfo resolveInfo) {
        if (context == null || resolveInfo == null || resolveInfo.activityInfo == null) {
            return null;
        }
        return getApplicationIcon(context, resolveInfo.activityInfo.applicationInfo);
    }

    @Nullable
    public static Drawable getApplicationIcon(Context context, ApplicationInfo applicationInfo) {
        if (context == null || applicationInfo == null) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return null;
        }
        return packageManager.getApplicationIcon(applicationInfo);
    }

    @Nullable
    public static String getApplicationLabel(Context context, ResolveInfo resolveInfo) {
        if (context == null || resolveInfo == null || resolveInfo.activityInfo == null) {
            return null;
        }
        return getApplicationLabel(context, resolveInfo.activityInfo.applicationInfo);
    }

    @Nullable
    public static String getApplicationLabel(Context context, ApplicationInfo applicationInfo) {
        String str = null;
        if (context == null || applicationInfo == null) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return null;
        }
        CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
        if (applicationLabel != null) {
            str = applicationLabel.toString();
        }
        return str;
    }

    @Nullable
    public static Drawable getActivityIcon(Context context, ResolveInfo resolveInfo) {
        if (context == null || resolveInfo == null) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return null;
        }
        return resolveInfo.loadIcon(packageManager);
    }

    @Nullable
    public static String getActivityLabel(Context context, ResolveInfo resolveInfo) {
        String str = null;
        if (context == null || resolveInfo == null) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return null;
        }
        CharSequence loadLabel = resolveInfo.loadLabel(packageManager);
        if (loadLabel != null) {
            str = loadLabel.toString();
        }
        return str;
    }

    @Nullable
    public static ApplicationInfo getApplicationInfo(Context context, String str) {
        if (context == null || str == null) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return null;
        }
        try {
            return packageManager.getApplicationInfo(str, 9344);
        } catch (NameNotFoundException unused) {
            return null;
        }
    }

    public static List<ResolveInfo> queryEmailActivities(Context context) {
        context.getPackageManager();
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setType("vnd.android.cursor.dir/email");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra("android.intent.extra.SUBJECT", "test topic");
        intent.putExtra("android.intent.extra.TEXT", "test text");
        ArrayList arrayList = new ArrayList();
        List<ResolveInfo> queryActivitiesForIntent = queryActivitiesForIntent(context, intent);
        if (queryActivitiesForIntent == null) {
            return arrayList;
        }
        List querySMSActivities = querySMSActivities(context);
        for (ResolveInfo resolveInfo : queryActivitiesForIntent) {
            if (!appExists(resolveInfo, querySMSActivities)) {
                arrayList.add(resolveInfo);
            }
        }
        Collections.sort(arrayList, new ResolbeInfoComparator(CompatUtils.getLocalDefault()));
        return arrayList;
    }

    public static boolean hasCameraApp(Context context) {
        List queryActivitiesForIntent = queryActivitiesForIntent(context, new Intent("android.media.action.IMAGE_CAPTURE"));
        return queryActivitiesForIntent != null && queryActivitiesForIntent.size() > 0;
    }

    private static boolean appExists(ResolveInfo resolveInfo, List<ResolveInfo> list) {
        for (ResolveInfo resolveInfo2 : list) {
            if (resolveInfo2.activityInfo == null || resolveInfo2.activityInfo.packageName == null || resolveInfo.activityInfo == null) {
                return false;
            }
            if (resolveInfo2.activityInfo.packageName.equals(resolveInfo.activityInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    public static List<ResolveInfo> querySMSActivities(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("sms_body", "test");
        intent.setData(Uri.parse("sms:"));
        List<ResolveInfo> queryActivitiesForIntent = queryActivitiesForIntent(context, intent);
        if (queryActivitiesForIntent == null) {
            return new ArrayList();
        }
        Collections.sort(queryActivitiesForIntent, new ResolbeInfoComparator(CompatUtils.getLocalDefault()));
        return queryActivitiesForIntent;
    }

    public static List<ResolveInfo> queryZoomMeetingInviteActivities(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getPackageName());
        sb.append(ACTION_MEETING_INVITE);
        List<ResolveInfo> queryActivitiesForIntent = queryActivitiesForIntent(context, new Intent(sb.toString()));
        if (queryActivitiesForIntent == null) {
            return new ArrayList();
        }
        Collections.sort(queryActivitiesForIntent, new ResolbeInfoComparator(CompatUtils.getLocalDefault()));
        return queryActivitiesForIntent;
    }

    public static List<ResolveInfo> queryCalendarActivities(Context context) {
        Intent intent = new Intent("android.intent.action.EDIT");
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("title", "test title");
        intent.putExtra(BoxItem.FIELD_DESCRIPTION, "test description");
        intent.putExtra("beginTime", System.currentTimeMillis());
        intent.putExtra("endTime", System.currentTimeMillis());
        List<ResolveInfo> queryActivitiesForIntent = queryActivitiesForIntent(context, intent);
        if (queryActivitiesForIntent == null) {
            return new ArrayList();
        }
        Collections.sort(queryActivitiesForIntent, new ResolbeInfoComparator(CompatUtils.getLocalDefault()));
        return queryActivitiesForIntent;
    }

    public static boolean hasEmailApp(Context context) {
        return queryEmailActivities(context).size() > 0;
    }

    public static boolean hasSMSApp(Context context) {
        return querySMSActivities(context).size() > 0;
    }

    public static boolean hasCalendarApp(Context context) {
        return queryCalendarActivities(context).size() > 0;
    }

    public static boolean hasBrowserApp(Context context) {
        return ZMIntentUtil.queryBrowserActivities(context).size() > 0;
    }

    public static boolean hasGooglePlayStoreApp(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        Iterator it = context.getPackageManager().getInstalledPackages(8192).iterator();
        while (true) {
            if (it.hasNext()) {
                if (((PackageInfo) it.next()).packageName.equals("com.android.vending")) {
                    z = true;
                    break;
                }
            } else {
                break;
            }
        }
        return z;
    }

    public static boolean sendEmailVia(ResolveInfo resolveInfo, Context context, String[] strArr, String str, String str2, String str3) {
        if (str2 != null) {
            str2 = str2.replace("\r\n", FontStyleHelper.SPLITOR);
        }
        Intent intent = new Intent();
        intent.setType("vnd.android.cursor.dir/email");
        if (strArr == null || strArr.length == 0 || !StringUtil.isEmptyOrNull(str3)) {
            intent.setAction("android.intent.action.SEND");
        } else {
            intent.setAction("android.intent.action.SENDTO");
            intent.setData(Uri.parse("mailto:"));
        }
        intent.putExtra("android.intent.extra.SUBJECT", str);
        intent.putExtra("android.intent.extra.TEXT", str2);
        if (strArr != null && strArr.length > 0) {
            intent.putExtra("android.intent.extra.EMAIL", strArr);
        }
        if (!StringUtil.isEmptyOrNull(str3)) {
            Uri uri = null;
            File file = new File(Uri.parse(str3).getPath());
            if (file.exists()) {
                uri = FileProvider.getUriForFile(context, context.getResources().getString(C4409R.string.zm_app_provider), file);
                intent.addFlags(1);
            }
            if (uri != null) {
                intent.putExtra("android.intent.extra.STREAM", uri);
            }
        }
        if (!(resolveInfo == null || resolveInfo.activityInfo == null)) {
            intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        }
        try {
            context.startActivity(intent);
        } catch (Exception unused) {
        }
        return true;
    }

    public static boolean sendEmail(Context context, String[] strArr, String str, String str2, String str3) {
        return sendEmailVia(null, context, strArr, str, str2, str3);
    }

    public static boolean sendZoomMeetingInvitationVia(ResolveInfo resolveInfo, Activity activity, String str, String str2, String str3, long j, String str4, String str5, int i) {
        Uri uri;
        StringBuilder sb = new StringBuilder();
        sb.append(activity.getPackageName());
        sb.append(ACTION_MEETING_INVITE);
        Intent intent = new Intent(sb.toString());
        try {
            uri = Uri.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            uri = null;
        }
        if (uri != null) {
            intent.setData(uri);
        }
        intent.putExtra(EXTRA_SUBJECT, str2);
        intent.putExtra(EXTRA_TEXT, str3);
        intent.putExtra(EXTRA_MEETING_ID, j);
        intent.putExtra(EXTRA_MEETING_PSW, str4);
        intent.putExtra(EXTRA_MEETING_RAW_PSW, str5);
        if (!(resolveInfo == null || resolveInfo.activityInfo == null)) {
            intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        }
        try {
            activity.startActivityForResult(intent, i);
        } catch (Exception unused) {
        }
        return true;
    }

    public static boolean sendZoomMeetingInvitation(Activity activity, String str, String str2, String str3, long j, String str4, String str5, int i) {
        return sendZoomMeetingInvitationVia(null, activity, str, str2, str3, j, str4, str5, i);
    }

    public static boolean sendSMSVia(ResolveInfo resolveInfo, Context context, String[] strArr, String str) {
        if (context == null || resolveInfo == null || resolveInfo.activityInfo == null) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("sms_body", str);
        if (strArr == null || strArr.length <= 0) {
            intent.setData(Uri.parse("sms:"));
        } else {
            char c = ';';
            if ("samsung".equalsIgnoreCase(Build.MANUFACTURER)) {
                c = ',';
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < strArr.length; i++) {
                sb.append(strArr[i]);
                if (i < strArr.length - 1) {
                    sb.append(c);
                }
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("smsto:");
            sb2.append(sb.toString());
            intent.setData(Uri.parse(sb2.toString()));
        }
        if (resolveInfo.activityInfo != null) {
            intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        }
        try {
            context.startActivity(intent);
        } catch (Exception unused) {
        }
        return true;
    }

    public static void sendSMS(Context context, String[] strArr, String str) {
        sendSMSVia(null, context, strArr, str);
    }

    public static boolean createCalendarEventVia(ResolveInfo resolveInfo, Context context, long j, long j2, String str, String str2, String str3) {
        if (context == null) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.EDIT");
        intent.setType("vnd.android.cursor.item/event");
        if (str != null) {
            intent.putExtra("title", str);
        }
        if (str2 != null) {
            intent.putExtra(BoxItem.FIELD_DESCRIPTION, str2);
        }
        if (j > 0) {
            intent.putExtra("beginTime", j);
        }
        if (j2 > 0) {
            intent.putExtra("endTime", j2);
        }
        intent.putExtra("allDay", false);
        if (str3 != null) {
            intent.putExtra("eventLocation", str3);
        }
        if (resolveInfo != null) {
            intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        }
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static void sendDial(Context context, String str) {
        if (context != null && !StringUtil.isEmptyOrNull(str)) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("tel:");
                sb.append(str);
                Uri parse = Uri.parse(sb.toString());
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DIAL");
                intent.setData(parse);
                context.startActivity(intent);
            } catch (Exception unused) {
            }
        }
    }

    public static boolean createCalendarEvent(Context context, long j, long j2, String str, String str2, String str3) {
        return createCalendarEventVia(null, context, j, j2, str, str2, str3);
    }

    @RequiresPermission("android.permission.READ_CALENDAR")
    @Nullable
    public static long[] queryCalendarEventsForMeeting(Context context, long j, String str) {
        if (context == null || j <= 0) {
            return null;
        }
        Uri uri = Events.CONTENT_URI;
        String str2 = "(((eventLocation = ?) OR (description like ?)) AND (deleted = ?))";
        ContentResolver contentResolver = context.getContentResolver();
        StringBuilder sb = new StringBuilder();
        sb.append("%");
        sb.append(str);
        sb.append("%");
        try {
            Cursor query = contentResolver.query(uri, new String[]{"_id"}, str2, new String[]{str, sb.toString(), "0"}, null);
            if (query == null) {
                return new long[0];
            }
            long[] jArr = new long[query.getCount()];
            int i = 0;
            while (query.moveToNext()) {
                int i2 = i + 1;
                jArr[i] = query.getLong(0);
                i = i2;
            }
            query.close();
            return jArr;
        } catch (Exception unused) {
            return null;
        }
    }

    @SuppressLint({"NewApi"})
    @Nullable
    public static Event loadCalendarEvent(Context context, long j) {
        Event event = null;
        if (context == null || j < 0) {
            return null;
        }
        try {
            Cursor query = context.getContentResolver().query(ContentUris.withAppendedId(Events.CONTENT_URI, j), new String[]{"dtstart", "title", BoxItem.FIELD_DESCRIPTION, "eventLocation", "rrule", "duration", "dtend"}, null, null, null);
            if (query == null) {
                return null;
            }
            if (query.getCount() > 0) {
                query.moveToNext();
                event = new Event();
                event.f509id = j;
                event.startMillis = query.getLong(0);
                event.title = query.getString(1);
                event.location = query.getString(3);
                event.rrule = query.getString(4);
                event.endMillis = query.getLong(6);
            }
            query.close();
            return event;
        } catch (Exception unused) {
            return null;
        }
    }

    @SuppressLint({"NewApi"})
    public static boolean updateCalendarEvent(Context context, long j, long j2, long j3, String str, String str2, String str3, String str4) {
        boolean z = false;
        if (context == null || j < 0) {
            return false;
        }
        Uri withAppendedId = ContentUris.withAppendedId(Events.CONTENT_URI, j);
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        if (j2 > 0) {
            contentValues.put("dtstart", Long.valueOf(j2));
        }
        if (str != null) {
            contentValues.put("title", str);
        }
        if (str2 != null) {
            contentValues.put(BoxItem.FIELD_DESCRIPTION, str2);
        }
        if (str3 != null) {
            contentValues.put("eventLocation", str3);
        }
        if (!StringUtil.isEmptyOrNull(str4)) {
            contentValues.put("rrule", str4);
            StringBuilder sb = new StringBuilder();
            sb.append("P");
            sb.append((j3 - j2) / 1000);
            sb.append(ExifInterface.LATITUDE_SOUTH);
            contentValues.put("duration", sb.toString());
        } else if (j3 > 0) {
            contentValues.put("dtend", Long.valueOf(j3));
        }
        try {
            if (contentResolver.update(withAppendedId, contentValues, null, null) > 0) {
                z = true;
            }
            return z;
        } catch (Exception unused) {
            return false;
        }
    }

    @SuppressLint({"NewApi"})
    public static void editCalendarEvent(Context context, long j, long j2, long j3, String str, String str2, String str3, boolean z) {
        Uri withAppendedId = ContentUris.withAppendedId(Events.CONTENT_URI, j);
        Intent intent = new Intent("android.intent.action.EDIT");
        intent.setData(withAppendedId);
        intent.putExtra("editMode", z);
        if (str != null) {
            intent.putExtra("title", str);
        }
        if (str2 != null) {
            intent.putExtra(BoxItem.FIELD_DESCRIPTION, str2);
        }
        if (j2 > 0) {
            intent.putExtra("beginTime", j2);
        }
        if (j3 > 0) {
            intent.putExtra("endTime", j3);
        }
        if (str3 != null) {
            intent.putExtra("eventLocation", str3);
        }
        try {
            context.startActivity(intent);
        } catch (Exception unused) {
        }
    }

    public static void viewCalendarEvent(Context context, long j) {
        viewCalendarEvent(context, j, 0, 0);
    }

    @SuppressLint({"NewApi"})
    public static void viewCalendarEvent(Context context, long j, long j2, long j3) {
        Uri withAppendedId = ContentUris.withAppendedId(Events.CONTENT_URI, j);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(withAppendedId);
        if (j2 > 0) {
            intent.putExtra("beginTime", j2);
        }
        if (j3 > 0) {
            intent.putExtra("endTime", j3);
        }
        try {
            context.startActivity(intent);
        } catch (Exception unused) {
        }
    }

    @RequiresPermission("android.permission.WRITE_CALENDAR")
    public static int deleteCalendarEvent(Context context, long j) {
        if (context == null || j < 0) {
            return 0;
        }
        deleteRemindersForEvent(context, j);
        try {
            return context.getContentResolver().delete(ContentUris.withAppendedId(Events.CONTENT_URI, j), null, null);
        } catch (Exception unused) {
            return 0;
        }
    }

    @RequiresPermission("android.permission.WRITE_CALENDAR")
    private static int deleteRemindersForEvent(Context context, long j) {
        if (context == null || j < 0) {
            return 0;
        }
        try {
            return context.getContentResolver().delete(Reminders.CONTENT_URI, "(event_id = ?)", new String[]{String.valueOf(j)});
        } catch (Exception unused) {
            return 0;
        }
    }

    @RequiresPermission("android.permission.READ_CALENDAR")
    private static long selectDefaultCalendar(Context context, @NonNull CalendarResult calendarResult, String str) {
        CalendarResult calendarResult2 = calendarResult;
        if (context == null) {
            return 0;
        }
        Cursor query = context.getContentResolver().query(Calendars.CONTENT_URI, new String[]{"_id", "account_name", "account_type", "ownerAccount"}, null, null, null);
        if (query == null) {
            return 0;
        }
        String str2 = null;
        String str3 = null;
        long j = -1;
        long j2 = -1;
        String str4 = str;
        while (query.moveToNext()) {
            long j3 = query.getLong(0);
            String string = query.getString(1);
            String string2 = query.getString(3);
            String string3 = query.getString(2);
            if (StringUtil.isEmptyOrNull(str4)) {
                return j3;
            }
            str4 = str4.toLowerCase();
            String lowerCase = string2 != null ? string2.toLowerCase() : "";
            String lowerCase2 = string != null ? string.toLowerCase() : "";
            if (!"com.google".equals(string3) || !str4.equals(lowerCase) || !str4.equals(lowerCase2)) {
                if (j2 == -1) {
                    str2 = string3;
                    j2 = j3;
                }
                if (j == -1 && string3.equals("com.google") && lowerCase.length() > 0 && lowerCase.equals(lowerCase2)) {
                    j = j3;
                }
                str3 = string3;
            } else {
                calendarResult2.setmAccountType(string3);
                return j3;
            }
        }
        query.close();
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i >= 0) {
            str2 = str3;
        }
        calendarResult2.setmAccountType(str2);
        if (i < 0) {
            j = j2;
        }
        return j;
    }

    @RequiresPermission("android.permission.READ_CALENDAR")
    private static long selectDefaultCalendar(Context context, String str) {
        if (context == null) {
            return 0;
        }
        Cursor query = context.getContentResolver().query(Calendars.CONTENT_URI, new String[]{"_id", "account_name", "account_type", "ownerAccount"}, null, null, null);
        if (query == null) {
            return 0;
        }
        long j = -1;
        long j2 = -1;
        while (query.moveToNext()) {
            long j3 = query.getLong(0);
            String string = query.getString(1);
            String string2 = query.getString(3);
            String string3 = query.getString(2);
            if (StringUtil.isEmptyOrNull(str)) {
                return j3;
            }
            str = str.toLowerCase();
            String lowerCase = string2 != null ? string2.toLowerCase() : "";
            String lowerCase2 = string != null ? string.toLowerCase() : "";
            if ("com.google".equals(string3) && str.equals(lowerCase) && str.equals(lowerCase2)) {
                return j3;
            }
            if (j2 == -1) {
                j2 = j3;
            }
            if (j == -1 && string3.equals("com.google") && lowerCase.length() > 0 && lowerCase.equals(lowerCase2)) {
                j = j3;
            }
        }
        query.close();
        if (j < 0) {
            j = j2;
        }
        return j;
    }

    @RequiresPermission(allOf = {"android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR"})
    public static long addNewCalendarEvent(Context context, String str, long j, long j2, String str2, String str3, String str4) {
        return addNewCalendarEvent(context, str, j, j2, str2, str3, str4, (String) null);
    }

    @RequiresPermission(allOf = {"android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR"})
    public static long addNewCalendarEvent(Context context, String str, long j, long j2, String str2, String str3, String str4, String str5) {
        try {
            long selectDefaultCalendar = selectDefaultCalendar(context, str);
            if (selectDefaultCalendar < 0) {
                return -1;
            }
            return addNewCalendarEvent(context, selectDefaultCalendar, j, j2, str2, str3, str4, str5);
        } catch (Exception unused) {
            return -1;
        }
    }

    @RequiresPermission(allOf = {"android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR"})
    public static long addNewCalendarEvent(Context context, CalendarResult calendarResult, String str, long j, long j2, String str2, String str3, String str4, String str5) {
        try {
            long selectDefaultCalendar = selectDefaultCalendar(context, calendarResult, str);
            if (selectDefaultCalendar < 0) {
                return -1;
            }
            return addNewCalendarEvent(context, selectDefaultCalendar, j, j2, str2, str3, str4, str5);
        } catch (Exception unused) {
            return -1;
        }
    }

    @RequiresPermission("android.permission.WRITE_CALENDAR")
    private static long addNewCalendarEvent(Context context, long j, long j2, long j3, String str, String str2, String str3, String str4) {
        if (context == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("dtstart", Long.valueOf(j2));
        contentValues.put("title", str);
        contentValues.put(BoxItem.FIELD_DESCRIPTION, str2);
        contentValues.put("calendar_id", Long.valueOf(j));
        contentValues.put("eventLocation", str3);
        contentValues.put("eventTimezone", TimeZone.getDefault().getID());
        contentValues.put("hasAlarm", Integer.valueOf(1));
        if (!StringUtil.isEmptyOrNull(str4)) {
            contentValues.put("rrule", str4);
            StringBuilder sb = new StringBuilder();
            sb.append("P");
            sb.append((j3 - j2) / 1000);
            sb.append(ExifInterface.LATITUDE_SOUTH);
            contentValues.put("duration", sb.toString());
        } else {
            contentValues.put("dtend", Long.valueOf(j3));
        }
        Uri insert = context.getContentResolver().insert(Events.CONTENT_URI, contentValues);
        if (insert == null) {
            return -1;
        }
        long parseLong = Long.parseLong(insert.getLastPathSegment());
        addReminder(context, parseLong, 15);
        return parseLong;
    }

    @RequiresPermission("android.permission.WRITE_CALENDAR")
    private static long addReminder(Context context, long j, int i) {
        if (context == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("minutes", Integer.valueOf(i));
        contentValues.put("event_id", Long.valueOf(j));
        contentValues.put("method", Integer.valueOf(1));
        Uri insert = context.getContentResolver().insert(Reminders.CONTENT_URI, contentValues);
        if (insert == null) {
            return -1;
        }
        return Long.parseLong(insert.getLastPathSegment());
    }

    @Nullable
    public static String buildCalendarRrule(Date date, EventRepeatType eventRepeatType, int i) {
        if (eventRepeatType == EventRepeatType.NONE) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("FREQ=");
        boolean z = true;
        switch (eventRepeatType) {
            case DAILY:
                sb.append("DAILY;");
                break;
            case WORKDAY:
                sb.append("WEEKLY;");
                break;
            case WEEKLY:
                sb.append("WEEKLY;");
                break;
            case BIWEEKLY:
                sb.append("WEEKLY;INTERVAL=2;");
                break;
            case MONTHLY:
                sb.append("MONTHLY;");
                break;
            case YEARLY:
                sb.append("YEARLY;");
                break;
        }
        z = false;
        if (i > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("COUNT=");
            sb2.append(i);
            sb2.append(";");
            sb.append(sb2.toString());
        }
        sb.append("WKST=SU");
        if (z) {
            sb.append(";");
            sb.append(buildByDay(date));
        }
        if (eventRepeatType == EventRepeatType.WORKDAY) {
            sb.append(";BYDAY=MO,TU,WE,TH,FR");
        }
        return sb.toString();
    }

    @Nullable
    public static String buildCalendarRrule(Date date, EventRepeatType eventRepeatType, Date date2) {
        if (eventRepeatType == EventRepeatType.NONE) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("FREQ=");
        boolean z = true;
        switch (eventRepeatType) {
            case DAILY:
                sb.append("DAILY;");
                break;
            case WORKDAY:
                sb.append("WEEKLY;");
                break;
            case WEEKLY:
                sb.append("WEEKLY;");
                break;
            case BIWEEKLY:
                sb.append("WEEKLY;INTERVAL=2;");
                break;
            case MONTHLY:
                sb.append("MONTHLY;");
                break;
            case YEARLY:
                sb.append("YEARLY;");
                break;
        }
        z = false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        if (date2 != null && date2.getTime() > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("UNTIL=");
            sb2.append(simpleDateFormat.format(date2).replace('-', 'T'));
            sb2.append("Z;");
            sb.append(sb2.toString());
        }
        sb.append("WKST=SU");
        if (z) {
            sb.append(";");
            sb.append(buildByDay(date));
        }
        if (eventRepeatType == EventRepeatType.WORKDAY) {
            sb.append(";BYDAY=MO,TU,WE,TH,FR");
        }
        return sb.toString();
    }

    private static String buildByDay(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int i = instance.get(7) - 1;
        StringBuilder sb = new StringBuilder();
        sb.append("BYDAY=");
        sb.append(DAY_OF_WEEK[i]);
        return sb.toString();
    }

    public static boolean isC2DMCapable(Context context) {
        boolean z = false;
        if ("OPPO".equals(Build.MANUFACTURER)) {
            return false;
        }
        if (hasGSFPackage(context) && hasC2DMPermission(context)) {
            z = true;
        }
        return z;
    }

    public static int getAppVersionCode(Context context) {
        if (context == null) {
            return 0;
        }
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception unused) {
            return 0;
        }
    }

    private static boolean hasC2DMPermission(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return false;
        }
        if (packageManager.checkPermission("com.google.android.c2dm.permission.RECEIVE", context.getPackageName()) == 0) {
            z = true;
        }
        return z;
    }

    private static boolean hasGSFPackage(Context context) {
        if (VERSION.SDK_INT < 8) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(GSF_PACKAGE, 0);
            return true;
        } catch (NameNotFoundException unused) {
            return false;
        }
    }

    @SuppressLint({"NewApi"})
    public static boolean copyText(Context context, CharSequence charSequence) {
        if (context == null) {
            return false;
        }
        try {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService("clipboard");
            if (clipboardManager == null) {
                return false;
            }
            ClipData newPlainText = ClipData.newPlainText(null, charSequence);
            if (newPlainText == null) {
                return false;
            }
            clipboardManager.setPrimaryClip(newPlainText);
            return true;
        } catch (Exception e) {
            ZMLog.m281e(TAG, e, "copy to clipboard failed", new Object[0]);
            return false;
        }
    }

    private static int getJpegRotation(String str) {
        int i = 0;
        try {
            int attributeInt = new android.media.ExifInterface(str).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            if (attributeInt == 6) {
                i = 90;
            } else if (attributeInt == 3) {
                i = 180;
            } else if (attributeInt == 8) {
                i = SubsamplingScaleImageView.ORIENTATION_270;
            }
            return i;
        } catch (Exception unused) {
            return 0;
        }
    }

    public static void addImageToGallery(Context context, File file) {
        addImageToGallery(context, file, IMAGE_MIME_TYPE_JPG);
    }

    public static void addImageToGallery(Context context, File file, String str) {
        if (!StringUtil.isEmptyOrNull(str) && !IMAGE_MIME_TYPE_UNKNOW.equals(str) && context != null && file != null && file.exists()) {
            String absolutePath = file.getAbsolutePath();
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_data", absolutePath);
                contentValues.put("datetaken", Long.valueOf(file.lastModified()));
                contentValues.put("date_added", Long.valueOf(file.lastModified()));
                contentValues.put("date_modified", Long.valueOf(file.lastModified()));
                contentValues.put("mime_type", str);
                contentValues.put("orientation", Integer.valueOf(getJpegRotation(absolutePath)));
                context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            } catch (Exception unused) {
            }
        }
    }

    @Nullable
    public static MimeType getMimeTypeOfFile(String str) {
        Object[][] objArr;
        String fileExtendName = getFileExtendName(str);
        if (fileExtendName == null) {
            return null;
        }
        String lowerCase = fileExtendName.toLowerCase(Locale.US);
        for (Object[] objArr2 : mimeTypesTable) {
            if (StringUtil.isSameString(lowerCase, (String) objArr2[0])) {
                return new MimeType((String) objArr2[1], ((Integer) objArr2[2]).intValue());
            }
        }
        return null;
    }

    public static int getFileTypeFromMimType(String str) {
        Object[][] objArr;
        if (StringUtil.isEmptyOrNull(str)) {
            return -1;
        }
        if (MIMTYPE_FOLDER.equals(str)) {
            return 100;
        }
        for (Object[] objArr2 : mimeTypesTable) {
            String str2 = (String) objArr2[1];
            int intValue = ((Integer) objArr2[2]).intValue();
            if (StringUtil.isSameString(str2, str)) {
                return intValue;
            }
        }
        return -1;
    }

    public static boolean isValidFileType(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        for (Object[] objArr : mimeTypesTable) {
            if (str.toLowerCase().endsWith((String) objArr[0])) {
                return true;
            }
        }
        return false;
    }

    public static String getFileExtendNameFromMimType(String str) {
        Object[][] objArr;
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        if (MIMTYPE_FOLDER.equals(str)) {
            return "";
        }
        for (Object[] objArr2 : mimeTypesTable) {
            String str2 = (String) objArr2[1];
            ((Integer) objArr2[2]).intValue();
            if (StringUtil.isSameString(str2, str)) {
                return (String) objArr2[0];
            }
        }
        return "";
    }

    @Nullable
    public static String getFileExtendName(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf < 0) {
            return null;
        }
        return str.substring(lastIndexOf);
    }

    public static boolean isFileExtBlockedBySecurityPolicy(String str) {
        String fileExtendName = getFileExtendName(str);
        if (StringUtil.isEmptyOrNull(fileExtendName)) {
            return false;
        }
        return Pattern.compile(".(exe)").matcher(fileExtendName).find();
    }

    @Nullable
    public static String getPathLastName(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        if ("/".equals(str)) {
            return str;
        }
        if (str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        int lastIndexOf = str.lastIndexOf("/");
        if (lastIndexOf >= 0) {
            str = str.substring(lastIndexOf + 1);
        }
        return str;
    }

    public static boolean hasActivityToOpenFile(Context context, File file) {
        if (context == null || file == null) {
            return false;
        }
        MimeType mimeTypeOfFile = getMimeTypeOfFile(file.getName());
        if (mimeTypeOfFile != null && hasActivityForIntent(context, buildIntentToOpenFile(context, file, mimeTypeOfFile))) {
            return true;
        }
        return false;
    }

    public static boolean openFile(Context context, File file) {
        return openFile(context, file, false);
    }

    public static boolean openFile(Context context, File file, boolean z) {
        if (context == null || file == null || !file.exists()) {
            return false;
        }
        File createPublicShareCopyFile = createPublicShareCopyFile(context, file);
        if (createPublicShareCopyFile == null) {
            return false;
        }
        Intent buildIntentToOpenFile = buildIntentToOpenFile(context, createPublicShareCopyFile);
        if (buildIntentToOpenFile == null) {
            return false;
        }
        return action(context, buildIntentToOpenFile, z);
    }

    public static boolean shareFile(Context context, File file) {
        return shareFile(context, file, false);
    }

    public static boolean shareFile(Context context, File file, boolean z) {
        if (context == null || file == null || !file.exists()) {
            return false;
        }
        File createPublicShareCopyFile = createPublicShareCopyFile(context, file);
        if (createPublicShareCopyFile == null) {
            return false;
        }
        Intent buildIntentToShareFile = buildIntentToShareFile(context, createPublicShareCopyFile);
        if (buildIntentToShareFile == null) {
            return false;
        }
        return action(context, buildIntentToShareFile, z);
    }

    private static boolean action(Context context, Intent intent, boolean z) {
        if (z) {
            if (!hasActivityForIntentIgnoreSelf(context, intent)) {
                return false;
            }
        } else if (!hasActivityForIntent(context, intent)) {
            return false;
        }
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    @Nullable
    private static File createPublicShareCopyFile(Context context, File file) {
        File file2;
        String name = file.getName();
        String publicDataPath = FileUtils.getPublicDataPath(context);
        if (publicDataPath == null) {
            return null;
        }
        if (!name.startsWith(publicDataPath)) {
            file2 = FileUtils.createPublicShareCopyFile(context, file.getName());
            if (file2 == null) {
                return null;
            }
            FileUtils.copyFile(file.getAbsolutePath(), file2.getAbsolutePath());
        } else {
            file2 = file;
        }
        return file2;
    }

    private static Intent buildIntentToOpenFile(Context context, File file) {
        MimeType mimeTypeOfFile = getMimeTypeOfFile(file.getName());
        if (mimeTypeOfFile == null) {
            return null;
        }
        return buildIntentToOpenFile(context, file, mimeTypeOfFile);
    }

    @Nullable
    private static Intent buildIntentToOpenFile(Context context, File file, MimeType mimeType) {
        if (file == null || mimeType == null) {
            return null;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(268435456);
        Uri uriForFile = FileProvider.getUriForFile(context, context.getResources().getString(C4409R.string.zm_app_provider), file);
        intent.addFlags(1);
        intent.setDataAndType(uriForFile, mimeType.mimeType);
        if (mimeType.fileType == 6 || mimeType.fileType == 5) {
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
        }
        return intent;
    }

    private static Intent buildIntentToShareFile(Context context, File file) {
        MimeType mimeTypeOfFile = getMimeTypeOfFile(file.getName());
        if (mimeTypeOfFile == null) {
            return null;
        }
        return buildIntentToShareFile(context, file, mimeTypeOfFile);
    }

    @Nullable
    private static Intent buildIntentToShareFile(Context context, File file, MimeType mimeType) {
        if (file == null || mimeType == null) {
            return null;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addFlags(268435456);
        Uri uriForFile = FileProvider.getUriForFile(context, context.getResources().getString(C4409R.string.zm_app_provider), file);
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.setType(mimeType.mimeType);
        if (mimeType.fileType == 6 || mimeType.fileType == 5) {
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
        }
        return intent;
    }

    public static boolean hasActiviyToSelectImage(Context context) {
        return hasActivityForIntent(context, createIntentForSelectImage());
    }

    public static Intent createIntentForSelectImage() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT", null);
        intent.setType("image/*");
        return intent;
    }

    @Nullable
    public static List<ResolveInfo> queryActivitiesForSelectImage(Context context) {
        if (context == null) {
            return null;
        }
        return queryActivitiesForIntent(context, createIntentForSelectImage());
    }

    public static boolean selectImageNoDefault(Activity activity, int i, int i2) {
        return selectImageNoDefault(null, activity, i, i2);
    }

    public static boolean selectImageNoDefault(Fragment fragment, int i, int i2) {
        return selectImageNoDefault(fragment, null, i, i2);
    }

    private static boolean selectImageNoDefault(final Fragment fragment, final Activity activity, int i, final int i2) {
        if (activity == null && fragment == null) {
            return false;
        }
        if (activity == null) {
            activity = fragment.getActivity();
        }
        if (activity == null) {
            return false;
        }
        List<ResolveInfo> queryActivitiesForSelectImage = queryActivitiesForSelectImage(activity);
        if (queryActivitiesForSelectImage == null) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo appItem : queryActivitiesForSelectImage) {
            arrayList.add(new AppItem(appItem));
        }
        final AppListAdapter appListAdapter = new AppListAdapter((ZMActivity) activity, arrayList);
        Builder builder = new Builder(activity);
        if (i == 0) {
            i = C4409R.string.zm_select_a_image;
        }
        ZMAlertDialog create = builder.setTitle(i).setAdapter(appListAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                AndroidAppUtil.onSelectSelectImageItem(activity, fragment, (AppItem) appListAdapter.getItem(i), i2);
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
        return true;
    }

    /* access modifiers changed from: private */
    public static void onSelectSelectImageItem(Activity activity, Fragment fragment, AppItem appItem, int i) {
        Intent createIntentForSelectImage = createIntentForSelectImage();
        if (!(appItem == null || appItem.appInfo == null || appItem.appInfo.activityInfo == null)) {
            createIntentForSelectImage.setClassName(appItem.appInfo.activityInfo.packageName, appItem.appInfo.activityInfo.name);
        }
        if (fragment != null) {
            try {
                fragment.startActivityForResult(createIntentForSelectImage, i);
            } catch (Exception unused) {
            }
        } else if (activity != null) {
            activity.startActivityForResult(createIntentForSelectImage, i);
        }
    }

    @Nullable
    public static List<ResolveInfo> queryActivitiesForIntent(Context context, Intent intent) {
        List<ResolveInfo> list = null;
        if (context == null || intent == null) {
            return null;
        }
        try {
            list = context.getPackageManager().queryIntentActivities(intent, 65536);
        } catch (Exception unused) {
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public static boolean hasActivityForIntentIgnoreSelf(Context context, Intent intent) {
        boolean z = false;
        if (context == null || intent == null) {
            return false;
        }
        List<ResolveInfo> queryActivitiesForIntent = queryActivitiesForIntent(context, intent);
        if (queryActivitiesForIntent == null || queryActivitiesForIntent.size() <= 0) {
            return false;
        }
        int i = 0;
        for (ResolveInfo resolveInfo : queryActivitiesForIntent) {
            if (!TextUtils.equals(resolveInfo.activityInfo.packageName, context.getApplicationContext().getPackageName())) {
                i++;
            }
        }
        if (i > 0) {
            z = true;
        }
        return z;
    }

    public static boolean hasActivityForIntent(Context context, Intent intent) {
        boolean z = false;
        if (context == null || intent == null) {
            return false;
        }
        List queryActivitiesForIntent = queryActivitiesForIntent(context, intent);
        if (queryActivitiesForIntent != null && queryActivitiesForIntent.size() > 0) {
            z = true;
        }
        return z;
    }

    @Nullable
    public static PackageInfo getPackageInfo(Context context, String str) {
        if (VERSION.SDK_INT < 8 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        try {
            return context.getPackageManager().getPackageInfo(str, 0);
        } catch (NameNotFoundException unused) {
            return null;
        }
    }

    public static int getIconForFile(String str) {
        if (str == null) {
            return C4409R.C4410drawable.zm_ic_filetype_unknown;
        }
        return getIconByExt(getFileExtendName(str));
    }

    public static int getFileTypeByExt(String str) {
        Object[][] objArr;
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        String lowerCase = str.toLowerCase(Locale.US);
        if (lowerCase.charAt(0) != '.') {
            StringBuilder sb = new StringBuilder();
            sb.append(".");
            sb.append(lowerCase);
            lowerCase = sb.toString();
        }
        for (Object[] objArr2 : mimeTypesTable) {
            if (StringUtil.isSameString(lowerCase, (String) objArr2[0])) {
                return ((Integer) objArr2[2]).intValue();
            }
        }
        return -1;
    }

    public static int getIconByExt(String str) {
        Object[][] objArr;
        if (TextUtils.isEmpty(str)) {
            return C4409R.C4410drawable.zm_ic_filetype_unknown;
        }
        String lowerCase = str.toLowerCase(Locale.US);
        if (lowerCase.charAt(0) != '.') {
            StringBuilder sb = new StringBuilder();
            sb.append(".");
            sb.append(lowerCase);
            lowerCase = sb.toString();
        }
        for (Object[] objArr2 : mimeTypesTable) {
            if (StringUtil.isSameString(lowerCase, (String) objArr2[0])) {
                return ((Integer) objArr2[3]).intValue();
            }
        }
        return C4409R.C4410drawable.zm_ic_filetype_unknown;
    }

    public static boolean isLocaleCN(@NonNull Context context) {
        if (hasSimCard(context)) {
            boolean isChinaSimCard = isChinaSimCard(context);
            if (isChinaSimCard) {
                isChinaSimCard = TimeZoneUtil.isProbablyInChina();
            }
            return isChinaSimCard;
        }
        Locale localDefault = CompatUtils.getLocalDefault();
        boolean z = false;
        if (localDefault == null) {
            return false;
        }
        String country = localDefault.getCountry();
        if (country != null && country.equalsIgnoreCase(CountryCodeUtil.CN_ISO_COUNTRY_CODE) && TimeZoneUtil.isProbablyInChina()) {
            z = true;
        }
        return z;
    }

    public static boolean isChinaUser(@NonNull Context context) {
        if (hasSimCard(context) && isChinaSimCard(context)) {
            return true;
        }
        Locale localDefault = CompatUtils.getLocalDefault();
        boolean z = false;
        if (localDefault == null) {
            return false;
        }
        String country = localDefault.getCountry();
        if (country != null && country.equalsIgnoreCase(CountryCodeUtil.CN_ISO_COUNTRY_CODE) && TimeZoneUtil.isProbablyInChina()) {
            z = true;
        }
        return z;
    }

    public static boolean isChinaSimCard(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(BoxUser.FIELD_PHONE);
        if (telephonyManager == null) {
            return false;
        }
        try {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (!isOperatorEmpty(networkOperator) && (networkOperator.startsWith("460") || networkOperator.startsWith("461"))) {
                return true;
            }
            String simOperator = telephonyManager.getSimOperator();
            if (isOperatorEmpty(simOperator) || (!simOperator.startsWith("460") && !simOperator.startsWith("461"))) {
                return false;
            }
            return true;
        } catch (Exception unused) {
        }
    }

    private static boolean isOperatorEmpty(String str) {
        return str == null || str.equals("") || str.toLowerCase(Locale.US).contains("null");
    }

    private static boolean hasSimCard(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(BoxUser.FIELD_PHONE);
        boolean z = false;
        if (telephonyManager == null) {
            return false;
        }
        try {
            if (telephonyManager.getSimState() != 1) {
                z = true;
            }
            return z;
        } catch (Exception unused) {
            return false;
        }
    }

    @Nullable
    private static String getSimOperator(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(BoxUser.FIELD_PHONE);
        if (telephonyManager == null) {
            return null;
        }
        try {
            return telephonyManager.getSimOperator();
        } catch (Exception unused) {
            return null;
        }
    }
}
