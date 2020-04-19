package com.zipow.videobox.ptapp.p013mm;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.IMProtos.ButtonParam;
import com.zipow.videobox.ptapp.IMProtos.EditParam;
import com.zipow.videobox.ptapp.IMProtos.FieldsEditParam;
import com.zipow.videobox.ptapp.IMProtos.RobotMsg;
import com.zipow.videobox.ptapp.IMProtos.SelectItem;
import com.zipow.videobox.ptapp.IMProtos.SelectParam;
import com.zipow.videobox.ptapp.IMProtos.SelectParam.Builder;
import com.zipow.videobox.ptapp.ZoomMessageTemplateUI;
import com.zipow.videobox.tempbean.IMessageTemplateSelectItem;
import java.util.List;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomMessageTemplate */
public class ZoomMessageTemplate {
    private static final String TAG = "ZoomMessageTemplate";
    private long mNativeHandle;

    private native boolean isOnlyVisibleToYouImpl(long j, String str, String str2);

    private native boolean isSupportItemImpl(long j, String str, int i);

    private native void registerCommonAppUICallbackImpl(long j, long j2);

    @Nullable
    private native byte[] robotDecodeImpl(long j, String str, String str2);

    private native boolean sendButtonCommandImpl(long j, byte[] bArr);

    private native boolean sendEditCommandImpl(long j, byte[] bArr);

    private native boolean sendFieldsEditCommandImpl(long j, byte[] bArr);

    private native boolean sendSelectCommandImpl(long j, byte[] bArr);

    private native boolean updateMessageBodyByJsonImpl(long j, String str, String str2, String str3);

    public ZoomMessageTemplate(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public RobotMsg robotDecode(String str, String str2) {
        RobotMsg robotMsg = null;
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        byte[] robotDecodeImpl = robotDecodeImpl(this.mNativeHandle, str, str2);
        if (robotDecodeImpl != null) {
            try {
                robotMsg = RobotMsg.parseFrom(robotDecodeImpl);
            } catch (InvalidProtocolBufferException unused) {
                return null;
            }
        }
        return robotMsg;
    }

    public boolean updateMessageBodyByJson(String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        return updateMessageBodyByJsonImpl(this.mNativeHandle, str, str2, str3);
    }

    public boolean sendSelectCommand(String str, String str2, String str3, @Nullable List<IMessageTemplateSelectItem> list) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        Builder newBuilder = SelectParam.newBuilder();
        if (TextUtils.isEmpty(str3)) {
            str3 = "";
        }
        newBuilder.setEventId(str3);
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        newBuilder.setMessageId(str2);
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        newBuilder.setSessionId(str);
        if (list != null && !list.isEmpty()) {
            for (IMessageTemplateSelectItem iMessageTemplateSelectItem : list) {
                SelectItem.Builder newBuilder2 = SelectItem.newBuilder();
                newBuilder2.setText(iMessageTemplateSelectItem.getText() == null ? "" : iMessageTemplateSelectItem.getText());
                newBuilder2.setValue(iMessageTemplateSelectItem.getValue() == null ? "" : iMessageTemplateSelectItem.getValue());
                newBuilder.addSelectedItems(newBuilder2.build());
            }
        }
        return sendSelectCommandImpl(this.mNativeHandle, newBuilder.build().toByteArray());
    }

    public boolean sendEditCommand(String str, String str2, String str3, String str4, String str5) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        EditParam.Builder newBuilder = EditParam.newBuilder();
        if (TextUtils.isEmpty(str3)) {
            str3 = "";
        }
        newBuilder.setEventId(str3);
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        newBuilder.setMessageId(str2);
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        newBuilder.setSessionId(str);
        if (TextUtils.isEmpty(str4)) {
            str4 = "";
        }
        newBuilder.setValueOld(str4);
        if (TextUtils.isEmpty(str5)) {
            str5 = "";
        }
        newBuilder.setValueNew(str5);
        return sendEditCommandImpl(this.mNativeHandle, newBuilder.build().toByteArray());
    }

    public boolean sendFieldsEditCommand(String str, String str2, String str3, String str4, String str5, String str6) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        FieldsEditParam.Builder newBuilder = FieldsEditParam.newBuilder();
        if (TextUtils.isEmpty(str3)) {
            str3 = "";
        }
        newBuilder.setEventId(str3);
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        newBuilder.setMessageId(str2);
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        newBuilder.setSessionId(str);
        if (TextUtils.isEmpty(str4)) {
            str4 = "";
        }
        newBuilder.setKey(str4);
        if (TextUtils.isEmpty(str5)) {
            str5 = "";
        }
        newBuilder.setValueOld(str5);
        if (TextUtils.isEmpty(str6)) {
            str6 = "";
        }
        newBuilder.setValueNew(str6);
        return sendFieldsEditCommandImpl(this.mNativeHandle, newBuilder.build().toByteArray());
    }

    public boolean sendButtonCommand(String str, String str2, String str3, String str4, String str5) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        ButtonParam.Builder newBuilder = ButtonParam.newBuilder();
        if (TextUtils.isEmpty(str3)) {
            str3 = "";
        }
        newBuilder.setEventId(str3);
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        newBuilder.setMessageId(str2);
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        newBuilder.setSessionId(str);
        if (TextUtils.isEmpty(str4)) {
            str4 = "";
        }
        newBuilder.setText(str4);
        if (TextUtils.isEmpty(str5)) {
            str5 = "";
        }
        newBuilder.setValue(str5);
        return sendButtonCommandImpl(this.mNativeHandle, newBuilder.build().toByteArray());
    }

    public boolean isSupportItem(String str, int i) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str)) {
            return isSupportItemImpl(this.mNativeHandle, str, i);
        }
        return false;
    }

    public boolean isOnlyVisibleToYou(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return isOnlyVisibleToYouImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public void registerCommonAppUICallback(@Nullable ZoomMessageTemplateUI zoomMessageTemplateUI) {
        long j = this.mNativeHandle;
        if (j != 0 && zoomMessageTemplateUI != null) {
            registerCommonAppUICallbackImpl(j, zoomMessageTemplateUI.getNativeHandle());
        }
    }
}
