package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.IMProtos.AtInfoItem;
import com.zipow.videobox.ptapp.IMProtos.AtInfoItem.Builder;
import com.zipow.videobox.ptapp.IMProtos.AtInfoList;
import com.zipow.videobox.ptapp.IMProtos.StickerInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.TextCommandHelper;
import com.zipow.videobox.util.TextCommandHelper.SpanBean;
import com.zipow.videobox.view.CommandEditText;
import com.zipow.videobox.view.CommandEditText.OnCommandActionListener;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import com.zipow.videobox.view.p014mm.sticker.StickerEvent;
import com.zipow.videobox.view.p014mm.sticker.StickerInputView;
import com.zipow.videobox.view.p014mm.sticker.StickerInputView.OnPrivateStickerSelectListener;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.videomeetings.C4558R;

public class MMEditMessageFragment extends ZMDialogFragment implements OnClickListener, ExtListener, OnPrivateStickerSelectListener, OnCommandActionListener {
    public static final String ARGS_GUID = "guid";
    public static final String ARGS_SERVER_TIME = "server_time";
    private static final String ARGS_SESSION_ID = "session_id";
    private static final int MODE_EMOJI = 1;
    private static final int MODE_KEYBOARD = 0;
    private static final int REQUEST_SELECT_CHANNEL = 106;
    private static final int REQUEST_SELECT_CONTACT = 105;
    /* access modifiers changed from: private */
    public TextView mBtnCancel;
    /* access modifiers changed from: private */
    public TextView mBtnDone;
    /* access modifiers changed from: private */
    public ImageButton mBtnEmojis;
    private ImageButton mBtnKeyBoard;
    private CommandEditText mContent;
    /* access modifiers changed from: private */
    @Nullable
    public String mGuid;
    private int mMode = 0;
    /* access modifiers changed from: private */
    public StickerInputView mPanelEmojis;
    /* access modifiers changed from: private */
    @Nullable
    public String mSessionId;
    /* access modifiers changed from: private */
    public TextView mTitle;
    @Nullable
    private ZoomMessage mZoomMessage;
    @Nullable
    private SimpleZoomMessengerUIListener mZoomMessengerUIListener;

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsFragment(Fragment fragment, String str, String str2) {
        showAsFragment(fragment, str, str2, -1);
    }

