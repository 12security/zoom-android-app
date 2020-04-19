package p021us.zoom.thirdparty.box;

import java.util.Date;
import p021us.zoom.androidlib.app.ZMFileListEntry;

/* renamed from: us.zoom.thirdparty.box.BoxFileListEntry */
public class BoxFileListEntry extends ZMFileListEntry {
    private BoxFileObject mEntry;

    public BoxFileListEntry(BoxFileObject boxFileObject, BoxFileObject boxFileObject2) {
        this.mEntry = boxFileObject;
        BoxFileObject boxFileObject3 = this.mEntry;
        if (boxFileObject3 != null) {
            setPath(boxFileObject3.getPath());
            setDisplayName(this.mEntry.getName());
            Date lastModifiedDate = this.mEntry.getLastModifiedDate();
            if (lastModifiedDate == null) {
                setDate(0);
            } else {
                setDate(lastModifiedDate);
            }
            setDir(this.mEntry.isDir());
            setBytes(this.mEntry.getSize());
        }
    }

    public BoxFileObject getObject() {
        return this.mEntry;
    }
}
