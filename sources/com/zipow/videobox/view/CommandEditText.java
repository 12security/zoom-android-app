package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View.OnFocusChangeListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.zipow.videobox.fragment.MMSelectContactsFragment;
import com.zipow.videobox.ptapp.IMProtos.AtInfoItem;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.TextCommandHelper;
import com.zipow.videobox.util.TextCommandHelper.AtSpan;
import com.zipow.videobox.util.TextCommandHelper.ChannelSpan;
import com.zipow.videobox.util.TextCommandHelper.DraftBean;
import com.zipow.videobox.util.TextCommandHelper.ParamSpan;
import com.zipow.videobox.util.TextCommandHelper.SlashSpan;
import com.zipow.videobox.util.TextCommandHelper.SpanBean;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class CommandEditText extends EmojiEditText {
    public static final String TAG = "CommandEditText";
    @Nullable
    private TextWatcher mEditMsgWatcher = new TextWatcher() {
        private int originLength;

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            this.originLength = charSequence.length();
        }

        public void onTextChanged(@NonNull CharSequence charSequence, int i, int i2, int i3) {
            if (TextUtils.isEmpty(CommandEditText.this.mThreadId)) {
                TextCommandHelper.getInstance().clearCommandParam(charSequence, i, i2, i3, CommandEditText.this.getEditableText());
                if (!TextCommandHelper.getInstance().slashCommandAction(charSequence, i, i2, i3, CommandEditText.this.getText())) {
                    if (!TextCommandHelper.getInstance().atCommandAction(charSequence, i, i2, i3, CommandEditText.this.getText(), this.originLength)) {
                        if (TextCommandHelper.getInstance().channelCommandAction(charSequence, i, i2, i3, CommandEditText.this.getText(), this.originLength) && CommandEditText.this.onCommandActionListener != null) {
                            CommandEditText.this.onCommandActionListener.onCommandAction(3);
                        }
                    } else if (CommandEditText.this.onCommandActionListener != null) {
                        CommandEditText.this.onCommandActionListener.onCommandAction(2);
                    }
                } else if (CommandEditText.this.onCommandActionListener != null) {
                    CommandEditText.this.onCommandActionListener.onCommandAction(1);
                }
            } else {
                if (!TextCommandHelper.getInstance().atCommandAction(charSequence, i, i2, i3, CommandEditText.this.getText(), this.originLength)) {
                    if (TextCommandHelper.getInstance().channelCommandAction(charSequence, i, i2, i3, CommandEditText.this.getText(), this.originLength) && CommandEditText.this.onCommandActionListener != null) {
                        CommandEditText.this.onCommandActionListener.onCommandAction(3);
                    }
                } else if (CommandEditText.this.onCommandActionListener != null) {
                    CommandEditText.this.onCommandActionListener.onCommandAction(2);
                }
            }
            CommandEditText.this.deleteAtSpanAsAWhole(charSequence, i, i2, i3);
        }
    };
    private boolean mIsPbx = false;
    private String mSessionId;
    /* access modifiers changed from: private */
    public String mThreadId;
    /* access modifiers changed from: private */
    public OnCommandActionListener onCommandActionListener;

    public interface OnCommandActionListener {
        void onCommandAction(int i);
    }

    public enum SendMsgType {
        MESSAGE,
        SLASH_COMMAND,
        GIPHY
    }

    public CommandEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CommandEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CommandEditText(Context context) {
        super(context);
    }

    public boolean requestFocus(int i, Rect rect) {
        boolean requestFocus = super.requestFocus(i, rect);
        if (requestFocus) {
            OnFocusChangeListener onFocusChangeListener = getOnFocusChangeListener();
            if (onFocusChangeListener != null) {
                onFocusChangeListener.onFocusChange(this, isFocused());
            }
        }
        return requestFocus;
    }

    private void restoreCommandText() {
        DraftBean restoreTextCommand = TextCommandHelper.getInstance().restoreTextCommand(this.mIsPbx, this.mSessionId, this.mThreadId);
        if (restoreTextCommand != null) {
            SpannableString spannableString = new SpannableString(restoreTextCommand.getLabel() == null ? "" : restoreTextCommand.getLabel());
            if (restoreTextCommand.getSpans() != null && !TextUtils.isEmpty(spannableString)) {
                for (SpanBean spanBean : restoreTextCommand.getSpans()) {
                    int type = spanBean.getType();
                    if (spanBean.getStart() >= 0 && spanBean.getEnd() <= spannableString.length()) {
                        if (type == 1) {
                            spannableString.setSpan(new SlashSpan(spanBean), spanBean.getStart(), spanBean.getEnd(), 33);
                        } else if (type == 2) {
                            spannableString.setSpan(new AtSpan(spanBean), spanBean.getStart(), spanBean.getEnd(), 33);
                            spannableString.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB)), spanBean.getStart(), spanBean.getEnd(), 33);
                        } else if (type == 3) {
                            spannableString.setSpan(new ChannelSpan(spanBean.getJid(), spanBean.getLabel()), spanBean.getStart(), spanBean.getEnd(), 33);
                        }
                    }
                }
            }
            setText(CommonEmojiHelper.getInstance().formatImgEmojiSize(getTextSize(), spannableString, true));
            setSelection(getText().length());
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        addTextChangedListener(this.mEditMsgWatcher);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeTextChangedListener(this.mEditMsgWatcher);
    }

    @NonNull
    public List<SpanBean> getTextCommand(int i) {
        ArrayList arrayList = new ArrayList();
        Editable editableText = getEditableText();
        int i2 = 0;
        if (i == 1) {
            SlashSpan[] slashSpanArr = (SlashSpan[]) editableText.getSpans(0, editableText.length(), SlashSpan.class);
            if (slashSpanArr != null && slashSpanArr.length > 0) {
                int length = slashSpanArr.length;
                while (i2 < length) {
                    SlashSpan slashSpan = slashSpanArr[i2];
                    arrayList.add(new SpanBean(editableText.getSpanStart(slashSpan), editableText.getSpanEnd(slashSpan), slashSpan));
                    i2++;
                }
            }
        } else if (i == 2) {
            AtSpan[] atSpanArr = (AtSpan[]) editableText.getSpans(0, editableText.length(), AtSpan.class);
            if (atSpanArr != null && atSpanArr.length > 0) {
                int length2 = atSpanArr.length;
                while (i2 < length2) {
                    AtSpan atSpan = atSpanArr[i2];
                    arrayList.add(new SpanBean(editableText.getSpanStart(atSpan), editableText.getSpanEnd(atSpan), atSpan));
                    i2++;
                }
            }
        } else if (i == 3) {
            ChannelSpan[] channelSpanArr = (ChannelSpan[]) editableText.getSpans(0, editableText.length(), ChannelSpan.class);
            if (channelSpanArr != null && channelSpanArr.length > 0) {
                int length3 = channelSpanArr.length;
                while (i2 < length3) {
                    ChannelSpan channelSpan = channelSpanArr[i2];
                    arrayList.add(new SpanBean(editableText.getSpanStart(channelSpan), editableText.getSpanEnd(channelSpan), channelSpan));
                    i2++;
                }
            }
        }
        return arrayList;
    }

    public void addCommandByIndex(@Nullable AtInfoItem atInfoItem) {
        if (atInfoItem != null) {
            Editable editableText = getEditableText();
            if (atInfoItem.getType() == 3) {
                ChannelSpan channelSpan = new ChannelSpan();
                channelSpan.groupId = atInfoItem.getJid();
                channelSpan.label = String.valueOf(editableText.subSequence(atInfoItem.getPositionStart(), editableText.length() < atInfoItem.getPositionEnd() + 2 ? editableText.length() : atInfoItem.getPositionEnd() + 2));
                editableText.setSpan(channelSpan, atInfoItem.getPositionStart(), editableText.length() < atInfoItem.getPositionEnd() + 2 ? editableText.length() : atInfoItem.getPositionEnd() + 2, 33);
                editableText.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB)), atInfoItem.getPositionStart(), editableText.length() < atInfoItem.getPositionEnd() + 2 ? editableText.length() : atInfoItem.getPositionEnd() + 2, 33);
            } else {
                AtSpan atSpan = new AtSpan();
                atSpan.jId = atInfoItem.getJid();
                atSpan.label = String.valueOf(editableText.subSequence(atInfoItem.getPositionStart(), editableText.length() < atInfoItem.getPositionEnd() + 2 ? editableText.length() : atInfoItem.getPositionEnd() + 2));
                editableText.setSpan(atSpan, atInfoItem.getPositionStart(), editableText.length() < atInfoItem.getPositionEnd() + 2 ? editableText.length() : atInfoItem.getPositionEnd() + 2, 33);
                editableText.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB)), atInfoItem.getPositionStart(), editableText.length() < atInfoItem.getPositionEnd() + 2 ? editableText.length() : atInfoItem.getPositionEnd() + 2, 33);
            }
        }
    }

    public void addAtCommandByJid(@Nullable String str) {
        int i;
        String str2;
        if (!TextUtils.isEmpty(str)) {
            if (str.equalsIgnoreCase(MMSelectContactsFragment.JID_SELECTED_EVERYONE)) {
                StringBuilder sb = new StringBuilder();
                sb.append(TextCommandHelper.REPLY_AT_CHAR);
                sb.append(getResources().getString(C4558R.string.zm_lbl_select_everyone));
                sb.append(OAuth.SCOPE_DELIMITER);
                String sb2 = sb.toString();
                String obj = getText().toString();
                i = obj.indexOf(sb2);
                if (i < 0) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(TextCommandHelper.REPLY_AT_CHAR);
                    sb3.append(getResources().getString(C4558R.string.zm_lbl_select_everyone));
                    String sb4 = sb3.toString();
                    i = obj.indexOf(sb4);
                    if (i >= 0) {
                        getEditableText().insert(sb4.length() + i, OAuth.SCOPE_DELIMITER);
                    }
                }
                str2 = sb2;
            } else {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                    String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, IMAddrBookItem.fromZoomBuddy(buddyWithJID));
                    if (!TextUtils.isEmpty(buddyDisplayName)) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(TextCommandHelper.REPLY_AT_CHAR);
                        sb5.append(buddyDisplayName);
                        sb5.append(OAuth.SCOPE_DELIMITER);
                        str2 = sb5.toString();
                        String obj2 = getText().toString();
                        int indexOf = obj2.indexOf(str2);
                        if (indexOf < 0) {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(TextCommandHelper.REPLY_AT_CHAR);
                            sb6.append(buddyDisplayName);
                            String sb7 = sb6.toString();
                            i = obj2.indexOf(sb7);
                            if (i >= 0) {
                                getEditableText().insert(sb7.length() + i, OAuth.SCOPE_DELIMITER);
                            }
                        } else {
                            i = indexOf;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (i >= 0) {
                Editable editableText = getEditableText();
                AtSpan atSpan = new AtSpan();
                atSpan.jId = str;
                atSpan.label = str2;
                editableText.setSpan(atSpan, i, str2.length() + i, 33);
                editableText.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB)), i, str2.length() + i, 33);
            }
        }
    }

    public void addTextCommand(int i, @Nullable String str, String str2, int i2) {
        addTextCommand(i, str, "", str2, i2);
    }

    public void addTextCommand(int i, @Nullable String str, String str2, String str3, int i2) {
        if (i2 >= 0 && !TextUtils.isEmpty(str)) {
            if (i == 1) {
                SlashSpan slashSpan = new SlashSpan();
                slashSpan.jId = str3;
                slashSpan.label = str;
                Editable editableText = getEditableText();
                editableText.clear();
                editableText.append(slashSpan.label).append(OAuth.SCOPE_DELIMITER).append(str2);
                editableText.setSpan(slashSpan, i2, str.length() + i2 + 1, 33);
                int indexOf = editableText.toString().indexOf(91);
                if (indexOf > str.length()) {
                    editableText.setSpan(new ParamSpan(ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_blue_0E71EB)), indexOf, editableText.length(), 33);
                }
                setText(editableText);
                if (indexOf > str.length()) {
                    setSelection(indexOf);
                } else {
                    setSelection(editableText.length());
                }
            } else if (i == 2) {
                Editable editableText2 = getEditableText();
                AtSpan atSpan = new AtSpan();
                atSpan.jId = str3;
                atSpan.label = str;
                editableText2.insert(i2, new SpannableString(str));
                editableText2.setSpan(atSpan, i2, str.length() + i2, 33);
                editableText2.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB)), i2, str.length() + i2, 33);
            } else if (i == 3) {
                Editable editableText3 = getEditableText();
                ChannelSpan channelSpan = new ChannelSpan(str3, str);
                editableText3.insert(i2, new SpannableString(str));
                editableText3.setSpan(channelSpan, i2, str.length() + i2, 33);
                editableText3.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB)), i2, str.length() + i2, 33);
            }
        }
    }

    public void enableStoreCommand(boolean z, String str, @Nullable String str2) {
        this.mSessionId = str;
        this.mThreadId = str2;
        this.mIsPbx = z;
        restoreCommandText();
    }

    public void setOnCommandActionListener(OnCommandActionListener onCommandActionListener2) {
        this.onCommandActionListener = onCommandActionListener2;
    }

    @NonNull
    public SendMsgType judgeSlashCommand(String str, boolean z) {
        if (TextUtils.isEmpty(getText())) {
            return SendMsgType.MESSAGE;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return SendMsgType.MESSAGE;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
        if (sessionById == null) {
            return SendMsgType.MESSAGE;
        }
        if (!sessionById.isGroup()) {
            ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
            if (sessionBuddy != null && sessionBuddy.isRobot()) {
                addTextCommand(1, sessionBuddy.getRobotCmdPrefix(), getText().toString(), sessionBuddy.getJid(), 0);
                return SendMsgType.SLASH_COMMAND;
            }
        }
        List textCommand = getTextCommand(1);
        if (!textCommand.isEmpty()) {
            SpanBean spanBean = (SpanBean) textCommand.get(0);
            String charSequence = getText().subSequence(spanBean.getStart(), spanBean.getEnd()).toString();
            if (!TextUtils.isEmpty(spanBean.getJid()) && StringUtil.isSameString(charSequence.trim(), spanBean.getLabel().trim())) {
                return SendMsgType.SLASH_COMMAND;
            }
            if (StringUtil.isSameString("/giphy", spanBean.getLabel().trim())) {
                return SendMsgType.GIPHY;
            }
            return SendMsgType.MESSAGE;
        } else if (!z) {
            return SendMsgType.MESSAGE;
        } else {
            if (hasSlashCommand(str)) {
                return SendMsgType.SLASH_COMMAND;
            }
            return SendMsgType.MESSAGE;
        }
    }

    private boolean hasSlashCommand(String str) {
        String trim = getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
        if (sessionById == null) {
            return false;
        }
        if (sessionById.isGroup()) {
            ZoomGroup sessionGroup = sessionById.getSessionGroup();
            if (sessionGroup == null) {
                return false;
            }
            List allRobotBuddies = zoomMessenger.getAllRobotBuddies(sessionGroup.getGroupID());
            if (allRobotBuddies == null || allRobotBuddies.isEmpty()) {
                return false;
            }
            for (int i = 0; i < allRobotBuddies.size(); i++) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID((String) allRobotBuddies.get(i));
                if (buddyWithJID != null && buddyWithJID.isRobot() && !TextUtils.isEmpty(buddyWithJID.getRobotCmdPrefix()) && trim.startsWith(buddyWithJID.getRobotCmdPrefix().trim())) {
                    String[] split = trim.split(OAuth.SCOPE_DELIMITER);
                    addTextCommand(1, buddyWithJID.getRobotCmdPrefix(), split.length > 1 ? split[1] : "", buddyWithJID.getJid(), 0);
                    return true;
                }
            }
        } else {
            ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
            if (sessionBuddy != null && sessionBuddy.isRobot() && !TextUtils.isEmpty(sessionBuddy.getRobotCmdPrefix()) && trim.startsWith(sessionBuddy.getRobotCmdPrefix())) {
                String[] split2 = trim.split(OAuth.SCOPE_DELIMITER);
                addTextCommand(1, sessionBuddy.getRobotCmdPrefix(), split2.length > 1 ? split2[1] : "", sessionBuddy.getJid(), 0);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void deleteAtSpanAsAWhole(@NonNull CharSequence charSequence, int i, int i2, int i3) {
        boolean z = i2 == 1 && i3 == 0;
        boolean z2 = i2 == 0 && i3 > 0;
        if (z || z2) {
            try {
                AtSpan[] atSpanArr = (AtSpan[]) getText().getSpans(0, getText().length(), AtSpan.class);
                int length = atSpanArr.length - 1;
                while (true) {
                    if (length < 0) {
                        break;
                    }
                    AtSpan atSpan = atSpanArr[length];
                    int spanStart = getText().getSpanStart(atSpan);
                    int spanEnd = getText().getSpanEnd(atSpan);
                    if (spanStart >= 0 && spanEnd >= 0 && spanStart <= charSequence.length()) {
                        if (spanEnd <= charSequence.length()) {
                            CharSequence subSequence = charSequence.subSequence(spanStart, spanEnd);
                            if (spanEnd >= i && spanStart <= i && !StringUtil.isEmptyOrNull(atSpan.label) && !subSequence.toString().contains(atSpan.label)) {
                                if (z) {
                                    getText().delete(getText().getSpanStart(atSpan), getText().getSpanEnd(atSpan));
                                } else if (z2) {
                                    getText().removeSpan(atSpan);
                                    ForegroundColorSpan[] foregroundColorSpanArr = (ForegroundColorSpan[]) getText().getSpans(0, getText().length(), ForegroundColorSpan.class);
                                    if (foregroundColorSpanArr != null && foregroundColorSpanArr.length > 0) {
                                        int length2 = foregroundColorSpanArr.length - 1;
                                        while (true) {
                                            if (length2 < 0) {
                                                break;
                                            }
                                            ForegroundColorSpan foregroundColorSpan = foregroundColorSpanArr[length2];
                                            int spanStart2 = getText().getSpanStart(foregroundColorSpan);
                                            int spanEnd2 = getText().getSpanEnd(foregroundColorSpan);
                                            if (spanStart == spanStart2 && spanEnd == spanEnd2) {
                                                getText().removeSpan(foregroundColorSpan);
                                                invalidate();
                                                break;
                                            }
                                            length2--;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    length--;
                }
                ChannelSpan[] channelSpanArr = (ChannelSpan[]) getText().getSpans(0, getText().length(), ChannelSpan.class);
                for (int length3 = channelSpanArr.length - 1; length3 >= 0; length3--) {
                    ChannelSpan channelSpan = channelSpanArr[length3];
                    int spanStart3 = getText().getSpanStart(channelSpan);
                    int spanEnd3 = getText().getSpanEnd(channelSpan);
                    if (spanStart3 >= 0 && spanEnd3 >= 0 && spanStart3 <= charSequence.length()) {
                        if (spanEnd3 <= charSequence.length()) {
                            CharSequence subSequence2 = charSequence.subSequence(spanStart3, spanEnd3);
                            if (spanEnd3 >= i && spanStart3 <= i && !StringUtil.isEmptyOrNull(channelSpan.label) && !subSequence2.toString().contains(channelSpan.label)) {
                                if (z) {
                                    getText().delete(getText().getSpanStart(channelSpan), getText().getSpanEnd(channelSpan));
                                    return;
                                } else if (z2) {
                                    getText().removeSpan(channelSpan);
                                    ForegroundColorSpan[] foregroundColorSpanArr2 = (ForegroundColorSpan[]) getText().getSpans(0, getText().length(), ForegroundColorSpan.class);
                                    if (foregroundColorSpanArr2 != null && foregroundColorSpanArr2.length > 0) {
                                        for (int length4 = foregroundColorSpanArr2.length - 1; length4 >= 0; length4--) {
                                            ForegroundColorSpan foregroundColorSpan2 = foregroundColorSpanArr2[length4];
                                            int spanStart4 = getText().getSpanStart(foregroundColorSpan2);
                                            int spanEnd4 = getText().getSpanEnd(foregroundColorSpan2);
                                            if (spanStart3 == spanStart4 && spanEnd3 == spanEnd4) {
                                                getText().removeSpan(foregroundColorSpan2);
                                                invalidate();
                                                return;
                                            }
                                        }
                                        return;
                                    }
                                    return;
                                } else {
                                    return;
                                }
                            }
                        }
                    }
                }
            } catch (Exception unused) {
            }
        }
    }
}
