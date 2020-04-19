package com.zipow.videobox.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import p021us.zoom.androidlib.util.StringUtil;

public class PreferenceUtil {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String ACCOUNT_LOGIN = "account_login";
    public static final String ALERT_IM_MSG = "alert_im_msg";
    public static final String ALWAYS_SHOW_MEETING_TOOLBAR = "always_show_meeting_toolbar";
    public static final String ANNOTATE_SHAPE_RECOGNITION = "annotate_shape_recognition";
    public static final String ANNOTATE_TEXT_BOLD = "annotate_text_bold";
    public static final String ANNOTATE_TEXT_COLOR = "annotate_text_color";
    public static final String ANNOTATE_TEXT_ITALIC = "annotate_text_italic";
    public static final String ANNOTATE_TEXT_SIZE = "annotate_text_size";
    public static final String BOOKMARKS = "bookmarks";
    public static final String CALLIN_SELECTED_COUNTRY_ID = "callin.selected_country_id";
    public static final String CALLIN_SELECTED_NUMBER = "callin.selected_number";
    public static final String CALLME_PHONE_NUMBER = "callme.phone_number";
    public static final String CALLME_SELECT_COUNTRY = "callme.select_country";
    public static final String CALLOUT_INVITE_SELECT_COUNTRY = "callout_invite.select_country";
    public static final String CAMERA_ANTIBANDING = "camera_antibanding";
    public static final String CAMERA_CAPABILITIES = "camera_capabilities";
    public static final String CAMERA_IS_FREEZED = "camera_is_freezed";
    public static final String CLOSED_CAPTION_ENABLED = "closed_caption_enabled";
    public static final String COMMON_EMOJI_DOWNLOAD_ID = "common_emoji_download_id";
    public static final String COMMON_EMOJI_PENDING_VERSION = "common_emoji_peding_version";
    public static final String COMMON_EMOJI_VERSION = "common_emoji_version";
    public static final String CONF_AUTO_CONNECT_AUDIO = "conf_auto_connect_audio";
    public static final String DRIVE_MODE_ENABLED = "drive_mode_enabled";
    public static final String EMAIL = "email";
    public static final String ENABLE_KUBI_DEVICE = "enable_kubi_device";
    public static final String FCM_REGISTRATION_TOKEN = "fcm_registration_token";
    public static final String FCM_REGISTRATION_VERSION_CODE = "fcm_registration_id_version_code";
    public static final String FIRST_OPEN_CONTACTS = "first_open_contacts";
    public static final String FIRST_OPEN_SIP = "first_open_sip";
    public static final String FIRST_SENT_MY_NOTES = "first_sent_my_notes";
    public static final String FREQUENTLY_USED_EMOJI = "FREQUENTLY_USED_EMOJI";
    public static final String FTE_ADDRBOOK_ADD_CONTACT = "fte_addrbook_add_contact";
    public static final String FTE_CHATS_LIST_ADD_CONTACTS = "fte_chats_list_add_contacts";
    public static final String FTE_CHATS_LIST_FTE = "fte_chats_list_fte";
    public static final String FTE_CHATS_LIST_MY_NOTE = "fte_chats_list_my_note";
    public static final String FTE_CHAT_SESSION_SAY_HI = "say_hi_";
    public static final String GALLERY_VIEW_CAPACITY = "gallery_view_capcity";
    public static final String GCM_BAIDU_CHANNEL_ID = "gcm_baidu_channel_id";
    public static final String GCM_BAIDU_USER_ID = "gcm_baidu_user_id";
    public static final String GCM_REGISTRATION_ID = "gcm_registration_id";
    public static final String GCM_REGISTRATION_ID_TIMESTAMP = "gcm_registration_id_timestamp";
    public static final String GCM_REGISTRATION_TOKEN = "gcm_registration_token";
    public static final String GCM_REGISTRATION_VERSION_CODE = "gcm_registration_id_version_code";
    public static final String HIDE_NO_VIDEO_USERS = "hide_no_video_users";
    public static final String IM_GIPHY_OPTION = "giphy_opthion";
    public static final String IM_LINK_PREVIEW_DESCRIPTION = "im_link_preview_description";
    public static final String IM_NOTIFICATION_MESSAGE_PREVIEW = "im_notification_message_preview";
    public static final String IM_OUT_OF_STORAGE_ALERT = "out_of_storage_alert";
    public static final String IM_TIMED_CHAT = "timed_chat";
    public static final String INCOMING_CALL_PLAY_ALERT_SOUND = "incoming_call_play_alert_sound";
    public static final String INCOMING_CALL_PLAY_ALERT_VIBRATE = "incoming_call_play_alert_vibrate";
    public static final String IS_DEVICE_NAME_CUSTOMIZED = "is_device_name_customized";
    public static final String KEYBOARD_HEIGHT = "keyboard_height";
    public static final String LARGE_SHARE_VIDEO_SCENE_MODE = "large_share_video_scene_mode";
    public static final String LAST_CLEAR_NEW_TIP_ON_SETTINGS_TAB_TIME = "last_clear_new_tip_on_settings_tab_time";
    public static final String LAST_GET_NEW_VERSION_NOTIFICATION_TIME = "last_get_new_version_notification_time";
    public static final String LAST_REQUEST_CONTACT_PERMISSION_TIME = "last_request_contact_permission_time";
    public static final String LAST_SHOW_NEW_VERSION_TIME = "last_show_new_version_time";
    public static final String LAST_SHOW_SET_PROFILE_TIME = "last_show_set_profile_time";
    public static final String LOCAL_AVATAR = "local_avatar";
    public static final String NEW_VERSION_ON_SERVER = "new_version_on_server";
    public static final String NO_GALLERY_VIDEOS_VIEW = "no_gallery_videos_view";
    public static final String NO_LEAVE_MEETING_BUTTON_FOR_HOST = "no_leave_meeting_button_for_host";
    public static final String NO_VIDEO_TILE_ON_SHARE_SCREEN = "no_video_tile_on_share_screen";
    public static final String PBX_AD_HOC_RECORDING = "pbx_ad_hoc_recording";
    public static final String PBX_FIRST_IGNORE = "PBX_FIRST_IGNORE";
    public static final String PBX_FLOAT_WINDOW_LOCATION = "PBX_FLOAT_WINDOW_LOCATION";
    public static final String PBX_FRAGMENT_INDEX = "pbx_fragment_index";
    public static final String PBX_SIP_SWITCH_TO_CARRIER_NUMBER = "sip_switch_to_carrier_number";
    public static final String PBX_SLA_FIRST_USE = "PBX_SLA_FIRST_USE";
    public static final String PERMISSION_PROMT_FOR_MEETING = "permission_promt_for_meeting";
    public static final String PLAY_ALERT_SOUND = "play_alert_sound";
    public static final String PLAY_ALERT_VIBRATE = "play_alert_vibrate";
    private static final String PREFERENCE_NAME = "config";
    public static final String RECENT_JID = "recent_jid";
    public static final String RECENT_ZOOM_JID = "recent_zoom_jid";
    private static final String SAY_HI_PREFERENCE_NAME = "say_hi";
    public static final String SCHEDULE_OPT_ADD_TO_CALENDAR = "schedule_opt.add_to_caclendar";
    public static final String SCHEDULE_OPT_ATTENDEE_VIDEO_ON = "schedule_opt.attendee_video_on";
    public static final String SCHEDULE_OPT_AUDIO_OPTION = "schedule_opt.audio_option";
    public static final String SCHEDULE_OPT_CN_MEETING = "schedule_opt.cn_meeting";
    public static final String SCHEDULE_OPT_ENABLE_WAITING_ROOM = "schedule_opt.enable_waiting_room";
    public static final String SCHEDULE_OPT_HOST_VIDEO_ON = "schedule_opt.host_video_on";
    public static final String SCHEDULE_OPT_JBH = "schedule_opt.jbh";
    public static final String SCHEDULE_OPT_JOIN_AUTH_ID = "schedule_opt.join_auth_id";
    public static final String SCHEDULE_OPT_JOIN_USER_TYPE = "schedule_opt.join_user_type";
    public static final String SCHEDULE_OPT_USE_PMI = "schedule_opt.use_pmi";
    public static final String SCHEDULE_VAL_JBH_TIME = "schedule_val.jbh_time";
    public static final String SCREEN_NAME = "screen_name";
    public static final String SDK_CONF_NOTIFICATION_CHANNEL_ID = "sdk_conf_notification_channel_id";
    public static final String SENT_MY_NOTE_JID = "sent_my_note_jid";
    public static final String SHOW_OFFLINE_USER = "show_offline_user";
    public static final String SHOW_TIMER_ENABLED = "show_timer_enabled";
    public static final String SWITCH_VIDEO_LAYOUT_ACCORDING_TO_USER_COUNT = "switch_video_layout_according_to_user_count";
    public static final String SWITCH_VIDEO_LAYOUT_USER_COUNT_THRESHOLD = "switch_video_layout_user_count_threshold";
    public static final String SYSTEM_DEVICE_ID = "system_device_id";
    public static final String SYSTEM_NOTIFICATION_DELETE_FLAG = "system_notification_delete_flag";
    public static final String UNIQUE_ID = "unique_id";
    public static final String UNREAD_START_FIRST = "UNREAD_START_FIRST";
    public static final String UN_SUPPORT_EMOJI_DIALOG_SHOW_TIMES = "un_support_emoji_dialog_show_times";
    public static final String WHITEBOARD_TEXT_BOLD = "whiteboard_text_bold";
    public static final String WHITEBOARD_TEXT_COLOR = "whiteboard_text_color";
    public static final String WHITEBOARD_TEXT_ITALIC = "whiteboard_text_italic";
    public static final String WHITEBOARD_TEXT_SIZE = "whiteboard_text_size";
    public static final String WIFI_MAC_ADDRESS = "wifi_mac_address";
    public static final String ZM_MM_GROUP_DESC_JOIN_FIRST = "ZM_MM_Group_Desc_Join_First";
    public static final String ZOOM_CONFIG_FOR_VCODE = "ZOOM_ConfigForVCode";
    public static final String ZOOM_PHONE_PREFERENCE_NAME = "zoom_phone";
    @Nullable
    private static Context s_context;

