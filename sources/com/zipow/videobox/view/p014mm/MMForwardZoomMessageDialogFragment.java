package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.Iterator;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMForwardZoomMessageDialogFragment */
public class MMForwardZoomMessageDialogFragment extends DialogFragment {
    private static final String ARG_SESSION_ID = "sessionid";
    private static final String ARG_SHAREE = "sharee";
    private static final String ARG_XMPP_ID = "xmppid";
    public static final String RESULT_ARG_REQUEST_ID = "messageId";

    public static void showForwardMessageDialog(@Nullable FragmentManager fragmentManager, @Nullable ArrayList<String> arrayList, String str, String str2, @Nullable Fragment fragment, int i) {
        if (fragmentManager != null && arrayList != null && arrayList.size() != 0 && !StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            MMForwardZoomMessageDialogFragment mMForwardZoomMessageDialogFragment = new MMForwardZoomMessageDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(ARG_SHAREE, arrayList);
            bundle.putString(ARG_XMPP_ID, str);
            bundle.putString(ARG_SESSION_ID, str2);
            mMForwardZoomMessageDialogFragment.setArguments(bundle);
            if (fragment != null) {
                mMForwardZoomMessageDialogFragment.setTargetFragment(fragment, i);
            }
            mMForwardZoomMessageDialogFragment.show(fragmentManager, MMForwardZoomMessageDialogFragment.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        ArrayList stringArrayList = arguments != null ? arguments.getStringArrayList(ARG_SHAREE) : null;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        StringBuffer stringBuffer = new StringBuffer();
        if (!(stringArrayList == null || zoomMessenger == null)) {
            Iterator it = stringArrayList.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                ZoomGroup groupById = zoomMessenger.getGroupById(str);
                if (groupById != null) {
                    String groupDisplayName = groupById.getGroupDisplayName(getActivity());
                    if (!StringUtil.isEmptyOrNull(groupDisplayName)) {
                        stringBuffer.append(groupDisplayName);
                        stringBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                    }
                } else {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                    if (buddyWithJID != null) {
                        String screenName = buddyWithJID.getScreenName();
                        if (!StringUtil.isEmptyOrNull(screenName)) {
                            stringBuffer.append(screenName);
                            stringBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                        }
                    }
                }
            }
        }
        String str2 = "";
        if (stringBuffer.length() > 0) {
            str2 = stringBuffer.substring(0, stringBuffer.length() - 1);
        }
        return new Builder(getActivity()).setMessage(getString(C4558R.string.zm_lbl_content_send_to, str2)).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MMForwardZoomMessageDialogFragment.this.doForwardMessage();
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).create();
    }

    private boolean checkE2E(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        if (zoomMessenger == null) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
        if (sessionById == null) {
            return false;
        }
        int e2eGetMyOption = zoomMessenger.e2eGetMyOption();
        if (!sessionById.isGroup()) {
            ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
            if (sessionBuddy != null && !sessionBuddy.isRobot()) {
                if (e2eGetMyOption == 2) {
                    return true;
                }
                if (sessionBuddy.getE2EAbility(e2eGetMyOption) == 2) {
                    z = true;
                }
                return z;
            }
        } else if (e2eGetMyOption == 2) {
            return true;
        } else {
            ZoomGroup sessionGroup = sessionById.getSessionGroup();
            if (sessionGroup != null) {
                return sessionGroup.isForceE2EGroup();
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0096  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00bb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doForwardMessage() {
        /*
            r14 = this;
            android.os.Bundle r0 = r14.getArguments()
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            java.lang.String r1 = "sharee"
            java.util.ArrayList r1 = r0.getStringArrayList(r1)
            java.lang.String r2 = "sessionid"
            java.lang.String r2 = r0.getString(r2)
            java.lang.String r3 = "xmppid"
            java.lang.String r0 = r0.getString(r3)
            if (r1 == 0) goto L_0x00d3
            int r3 = r1.size()
            if (r3 != 0) goto L_0x0023
            goto L_0x00d3
        L_0x0023:
            com.zipow.videobox.ptapp.PTApp r3 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r4 = r3.getZoomMessenger()
            if (r4 != 0) goto L_0x002e
            return
        L_0x002e:
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            r12 = 0
            r13 = 0
            if (r3 != 0) goto L_0x008e
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 != 0) goto L_0x008e
            com.zipow.videobox.ptapp.mm.ZoomChatSession r2 = r4.getSessionById(r2)
            if (r2 == 0) goto L_0x008e
            com.zipow.videobox.ptapp.mm.ZoomMessage r0 = r2.getMessageByXMPPGuid(r0)
            if (r0 == 0) goto L_0x008e
            java.lang.Object r2 = r1.get(r13)
            java.lang.String r2 = (java.lang.String) r2
            com.zipow.videobox.ptapp.mm.ZoomChatSession r2 = r4.getSessionById(r2)
            if (r2 == 0) goto L_0x008e
            boolean r2 = r2.isGroup()
            if (r2 == 0) goto L_0x0063
            java.lang.Object r2 = r1.get(r13)
            java.lang.String r2 = (java.lang.String) r2
            r5 = r2
            r6 = r12
            goto L_0x006b
        L_0x0063:
            java.lang.Object r2 = r1.get(r13)
            java.lang.String r2 = (java.lang.String) r2
            r6 = r2
            r5 = r12
        L_0x006b:
            java.lang.CharSequence r2 = r0.getBody()
            com.zipow.videobox.ptapp.IMProtos$FontStyte r0 = r0.getFontStyte()
            java.lang.CharSequence r7 = com.zipow.videobox.view.p014mm.message.FontStyleHelper.getCharSequenceFromMMMessageItem(r2, r0)
            java.lang.Object r0 = r1.get(r13)
            java.lang.String r0 = (java.lang.String) r0
            boolean r8 = r14.checkE2E(r0)
            r9 = 0
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_msg_e2e_fake_message
            java.lang.String r10 = r14.getString(r0)
            r11 = 0
            java.lang.String r0 = r4.sendText(r5, r6, r7, r8, r9, r10, r11)
            goto L_0x008f
        L_0x008e:
            r0 = r12
        L_0x008f:
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r0)
            r2 = -1
            if (r1 == 0) goto L_0x00bb
            androidx.fragment.app.Fragment r0 = r14.getTargetFragment()
            if (r0 == 0) goto L_0x00a3
            int r1 = r14.getTargetRequestCode()
            r0.onActivityResult(r1, r13, r12)
        L_0x00a3:
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_alert_share_message_failed_93748
            java.lang.String r0 = r14.getString(r0)
            com.zipow.videobox.fragment.ErrorMsgDialog r0 = com.zipow.videobox.fragment.ErrorMsgDialog.newInstance(r0, r2)
            androidx.fragment.app.FragmentManager r1 = r14.getFragmentManager()
            java.lang.Class<com.zipow.videobox.fragment.ErrorMsgDialog> r2 = com.zipow.videobox.fragment.ErrorMsgDialog.class
            java.lang.String r2 = r2.getName()
            r0.show(r1, r2)
            goto L_0x00d2
        L_0x00bb:
            android.content.Intent r1 = new android.content.Intent
            r1.<init>()
            java.lang.String r3 = "messageId"
            r1.putExtra(r3, r0)
            androidx.fragment.app.Fragment r0 = r14.getTargetFragment()
            if (r0 == 0) goto L_0x00d2
            int r3 = r14.getTargetRequestCode()
            r0.onActivityResult(r3, r2, r1)
        L_0x00d2:
            return
        L_0x00d3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMForwardZoomMessageDialogFragment.doForwardMessage():void");
    }
}
