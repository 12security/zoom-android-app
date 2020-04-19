package com.zipow.videobox.view.sip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.zipow.videobox.sip.server.CmmSIPAudioFileItemBean;
import com.zipow.videobox.sip.server.CmmSIPCallHistoryItemBean;
import com.zipow.videobox.sip.server.CmmSIPVoiceMailItemBean;
import com.zipow.videobox.util.ZMPhoneUtils;

public class PBXCallHistory implements Parcelable {
    public static final Creator<PBXCallHistory> CREATOR = new Creator<PBXCallHistory>() {
        public PBXCallHistory createFromParcel(Parcel parcel) {
            return new PBXCallHistory(parcel);
        }

        public PBXCallHistory[] newArray(int i) {
            return new PBXCallHistory[i];
        }
    };
    public CmmSIPAudioFileItemBean audioFile;
    public long createTime;
    public String displayName;
    private String displayNameAndNumber;
    public String displayNumber;
    public boolean highLight;

    /* renamed from: id */
    public String f349id;
    public boolean isAll;
    public boolean isInbound;
    public boolean isRestricted;
    public String name;
    public String number;

    public int describeContents() {
        return 0;
    }

    public PBXCallHistory(@NonNull CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean) {
        this.f349id = cmmSIPCallHistoryItemBean.getId();
        this.isAll = true;
        this.highLight = cmmSIPCallHistoryItemBean.isMissedCall();
        this.isInbound = cmmSIPCallHistoryItemBean.isInBound();
        this.createTime = cmmSIPCallHistoryItemBean.getCreateTime();
        this.audioFile = cmmSIPCallHistoryItemBean.getRecordingAudioFile();
        if (this.isInbound) {
            this.name = cmmSIPCallHistoryItemBean.getFromUserName();
            this.number = cmmSIPCallHistoryItemBean.getFromPhoneNumber();
        } else {
            this.name = cmmSIPCallHistoryItemBean.getToUserName();
            this.number = cmmSIPCallHistoryItemBean.getToPhoneNumber();
        }
        this.displayNumber = cmmSIPCallHistoryItemBean.getDisplayPhoneNumber();
        this.displayName = cmmSIPCallHistoryItemBean.getDisplayName();
        this.isRestricted = cmmSIPCallHistoryItemBean.isRestricted();
    }

    public PBXCallHistory(@NonNull CmmSIPVoiceMailItemBean cmmSIPVoiceMailItemBean) {
        this.f349id = cmmSIPVoiceMailItemBean.getId();
        this.isAll = false;
        this.highLight = cmmSIPVoiceMailItemBean.isUnread();
        this.isInbound = true;
        this.createTime = cmmSIPVoiceMailItemBean.getCreateTime();
        if (cmmSIPVoiceMailItemBean.getAudioFileList() != null && cmmSIPVoiceMailItemBean.getAudioFileList().size() > 0) {
            this.audioFile = (CmmSIPAudioFileItemBean) cmmSIPVoiceMailItemBean.getAudioFileList().get(0);
        }
        this.name = cmmSIPVoiceMailItemBean.getFromUserName();
        this.number = cmmSIPVoiceMailItemBean.getFromPhoneNumber();
        this.displayNumber = cmmSIPVoiceMailItemBean.getDisplayPhoneNumber();
        this.displayName = cmmSIPVoiceMailItemBean.getDisplayName();
        this.isRestricted = cmmSIPVoiceMailItemBean.isRestricted();
    }

    protected PBXCallHistory(Parcel parcel) {
        this.f349id = parcel.readString();
        this.createTime = parcel.readLong();
        boolean z = true;
        this.highLight = parcel.readByte() != 0;
        this.isInbound = parcel.readByte() != 0;
        this.name = parcel.readString();
        this.number = parcel.readString();
        this.isAll = parcel.readByte() != 0;
        this.displayName = parcel.readString();
        this.displayNumber = parcel.readString();
        if (parcel.readByte() == 0) {
            z = false;
        }
        this.isRestricted = z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f349id);
        parcel.writeLong(this.createTime);
        parcel.writeByte(this.highLight ? (byte) 1 : 0);
        parcel.writeByte(this.isInbound ? (byte) 1 : 0);
        parcel.writeString(this.name);
        parcel.writeString(this.number);
        parcel.writeByte(this.isAll ? (byte) 1 : 0);
        parcel.writeString(this.displayName);
        parcel.writeString(this.displayNumber);
        parcel.writeByte(this.isRestricted ? (byte) 1 : 0);
    }

    public String getDisplayNameAndNumber() {
        String str;
        if (TextUtils.isEmpty(this.displayNameAndNumber)) {
            String str2 = this.displayName;
            if (PhoneNumberUtils.isGlobalPhoneNumber(str2) || ZMPhoneUtils.isE164Format(str2)) {
                str = ZMPhoneUtils.formatPhoneNumber(this.displayNumber);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(OAuth.SCOPE_DELIMITER);
                sb.append(ZMPhoneUtils.formatPhoneNumber(this.displayNumber));
                str = sb.toString();
            }
            this.displayNameAndNumber = str;
        }
        return this.displayNameAndNumber;
    }
}