    public static void initialize(@Nullable Context context) {
        s_context = context;
    }

    public static void removeValue(String str) {
        Editor edit = s_context.getSharedPreferences("config", 0).edit();
        edit.remove(str);
        edit.commit();
    }

    public static void saveMapStringValues(Map<String, String> map) {
        Editor edit = s_context.getSharedPreferences("config", 0).edit();
        for (String str : map.keySet()) {
            edit.putString(str, (String) map.get(str));
        }
        edit.commit();
    }

    @Nullable
    public static HashMap<String, String> readMapStringValues(@Nullable Set<String> set, String str) {
        if (set == null || set.size() == 0) {
            return null;
        }
        SharedPreferences sharedPreferences = s_context.getSharedPreferences("config", 4);
        HashMap<String, String> hashMap = new HashMap<>();
        for (String str2 : set) {
            hashMap.put(str2, sharedPreferences.getString(str2, str));
        }
        return hashMap;
    }

    public static void saveStringValue(String str, @Nullable String str2) {
        Editor edit = s_context.getSharedPreferences("config", 0).edit();
        if (str2 == null) {
            edit.remove(str);
        } else {
            edit.putString(str, str2);
        }
        edit.commit();
    }

    @Nullable
    public static String readStringValue(@Nullable String str, String str2) {
        if (str == null || str.length() == 0) {
            return null;
        }
        return s_context.getSharedPreferences("config", 4).getString(str, str2);
    }

