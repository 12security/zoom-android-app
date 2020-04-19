package p021us.zoom.thirdparty.googledrive;

import com.google.api.services.drive.model.File;
import p021us.zoom.androidlib.util.AndroidAppUtil;

/* renamed from: us.zoom.thirdparty.googledrive.GoogleDriveUtil */
public class GoogleDriveUtil {
    public static final String ROOT_ID = "root";

    public static int getFileType(File file) {
        if (file == null) {
            return -1;
        }
        return AndroidAppUtil.getFileTypeFromMimType(file.getMimeType());
    }

    public static boolean isFolder(File file) {
        return getFileType(file) == 100;
    }
}
