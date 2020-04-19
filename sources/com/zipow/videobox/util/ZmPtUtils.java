package com.zipow.videobox.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.RemoteException;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.FingerprintOption;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CountryCodePT;
import com.zipow.videobox.ptapp.PTAppProtos.CountryCodelistProto;
import com.zipow.videobox.ptapp.PTAppProtos.GoogCalendarEventList;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.SBWebServiceErrorCode;
import com.zipow.videobox.ptapp.UrlActionInfo;
import com.zipow.videobox.ptapp.ZoomProductHelper;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.ScheduledMeetingItem;
import com.zipow.videobox.view.adapter.ZMLatestMeetingAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.FingerprintUtil;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.ZMLocaleUtils;
import p021us.zoom.androidlib.widget.ZMSpanny;
import p021us.zoom.videomeetings.C4558R;

public class ZmPtUtils {
    private static final String TAG = "com.zipow.videobox.util.ZmPtUtils";

    public static boolean isImageFile(int i) {
        return i == 1 || i == 5 || i == 1 || i == 4;
    }

    public static int parseVendorFromUrl(String str) {
        UrlActionInfo urlActionInfo = new UrlActionInfo();
        if (PTApp.getInstance().parseAppProtocol(urlActionInfo, str, false)) {
            if (urlActionInfo.isCnMeeting() && !urlActionInfo.isCurrHostCnMeeting()) {
                return 1;
            }
            if (!urlActionInfo.isCnMeeting() && urlActionInfo.isCurrHostCnMeeting()) {
                return 0;
            }
        }
        return 100;
    }

    public static void switchToVendor(int i) {
        if (i == 1 || i == 0) {
            PTApp instance = PTApp.getInstance();
            boolean z = i == 1;
            if (!instance.hasActiveCall()) {
                if (instance.isWebSignedOn()) {
                    instance.logout(0, !z);
                }
                ZoomProductHelper zoomProductHelper = instance.getZoomProductHelper();
                if (zoomProductHelper != null) {
                    zoomProductHelper.vendorSwitchTo(i);
                    instance.setmNeedSwitchVendor(!z);
                }
            }
        }
    }

    public static int getDefaultAudioOption(@NonNull PTUserProfile pTUserProfile) {
        int i = (pTUserProfile.isDisablePSTN() || pTUserProfile.alwaysUseVoipOnlyAsDefaultAudio()) ? 0 : pTUserProfile.alwaysUseTelephonyAsDefaultAudio() ? 1 : pTUserProfile.alwaysUseBothAsDefaultAudio() ? 2 : pTUserProfile.alwaysUse3rdPartyAsDefaultAudio() ? 3 : PreferenceUtil.readIntValue(PreferenceUtil.SCHEDULE_OPT_AUDIO_OPTION, 2);
        if (i != 3 || pTUserProfile.hasSelfTelephony()) {
            if (i != 1 || !pTUserProfile.isDisablePSTN()) {
                return i;
            }
            return 0;
        } else if (pTUserProfile.isDisablePSTN()) {
            return 0;
        } else {
            return 2;
        }
    }

    public static int getMeetingDefaultAudioOption(@NonNull PTUserProfile pTUserProfile, @NonNull ScheduledMeetingItem scheduledMeetingItem) {
        if (pTUserProfile.isLockAudioType()) {
            return getDefaultAudioOption(pTUserProfile);
        }
        int i = 2;
        if (scheduledMeetingItem.isSelfTelephoneOn()) {
            if (pTUserProfile.hasSelfTelephony()) {
                i = 3;
            } else if (pTUserProfile.isDisablePSTN()) {
                i = 0;
            }
        } else if (scheduledMeetingItem.isTelephonyOff() || pTUserProfile.isDisablePSTN()) {
            i = 0;
        } else if (scheduledMeetingItem.isVoipOff()) {
            i = 1;
        }
        return i;
    }

