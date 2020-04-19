package com.zipow.videobox.util;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.PTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.AppStateMonitor.IAppStateListener;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class FloatWindow implements IAppStateListener {
    private static final int DEFAULT_STATUS_BAR_HEIGHT = 20;
    private static final String TAG = "FloatWindow";
    private static final long[] VIBRATES = {2000, 1000, 2000, 1000};
    private static FloatWindow instance;
    private final String FILE_NAME = PTService.FILE_NAME;
    WeakReference<ZMActivity> activityWeakReference;
    WeakReference<LinearLayout> linearLayoutWeakReference;
    @Nullable
    private Vibrator mVibrator;
    @Nullable
    WeakReference<RelativeLayout> relativeLayoutWeakReference;
    @NonNull
    private Set<String> tempJIDs = new HashSet();
    @NonNull
    private Map<String, String> timeMap = new HashMap();

    public void onAppInactivated() {
    }

    public static FloatWindow getInstance() {
        if (instance == null) {
            synchronized (AlertWhenAvailableHelper.class) {
                if (instance == null) {
                    instance = new FloatWindow();
                }
            }
        }
        return instance;
    }

    private FloatWindow() {
    }

    public boolean dispatchShowAlertAvailable(@Nullable String str) {
        boolean z = true;
        if (StringUtil.isEmptyOrNull(str)) {
            return true;
        }
        saveTime(str);
        if (isPTAPPFront()) {
            showAlertWhenAvailable(str);
            deleteTime(str);
        } else {
            z = tellConfToShow(str);
            if (z) {
                deleteTime(str);
            } else {
                createSyncFile();
                addCallBack();
                saveTempJID(str);
            }
        }
        return z;
    }

    private void saveTime(@NonNull String str) {
        if (!this.timeMap.containsKey(str)) {
            ZMActivity frontActivity = ZMActivity.getFrontActivity();
            if (frontActivity != null) {
                this.timeMap.put(str, getCurrentTime(frontActivity));
            }
        }
    }

    private void deleteTime(String str) {
        this.timeMap.remove(str);
    }

    public void showAllPendingAlertAvailable() {
        if (this.tempJIDs.size() != 0) {
            boolean z = true;
            for (String dispatchShowAlertAvailable : this.tempJIDs) {
                z &= dispatchShowAlertAvailable(dispatchShowAlertAvailable);
            }
            if (z) {
                clearTempJID();
            }
        }
    }

    private void saveTempJID(String str) {
        this.tempJIDs.add(str);
    }

    private void clearTempJID() {
        this.tempJIDs.clear();
    }

    private void createSyncFile() {
        File filesDir = VideoBoxApplication.getInstance().getFilesDir();
        if (filesDir != null) {
            filesDir.mkdir();
            if (filesDir.exists() && filesDir.isDirectory()) {
                String absolutePath = filesDir.getAbsolutePath();
                if (!absolutePath.endsWith("/")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(absolutePath);
                    sb.append("/");
                    absolutePath = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(absolutePath);
                sb2.append(PTService.FILE_NAME);
                try {
                    new File(sb2.toString()).createNewFile();
                } catch (IOException unused) {
                }
            }
        }
    }

    private boolean checkSyncFile() {
        File filesDir = VideoBoxApplication.getInstance().getFilesDir();
        if (filesDir == null) {
            return false;
        }
        filesDir.mkdir();
        if (!filesDir.exists() || !filesDir.isDirectory()) {
            return false;
        }
        String absolutePath = filesDir.getAbsolutePath();
        if (!absolutePath.endsWith("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append(absolutePath);
            sb.append("/");
            absolutePath = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(absolutePath);
        sb2.append(PTService.FILE_NAME);
        File file = new File(sb2.toString());
        if (!file.exists()) {
            return false;
        }
        file.delete();
        return true;
    }

    private void removeCallBack() {
        AppStateMonitor.getInstance().removeListener(this);
    }

    private void addCallBack() {
        AppStateMonitor.getInstance().addListener(this);
    }

    public void showAlertWhenAvailable(String str) {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        checkToShowFloatWindow(frontActivity, getName(str), getAvatarBitmapPath(str), getTime(str), !AccessibilityUtil.isSpokenFeedbackEnabled(frontActivity) && shouldPlaySoundAndVibrate(), str);
    }

    private boolean isPTAPPFront() {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if (frontActivity == null) {
            return false;
        }
        return frontActivity.isActive();
    }

    public boolean tellConfToShow(String str) {
        boolean z = false;
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        String name = getName(str);
        String avatarBitmapPath = getAvatarBitmapPath(str);
        String time = getTime(str);
        IConfService confService = VideoBoxApplication.getInstance().getConfService();
        boolean shouldPlaySoundAndVibrate = shouldPlaySoundAndVibrate();
        if (confService != null) {
            try {
                z = confService.onAlertWhenAvailable(name, avatarBitmapPath, time, shouldPlaySoundAndVibrate, str);
            } catch (RemoteException unused) {
            }
        }
        return z;
    }

    public void checkToShowFloatWindow(ZMActivity zMActivity, String str, String str2, String str3, boolean z, String str4) {
        this.activityWeakReference = new WeakReference<>(zMActivity);
        if (getZMActivity() != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            final String str5 = str;
            final String str6 = str2;
            final String str7 = str3;
            final String str8 = str4;
            final boolean z2 = z;
            C33681 r1 = new Runnable() {
                public void run() {
                    FloatWindow floatWindow = FloatWindow.this;
                    floatWindow.addViewToFloatWindowViewGroup(floatWindow.getZMActivity(), str5, str6, str7, str8);
                    if (z2) {
                        AudioManager audioManager = (AudioManager) VideoBoxApplication.getNonNullInstance().getSystemService("audio");
                        if (audioManager != null) {
                            if (2 == audioManager.getRingerMode()) {
                                FloatWindow.this.playSound();
                                FloatWindow.this.vibrate();
                            } else if (1 == audioManager.getRingerMode()) {
                                FloatWindow.this.vibrate();
                            }
                        }
                    }
                }
            };
            handler.post(r1);
        }
    }

    /* access modifiers changed from: private */
    public void addViewToFloatWindowViewGroup(@NonNull ZMActivity zMActivity, String str, String str2, String str3, String str4) {
        View createItemView = createItemView(zMActivity, str, str2, str3, str4);
        if (getRelativeLayout() == null) {
            createViewGroup(zMActivity);
        }
        addView(createItemView);
        axScreenReader(createItemView, str);
    }

    private void axScreenReader(final View view, String str) {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(view.getContext()) && VideoBoxApplication.getInstance() != null) {
            final String string = VideoBoxApplication.getInstance().getString(C4558R.string.zm_mm_lbl_alert_when_available_notification_65420, new Object[]{str});
            view.post(new Runnable() {
                public void run() {
                    AccessibilityUtil.announceForAccessibilityCompat(view, (CharSequence) string);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void removeViewFromFloatWindowViewGroup(@NonNull ZMActivity zMActivity, @Nullable View view) {
        if (getRelativeLayout() != null) {
            if (view != null) {
                getRelativeLayout().removeView(view);
            }
            if (getRelativeLayout().getChildCount() == 0) {
                clearViewGroup(zMActivity);
            }
        }
    }

    private void clearViewGroup(ZMActivity zMActivity) {
        WindowManager windowManager = zMActivity.getWindowManager();
        if (windowManager != null) {
            windowManager.removeView(getRelativeLayout());
            this.relativeLayoutWeakReference = null;
        }
    }

    private void createViewGroup(ZMActivity zMActivity) {
        WindowManager windowManager = zMActivity.getWindowManager();
        if (windowManager != null) {
            this.relativeLayoutWeakReference = new WeakReference<>(new RelativeLayout(zMActivity));
            LayoutParams layoutParams = new LayoutParams();
            layoutParams.type = 1000;
            layoutParams.format = 1;
            layoutParams.flags |= 1320;
            layoutParams.width = -1;
            layoutParams.height = -2;
            layoutParams.gravity = 48;
            layoutParams.y = getStateBarHeight(zMActivity);
            windowManager.addView(getRelativeLayout(), layoutParams);
        }
    }

    private RelativeLayout getRelativeLayout() {
        WeakReference<RelativeLayout> weakReference = this.relativeLayoutWeakReference;
        if (weakReference != null) {
            return (RelativeLayout) weakReference.get();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public ZMActivity getZMActivity() {
        WeakReference<ZMActivity> weakReference = this.activityWeakReference;
        if (weakReference != null) {
            return (ZMActivity) weakReference.get();
        }
        return null;
    }

    private void addView(View view) {
        if (getRelativeLayout() != null) {
            getRelativeLayout().addView(view);
        }
    }

    private View createItemView(@NonNull final ZMActivity zMActivity, String str, String str2, String str3, String str4) {
        View inflate = View.inflate(zMActivity, C4558R.layout.zm_mm_message_alert_available, null);
        inflate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FloatWindow.this.removeViewFromFloatWindowViewGroup(zMActivity, view);
            }
        });
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.tvTime);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.tvContent);
        AvatarView avatarView = (AvatarView) inflate.findViewById(C4558R.C4560id.avatarView);
        ((TextView) inflate.findViewById(C4558R.C4560id.tvName)).setText(str);
        textView.setText(str3);
        textView2.setText(zMActivity.getString(C4558R.string.zm_mm_lbl_alert_when_available_toast_65420));
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(textView2.getText());
        textView2.setContentDescription(sb.toString());
        avatarView.show(new ParamsBuilder().setName(str, str4).setPath(str2));
        return inflate;
    }

    @Nullable
    private String getName(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null) {
                return buddyWithJID.getScreenName();
            }
        }
        return "";
    }

    @Nullable
    private String getAvatarBitmapPath(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return "";
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
        if (buddyWithJID != null) {
            String localBigPicturePath = buddyWithJID.getLocalBigPicturePath();
            if (ImageUtil.isValidImageFile(localBigPicturePath)) {
                return localBigPicturePath;
            }
            String localPicturePath = buddyWithJID.getLocalPicturePath();
            if (ImageUtil.isValidImageFile(localPicturePath)) {
                return localPicturePath;
            }
        }
        return "";
    }

    private String getCurrentTime(@NonNull ZMActivity zMActivity) {
        return formatTime(zMActivity, System.currentTimeMillis());
    }

    @NonNull
    private String getTime(String str) {
        return StringUtil.safeString((String) this.timeMap.get(str));
    }

    private String formatTime(@NonNull Context context, long j) {
        long currentTimeMillis = System.currentTimeMillis();
        long j2 = currentTimeMillis - 86400000;
        if (TimeUtil.isSameDate(j, currentTimeMillis)) {
            return TimeUtil.formatTime(context, j);
        }
        if (TimeUtil.isSameDate(j, j2)) {
            return context.getString(C4558R.string.zm_lbl_yesterday);
        }
        return TimeUtil.formatDate(context, j);
    }

    private int getStateBarHeight(ZMActivity zMActivity) {
        Resources resources = zMActivity.getResources();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return UIUtil.dip2px(zMActivity, 20.0f);
    }

    /* access modifiers changed from: private */
    public void playSound() {
        Ringtone ringtone = RingtoneManager.getRingtone(VideoBoxApplication.getInstance(), RingtoneManager.getDefaultUri(2));
        if (ringtone != null) {
            ringtone.play();
        }
    }

    /* access modifiers changed from: private */
    public void vibrate() {
        if (this.mVibrator == null) {
            this.mVibrator = (Vibrator) VideoBoxApplication.getInstance().getSystemService("vibrator");
        }
        this.mVibrator.vibrate(VIBRATES, -1);
    }

    private boolean shouldPlaySoundAndVibrate() {
        return !isImDND() && isNotificationChannelEnabled() && (!isImMeeting() || !isImNotificationDisableInMeeting());
    }

    private boolean isNotificationChannelEnabled() {
        return NotificationMgr.isNotificationChannelEnabled(VideoBoxApplication.getInstance());
    }

    private boolean isImDND() {
        return AlertWhenAvailableHelper.getInstance().isImDND();
    }

    private boolean isImMeeting() {
        return AlertWhenAvailableHelper.getInstance().isImMeeting();
    }

    private boolean isImNotificationDisableInMeeting() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr == null) {
            return false;
        }
        return notificationSettingMgr.getInCallSettings();
    }

    public boolean isPresenceOnLine(@Nullable ZoomBuddy zoomBuddy) {
        boolean z = false;
        if (zoomBuddy == null) {
            return false;
        }
        if (zoomBuddy.getPresence() == 3) {
            z = true;
        }
        return z;
    }

    public void onAppActivated() {
        removeCallBack();
        if (checkSyncFile()) {
            showAllPendingAlertAvailable();
        }
    }
}
