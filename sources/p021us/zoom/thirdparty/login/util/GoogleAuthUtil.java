package p021us.zoom.thirdparty.login.util;

import com.google.android.gms.common.Scopes;
import com.google.api.services.drive.DriveScopes;

/* renamed from: us.zoom.thirdparty.login.util.GoogleAuthUtil */
public class GoogleAuthUtil {
    public static final String[] GOOGLE_DRIVE_SCOPES = {"https://www.googleapis.com/auth/drive", "https://www.googleapis.com/auth/drive.file", DriveScopes.DRIVE_READONLY};
    public static final String[] SCOPES = {Scopes.PROFILE, "email", "https://www.googleapis.com/auth/drive", "https://www.googleapis.com/auth/drive.file", DriveScopes.DRIVE_READONLY};
}
