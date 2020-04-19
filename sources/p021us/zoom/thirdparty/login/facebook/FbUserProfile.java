package p021us.zoom.thirdparty.login.facebook;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import p021us.zoom.androidlib.cache.IoUtils;
import p021us.zoom.androidlib.util.CompatUtils;

/* renamed from: us.zoom.thirdparty.login.facebook.FbUserProfile */
public class FbUserProfile implements Parcelable {
    public static final Creator<FbUserProfile> CREATOR = new Creator<FbUserProfile>() {
        public FbUserProfile createFromParcel(Parcel parcel) {
            return new FbUserProfile(parcel);
        }

        public FbUserProfile[] newArray(int i) {
            return new FbUserProfile[i];
        }
    };
    private String avatarUrl;
    private String email;
    private int errorCode;
    private String errorMsg;
    private String errorType;
    private String name;

    public int describeContents() {
        return 0;
    }

    public FbUserProfile() {
    }

    protected FbUserProfile(Parcel parcel) {
        this.name = parcel.readString();
        this.email = parcel.readString();
        this.avatarUrl = parcel.readString();
        this.errorMsg = parcel.readString();
        this.errorType = parcel.readString();
        this.errorCode = parcel.readInt();
    }

    public static FbUserProfile parse(InputStream inputStream) {
        FbUserProfile fbUserProfile = new FbUserProfile();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, CompatUtils.getStardardCharSetUTF8());
        JsonReader jsonReader = new JsonReader(inputStreamReader);
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String nextName = jsonReader.nextName();
                if (nextName.equals("email")) {
                    fbUserProfile.email = jsonReader.nextString();
                } else if (nextName.equals("name")) {
                    fbUserProfile.name = jsonReader.nextString();
                } else if (nextName.equals("picture")) {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        if (jsonReader.nextName().equals("data")) {
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                if (jsonReader.nextName().equals("url")) {
                                    fbUserProfile.avatarUrl = jsonReader.nextString();
                                } else {
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                } else if (nextName.equals("error")) {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String nextName2 = jsonReader.nextName();
                        if (nextName2.equals("message")) {
                            fbUserProfile.errorMsg = jsonReader.nextString();
                        } else if (nextName2.equals("type")) {
                            fbUserProfile.errorType = jsonReader.nextString();
                        } else if (nextName2.equals("code")) {
                            fbUserProfile.errorCode = jsonReader.nextInt();
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            return fbUserProfile;
        } catch (IOException unused) {
            return fbUserProfile;
        } finally {
            IoUtils.closeSilently(jsonReader);
            IoUtils.closeSilently(inputStreamReader);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public String getErrorType() {
        return this.errorType;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.email);
        parcel.writeString(this.avatarUrl);
        parcel.writeString(this.errorMsg);
        parcel.writeString(this.errorType);
        parcel.writeInt(this.errorCode);
    }
}
