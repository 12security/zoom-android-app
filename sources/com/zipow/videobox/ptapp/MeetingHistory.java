package com.zipow.videobox.ptapp;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.PTAppProtos.MeetingParticipant;

public class MeetingHistory {
    private static final String TAG = "com.zipow.videobox.ptapp.MeetingHistory";
    private long mNativeHandle = 0;

    private native int getCallTypeImpl(long j);

    private native int getDurationInMinutesImpl(long j);

    private native long getItemIDImpl(long j);

    private native long getJoinedTimeImpl(long j);

    private native long getMeetingNumberImpl(long j);

    @Nullable
    private native String getMeetingTopicImpl(long j);

    private native int getParticipantCountImpl(long j);

    @Nullable
    private native byte[] getParticipantDataAtIndexImpl(long j, int i);

    public MeetingHistory(long j) {
        this.mNativeHandle = j;
    }

    public long getMeetingNumber() {
        return getMeetingNumberImpl(this.mNativeHandle);
    }

    @Nullable
    public String getMeetingTopic() {
        return getMeetingTopicImpl(this.mNativeHandle);
    }

    public long getJoinedTime() {
        return getJoinedTimeImpl(this.mNativeHandle) * 1000;
    }

    public int getDurationInMinutes() {
        return getDurationInMinutesImpl(this.mNativeHandle);
    }

    public int getParticipantCount() {
        return getParticipantCountImpl(this.mNativeHandle);
    }

    @Nullable
    public MeetingParticipant getParticipantAtIndex(int i) {
        byte[] participantDataAtIndexImpl = getParticipantDataAtIndexImpl(this.mNativeHandle, i);
        MeetingParticipant meetingParticipant = null;
        if (participantDataAtIndexImpl == null) {
            return null;
        }
        try {
            meetingParticipant = MeetingParticipant.parseFrom(participantDataAtIndexImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return meetingParticipant;
    }

    public long getItemID() {
        return getItemIDImpl(this.mNativeHandle);
    }

    public int getCallType() {
        return getCallTypeImpl(this.mNativeHandle);
    }
}
