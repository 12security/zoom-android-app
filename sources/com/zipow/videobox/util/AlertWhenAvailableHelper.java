package com.zipow.videobox.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.eventbus.ZMAlertAvailable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.MMChatsListItem;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CollectionsUtil.Filter;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class AlertWhenAvailableHelper {
    private static final String TAG = "AlertWhenAvailableHelper";
    private static volatile AlertWhenAvailableHelper instance;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_RemoveAvailableAlert(String str, boolean z) {
            AlertWhenAvailableHelper.this.refreshRingBellOnUI(str);
        }

        public void Indicate_AddAvailableAlert(String str, boolean z) {
            AlertWhenAvailableHelper.this.refreshRingBellOnUI(str);
        }

        public void Indicate_GetAllAvailableAlert() {
            AlertWhenAvailableHelper.this.handleAvailableAlertForOnlineBuddies();
        }

        public void Indicate_OnlineBuddies(List<String> list) {
            AlertWhenAvailableHelper.this.handleAvailableAlertForOnlineBuddies();
        }
    };

    /* access modifiers changed from: private */
    public void handleAvailableAlertForOnlineBuddies() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            List queryAvailableAlertBuddyAll = zoomMessenger.queryAvailableAlertBuddyAll();
            if (!CollectionsUtil.isListEmpty(queryAvailableAlertBuddyAll)) {
                for (String str : CollectionsUtil.filter(queryAvailableAlertBuddyAll, new Filter<String>() {
                    public boolean apply(String str) {
                        return AlertWhenAvailableHelper.this.isDesktopPresenceOnline(str);
                    }
                })) {
                    removeJidFromAlertQueen(str);
                    showAlertNotification(str);
                }
                for (String addJidToAlertQueen : CollectionsUtil.filter(queryAvailableAlertBuddyAll, new Filter<String>() {
                    public boolean apply(String str) {
                        return !AlertWhenAvailableHelper.this.isDesktopPresenceOnline(str);
                    }
                })) {
                    addJidToAlertQueen(addJidToAlertQueen);
                }
            }
        }
    }

    public static AlertWhenAvailableHelper getInstance() {
        if (instance == null) {
            synchronized (AlertWhenAvailableHelper.class) {
                if (instance == null) {
                    instance = new AlertWhenAvailableHelper();
                }
            }
        }
        return instance;
    }

    private AlertWhenAvailableHelper() {
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
    }

    public void checkAndAddToAlertQueen(@NonNull ZMActivity zMActivity, @Nullable MMChatsListItem mMChatsListItem) {
        if (mMChatsListItem != null) {
            checkAndAddToAlertQueen(zMActivity, mMChatsListItem.getFromContact());
        }
    }

    public void checkAndAddToAlertQueen(@NonNull ZMActivity zMActivity, @Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            checkAndAddToAlertQueen(zMActivity, iMAddrBookItem.getJid());
        }
    }

    public void checkAndAddToAlertQueen(@NonNull ZMActivity zMActivity, String str) {
        if (isConnectionGood() && !StringUtil.isEmptyOrNull(str)) {
            if (isInAlertQueen(str)) {
                removeJidFromAlertQueen(str);
                showTurnOffToast(str);
            } else if (isDesktopPresenceOnline(str)) {
                showErrorToast(zMActivity, str);
            } else {
                addJidToAlertQueen(str);
                showTurnOnToast(str);
            }
        }
    }

    public void refreshRingBellOnUI(String str) {
        EventBus.getDefault().post(new ZMAlertAvailable(str));
    }

    private void showTurnOnToast(String str) {
        Toast.makeText(VideoBoxApplication.getInstance(), VideoBoxApplication.getInstance().getString(C4558R.string.zm_mm_lbl_alert_when_available_hint_65420, new Object[]{getScreenName(str)}), 1).show();
    }

    private void showTurnOffToast(String str) {
        Toast.makeText(VideoBoxApplication.getInstance(), VideoBoxApplication.getInstance().getString(C4558R.string.zm_mm_lbl_alert_when_available_close_hint_65420, new Object[]{getScreenName(str)}), 1).show();
    }

    public void showErrorToast(@NonNull final ZMActivity zMActivity, final String str) {
        if (DialogUtils.isCanShowDialog(zMActivity)) {
            Builder negativeButton = new Builder(zMActivity).setMessage(zMActivity.getString(C4558R.string.zm_mm_lbl_alert_when_available_dialog_title_65420)).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null);
            if (chatEnabled()) {
                negativeButton.setPositiveButton(C4558R.string.zm_mm_lbl_alert_when_available_chat_65420, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertWhenAvailableHelper.this.startChat(zMActivity, str);
                    }
                });
            }
            negativeButton.create().show();
        }
    }

    public void removeJidFromAlertQueen(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.removeAvailableAlertBuddy(str);
        }
    }

    public void addJidToAlertQueen(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.addAvailableAlertBuddy(str);
        }
    }

    public boolean isInAlertQueen(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
        if (buddyWithJID != null) {
            return buddyWithJID.isAvailableAlert();
        }
        return false;
    }

    private ZoomBuddy getZoomBuddy(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        return zoomMessenger.getBuddyWithJID(str);
    }

    public void showAlertNotification(@NonNull String str) {
        FloatWindow.getInstance().dispatchShowAlertAvailable(str);
    }

    private int getPresence(String str) {
        ZoomBuddy zoomBuddy = getZoomBuddy(str);
        if (zoomBuddy != null) {
            return zoomBuddy.getPresence();
        }
        return -1;
    }

    public boolean showAlertWhenAvailable(MMChatsListItem mMChatsListItem) {
        return showAlertWhenAvailable(getJID(mMChatsListItem));
    }

    public boolean showAlertWhenAvailable(String str) {
        boolean z = false;
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(getZoomBuddy(str));
        boolean isConnectionGood = isConnectionGood();
        boolean isFriend = isFriend(str);
        boolean z2 = getZoomBuddy(str) != null;
        boolean isMyself = isMyself(str);
        boolean isZoomRoomContact = fromZoomBuddy != null ? fromZoomBuddy.isZoomRoomContact() : true;
        if (isConnectionGood && isFriend && z2 && !isMyself && !isZoomRoomContact) {
            z = true;
        }
        return z;
    }

    private boolean isMyself(String str) {
        return StringUtil.isSameString(str, getMyJID());
    }

    @Nullable
    private String getMyJID() {
        String str = "";
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return str;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        return myself != null ? myself.getJid() : str;
    }

    public boolean isFriend(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.canSubscribePresenceAlert(str);
    }

    private boolean isConnectionGood() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.isConnectionGood();
    }

    public boolean isDesktopPresenceOnline(String str) {
        if (StringUtil.isEmptyOrNull(str) || getPresence(str) != 3) {
            return false;
        }
        ZoomBuddy zoomBuddy = getZoomBuddy(str);
        IMAddrBookItem iMAddrBookItem = null;
        if (zoomBuddy != null) {
            iMAddrBookItem = IMAddrBookItem.fromZoomBuddy(zoomBuddy);
        }
        if (iMAddrBookItem != null) {
            return iMAddrBookItem.getIsDesktopOnline();
        }
        return false;
    }

    public boolean isImDND() {
        return getPresence(getMyJID()) == 4;
    }

    public boolean isImMeeting() {
        return getPresence(getMyJID()) == 2;
    }

    @Nullable
    public String getMenuString(@NonNull MMChatsListItem mMChatsListItem) {
        return getMenuString(mMChatsListItem.getFromContact());
    }

    @Nullable
    public String getMenuString(@Nullable IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem == null) {
            return null;
        }
        return getMenuString(iMAddrBookItem.getJid());
    }

    @NonNull
    public String getMenuString(String str) {
        boolean isInAlertQueen = isInAlertQueen(str);
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext == null) {
            return "";
        }
        return globalContext.getString(isInAlertQueen ? C4558R.string.zm_mm_lbl_alert_when_available_cancel_65420 : C4558R.string.zm_mm_lbl_alert_when_available_65420);
    }

    @Nullable
    private String getJID(@Nullable MMChatsListItem mMChatsListItem) {
        if (mMChatsListItem == null) {
            return null;
        }
        IMAddrBookItem fromContact = mMChatsListItem.getFromContact();
        if (fromContact != null) {
            return fromContact.getJid();
        }
        return null;
    }

    @NonNull
    private String getScreenName(String str) {
        String str2 = "";
        ZoomBuddy zoomBuddy = getZoomBuddy(str);
        if (zoomBuddy == null) {
            return str2;
        }
        String screenName = zoomBuddy.getScreenName();
        return StringUtil.isEmptyOrNull(screenName) ? "" : screenName;
    }

    /* access modifiers changed from: private */
    public void startChat(ZMActivity zMActivity, String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                if (buddyWithJID != null && !buddyWithJID.isPending()) {
                    MMChatActivity.showAsOneToOneChat(zMActivity, buddyWithJID);
                }
            }
        }
    }

    private boolean chatEnabled() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        return (zoomMessenger == null || zoomMessenger.imChatGetOption() == 2) ? false : true;
    }
}