    public static void showAsFragment(Fragment fragment, @Nullable String str, @Nullable String str2, int i) {
        Bundle bundle = new Bundle();
        String str3 = "session_id";
        if (str == null) {
            str = "";
        }
        bundle.putString(str3, str);
        String str4 = "guid";
        if (str2 == null) {
            str2 = "";
        }
        bundle.putString(str4, str2);
        SimpleActivity.show(fragment, MMEditMessageFragment.class.getName(), bundle, i, 2);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("mMode", this.mMode);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C4558R.layout.zm_mm_edit_message, viewGroup, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mBtnCancel = (TextView) view.findViewById(C4558R.C4560id.btn_cancel);
        this.mBtnDone = (TextView) view.findViewById(C4558R.C4560id.btn_done);
        this.mTitle = (TextView) view.findViewById(C4558R.C4560id.title);
        this.mContent = (CommandEditText) view.findViewById(C4558R.C4560id.ext_content);
        this.mContent.setEnableLine(false);
        this.mBtnKeyBoard = (ImageButton) view.findViewById(C4558R.C4560id.btnSetModeKeyboard);
        this.mPanelEmojis = (StickerInputView) view.findViewById(C4558R.C4560id.panelEmojis);
        this.mPanelEmojis.enableCustomSticker(false);
        this.mPanelEmojis.disableGiphy();
        this.mBtnEmojis = (ImageButton) view.findViewById(C4558R.C4560id.btnEmoji);
        if (bundle != null) {
            this.mMode = bundle.getInt("mMode", 0);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnDone.setOnClickListener(this);
        this.mBtnEmojis.setOnClickListener(this);
        this.mBtnKeyBoard.setOnClickListener(this);
        this.mPanelEmojis.setEmojiInputEditText(this.mContent);
        this.mPanelEmojis.setOnPrivateStickerSelectListener(this);
        this.mContent.setOnClickListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSessionId = arguments.getString("session_id");
            this.mGuid = arguments.getString("guid");
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                this.mZoomMessage = sessionById.getMessageByXMPPGuid(this.mGuid);
                ZoomMessage zoomMessage = this.mZoomMessage;
                if (zoomMessage != null) {
                    this.mContent.setText(FontStyleHelper.getCharSequenceFromMMMessageItem(zoomMessage.getBody(), this.mZoomMessage.getFontStyte()));
                    AtInfoList msgAtInfoList = this.mZoomMessage.getMsgAtInfoList();
                    if (msgAtInfoList != null && msgAtInfoList.getAtInfoItemCount() > 0) {
                        for (int i = 0; i < msgAtInfoList.getAtInfoItemCount(); i++) {
                            AtInfoItem atInfoItem = msgAtInfoList.getAtInfoItem(i);
                            if (atInfoItem != null) {
                                if (atInfoItem.getType() != 0 && UIUtil.isLegalSpanIndex(this.mContent.getEditableText(), atInfoItem.getPositionStart(), atInfoItem.getPositionEnd())) {
                                    this.mContent.addCommandByIndex(atInfoItem);
                                } else if (this.mZoomMessage.isMessageAtEveryone()) {
                                    this.mContent.addAtCommandByJid(MMSelectContactsFragment.JID_SELECTED_EVERYONE);
                                } else {
                                    this.mContent.addAtCommandByJid(atInfoItem.getJid());
                                }
                            }
                        }
                    }
                    CommandEditText commandEditText = this.mContent;
                    commandEditText.setSelection(commandEditText.getText().length());
                    this.mContent.setOnCommandActionListener(this);
                    this.mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
                        public void Indicate_EditMessageResultIml(String str, String str2, String str3, long j, long j2, boolean z) {
                            if (!TextUtils.isEmpty(str3) && TextUtils.equals(str3, MMEditMessageFragment.this.mGuid)) {
                                if (!z) {
                                    MMEditMessageFragment.this.mBtnCancel.setEnabled(true);
                                    MMEditMessageFragment.this.mBtnDone.setEnabled(true);
                                    MMEditMessageFragment.this.mTitle.setText(MMEditMessageFragment.this.getResources().getString(C4558R.string.zm_mm_edit_message_19884));
                                } else if (MMEditMessageFragment.this.getActivity() != null) {
                                    Intent intent = new Intent();
                                    intent.putExtra("guid", str3);
                                    intent.putExtra(MMEditMessageFragment.ARGS_SERVER_TIME, j2);
                                    MMEditMessageFragment.this.getActivity().setResult(-1, intent);
                                    MMEditMessageFragment.this.getActivity().finish();
                                }
                            }
                        }

                        public void NotifyEditMsgFailed(String str, String str2) {
                            if (StringUtil.isSameString(MMEditMessageFragment.this.mSessionId, str)) {
                                Toast.makeText(MMEditMessageFragment.this.getActivity(), str2, 1).show();
                                MMEditMessageFragment.this.mBtnCancel.setEnabled(true);
                                MMEditMessageFragment.this.mBtnDone.setEnabled(true);
                                MMEditMessageFragment.this.mTitle.setText(MMEditMessageFragment.this.getResources().getString(C4558R.string.zm_mm_edit_message_19884));
                            }
                        }
                    };
                    ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
                }
            }
        }
    }

    public void onDestroy() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        super.onDestroy();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btn_cancel) {
            dismiss();
        } else if (id == C4558R.C4560id.btn_done) {
            checkNoity2ClickDone();
        } else if (id == C4558R.C4560id.btnSetModeKeyboard) {
            onClickSetModeKeyboard();
        } else if (id == C4558R.C4560id.btnEmoji) {
            onClickBtnEmoji();
        } else if (id == C4558R.C4560id.ext_content) {
            this.mMode = 0;
            updateUIMode(this.mMode);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x009b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkNoity2ClickDone() {
        /*
            r10 = this;
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r0 = r0.getZoomMessenger()
            if (r0 != 0) goto L_0x000b
            return
        L_0x000b:
            java.lang.String r1 = r10.mSessionId
            com.zipow.videobox.ptapp.mm.ZoomChatSession r0 = r0.getSessionById(r1)
            if (r0 != 0) goto L_0x0014
            return
        L_0x0014:
            com.zipow.videobox.ptapp.mm.ZoomMessage r1 = r10.mZoomMessage
            if (r1 != 0) goto L_0x0019
            return
        L_0x0019:
            boolean r1 = r0.isGroup()
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x006f
            com.zipow.videobox.ptapp.mm.ZoomGroup r0 = r0.getSessionGroup()
            if (r0 == 0) goto L_0x006f
            com.zipow.videobox.view.CommandEditText r1 = r10.mContent
            r4 = 2
            java.util.List r1 = r1.getTextCommand(r4)
            boolean r4 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r1)
            if (r4 != 0) goto L_0x006f
            java.util.Iterator r1 = r1.iterator()
        L_0x0038:
            boolean r4 = r1.hasNext()
            if (r4 == 0) goto L_0x006f
            java.lang.Object r4 = r1.next()
            com.zipow.videobox.util.TextCommandHelper$SpanBean r4 = (com.zipow.videobox.util.TextCommandHelper.SpanBean) r4
            java.lang.String r5 = r4.getJid()
            java.lang.String r6 = "jid_select_everyone"
            boolean r5 = p021us.zoom.androidlib.util.StringUtil.isSameString(r5, r6)
            if (r5 != 0) goto L_0x0060
            java.lang.String r4 = r4.getJid()
            java.lang.String r5 = r10.mSessionId
            java.lang.String r5 = p021us.zoom.androidlib.util.UIUtil.generateAtallSessionId(r5)
            boolean r4 = android.text.TextUtils.equals(r4, r5)
            if (r4 == 0) goto L_0x0038
        L_0x0060:
            int r1 = r0.getBuddyCount()
            r4 = 1000(0x3e8, float:1.401E-42)
            if (r1 <= r4) goto L_0x006f
            int r0 = r0.getBuddyCount()
            r1 = r0
            r0 = 1
            goto L_0x0071
        L_0x006f:
            r0 = 0
            r1 = 0
        L_0x0071:
            if (r0 == 0) goto L_0x009b
            androidx.fragment.app.FragmentActivity r0 = r10.getActivity()
            r4 = r0
            us.zoom.androidlib.app.ZMActivity r4 = (p021us.zoom.androidlib.app.ZMActivity) r4
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_mm_atall_notify_title_113595
            java.lang.String r5 = r10.getString(r0)
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_mm_atall_notify_message_113595
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r2[r3] = r1
            java.lang.String r6 = r10.getString(r0, r2)
            int r7 = p021us.zoom.videomeetings.C4558R.string.zm_mm_atall_notify_button_113595
            int r8 = p021us.zoom.videomeetings.C4558R.string.zm_btn_cancel
            com.zipow.videobox.fragment.MMEditMessageFragment$2 r9 = new com.zipow.videobox.fragment.MMEditMessageFragment$2
            r9.<init>()
            com.zipow.videobox.util.DialogUtils.showAlertDialog(r4, r5, r6, r7, r8, r9)
            goto L_0x009e
        L_0x009b:
            r10.onClickDone()
        L_0x009e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMEditMessageFragment.checkNoity2ClickDone():void");
    }

    /* access modifiers changed from: private */
    public void onClickDone() {
        boolean z;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null && this.mZoomMessage != null) {
                ArrayList arrayList = new ArrayList();
                ArrayList<SpanBean> arrayList2 = new ArrayList<>();
                arrayList2.addAll(this.mContent.getTextCommand(2));
                arrayList2.addAll(this.mContent.getTextCommand(3));
                if (!arrayList2.isEmpty()) {
                    boolean z2 = false;
                    for (SpanBean spanBean : arrayList2) {
                        Builder newBuilder = AtInfoItem.newBuilder();
                        String charSequence = this.mContent.getText().subSequence(spanBean.getStart(), spanBean.getEnd()).toString();
                        newBuilder.setJid(spanBean.getJid());
                        newBuilder.setPositionStart(spanBean.getStart());
                        newBuilder.setPositionEnd(spanBean.getEnd() - 2);
                        if (spanBean.getType() == 2) {
                            newBuilder.setType(1);
                        } else if (spanBean.getType() == 3) {
                            newBuilder.setType(3);
                        } else {
                            newBuilder.setType(0);
                        }
                        if (StringUtil.isSameString(charSequence, spanBean.getLabel()) && spanBean.getEnd() < 4096) {
                            if (StringUtil.isSameString(spanBean.getJid(), MMSelectContactsFragment.JID_SELECTED_EVERYONE) || TextUtils.equals(spanBean.getJid(), UIUtil.generateAtallSessionId(this.mSessionId))) {
                                newBuilder.setType(2);
                                newBuilder.setJid(UIUtil.generateAtallSessionId(this.mSessionId));
                                z2 = true;
                            }
                            arrayList.add(newBuilder.build());
                        }
                    }
                    z = z2;
                } else {
                    z = false;
                }
                if (sessionById.editMessageByXMPPGuid(this.mContent.getText(), this.mGuid, this.mSessionId, arrayList, z)) {
                    this.mBtnDone.setEnabled(false);
                    this.mBtnCancel.setEnabled(false);
                    this.mTitle.setText(getResources().getString(C4558R.string.zm_mm_edit_message_saving_19884));
                }
                if (getActivity() != null) {
                    UIUtil.closeSoftKeyboard(getActivity(), this.mContent);
                }
            }
        }
    }

    private void onClickSetModeKeyboard() {
        this.mMode = 0;
        updateUIMode(this.mMode);
        UIUtil.openSoftKeyboard(getActivity(), this.mContent);
    }

    private void onClickBtnEmoji() {
        this.mMode = 1;
        updateUIMode(this.mMode);
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(0);
    }

    public void onKeyboardOpen() {
        if (!this.mContent.hasFocus()) {
            this.mContent.requestFocus();
        }
        this.mMode = 0;
        updateUIMode(this.mMode);
        if (getActivity() instanceof SimpleActivity) {
            ZMKeyboardDetector keyboardDetector = ((SimpleActivity) getActivity()).getKeyboardDetector();
            if (keyboardDetector != null) {
                this.mPanelEmojis.setKeyboardHeight(keyboardDetector.getKeyboardHeight());
            }
        }
    }

    private void updateUIMode(int i) {
        if (i != 1) {
            this.mPanelEmojis.setVisibility(8);
            this.mBtnEmojis.setVisibility(0);
            this.mBtnKeyBoard.setVisibility(8);
            return;
        }
        this.mPanelEmojis.startAnimation(AnimationUtils.loadAnimation(getActivity(), C4558R.anim.zm_slide_in_bottom));
        this.mPanelEmojis.setVisibility(0);
        this.mBtnEmojis.setVisibility(8);
        this.mBtnKeyBoard.startAnimation(AnimationUtils.loadAnimation(getActivity(), C4558R.anim.zm_fade_in));
        this.mBtnKeyBoard.setVisibility(0);
        UIUtil.closeSoftKeyboard(getActivity(), this.mContent);
    }

    private void closePanelWithAnimation() {
        this.mBtnKeyBoard.setVisibility(8);
        Animation loadAnimation = AnimationUtils.loadAnimation(getActivity(), C4558R.anim.zm_slide_out_bottom);
        loadAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                MMEditMessageFragment.this.mPanelEmojis.setVisibility(8);
                MMEditMessageFragment.this.mBtnEmojis.setVisibility(0);
            }
        });
        this.mPanelEmojis.startAnimation(loadAnimation);
    }

    public void onKeyboardClosed() {
        if (this.mMode == 1) {
            this.mPanelEmojis.setVisibility(0);
        }
    }

    public boolean onBackPressed() {
        if (this.mMode != 1) {
            return false;
        }
        this.mMode = 0;
        closePanelWithAnimation();
        return true;
    }

    public void onPrivateStickerSelect(@Nullable StickerEvent stickerEvent) {
        if (stickerEvent != null && !StringUtil.isEmptyOrNull(stickerEvent.getStickerId())) {
            MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
            if (zoomPrivateStickerMgr != null) {
                StickerInfo.Builder newBuilder = StickerInfo.newBuilder();
                newBuilder.setFileId(stickerEvent.getStickerId());
                newBuilder.setStatus(stickerEvent.getStatus());
                if (stickerEvent.getStickerPath() != null) {
                    newBuilder.setUploadingPath(stickerEvent.getStickerPath());
                }
                if (zoomPrivateStickerMgr.sendSticker(newBuilder.build(), this.mSessionId) != 1) {
                    Toast.makeText(getActivity(), C4558R.string.zm_hint_sticker_send_failed, 1).show();
                }
            }
        }
    }

    public void onCommandAction(int i) {
        if (i == 2) {
            selectATBuddy();
        } else if (i == 3) {
            selectChannel();
        }
    }

    private void selectChannel() {
        if (((ZMActivity) getActivity()) != null) {
            MMSelectGroupFragment.showAsActivity(this, false, false, null, getString(C4558R.string.zm_mm_title_select_channel_113595), 106, null);
        }
    }

    private void onATBuddySelect(@Nullable IMAddrBookItem iMAddrBookItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null && iMAddrBookItem != null && !TextUtils.isEmpty(myself.getJid()) && !TextUtils.isEmpty(iMAddrBookItem.getJid()) && !myself.getJid().equals(iMAddrBookItem.getJid())) {
                StringBuilder sb = new StringBuilder();
                sb.append(TextCommandHelper.REPLY_AT_CHAR);
                sb.append(iMAddrBookItem.getScreenName());
                sb.append(OAuth.SCOPE_DELIMITER);
                String sb2 = sb.toString();
                int selectionStart = this.mContent.getSelectionStart();
                if (selectionStart > 0) {
                    int i = selectionStart - 1;
                    if (this.mContent.getEditableText().charAt(i) == '@') {
                        this.mContent.getEditableText().delete(i, selectionStart);
                        selectionStart = i;
                    }
                }
                this.mContent.addTextCommand(2, sb2, iMAddrBookItem.getJid(), selectionStart);
                if (this.mMode != 0) {
                    this.mMode = 0;
                    updateUIMode(this.mMode);
                    this.mContent.requestFocus();
                    UIUtil.openSoftKeyboard(getActivity(), this.mContent);
                }
            }
        }
    }

    private void selectATBuddy() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null && sessionById.isGroup()) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null) {
                    MMSelectContactsActivity.show(this, zMActivity.getString(C4558R.string.zm_mm_title_select_a_contact), null, zMActivity.getString(C4558R.string.zm_btn_ok), null, false, null, true, 105, true, sessionById.getSessionId());
                }
            }
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 105 && i2 == -1 && intent != null) {
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectedItems");
            if (arrayList != null && arrayList.size() == 1) {
                onATBuddySelect((IMAddrBookItem) arrayList.get(0));
            }
        } else if (i == 106 && i2 == -1 && intent != null) {
            ArrayList stringArrayListExtra = intent.getStringArrayListExtra(MMSelectGroupFragment.RESULT_SELECT_GROUPS);
            if (!CollectionsUtil.isListEmpty(stringArrayListExtra)) {
                onChannelSelect((String) stringArrayListExtra.get(0));
            }
        }
    }

    public void onChannelSelect(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(str);
                if (groupById != null) {
                    String groupDisplayName = groupById.getGroupDisplayName(getContext());
                    StringBuilder sb = new StringBuilder();
                    sb.append(TextCommandHelper.CHANNEL_CMD_CHAR);
                    sb.append(groupDisplayName);
                    sb.append(OAuth.SCOPE_DELIMITER);
                    String sb2 = sb.toString();
                    int selectionStart = this.mContent.getSelectionStart();
                    if (selectionStart > 0) {
                        int i = selectionStart - 1;
                        if (this.mContent.getEditableText().charAt(i) == '#') {
                            this.mContent.getEditableText().delete(i, selectionStart);
                            selectionStart = i;
                        }
                    }
                    this.mContent.addTextCommand(3, sb2, str, selectionStart);
                    if (this.mMode != 0) {
                        this.mMode = 0;
                        updateUIMode(this.mMode);
                        this.mContent.requestFocus();
                        UIUtil.openSoftKeyboard(getActivity(), this.mContent);
                    }
                }
            }
        }
    }
}
