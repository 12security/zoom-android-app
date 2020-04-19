package com.zipow.videobox.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.widget.ZMTip;

public class ChatTip extends ZMTipFragment implements OnClickListener {
    public static final String ARG_ANCHOR_ID = "anchorId";
    public static final String ARG_AVATAR = "avatar";
    public static final String ARG_CHAT_TIP_INDEX = "chatTipIndex";
    public static final String ARG_MESSAGE = "message";
    public static final String ARG_MESSAGE_ID = "messageId";
    public static final String ARG_NAME = "name";
    public static final String ARG_RECEIVER = "receiver";
    public static final String ARG_SENDER = "sender";
    private static final int MAX_CHAT_TIP_NUMBER = 2;
    private static int gChatTipIndex;

    public static class ChatTipComparator implements Comparator<ChatTip> {
        public int compare(@NonNull ChatTip chatTip, @NonNull ChatTip chatTip2) {
            Bundle arguments = chatTip.getArguments();
            long j = 0;
            long j2 = arguments != null ? (long) arguments.getInt("chatTipIndex", 0) : 0;
            Bundle arguments2 = chatTip2.getArguments();
            if (arguments2 != null) {
                j = (long) arguments2.getInt("chatTipIndex", 0);
            }
            return (int) (j2 - j);
        }
    }

    public static void show(@Nullable FragmentManager fragmentManager, String str, String str2, String str3, long j, long j2, int i, String str4) {
        if (fragmentManager != null) {
            dismissExceedChatTips(fragmentManager);
            Bundle bundle = new Bundle();
            bundle.putString("avatar", str);
            bundle.putString("name", str2);
            bundle.putString("message", str3);
            bundle.putLong(ARG_SENDER, j);
            bundle.putLong(ARG_RECEIVER, j2);
            bundle.putInt("anchorId", i);
            int i2 = gChatTipIndex;
            gChatTipIndex = i2 + 1;
            bundle.putInt("chatTipIndex", i2);
            bundle.putString("messageId", str4);
            ChatTip chatTip = new ChatTip();
            chatTip.setArguments(bundle);
            chatTip.show(fragmentManager, ChatTip.class.getName(), 6000);
            fragmentManager.executePendingTransactions();
        }
    }