    public static int getMeetingDefaultAudioOption(@NonNull PTUserProfile pTUserProfile, MeetingInfoProto meetingInfoProto) {
        if (meetingInfoProto.getIsSelfTelephonyOn()) {
            if (pTUserProfile.hasSelfTelephony()) {
                return 3;
            }
            if (pTUserProfile.isDisablePSTN()) {
                return 0;
            }
            return 2;
        } else if (meetingInfoProto.getTelephonyOff() || pTUserProfile.isDisablePSTN()) {
            return 0;
        } else {
            if (meetingInfoProto.getVoipOff()) {
                return 1;
            }
            return 2;
        }
    }

    public static boolean isSupportFingerprintAndEnableFingerprintWithUserInfo(@NonNull ZMActivity zMActivity) {
        boolean z = false;
        if (!OsUtil.isAtLeastN() || !new FingerprintUtil(zMActivity).isSupportFingerprint()) {
            return false;
        }
        FingerprintOption readFromPreference = FingerprintOption.readFromPreference();
        if (readFromPreference != null && readFromPreference.isEnableFingerprintWithUserInfo()) {
            z = true;
        }
        return z;
    }

    public static boolean isSupportFingerprintAndDisableFingerprintWithUserInfo(@NonNull ZMActivity zMActivity) {
        boolean z = false;
        if (!OsUtil.isAtLeastN() || !new FingerprintUtil(zMActivity).isSupportFingerprint()) {
            return false;
        }
        FingerprintOption readFromPreference = FingerprintOption.readFromPreference();
        if (readFromPreference != null && readFromPreference.isDisableFingerprintWithUserInfo()) {
            z = true;
        }
        return z;
    }

