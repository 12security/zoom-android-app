package p021us.zoom.thirdparty.onedrive;

import com.onedrive.sdk.extensions.Item;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMFileListEntry;
import p021us.zoom.androidlib.util.TimeUtil;

/* renamed from: us.zoom.thirdparty.onedrive.OneDriveEntry */
public class OneDriveEntry extends ZMFileListEntry {
    private static final String TAG = "OneDriveEntry";
    private static final DateFormat gDateFormat = new SimpleDateFormat(TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z, Locale.US);
    private Item mOwner;
    private Item mParent;

    public OneDriveEntry(Item item, Item item2) {
        this.mOwner = item;
        this.mParent = item2;
        Item item3 = this.mOwner;
        if (item3 != null) {
            setPath(item3.getPath());
            setDisplayName(this.mOwner.getShowName());
            Calendar calendar = this.mOwner.lastModifiedDateTime;
            if (calendar == null) {
                calendar = this.mOwner.createdDateTime;
            }
            if (calendar != null) {
                setDate(calendar.getTime());
            }
            if (this.mOwner.folder != null) {
                setDir(true);
            } else {
                setDir(false);
                setBytes(getSize());
            }
        }
    }

    public static Date parseDate(String str) {
        try {
            return gDateFormat.parse(str);
        } catch (ParseException unused) {
            return null;
        }
    }

    private long getSize() {
        Item item = this.mOwner;
        if (item == null || item.size == null) {
            return 0;
        }
        return this.mOwner.size.longValue();
    }

    public Item getObject() {
        return this.mOwner;
    }

    public Item getParentObject() {
        return this.mParent;
    }
}