    private static void dismissExceedChatTips(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        for (Fragment fragment : fragments) {
            if (fragment instanceof ChatTip) {
                i2++;
                ChatTip chatTip = (ChatTip) fragment;
                arrayList.add(chatTip);
                chatTip.setTransparent();
            }
        }
        if (i2 >= 2) {
            Collections.sort(arrayList, new ChatTipComparator());
            while (i < arrayList.size()) {
                ((ChatTip) arrayList.get(i)).dismiss();
                i2--;
                if (i2 >= 2) {
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public static boolean isShown(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        if (((ChatTip) fragmentManager.findFragmentByTag(ChatTip.class.getName())) != null) {
            z = true;
        }
        return z;
    }

    public static boolean dismiss(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment instanceof ChatTip) {
                ((ChatTip) fragment).dismiss();
                z = true;
            }
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x013a  */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public p021us.zoom.androidlib.widget.ZMTip onCreateTip(@androidx.annotation.NonNull android.content.Context r16, @androidx.annotation.NonNull android.view.LayoutInflater r17, android.os.Bundle r18) {
        /*
            r15 = this;
            r0 = r15
            int r1 = p021us.zoom.videomeetings.C4558R.layout.zm_chat_tip
            r2 = 0
            r3 = r17
            android.view.View r1 = r3.inflate(r1, r2)
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.content
            android.view.View r3 = r1.findViewById(r3)
            int r4 = p021us.zoom.videomeetings.C4558R.C4560id.avatarView
            android.view.View r4 = r1.findViewById(r4)
            com.zipow.videobox.view.AvatarView r4 = (com.zipow.videobox.view.AvatarView) r4
            int r5 = p021us.zoom.videomeetings.C4558R.C4560id.txtScreenName
            android.view.View r5 = r1.findViewById(r5)
            android.widget.TextView r5 = (android.widget.TextView) r5
            int r6 = p021us.zoom.videomeetings.C4558R.C4560id.txtMessage
            android.view.View r6 = r1.findViewById(r6)
            android.widget.TextView r6 = (android.widget.TextView) r6
            android.os.Bundle r7 = r15.getArguments()
            if (r7 != 0) goto L_0x002f
            return r2
        L_0x002f:
            java.lang.String r8 = "avatar"
            java.lang.String r8 = r7.getString(r8)
            com.zipow.videobox.view.AvatarView$ParamsBuilder r9 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r9.<init>()
            com.zipow.videobox.view.AvatarView$ParamsBuilder r9 = r9.setPath(r8)
            r4.show(r9)
            java.lang.String r9 = "receiver"
            long r9 = r7.getLong(r9)
            java.lang.String r11 = "messageId"
            java.lang.String r11 = r7.getString(r11)
            com.zipow.videobox.confapp.ConfMgr r12 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            boolean r12 = r12.isConfConnected()
            r13 = 1
            r14 = 0
            if (r12 == 0) goto L_0x006b
            com.zipow.videobox.confapp.ConfMgr r12 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmConfContext r12 = r12.getConfContext()
            if (r12 == 0) goto L_0x006b
            boolean r12 = r12.isWebinar()
            if (r12 == 0) goto L_0x006b
            r12 = 1
            goto L_0x006c
        L_0x006b:
            r12 = 0
        L_0x006c:
            if (r12 == 0) goto L_0x00e1
            com.zipow.videobox.confapp.ConfMgr r9 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.ConfChatMessage r9 = r9.getChatMessageItemByID(r11)
            if (r9 != 0) goto L_0x0079
            return r2
        L_0x0079:
            java.lang.String r10 = r9.getReceiverDisplayName()
            com.zipow.videobox.confapp.ConfMgr r11 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmConfStatus r11 = r11.getConfStatusObj()
            if (r11 == 0) goto L_0x009a
            r17 = r3
            long r2 = r9.getReceiverID()
            boolean r2 = r11.isMyself(r2)
            if (r2 == 0) goto L_0x009c
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_webinar_txt_me
            java.lang.String r10 = r15.getString(r2)
            goto L_0x009c
        L_0x009a:
            r17 = r3
        L_0x009c:
            int r2 = r9.getMsgType()
            r3 = 2
            switch(r2) {
                case 0: goto L_0x00bf;
                case 1: goto L_0x00b8;
                case 2: goto L_0x00a5;
                default: goto L_0x00a4;
            }
        L_0x00a4:
            goto L_0x00c5
        L_0x00a5:
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_webinar_txt_label_ccPanelist
            java.lang.Object[] r11 = new java.lang.Object[r3]
            r11[r14] = r10
            int r10 = p021us.zoom.videomeetings.C4558R.string.zm_webinar_txt_all_panelists
            java.lang.String r10 = r15.getString(r10)
            r11[r13] = r10
            java.lang.String r10 = r15.getString(r2, r11)
            goto L_0x00c5
        L_0x00b8:
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_webinar_txt_all_panelists
            java.lang.String r10 = r15.getString(r2)
            goto L_0x00c5
        L_0x00bf:
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_mi_everyone_122046
            java.lang.String r10 = r15.getString(r2)
        L_0x00c5:
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_webinar_txt_label_from
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r9 = r9.getSenderDisplayName()
            r3[r14] = r9
            r3[r13] = r10
            java.lang.String r2 = r15.getString(r2, r3)
            boolean r3 = android.text.TextUtils.isEmpty(r8)
            if (r3 == 0) goto L_0x00fc
            r3 = 8
            r4.setVisibility(r3)
            goto L_0x00fc
        L_0x00e1:
            r17 = r3
            r2 = 0
            int r2 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1))
            if (r2 != 0) goto L_0x00ec
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_title_conf_chat_public
            goto L_0x00ee
        L_0x00ec:
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_title_conf_chat_private_from
        L_0x00ee:
            java.lang.Object[] r3 = new java.lang.Object[r13]
            java.lang.String r4 = "name"
            java.lang.String r4 = r7.getString(r4)
            r3[r14] = r4
            java.lang.String r2 = r15.getString(r2, r3)
        L_0x00fc:
            r5.setText(r2)
            java.lang.String r2 = "message"
            java.lang.String r2 = r7.getString(r2)
            r6.setText(r2)
            us.zoom.androidlib.widget.ZMTip r2 = new us.zoom.androidlib.widget.ZMTip
            r3 = r16
            r2.<init>(r3)
            r2.addView(r1)
            java.lang.String r1 = "anchorId"
            int r1 = r7.getInt(r1, r14)
            if (r1 <= 0) goto L_0x013a
            androidx.fragment.app.FragmentActivity r4 = r15.getActivity()
            if (r4 != 0) goto L_0x0122
            r5 = 0
            return r5
        L_0x0122:
            android.view.View r1 = r4.findViewById(r1)
            if (r1 == 0) goto L_0x0137
            androidx.fragment.app.FragmentActivity r4 = r15.getActivity()
            boolean r4 = com.zipow.videobox.util.UIMgr.isLargeMode(r4)
            if (r4 == 0) goto L_0x0133
            goto L_0x0134
        L_0x0133:
            r13 = 3
        L_0x0134:
            r2.setAnchor(r1, r13)
        L_0x0137:
            r1 = r17
            goto L_0x013f
        L_0x013a:
            r2.setOverlyingType(r13)
            r1 = r17
        L_0x013f:
            r1.setOnClickListener(r15)
            android.content.res.Resources r1 = r16.getResources()
            int r4 = p021us.zoom.videomeetings.C4558R.color.zm_message_tip_background
            int r1 = r1.getColor(r4)
            r2.setBackgroundColor(r1)
            android.content.res.Resources r1 = r16.getResources()
            int r4 = p021us.zoom.videomeetings.C4558R.color.zm_message_tip_border
            int r1 = r1.getColor(r4)
            r2.setBorderColor(r1)
            r1 = 1082130432(0x40800000, float:4.0)
            android.content.res.Resources r3 = r16.getResources()
            int r4 = p021us.zoom.videomeetings.C4558R.color.zm_message_tip_shadow
            int r3 = r3.getColor(r4)
            r2.setShadow(r1, r14, r14, r3)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.ChatTip.onCreateTip(android.content.Context, android.view.LayoutInflater, android.os.Bundle):us.zoom.androidlib.widget.ZMTip");
    }

    public void onClick(View view) {
        dismiss();
        showInChatUI();
    }

    public void setTransparent() {
        ZMTip tip = getTip();
        if (tip != null && getShowsTip()) {
            tip.setAlpha(0.5f);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0023 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0024  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showInChatUI() {
        /*
            r8 = this;
            com.zipow.videobox.confapp.ConfMgr r0 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            boolean r0 = r0.isConfConnected()
            if (r0 == 0) goto L_0x001c
            com.zipow.videobox.confapp.ConfMgr r0 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmConfContext r0 = r0.getConfContext()
            if (r0 == 0) goto L_0x001c
            boolean r0 = r0.isWebinar()
            if (r0 == 0) goto L_0x001c
            r0 = 1
            goto L_0x001d
        L_0x001c:
            r0 = 0
        L_0x001d:
            android.os.Bundle r1 = r8.getArguments()
            if (r1 != 0) goto L_0x0024
            return
        L_0x0024:
            java.lang.String r2 = "sender"
            long r2 = r1.getLong(r2)
            java.lang.String r4 = "receiver"
            long r4 = r1.getLong(r4)
            r6 = 0
            if (r0 != 0) goto L_0x003a
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 == 0) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            r2 = r6
        L_0x003a:
            androidx.fragment.app.FragmentActivity r0 = r8.getActivity()
            if (r0 != 0) goto L_0x0041
            return
        L_0x0041:
            boolean r1 = com.zipow.videobox.util.UIMgr.isLargeMode(r0)
            if (r1 == 0) goto L_0x004f
            androidx.fragment.app.FragmentManager r0 = r8.getFragmentManager()
            com.zipow.videobox.fragment.ConfChatFragment.showAsFragment(r0, r2)
            goto L_0x0056
        L_0x004f:
            us.zoom.androidlib.app.ZMActivity r0 = (p021us.zoom.androidlib.app.ZMActivity) r0
            r1 = 1002(0x3ea, float:1.404E-42)
            com.zipow.videobox.fragment.ConfChatFragment.showAsActivity(r0, r1, r2)
        L_0x0056:
            androidx.fragment.app.FragmentManager r0 = r8.getFragmentManager()
            dismiss(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.ChatTip.showInChatUI():void");
    }
}