    public static boolean isCalendarServiceReady() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        return currentUserProfile != null && currentUserProfile.isEnableZoomCalendar() && currentUserProfile.hasCalendarAccountConfigured();
    }

    public static boolean isShouldHideAddCalendar() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        return currentUserProfile != null && (!currentUserProfile.isEnableZoomCalendar() || currentUserProfile.hasCalendarAccountConfigured());
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0092 A[RETURN] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.zipow.videobox.view.ScheduledMeetingItem getScheduleItemFromCalEvent(@androidx.annotation.Nullable com.zipow.videobox.ptapp.PTAppProtos.GoogCalendarEvent r6, java.lang.String r7) {
        /*
            r0 = 0
            if (r6 != 0) goto L_0x0004
            return r0
        L_0x0004:
            long r1 = r6.getMeetNo()
            r3 = 0
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 != 0) goto L_0x001d
            java.lang.String r1 = r6.getPersonalLink()
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r1 == 0) goto L_0x001d
            com.zipow.videobox.view.ScheduledMeetingItem r6 = com.zipow.videobox.view.ScheduledMeetingItem.fromGoogCalendarEventForNotZoomMeeting(r6)
            return r6
        L_0x001d:
            java.lang.String r1 = r6.getStartTime()
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r1 != 0) goto L_0x0093
            java.lang.String r1 = r6.getEndTime()
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r1 == 0) goto L_0x0032
            goto L_0x0093
        L_0x0032:
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.MeetingHelper r1 = r1.getMeetingHelper()
            if (r1 == 0) goto L_0x0045
            long r2 = r6.getMeetNo()
            com.zipow.videobox.ptapp.MeetingInfoProtos$MeetingInfoProto r1 = r1.getMeetingItemByNumber(r2)
            goto L_0x0046
        L_0x0045:
            r1 = r0
        L_0x0046:
            r2 = 0
            if (r1 == 0) goto L_0x0073
            java.util.List r3 = r1.getAlterHostList()
            boolean r4 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r3)
            if (r4 != 0) goto L_0x0073
            java.util.Iterator r3 = r3.iterator()
            r4 = 0
        L_0x0058:
            boolean r5 = r3.hasNext()
            if (r5 == 0) goto L_0x0074
            java.lang.Object r4 = r3.next()
            com.zipow.videobox.ptapp.MeetingInfoProtos$AlterHost r4 = (com.zipow.videobox.ptapp.MeetingInfoProtos.AlterHost) r4
            java.lang.String r5 = r6.getCreatorEmail()
            java.lang.String r4 = r4.getEmail()
            boolean r4 = p021us.zoom.androidlib.util.StringUtil.isSameStringForNotAllowNull(r5, r4)
            if (r4 == 0) goto L_0x0058
            goto L_0x0074
        L_0x0073:
            r4 = 0
        L_0x0074:
            java.lang.String r3 = r6.getCreatorEmail()
            boolean r7 = p021us.zoom.androidlib.util.StringUtil.isSameStringForNotAllowNull(r7, r3)
            if (r1 == 0) goto L_0x008b
            if (r7 != 0) goto L_0x0082
            if (r4 == 0) goto L_0x008b
        L_0x0082:
            com.zipow.videobox.ptapp.MeetingInfoProtos$MeetingInfoProto$MeetingType r7 = r1.getType()
            com.zipow.videobox.ptapp.MeetingInfoProtos$MeetingInfoProto$MeetingType r1 = com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.MeetingType.REPEAT
            if (r7 == r1) goto L_0x008b
            r2 = 1
        L_0x008b:
            if (r2 != 0) goto L_0x0092
            com.zipow.videobox.view.ScheduledMeetingItem r6 = com.zipow.videobox.view.ScheduledMeetingItem.fromGoogCalendarEvent(r6, r4)
            return r6
        L_0x0092:
            return r0
        L_0x0093:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ZmPtUtils.getScheduleItemFromCalEvent(com.zipow.videobox.ptapp.PTAppProtos$GoogCalendarEvent, java.lang.String):com.zipow.videobox.view.ScheduledMeetingItem");
    }

    @Nullable
    public static List<ScheduledMeetingItem> getMeetingListFromCalEvents(@Nullable GoogCalendarEventList googCalendarEventList, @NonNull LongSparseArray<ScheduledMeetingItem> longSparseArray) {
        boolean z;
        if (googCalendarEventList == null || googCalendarEventList.getGoogCalendarEventCount() == 0) {
            return null;
        }
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            return null;
        }
        String email = currentUserProfile.getEmail();
        if (StringUtil.isEmptyOrNull(email)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int googCalendarEventCount = googCalendarEventList.getGoogCalendarEventCount();
        boolean z2 = longSparseArray.size() == 0;
        for (int i = 0; i < googCalendarEventCount; i++) {
            ScheduledMeetingItem scheduleItemFromCalEvent = getScheduleItemFromCalEvent(googCalendarEventList.getGoogCalendarEvent(i), email);
            if (scheduleItemFromCalEvent != null) {
                ScheduledMeetingItem scheduledMeetingItem = (ScheduledMeetingItem) longSparseArray.get(scheduleItemFromCalEvent.getMeetingNo());
                if (scheduledMeetingItem != null && scheduledMeetingItem.isRecurring() && scheduledMeetingItem.getExtendMeetingType() == 2) {
                    z = true;
                } else {
                    z = false;
                }
                if (z2 || longSparseArray.indexOfKey(scheduleItemFromCalEvent.getMeetingNo()) < 0 || z) {
                    if (z) {
                        ScheduledMeetingItem fromMeetingItem = ScheduledMeetingItem.fromMeetingItem(scheduledMeetingItem);
                        fromMeetingItem.setmIsRecCopy(true);
                        fromMeetingItem.setmRecCopyStartTime(scheduleItemFromCalEvent.getStartTime());
                        arrayList.add(fromMeetingItem);
                    } else {
                        arrayList.add(scheduleItemFromCalEvent);
                    }
                }
            }
        }
        return arrayList;
    }

    @Nullable
    public static List<ScheduledMeetingItem> getLatestUpcomingMeetingItems() {
        long j;
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper == null) {
            return null;
        }
        int filteredMeetingCount = meetingHelper.getFilteredMeetingCount();
        TreeMap treeMap = new TreeMap();
        TreeMap treeMap2 = new TreeMap();
        long currentTimeMillis = System.currentTimeMillis();
        LongSparseArray longSparseArray = new LongSparseArray();
        if (filteredMeetingCount > 0) {
            j = -1;
            for (int i = 0; i < filteredMeetingCount; i++) {
                MeetingInfoProto filteredMeetingItemByIndex = meetingHelper.getFilteredMeetingItemByIndex(i);
                if (filteredMeetingItemByIndex != null) {
                    ScheduledMeetingItem fromMeetingInfo = ScheduledMeetingItem.fromMeetingInfo(filteredMeetingItemByIndex);
                    if (fromMeetingInfo.getExtendMeetingType() != 1) {
                        longSparseArray.put(fromMeetingInfo.getMeetingNo(), fromMeetingInfo);
                        if (!fromMeetingInfo.isRecurring() || (fromMeetingInfo.getRepeatType() != 0 && (fromMeetingInfo.getRepeatEndTime() <= 0 || fromMeetingInfo.getRepeatEndTime() - currentTimeMillis >= -600000))) {
                            long startTime = fromMeetingInfo.getStartTime();
                            if (fromMeetingInfo.isRecurring()) {
                                startTime = getStartTimeBaseCurrentTime(currentTimeMillis, fromMeetingInfo);
                                if (TimeUtil.isSameDate(startTime, currentTimeMillis)) {
                                    fromMeetingInfo.setmIsRecCopy(true);
                                    fromMeetingInfo.setmRecCopyStartTime(startTime);
                                }
                            }
                            if (TimeUtil.isSameDate(startTime, currentTimeMillis)) {
                                long j2 = startTime - currentTimeMillis;
                                int i2 = (j2 > 0 ? 1 : (j2 == 0 ? 0 : -1));
                                if (i2 >= 0 && (j == -1 || (j >= 0 && j2 <= j))) {
                                    List list = (List) treeMap.get(Long.valueOf(j2));
                                    if (list == null) {
                                        list = new ArrayList();
                                        treeMap.put(Long.valueOf(j2), list);
                                    }
                                    list.add(fromMeetingInfo);
                                    j = j2;
                                } else if (i2 < 0 && j2 >= -600000) {
                                    List list2 = (List) treeMap2.get(Long.valueOf(j2));
                                    if (list2 == null) {
                                        list2 = new ArrayList();
                                        treeMap2.put(Long.valueOf(j2), list2);
                                    }
                                    list2.add(fromMeetingInfo);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            j = -1;
        }
        if (isCalendarServiceReady()) {
            List<ScheduledMeetingItem> meetingListFromCalEvents = getMeetingListFromCalEvents(meetingHelper.getCalendarEvents(), longSparseArray);
            if (!CollectionsUtil.isListEmpty(meetingListFromCalEvents)) {
                for (ScheduledMeetingItem scheduledMeetingItem : meetingListFromCalEvents) {
                    if (scheduledMeetingItem != null) {
                        long realStartTime = scheduledMeetingItem.getRealStartTime();
                        if (TimeUtil.isSameDate(realStartTime, currentTimeMillis)) {
                            long j3 = realStartTime - currentTimeMillis;
                            int i3 = (j3 > 0 ? 1 : (j3 == 0 ? 0 : -1));
                            if (i3 >= 0) {
                                if (j == -1 || (j >= 0 && j3 <= j)) {
                                    List list3 = (List) treeMap.get(Long.valueOf(j3));
                                    if (list3 == null) {
                                        list3 = new ArrayList();
                                        treeMap.put(Long.valueOf(j3), list3);
                                    }
                                    list3.add(scheduledMeetingItem);
                                    j = j3;
                                }
                            }
                            if (i3 < 0) {
                                if (j3 >= -600000) {
                                    List list4 = (List) treeMap2.get(Long.valueOf(j3));
                                    if (list4 == null) {
                                        list4 = new ArrayList();
                                        treeMap2.put(Long.valueOf(j3), list4);
                                    }
                                    list4.add(scheduledMeetingItem);
                                }
                            }
                        }
                    }
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        if (treeMap2.size() > 0) {
            arrayList.addAll((Collection) treeMap2.get(treeMap2.lastKey()));
        }
        if (treeMap.size() > 0) {
            if (CollectionsUtil.isListEmpty(arrayList)) {
                arrayList.addAll((Collection) treeMap.get(treeMap.firstKey()));
            } else if (((Long) treeMap.firstKey()).longValue() <= ZMLatestMeetingAdapter.UPCOMING_MEETING_CHECK_INTERVAL) {
                arrayList.addAll((Collection) treeMap.get(treeMap.firstKey()));
            }
        }
        return arrayList;
    }

    public static boolean isEnableIM() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        return (!PTApp.getInstance().hasZoomMessenger() || zoomMessenger == null || zoomMessenger.imChatGetOption() == 2) ? false : true;
    }

    public static boolean isIMChatOptionChanaged() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        return PTApp.getInstance().hasZoomMessenger() && zoomMessenger != null && zoomMessenger.isIMChatOptionChanged();
    }

    public static long getStartTimeBaseCurrentTime(long j, @NonNull ScheduledMeetingItem scheduledMeetingItem) {
        long startTime = scheduledMeetingItem.getStartTime();
        if (startTime >= j) {
            return startTime;
        }
        int repeatType = scheduledMeetingItem.getRepeatType();
        if (repeatType == 1) {
            return (((long) TimeUtil.dateDiff(j, startTime)) * 86400000) + startTime;
        }
        if (repeatType == 2) {
            int dateDiff = TimeUtil.dateDiff(j, startTime);
            if (dateDiff % 7 == 0) {
                return (((long) dateDiff) * 86400000) + startTime;
            }
            return (((long) (((dateDiff / 7) + 1) * 7)) * 86400000) + startTime;
        } else if (repeatType == 3) {
            int dateDiff2 = TimeUtil.dateDiff(j, startTime);
            if (dateDiff2 % 14 == 0) {
                return (((long) dateDiff2) * 86400000) + startTime;
            }
            return (((long) (((dateDiff2 / 14) + 1) * 14)) * 86400000) + startTime;
        } else if (repeatType == 4) {
            int monthsDiff = TimeUtil.monthsDiff(startTime, j);
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(startTime);
            instance.add(2, monthsDiff);
            return instance.getTimeInMillis();
        } else {
            int yearsDiff = TimeUtil.yearsDiff(startTime, j);
            Calendar instance2 = Calendar.getInstance();
            instance2.setTimeInMillis(startTime);
            instance2.add(1, yearsDiff);
            return instance2.getTimeInMillis();
        }
    }

    @Nullable
    public static CountryCodeItem getDefaultAutoCallCountryCode(@Nullable Context context) {
        boolean z;
        CountryCodeItem countryCodeItem;
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            return null;
        }
        CountryCodeItem readFromPreference = CountryCodeItem.readFromPreference(PreferenceUtil.CALLME_SELECT_COUNTRY);
        if (readFromPreference == null || StringUtil.isEmptyOrNull(readFromPreference.isoCountryCode)) {
            String isoCountryCode = CountryCodeUtil.getIsoCountryCode(context);
            if (isoCountryCode == null) {
                return null;
            }
            readFromPreference = new CountryCodeItem(CountryCodeUtil.isoCountryCode2PhoneCountryCode(isoCountryCode), isoCountryCode, new Locale("", isoCountryCode.toLowerCase(Locale.US)).getDisplayCountry());
        }
        CountryCodelistProto callinCountryCodes = currentUserProfile.getCallinCountryCodes();
        if (callinCountryCodes == null) {
            return null;
        }
        List callinCountryCodesList = callinCountryCodes.getCallinCountryCodesList();
        if (CollectionsUtil.isListEmpty(callinCountryCodesList)) {
            return null;
        }
        Iterator it = callinCountryCodesList.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            if (readFromPreference.isoCountryCode.equalsIgnoreCase(((CountryCodePT) it.next()).getId())) {
                z = true;
                break;
            }
        }
        if (!z) {
            CountryCodePT countryCodePT = (CountryCodePT) callinCountryCodesList.get(0);
            String code = countryCodePT.getCode();
            countryCodeItem = new CountryCodeItem(code.startsWith("+") ? code.substring(1) : code, countryCodePT.getId(), countryCodePT.getName(), countryCodePT.getNumber(), countryCodePT.getDisplaynumber(), countryCodePT.getCalltype());
        } else {
            countryCodeItem = readFromPreference;
        }
        return countryCodeItem;
    }

    public static boolean isRequiredPasswordForUpdateMeeting(boolean z, boolean z2) {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        boolean z3 = false;
        if (currentUserProfile == null) {
            return false;
        }
        boolean isLockPMIRequirePassword = currentUserProfile.isLockPMIRequirePassword();
        boolean z4 = currentUserProfile.isEnableRequirePassword() && currentUserProfile.isLockScheduleRequirePassword() && !z2;
        boolean z5 = (isLockPMIRequirePassword || !currentUserProfile.isSupportFeatureRequirePassword()) && z && z2 && currentUserProfile.isEnableForcePMIJBHWithPassword();
        boolean z6 = isLockPMIRequirePassword && currentUserProfile.isEnablePMIRequirePassword() && z2;
        if (z4 || z5 || z6) {
            z3 = true;
        }
        return z3;
    }

    public static boolean isRequiredWebPassword(boolean z, boolean z2) {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        return currentUserProfile != null && ((((currentUserProfile.isEnableForcePMIJBHWithPassword() && z) || currentUserProfile.isEnablePMIRequirePassword()) && z2) || (currentUserProfile.isEnableRequirePassword() && !z2));
    }

    public static boolean isNeedHidePassword(boolean z) {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        boolean z2 = false;
        if (currentUserProfile == null) {
            return false;
        }
        boolean isLockPMIRequirePassword = currentUserProfile.isLockPMIRequirePassword();
        if ((!currentUserProfile.isEnableRequirePassword() && currentUserProfile.isLockScheduleRequirePassword() && !z) || (z && isLockPMIRequirePassword && !currentUserProfile.isEnablePMIRequirePassword() && !currentUserProfile.isEnableForcePMIJBHWithPassword())) {
            z2 = true;
        }
        return z2;
    }

    public static boolean isLockedPassword(boolean z, boolean z2) {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        boolean z3 = false;
        if (currentUserProfile == null) {
            return false;
        }
        boolean isLockPMIRequirePassword = currentUserProfile.isLockPMIRequirePassword();
        if ((currentUserProfile.isEnableRequirePassword() && currentUserProfile.isLockScheduleRequirePassword() && !z) || (z && isLockPMIRequirePassword && ((!z2 && currentUserProfile.isEnablePMIRequirePassword()) || (currentUserProfile.isEnableForcePMIJBHWithPassword() && z2)))) {
            z3 = true;
        }
        return z3;
    }

    public static boolean isPMIRequirePasswordOff(boolean z) {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        boolean z2 = false;
        if (currentUserProfile == null) {
            return false;
        }
        if (z && !currentUserProfile.isEnablePMIRequirePassword() && !currentUserProfile.isEnableForcePMIJBHWithPassword()) {
            z2 = true;
        }
        return z2;
    }

    public static void joinMeeting(@NonNull Context context, long j, @Nullable String str, @Nullable String str2) {
        String str3 = "";
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        String userName = currentUserProfile != null ? currentUserProfile.getUserName() : str3;
        if (j != 0) {
            ConfActivity.joinById(context, j, userName, "", str2, false, false);
        } else {
            ConfActivity.joinById(context, 0, userName, str, str2, false, false);
        }
    }

    @Nullable
    public static ScheduledMeetingItem getPMIMeetingItem() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper == null) {
            return null;
        }
        MeetingInfoProto pmiMeetingItem = meetingHelper.getPmiMeetingItem();
        if (pmiMeetingItem == null) {
            return null;
        }
        return ScheduledMeetingItem.fromMeetingInfo(pmiMeetingItem);
    }

    @Nullable
    public static String getMyZoomId() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            return null;
        }
        return currentUserProfile.getUserID();
    }

    public static long getPMINumber() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper == null) {
            return getRoomMeetingID();
        }
        MeetingInfoProto pmiMeetingItem = meetingHelper.getPmiMeetingItem();
        if (pmiMeetingItem == null) {
            return getRoomMeetingID();
        }
        return pmiMeetingItem.getMeetingNumber();
    }

    public static long getRoomMeetingID() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            return 0;
        }
        return currentUserProfile.getRoomMeetingID();
    }

    public static void onCallError(long j) {
        IConfService confService = VideoBoxApplication.getInstance().getConfService();
        if (confService != null) {
            try {
                confService.sinkPTAppCustomEvent(1, j);
            } catch (RemoteException unused) {
            }
        }
    }

    @NonNull
    public static String errorCodeToMessageForJoinFail(@NonNull Resources resources, int i, int i2) {
        switch (i) {
            case 8:
                return resources.getString(C4558R.string.zm_msg_conffail_callnotthere_confirm);
            case 9:
                if (i2 > 0) {
                    return resources.getString(C4558R.string.zm_msg_conffail_userfull_confirm, new Object[]{Integer.valueOf(i2)});
                }
                return resources.getString(C4558R.string.zm_msg_conffail_unknownerror_confirm, new Object[]{Integer.valueOf(i)});
            case 10:
                return resources.getString(C4558R.string.zm_msg_conffail_needupdate_confirm);
            case 11:
                return resources.getString(C4558R.string.zm_msg_conffail_no_mmr_confirm);
            case 12:
                return resources.getString(C4558R.string.zm_msg_conffail_locked_confirm);
            case 13:
                return resources.getString(C4558R.string.zm_msg_conffail_single_meeting_restricted_confirm);
            case 14:
                return resources.getString(C4558R.string.zm_msg_conffail_single_meeting_restricted_jbh_confirm);
            default:
                switch (i) {
                    case 19:
                        return resources.getString(C4558R.string.zm_msg_conffail_webinar_register_full);
                    case 20:
                        return resources.getString(C4558R.string.zm_msg_conffail_webinar_register_with_host_email);
                    case 21:
                        return resources.getString(C4558R.string.zm_msg_conffail_webinar_register_with_panelist_email);
                    case 22:
                        return resources.getString(C4558R.string.zm_msg_conffail_webinar_register_denied);
                    case 23:
                        return resources.getString(C4558R.string.zm_msg_conffail_webinar_register_enforce_login);
                    case 24:
                        return resources.getString(C4558R.string.zm_msg_conffail_certificate_changed, new Object[]{ZMDomainUtil.getZmUrlWebServerPostfix(), resources.getString(C4558R.string.zm_firewall_support_url)});
                    default:
                        switch (i) {
                            case 27:
                                return resources.getString(C4558R.string.zm_msg_conffail_meeting_name_unvalid);
                            case 28:
                                return resources.getString(C4558R.string.zm_msg_conffail_join_webinar_withsameemail);
                            default:
                                switch (i) {
                                    case SBWebServiceErrorCode.SB_ERROR_WS_OVER_TIME /*5003*/:
                                    case SBWebServiceErrorCode.SB_ERROR_CANNOT_RESOLVE_HOST /*5004*/:
                                        break;
                                    default:
                                        switch (i) {
                                            case 1:
                                                return resources.getString(C4558R.string.zm_msg_conffail_neterror_confirm, new Object[]{Integer.valueOf(i2)});
                                            case 3:
                                                return resources.getString(C4558R.string.zm_msg_conffail_retry_confirm);
                                            case 6:
                                                return resources.getString(C4558R.string.zm_msg_conffail_callover_confirm);
                                            case 61:
                                                return resources.getString(C4558R.string.zm_msg_conffail_cannot_rejoin_by_removed_44379);
                                            case SBWebServiceErrorCode.SB_ERROR_WS_OVER_TIME_3 /*10107000*/:
                                            case SBWebServiceErrorCode.SB_ERROR_WS_OVER_TIME_2 /*100006000*/:
                                            case SBWebServiceErrorCode.SB_ERROR_WS_OVER_TIME_1 /*1006007000*/:
                                                break;
                                            default:
                                                return resources.getString(C4558R.string.zm_msg_conffail_unknownerror_confirm, new Object[]{Integer.valueOf(i)});
                                        }
                                }
                                return resources.getString(C4558R.string.zm_msg_unable_to_connect_50129, new Object[]{Integer.valueOf(i)});
                        }
                }
        }
    }

    public static InvitationItem enhanceInvitationItem(InvitationItem invitationItem) {
        ZoomBuddy zoomBuddy;
        if (!StringUtil.isEmptyOrNull(invitationItem.getFromUserScreenName())) {
            return invitationItem;
        }
        String senderJID = invitationItem.getSenderJID();
        String callerPhoneNumber = invitationItem.getCallerPhoneNumber();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (senderJID.indexOf(64) > 0) {
                zoomBuddy = zoomMessenger.getBuddyWithJID(senderJID);
            } else {
                zoomBuddy = zoomMessenger.getBuddyWithPhoneNumber(senderJID);
            }
            if (zoomBuddy != null) {
                String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(zoomBuddy, null);
                if (StringUtil.isEmptyOrNull(buddyDisplayName) && !StringUtil.isEmptyOrNull(callerPhoneNumber)) {
                    Contact firstContactByPhoneNumber = ABContactsCache.getInstance().getFirstContactByPhoneNumber(callerPhoneNumber);
                    if (firstContactByPhoneNumber != null) {
                        callerPhoneNumber = firstContactByPhoneNumber.displayName;
                    }
                    buddyDisplayName = callerPhoneNumber;
                }
                if (!StringUtil.isEmptyOrNull(buddyDisplayName)) {
                    return InvitationItem.newBuilder(invitationItem).setFromUserScreenName(buddyDisplayName).build();
                }
            }
        }
        return invitationItem;
    }

    public static void handleActionNosIncomingCall(String str, String str2) {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            if (VideoBoxApplication.getInstance() == null) {
                VideoBoxApplication.initialize(VideoBoxApplication.getGlobalContext(), false, 0);
            }
            VideoBoxApplication.getInstance().initPTMainboard();
            PTApp.getInstance().autoSignin();
        }
        PTApp.getInstance().nos_NotificationReceived(str2, str);
    }

    public static boolean checkNetWork(@NonNull Fragment fragment) {
        if (NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            return true;
        }
        SimpleMessageDialog.newInstance(C4558R.string.zm_alert_network_disconnected).show(fragment.getFragmentManager(), SimpleMessageDialog.class.getName());
        return false;
    }

    public static void initPrivacyAndTerms(@NonNull final ZMActivity zMActivity, @NonNull TextView textView) {
        final String string = zMActivity.getString(C4558R.string.zm_title_privacy_policy);
        final String string2 = zMActivity.getString(C4558R.string.zm_msg_terms_service_137212);
        ZMSpanny zMSpanny = new ZMSpanny(zMActivity.getString(C4558R.string.zm_lbl_cn_login_privacy_137212, new Object[]{string, string2}));
        zMSpanny.setSpans(string, new StyleSpan(1), new ForegroundColorSpan(zMActivity.getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB)), new ClickableSpan() {
            public void onClick(@NonNull View view) {
                String uRLByType = PTApp.getInstance().getURLByType(ZMLocaleUtils.isChineseLanguage() ? 20 : 21);
                if (!StringUtil.isEmptyOrNull(uRLByType)) {
                    ZMWebPageUtil.startWebPage(zMActivity, uRLByType, string);
                }
            }
        });
        zMSpanny.setSpans(string2, new StyleSpan(1), new ForegroundColorSpan(zMActivity.getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB)), new ClickableSpan() {
            public void onClick(@NonNull View view) {
                String uRLByType = PTApp.getInstance().getURLByType(ZMLocaleUtils.isChineseLanguage() ? 22 : 23);
                if (!StringUtil.isEmptyOrNull(uRLByType)) {
                    ZMWebPageUtil.startWebPage(zMActivity, uRLByType, string2);
                }
            }
        });
        textView.setText(zMSpanny);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
