package com.zipow.videobox.confapp.meeting;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AvailableDialinCountry;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AvailableDialinCountry.Builder;
import com.zipow.videobox.util.ZMUtils;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class AudioOptionParcelItem implements Parcelable {
    public static final Creator<AudioOptionParcelItem> CREATOR = new Creator<AudioOptionParcelItem>() {
        public AudioOptionParcelItem createFromParcel(@NonNull Parcel parcel) {
            return new AudioOptionParcelItem(parcel);
        }

        public AudioOptionParcelItem[] newArray(int i) {
            return new AudioOptionParcelItem[i];
        }
    };
    @Nullable
    private String hash;
    private boolean isIncludeTollFree;
    @Nullable
    private List<String> mAllDialInCountries;
    private int mSelectedAudioType = 2;
    @Nullable
    private List<String> mSelectedDialInCountries;

    public int describeContents() {
        return 0;
    }

    public boolean isCanEditCountry() {
        int i = this.mSelectedAudioType;
        return i == 2 || i == 1;
    }

    public void setHash(String str) {
        this.hash = StringUtil.safeString(str);
    }

    public int getmSelectedAudioType() {
        return this.mSelectedAudioType;
    }

    public void setmSelectedAudioType(int i) {
        this.mSelectedAudioType = i;
    }

    public boolean isIncludeTollFree() {
        return this.isIncludeTollFree;
    }

    public void setIncludeTollFree(boolean z) {
        this.isIncludeTollFree = z;
    }

    @Nullable
    public String getmSelectedDialInCountryDesc(@Nullable Context context) {
        if (context != null) {
            List<String> list = this.mSelectedDialInCountries;
            if (list != null && !list.isEmpty()) {
                int size = this.mSelectedDialInCountries.size();
                if (size == 1) {
                    return ZMUtils.getCountryName((String) this.mSelectedDialInCountries.get(0));
                }
                if (size == 2) {
                    return context.getString(C4558R.string.zm_desc_two_countries_19247, new Object[]{ZMUtils.getCountryName((String) this.mSelectedDialInCountries.get(0)), ZMUtils.getCountryName((String) this.mSelectedDialInCountries.get(1))});
                }
                return context.getString(C4558R.string.zm_desc_more_than_three_countries_19247, new Object[]{ZMUtils.getCountryName((String) this.mSelectedDialInCountries.get(0)), Integer.valueOf(size - 1)});
            }
        }
        return null;
    }

    @Nullable
    public List<String> getmShowSelectedDialInCountries() {
        if (isCanEditCountry()) {
            return this.mSelectedDialInCountries;
        }
        return null;
    }

    public void setmSelectedDialInCountries(@Nullable List<String> list) {
        this.mSelectedDialInCountries = list;
    }

    @Nullable
    public List<String> getmAllDialInCountries() {
        return this.mAllDialInCountries;
    }

    public void setmAllDialInCountries(@Nullable List<String> list) {
        this.mAllDialInCountries = list;
    }

    public AudioOptionParcelItem() {
    }

    @NonNull
    public AvailableDialinCountry getAvailableDialinCountry() {
        Builder newBuilder = AvailableDialinCountry.newBuilder();
        if (this.mAllDialInCountries == null) {
            this.mAllDialInCountries = new ArrayList();
        }
        if (this.mSelectedDialInCountries == null) {
            this.mSelectedDialInCountries = new ArrayList();
        }
        newBuilder.setIncludedTollfree(this.isIncludeTollFree).setHash(StringUtil.safeString(this.hash)).addAllSelectedCountries(this.mSelectedDialInCountries);
        return newBuilder.build();
    }

    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(this.mSelectedAudioType);
        parcel.writeByte(this.isIncludeTollFree ? (byte) 1 : 0);
        parcel.writeStringList(this.mSelectedDialInCountries);
        parcel.writeStringList(this.mAllDialInCountries);
        parcel.writeString(this.hash);
    }

    protected AudioOptionParcelItem(Parcel parcel) {
        this.mSelectedAudioType = parcel.readInt();
        this.isIncludeTollFree = parcel.readByte() != 0;
        this.mSelectedDialInCountries = parcel.createStringArrayList();
        this.mAllDialInCountries = parcel.createStringArrayList();
        this.hash = parcel.readString();
    }
}