    public static void saveBooleanValue(String str, boolean z) {
        Editor edit = s_context.getSharedPreferences("config", 0).edit();
        edit.putBoolean(str, z);
        edit.commit();
    }

    public static boolean readBooleanValue(@Nullable String str, boolean z) {
        return (str == null || str.length() == 0) ? z : s_context.getSharedPreferences("config", 4).getBoolean(str, z);
    }

    public static void saveLongValue(String str, long j) {
        Editor edit = s_context.getSharedPreferences("config", 0).edit();
        edit.putLong(str, j);
        edit.commit();
    }

    public static long readLongValue(@NonNull String str, long j) {
        return s_context.getSharedPreferences("config", 4).getLong(str, j);
    }

    public static void saveIntValue(String str, int i) {
        Editor edit = s_context.getSharedPreferences("config", 0).edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public static int readIntValue(@NonNull String str, int i) {
        return s_context.getSharedPreferences("config", 4).getInt(str, i);
    }

    public static boolean containsKey(@Nullable String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return s_context.getSharedPreferences("config", 4).contains(str);
    }

    public static boolean clearKeys(@Nullable String... strArr) {
        if (strArr == null || strArr.length == 0) {
            return false;
        }
        Editor edit = s_context.getSharedPreferences("config", 0).edit();
        for (String str : strArr) {
            if (!StringUtil.isEmptyOrNull(str)) {
                edit.remove(str);
            }
        }
        return edit.commit();
    }

    @Nullable
    public static String getPreferenceName(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return str;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("_");
        sb.append(myself.getJid());
        return sb.toString();
    }

    public static void saveSayHiFTE(String str, boolean z) {
        Editor edit = s_context.getSharedPreferences(getPreferenceName(SAY_HI_PREFERENCE_NAME), 0).edit();
        edit.putBoolean(str, z);
        edit.commit();
    }

    public static boolean readSayHiFTE(@Nullable String str, boolean z) {
        return (str == null || str.length() == 0) ? z : s_context.getSharedPreferences(getPreferenceName(SAY_HI_PREFERENCE_NAME), 4).getBoolean(str, z);
    }

    public static void saveBooleanValue(@NonNull String str, @NonNull String str2, boolean z) {
        Editor edit = s_context.getSharedPreferences(str, 0).edit();
        edit.putBoolean(str2, z);
        edit.commit();
    }

    public static boolean readBooleanValue(@NonNull String str, @Nullable String str2, boolean z) {
        return (str2 == null || str2.length() == 0) ? z : s_context.getSharedPreferences(str, 4).getBoolean(str2, z);
    }

    public static void saveIntValue(@NonNull String str, @NonNull String str2, int i) {
        Editor edit = s_context.getSharedPreferences(str, 0).edit();
        edit.putInt(str2, i);
        edit.commit();
    }

    public static int readIntValue(@NonNull String str, @Nullable String str2, int i) {
        return (str2 == null || str2.length() == 0) ? i : s_context.getSharedPreferences(str, 4).getInt(str2, i);
    }

    public static void saveStringValue(@NonNull String str, @NonNull String str2, String str3) {
        Editor edit = s_context.getSharedPreferences(str, 0).edit();
        edit.putString(str2, str3);
        edit.commit();
    }

    public static String readStringValue(@NonNull String str, @Nullable String str2, String str3) {
        if (str2 == null || str2.length() == 0) {
            return null;
        }
        return s_context.getSharedPreferences(str, 4).getString(str2, str3);
    }
}
